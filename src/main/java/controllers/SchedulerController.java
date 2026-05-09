package controller;

import models.Process;
import models.Result;
import services.PriorityService;
import services.RoundRobinService;
import utils.Cloner;

import java.util.List;

public class SchedulerController {

    private final PriorityService priorityService;
    private final RoundRobinService roundRobinService;

    public SchedulerController() {
        this.priorityService = new PriorityService();
        this.roundRobinService = new RoundRobinService();
    }

    public Result[] runScheduling(List<Process> processes, int quantum) {

        List<Process> priorityProcesses = Cloner.cloneList(processes);
        List<Process> roundRobinProcesses = Cloner.cloneList(processes);

        Result priorityResult = priorityService.run(priorityProcesses);
        Result roundRobinResult = roundRobinService.run(roundRobinProcesses, quantum);

        return new Result[] {
                priorityResult,
                roundRobinResult
        };
    }
}