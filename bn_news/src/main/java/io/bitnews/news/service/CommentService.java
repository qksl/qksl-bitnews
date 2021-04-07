package io.bitnews.news.service;



import io.bienews.common.helper.PageUtil;
import io.bitnews.common.constants.CommonConstant;
import io.bitnews.common.model.BNPageResponse;
import io.bitnews.model.internal.PostVo;
import io.bitnews.model.news.po.TLiked;
import io.bitnews.model.news.po.TPost;
import io.bitnews.model.param.DiscussionParam;
import io.bitnews.model.param.PostParam;
import io.bitnews.news.dao.TLikedDao;
import io.bitnews.news.dao.TPostDao;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ywd on 2019/7/12.
 */
@Service
@CacheConfig(cacheNames = "comment")
public class CommentService {

    @Autowired
    private TPostDao tPostDao;

    @Autowired
    private DiscussionService discussionService;

    @Autowired
    private TLikedDao tLikedDao;

    public TPost findById(Long id) {
        return tPostDao.single(id);
    }

    /**
     * 发布一条评论
     * @param vo
     */
    public void issue(PostVo vo) {
        Long discussionId = vo.getDiscussionId();
        TPost po = new TPost();
        BeanUtils.copyProperties(vo, po);
        po.setStatus(CommonConstant.COMMENT_ISSUE);
        tPostDao.insert(po);
        if (vo.getType().equals(CommonConstant.BTC_BULL)){
            discussionService.updateBull(1, discussionId);
        }else if(vo.getType().equals(CommonConstant.BTC_BEAR)) {
            discussionService.updateBear(1, discussionId);
        }
    }

    public boolean liked(PostParam tPost) {
        TLiked byUnique = tLikedDao.findByUnique(tPost.getId(), tPost.getUserId());
        if (byUnique != null){
            return false;
        }
        TLiked tLiked = new TLiked();
        tLiked.setPostId(tPost.getId());
        tLiked.setUserId(tPost.getUserId());
        tLikedDao.insert(tLiked);
        tPostDao.updateLiked(1, tPost.getId());
        return true;
    }

    public void cancelLike(PostParam tPost) {
        tLikedDao.deleteByPostAndUser(tPost.getId(), tPost.getUserId());
        tPostDao.updateLiked(-1, tPost.getId());
    }

    public TPost maxLiked(Long discussionId) {
        return tPostDao.findMostLike(discussionId);
    }

    /**
     * 查询点赞数最多评论
     * @return
     */
    public PostVo queryMaxLiked(String discussionId) {
        TPost tPost = maxLiked(Long.parseLong(discussionId));
        return transfrom(tPost);
    }


    private List<PostVo> transfrom(List<TPost> tPosts) {
        if (tPosts == null) {
            return null;
        }
        return tPosts.stream().map(po ->
             transfrom(po)
        ).collect(Collectors.toList());
    }

    private PostVo transfrom(TPost po) {
        if (po == null) {
            return null;
        }
        PostVo vo = new PostVo();
        BeanUtils.copyProperties(po, vo);

        return vo;
    }

    public List<PostVo> queryHots(Long discussionId) {
        List<TPost> hots = tPostDao.hots(discussionId);
        return transfrom(hots);
    }

    public BNPageResponse<PostVo> queryPageByCondition(PageQuery<TPost> query, String cacheKey) {
        tPostDao.queryByPage(query);
        List<TPost> tList = query.getList();
        List<PostVo> vList = transfrom(tList);
        return PageUtil.createPage(vList,
                query.getPageNumber(), query.getPageSize(), query.getTotalPage(), query.getTotalRow());
    }

}
