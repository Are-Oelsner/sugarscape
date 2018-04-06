import java.util.Random;

public class Agent
{
  private String id;   // identifier for the agent
  private int    row;
  private int    col;

  private int vision;
  private double metabolicRate;
  private double resourceEndowment;
  private double maxAge;

  private double intermovementTime;
  private double deathTime;
  private double minTime;

  public Agent(String id)
  {
    this.id = id;

    Random r = new Random();
    this.vision = r.nextInt(6) + 1;
    this.metabolicRate = r.nextDouble() * 3 + 1;
    this.resourceEndowment = r.nextDouble() * 20 + 5;
    this.maxAge = r.nextDouble() * 40 + 60;

    this.intermovementTime = Math.log(1 - r.nextDouble()) / -0.5;

    this.deathTime = resourceEndowment / metabolicRate;
    this.minTime = Math.min(intermovementTime, deathTime);
    System.out.println("min time: " + minTime);
  }

  // simple accessor methods below
  public int    getRow() { return this.row; }
  public int    getCol() { return this.col; }
  public String getID()  { return this.id;  }
  public double getMinTime() {return this.minTime;}

  // simple mutator methods below
  public void   setRowCol(int row, int col)
  {
    this.row = row;
    this.col = col;
  }

  public int getVision() {
    return vision;
  }
}

