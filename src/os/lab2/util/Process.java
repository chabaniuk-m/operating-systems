package os.lab2.util;

public class Process {
  public int executionTime;
  public int deadline;
  public int period;
  public int CPUUseTime = 0;
  public int executed = 0;
  public boolean available = false;

  public Process(int executionTime, int deadline, int period) {
    this.executionTime = executionTime;
    this.deadline = deadline;
    this.period = period;
  }

  @Override
  public String toString() {
    return String.format("""
            Process {
             executionTime = %d,
             deadline = %d,
             period = %d,
             CPUUseTime = %d,
             executed = %d }
            """, executionTime, deadline, period, CPUUseTime, executed);
  }
}
