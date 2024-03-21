package manager.liftsystem;

import manager.utilities.Call;
import manager.utilities.Direction;
import manager.utilities.TooMuchWeightException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LiftManagerTest {

    @Test
    public void testLiftManager() {
        LiftScheduler liftScheduler = new LiftScheduler();
        LiftManager liftManager = new LiftManager(liftScheduler);
        Lift lift1 = new Lift(1, liftScheduler, liftManager);
        Lift lift2 = new Lift(1, liftScheduler, liftManager);
        Lift[] lifts = {lift1, lift2};
        liftScheduler.initializeLifts(lifts);
        liftScheduler.call(new Call(2, Direction.UP));
        assertEquals(lift2.direction, Direction.UP);
        assertEquals(lift2.floorsUp.size(), 1);
        try{
            lift2.doStep();
        } catch (TooMuchWeightException e) {
            e.printStackTrace();
            liftScheduler.call(new Call(4, Direction.UP));
            assertEquals(lift2.floorsDown.size(), 2);
        }
    }
}
