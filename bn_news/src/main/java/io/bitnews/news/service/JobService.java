package io.bitnews.news.service;

import io.bitnews.common.constants.CommonConstant;
import io.bitnews.model.external.GuessInfo;
import io.bitnews.news.scheduled.CmcFairValueJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Created by ywd on 2020/5/13.
 */
@Service
@Slf4j
public class JobService {

    @Autowired
    private PromoterService promoterService;

    @Autowired
    private CmcFairValueJob cmcFairValueJob;

    public void closeJob(String id) {
        log.info("竞猜封盘：" + id);
        promoterService.closePromoter(id);
    }

    public GuessInfo verification(String topicId) {
        return promoterService.findById(Long.parseLong(topicId));
    }

    public void settlementJob(String id) throws Exception {
        log.info("竞猜结算：" + id);
        GuessInfo guessInfo = promoterService.findById(Long.parseLong(id));
        BigDecimal priceNow = cmcFairValueJob.getPrice(guessInfo.getTokenType());
        //当前大于猜测金额则表示能结果为0
        log.info(String.format("查询当前时间点%s的价格为%s", guessInfo.getTokenType(), priceNow.toString()));
        String compare = priceNow.compareTo(guessInfo.getGuessGold()) >= 0?"0":"1";//0是大于，1是小于
        String winner;
        if(guessInfo.getGuessWinner().equals(compare)) {
            //如果结果和判断相等则，支持者获胜
            winner = CommonConstant.GUESS_YES;
        } else {
            winner = CommonConstant.GUESS_NO;
        }
        promoterService.settlement(id,winner);
    }
}
