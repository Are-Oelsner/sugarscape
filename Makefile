default:
	javac *.java

c: 
	javac *.java -Xlint:unchecked

r:
	java SimulationManager

o:
	vim -p Agent.java Cell.java Landscape.java SimulationManager.java
