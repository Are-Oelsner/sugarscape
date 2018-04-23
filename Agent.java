import java.util.Random;

public class Agent {
  ///Private Variables
  private String id;   // identifier for the agent
  private int    row;
  private int    col;

  // Characteristics
  private int vision;
  private double sugarMetabolicRate;
  private double spiceMetabolicRate;
  private double sugar;
  private double spice;
  private double maxAge;
  double birthTime; //TODO can delete, using for color change in AgentCanvas

  // Events
  private double intermovementTime;
  private double deathTime;
  private double minTime;
  private static Random r = new Random();


  ///Constructor
  public Agent(String id, double time) {
    this.id = id;
    this.birthTime = time;
    this.vision = r.nextInt(6) + 1;
    this.sugarMetabolicRate = r.nextDouble() * 3 + 1;
    this.spiceMetabolicRate = r.nextDouble() * 3 + 1;
    this.sugar = r.nextDouble() * 20 + 5;
    this.spice = r.nextDouble() * 20 + 5;
    this.maxAge = time + (r.nextDouble() * 40 + 60);
    this.intermovementTime = time + (Math.log(1 - r.nextDouble()) / -0.5);
    this.deathTime = Math.min(time + (sugar / sugarMetabolicRate), time + (spice / spiceMetabolicRate));
    this.deathTime = Math.min(this.deathTime, maxAge);
    this.minTime = Math.min(intermovementTime, deathTime);
    System.out.println(id + "\tCurrent time:\t" + time + "  \tmin time: " + minTime);
  }

  ///Functions
  public void harvest(double Sugar, double Spice) {
    this.sugar += Sugar;
    this.spice += Spice;
    this.intermovementTime = minTime + (Math.log(1 - r.nextDouble()) / -0.5);
    this.deathTime = Math.min(minTime + (sugar / sugarMetabolicRate), minTime + (spice / spiceMetabolicRate));
    this.deathTime = Math.min(this.deathTime, maxAge);
    this.minTime = Math.min(intermovementTime, deathTime);
  }

  // Returns 0 for intermovement event, 1 for death
  public int getNextEventType() {
    if(minTime == intermovementTime)
      return 0;
    else
      return 1;
  }

  // Getters
  public int    getRow() { return this.row; }
  public int    getCol() { return this.col; }
  public String getID()  { return this.id;  }
  public double getMinTime() {return this.minTime;}
  public double getBirth() {return this.birthTime;} //TODO can delete, using for color setting in AgentCanvas
  public int getVision() { return vision;}

  // simple mutator methods below
  public void   setRowCol(int row, int col) {
    this.row = row;
    this.col = col;
  }

}

