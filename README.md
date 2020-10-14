# The Hunt
The Hunt is a Pac-Man inspired Distributed Artificial Intelligence project built with the Java Agent Development Framework (JADE).

The hunters are tasked with catching the beast, who is quicker than them. So they'll have to outsmart it by working together and trapping it.

## Compiling
This project uses Maven to get the JADE library. However, its repository does not have a valid SSL certificate, which makes it necessary to do the following for the first time you compile the project:

```
mvn compile --define maven.wagon.http.ssl.insecure=true --define maven.wagon.http.ssl.allowall=true
```

This will download the necessary dependencies and from then on you can compile the project normally (unless you delete them, of course):

```
mvn compile
```