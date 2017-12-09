package seabattle.game;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import seabattle.game.player.PlayerAI;
import seabattle.game.ship.ShipsValidator;
import static org.junit.Assert.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RunWith(SpringRunner.class)
public class PlayerAITest {
    @Autowired
    private ShipsValidator shipsValidator;

    @Test
    public void generatedShips() {
        assertTrue(shipsValidator.isValidShips( PlayerAI.generateShips()));
        assertTrue(shipsValidator.isValidShips( PlayerAI.generateShips()));
        assertTrue(shipsValidator.isValidShips( PlayerAI.generateShips()));
    }
}
