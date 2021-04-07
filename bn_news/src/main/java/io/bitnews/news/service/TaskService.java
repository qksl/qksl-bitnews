package io.bitnews.news.service;

import io.bitnews.common.constants.CommonConstant;
import io.bitnews.common.constants.UserSdkErrorCode;
import io.bitnews.common.model.BaseResponse;
import io.bitnews.model.external.TaskCenterVo;
import io.bitnews.model.external.TaskExhibition;
import io.bitnews.model.external.TaskVo;
import io.bitnews.model.news.po.TTasks;
import io.bitnews.model.news.po.TTasksComplete;
import io.bitnews.model.news.po.TToken;
import io.bitnews.news.dao.TTasksCompleteDao;
import io.bitnews.news.dao.TTasksDao;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by ywd on 2019/12/16.
 */
@Service
@Slf4j
@CacheConfig(cacheNames = "task")
public class TaskService {

    @Autowired
    private TTasksDao tTasksDao;

    @Autowired
    private TTasksCompleteDao tTasksCompleteDao;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PromoterService promoterService;

    private static final String TASK_MSG = "任务奖励: ";


    /**
     * 签到
     *
     * @param userId
     * @return
     */
    public BaseResponse signIn(String userId) {
        TTasks task = queryTaskByMark(CommonConstant.TASK_MARK_NAME_DAILY_SIGNIN);
        String taskId = task.getId().toString();
        TTasksComplete signTask = queryDailyTasks(taskId, userId);
        if (null != signTask) {
            return new BaseResponse(UserSdkErrorCode.TASK_BAD_REQUEST_REWARD);
        }
        TTasksComplete tTasksComplete = new TTasksComplete();
        tTasksComplete.setTaskId(Long.parseLong(taskId));
        tTasksComplete.setUserId(Long.parseLong(userId));
        tTasksCompleteDao.insert(tTasksComplete);
        //签到成功发放积分
        tokenService.newAdd(Long.parseLong(userId), BigDecimal.valueOf(task.getReward()), task.getDesc());
        return new BaseResponse();
    }

    public TaskCenterVo taskCenter(String userId) {
        List<TaskExhibition> dailyTasks = dailyTasks(userId);
        List<TaskExhibition> growUpTasks = growUpTasks(userId);
        List<TaskExhibition> operateTasks = operateTasks(userId);
        TaskCenterVo taskCenterVo = new TaskCenterVo();
        taskCenterVo.setDailyTasks(dailyTasks);
        taskCenterVo.setGrowUpTasks(growUpTasks);
        taskCenterVo.setOperateTasks(operateTasks);
        return taskCenterVo;
    }

    public List<TaskExhibition> dailyTasks(String userId) {
        List<TTasks> tTasks = tTasksDao.queryTasks(CommonConstant.TASK_TYPE_DAILY);
        return convert(tTasks, userId);
    }

    public List<TaskExhibition> growUpTasks(String userId) {
        List<TTasks> growUpTask = tTasksDao.queryTasks(CommonConstant.TASK_TYPE_GROW_UP);
        return convert(growUpTask, userId);
    }

    public List<TaskExhibition> operateTasks(String userId) {
        List<TTasks> operateTasks = tTasksDao.queryTasks(CommonConstant.TASK_TYPE_OPERATE);
        return convert(operateTasks, userId);
    }

    public void saveTask(TaskVo taskVo) {
        TTasks tTasks = new TTasks();
        BeanUtils.copyProperties(taskVo, tTasks);
        tTasksDao.insert(tTasks);
    }

    public void deleteTask(String taskId) {
        List<TTasksComplete> tTasksCompletes = tTasksCompleteDao.queryByTaskId(taskId);
        for (TTasksComplete tasksComplete : tTasksCompletes) {
            tTasksCompleteDao.deleteById(tasksComplete.getId());
        }
        tTasksDao.deleteById(taskId);
    }

    public TTasks findTask(String id) {
        return tTasksDao.single(id);
    }

    public TTasks queryTaskByMark(String mark) {
        return tTasksDao.queryTaskByMark(mark);
    }

    public TTasksComplete queryDailyTasks(String taskId, String userId) {
        return tTasksCompleteDao.queryDailyTasks(taskId, userId);
    }


    public TaskVo queryById(String id) {
        TTasks tTasks = tTasksDao.single(id);
        if (null == tTasks) {
            return null;
        }
        TaskVo taskVo = new TaskVo();

        BeanUtils.copyProperties(tTasks, taskVo);
        return taskVo;
    }

    public BaseResponse receiveAwards(String taskId, String userId) {
        TTasks task = findTask(taskId);
        if (CommonConstant.TASK_MARK_NAME_FIRST_LOGIN.equals(task.getUniqueMark())) {
            award(taskId, userId);
            return new BaseResponse();
        }
        int status = taskStatus(task, userId);
        if (status != CommonConstant.TASK_STATUS_YES ) {
            return new BaseResponse(UserSdkErrorCode.TASK_BAD_REQUEST_REWARD);
        }
        award(taskId, userId);
        return new BaseResponse();
    }

    public void award(String taskId, String userId) {
        TTasks task = findTask(taskId);
        //发积分
        tokenService.newAdd(Long.parseLong(userId), BigDecimal.valueOf(task.getReward()), TASK_MSG + task.getDesc());
        TTasksComplete tTasksComplete = new TTasksComplete();
        tTasksComplete.setUserId(Long.parseLong(userId));
        tTasksComplete.setTaskId(Long.parseLong(taskId));
        //保存记录
        tTasksCompleteDao.insert(tTasksComplete);
    }

    public int taskStatus(TTasks tasks, String userId) {
        int rs = 0;
        switch (tasks.getUniqueMark()) {
            case CommonConstant.TASK_MARK_NAME_FIRST_LOGIN:
                rs = CommonConstant.TASK_STATUS_REWARD;
                break;
            case CommonConstant.TASK_MARK_NAME_DAILY_SIGNIN:
                TTasksComplete signin = queryDailyTasks(tasks.getId().toString(), userId);
                if (null != signin) {
                    rs = CommonConstant.TASK_STATUS_REWARD;
                }else {
                    rs = CommonConstant.TASK_STATUS_YES;
                }
                break;
            case CommonConstant.TASK_MARK_NAME_DAILY_GUESS:
                TTasksComplete guess = queryDailyTasks(tasks.getId().toString(), userId);
                if (null != guess) {
                    rs = CommonConstant.TASK_STATUS_REWARD;
                } else {
                    boolean isJoinGuess = promoterService.userIsJoinGuessToday(userId);
                    if (isJoinGuess) {
                        rs = CommonConstant.TASK_STATUS_YES;
                    }
                }
                break;
            default:
                log.error("请检查任务标识：" + tasks.getUniqueMark());

        }
        return rs;
    }

    private List<TaskExhibition> convert(List<TTasks> tTasks, String userId) {
        List<TaskExhibition> collect = tTasks.stream().map(task -> {
            TaskExhibition taskExhibition = new TaskExhibition();
            BeanUtils.copyProperties(task, taskExhibition);
            int status = taskStatus(task, userId);
            taskExhibition.setStatus(status);
            return taskExhibition;
        }).collect(Collectors.toList());
        return collect;
    }

    public void firstLogin(String userId) {
        TToken tToken = tokenService.findByUserId(Long.parseLong(userId));
        if (null == tToken) {
            tokenService.createTokenAccount(userId);
        }
        TTasks tTasks = queryTaskByMark(CommonConstant.TASK_MARK_NAME_FIRST_LOGIN);
        receiveAwards(tTasks.getId().toString(), userId);
    }
}
