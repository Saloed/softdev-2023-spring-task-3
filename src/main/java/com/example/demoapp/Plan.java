package com.example.demoapp;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Plan {
    private SimpleStringProperty taskType;
    private SimpleStringProperty taskDesc;
    private SimpleIntegerProperty taskTime;

    public Plan(String taskType, String taskDesc, Integer taskTime) {
        this.taskType = new SimpleStringProperty(taskType);
        this.taskDesc = new SimpleStringProperty(taskDesc);
        this.taskTime = new SimpleIntegerProperty(taskTime);
    }

    public String getTaskType() {
        return taskType.get();
    }

    public void setTaskType(String taskType) {
        this.taskType = new SimpleStringProperty(taskType);
    }

    public String getTaskDesc() {
        return taskDesc.get();
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = new SimpleStringProperty(taskDesc);
    }

    public Integer getTaskTime() {
        return taskTime.get();
    }

    public void setTaskTime(Integer taskTime) {
        this.taskTime = new SimpleIntegerProperty(taskTime);
    }
}
