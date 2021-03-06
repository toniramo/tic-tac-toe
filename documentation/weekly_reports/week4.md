# Week 4
Week started with investigating and refactoring the existing implementation. Performance of implemented minimax algorithm was tested manually. Time to find optimal move was somewhat disappointing even with depth of 2 or more. After dicussing with course teacher it became evident that using objects in arrays and copying array for each game tree node (!) was most likely one big bottle neck for the performance of the AI player. As a result, I needed  to create separate data structure for AI class that utilizes only integers - not any custom made objects. This resulted in somewhat copying the code from the other classes as it was not possible to use e.g. existing static method to check win as such any more. Actually, also winning check method needed improving in any case since it was not necessary to study whole game board every time one move was made - just around the latest move. This together with other introduced improvements should have had notable effect on performance.

While trying to find ways to increase performance of the AI, I also studied ways to improve already done heuristic calculation for exising TicTacToeNode class. I came up with alternative that is based on the last move, but decided to so far go with the earlier version. At least work done for these Node objects was not complitely wasted. Though, while being better than earlier, perhaps that could stil be further improved since even with just maximum search depth of 2, it can take considerable time (tens of seconds) as the play area increases enough. Initially with small area search could be even deeper (perhaps case for iterative deepening?). Though, even with just depth 1 AI is fairly good - at least won my family member who was used as guinea pig. I am able to trick it still, but I guess I know the heuristic too well (or planning it have made me find winning strategies easier). Discussion about improvement topics and implementation was introduced in [implementation_document.md](./implementation_document.md).

Additionally, AI class using updated algorithm class was implemented and UI was updated to support AI player. Furthermore, tests were updated and more developed for changed features. While it is fairly easy to see manually if AI is playing well, more automated tests could be still developed during coming weeks.

Estimation of time spent this week: 20+ h.
