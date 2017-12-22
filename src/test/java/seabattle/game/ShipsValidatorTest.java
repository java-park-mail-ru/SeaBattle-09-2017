package seabattle.game;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import seabattle.game.ship.Ship;
import seabattle.game.ship.ShipsValidator;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RunWith(SpringRunner.class)
public class ShipsValidatorTest {
    @Autowired
    private ShipsValidator shipsValidator;

    @Test
    public void isValidShips() {
        List<Ship> testShips = new ArrayList<>();
        testShips.add(new Ship(0, 6, 4, false));
        testShips.add(new Ship(0, 3, 3, true));
        testShips.add(new Ship(4, 1, 3, true));
        testShips.add(new Ship(0, 0, 2, false));
        testShips.add(new Ship(2, 9, 2, true));
        testShips.add(new Ship(8, 7, 2, true));
        testShips.add(new Ship(5, 6, 1, false));
        testShips.add(new Ship(8, 3, 1, false));
        testShips.add(new Ship(8, 5, 1, false));
        testShips.add(new Ship(9, 0, 1, false));
        assertTrue(shipsValidator.isValidShips(testShips));
    }

    @Test
    public void fewShips() {
        List<Ship> testShips = new ArrayList<>();
        testShips.add(new Ship(0, 6, 4, false));
        testShips.add(new Ship(0, 3, 3, true));
        testShips.add(new Ship(4, 1, 3, true));
        testShips.add(new Ship(0, 0, 2, false));
        assertTrue(!shipsValidator.isValidShips(testShips));
    }

    @Test
    public void shipsOutsideTheField() {
        List<Ship> testShips = new ArrayList<>();
        testShips.add(new Ship(10, 10, 4, false));
        testShips.add(new Ship(0, 3, 3, true));
        testShips.add(new Ship(4, 1, 3, true));
        testShips.add(new Ship(0, 0, 2, false));
        testShips.add(new Ship(2, 9, 2, true));
        testShips.add(new Ship(8, 7, 2, true));
        testShips.add(new Ship(5, 6, 1, false));
        testShips.add(new Ship(8, 3, 1, false));
        testShips.add(new Ship(8, 5, 1, false));
        testShips.add(new Ship(9, 0, 1, false));
        assertTrue(!shipsValidator.isValidShips(testShips));
    }

    @Test
    public void shipsOfTheWrongFormat() {
        List<Ship> testShips = new ArrayList<>();
        testShips.add(new Ship(0, 6, 4, false));
        testShips.add(new Ship(0, 3, 4, true));
        testShips.add(new Ship(4, 1, 3, true));
        testShips.add(new Ship(0, 0, 2, false));
        testShips.add(new Ship(2, 9, 2, true));
        testShips.add(new Ship(8, 7, 2, true));
        testShips.add(new Ship(5, 6, 2, false));
        testShips.add(new Ship(8, 3, 1, false));
        testShips.add(new Ship(8, 5, 1, false));
        testShips.add(new Ship(9, 0, 1, false));
        assertTrue(!shipsValidator.isValidShips(testShips));
    }

    @Test
    public void shipsNextToEachOther() {
        List<Ship> testShips = new ArrayList<>();
        testShips.add(new Ship(0, 6, 4, false));
        testShips.add(new Ship(0, 5, 3, true));
        testShips.add(new Ship(4, 1, 3, true));
        testShips.add(new Ship(0, 0, 2, false));
        testShips.add(new Ship(2, 9, 2, true));
        testShips.add(new Ship(8, 7, 2, true));
        testShips.add(new Ship(5, 6, 1, false));
        testShips.add(new Ship(8, 3, 1, false));
        testShips.add(new Ship(8, 5, 1, false));
        testShips.add(new Ship(9, 0, 1, false));
        assertTrue(!shipsValidator.isValidShips(testShips));
    }


}
