# Implementation document (draft)

Project is implemented with Java together with Gradle build tool. Graphical user interface utilizes JavaFx platform, and unit testing Junit testing framework with Mockito mocking tools.

## Project structure
Project consists of four main packages:
- UI
- Logic
- Dao
- AI

Logic package holds (quite obviously) functionalities related to logic of the game. Its main class GameService handles and collects necessary information related to the game and passes it between main classes of the packages. Additionally RuleBook that contains essential rules related information, GameBoard, Player and Move classes that all are used to represent state of the game at given moment are part of logic.

Project has package, Dao, to hold data access objects (DAOs) to separate game data from the logic. Package contains so far only one class that implements Dao interface; InMemoryDao which holds the game data in memory. Such separate package for storing the game data may feel somewhat artificial at the moment but this could enable longer term storing of the data if desirable without interfering other packages. (Well, game is simple at the moment but perhaps this could allow introduction of more complex features in the future).

AI (or artificial intelligence) package contains functions to generate AI player. In practice, so called intelligence is simulated by using minimax algorithm with alpha beta pruning and self-made heuristic method to evaluate possible moves and find potentially the most optimal one. 

UI package provides graphical user interface via which communication with human player takes place.

## Implemented time and space complexities

Most complex part of the project in terms of both time and space is the minimax algorithm used to choose the move for AI player. Therefore it is justified to focus complexity analysis on that.

Following parameters affect the complexities:
- Play area: smallest (x,y) - 1  - largest played (x,y) + 1, max n*n where n is width of the game board (number of tiles per edge)
- Number of played moves (/ number of available moves)
- Maximum search depth
- Number of marks in winning row (now 5)

Most exhaustive calculation taking place while algorithm is executed is heuristic value calculation. It analyses given play area in all 4 possible direction: horizontal, vertical and diagonals using moving windows with each lenght equal to marks needed to form winning row (5). There are n rows and n columns to go through and 2n-1 digonals per direction (bottom left to up right / and up left to bottom right \ ).
In total, there are n+n+2*(2*n-1) slices of game board to analyze'(this including now the short diagonals with lenght of 1-4 in which window is out of bounds). Moving window is implemented so that it visits each tile four times coming from all available directions once. (Notice that it could be improved a bit to avoid diagonals where maximum lenght is between 1-4). 

As a result, 4\*n\*m (n: width of play area, m: height) tile visits are made heuristic calculation. Given that there are n\*m-p (p: played moves), and max search depth d_max is set, up to
approximately 4\*n\*m\*(n\*m-p)^(d_max+1) visits are made during each turn in case any pruning does not take place (and exluding possible changes to play area and number of available moves). So for instance n=10, m=10, p=20 and d_max = 2 yields up to 3.2768e+13 visits. It is possible that some moves result to win so the number is likely smaller if p >= 8. Winning move in turn is checked around the latest move within range of 4 tiles from the one move was placed on. Again check is performed for all 4 directions resulting in 9*4=36 tile visits in total per check.

## Possible flaws and improvements (some may still be implementable during last weeks of the project)
- User interface waits for AI to finish move before updating its state. This means that move that changed turn to AI is not shown on UI until also AI's move is registered. Not problem if loading time of move is fast but with e.g. deeper searching this affects user experience.
- UI to show winning row
- Menu to UI to have option to select different game configurations (human vs AI, AI vs. human, AI vs. AI, human vs. human).
- Improve alpha beta pruning algorithm further, e.g. by utilizing iterative deepening and/or changing the order of nodes to increase likelyhood of finding the most valuable node early on - for instance starting from moves closest to the latest moves.
- Take into account human element in game: it is possible that opponent does not notice already achieved victory (e.g. | |X|X|X|X| |). In such case AI may just ''give up'' i.e. choose last of the observed moves since all are equally bad based on algorithm.

## Sources
- Same as in project specification (add here)
- Introduction to Artificial intelligence course material, [part 2h](https://materiaalit.github.io/intro-to-ai/part2/)

Notes (to be removed):
Implementation document should contain:

>   - Project structure
>   - Implemented time and space complexities (big-O complexity analysis of (pseudo)code)
>   - Comparative performance and complexity analysis if applicable
>   - Possible flaws and improvements
>   - Sources
