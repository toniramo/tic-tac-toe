# Week 2

This week I managed to achieve clear progress in developing the game itself. For instance, planned and implemented layered program structure that pursues to separate different function of the program resulting in three packages: ui, domain and dao. This kind of structure was presented in some earlier university course (perhaps "Ohjelmistotekniikka" course) and it felt still reasonable. Though, it would be interresting to see other approaches for this kind of project as well.

UI contains, well as the name suggests, functionalities responsible for user interface. The game logic is included in domain package of which main class is GameService. Dao package is built to create data access object for the layer above.

I decided to keep things simple and keep the game data in memory. Besides, having long-term storage of game data is not in focus for this project. Nonetheless, it is still possible to introduce one later on, thanks to layered design of the program.

I need to admit that it was not always clear how to separate certain tasks between GameService and Dao classes but I pursued to follow logic: GameService analyzes the data and facilitates the game while Dao just stores it. There may still be parts of code to be revised to follow this principle.

Initially it felt somewhat artificial to split such small and so far simple program into several smaller packages (except GUI feels always reasonable to keep separate). I hope it pays off later when more actions are introduced and of course, such planning is good excercise in any case.


In addition to just desinging program structure and implementing game logic, there became something testable for the actual users: program has graphical user interface that takes input from users, change state based on that and inform user when game is over and who won. Something that really helps when developing the AI for the game (most likely next on the table).

As the complexity of the program starts to grow, unit tests are created to ensure the expected quality. Code coverage is already fairly good (excluding UI) but more complex tests could be later implemented and coverage further increased. 

Just recently I also included javaDoc to the code according to assignment. While writing it, I noticed that there is a risk to violate DRY principle (don't repeat yourself) and I am not sure yet how to tackle it efficiently. For instance, what is obvious information clear from the code itself and should not be repeated? Furthermore, what if I now refer to certain name or value and its name later changes, is there double work to update also included additional documentation. Same applies to tests in which I used method names when naming the tests (or test methods to be precise).

Next I start to plan how to implement AI for the game. I could also improve the GUI further e.g. by showing the winning line certain way. Additionally the game logic checking wins could be made more efficient. (Maybe similar is needed when AI tries to find winning moves).

Estimation of time used this week: 15h.

