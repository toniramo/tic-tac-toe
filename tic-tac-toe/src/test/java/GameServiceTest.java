/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import tictactoe.dao.Dao;
import tictactoe.dao.InMemoryDao;
import tictactoe.domain.GameService;
//import static org.mockito.Mockito.*;

/**
 *
 * @author toniramo
 */
public class GameServiceTest {
    private GameService gameService;
    Dao mockedDao;
    
    public GameServiceTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        mockedDao = mock(InMemoryDao.class);
        gameService = new GameService(mockedDao);
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void gameServiceCallsInitializeGameBoardMethodOfDao() {
        gameService.startNewGame(20, 5);
        verify(mockedDao).initializeGameBoard(20, 5);
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
