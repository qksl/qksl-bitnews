package io.bitnews.admin.beanmapper;

import io.bitnews.model.internal.UserVo;
import io.bitnews.model.passport.po.TUser;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Created by ywd on 2020/4/27.
 */
@Mapper
public abstract class UserBeanMapper {

    public static final UserBeanMapper INSTANCE = Mappers.getMapper(UserBeanMapper.class);

    public abstract UserVo toUserVo(TUser tUser);


}
