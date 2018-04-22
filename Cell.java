public class Cell
{
  private double resourceCapacity = 1.0;
  private double resourceStatus = 1.0;
  private double regrowthRate = 1.0;
  private boolean occupied = false;
  private double timeLastDepleted = 0;

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

  public void setResourceStatus(double r) {
    resourceStatus = r;
  }

  public void updateCell(double time) {
    resourceStatus = 0;
    timeLastDepleted = time;
  }

  public void regrowCell(double time) {
    resourceStatus = Math.min(resourceCapacity, resourceStatus + ((time - timeLastDepleted) * regrowthRate));
  }

  public void setOccupied(boolean status) {
    occupied = status;
  }

  public void setRegrowthRate(double r) {
    regrowthRate = r;
  }
}
