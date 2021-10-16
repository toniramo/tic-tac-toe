# Testing document (draft)

## Maintaing quality 
Unit level testing is carried out with JUnit and partially supported with Mockito library to mock other classes when appropriate. 

More information about testing coverage can be found (almost in real time) from Codecov, navigate via this badge:

> [![codecov](https://codecov.io/gh/toniramo/tic-tac-toe/branch/main/graph/badge.svg?token=08l4tRIjI8)](https://codecov.io/gh/toniramo/tic-tac-toe)


Tests are run and badge is updated every time new commit is made and pushed to Github based on jacoco test report.

Focus in automated testing development has been in the most important and complex classes, exluding the graphical user interface that is tested manually.

Additionally quality has been monitored with continuous CI with Gradle workflow (thanks to which actually also the Codecov is updated). It updates the CI status not alone based on test results but also other quality factors like compliance with configured checkstyle rules. See status by monitoring this badge: 
> ![Gradle workflow](https://github.com/toniramo/tic-tac-toe/actions/workflows/gradle.yml/badge.svg)

## Validating AI
The success of AI / minimax algoritm that implements it, is evaluated both manually and in automated manner. 

Manual testing takes place via UI with game modes "Human vs. AI" (human player starts), "AI vs. human" (AI starts), and "AI vs. AI" (actually partially automated since AIs play against each other) as AIs moves are observed, and in case of first two modes, also challenged by human. Higher level, though somewhat vague, criteria for the first two modes is that AI should provide considerable challenge for human player. More precicely, this means for all game modes that it should (regardless playing against human or other AI) choose obvious winning move if available or counter others winning move if possible. Of course, eventually it may loose while game board becomes more complex as the algorithm is not able to calculate whole game tree and optimal move is partly evaluated by self-made [heuristic](https://github.com/toniramo/tic-tac-toe/blob/951a5f7fa3ccbc18bea8dac81f3d9b42b89210a0/tic-tac-toe/src/main/java/tictactoe/ai/AlphaBetaMoveChooser.java#L326) that is just a simple _estimate_ about possible value of observed move (or game tree node). Another criteria during the actual game play relates to performance; it should not take too long for AI to choose the move. Yet again, it is subjective what is "too long" but for sure no more than couple of seconds.

Automated validation is performed with JUnit automated tests found in [src/test/java/tictactoe/ai](https://github.com/toniramo/tic-tac-toe/tree/main/tic-tac-toe/src/test/java/tictactoe/ai). Cases ensure that AI, depending on case, either chooses winning move or counters others. Cases include various starting locations and row directions.

## Performance of AI

Performance of AI is tested with [AIPerformanceTest.java](https://github.com/toniramo/tic-tac-toe/blob/main/tic-tac-toe/src/test/java/tictactoe/ai/AIPerformanceTest.java). It is excluded from regular test cycle and should be executed separately with command line:
```sh
./gradlew performanceTest
```
During the test, AI vs. AI games from all possible starting positions are played and certain key measurements are logged in resulting test file `./tictactoe/build/reports/tests/performanceTest/data/perfTest_*txt`.

Notice that it takes up to **1,5 - 2 h** to run the test.

Based on [run on 15.10.2021](./test_data/performance_test_20211015.txt) (see graph below) most of the moves are found within acceptable 2 seconds. In fact, average move evaluation time was in most of the games well below 1 second. Although, occasional peaks of even up to 10 seconds (one observation even over 20 seconds) where seen, but relatively ralely compared to the size of whole sample set. This indicates that the search depths are reasonable, although some tweaking could be done for thershold between max search depths 2 and 3. 

Pay attention also how the extremes of the sample set start to spread exponentially when approaching the treshold of each search depth (most noticable in case of 3). Though, as the average search times remain way below the maximum values, we can presume that the alpha-beta pruning is done succesfully. Otherwise there would be equally many nodes for given number of free tiles searched every time and thus observed search times would approach the observed extremes.

![result](./test_data/performance_test_20211015.svg)
👆Visualization of test data: x-axis shows number of free tiles on play area, left y-axis together with red dots time to find move (in seconds), and right y-axis with grey dots used search depth

It is hard to evaluate goodness of two AIs that are set to play against each other but at least some measures combined with certain assumptions could be used to estimate it.

For instance, we can assume that first player should win most of the cases. Used heuristic is not perfect and certain starting positions may not be the most optimal for winning (like corner 1,1) so it is likely that occasionally first player looses. Based on test results, first AI wins 258 out of 400 or up to 64,5% of the cases. This indicates that the AI is most often able to take the benefit of potential winning positions and avoid most absurd moves.

Another measure of goodness, or at least reference point for future development, could be number of moves per game if we assume that the length of the game depends on the competence of the players (although, even equally bad players could make the game last long if winning moves are not utilized properly). In this run average game length (in terms of moves) was 22.575. Shortest game needed 9 moves (that is, first player got 5 in row as soon as possible) and longest took up to 60 moves.

**Notes**

According to [instructions](https://tiralabra.github.io/2021_p1/en/documentation/), document should include:
> - What has been tested and how
> - What types of input were used (especially important for comparative analysis)
> - How can the tests be repeated
> - Results of empirical testing presented in graphical form
> - Tests should ideally be a runnable program. This makes repeating the tests easy
> - For Java it is recommended to do unit testing with JUnit

