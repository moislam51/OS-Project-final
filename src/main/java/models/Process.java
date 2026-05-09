package models;

public class Process {
    //updated fields, getters, setters to be double instead of int
    private String id;
    private int arrivalTime;
    private int priority;
    private int burstTime;

    private int remainingTime;
    private int startTime = -1;
    private int completionTime;
    private double waitingTime;
    private double turnaroundTime; 
    private double responseTime;

    public Process(String id, int arrivalTime, int burstTime, int priority) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.remainingTime = burstTime;
    }

    public String getId() {
        return id;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public int getPriority() {
        return priority;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(int completionTime) {
        this.completionTime = completionTime;
    }

    public double getWaitingTime() {
    return waitingTime;
    }

    public void setWaitingTime(double waitingTime) {
    this.waitingTime = waitingTime;
    }

    public double getTurnaroundTime() {
    return turnaroundTime;
    }

    public void setTurnaroundTime(double turnaroundTime) {
    this.turnaroundTime = turnaroundTime;
}

    public double getResponseTime() {
    return responseTime;
    }

    public void setResponseTime(double responseTime) {
    this.responseTime = responseTime;
    }
}