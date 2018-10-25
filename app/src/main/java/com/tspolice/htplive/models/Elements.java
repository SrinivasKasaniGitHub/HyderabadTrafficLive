package com.tspolice.htplive.models;

public class Elements {
    private Duration duration;

    private Distance distance;

    private String status;

    private Duration_in_traffic duration_in_traffic;

    public Duration getDuration ()
    {
        return duration;
    }

    public void setDuration (Duration duration)
    {
        this.duration = duration;
    }

    public Distance getDistance ()
    {
        return distance;
    }

    public void setDistance (Distance distance)
    {
        this.distance = distance;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public Duration_in_traffic getDuration_in_traffic ()
    {
        return duration_in_traffic;
    }

    public void setDuration_in_traffic (Duration_in_traffic duration_in_traffic)
    {
        this.duration_in_traffic = duration_in_traffic;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [duration = "+duration+", distance = "+distance+", status = "+status+", duration_in_traffic = "+duration_in_traffic+"]";
    }
}
