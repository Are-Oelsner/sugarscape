public class Cell
{
  private double resourceCapacity = 1.0;
  private double resourceStatus = 0;
  private double regrowthRate = 1.0;
  private boolean occupied = false;

  public Cell() {

  }

  public Cell(double capacity, double regrowth) {
    resourceCapacity = capacity;
    regrowthRate = regrowth;
    resourceStatus = resourceCapacity;
  }

  public double getCapacity() {
    return resourceCapacity;
  }

  public double getResourceStatus() {
    return resourceStatus;
  }

  public boolean getOccupied() {
    return occupied;
  }

  public void setOccupied(boolean status) {
    occupied = status;
  }

  public void setRegrowthRate(double r) {
    regrowthRate = r;
  }
}
