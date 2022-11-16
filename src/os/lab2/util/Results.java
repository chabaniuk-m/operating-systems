package os.lab2.util;

public class Results {
  public String schedulingType;
  public String schedulingName;
  public int computationTime;
  public int downtime;

  public Results(String schedulingType, String schedulingName, int computationTime, int downtime) {
    this.schedulingType = schedulingType;
    this.schedulingName = schedulingName;
    this.computationTime = computationTime;
    this.downtime = downtime;
  }
}
