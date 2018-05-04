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

Output:
  In the output file generate by the program ("output.txt" if default) you will find:
  1) summary of the experiment(grid size, number of agents, initial seed)
  2) summary of each event (time and type)
  3) related agent (agent ID)
  4) summary of each trade (related agent and price)
