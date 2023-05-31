package com.mygdx.game.playStateActivities;

public class ActionTime {
    protected float actionTime;
    protected float stateTime;
    protected float trainingTime;

    public ActionTime() {
        actionTime = 0;
        stateTime = 0;
        trainingTime = 0;
    }

    public float getActionTime() {
        return actionTime;
    }

    public void setActionTime(float time) {
        actionTime  = time;
    }

    public void plusStateTime(float time) {
        this.stateTime += time;
    }
    public float getStateTime(){
        return stateTime;
    }
    public void setTrainingTime(float time){
        this.trainingTime = time;
    }
    public float getTrainingTime(){
        return trainingTime;
    }
}
