# Project specification

Aim of the project is to provide variant of tic-tac-toe with an AI player. Game should be played on larger than usual 3x3 sized board, let's say between 10x10 and 20x20. Additionally, the number of Xs or Os in a row should be 5 instead of traditional 3. Rows can be arranged vertically, horizontally or diagonally. AI should be competitive (this is somewhat subjective, at least programmer himself should be beaten) but also perform reasonably fast keeping the gaming experience satisfying for the human player (i.e. deciding the move for up to couple of seconds may still be acceptable). 

## Algorithms and data structures used

Program should be able to generate square game board (with n times n cells) and keep on track the status of each cell. Status could be one of the three: i) free (empty), ii) taken by X (or just "X"), iii) taken by O (or just "O"). Two dimensional arrays (or array of arrays) could be used for this.

Program needs to know whose turn (player 1 or player 2) it is at any time.

Every time, after either of the players have placed own move, game should check if the game is still ongoing and pass the turn to the other player or end the game if latest turn have lead to win. Thereby, status of adjacent cells should be checked after every move to see if there are 5 Xs or Os in a row (either vertically, horizontally or diagonally).

This would be enough for two human players but, since we want to have an competent AI to play againts, some algorithm should be used to make that possible. ![Minimax](https://en.wikipedia.org/wiki/Minimax) is fairly simple and commonly used algorithm used in these kind of board games. However, it can be potentially improved (in terms of performance) by enhancing it with ![alpha-beta pruning](https://en.wikipedia.org/wiki/Alpha%E2%80%93beta_pruning). There is more information about performance with the algorithm in question [below](#expected-time-and-space-complexities-of-the-program). We will see that even with the game simple as the one in question, the total number of all possible outcomes grows too large to calculate after every move. Hence, to achieve satisfying level of performance, some further streamlining should be made. We can do this by limiting the search depth of possible moves and use chosen heuristic to indicate the likelyhood of winning with the observed outcome if winning move is not found with the depth used.

## Program input and how will it be used

Main input for the program during the actual game is moves made human player. That is, in which cell will the user place X or O (depending on the starting order). Program stores this information (in two dimensional array) and checks if player has win or not. If not, turn is passed to another player (human or AI) and game continues, otherwise game ends. 

Before the game starts, user should be able to choose is he/she playing against another human player or against AI. If game is played against AI, user chooses to start or let AI be the first (X). Program could be improved later to let user(s) to choose the number of rounds to be played and count the number of wins.

## Expected time and space complexities of the program

[Game trees](https://en.wikipedia.org/wiki/Game_tree) (or search trees) can be used to represent possible states within game in question and to study game complexity. Below simplefied example game tree of basic 3x3 tic-tac-toe. It does not show all possible stages but hopefully gives the reader basic idea of the consept. This example was inspired by picture at https://en.wikipedia.org/wiki/Game_tree#/media/File:Tic-tac-toe-game-tree.svg.

```
                                |   |
Root node                    ---+---+---
(empty board)                   |   |
                             ---+---+---
                                |   |

                           /      |       \
Branches                 1        2   ...  9                     <--- Initially 9 possible (legal) moves                   
(possible moves)       /          |          \


Nodes          X |   |            | X |             |   |       
Ply 1:        ---+---+---      ---+---+---       ---+---+---
Player 1 (X)     |   |            |   |     ...     |   |
              ---+---+---      ---+---+---       ---+---+---
                 |   |            |   |             |   | X

               /   |...\        /   |   \          /   |...\    
              #    #    #      1    2 ...8        #    #    #    <--- Next player now has 8 possible moves
                             /      |      \                          

                     | O |         |   | O     X |   |       
Ply 2:            ---+---+---   ---+---+---   ---+---+---
Player 2 (O)  ...    | X |         | X |   ...   | X |    ...
                  ---+---+---   ---+---+---   ---+---+---
                     |   |         |   |         |   | O

                   /   |...\     /   |...\     /   |...\     
                  #    #    #   #    #    #   #    #    #       <--- 7 possible moves
(Plies 3..4)                     /   |...\
                                #    #    #                     <--- 6 possible moves
                                     |
                                X  | O | O       
Ply 5:                          ---+---+---  
Player 1 (X)        ...            | X |          ...           <--- No more moves in this outcome
                                ---+---+---   
                                   |   | X       
                                  
                Player 1 (X) wins. Game ends. This is leaf node.

```

According to [Wikipedia article about alpha-beta pruning](https://en.wikipedia.org/wiki/Alpha%E2%80%93beta_pruning) (accessed 10.9.2021), when using simple minimax search, the maximum number of leaf node positions evaluated (or time complexity) is ![O(b^d)](https://latex.codecogs.com/svg.latex?O%28b%5Ed%29) where _b_ is average ![branching factor](https://en.wikipedia.org/wiki/Branching_factor) and _d_ search depth. This is the same with alpha-beta pruning in the worst case when none of the nodes are skipped. In the optimal case, when best moves are always searched first, the number of leaf node positions evualuated is about ![O(b^(d/2)) = O(sqrt(b^d))](https://latex.codecogs.com/svg.latex?%5Cinline%20O%28b%5E%7B%5Cfrac%7Bd%7D%7B2%7D%7D%29%20%3D%20O%28%5Csqrt%7Bb%5Ed%7D%29). 

Space complexity for minimax and alpha-beta pruning is O(_b_ \* _d_).

In this case, branching factor _b_ depends on the game board size that should be, according to specification, large (i.e. much larger than usual 3x3). If we have game board of _n_x_n_ cells, maximum number of possible moves in the beginning is _n_<sup>2</sup>. During the next ply the equivalent number is _n_<sup>2</sup>-1 then _n_<sup>2</sup>-2, then _n_<sup>2</sup>-3, and so on. If we assume that the game board can be filled entirely with Xs and Os before either of the players win (theories about [m,n,k-games](https://en.wikipedia.org/wiki/M,n,k-game) could provide more information about this), there is only one option for the last possible move. Thereby number of branches per ply ranges from 1 to _n_<sup>2</sup>. If we exclude nodes thet lead to win before game board is full, average number of branches per node is ![\frac{\sum_{i=1}^{n^2} i}{n^2} = \frac{n^2(1+n^2)}{2}/n^2 = \frac{1+n^2}{2}](https://latex.codecogs.com/svg.latex?%5Cinline%20%5Cfrac%7B%5Csum_%7Bi%3D1%7D%5E%7Bn%5E2%7D%20i%7D%7Bn%5E2%7D%20%3D%20%5Cfrac%7Bn%5E2%281&plus;n%5E2%29%7D%7B2%7D/n%5E2%20%3D%20%5Cfrac%7B1&plus;n%5E2%7D%7B2%7D). As a result, we can estime that in full game tree, containing all possible legal outcomes, average number of branches per node must be something between (1+n²)/2 and n², so (1+n²)/2<b<n². As an example, game with 10x10 sized board has then 50.5<b<100. 

Search depth can range, depending on the number of the ply in question and when the winning move is found, from 1 up to n^2. If not yet evident, at the latest now the vast complexity of the game tree is revealing (consider e.g. with b=d=50, then b^d=50^(50)≈8.9e+84). It is certain that game ends at the earliest after 9th ply (the first player gets 5 in row as soon as possible) but using even such depth may be too exhausting (50⁹≈1.95e+15) and in the actual implementation search depth should be limited below that. The remaining uncertainty should be compensated with reasonable heuristic that helps the AI to estimate the likelyhood of the winning even if winning move is not found. Additionaly, search depth could be used as one option to adjust the competitiveness of the AI - at least make it easier if needed by limiting it enough, closer to 1.

## Sources

- https://en.wikipedia.org/wiki/Minimax
- https://en.wikipedia.org/wiki/Alpha%E2%80%93beta_pruning
- https://en.wikipedia.org/wiki/Game_complexity
- https://en.wikipedia.org/wiki/Branching_factor
- https://en.wikipedia.org/wiki/M,n,k-game
- http://www.cs.umd.edu/~hajiagha/474GT15/Lecture12122013.pdf
- https://cis.temple.edu/~vasilis/Courses/CIS603/Lectures/l7.html
- https://www.youtube.com/watch?v=l-hh51ncgDI
- https://www.youtube.com/watch?v=STjW3eH0Cik

## Other information

**Degree programme**: Bachelor’s in computer science (CS)

**Documentation language**: English
