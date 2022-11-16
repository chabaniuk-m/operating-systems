package os.lab2.util;

import java.util.*;

public class Common {
    public static List<Process> randProcesses(int count) {

        List<Process> processes = new ArrayList<>(count);
        for (int i = 0; i < count; i++)
            processes.add(new Process(0, 0, 0));

        Random random = new Random(System.currentTimeMillis());
        int sumOfExecutionTime = (int) (count * (random.nextDouble(2) + 2));

        List<Integer> list = new ArrayList<>(sumOfExecutionTime);
        for (int i = 0; i < sumOfExecutionTime; i++)
            list.add(i);
        Collections.shuffle(list);
        list = list.subList(0, count - 1);
        list.sort(Comparator.comparingInt(a -> a));

        int executionTime = 1;
        for (int i = 0, j = 0; i < sumOfExecutionTime; i++) {
            if (list.get(0) == i) {
                processes.get(j++).executionTime = executionTime;
                list.remove(0);
                executionTime = 1;
                if (list.isEmpty()) break;
            } else {
                ++executionTime;
            }
        }
        processes.get(count - 1).executionTime = 1;

        processes.forEach(process -> process.deadline = (int) ((1 + 3.0 / count + random.nextDouble(0.3)) * count * process.executionTime));
        processes.forEach(p -> p.period = (int) ((1 + random.nextDouble(0.2)) * p.deadline));

        return processes;
    }
}

