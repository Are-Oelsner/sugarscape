default:
	javac *.java

r:
	java SimulationManager

o:
	vim -p Agent.java Cell.java Landscape.java SimulationManager.java
