package com.future.getd.ui.bean;

public class StepData {
    long time;
    int totalSteps = 0;
    float totalCalories = 0;
    float totalDistance = 0;
    int MontTotalSteps = 0;
    int avgMonSteps = 0;

    public StepData() {
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getTotalSteps() {
        return totalSteps;
    }

    public void setTotalSteps(int totalSteps) {
        this.totalSteps = totalSteps;
    }

    public float getTotalCalories() {
        return totalCalories;
    }

    public void setTotalCalories(float totalCalories) {
        this.totalCalories = totalCalories;
    }

    public float getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(float totalDistance) {
        this.totalDistance = totalDistance;
    }

    public int getMontTotalSteps() {
        return MontTotalSteps;
    }

    public void setMontTotalSteps(int montTotalSteps) {
        MontTotalSteps = montTotalSteps;
    }

    public int getAvgMonSteps() {
        return avgMonSteps;
    }

    public void setAvgMonSteps(int avgMonSteps) {
        this.avgMonSteps = avgMonSteps;
    }


    @Override
    public String toString() {
        return "StepData{" +
                "time=" + time +
                ", totalSteps=" + totalSteps +
                ", totalCalories=" + totalCalories +
                ", totalDistance=" + totalDistance +
                ", MontTotalSteps=" + MontTotalSteps +
                ", avgMonSteps=" + avgMonSteps +
                '}';
    }
}
