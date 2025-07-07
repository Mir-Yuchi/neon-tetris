# Neon Tetris

Neon Tetris is a modern implementation of the classic Tetris game, built using Java and JavaFX. It features a sleek, neon-themed user interface, real-time rendering, and a scoring system that includes combos and back-to-back clears. The game supports standard Tetris mechanics such as piece rotation, holding pieces, and hard drops, along with a dynamic background that changes with the player's level.

## Table of Contents
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Installation](#installation)
- [How to Play](#how-to-play)
- [Development Process](#development-process)

## Features
- **Classic Tetris Gameplay**: Move, rotate, and drop Tetromino pieces to clear lines and score points.
- **Hold Functionality**: Swap the current piece with a held piece once per turn.
- **Next Pieces Preview**: View the next three upcoming pieces.
- **Ghost Piece**: See where the current piece will land if hard-dropped.
- **Scoring System**: Earn points for clearing lines, with bonuses for combos and back-to-back Tetrises.
- **Level Progression**: The game speeds up as you clear more lines.
- **High Score Tracking**: Persistent high scores stored locally.
- **Neon-Themed UI**: A visually appealing interface with a dynamic background that changes based on the level.

## Technologies Used
- **Java 17**: The core programming language used for development.
- **JavaFX 17**: Used for creating the graphical user interface and handling user inputs.
- **Maven**: For project management, dependency handling, and building the application.
- **CSS**: Custom styles for the game's UI components.
- **Java Preferences API**: For storing and retrieving high scores persistently.

## Installation

### Prerequisites
- **Java Development Kit (JDK) 17 or higher**: Ensure you have JDK installed. You can download it from [Oracle's website](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html).
- **Maven**: Required for building and running the project. Install Maven from [Apache Maven](https://maven.apache.org/download.cgi).

### Steps to Install
1. **Clone the Repository**:
   ```bash
   git clone https://github.com/Mir-Yuchi/neon-tetris.git
   cd neon-tetris
   ```

2. **Build the Project**:
   Use Maven to compile the project and resolve dependencies.
   ```bash
   mvn clean install
   ```

3. **Run the Application**:
   Launch the game using Maven's JavaFX plugin.
   ```bash
   mvn javafx:run
   ```

   Alternatively, you can run the JAR file directly if you've built it:
   ```bash
   java -jar target/group__25-1.0-SNAPSHOT.jar
   ```

## How to Play
- **Controls**:
    - **Left Arrow**: Move the current piece left.
    - **Right Arrow**: Move the current piece right.
    - **Down Arrow**: Soft drop the current piece (faster downward movement).
    - **Up Arrow**: Rotate the current piece clockwise.
    - **Spacebar**: Hard drop the current piece instantly to the bottom.
    - **C**: Hold the current piece or swap with the held piece.
    - **R**: Restart the game after a game over.

- **Objective**:
    - Arrange falling Tetromino pieces to form complete horizontal lines without gaps.
    - Clearing lines earns points, and the game speeds up as you progress through levels.
    - The game ends when the stack of pieces reaches the top of the board.

- **Scoring**:
    - Points are awarded based on the number of lines cleared at once:
        - 1 line: 100 points
        - 2 lines: 300 points
        - 3 lines: 500 points
        - 4 lines (Tetris): 800 points
    - Combos and back-to-back Tetrises provide additional bonuses.

## Development Process
The development of Neon Tetris followed an object-oriented design approach, breaking down the game into manageable components:

1. **Game Logic**:
    - The `Board` class manages the game grid, piece movement, rotation, and line clearing.
    - Tetromino pieces are defined in separate classes (`IPiece`, `JPiece`, etc.), extending a base `Tetromino` class.
    - The `TetrominoFactory` generates random pieces using a bag system to ensure fair distribution.

2. **Rendering**:
    - The `Renderer` class handles all UI rendering, including the game board, hold area, next pieces, and stats.
    - JavaFX's `Pane`, `Rectangle`, and `Label` components are used to create the visual elements.
    - Dynamic resizing ensures the game looks good on different window sizes.

3. **Game State Management**:
    - A state pattern is used in `GameEngine` to manage different game states (e.g., running, game over).
    - The `AnimationTimer` drives the game loop, updating and rendering the game at each frame.

4. **Scoring and Persistence**:
    - The `ScoreSystem` class calculates scores based on line clears, combos, and level.
    - High scores are stored using Java's `Preferences` API for persistence across sessions.

5. **UI Styling**:
    - Custom CSS is applied to enhance the visual appeal, including neon colors and effects.

