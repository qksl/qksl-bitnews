package io.bitnews.model.internal;

import lombok.Data;

import java.util.Date;

/**
 * Created by ywd on 2019/9/27.
 */
@Data
public class GuessTriggerVo {

    /**
     * job id对应的是topicid
     */
    String jobId;

    /**
     * 封盘调度时间
     */
    Date closeTriggerTime;

    /**
     * 结算调度时间
     */
    Date settlementTriggerTime;
}
