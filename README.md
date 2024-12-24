# Space Ship Game From a Student
This is a simple project I made in a first-year Java course (Intro to Java). 
Can only be played in the terminal. No GUI or JavaFX.
This is a 2-dimensional spaceship game travelling on a 3D map.

## Map
Players can set map sizes with x, y, and z-coordinates.
The player's ship always starts at the location (2, 2, 2).
The destination is always located at (x - 3, y - 3, z - 3), depending on the map size x, y, z.
There are only 50 turns/moves in a playthrough.

The ship has 2 view modes:
- Top View: A 3x3 of the ship on the x and y coordinates
- Side View: A 3x3 of the ship on the x and z coordinates
- Can swap views without costing a turn

## Actions & Directions
The ship has 5 different possible actions (numbered 1, 2, 3, 4, 5):
- 1             --> Change view                   (cost 0 turn)
- 2             --> Charge up a shield            (cost 1 turn)
- 3             --> Scan a 3x3 in any direction   (cost 0 turn)
- 4             --> Move in any direction         (cost 1 turn)
- 5             --> Pass a turn                   (cost 1 turn)

And 6 directions (uses the key IKJLUO, similar to WASD):
- I             --> Front                         (positive x-direction)
- K             --> Back                          (negative x-direction)
- J             --> Left                          (positive y-direction)
- L             --> Right                         (negative y-direction)
- U             --> Top                           (positive z-direction)
- O             --> Bottom                        (negative z-direction)

The ship can only move 1 unit at a time in the IKJLUO direction. Same with the scanning mechanics.

## Symbols
In the game, there are symbols such as:
- 'Δ' OR '‣'    --> Your ship
- '✪'           --> Destination
- '?'           --> Unknown/Unscanned location (Unknown consequences)
- '*'           --> Empty Space (deal 0 damage to the ship)
- 'D'           --> Debris (deal 1 damage to the ship)
- 'N'           --> Nova (deal 0-2 damages to the ship)
- 'ø'           --> Sector Interference (Map: Out of Bounds)

## Game Conditions
There are 3 "Game Over" conditions:
  - Reaching the destination '✪'
  - Running out of turns/moves
  - Hull point is reduced to 0

# Have fun and enjoy the game I made :)
