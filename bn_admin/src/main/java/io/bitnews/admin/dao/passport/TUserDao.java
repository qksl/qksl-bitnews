package io.bitnews.admin.dao.passport;

import io.bitnews.model.passport.po.TUser;
import org.beetl.sql.core.annotatoin.Sql;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

@Repository
public interface TUserDao extends BaseMapper<TUser> {

    @Sql(value="select * from t_user where email=?")
    TUser findByEmailAll(String email);

    @Sql(value="select * from t_user where username=?")
    TUser findByUsername(String username);

    @Sql(value="select * from t_user where phone_number=?")
    TUser findByMobile(String mobile);

    void selectByPage(PageQuery<TUser> query);

    @Sql(value = "SELECT COUNT(*) FROM t_user WHERE TO_DAYS(create_time) = TO_DAYS(NOW())")
    Long newIncUser();

    @Sql(value = "SELECT COUNT(*) FROM t_user WHERE TO_DAYS(last_login_time) = TO_DAYS(NOW())")
    Long newActiviUser();

    @Sql(value = "SELECT COUNT(*) FROM t_user")
    Long calUser();

    @Sql(value = "UPDATE t_user SET type=1 WHERE id=?;")
    void influencer(long parseLong);
}