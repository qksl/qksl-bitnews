package io.bitnews.passport.service;

import com.alibaba.fastjson.JSONObject;
import io.bitnews.client.news.NewsServiceClient;
import io.bitnews.common.constants.RedisConstant;
import io.bitnews.common.model.BNPageResponse;
import io.bitnews.common.model.BNResponse;
import io.bitnews.framework.redis.RedisManager;
import io.bitnews.model.external.DistributionInfo;
import io.bitnews.model.external.MarketInfo;
import io.bitnews.model.external.News;
import io.bitnews.model.internal.*;
import io.bitnews.model.passport.po.TUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ywd on 2019/7/23.
 */
@Service
public class NewService {

    @Autowired
    private NewsServiceClient newsServiceClient;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisManager redisManager;

    public BNPageResponse<News> queryLatestNews(String tag, String pageNum, String pageSize) {
        BNPageResponse<News> response = newsServiceClient.latestNews(tag, pageNum, pageSize);
        if (response.isToast()) {
            List<News> list = response.getList();
            List<News> collect = list.stream().map(news -> {
                Long upCommontUserId = news.getUpCommontUserId();
                if (upCommontUserId != null) {
                    TUser tUser = userService.findById(upCommontUserId.toString());
                    news.setUpCommontUsername(tUser.getNickName());
                    news.setUpCommontUserImage(tUser.getTxPicture());
                }
                return news;
            }).collect(Collectors.toList());
            response.setList(collect);
        }
        return response;
    }

    public BNPageResponse<DiscussionVo> queryEvent(String start, String end) {
        return newsServiceClient.event(start, end);
    }

    public BNPageResponse<BannerVo> queryBanner(String type) {
        return newsServiceClient.list(type);
    }

    public BNPageResponse<PostVo> queryCommentPage(String discussionId, String type, String status, String userId, String pageNum, String pageSize) {
        BNPageResponse<PostVo> response = newsServiceClient.list(discussionId, type, status, userId, pageNum, pageSize);
        if (response.isToast()) {
            List<PostVo> list = response.getList();
            for (PostVo postVo : list) {
                TUser user = userService.findById(postVo.getUserId().toString());
                if (null != user) {
                    postVo.setUserName(user.getNickName());
                    postVo.setTxPicture(user.getTxPicture());
                }
            }
            response.setList(list);
        }
        return response;
    }

    public BNResponse<PostVo> queryCommentMaxLike(String discussionId) {
        BNResponse<PostVo> response = newsServiceClient.maxLike(discussionId);
        if (response.isToast()) {
            PostVo postVo = response.getBody();
            if (null != postVo) {
                TUser user = userService.findById(postVo.getUserId().toString());
                if (null != user) {
                    postVo.setUserName(user.getNickName());
                    postVo.setTxPicture(user.getTxPicture());
                }
                response.setBody(postVo);
            }
        }
        return response;
    }

    public BNPageResponse<CoinMarketVo> queryLatestQuotes(String coinCode, String start, String end) {
        return newsServiceClient.latestQuotes(coinCode, start, end);
    }

    public BNResponse<DetailsDiscussionVo> queryDetails(String id) {
        DetailsDiscussionVo body = newsServiceClient.details(id).getBody();
        if(null != body){
            List<PostVo> posts = body.getPosts();
            List<PostVo> postNew = posts.stream().map(postVo -> {
                TUser tUser = userService.findById(postVo.getUserId().toString());
                postVo.setUserName(tUser.getNickName());
                postVo.setTxPicture(tUser.getTxPicture());
                return postVo;
            }).collect(Collectors.toList());
            body.setPosts(postNew);
        }
        return new BNResponse<>(body);
    }

    public BNResponse<MarketInfo> getMarketInfo() {
        Object object = redisManager.get(RedisConstant.MARKET_CAP_INFO);
        if (null != object) {
            MarketInfo marketInfo = (MarketInfo) object;
            return new BNResponse<>(marketInfo);
        }
        return newsServiceClient.marketInfo();
    }

    public BNResponse<DistributionInfo> getMarketDistribution() {
        Object object = redisManager.get(RedisConstant.MARKET_DISTRIBUTION_INFO);
        if (null != object) {
            DistributionInfo distributionInfo = (DistributionInfo) object;
            return new BNResponse<>(distributionInfo);
        }
        return newsServiceClient.marketDistribution();
    }
}
