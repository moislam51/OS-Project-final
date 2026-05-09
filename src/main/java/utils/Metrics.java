package utils;

import models.Result;
import models.Process;
import java.util.*;

public final class Metrics {

    private Metrics() {} // prevent instantiation

    // =========================
    // Basic Metric Calculations
    // =========================

    public static double getTAT(Process p) {
        return p.getCompletionTime() - p.getArrivalTime();
    }

    public static double getWT(Process p) {
        return getTAT(p) - p.getBurstTime();
    }

    public static double getRT(Process p) {
        return p.getStartTime() - p.getArrivalTime();
    }

    // =========================
    // Aggregate Calculation
    // =========================

    public static Result calculate(List<Process> processes) {
        if (processes == null || processes.isEmpty()) {
            throw new IllegalArgumentException("Process list cannot be null or empty.");
        }

        double totalWT = 0;
        double totalTAT = 0;
        double totalRT = 0;

        for (Process p : processes) {

            // Compute metrics
            double tat = getTAT(p);
            double wt  = getWT(p);
            double rt  = getRT(p);

            // Optional safety check (can be removed if confident)
            if (tat < 0 || wt < 0 || rt < 0) {
                throw new IllegalStateException(
                    "Invalid timing detected for process " + p.getId()
                );
            }

            // Store in process
            p.setTurnaroundTime(tat);
            p.setWaitingTime(wt);
            p.setResponseTime(rt);

            // Aggregate
            totalTAT += tat;
            totalWT  += wt;
            totalRT  += rt;
        }

        int n = processes.size();

        return new Result(
            processes,
            totalWT / n,
            totalTAT / n,
            totalRT / n
        );
    }

   
    public static String compare(Result rrResult, Result priorityResult) {
        StringBuilder sb = new StringBuilder();

        // Which algorithm gave better average waiting time?
        sb.append("Average Waiting Time:\n");
        if (rrResult.getAvgWT() < priorityResult.getAvgWT())
            sb.append("  Round Robin is better (")
              .append(f(rrResult.getAvgWT())).append(" < ")
              .append(f(priorityResult.getAvgWT())).append(")\n\n");
        else if (priorityResult.getAvgWT() < rrResult.getAvgWT())
            sb.append("  Priority is better (")
              .append(f(priorityResult.getAvgWT())).append(" < ")
              .append(f(rrResult.getAvgWT())).append(")\n\n");
        else
            sb.append("  Both algorithms are equal.\n\n");

       
        sb.append("Average Response Time:\n");
        if (rrResult.getAvgRT() < priorityResult.getAvgRT())
            sb.append("  Round Robin is better (")
              .append(f(rrResult.getAvgRT())).append(" < ")
              .append(f(priorityResult.getAvgRT())).append(")\n\n");
        else if (priorityResult.getAvgRT() < rrResult.getAvgRT())
            sb.append("  Priority is better (")
              .append(f(priorityResult.getAvgRT())).append(" < ")
              .append(f(rrResult.getAvgRT())).append(")\n\n");
        else
            sb.append("  Both algorithms are equal.\n\n");

        
        sb.append("Fairness:\n");
        if (rrResult.getAvgWT() <= priorityResult.getAvgWT())
            sb.append("  Round Robin distributes CPU time more evenly ")
              .append("— appears fairer across all processes.\n\n");
        else
            sb.append("  Priority Scheduling achieved better average metrics, ")
              .append("but Round Robin still guarantees no starvation.\n\n");

        
        sb.append("Starvation Risk:\n");
        sb.append("  Priority Scheduling may cause starvation for low-priority ")
          .append("processes if high-priority processes keep arriving.\n")
          .append("  Round Robin avoids starvation by design.\n\n");

       
        sb.append("Urgency:\n");
        if (priorityResult.getAvgRT() <= rrResult.getAvgRT())
            sb.append("  Priority Scheduling handles urgent processes better ")
              .append("— high-priority processes get CPU faster.\n\n");
        else
            sb.append("  Round Robin gave faster first response overall, ")
              .append("but does not differentiate by urgency.\n\n");

       
        sb.append("Recommendation:\n");
        double rrScore    = rrResult.getAvgWT()  + rrResult.getAvgRT();
        double prScore    = priorityResult.getAvgWT() + priorityResult.getAvgRT();
        if (rrScore <= prScore)
            sb.append("  Round Robin is recommended for this workload ")
              .append("due to better fairness and balanced response times.");
        else
            sb.append("  Priority Scheduling is recommended for this workload ")
              .append("when urgency and fast service for important processes matter.");

        return sb.toString();
    }

    
    private static String f(double val) {
        return String.format("%.2f", val);
    }
}
