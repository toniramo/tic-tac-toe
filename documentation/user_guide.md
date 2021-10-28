# User guide

1. Build and run project using [Command-line commands](#command-line-commands).
2. [Play](#how-to-play) the game.

## Command-line commands
**Note:** Instructions expect you to run commands from `<project root>/tic-tac-toe`.

### Building project
Build runnable jar-file:
```
./gradlew build
```
- This runs Gradle build task which includes also checkstyle checks and unit tests and if all checks and tests are passing, `tictactoe.jar` is generated to `./tic-tac-toe/build/libs`

### Running project

When you have generated `tictactoe.jar` according to instruction above, you can run it using command:
```
java -jar ./build/libs/tictactoe.jar
```

Alternatively you can run project with Gradle as follows:
```
./gradlew run
```
### Checks, tests and reports
Run both checkstyle and unit tests and generate reports:
```
./tictactoe/gradlew check
```
   - Output reports for checkstyle, `main.html` and `test.html`, will be found from `./build/reports/checkstyle`.
   - Jacoco test report is generated in `./build/reports/jacoco/test/html`. You can see main page of the report by opening `index.html`.

Run unit tests:
```
./gradlew test
```

Run AI performance test:
```
./gradlew performanceTest
```
- Notice that this may take up to 1.5 - 2h to finish. 
- Output data will be printed in `./build/reports/tests/performanceTest/data/perfTest_<system time in milliseconds>.txt`
### Cleaning outputs
In order to delete contents of `build` directory, run
```
./gradlew clean
```
### For more information 
about Gradle command-line interface see https://docs.gradle.org/current/userguide/command_line_interface.html.

## How to play

### Rules
Game rules follow the rules of traditional [tic-tac-toe](https://en.wikipedia.org/wiki/Tic-tac-toe) with following exceptions:
- game is played on 20 x 20 board instead of 3x3 board
- instead of row of 3, row of 5 is required for win. 

In fact, while traditional tic-tac-toe is 3,3,3 version of [n,m,k-game](https://en.wikipedia.org/wiki/Tic-tac-toe) this variant is 20,20,5 version.

Consequently, there are two players who play against each other and in turns. Players have their own marks and, as usual, `X` represents the first player and `O` the second. Game proceeds as follows:
1. First player places `X` on chosen free tile on board. If game has not ended (3), second player gets the turn (2).
2. Second player places `O` on chosen free tile on board. If game has not ended (3), first player gets the turn (1).
3. Game ends if 
    - first player wins: there are 5 Xs in row. 
    - second player wins:  there are 5 Os in row.
      - 5 in rows can be located horizontally (➡), vertically (⬆) or diagonally (↗ or ↘).
    - it is draw: game board is full without either of players having winning row. 

### Starting the game

When you start the program, first thing you see is the main menu.
From here you can start a new game by clicking the button corresponding the [game mode](#game-modes) of you choice or exit the game:

![menu](https://user-images.githubusercontent.com/47885648/139231798-27780966-a6e2-4f63-a2ae-f72a1ffa764b.gif)


### Game modes
There are four different game modes to choose from:
- `Human vs. human`: two human players play againts each other
- `Human vs. AI`: first player human, second player computer or "AI"
- `AI vs. human`: first player computer, second human
- `AI vs. AI`: both are computer players but human chooses the first move of the first player

### During the game

When it is your (or other human player's) turn, you can simply place your mark on chosen free tile by dragging mouse above it and left-click. If move is valid (i.e. tile is free), game shows transparent mark above the tile on which mark will be placed if left-click is performed. Once placement is done, mark becomes opaque and the other player gets the turn.

![human_vs_human_cropped](https://user-images.githubusercontent.com/47885648/139236072-211599b9-a995-48dd-ab8c-caa4c614f3c6.gif)


Once game is over, no more moves can be placed on board. Winning row is shown (if any) by highlighting the tiles behind marks forming that row.

![gameOver_cropped](https://user-images.githubusercontent.com/47885648/139236621-3c14c104-0c0d-481a-ad2d-c2007cbc497f.png)


### Starting a new game 
If you wish to start a new game with the same game mode, press "New game" during or after the game.

![newGame_cropped](https://user-images.githubusercontent.com/47885648/139239169-f8e49b46-3d3a-45ba-8ba8-c59633151eb2.gif)


If you wish to start a new game but with different game mode, press "Back to main menu" and choose another [game mode](#game-modes) of your choice.

![backToMenu](https://user-images.githubusercontent.com/47885648/139240925-2b66324c-0075-4838-8e28-328843cb44e4.gif)

Or if you have had enough, just click "Exit".

![exit2](https://user-images.githubusercontent.com/47885648/139241326-95998dd9-b0ef-43dc-8161-09c39a0400e3.png)

**Have fun!**
