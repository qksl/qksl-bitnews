package io.bitnews.admin.service;

import io.bienews.common.helper.PageUtil;
import io.bienews.common.helper.StringUtil;
import io.bitnews.admin.beanmapper.NewsBeanMapper;
import io.bitnews.admin.beanmapper.UserBeanMapper;
import io.bitnews.admin.dao.news.TDiscussionDao;
import io.bitnews.admin.dao.news.TPostDao;
import io.bitnews.client.news.NewsServiceClient;
import io.bitnews.common.model.BNPageResponse;
import io.bitnews.common.model.BaseResponse;
import io.bitnews.model.external.News;
import io.bitnews.model.internal.DiscussionVo;
import io.bitnews.model.news.po.TDiscussion;
import io.bitnews.model.news.po.TPost;
import io.bitnews.model.passport.po.TUser;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ywd on 2019/7/23.
 */
@Service
public class NewsService {

    @Autowired
    private TDiscussionDao discussionDao;

    @Autowired
    private TPostDao tPostDao;

    private final static String NEW_LASTE = "0";

    public void createDiscussion(TDiscussion tDiscussion) {
        discussionDao.insert(tDiscussion);
    }

    public void update(Long id, TDiscussion tDiscussion) {
        discussionDao.createLambdaQuery().andEq(TDiscussion::getId, id)
                .updateSelective(tDiscussion);
    }

    public void issueDiscussion(Long id) {
        discussionDao.issue(id);
    }

    @Transactional
    public void delDiscussion(Long id) {
        discussionDao.deleteById(id);
        tPostDao.createLambdaQuery().andEq(TPost::getDiscussionId, id).delete();
    }

    public void cancelDiscussion(Long id) {
        discussionDao.cancel(id);
    }

    public BNPageResponse<DiscussionVo> listDiscussion(String tag, Integer pageNum, Integer pageSize) {
        Query<TDiscussion> query = discussionDao.createLambdaQuery();
        if (!NEW_LASTE.equals(tag)) {
            query.andEq("tag", tag);
        }
        PageQuery<TDiscussion> queryPage = query.orderBy("create_time desc")
                .page(pageNum, pageSize);
        return PageUtil.createPage(queryPage).map(NewsBeanMapper.INSTANCE::toDiscussionVo);
    }

    public List<DiscussionVo> queryEvent(String start, String end) {
        return discussionDao.createLambdaQuery()
                .andBetween(TDiscussion::getEventTime, start, end)
                .andEq(TDiscussion::getEventFlag, 1)
                .orderBy("hot_num desc")
                .select("*", "bull_count+bear_count as hot_num")
                .stream()
                .map(NewsBeanMapper.INSTANCE::toDiscussionVo)
                .collect(Collectors.toList());
    }
}
