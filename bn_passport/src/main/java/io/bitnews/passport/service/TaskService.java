package io.bitnews.passport.service;

import io.bitnews.client.news.NewsServiceClient;
import io.bitnews.common.constants.RedisConstant;
import io.bitnews.common.model.BNResponse;
import io.bitnews.common.model.BaseResponse;
import io.bitnews.framework.redis.RedisManager;
import io.bitnews.model.external.TaskCenterVo;
import io.bitnews.model.passport.po.TUser;
import io.bitnews.passport.dao.TUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ywd on 2019/12/16.
 */
@Service
public class TaskService {

    @Autowired
    private UserService userService;

    @Autowired
    private NewsServiceClient newsServiceClient;

    @Autowired
    private RedisManager redisManager;

    public BaseResponse signIn(String userId) {
        TUser tUser = userService.findById(userId);
        BaseResponse baseResponse = newsServiceClient.signin(tUser.getId().toString());
//        if (baseResponse.isToast()) {
//            int lastSeconds = TimeUtil.getLastSeconds();
//        }
        return baseResponse;
    }



    private String genKey(String taskId, String name) {
        String key = RedisConstant.DAILY_TASKS_KEY + name + taskId;
        return key;
    }

    public BNResponse<TaskCenterVo> taskCenter(String userId) {
        return newsServiceClient.listTask(userId);
    }

    public BaseResponse receiveAwards(String taskId, String userId) {
        return newsServiceClient.receiveAwardsTask(taskId, userId);
    }

    public BaseResponse firstLogin(String userId) {
        return newsServiceClient.firstLogin(userId);
    }
}
