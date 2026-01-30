package com.xxx.xxl.entity;

import java.io.Serializable;
import java.util.Date;

public class XxlJobLog implements Serializable {
    private Integer id;

    private Integer jobGroup;

    private Integer jobId;

    private String executorAddress;

    private String executorHandler;

    private String executorParam;

    private String executorShardingParam;

    private Integer executorFailRetryCount;

    private Date triggerTime;

    private Integer triggerCode;

    private String triggerMsg;

    private Date handleTime;

    private Integer handleCode;

    private String handleMsg;

    private Integer alarmStatus;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(Integer jobGroup) {
        this.jobGroup = jobGroup;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public String getExecutorAddress() {
        return executorAddress;
    }

    public void setExecutorAddress(String executorAddress) {
        this.executorAddress = executorAddress;
    }

    public String getExecutorHandler() {
        return executorHandler;
    }

    public void setExecutorHandler(String executorHandler) {
        this.executorHandler = executorHandler;
    }

    public String getExecutorParam() {
        return executorParam;
    }

    public void setExecutorParam(String executorParam) {
        this.executorParam = executorParam;
    }

    public String getExecutorShardingParam() {
        return executorShardingParam;
    }

    public void setExecutorShardingParam(String executorShardingParam) {
        this.executorShardingParam = executorShardingParam;
    }

    public Integer getExecutorFailRetryCount() {
        return executorFailRetryCount;
    }

    public void setExecutorFailRetryCount(Integer executorFailRetryCount) {
        this.executorFailRetryCount = executorFailRetryCount;
    }

    public Date getTriggerTime() {
        return triggerTime;
    }

    public void setTriggerTime(Date triggerTime) {
        this.triggerTime = triggerTime;
    }

    public Integer getTriggerCode() {
        return triggerCode;
    }

    public void setTriggerCode(Integer triggerCode) {
        this.triggerCode = triggerCode;
    }

    public String getTriggerMsg() {
        return triggerMsg;
    }

    public void setTriggerMsg(String triggerMsg) {
        this.triggerMsg = triggerMsg;
    }

    public Date getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(Date handleTime) {
        this.handleTime = handleTime;
    }

    public Integer getHandleCode() {
        return handleCode;
    }

    public void setHandleCode(Integer handleCode) {
        this.handleCode = handleCode;
    }

    public String getHandleMsg() {
        return handleMsg;
    }

    public void setHandleMsg(String handleMsg) {
        this.handleMsg = handleMsg;
    }

    public Integer getAlarmStatus() {
        return alarmStatus;
    }

    public void setAlarmStatus(Integer alarmStatus) {
        this.alarmStatus = alarmStatus;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", jobGroup=").append(jobGroup);
        sb.append(", jobId=").append(jobId);
        sb.append(", executorAddress=").append(executorAddress);
        sb.append(", executorHandler=").append(executorHandler);
        sb.append(", executorParam=").append(executorParam);
        sb.append(", executorShardingParam=").append(executorShardingParam);
        sb.append(", executorFailRetryCount=").append(executorFailRetryCount);
        sb.append(", triggerTime=").append(triggerTime);
        sb.append(", triggerCode=").append(triggerCode);
        sb.append(", triggerMsg=").append(triggerMsg);
        sb.append(", handleTime=").append(handleTime);
        sb.append(", handleCode=").append(handleCode);
        sb.append(", handleMsg=").append(handleMsg);
        sb.append(", alarmStatus=").append(alarmStatus);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}