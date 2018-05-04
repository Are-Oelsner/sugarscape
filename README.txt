Simulations ABS Final Project
Extending Sugarscape Simulation to include Sugar-Spice trade
Authors: Are, TianChang, Nick

Build and Run:
  Note: This project has only been tested under linux and OSX environment.
  To build the project, go to the root directory and run
  make
  To run the project with default parameters (40x40 grid, 400 agents, initial seed=8675309, output file="output.txt"), run
  make r
  To run the project with custom settings, run
  java SimulationManager [grid_ize] [num_of_agents] [initial_seed] [output_file_path]

Project Desgin:
  SimulationManager: This is the entry point of our program and also the we place our simulation structure in. It extends the WindowManager. It has one instance object of Landscape and an ArrayList of objects of Agent.
  
  Landscape: This class represents the grid. It holds and controls all the cells.
  
  Cell: This class represents each cell. Each object has capacity, regrowth rate and current level for both resources (sugar and spice).
  
  Agent: This class represents each Agent. Each object has ID, current position, metabolic rate for both resources and current level of both resources. We calculate the agent color base on its MRS, if it dies more quickly because of the starvation of sugar, it appears to be light pink on the grid. If it dies more quickly because of the starvation of spice, it appears to be blue on the grid.

Output:
  In the output file generate by the program ("output.txt" if default) you will find:
  1) summary of the experiment(grid size, number of agents, initial seed)
  2) summary of each event (time and type)
  3) related agent (agent ID)
  4) summary of each trade (related agent and price)

Experiments:
  In this project, we conducted 3 main experiments to see the effect on price of different metabolic rate:
  1) In the first (normal) case, we set the Agents' metabolic rate for both sugar and spice to unif(1, 4).
    According to the Graphical Simulation, we can see that the pink agents(need sugar more desperatly) tends to gather around the cells with high sugar capacity (green cells). Also, the blue agents(need spice more desperatly) tends to gather around the cells with high spice capacity (red cells).
  
  2) In the second case, we set the Agents' metabolic rate for sugar to be unif(10, 40) and keep the metabolic rate for spice to be unif(1, 4).
    The first thing we notice is that 