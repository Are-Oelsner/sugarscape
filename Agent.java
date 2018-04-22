import java.util.Random;

public class Agent
{
  private String id;   // identifier for the agent
  private int    row;
  private int    col;

  private int vision;
  private double metabolicRate;
  private double resources;
  private double maxAge;

  private double intermovementTime;
  private double deathTime;
  private double minTime;
  private static Random r = new Random();

  public Agent(String id, double time) {
    this.id = id;

    //Random r = new Random();

    this.vision = r.nextInt(6) + 1;
    this.metabolicRate = r.nextDouble() * 3 + 1;
    this.resources = r.nextDouble() * 20 + 5;
    this.maxAge = time + (r.nextDouble() * 40 + 60);

    this.intermovementTime = time + (Math.log(1 - r.nextDouble()) / -0.5);
    this.deathTime = Math.min(time + (resources / metabolicRate), maxAge);
    this.minTime = Math.min(intermovementTime, deathTime);
    System.out.println("time: " + time + "  min time: " + minTime);
  }

  //TODO
  public void harvest(double resources) {
    this.resources += resources;
    this.intermovementTime = minTime + (Math.log(1 - r.nextDouble()) / -0.5);

    this.deathTime = Math.min(minTime + (resources / metabolicRate), maxAge);
    this.minTime = Math.min(intermovementTime, deathTime);
  }

  public int getNextEventType() {
    if(minTime == intermovementTime)
      return 0;
    else
      return 1;
  }

  // simple accessor methods below
  public int    getRow() { return this.row; }
  public int    getCol() { return this.col; }
  public String getID()  { return this.id;  }
  public double getMinTime() {return this.minTime;}

  // simple mutator methods below
  public void   setRowCol(int row, int col) {
    this.row = row;
    this.col = col;
  }

  public int getVision() {
    return vision;
  }
}

