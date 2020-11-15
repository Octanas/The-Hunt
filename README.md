# The Hunt
The Hunt is a Pac-Man inspired Distributed Artificial Intelligence project built with the Java Agent Development Framework (JADE).

The hunters are tasked with catching the beast, who is quicker than them. So they'll have to outsmart it by working together and trapping it.

## Dependencies

The JADE jar file is already [included in the repository](lib/) and in the classpath, so the project can be compiled and executed normally.

To test if JADE is working, open its GUI:

```
java -cp lib/jade.jar jade.Boot -gui
```

## Compiling

To compile the project through the command line, run the following command:

```
javac -d "bin" -cp "lib/jade.jar;src" .\src\Agents\*.java .\src\Behaviours\*.java .\src\Client\*.java .\src\Environment\*.java
```

## Running

To run the project, run the following command:

```
java -cp "lib/jade.jar;bin" Client/Hunt
```

The program accepts the following parameters (the order must be the same):
* Number of hunters (default = 4)
* Number of prey (default = 1)
* If the prey start at a fixed location ("true" or "false", default = false)

The following example runs the project with 3 hunters and 2 prey, with the prey starting in a fixed location (not a random location):

```
java -cp "lib/jade.jar;bin" Client/Hunt 3 2 true
```

## Trials

We did some trials to see and compare the results of the program with different parameters, they can be found in the [tests.txt](tests.txt) file.