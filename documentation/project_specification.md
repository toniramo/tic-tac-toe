# Project specification

_What data structures and algorithms will you be using_

- Data structures: Arrays to create game board and store moves in x,y coordinates. 
- Algorithms: Minimax with alpha-beta-pruning to generate the AI.

_What problem are you solving and why did you chose these specific data structures and algorithms_

- Problem: How to store moves on game board of certain size. 
  - Solution: This could be done with arrays by forming x,y coodinate system. Each cell in the game board can should be able to store three stages, one at a time: i) free, ii) taken by X, iii) taken by O. Status can be changed only if the cell is free.

- Problem: Detect if there are either Xs or Os 5 in row.
  - Solution: 

- Problem: Make competent and reasonably fast AI to play against human player
  - Solution: Minimax algorithm as it seems to be simple enough to understand and implement. It could be made more efficient with alpha-beta-pruning. Search depth may need to be limited to keep performance reasonable and, as a result, algorithm should be supported with certain heuristic that can suggest the move which most likely leads to win (if winning move is not found with the available search depth used).

_What is the program input and how will it be used_

Input will be generated by user and an AI.
Before the game starts, user is able to tell the program is he/she playing as X or O. Program then starts the program accordingly; either user or AI starts as X.
During the game, user, just like AI, inputs the chosen move on the game board. Program checks if the move is valid (or ensures that illegal moves cannot be made in the first place), stores the move and checks if player who made the move has won, i.e. there are 5 Xs or Os in row. If not, game continues. Otherwise it ends and user is directed back to beginning. It is possible to improve program to have option keep playing for chosen amount of rounds and keep count of wins of each player. 

_Expected time and space complexities of the program (big-O notations)_

When using simple minimax search, the maximum number of leaf node positions evaluated is O(b^d) where b is average branching factor and d search depth. This is the same with worst-case alpha-beta pruning (when none of the nodes are skipped). In the optimal case, when best moves are always searched first, the number of leaf node positions evualuated is about O(b^(d/2) = O(sqrt(b^d)).

_Sources_
- https://en.wikipedia.org/wiki/Minimax
- https://en.wikipedia.org/wiki/Alpha%E2%80%93beta_pruning
- https://cis.temple.edu/~vasilis/Courses/CIS603/Lectures/l7.html
- https://www.youtube.com/watch?v=l-hh51ncgDI
- https://www.youtube.com/watch?v=STjW3eH0Cik

_Degree programme_
Bachelor’s in computer science (CS)

_Documentation language_
English

## Temporary notes for this section

According to https://tiralabra.github.io/2021_p1/en/documentation/ specification should contain:
> What data structures and algorithms will you be using
>
> What problem are you solving and why did you chose these specific data structures and algorithms
> 
> What is the program input and how will it be used
> 
> Expected time and space complexities of the program (big-O notations)
> 
> Sources
> 
> Due to administrative practicalities you should also mention your degree programme in the Project Specification. For example, bachelor’s in computer science (CS) or bachelor’s in science (bSc)
> 
> You should also mention the documentation language you are going to use and have all code, comments and documentation written in this language. Typically Finnish or English. This requirement is due to the code reviews done around the half way point of the course. This hopefully helps keep the internal language of the project consistent