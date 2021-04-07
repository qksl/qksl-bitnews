package io.bitnews.oauth.dao;

import io.bitnews.model.passport.po.TUser;
import org.beetl.sql.core.annotatoin.Sql;
import org.beetl.sql.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

@Repository
public interface TUserDao extends BaseMapper<TUser> {
    //
    @Sql(value="select * from t_user where email=?")
    TUser findByEmail(String username);

    @Sql(value="select * from t_user where username=?")
    TUser findByname(String username);

    @Sql(value="select * from t_user where phone_number=?")
    TUser findByMobile(String phoneNumber);

}