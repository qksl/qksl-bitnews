package io.bitnews.admin.service;

import io.bienews.common.helper.PageUtil;
import io.bitnews.admin.beanmapper.NewsBeanMapper;
import io.bitnews.admin.dao.news.TDiscussionDao;
import io.bitnews.admin.dao.news.TPostDao;
import io.bitnews.admin.model.enums.CommentType;
import io.bitnews.client.news.NewsServiceClient;
import io.bitnews.common.constants.CommonConstant;
import io.bitnews.common.model.BNPageResponse;
import io.bitnews.common.model.BaseResponse;
import io.bitnews.model.internal.PostVo;
import io.bitnews.model.news.po.TDiscussion;
import io.bitnews.model.news.po.TPost;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by ywd on 2019/7/23.
 */
@Service
public class CommentService {

    @Autowired
    private TPostDao postDao;

    @Autowired
    private TDiscussionDao tDiscussionDao;


    public BNPageResponse<PostVo> queryComment(String discussionId, Integer pageNum, Integer pageSize) {
        PageQuery<TPost> queryPage = postDao.createLambdaQuery()
                .andEq(TPost::getDiscussionId, discussionId)
                .orderBy("create_time desc")
                .page(pageNum, pageSize);
        return PageUtil.createPage(queryPage).map(NewsBeanMapper.INSTANCE::toPostVo);
    }


    @Transactional(rollbackFor = Exception.class)
    public void deleteComment(Long postId) {
        TPost single = postDao.single(postId);
        long bull = postDao.createLambdaQuery()
                .andEq(TPost::getDiscussionId, single.getDiscussionId())
                .andEq(TPost::getType,CommentType.Bull)
                .count();
        long bear = postDao.createLambdaQuery()
                .andEq(TPost::getDiscussionId, single.getDiscussionId())
                .andEq(TPost::getType,CommentType.Bear)
                .count();
        TDiscussion td = new TDiscussion();
        td.setBullCount(Integer.parseInt(Long.valueOf(bull).toString()));
        td.setBearCount(Integer.parseInt(Long.valueOf(bear).toString()));
        tDiscussionDao.createLambdaQuery()
                .andEq(TDiscussion::getId, single.getDiscussionId())
                .updateSelective(td);
        postDao.createLambdaQuery().andEq(TPost::getId, postId).delete();
    }
}
