package com.tspolice.htplive.models;

import com.google.gson.annotations.SerializedName;

public class Elements {

    /*private Duration duration;
    private Distance distance;
    private String status;
    private Duration_in_traffic duration_in_traffic;

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Duration_in_traffic getDuration_in_traffic() {
        return duration_in_traffic;
    }

    public void setDuration_in_traffic(Duration_in_traffic duration_in_traffic) {
        this.duration_in_traffic = duration_in_traffic;
    }

    @Override
    public String toString() {
        return "ClassPojo [duration = " + duration + ", distance = " + distance + ", status = " + status + ", duration_in_traffic = " + duration_in_traffic + "]";
    }*/

    private Distance distance;

    private Duration duration;

    @SerializedName("duration_in_traffic")
    private Duration_in_traffic durationInTraffic;

    private String status;

    public Elements() {
    }

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Duration_in_traffic getDurationInTraffic() {
        return durationInTraffic;
    }

    public void setDurationInTraffic(Duration_in_traffic durationInTraffic) {
        this.durationInTraffic = durationInTraffic;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ClassPojo [distance = " + distance + ", duration = " + duration + ", status = " + status + ", duration_in_traffic = " + durationInTraffic + "]";
    }
}
