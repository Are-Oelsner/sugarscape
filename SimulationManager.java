import javax.swing.*;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Random;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.PriorityQueue;

class SimulationManager extends WindowManager {
  ///Simulation Variables
  protected PriorityQueue<Double> pq;   // PriorityQueue for next event times
  protected Map<Double, Integer> map;   // Map from next event times to agent index in agentList
  protected ArrayList<Agent> agentList; // List of agents in simulation
  protected Landscape landscape;        // Landscape object
  protected int gridSize;               // Landscape edge length in cells
  private AgentCanvas canvas;           // the canvas on which agents are drawn
  private double time;                  // current simulation time
  private Random rng;

  //======================================================================
  //* public SimulationManager(int gridSize, int numAgents, int initialSeed)
  //======================================================================
  public SimulationManager(int gridSize, int numAgents, int initialSeed)
  {
    super("Sugarscape", 500, 500);  // name, window width, window height

    this.pq = new PriorityQueue<Double>(numAgents);
    this.map = new HashMap<Double, Integer>();
    this.agentList = new ArrayList<Agent>(numAgents);
    this.landscape = new Landscape(gridSize, gridSize);
    this.gridSize  = gridSize;
    this.time = 0.;
    this.rng = new Random(initialSeed);

    // TODO Might want to make the max value much lower. This would be one agent per
    // cell, which wouldn't allow for movement. Also because of the random
    // nature of placing agents originally it would make setup times increase
    // exponentially.
    if(numAgents > (gridSize * gridSize))
      System.out.println("Error: number of agents ( " + numAgents + " ) exceeds number of cells ( " + (gridSize * gridSize) + ").");

    this.time = 0;   // initialize the simulation clock

    //Function temp variables
    double minTime;
    int row, col;

    for (int i = 0; i < numAgents; i++) {
      agentList.add(new Agent("agent " + i, 0));
      minTime = agentList.get(i).getMinTime();
      map.put(minTime, i);
      pq.add(minTime);

      do { // Randomly assigns agent to unoccupied cell
        row = rng.nextInt(gridSize); // an int in [0, gridSize-1]
        col = rng.nextInt(gridSize); // an int in [0, gridSize-1]
      } while(landscape.getCellAt(row, col).getOccupied());
      agentList.get(i).setRowCol(row, col);
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
    System.out.println("Run");
    // Function temporary variables
    int agentIndex;
    double minTime;
    while(this.time < 100000) {
      this.time = (double)pq.peek();

      // Get next agent
      agentIndex = map.get(this.time);
      Agent a = agentList.get(agentIndex);
      // Remove from map and priorityqueue
      map.remove(pq.poll());
      // Regrow old cell based on time since lastDepleted time
      landscape.getCellAt(a.getRow(), a.getCol()).regrowCell(this.time);
      // Handles Event
      if(a.getNextEventType() == 0) { // Handle movement and resource consumption
        // Move agent to richest available cell within vision range
        moveAgent(a);
        // Depletes resources of new cell, sets lastDepleted time of cell.
        landscape.getCellAt(a.getRow(), a.getCol()).updateCell(this.time);
      }
      else { // Handle death
        agentList.set(agentIndex, new Agent("agent " + agentIndex, this.time));
        placeAgent(agentList.get(agentIndex));
      }

      // Add agent back to map and PQ
      minTime = agentList.get(agentIndex).getMinTime();
      map.put(minTime, agentIndex);
      pq.add(minTime);

      canvas.repaint();
      //try { Thread.sleep(500); } catch (Exception e) {}
      try { Thread.sleep(100); } catch (Exception e) {}

    }
  }

  private void placeAgent(Agent a) {
    int row, col;
      do { // Randomly assigns agent to unoccupied cell
        row = rng.nextInt(gridSize); // an int in [0, gridSize-1]
        col = rng.nextInt(gridSize); // an int in [0, gridSize-1]
      } while(landscape.getCellAt(row, col).getOccupied());
      a.setRowCol(row, col);
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

    // Assign new cell
    a.setRowCol(maxRow, maxCol);
    // Pass agent current resource levels of cell
    a.harvest(landscape.getCellAt(maxRow, maxCol).getResourceStatus());
    // Sets cell as occupied
    landscape.getCellAt(maxRow, maxCol).setOccupied(true);
  }


  //======================================================================
  //* public static void main(String[] args)
  //* Just including main so that the simulation can be executed from the
  //* command prompt.  Note that main just creates a new instance of this
  //* class, which will start the GUI window and then we're off and
  //* running...
  //======================================================================
  public static void main(String[] args) {
    new SimulationManager(40, 400, 8675309);
  }
}
