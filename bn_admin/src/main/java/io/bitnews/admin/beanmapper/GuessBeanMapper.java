package io.bitnews.admin.beanmapper;

import io.bitnews.model.external.GuessInfo;
import io.bitnews.model.internal.DiscussionVo;
import io.bitnews.model.news.po.TDiscussion;
import io.bitnews.model.news.po.TPromoterTopic;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Created by ywd on 2020/4/27.
 */
@Mapper
public abstract class GuessBeanMapper {

    public static final GuessBeanMapper INSTANCE = Mappers.getMapper(GuessBeanMapper.class);

    public abstract GuessInfo toGuessInfo(TPromoterTopic tPromoterTopic);



}
