# The Hunt
The Hunt is a Pac-Man inspired Distributed Artificial Intelligence project built with the Java Agent Development Framework (JADE).

The hunters are tasked with catching the beast, who is quicker than them. So they'll have to outsmart it by working together and trapping it.

## Compiling
The JADE jar file is already [included in the repository](lib/) and in the classpath, so the project can be compiled and executed normally.

To test if JADE is working, open its GUI:

```
java -cp lib/jade.jar jade.Boot -gui
```

## Running

The program accepts the number of hunters as a parameter. If ommitted, the default value is 4.