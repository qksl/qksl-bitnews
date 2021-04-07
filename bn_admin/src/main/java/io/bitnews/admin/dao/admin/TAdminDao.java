package io.bitnews.admin.dao.admin;

import io.bitnews.model.admin.po.TAdmin;
import org.beetl.sql.core.annotatoin.Param;
import org.beetl.sql.core.annotatoin.Sql;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TAdminDao extends BaseMapper<TAdmin> {

    @Sql(value="select * from t_admin where username=?")
    TAdmin findByUsername(String username);

    List<TAdmin> selectByPage(@Param("sort") String sort, @Param("order") String order,@Param("start") int start, @Param("offset") int offset);

    void queryPage(PageQuery<TAdmin> query);
}