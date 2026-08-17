# The Drake

A JavaFX desktop implementation of *The Drake*, a two-player abstract strategy board game (a simplified variant of *The Duke*). Built as a semestral project for the Programming in Java course at the Faculty of Information Technology, CTU in Prague.

## Overview

Players place and move troops on a board, each with its own set of actions (step, slide, strike) that determine how it can move or capture. The game is won by capturing the opponent's leader.

## Features

- Full rule engine: troop placement, movement actions (step/slide/strike), captures, and turn/game-state management.
- JavaFX UI with board, troop stacks, captured troops, and game setup/menu screens.
- Game state serialization to/from JSON.
- Unit tests (JUnit 5) covering the core game logic.

## Tech Stack

Java 21, JavaFX, Maven, JUnit 5.

## Running

```bash
mvn clean javafx:run
```
