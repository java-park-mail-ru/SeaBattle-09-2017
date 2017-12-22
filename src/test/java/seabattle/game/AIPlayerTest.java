package seabattle.game;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import seabattle.game.player.AIPlayer;
import seabattle.game.player.Player;
import seabattle.game.ship.ShipsValidator;
import static org.junit.Assert.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RunWith(SpringRunner.class)
public class AIPlayerTest {
    @Autowired
    private ShipsValidator shipsValidator;

    @Test
    public void generatedShips() {
        final int testGenerateShipsCount = 400;
        Player AIPlayer = new AIPlayer();
        for (int i = 0; i < testGenerateShipsCount; i++) {
            assertTrue(shipsValidator.isValidShips( AIPlayer.generateShips()));
        }
    }
}
