package utils;

import java.util.*;
import models.Process;

public class Cloner {

    public static List<Process> cloneList(List<Process> original) {
        List<Process> copy = new ArrayList<>();

        for (Process p : original) {
            copy.add(new Process(
                p.getId(),
                p.getArrivalTime(),
                p.getBurstTime(),
                p.getPriority()
            ));
        }

        return copy;
    }
}
