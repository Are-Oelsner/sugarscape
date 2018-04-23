public class Cell {
  //////////////////////////////////////////////////////////////////
  ///Private Variables
  //////////////////////////////////////////////////////////////////
  // maximum capacities
  private double sugarCapacity = 1.0;
  private double spiceCapacity = 1.0;
  // current capacities
  private double sugar = 1.0;
  private double spice = 1.0;
  // Regrowth rates
  private double sugarRR = 1.0;
  private double spiceRR = 1.0;
  // occupied status
  private boolean occupied = false;
  // time of last resource depletion
  private double timeLastUpdated = 0;

  //////////////////////////////////////////////////////////////////
  ///Constructors
  //////////////////////////////////////////////////////////////////
  // Default constructor
  public Cell() {}

  // Constructor
  public Cell(double sugar, double sugarRegrowth, double spice, double spiceRegrowth) {
    this.sugarCapacity = sugar;
    this.sugar = sugar;
    this.sugarRR = sugarRegrowth;
    this.spiceCapacity = spice;
    this.spice = spice;
    this.spiceRR = spiceRegrowth;
  }

  //////////////////////////////////////////////////////////////////
  ///Functions
  //////////////////////////////////////////////////////////////////
  ///Getters
  public double getSugarCapacity() { return sugarCapacity;}
  public double getSpiceCapacity() { return spiceCapacity;}
  public double getSugar() { return sugar;}
  public double getSpice() { return spice;}
  public boolean getOccupied() { return occupied;}

  public void setSugar(double r) { sugar = r;}
  public void setSpice(double r) { spice = r;}
  public void setSugarRegrowthRate(double r) { sugarRR = r;}
  public void setSpiceRegrowthRate(double r) { spiceRR = r;}

  public void updateCell(double time) {
    sugar = 0;
    spice = 0;
    timeLastUpdated = time;
  }

  public void regrowCell(double time) {
    sugar = Math.min(sugarCapacity, sugar + ((time - timeLastUpdated) * sugarRR));
    spice = Math.min(spiceCapacity, spice + ((time - timeLastUpdated) * spiceRR));
  }

  public void setOccupied(boolean status) {
    occupied = status;
  }

}
