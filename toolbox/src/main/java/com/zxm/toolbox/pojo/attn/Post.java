package com.zxm.toolbox.pojo.attn;

import java.util.Objects;

public class Post {

    private String postName;
    private String clockInTime;
    private String clockOutTime;
    private double manHour;
    private boolean isDuty;

    public Post() {
        super();
    }

    public Post(String postName) {
        super();
        this.postName = postName;
        clockInTime = null;
        clockOutTime = null;

    }

    public Post(String postName, String clockInTime, String clockOutTime) {
        super();
        this.postName = postName;
        this.clockInTime = clockInTime;
        this.clockOutTime = clockOutTime;

    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getClockInTime() {
        return clockInTime;
    }

    public void setClockInTime(String clockInTime) {
        this.clockInTime = clockInTime;
    }

    public String getClockOutTime() {
        return clockOutTime;
    }

    public void setClockOutTime(String clockOutTime) {
        this.clockOutTime = clockOutTime;
    }

    public double getManHour() {
        return manHour;
    }

    public void setManHour(double manHour) {
        this.manHour = manHour;
    }

    public boolean getIsDuty() {
        return isDuty;
    }

    public void setIsDuty(boolean isDuty) {
        this.isDuty = isDuty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return isDuty == post.isDuty
                && Objects.equals(postName, post.postName)
                && Objects.equals(clockInTime, post.clockInTime)
                && Objects.equals(clockOutTime, post.clockOutTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postName, clockInTime, clockOutTime, isDuty);
    }

    @Override
    public String toString() {
        return "Post [postName=" + postName + ", clockInTime=" + clockInTime + ", clockOutTime=" + clockOutTime
                + ", manHour=" + manHour + ", isDuty=" + isDuty + "]";
    }

}
