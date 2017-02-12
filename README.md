# game-of-life
A simple implementation of [Conway's Game of Life](https://en.wikipedia.org/wiki/Conway's_Game_of_Life).

## Overview
Two dimensional grid board of cells represents the life and the colored cells represents the living ones.

And each cell's destiny is determined by their society.

* If the cell has less than 2 neighbours, it dies because of the loneliness.
* If the cell has exactly 2 neighbours, then it lives happily ever after.
* If the cell has exactly 3 neighbours, then it regenerates itself.
* If the cell has more than 3 neighbours, then it dies because of the overpopulation.
#### The challenge
    With this simulation, it is not allowed to use any conditional statement for these rules.

## Requirements
* Java 8+
* Maven 2+

## How to run?

`mvn package -DskipTests`

`java -jar target/game-of-life-1.0.jar`

