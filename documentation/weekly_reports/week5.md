# Week 5
Since the core functionalities were already in place, I was able to concentrate on improving the application. As a result there were many fairly small alone but together significant additions to end-users. Changes were made to UI but also to AI:

## UI
- Main menu and introduction of different game modes:
  - "Human vs. human" (human player agains other human)
  - "Human vs. AI" (human player agains AI, human starts)
  - "AI vs. human" (human player against AI, AI starts)
  - "AI vs. AI" (two AIs against each other but human chooses first move)
- Improved visual style and layout
- Show winning row
- Show trasparent mark on tiles on which it can be placed
- Animate AIs moves
- Navigation between menu and game modes

## AI/AlphaBetaMoveChooser
Improved performance and potentially clever moves
- Skip tiles that have no neighbouring marks since usually so called best moves are located next to existing marks on game board
- Introduce adaptive maximum search depth that is dependent on available tiles (Tiles on play area - played moves)

## Game service
- Get winning row if available

## Other things
In addition to these changes, I wrote new tests to among other things validate AI. To do that, I needed to study about mocking further since AI uses static methods of AlphaBetaMoveChooser and mocking such is not done exactly same way as with non-static methods. While also modifying the alpha beta class in question, I started to think if it was wise to use static methods in the first place. (Well, this was because separate TicTacToeNode class included earlier some of the methods now added to this. ) That is, there are bunch of arguments to be passed between each method within the class which may sometimes feel somewhat tedious. Then again, it is transparent what variables each method in question uses.

Documentation was partly updated as usual. Also, made [peer-review](https://github.com/jarkmaen/labyrintin-ratkaisija/issues/1) of other course project which was teaching. As a result, I even got idea about how to animate AI's moves on game board (needed JavaFX Timeline object like reviewed project had used).

## Next
At least:
- Figure out how to make working jar (birefly studied that it is possible with thing called "shadow jar")
- Do remaining needed documents and finalize exising
- More tests
- If more ideas how to improve AI and enough time to implement, do those
- Possible refactoring based on peer-reviews and own observations

Time spent this week: about 15h.

