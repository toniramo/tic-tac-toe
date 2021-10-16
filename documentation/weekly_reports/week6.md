# Week 6
## Focus in testing
Focus of this week was mostly on increasing test coverage. This is evident when checking the test coverage which is now close to 90% line coverage. Now more complex cases of AI should be covered. Again, just like last week, I encountered difficulties with creating test for a class that is using static methods of another class (i.e. AI -> AlphaBetaMoveChooser). This time it related to verifying arguments passed to mocked static method. Luckily a workaround was found, although I am pretty sure test could have been implemented in another, perhaps more elegant way as well. Nonetheless, it works.

In addition to creating tests, I wanted to exlude certain, deprecated classes or methods out of coverage. This needed studying. I learned that adding annotation which includes `Generated` can be used for that (https://stackoverflow.com/a/66918619). I am not sure what is proper place (i.e. package) to store custom made annotations but I decided to create unsuprisingly "annotations" package for that.

New tests included also performance test for testing the AI. You can see more details about that in [Performance test chapter](https://github.com/toniramo/tic-tac-toe/blob/main/documentation/testing_document.md#performance-of-ai) of testing document.

## Refactoring
Even though UI is not part of automated tests or checkstyle, it was refactored to make it at least somewhat easier to read and maintain.

## Peer reviews and jar
According to [peer-review](https://github.com/toniramo/tic-tac-toe/issues/2) received last week, there was difficulties to run the project. I decided to make a [pre-release](https://github.com/toniramo/tic-tac-toe/releases) with a runnable Jar file. Even if it is not used by other peers, this was good exercise for the final release as it was not as straight forward as I initially expected. For instance, jar generated with default configuration did not include needed code of used external libraries like JavaFx. This resulted in problems when trying to run the file. Luckily this was easily fixed after all by adding correct [jar configuration](https://github.com/toniramo/tic-tac-toe/blob/5faa89ed1cf9fbb44509ea9c3e223a881e46fe50/tic-tac-toe/build.gradle#L30) in build.gradle file. Additionally, [background picture](https://github.com/toniramo/tic-tac-toe/blob/main/tic-tac-toe/src/main/resources/background2.png) used in main menu of the program was not initially included but after changing the method via which the picture was read by the program and the location of the picture to resources folder, it was bundled as part of jar.

Of course, I made another [peer-review](https://github.com/ilkkaluu/tiralabra/issues/2) myself this week according to the assignment. It was once again interresting to see other project and read other persons code. Hopefully this resulted in some useful tips for the peer developer.

## Next
Start finalizing the project documentation and ensure project code is satisfying for final release.


Time spent this week: around 12h.
