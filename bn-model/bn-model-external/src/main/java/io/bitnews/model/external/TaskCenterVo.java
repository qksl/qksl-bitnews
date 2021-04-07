package io.bitnews.model.external;

import lombok.Data;

import java.util.List;

/**
 * Created by ywd on 2019/12/13.
 */
@Data
public class TaskCenterVo {

    private List<TaskExhibition> dailyTasks;
    private List<TaskExhibition> growUpTasks;
    private List<TaskExhibition> operateTasks;
}
