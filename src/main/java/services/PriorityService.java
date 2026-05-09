package services;

import models.Process;
import models.Result;
import utils.Metrics;

import java.util.*;

public class PriorityService {

    public Result run(List<Process> processes) {

        List<String> ganttChart = new ArrayList<>();
        List<Process> completedProcesses = new ArrayList<>();

        int currentTime = 0;
        int completedCount = 0; // completed tasks
        int n = processes.size(); // number of tasks

        Process lastProcess = null;
        int segmentStart = 0; 

        while (completedCount < n) {

            Process selected = null; // the process i am currently working on 

            for (Process p : processes) { 
                if (p.getArrivalTime() <= currentTime && p.getRemainingTime() > 0) {
                    if (selected == null ||
                            p.getPriority() < selected.getPriority() ||
                            (p.getPriority() == selected.getPriority()
                                    && p.getArrivalTime() < selected.getArrivalTime()) ||
                            (p.getPriority() == selected.getPriority()
                                    && p.getArrivalTime() == selected.getArrivalTime()
                                    && p.getId().compareTo(selected.getId()) < 0)) {

                        selected = p;
                    }
                }
            }

            if (selected == null) {
                currentTime++;
                continue;
            }

            if (selected.getStartTime() == -1) {
                selected.setStartTime(currentTime);
            }

            if (lastProcess != selected) {
                if (lastProcess != null) {
                    ganttChart.add(lastProcess.getId() + " [" + segmentStart + " - " + currentTime + "]");
                }

                segmentStart = currentTime;
                lastProcess = selected;
            }

            selected.setRemainingTime(selected.getRemainingTime() - 1);
            currentTime++;

            if (selected.getRemainingTime() == 0) {
                selected.setCompletionTime(currentTime);

                int turnaroundTime = selected.getCompletionTime() - selected.getArrivalTime();
                int waitingTime = turnaroundTime - selected.getBurstTime();
                int responseTime = selected.getStartTime() - selected.getArrivalTime();

                selected.setTurnaroundTime(turnaroundTime);
                selected.setWaitingTime(waitingTime);
                selected.setResponseTime(responseTime);

                completedProcesses.add(selected);
                completedCount++;
            }
        }

        if (lastProcess != null) {
            ganttChart.add(lastProcess.getId() + " [" + segmentStart + " - " + currentTime + "]");
        }

        Result result = Metrics.calculate(completedProcesses);
        result.ganttChart = ganttChart;
        return result;

    }
}