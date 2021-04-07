package io.bitnews.passport.service;

import io.bitnews.client.news.NewsServiceClient;
import io.bitnews.common.model.BaseResponse;
import io.bitnews.model.internal.PostVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ywd on 2019/7/23.
 */
@Service
public class CommentService {

    @Autowired
    private NewsServiceClient newsServiceClient;


    public BaseResponse issueComment(PostVo postVo) {
        return newsServiceClient.issue(postVo);
    }


    public BaseResponse liked(String postId, String userId) {
        return newsServiceClient.liked(postId, userId);
    }

    public BaseResponse cancelLike(String postId, String userId) {
        return newsServiceClient.cancelLike(postId, userId);
    }
}
