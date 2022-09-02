# Chess repertoire API

## What is it ?
Chess repertoire is a project where users can store their repertoire, it's a memo of what they learned.
This project is the core API.

## What will it be possible to do with this API ?
1. Storing custom positions
2. Storing games (openings, end games or whole games), with the possibility to add variants for each ply
3. Commenting plies
4. Importing games from other players

## How is it made ?
It's made with clean architecture and TDD as a practice.
As for now, no persistence system or framework has been chosen yet, the most important being what the 
application can do, and not which tools were used to build it. 

## Current features
- Registration
- Position creation (check for FEN format, no check for illegal position)
