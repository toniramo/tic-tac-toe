# User guide

## Command-line commands
**Note:** Instructions expect you to run commands from `<project root>/tic-tac-toe`.

### Building project
Build runnable jar-file:
```
./gradlew build
```
- This runs Gradle build task which includes also checkstyle checks and unit tests and if all checks and tests are passing, `tictactoe.jar` is generated to `./tic-tac-toe/build/libs`

### Running project

When you have generated `tictactoe.jar` according to instruction above, you can run it using command:
```
java -jar ./build/libs/tictactoe.jar
```

Alternatively you can run project with Gradle as follows:
```
./gradlew run
```
### Checks, tests and reports
Run both checkstyle and unit tests:
```
./tictactoe/gradlew check
```
   - Output reports for checkstyle, `main.html` and `test.html`, will be found from `./build/reports/checkstyle`.
   - Jacoco test report is generated in `./build/reports/jacoco/test/html`. You can see main page of the report by opening `index.html`.

Run AI performance test:
```
./gradlew performanceTest
```
- Notice that this may take up to 1.5 - 2h to finish. 
- Output data will be printed in `./build/reports/tests/performanceTest/data/perfTest_<system time in milliseconds>.txt`
### Cleaning outputs
In order to delete contents of `build` directory, run
```
./gradlew clean
```
### For more information 
about Gradle command-line interface see https://docs.gradle.org/current/userguide/command_line_interface.html.
