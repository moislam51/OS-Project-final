package services;

import models.Process;
import models.Result;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import utils.Metrics;

public class RoundRobinService {

    public Result run(List<Process> processes, int quantum) {

        List<Process> processList = new ArrayList<>(processes);
        processList.sort(Comparator.comparingInt(Process::getArrivalTime));

        Queue<Process> readyQueue = new LinkedList<>();
        List<String> ganttChart = new ArrayList<>();
        List<Process> completedProcesses = new ArrayList<>();

        int currentTime = 0;
        int index = 0;
        int n = processList.size();

        while (completedProcesses.size() < n) {

            while (index < n && processList.get(index).getArrivalTime() <= currentTime) {
                readyQueue.add(processList.get(index));
                index++;
            } // push all valid processes (time-wise)

            if (readyQueue.isEmpty()) {
                currentTime = processList.get(index).getArrivalTime();
                continue;
            }// to skip uneccesary time

            Process current = readyQueue.peek();
            readyQueue.remove();

            if (current.getStartTime() == -1) {
                current.setStartTime(currentTime);
            } // set current if not started yet

            int startTime = currentTime;
            int executionTime = Math.min(quantum, current.getRemainingTime());

            current.setRemainingTime(current.getRemainingTime() - executionTime);
            currentTime += executionTime;

            ganttChart.add(current.getId() + " [" + startTime + " - " + currentTime + "]");

            while (index < n && processList.get(index).getArrivalTime() <= currentTime) {
                readyQueue.add(processList.get(index));
                index++;
            }

            if (current.getRemainingTime() > 0) {
                readyQueue.add(current);
            } else {
                current.setCompletionTime(currentTime);

                int turnaroundTime = current.getCompletionTime() - current.getArrivalTime();
                int waitingTime = turnaroundTime - current.getBurstTime();
                int responseTime = current.getStartTime() - current.getArrivalTime();

                current.setTurnaroundTime(turnaroundTime);
                current.setWaitingTime(waitingTime);
                current.setResponseTime(responseTime);

                completedProcesses.add(current);
            }
        }

        Result result = Metrics.calculate(completedProcesses);
        result.ganttChart = ganttChart;
        return result;
    }
}