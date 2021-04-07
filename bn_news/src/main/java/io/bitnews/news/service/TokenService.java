package io.bitnews.news.service;

import io.bienews.common.helper.PageUtil;
import io.bienews.common.helper.exception.CommonException;
import io.bitnews.common.model.BNPageResponse;
import io.bitnews.model.external.UserTokenHistory;
import io.bitnews.model.news.po.TToken;
import io.bitnews.model.news.po.TTokenHistory;
import io.bitnews.news.dao.TTokenDao;
import io.bitnews.news.dao.TTokenHistoryDao;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ywd on 2019/8/26.
 */
@Slf4j
@Service
@CacheConfig(cacheNames = "banner")
public class TokenService {

    @Autowired
    private TTokenDao tokenDao;

    @Autowired
    private TTokenHistoryDao tokenHistoryDao;

    /**
     * 新增token
     * @param userId
     * @param amount
     * @param reason
     */
    @CacheEvict(value={"token"}, key = "#userId",beforeInvocation = true)
    public void newAdd(Long userId, BigDecimal amount, String reason){
        tokenDao.addToken(amount,userId);
        //记录日志历史
        TTokenHistory tokenHistory = new TTokenHistory();
        tokenHistory.setUserId(userId);
        tokenHistory.setToken(amount);
        tokenHistory.setReason(reason);
        tokenHistoryDao.insert(tokenHistory);
    }

    /**
     * 消费token
     * @param userId
     * @param amount
     * @param reason
     */
    @CacheEvict(value={"token"}, key = "#userId",beforeInvocation = true)
    public void consume(Long userId, BigDecimal amount, String reason){
        TToken tToken = tokenDao.findByUserId(userId);
        if (tToken.getToken().compareTo(amount) == -1) {
            throw new CommonException("拥有token不够");
        }
        tokenDao.subToken(amount, userId);
        //记录日志历史
        TTokenHistory tokenHistory = new TTokenHistory();
        tokenHistory.setUserId(userId);
        tokenHistory.setToken(amount.negate());
        tokenHistory.setReason(reason);
        tokenHistoryDao.insert(tokenHistory);
    }

    /**
     * 查询token
     * @param userId
     * @return
     */
    @Cacheable(value = "token", key = "#userId", unless="#result == null")
    public BigDecimal queryTokenAmount(Long userId) {
        TToken tToken = tokenDao.findByUserId(userId);
        if (null == tToken) {
            return null;
        }
        return tToken.getToken();
    }

    public TToken findByUserId(long userId) {
        return tokenDao.findByUserId(userId);
    }

    public void createTokenAccount(String userId) {
        TToken tToken = new TToken();
        tToken.setUserId(Long.parseLong(userId));
        tToken.setToken(BigDecimal.ZERO);
        tokenDao.insert(tToken);
    }

    public BNPageResponse<UserTokenHistory> queryTokenAmountHistory(PageQuery<TTokenHistory> query, String cacheKey) {
        tokenHistoryDao.queryPage(query);
        List<TTokenHistory> list = query.getList();
        List<UserTokenHistory> userTokenHistories = list.stream().map(tTokenHistory -> {
            UserTokenHistory userTokenHistory = new UserTokenHistory();
            BeanUtils.copyProperties(tTokenHistory, userTokenHistory);
            return userTokenHistory;
        }).collect(Collectors.toList());
        return PageUtil.createPage(userTokenHistories,
                query.getPageNumber(), query.getPageSize(), query.getTotalPage(), query.getTotalRow());
    }
}
