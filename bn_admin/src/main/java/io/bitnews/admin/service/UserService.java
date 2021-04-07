package io.bitnews.admin.service;

import io.bienews.common.helper.PageUtil;
import io.bienews.common.helper.StringUtil;
import io.bitnews.admin.beanmapper.UserBeanMapper;
import io.bitnews.admin.dao.passport.TUserDao;
import io.bitnews.common.constants.CommonConstant;
import io.bitnews.common.model.BNPageResponse;
import io.bitnews.model.internal.UserDataVo;
import io.bitnews.model.internal.UserVo;
import io.bitnews.model.news.po.TToken;
import io.bitnews.model.passport.po.TUser;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Created by ywd on 2019/7/23.
 */
@Service
public class UserService {

    @Autowired
    private TUserDao userDao;

    @Autowired
    private TokenService tokenService;

    public void modifyUserStatus(String userId, String status) {
        TUser tUser = new TUser();
        tUser.setStatus(status);
        userDao.createLambdaQuery()
                .andEq(TUser::getId, userId)
                .updateSelective(tUser);
    }

    public BNPageResponse<UserVo> userList(Integer pageNum, Integer pageSize, String condition) {
        Query<TUser> query = userDao.createLambdaQuery();
        if (StringUtil.isNotEmpty(condition)) {
            query.andEq("id", condition);
        }
        PageQuery<TUser> queryPage = query.orderBy("create_time desc")
                .page(pageNum, pageSize);
        return PageUtil.createPage(queryPage).map(UserBeanMapper.INSTANCE::toUserVo).map(c -> {
            TToken tToken = tokenService.queryAmount(c.getId());
            BigDecimal token = tToken != null?tToken.getToken():BigDecimal.ZERO;
            c.setToken(token);
            return c;
        });
    }

    public UserDataVo show() {
        UserDataVo userDataVo = new UserDataVo();
        Long calUser = userDao.calUser();
        Long incUser = userDao.newIncUser();
        Long activiUser = userDao.newActiviUser();
        userDataVo.setActiveUser(activiUser);
        userDataVo.setNewIncUser(incUser);
        userDataVo.setNowUser(calUser);
        return userDataVo;
    }

    public void authInfluencer(String id) {
        TUser tUser = userDao.single(id);
        if (CommonConstant.USER_TYPE_BIGV.equals(tUser.getType())) {
            tUser.setType(CommonConstant.USER_TYPE_COMMON);
        } else {
            tUser.setType(CommonConstant.USER_TYPE_BIGV);
        }
        userDao.updateById(tUser);
    }
}
