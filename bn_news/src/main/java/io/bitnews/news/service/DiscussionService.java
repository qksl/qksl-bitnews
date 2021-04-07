package io.bitnews.news.service;


import io.bienews.common.helper.PageUtil;
import io.bienews.common.helper.TimeUtil;
import io.bitnews.common.model.BNPageResponse;
import io.bitnews.model.external.News;
import io.bitnews.model.internal.DiscussionVo;
import io.bitnews.model.internal.PostVo;
import io.bitnews.model.news.po.TDiscussion;
import io.bitnews.model.news.po.TPost;
import io.bitnews.model.news.po.TPromoterTopic;
import io.bitnews.model.param.DiscussionParam;
import io.bitnews.news.dao.TDiscussionDao;
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
@CacheConfig(cacheNames = "news")
public class DiscussionService {

    @Autowired
    private TDiscussionDao tDiscussionDao;

    @Autowired
    private CommentService commentService;

    @Autowired
    private PromoterService promoterService;

    /**
     * 分页返回
     *
     * @return
     */
    public BNPageResponse<News> queryNewsByPage(PageQuery<TDiscussion> query, String cacheKey) {
        tDiscussionDao.selectByPage(query);
        List<TDiscussion> tList = query.getList();
        List<News> vList = getNews(tList);
        return PageUtil.createPage(vList,
                query.getPageNumber(), query.getPageSize(), query.getTotalPage(), query.getTotalRow());
    }

    public void updateBear(Integer num, Long id) {
        tDiscussionDao.bear(num, id);
    }

    public void updateBull(Integer num, Long id) {
        tDiscussionDao.bull(num, id);
    }

    /**
     * 通过id返回标题
     */
    public String findTitleById(Long id) {
        TDiscussion tDiscussion = findById(id);
        if (null == tDiscussion) {
            return "";
        }
        return tDiscussion.getTitle();
    }

    /**
     * 查询重大事件,查询发布状态
     *
     * @param start
     * @param end
     * @return
     */
    public List<DiscussionVo> queryEventWithStatus(String status, String start, String end) {
        List<TDiscussion> tDiscussions = tDiscussionDao.listEvent(status, start, end);
        return transfrom(tDiscussions);
    }

    private List<News> getNews(List<TDiscussion> tDiscussions) {
        if (tDiscussions == null) {
            return null;
        }
        return tDiscussions.stream().map(po ->
                createNews(po)
        ).collect(Collectors.toList());
    }

    private News createNews(TDiscussion po) {
        News news = new News();
        BeanUtils.copyProperties(po, news);
        news.setCommontNums(news.getBearCount() + news.getBullCount());
        TPost tPost = commentService.maxLiked(po.getId());
        if (null != tPost) {
            news.setUpCommontUserId(tPost.getUserId());
            news.setUpCommont(tPost.getContent());
            String diff = TimeUtil.getDiff(tPost.getUpdateTime());
            news.setUpCommontTime(diff);
            boolean launchFlag = promoterService.isLaunchByDiscussionIdAndUserID(po.getId(),
                    tPost.getUserId());
            news.setLaunchFlag(launchFlag);
        }
        return news;
    }

    private List<DiscussionVo> transfrom(List<TDiscussion> tDiscussions) {
        if (tDiscussions == null) {
            return null;
        }
        return tDiscussions.stream().map(po ->
                transfrom(po)
        ).collect(Collectors.toList());
    }

    private DiscussionVo transfrom(TDiscussion po) {
        DiscussionVo vo = new DiscussionVo();
        BeanUtils.copyProperties(po, vo);
        return vo;
    }


    public TDiscussion findById(Long id) {
        return tDiscussionDao.single(id);
    }

    public DiscussionVo get(Long id) {
        TDiscussion single = findById(id);
        if (null == single) {
            return null;
        }
        return transfrom(single);
    }

    public List<DiscussionVo> queryRelated(Long id, String tag) {
        List<TDiscussion> pos = tDiscussionDao.related(id, tag);
        return transfrom(pos);
    }
}
