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
- The success of AI / minimax algoritm that implements it, is evaluated both manually and in automated manner. Manual testing takes place via UI with game modes "Human vs. AI" (human player starts), "AI vs. human" (AI starts), and "AI vs. AI" (actually partially automated since AIs play against each other) as AIs moves are observed, and in case of first two modes, also challenged by human. Higher level, though somewhat vague, criteria for the first two modes is that AI should provide considerable challenge for human player. More precicely, this means for all game modes, that it should (regardless playing against human or other AI) choose obvious winning move if available or counter others winning move if possible. Of course, eventually it may loose while game board becames more complex as the algorithm is not able to calculate whole game tree and optimal move is partly evaluated by self-made [heuristic](https://github.com/toniramo/tic-tac-toe/blob/951a5f7fa3ccbc18bea8dac81f3d9b42b89210a0/tic-tac-toe/src/main/java/tictactoe/ai/AlphaBetaMoveChooser.java#L326).

Automated validation is performed with JUnit automated tests found in [src/test/java/tictactoe/ai](https://github.com/toniramo/tic-tac-toe/tree/main/tic-tac-toe/src/test/java/tictactoe/ai). Cases ensure that AI, depending on case, either chooses winning move or counters others. Cases include various starting locations and row directions.

**Temporary notes (to be removed):**
Earlier ideas and thoughts about testing:
- At least alpha beta pruning should be able to work so that obvious winning cases are observed by AI - and in general at least most bizarre moves should be avoided by AI and by so make it a reasonable opponent. 
- It could be interresting to study number of nodes visited with and without alpha beta pruning and perhaps performance as well
- With programs like this, it may be hard to come up with repeatable tests (unless very close to end state defined states with limited childs and obvious good choises are set up before the test). At least subjective test can be made by playing against the AI on the user interface. One option could be to make two AIs playing against each other and see what happens (repeat multiple times and collect some statistic).

According to [instructions](https://tiralabra.github.io/2021_p1/en/documentation/), document should include:
> - What has been tested and how
> - What types of input were used (especially important for comparative analysis)
> - How can the tests be repeated
> - Results of empirical testing presented in graphical form
> - Tests should ideally be a runnable program. This makes repeating the tests easy
> - For Java it is recommended to do unit testing with JUnit

