package io.bitnews.admin.service;

import io.bienews.common.helper.PageUtil;
import io.bitnews.admin.beanmapper.TokenBeanMapper;
import io.bitnews.admin.dao.news.TTokenDao;
import io.bitnews.admin.dao.news.TTokenHistoryDao;
import io.bitnews.common.constants.CommonConstant;
import io.bitnews.common.model.BNPageResponse;
import io.bitnews.model.external.UserTokenHistory;
import io.bitnews.model.news.po.TToken;
import io.bitnews.model.news.po.TTokenHistory;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Created by ywd on 2019/8/28.
 */
@Service
public class TokenService {

    @Autowired
    private TTokenDao tokenDao;
    @Autowired
    private TTokenHistoryDao tokenHistoryDao;

    @Transactional
    public void grant(Long userId, BigDecimal amount) {
        tokenDao.addToken(amount,userId);
        addTokenHistory(userId, amount, CommonConstant.ADMIN_GRANT, "系统发放");
    }

    public TToken queryAmount(Long userId) {
        return tokenDao.findByUserId(userId);
    }

    public BNPageResponse<UserTokenHistory> amountHistory(Long userId, Integer pageNum, Integer pageSize) {
        Query<TTokenHistory> query = tokenHistoryDao.createLambdaQuery()
                .andEq(TTokenHistory::getUserId, userId);
        PageQuery<TTokenHistory> queryPage = query.orderBy("create_time desc")
                .page(pageNum, pageSize);
        return PageUtil.createPage(queryPage).map(TokenBeanMapper.INSTANCE::toUserTokenHistory);
    }

    public void newAdd(Long userId, BigDecimal amount, String joinIncome, String reason) {
        tokenDao.addToken(amount,userId);
        addTokenHistory(userId, amount, joinIncome, reason);
    }

    private void addTokenHistory(Long userId, BigDecimal amount, String type, String reason) {
        TTokenHistory tokenHistory = new TTokenHistory();
        tokenHistory.setUserId(userId);
        tokenHistory.setToken(amount);
        tokenHistory.setType(type);
        tokenHistory.setReason(reason);
        tokenHistoryDao.insert(tokenHistory);
    }
}
