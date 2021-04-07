package io.bitnews.news.service;

import com.google.common.base.Throwables;
import io.bienews.common.helper.BigDecimalUtil;
import io.bienews.common.helper.PageUtil;
import io.bienews.common.helper.exception.CommonException;
import io.bitnews.common.constants.CommonConstant;
import io.bitnews.common.model.BNPageResponse;
import io.bitnews.common.model.BaseResponse;
import io.bitnews.model.external.GuessInfo;
import io.bitnews.model.internal.*;
import io.bitnews.model.news.po.TBets;
import io.bitnews.model.news.po.TPromoter;
import io.bitnews.model.news.po.TPromoterTopic;
import io.bitnews.model.news.po.TTokenHistory;
import io.bitnews.news.dao.*;
import io.bitnews.news.job.GuessCloseJob;
import io.bitnews.news.job.GuessSettlementJob;
import io.bitnews.news.job.SchedulerManager;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.db.KeyHolder;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ywd on 2019/7/30.
 */
@Service
@Slf4j
@CacheConfig(cacheNames = "guess")
public class PromoterService {

    @Autowired
    private TPromoterTopicDao tPromoterTopicDao;

    @Autowired
    private TPromoterDao tPromoterDao;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private BetsService betsService;

    @Autowired
    private DiscussionService discussionService;

    private static final String WINNER = "1";

    private static final String LOSER = "2";

    private static final String GROUP_CLOSE = "GUESS_CLOSE";
    private static final String GROUP_SETTLEMENT = "GUESS_SETTLEMENT";
    private static final String JOB_CLOSE = "JOB_CLOSE_";
    private static final String JOB_SETTLEMENT = "JOB_SETTLEMENT_";

    @Autowired
    SchedulerManager schedulerManager;

    /**
     * 发起一个竞猜
     *
     * @param promoterTopicVo
     */
    @Transactional(rollbackFor = Exception.class)
    public void insertTopic(PromoterTopicVo promoterTopicVo) throws Exception {
        long createUserId = promoterTopicVo.getCreateUserId();
        TPromoterTopic po = new TPromoterTopic();
        BeanUtils.copyProperties(promoterTopicVo, po);
        po.setStatus(CommonConstant.GUESS_STATUS);
        po.setGuessGold(BigDecimal.valueOf(Double.parseDouble(promoterTopicVo.getGuessGold())));
        KeyHolder keyHolder = tPromoterTopicDao.insertReturnKey(po);
        TBets tBets = new TBets();
        tBets.setPromoterTopicId(keyHolder.getLong());
        tBets.setBetsGold(promoterTopicVo.getBetsGold());
        tBets.setUserId(createUserId);
        tBets.setIncome(BigDecimal.ZERO);
        //如果是类型1：自动收益类型
        if (CommonConstant.GUESS_TYPE_ONE.equals(po.getType())) {
            tBets.setOdds(BigDecimal.ZERO);
            //第一种都是支持
            tBets.setBets(CommonConstant.GUESS_YES);
        }else {
            //第二种情况就要考虑，是支持还是反对
            tBets.setBets(promoterTopicVo.getGuessWinner());
            BigDecimal odds = BigDecimal.ONE.divide(promoterTopicVo.getOdds(), 2, BigDecimal.ROUND_FLOOR);
            tBets.setOdds(odds);
        }
        betsService.insert(tBets);
        //扣除抵押的底金
        tokenService.consume(createUserId, promoterTopicVo.getBetsGold(), "竞猜抵押底金");
        String jobId = Long.valueOf(keyHolder.getLong()).toString();
        GuessTriggerVo guessTriggerVo = new GuessTriggerVo();
        guessTriggerVo.setJobId(jobId);
        guessTriggerVo.setCloseTriggerTime(po.getPermitTime());
        guessTriggerVo.setSettlementTriggerTime(po.getSettlementTime());
        startJob(guessTriggerVo);
    }

    public void startJob(GuessTriggerVo guessTriggerVo) throws Exception{
        String jobId = guessTriggerVo.getJobId();
        //设置封盘job
        schedulerManager.startJobWithDate(guessTriggerVo.getCloseTriggerTime(), JOB_CLOSE+jobId, GROUP_CLOSE+jobId, GuessCloseJob.class);
        //设置结算job
        schedulerManager.startJobWithDate(guessTriggerVo.getSettlementTriggerTime(), JOB_SETTLEMENT+jobId, GROUP_SETTLEMENT+jobId, GuessSettlementJob.class);
    }

    /**
     * 发起对赌协议
     *
     * @param promoterVo
     */
    public void insertPromoter(PromoterVo promoterVo) {
        TPromoter po = new TPromoter();
        BeanUtils.copyProperties(promoterVo, po);
        try {
            tPromoterDao.insert(po);
        }catch (Throwable t) {
            log.error("异常点", t);
            throw new CommonException(t.getMessage());
        }
    }

    /**
     * 竞猜状态封盘
     *
     * @param promoterTopicId
     */
    @Transactional
    public void closePromoter(String promoterTopicId) {
        long id = Long.parseLong(promoterTopicId);
        //停止开猜和下注
        TPromoterTopic tPromoterTopic = tPromoterTopicDao.unique(id);
        if(!CommonConstant.GUESS_STATUS.equals(tPromoterTopic.getStatus())) {
            throw new CommonException("该竞猜不为开盘状态，不能执行封盘操作");
        }
        tPromoterTopicDao.close(id);
        //判断竞猜的类型，如果是类型1：自动收益类型
        //如果是类型1：自动收益类型
        if (CommonConstant.GUESS_TYPE_ONE.equals(tPromoterTopic.getType())) {
            BigDecimal sumByYes = betsService.sumByYes(id);
            BigDecimal sumByNo = betsService.sumByNo(id);
            //如果有一方为0则本次流猜，返回代币
            if (BigDecimalUtil.isZero(sumByYes) || BigDecimalUtil.isZero(sumByNo)) {
                BigDecimal betsGold = betsService.queryBetsGold(tPromoterTopic.getCreateUserId(), tPromoterTopic.getId());
                log.info(String.format("本次为流局,返回%s代币: %s", tPromoterTopic.getCreateUserId(), betsGold.toString()));
                tokenService.newAdd(tPromoterTopic.getCreateUserId(), betsGold, "竞猜抵押底金返还");
                //标记为流局
                tPromoterTopicDao.abandon(id);
            } else {
                //计算本次的最终赔率
                PromoterTopicChangeVo pcv = new PromoterTopicChangeVo(sumByYes, sumByNo);
                //更新赔率
                List<TBets> noBets = betsService.findNoByPromoterTopicId(id);
                List<TBets> yesBets = betsService.findYesByPromoterTopicId(id);
                for (TBets nobet : noBets) {
                    nobet.setOdds(pcv.getNoOdds());
                    betsService.updateById(nobet);
                }
                for (TBets yesbet : yesBets) {
                    yesbet.setOdds(pcv.getYesOdds());
                    betsService.updateById(yesbet);
                }
            }
        } else {
            //如果是类型2：发起对赌
            //未被买入的开猜，标记为流局，返回代币
            //获取开猜人的对立面
            String oppose = tPromoterTopic.getWinner().equals(CommonConstant.GUESS_YES)?"1":"0";
            BigDecimal opposeBigDecimal = betsService.findByPromoterTopicIdAndBets(tPromoterTopic.getId(), oppose);
            BigDecimal reback = BigDecimal.ZERO;
            if (opposeBigDecimal == null || opposeBigDecimal.compareTo(BigDecimal.ZERO) == 0) {
                //流猜
                tPromoterTopicDao.abandon(tPromoterTopic.getId());
            } else{
                reback = tPromoterTopic.getBetsGold().subtract(opposeBigDecimal.multiply(tPromoterTopic.getOdds()));
                tPromoterTopicDao.close(tPromoterTopic.getId());
            }
            //返回资金
            tokenService.newAdd(tPromoterTopic.getCreateUserId(), reback, "竞猜抵押底金返还");
        }

    }

    /**
     * 发起结算
     */
    @Transactional
    public void settlement(String promoterTopicId, String winner) {
        TPromoterTopic po = tPromoterTopicDao.unique(promoterTopicId);
        if(!CommonConstant.GUESS_STATUS_CLOSE.equals(po.getStatus())) {
            throw new CommonException("该竞猜不为封盘状态，不能执行结算操作");
        }
        po.setStatus(CommonConstant.PROMOTER_SETTLEMENT);
        tPromoterTopicDao.updateById(po);
        String lose = winner == CommonConstant.GUESS_YES?CommonConstant.GUESS_NO:CommonConstant.GUESS_YES;
        //胜利者
        List<TBets> winnerList = betsService.findListByPromoterTopicIdAndBets(po.getId(), winner);
        //失败者
        List<TBets> loserList = betsService.findListByPromoterTopicIdAndBets(po.getId(), lose);
        //如果是类型1：自动收益类型
        if (CommonConstant.GUESS_TYPE_ONE.equals(po.getType())) {
            for (TBets ws : winnerList) {
                setBetsWiner(ws, winner);
            }
            for (TBets ls : loserList) {
                setBetsLoser(ls, winner);
            }
        } else {

            //判断发起竞猜者是否正确
            if(po.getWinner().equals(winner)) {
                //发起者获胜
                for (TBets ls : loserList) {
                    setBetsLoser(ls, winner);
                }
                //获胜收益
                BigDecimal loseTotal = betsService.findByPromoterTopicIdAndBets(po.getId(), lose);
                TBets ws = winnerList.get(0);
                ws.setStatus(WINNER);
                ws.setWinner(winner);
                ws.setIncome(loseTotal);
                tokenService.newAdd(ws.getUserId(), loseTotal, "竞猜收益");
                betsService.updateById(ws);
            }
        }

    }

    private void setBetsLoser(TBets ls, String winner) {
        ls.setStatus(LOSER);
        ls.setWinner(winner);
        ls.setIncome(ls.getBetsGold().negate());
        betsService.updateById(ls);
    }

    private void setBetsWiner(TBets ws, String winner) {
        ws.setStatus(WINNER);
        ws.setWinner(winner);
        BigDecimal in = ws.getBetsGold().multiply(ws.getOdds()).add(ws.getBetsGold());
        ws.setIncome(in);
        tokenService.newAdd(ws.getUserId(), in, "竞猜收益");
        betsService.updateById(ws);
    }

    /**
     * 参加竞猜
     *
     * @param betsVo
     */
    @Transactional
    public void joinBets(BetsVo betsVo, TPromoterTopic tPromoterTopic) {
        TBets tBets = new TBets();
        BeanUtils.copyProperties(betsVo, tBets);
        if (CommonConstant.GUESS_TYPE_ONE.equals(tPromoterTopic.getType())) {
            tBets.setOdds(BigDecimal.ZERO);
        } else {
            //第二种要判断是否有足够的资金
            String oppose = tPromoterTopic.getWinner().equals(CommonConstant.GUESS_YES)?"1":"0";
            BigDecimal sum = betsService.findByPromoterTopicIdAndBets(tPromoterTopic.getId(),
                    oppose);
            sum = sum.add(betsVo.getBetsGold());
            sum = sum.multiply(tBets.getOdds());
            if (tPromoterTopic.getBetsGold().compareTo(sum) == -1) {
                throw new CommonException("资金池数量不足");
            }
            tBets.setOdds(tBets.getOdds());
        }
        tBets.setIncome(BigDecimal.ZERO);
        //扣除抵押的底金
        tokenService.consume(betsVo.getUserId(), betsVo.getBetsGold(), "竞猜投注底金");

        betsService.insert(tBets);
    }

    public TPromoterTopic findByPromoterTopicId(Long promoterTopicId) {
        return tPromoterTopicDao.single(promoterTopicId);
    }

    @Cacheable(value = "guessInfo", key = "#promoterTopicId", unless = "#result==null")
    public GuessInfo findById(Long promoterTopicId) {
        return getGuessInfo(findByPromoterTopicId(promoterTopicId));
    }


    public BNPageResponse<GuessInfo> queryPageByCondition(PageQuery<TPromoterTopic> query) {
        tPromoterTopicDao.latestByPage(query);
        List<TPromoterTopic> tList = query.getList();
        List<GuessInfo> vList = guessInfo(tList);
        return PageUtil.createPage(vList,
                query.getPageNumber(), query.getPageSize(), query.getTotalPage(), query.getTotalRow());
    }

    private List<GuessInfo> guessInfo(List<TPromoterTopic> list) {
        return list.stream().map(pt ->
                getGuessInfo(pt)
        ).collect(Collectors.toList());
    }

    private GuessInfo getGuessInfo(TPromoterTopic pt) {
        GuessInfo pv = new GuessInfo();
        BeanUtils.copyProperties(pt, pv);
        BigDecimal yes,no;
        Integer noNumber,yesNumber;
        //判断类型
        if (CommonConstant.GUESS_TYPE_ONE.equals(pt.getType())) {
            yes = betsService.sumByYes(pt.getId());
            noNumber = betsService.countBetsNoNumber(pt.getId());
            yesNumber = betsService.countBetsYesNumber(pt.getId());
            no = betsService.sumByNo(pt.getId());
            //如果有一方为0，则无赔率
            if (BigDecimalUtil.isZero(yes) || BigDecimalUtil.isZero(no)) {
                pv.setNoOdds(BigDecimal.ZERO);
                pv.setYesOdds(BigDecimal.ZERO);
            } else {
                PromoterTopicChangeVo pcv = new PromoterTopicChangeVo(yes, no);
                pv.setNoOdds(pcv.getNoOdds());
                pv.setYesOdds(pcv.getYesOdds());
            }
            pv.setGuessWinner(pt.getGuessWinner());
        }else {
            //计算自己的收益率
            BigDecimal odds = BigDecimal.ONE.divide(pt.getOdds(), 2, BigDecimal.ROUND_FLOOR);
            //如果是水皮开盘，查看其选择的方向
            if (CommonConstant.GUESS_YES.equals(pt.getWinner())) {
                //支持
                yes = pt.getBetsGold();
                yesNumber = 1;
                noNumber = betsService.countBetsNoNumber(pt.getId());
                no = betsService.sumByNo(pt.getId());
                pv.setNoOdds(pt.getOdds());
                pv.setYesOdds(odds);
            }else {
                no = pt.getBetsGold();
                noNumber = 1;
                yes = betsService.sumByYes(pt.getId());
                yesNumber = betsService.countBetsYesNumber(pt.getId());
                pv.setNoOdds(odds);
                pv.setYesOdds(pt.getOdds());
            }
            pv.setGuessWinner(pt.getWinner());
        }
        String title = discussionService.findTitleById(pt.getDiscussionId());
        pv.setTitle(title);
        pv.setYes(yes);
        pv.setYesNumber(yesNumber);
        pv.setNo(no);
        pv.setNoNumber(noNumber);
        return pv;
    }

    public void settPromoterTopicAbandon(Long id) {
        TPromoterTopic topic = tPromoterTopicDao.single(id);
        if (null == topic) {
            throw new CommonException("找不到该竞猜");
        }
        if (CommonConstant.GUESS_STATUS.equals(topic.getStatus()) || CommonConstant.GUESS_STATUS_CLOSE.equals(topic.getStatus())) {
            List<TBets> promoterTopicIds = betsService.findByPromoterTopicId(id);
            for (TBets tBets : promoterTopicIds) {
                log.info(String.format("删除竞猜%s,返回%s代币: %s", tBets.getPromoterTopicId().toString(),
                        tBets.getUserId().toString(), tBets.getBetsGold().toString()));
                tokenService.newAdd(tBets.getUserId(), tBets.getBetsGold(), "竞猜抵押底金返还");
                betsService.deleteById(tBets.getId());
            }
            topic.setStatus(CommonConstant.GUESS_STATUS_ABANDON);
            tPromoterTopicDao.updateTemplateById(topic);
        }
    }

    public boolean userIsJoinGuessToday(String userId) {
        List<TBets> tBets = betsService.findByUserIdToday(userId);
        if (tBets.size() > 0) {
            return true;
        }
        return false;
    }

    public TPromoter findByPromoterId(Long promoterId) {
        return tPromoterDao.single(promoterId);
    }

    public List<TPromoterTopic> findByDiscussionIdAndUserID(Long discussionId, Long userId) {
        TPromoterTopic param = new TPromoterTopic();
        param.setDiscussionId(discussionId);
        param.setCreateUserId(userId);
        return  tPromoterTopicDao.template(param);
    }

    @Cacheable(value = "isLaunch", key = "#discusionId+#userId")
    public boolean isLaunchByDiscussionIdAndUserID(Long discusionId, Long userId) {
        return findByDiscussionIdAndUserID(discusionId, userId).size() > 0 ?true:false;
    }
}
