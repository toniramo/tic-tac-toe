# Implementation document

Project is implemented with [Java](https://dev.java/) (v11.0.11) language together with [Gradle build tool](https://gradle.org/) (v5.1.1) . Graphical user interface utilizes [JavaFX platform](https://openjfx.io/) (v.11.0.2), and unit testing [Junit testing framework](https://junit.org/junit4/) (v.4.13) with [Mockito framework](https://site.mockito.org/) (v3.+).

You can see more comprehensive depenency list with Gradle (run from `<project root>/tictactoe`):
```
./gradlew dependencies
```

## Project structure
Project consists of four main packages (1) `logic`, (2) `dao`, (3) `ai` and  (4)`ui`.

(1) `logic` package holds (quite obviously) functionalities related to logic of the game. Its main class `GameService` handles and collects necessary information related to the game and passes it between main classes of the packages. Additionally `RuleBook` that contains essential rules related information, `GameBoard`, `Player` and `Move` classes that all are used to represent state of the game at given moment are part of logic.

(2) Project has package `dao` to hold data access objects (DAOs) to separate game data from the logic. Package contains so far only one class that implements `Dao` interface; `InMemoryDao` which holds the game data in memory. Such separate package for storing the game data may feel somewhat artificial at the moment but this could enable longer term storing of the data if desirable without interfering other packages. (Well, game is simple at the moment but perhaps this could allow introduction of more complex features in the future).

(3) `ai` (comes from artificial intelligence, AI) package contains functions to generate `AI` player. In practice, so called intelligence is simulated by using minimax algorithm with alpha beta pruning and self-made heuristic methods to evaluate possible moves and find potentially the most optimal one. Actual evualuation is performed by `AlphaBetaMoveChooser`. 
<sub>Notice that package contains deprecated classes `TicTacToeNode` and `GameTreeNodePruner` and interface `GameTreeNode` (which `TicTacToeNode` implements). These were designed to analyze the game tree at given state of the game but were deprecated due to performance concerns; Java is usually faster to perform operations with primitive data structures compared to structures consiting of custom made objects. Concequently, AI maintains the state of the game in primitive integer (`int`) array instead of using `GameTreeNode`s.</sub>


(4) `ui` package provides graphical user interface, `GUI`, via which user can choose game modes and play the game. `GUI` keeps track of `AI` players and requests `AI`(s) to choose move when the game requires, otherwise it propmts move from actual user.  

Relationships between classes are illustrated in class diagram below:

![Class diagram](./images/class_diagram.svg)

**Figure 1.** Simplefied class diagram of implementation to show relationships between classes. (It excludes deprecated classes and any class specific information about object variables and methods and depricated classes).

## Implemented time and space complexities

Most complex part of the project in terms of both time and space is the minimax algorithm that is realized by `AlphaBetaMoveChooser` and used to choose the move for AI player. Therefore it is justified to focus complexity analysis on that.

When `AI` calls `getMoveWithOptimizedSearchDepth` of `AlphaBetaMoveChooser` following happens:

`optimizedMaxSearchDepth` is called. Since it only compares the given parameters with predefined values time complexity is constant O(1).
Next `getMove` with bunch of parameters is called. It has two nested for loops:
```java
for (int x = playArea[0]; x <= playArea[2]; x++) {
  for (int y = playArea[1]; y <= playArea[3]; y++) {
```
In practice, code iterates through x and y coordinates within given play area coordinates. Therefore, number of tiles on play area or coordinates to go through is
d_x_\*d_y_ = _n_<sub>play area</sub>. 

However, invalid locations are skipped before any further operations are performed:
```java
if (!validMove(node, playArea, playedMoves, x, y)) {
  continue;
}
```
More precisely locations that are not on board and such tiles that are not free but also tiles that do not have any neighbouring tile reserved after first move are all skipped:
```java
private static boolean validMove(int[][] node, int[] playArea, int playedMoves, int x, int y) {
  return coordinateOnBoard(node, x, y) //this may not in fact lead to any skips in fact as playArea should be already within the range
          && tileFree(node, x, y)
          && (atLeastOneNeighbourReserved(node, playArea, x, y) && playedMoves > 0);
    }
```
As a result, we can reduce the number of tiles that are not next to reserved ones _n_<sub>outliers</sub> and the  number of reserved tiles, _n_<sub>played</sub>, from _n_<sub>play area</sub>. Hence time complexity depends on input as follows: _n_<sub>play area</sub> - _n_<sub>outliers</sub> - _n_<sub>played</sub>.

Next actual minimax related operations start. We assing new value in given coordinate x,y to `node`, call `value` method and revert state to previous:
```java
node[x][y] = turn;
int alphaBetaValue = value(node, playArea, new int[]{x, y},
  playedMoves + 1, (turn + 1) % 2, rowLenght, alpha, beta, 0, maxSearchDepth);
node[x][y] = -1;
```
Notice that this enables us to use single `node` array instead of copying it reducing the space complexity of the program. Space needed so far is thus dependent on total number of tiles on board, _n_<sub>board size</sub><sup>2</sup>, where _n_<sub>board size</sub> denotes number of tiles on game board. Furthermore, copying arrays is costly operation on time perspective as well so any additions to time complexity are also avoided.

`value` method starts by checking if the observed state is end state:
```java
int maxValue = (int) 1e9 - nodeDepth;
if (lastMoveWinning(node, move, rowLenght)) {
  return (turn + 1) % 2 == 0 ? maxValue : -maxValue;
}
if (gameBoardFull(node, playedMoves)) {
  return 0;
}
if (nodeDepth >= maxSearchDepth) {
  return heuristicBasedOnPlayArea(node, playArea, (turn + 1 % 2), rowLenght);
}
```
Running `lastMoveWinning` checks if there are _k_ marks in row (`rowLenght`) within _k_-1 radius from the location of last move. Thus it only depends on the used _k_ during the game.

`gameBoardFull` compares number of tiles on board,_n_<sub>board size</sub><sup>2</sup>, with number of played moves _n_<sub>played</sub> so the complexity is not affected by its input.

If maximum search depth _d_<sub>max</sub> is reached, `heuristicBasedOnPlayArea` is called. This is by far more complex method than two previous.
It performs 4 nested for loops and assigns 7 temporary variables.
```java
int n = node.length - 1;
int[] playerValues = new int[2];
for (int i = 1; i <= n; i++) {
  int[][][] counters = new int[2][6][rowLenght + 1];
  int offset = 0;
  int[][] marksOnRange = new int[2][n];
  for (int j = 1; j <= n; j++) {
    int[] x = new int[]{i, j, (i + offset), (i + offset - n), j, j};
    int[] y = new int[]{j, i, j, j, i - offset, i - offset + n};
      for (int k = 0; k < x.length; k++) {
        if (x[k] < 1 || x[k] > n || y[k] < 1 || y[k] > n
            || x[k] < playArea[0] - 4 || x[k] > playArea[2] + 4
            || y[k] < playArea[1] - 4 || y[k] > playArea[3] + 4) {
          continue;
        }
        for (int p = 0; p < 2; p++) {
```
First two loops depend on _n_<sub>board size</sub> so this yields to time complexity of O(_n_<sub>board size</sub><sup>2</sup>).

<details>
  <summary> temporary</summary>
Following parameters affect the complexities:
- `int[] playArea`: area within which all marks are located on game board. In early game this is usually much smaller than actual game board limiting the number of free tiles (i.e. observed child nodes).
- `int numberOfPlayedMoves`: Number of played moves, used to count the number of free tiles on play area (i.e. number of child nodes)
- `int maximumSearchDepth`: Maximum search depth, if the depth of observed node is equal to this, no more searches from this node are done. Depth is determined by number of free tiles on play area, n 4: n < 9; 3: 9 <= n < 37, 2: 37 <= n < 77, 1: 77 <= n)
- `int rowLenght`: Number of marks needed to form a winning row (in this case 5.

For each possible move within playArea:
  Check that there is mark next to it wihtin radius of 1 tile. If not, skip.
  Calculate its value based on childs within depth of maximumSearchDepth.

Most exhaustive calculation taking place while algorithm is executed is heuristic value calculation. It analyses given play area in all 4 possible direction: horizontal, vertical and diagonals using moving windows with each lenght equal to marks needed to form winning row (5). There are n rows and n columns to go through and 2n-1 digonals per direction (bottom left to up right / and up left to bottom right \ ).
In total, there are n+n+2*(2*n-1) slices of game board to analyze'(this including now the short diagonals with lenght of 1-4 in which window is out of bounds). Moving window is implemented so that it visits each tile four times coming from all available directions once. (Notice that it could be improved a bit to avoid diagonals where maximum lenght is between 1-4). 

As a result, 4\*n\*m (n: width of play area, m: height) tile visits are made heuristic calculation. Given that there are n\*m-p (p: played moves), and max search depth d_max is set, up to
approximately 4\*n\*m\*(n\*m-p)^(d_max+1) visits are made during each turn in case any pruning does not take place (and exluding possible changes to play area and number of available moves). So for instance n=10, m=10, p=20 and d_max = 2 yields up to 3.2768e+13 visits. It is possible that some moves result to win so the number is likely smaller if p >= 8. Winning move in turn is checked around the latest move within range of 4 tiles from the one move was placed on. Again check is performed for all 4 directions resulting in 9\*4=36 tile visits in total per check. 
</details>

## Possible flaws and ideas for improvements
- Improve alpha beta pruning algorithm further, e.g. by utilizing iterative deepening and/or changing the order of nodes to increase likelyhood of finding the most valuable node early on - for instance starting from moves closest to the latest moves.
- Take into account human element in game: it is possible that opponent does not notice already achieved victory (e.g. | |X|X|X|X| |). In such case AI may just ''give up'' i.e. choose last of the observed moves since all are equally bad based on algorithm.

## Sources
- https://en.wikipedia.org/wiki/Minimax
- https://en.wikipedia.org/wiki/Alpha%E2%80%93beta_pruning
- https://en.wikipedia.org/wiki/Branching_factor
- https://en.wikipedia.org/wiki/M,n,k-game
- http://www.cs.umd.edu/~hajiagha/474GT15/Lecture12122013.pdf
- https://cis.temple.edu/~vasilis/Courses/CIS603/Lectures/l7.html
- Introduction to Artificial intelligence course material, [part 2](https://materiaalit.github.io/intro-to-ai/part2/)
- StackOverflow with error cases.
