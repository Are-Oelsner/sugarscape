import javax.swing.*;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Random;

import java.util.Map;
import java.util.PriorityQueue;

class SimulationManager extends WindowManager
{
  protected ArrayList<Agent> agentList;
  // A list of all agents in the simulation; this is declared as
  // protected because we access it directly from within AgentCanvas.
  // Why?  Because we only access it to draw the agents, and given
  // that the list may be large, it doesn't make sense to
  // make a copy and return that copy to AgentCanvas.

  protected Landscape landscape;
  protected int gridSize;

  private AgentCanvas canvas;  // the canvas on which agents are drawn
  private Random rng;

  private double time;  // the simulation time

  //======================================================================
  //* public SimulationManager(int gridSize, int numAgents, int initialSeed)
  //======================================================================
  public SimulationManager(int gridSize, int numAgents, int initialSeed)
  {
    super("Sugarscape", 500, 500);  // name, window width, window height

    this.gridSize  = gridSize;
    this.agentList = new ArrayList<Agent>();

    rng = new Random(initialSeed);

    this.time = 0;   // initialize the simulation clock

    landscape = new Landscape(gridSize, gridSize);

    for (int i = 0; i < numAgents; i++)
    {
      Agent a = new Agent("agent " + agentList.size());
      agentList.add(a);

      int row = rng.nextInt(gridSize); // an int in [0, gridSize-1]
      int col = rng.nextInt(gridSize); // an int in [0, gridSize-1]

      // we should check to make sure the cell isn't already occupied!

      a.setRowCol(row, col);
    }

    this.createWindow();
    this.run();
  }

  //======================================================================
  //* public void createWindow()
  //======================================================================
  public void createWindow()
  {
    this.setLayout(new BorderLayout());
    this.getContentPane().setLayout(new BorderLayout()); // java.awt.*
    canvas = new AgentCanvas(this);
    this.getContentPane().add( new JScrollPane(canvas), BorderLayout.CENTER);
  }

  // simple accessor methods
  public int    getGridSize() { return this.gridSize; }
  public double getTime()     { return this.time;     }

  //======================================================================
  //* public void run()
  //* This is where your main simulation event engine code should go...
  //======================================================================
  public void run() {
    // bogus simulation code below...
    int t = 0;
    while(t < 100) {
      this.time = t;

      int[] rand = new Random().ints(0, agentList.size()).distinct().limit(agentList.size()).toArray();
      System.out.println(rand[1]);

      for (int i = 0; i < rand.length; i++)
      {
        Agent a = agentList.get(rand[i]);
        moveAgent(a);

      }
      canvas.repaint();
      try { Thread.sleep(500); } catch (Exception e) {}

      t++;
    }
  }

  private void moveAgent(Agent a) {
    int xSize = landscape.getXSize();
    int ySize = landscape.getYSize();
    int row = a.getRow();
    int col = a.getCol();
    int fov = a.getVision();
    double maxResource = 0;
    int maxRow = 0;
    int maxCol = 0;
    Cell temp;

    landscape.getCellAt(row, col).setOccupied(false);

    // need to account for multiple squares with the same amount of resources
    for (int j = 0; j <= fov; j++) {
      temp = landscape.getCellAt(row, (col + fov) % ySize);
      if (temp.getCapacity() > maxResource && temp.getOccupied() == false) {
        maxResource = temp.getCapacity();
        maxRow = row;
        maxCol = (col + fov) % ySize;
      }
      temp = landscape.getCellAt((row + fov) % xSize, col);
      if (temp.getCapacity() > maxResource && temp.getOccupied() == false) {
        maxResource = temp.getCapacity();
        maxRow = (row + fov) % xSize;
        maxCol = col;
      }
      temp = landscape.getCellAt(row, (col - fov + ySize) % ySize);
      if (temp.getCapacity() > maxResource && temp.getOccupied() == false) {
        maxResource = temp.getCapacity();
        maxRow = row;
        maxCol = (col - fov + ySize) % ySize;
      }
      temp = landscape.getCellAt((row - fov + xSize) % xSize, col);
      if (temp.getCapacity() > maxResource && temp.getOccupied() == false) {
        maxResource = temp.getCapacity();
        maxRow = (row - fov + xSize) % xSize;
        maxCol = col;
      }
    }

    // we should check to make sure the cell isn't already occupied!
    a.setRowCol(maxRow, maxCol);
    landscape.getCellAt(maxRow, maxCol).setOccupied(true);
  }




  //======================================================================
  //* public static void main(String[] args)
  //* Just including main so that the simulation can be executed from the
  //* command prompt.  Note that main just creates a new instance of this
  //* class, which will start the GUI window and then we're off and
  //* running...
  //======================================================================
  public static void main(String[] args)
  {
    new SimulationManager(40, 400, 8675309);
  }
}
