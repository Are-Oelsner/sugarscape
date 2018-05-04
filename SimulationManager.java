import javax.swing.*;
import java.awt.BorderLayout;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.*;

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
  private PrintWriter pw;
  //======================================================================
  //* public SimulationManager(int gridSize, int numAgents, int initialSeed)
  //======================================================================

  public SimulationManager(int gridSize, int numAgents, int initialSeed, String outFile, int windowSize)
  {
    super("Sugarscape", windowSize, windowSize);  // name, window width, window height

    this.pq = new PriorityQueue<Double>(numAgents);
    this.map = new HashMap<Double, Integer>();
    this.agentList = new ArrayList<Agent>(numAgents);
    this.landscape = new Landscape(gridSize, gridSize);
    this.gridSize  = gridSize;
    this.time = 0.;
    this.rng = new Random(initialSeed);
    try{
      this.pw = new PrintWriter(outFile, "UTF-8");
      pw.print("Result of extended sugarscape simulation: \n");
      pw.print("Parameters: \n");
      pw.print("Grid Size: ");
      pw.println(gridSize);
      pw.print("NUmber of agents: ");
      pw.println(numAgents);
      pw.print("Initial seed: ");
      pw.println(initialSeed);
      pw.println();
    }catch(Exception e){
      System.out.println("Error: " + e.getMessage());
	    System.exit(0);
    }
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
      } while(landscape.getCellAt(row, col).getOccupied() != null);
      agentList.get(i).setRowCol(row, col);
    }

    this.createWindow();
    this.run();
    // this.destroyWindow();
    return;
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
    while(this.time < 100) {
      this.time = (double)pq.peek();
      pw.print("Event time: ");
      pw.print(this.time);
      pw.print("   ");
      // Get next agent
      agentIndex = map.get(this.time);
      Agent a = agentList.get(agentIndex);
      // Remove from map and priorityqueue
      map.remove(pq.poll());
      // Handles Event
      if(a.getNextEventType() == 0) { // Handle movement and resource consumption
        // Move agent to richest available cell within vision range
        pw.print("Move Event: ");
        pw.print(" Agent: ");
        pw.print(a.getID());
        pw.print(" -> current row and col: ");
        pw.print(a.getRow());
        pw.print(" ");
        pw.print(a.getCol());
        pw.print("\n");
        moveAgent(a);
        // Regrow new cell based on time since lastDepleted time TODO should this be here?
        landscape.getCellAt(a.getRow(), a.getCol()).regrowCell(this.time);
        // Depletes resources of new cell, sets lastDepleted time of cell.
        landscape.getCellAt(a.getRow(), a.getCol()).updateCell(this.time);
      }
      else { // Handle death
        Agent temp = agentList.get(agentIndex);
        pw.print("Death Event: ");
        pw.print(" Agent: ");
        pw.print(temp.getID());
        pw.print(" -> current row and col: ");
        pw.print(temp.getRow());
        pw.print(" ");
        pw.print(temp.getCol());
        pw.print("\n");
        landscape.getCellAt(temp.getRow(), temp.getCol()).setOccupied(null);
        agentList.set(agentIndex, new Agent("agent " + agentIndex, this.time));
        placeAgent(agentList.get(agentIndex));
      }

      // Add agent back to map and PQ
      minTime = agentList.get(agentIndex).getMinTime();
      map.put(minTime, agentIndex);
      pq.add(minTime);

      landscape.update(this.time);
      canvas.repaint();
      //try { Thread.sleep(500); } catch (Exception e) {}
      try { Thread.sleep(1); } catch (Exception e) {}

    }
    pw.close();
  }

  private void placeAgent(Agent a) {
    int row, col;
      do { // Randomly assigns agent to unoccupied cell
        row = rng.nextInt(gridSize); // an int in [0, gridSize-1]
        col = rng.nextInt(gridSize); // an int in [0, gridSize-1]
      } while(landscape.getCellAt(row, col).getOccupied() != null);
      a.setRowCol(row, col);
      landscape.getCellAt(row, col).setOccupied(a);
  }

  private double getLife(Agent a, Cell c) {
    double sugar = a.getSugar();
    double spice = a.getSpice();
    double sugarMetabolic = a.getSugarMetabolicRate();
    double spiceMetabolic = a.getSpiceMetabolicRate();

    sugar += c.getSugar();
    spice += c.getSpice();

    double life = sugar / sugarMetabolic;
    if (spice / spiceMetabolic < life) {
      life = spice / spiceMetabolic;
    }

    return life;
  }

  private void moveAgent(Agent a) {
    pw.print("    ");
    int xSize = landscape.getXSize();
    int ySize = landscape.getYSize();
    int row = a.getRow();
    int col = a.getCol();
    int fov = a.getVision();
    double maxLife = 0;
    int maxRow = 0;
    int maxCol = 0;
    Cell temp;

    landscape.getCellAt(row, col).setOccupied(null);

    // need to account for multiple squares with the same amount of resources
    for (int j = 0; j <= fov; j++) {
      temp = landscape.getCellAt(row, (col + fov) % ySize);
      if (getLife(a, temp) > maxLife && temp.getOccupied() == null) {
        maxLife = getLife(a, temp);
        maxRow = row;
        maxCol = (col + fov) % ySize;
      }
      temp = landscape.getCellAt((row + fov) % xSize, col);
      if (getLife(a, temp) > maxLife && temp.getOccupied() == null) {
        maxLife = getLife(a, temp);
        maxRow = (row + fov) % xSize;
        maxCol = col;
      }
      temp = landscape.getCellAt(row, (col - fov + ySize) % ySize);
      if (getLife(a, temp) > maxLife && temp.getOccupied() == null) {
        maxLife = getLife(a, temp);
        maxRow = row;
        maxCol = (col - fov + ySize) % ySize;
      }
      temp = landscape.getCellAt((row - fov + xSize) % xSize, col);
      if (getLife(a, temp) > maxLife && temp.getOccupied() == null) {
        maxLife = getLife(a, temp);
        maxRow = (row - fov + xSize) % xSize;
        maxCol = col;
      }
    }
    
    // Assign new cell
    pw.print("Agent move to row and col: ");
    pw.print(maxRow);
    pw.print(" ");
    pw.print(maxCol);
    a.setRowCol(maxRow, maxCol);
    // Pass agent current resource levels of cell
    pw.print("  Resources collected: sugar->");
    pw.print(landscape.getCellAt(maxRow, maxCol).getSugar());
    pw.print("  spice->");
    pw.print(landscape.getCellAt(maxRow, maxCol).getSpice());
    pw.print("\n");
    a.harvest(landscape.getCellAt(maxRow, maxCol).getSugar(), landscape.getCellAt(maxRow, maxCol).getSpice());
    // Sets cell as occupied
    landscape.getCellAt(maxRow, maxCol).setOccupied(a);

    trade(a);
  }

  private void trade(Agent a) {
    int row = a.getRow();
    int col = a.getCol();

    if (col + 1 < landscape.getYSize()) {
      Agent temp = landscape.getCellAt(row, col + 1).getOccupied();
      if (temp != null) {
        trade(a, temp);
      }
    }
    if (row + 1 < landscape.getXSize()) {
      Agent temp = landscape.getCellAt(row + 1, col).getOccupied();
      if (temp != null) {
        trade(a, temp);
      }
    }
    if (col - 1 >= 0) {
      Agent temp = landscape.getCellAt(row, col - 1).getOccupied();
      if (temp != null) {
        trade(a, temp);
      }
    }
    if (row - 1 >= 0) {
      Agent temp = landscape.getCellAt(row - 1, col).getOccupied();
      if (temp != null) {
        trade(a, temp);
      }
    }
  }

  private void trade(Agent a, Agent b) {
    double MRSa = a.computeRatio();
    double MRSb = b.computeRatio();
    if (MRSa == MRSb) {
      return;
    }
    pw.print("    ");
    pw.print("    ");
    pw.print("Trade between: ");
    pw.print(a.getID());
    pw.print(" and ");
    pw.print(b.getID());
    double price = Math.sqrt(MRSa * MRSb);
    pw.print("  price->");
    pw.print(price);
    pw.print("\n");
    double aSugar = a.getSugar();
    double aSpice = a.getSpice();
    double bSugar = b.getSugar();
    double bSpice = b.getSpice();

    double aSugarMetabolic = a.getSugarMetabolicRate();
    double aSpiceMetabolic = a.getSpiceMetabolicRate();
    double bSugarMetabolic = b.getSugarMetabolicRate();
    double bSpiceMetabolic = b.getSpiceMetabolicRate();

    double aTimeToLive = a.getTimeToLive();
    double bTimeToLive = b.getTimeToLive();

    boolean MRSaIsLarger = false;
    if (price > 1) {
      if (MRSa > MRSb) {
        MRSaIsLarger = true;
        aSugar += 1;
        aSpice -= price;
        bSugar -= 1;
        bSpice += price;
      } else {
        MRSaIsLarger = false;
        aSugar -= 1;
        aSpice += price;
        bSugar += 1;
        bSpice -= price;
      }
    } else {
      if (MRSa > MRSb) {
        MRSaIsLarger = true;
        aSugar += price;
        aSpice -= 1;
        bSugar -= price;
        bSpice += 1;
      } else {
        MRSaIsLarger = false;
        aSugar -= price;
        aSpice += 1;
        bSugar += price;
        bSpice -= 1;
      }
    }

    if (MRSaIsLarger = true) {
      double tempMRSa = (aSpice / aSpiceMetabolic) / (aSugar / aSugarMetabolic);
      double tempMRSb = (bSpice / bSpiceMetabolic) / (bSugar / bSugarMetabolic);
      double aTimeToLiveTemp = Math.min(aSpice / aSpiceMetabolic, aSugar / aSugarMetabolic);
      double bTimeToLiveTemp = Math.min(bSpice / bSpiceMetabolic, bSugar / bSugarMetabolic);

      if (tempMRSa > tempMRSb && aTimeToLiveTemp > aTimeToLive && bTimeToLiveTemp > bTimeToLive) {
        a.setSugar(aSugar);
        a.setSpice(aSpice);
        b.setSugar(bSugar);
        b.setSpice(bSpice);
        System.out.println("trade happened");
        System.out.println("price: " + price);
        trade(a, b);
      } else {
        return;
      }
    } else {
      double tempMRSa = (aSpice / aSpiceMetabolic) / (aSugar / aSugarMetabolic);
      double tempMRSb = (bSpice / bSpiceMetabolic) / (bSugar / bSugarMetabolic);
      double aTimeToLiveTemp = Math.min(aSpice / aSpiceMetabolic, aSugar / aSugarMetabolic);
      double bTimeToLiveTemp = Math.min(bSpice / bSpiceMetabolic, bSugar / bSugarMetabolic);

      if (tempMRSa < tempMRSb && aTimeToLiveTemp > aTimeToLive && bTimeToLiveTemp > bTimeToLive) {
        a.setSugar(aSugar);
        a.setSpice(aSpice);
        b.setSugar(bSugar);
        b.setSpice(bSpice);
        System.out.println("trade happened");
        System.out.println("price: " + price);
        trade(a, b);
      } else {
        return;
      }
    }
  }

  //======================================================================
  //* public static void main(String[] args)
  //* Just including main so that the simulation can be executed from the
  //* command prompt.  Note that main just creates a new instance of this
  //* class, which will start the GUI window and then we're off and
  //* running...
  //======================================================================
  public static void main(String[] args) {
    int gridsize = 40;
    int numAgents = 400;
    int initialSeed = 8675309;
    String file = "output.txt";
    if(args.length > 0) {
      try {
        gridsize = Integer.parseInt(args[0]);
      }catch(NumberFormatException e) {
        System.err.println("Argument" + args[0] + " must be an integer.");
        gridsize = 40;
      }
    }
    if(args.length > 1) {
      try {
        numAgents = Integer.parseInt(args[1]);
      }catch(NumberFormatException e) {
        System.err.println("Argument" + args[1] + " must be an integer.");
        numAgents = 400;
      }
    }
    if(args.length > 2) {
      try {
        initialSeed = Integer.parseInt(args[2]);
      }catch(NumberFormatException e) {
        System.err.println("Argument" + args[2] + " must be an integer.");
        initialSeed = 8675309;
      }
    }
    if(args.length > 3) {
      file = args[3];  
    }

    int windowSize = gridsize * 10 + 100;
    new SimulationManager(gridsize, numAgents, initialSeed, file, windowSize);
  }
}
