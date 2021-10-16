package tictactoe.ai;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javafx.scene.paint.Color;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import tictactoe.dao.InMemoryDao;
import tictactoe.logic.GameService;
import tictactoe.logic.Move;
import tictactoe.logic.Player;
import tictactoe.logic.Player.PlayerType;
import tictactoe.logic.RuleBook;

/**
 * Performance tests for AI class.
 */
public class AIPerformanceTest {

    private RuleBook rules;
    private GameService service;
    private AI[] ais;

    @Before
    public void setUp() {
        Player ai1 = new Player("X", Color.RED, PlayerType.AI);
        Player ai2 = new Player("O", Color.BLUE, PlayerType.AI);
        rules = new RuleBook(20, 5, new Player[]{ai1, ai2});
        service = new GameService(new InMemoryDao());

    }

    @After
    public void tearDown() {
    }

    /**
     * Test plays AI vs AI games from all possible starting positions and logs
     * the data to ./build/reports/tests/performanceTest/data/perfTest_*_.txt.
     * Notice that it takes up to 2h to run the test! This is excluded from
     * regular test cycle and run with a separate task.
     */
    @Test
    public void aiVsAiFromAllStartingPositions() {
        boolean testSuccess;

        String fileName = "perfTest_" + System.currentTimeMillis() + ".txt";
        String path = "build/reports/tests/performanceTest/data";

        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        try (FileWriter writer = new FileWriter(path + "/" + fileName)) {
            double testStart = System.nanoTime();
            writeHeader(writer);
            for (int x0 = 1; x0 <= 20; x0++) {
                for (int y0 = 1; y0 <= 20; y0++) {
                    play(x0, y0, testStart, writer);
                }
            }
            testSuccess = true;
        } catch (IOException e) {
            testSuccess = false;
        }
        assertTrue(testSuccess);
    }

    private void play(int x0, int y0, double testStart, FileWriter writer) throws IOException {
        service.startNewGame(rules);
        ais = new AI[]{new AI(service, rules.getPlayerBasedOnTurn(0)),
            new AI(service, rules.getPlayerBasedOnTurn(1))};
        service.makeMove(x0, y0);
        ais[0].updateStateBasedOnLastMove();
        ais[1].updateStateBasedOnLastMove();
        double total = 0;
        while (!service.gameOver()) {
            int turn = service.getTurn();
            int[] areaBefore = ais[(turn + 1) % 2].getPlayArea();
            int areaSizeBefore = (areaBefore[3] - areaBefore[1] + 1) * (areaBefore[2] - areaBefore[0] + 1);
            int playedMovesBefore = service.getGameBoard().getNumberOfPlayedMoves();
            double start = System.nanoTime();
            Move move = ais[turn].chooseMove();
            double end = System.nanoTime();
            service.makeMove(move.getX(), move.getY());
            total += end - start;
            int[] areaAfter = ais[turn].getPlayArea();
            int areaSizeAfter = (areaAfter[3] - areaAfter[1] + 1) * (areaAfter[2] - areaAfter[0] + 1);
            int playedMovesAfter = playedMovesBefore + 1;
            writer.write(x0 + "," + y0 + ","
                    + move.getX() + "," + move.getY() + ","
                    + turn + ","
                    + playedMovesBefore + ","
                    + playedMovesAfter + ","
                    + (areaSizeBefore - playedMovesBefore) + ","
                    + (areaSizeAfter - playedMovesAfter) + ","
                    + Double.toString(nanoSecToSec(end - start)) + ","
                    + Double.toString(nanoSecToSec(total)) + ","
                    + Double.toString(nanoSecToSec(end - testStart)) + "\n");
        }
    }

    /**
     * Writes header to the resulting test data file.
     * @param writer writer that is connected to the test data file
     * @throws IOException 
     */
    private void writeHeader(FileWriter writer) throws IOException {
        writer.write("x0,y0,x,y,turn,number of moves before,number of moves after,"
                + "number of free tiles before,number of free tiles after,"
                + "time to choose move (s),total game time (s),total test time (s) \n");
    }

    /**
     * Converts nano seconds to milli seconds.
     *
     * @param nano nano seconds
     * @return milli seconds
     */
    private double nanoToMilliSec(double nano) {
        return nano / 1e6;
    }

    /**
     * Converts nano seconds to seconds.
     *
     * @param nano nano seconds
     * @return seconds
     */
    private double nanoSecToSec(double nano) {
        return nano / 1e9;
    }
}
