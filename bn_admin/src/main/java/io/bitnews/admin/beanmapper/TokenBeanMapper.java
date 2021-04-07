package io.bitnews.admin.beanmapper;

import io.bitnews.model.external.UserTokenHistory;
import io.bitnews.model.internal.UserVo;
import io.bitnews.model.news.po.TTokenHistory;
import io.bitnews.model.passport.po.TUser;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Created by ywd on 2020/4/27.
 */
@Mapper
public abstract class TokenBeanMapper {

    public static final TokenBeanMapper INSTANCE = Mappers.getMapper(TokenBeanMapper.class);

    public abstract UserTokenHistory toUserTokenHistory(TTokenHistory tTokenHistory);


}
