package io.bitnews.admin.beanmapper;

import io.bitnews.model.internal.BannerVo;
import io.bitnews.model.internal.DiscussionVo;
import io.bitnews.model.internal.PostVo;
import io.bitnews.model.internal.UserVo;
import io.bitnews.model.news.po.TBanner;
import io.bitnews.model.news.po.TDiscussion;
import io.bitnews.model.news.po.TPost;
import io.bitnews.model.passport.po.TUser;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Created by ywd on 2020/4/27.
 */
@Mapper
public abstract class NewsBeanMapper {

    public static final NewsBeanMapper INSTANCE = Mappers.getMapper(NewsBeanMapper.class);

    public abstract TDiscussion toTDiscussion(DiscussionVo discussionVo);

    public abstract DiscussionVo toDiscussionVo(TDiscussion tDiscussion);

    public abstract PostVo toPostVo(TPost tPost);

    public abstract TBanner toTBanner(BannerVo bannerVo);

    public abstract BannerVo toBannerVo(TBanner tBanner);

}
