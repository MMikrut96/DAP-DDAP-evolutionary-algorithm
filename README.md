# DAP and DDAP
Project aims to solve DAP (demand allocation problem) and DDAP (dimensioning and demand allocation problem) in network, using evolutionary algorithm for three given network topology descripcions in Backus-Naur Form.

Application allows to:
-choose between DAP or DDAP problem to solve
-give seed for random generator
-define population size in EV algorithm generation
-give mutation probability
-choose STOP criterion

Application also allows to solve problem with brute force method, but it is only recommended for network topology described in net4.txt file due to prolonged solving time for bigger networks.


## How to run
1. Open project folder /out/artifacts/Optymalizacja_jar in CLI
2. Make sure the files with network topology descrption are in the same folder as file Optymalizacja.jar
2. Run project with command (for e.g.):
```sh
java -jar .\Optymalizacja.jar
```
