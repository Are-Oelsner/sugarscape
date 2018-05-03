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


Things to do:
  convert SimulationManager::moveAgent() function to judge based on both resources and the agents metabolic rates
  Trade
  Figure out why gridSize can't be set higher than the default of 40 without showing a blank display
  Check for trade in four cardinal neighbors after each move
  Generate more information in output file