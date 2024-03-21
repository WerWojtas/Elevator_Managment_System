package manager.liftsystem;

import manager.utilities.Call;
import manager.utilities.Direction;
import manager.utilities.TooMuchWeightException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LiftAndManagerTest {

    @Test
    public void testLift() {
        LiftScheduler liftSheduler = new LiftScheduler();
        LiftManager liftManager = new LiftManager(liftSheduler);
        Lift lift = new Lift(1, liftSheduler, liftManager);
        assertEquals(lift.getCurrFloor(), 1);
        assertEquals(lift.getDirection(), Direction.NONE);
        lift.callLift(new Call(2, Direction.UP));
        assertEquals(lift.floorsUp.size(), 1);
        assertEquals(lift.getDirection(), Direction.UP);
        try {
            lift.doStep();
        } catch (TooMuchWeightException e) {
            e.printStackTrace();
        }
        assertEquals(lift.getCurrFloor(), 2);
        assertEquals(lift.getDirection(), Direction.UP);
        try {
            lift.doStep();
            lift.doStep();
            lift.doStep();
        } catch (TooMuchWeightException e) {
            e.printStackTrace();
        }
        assertEquals(lift.getCurrFloor(), 2);
        assertEquals(lift.getDirection(), Direction.NONE);
        Lift lift2 = new Lift(1, liftSheduler, liftManager);
        lift2.callLift(new Call(3, Direction.DOWN));
        assertEquals(lift2.target, 3);
        assertEquals(lift2.getDirection(), Direction.UP);
        assertFalse(lift2.reserved == 0 );
    }

    @Test
    public void testScheduler() {
        LiftScheduler liftScheduler = new LiftScheduler();
        LiftManager liftManager = new LiftManager(liftScheduler);
        Lift lift1 = new Lift(1, liftScheduler, liftManager);
        Lift lift2 = new Lift(1, liftScheduler, liftManager);
        Lift lift3 = new Lift(1, liftScheduler, liftManager);
        Lift[] lifts = {lift1, lift2, lift3};
        liftScheduler.initializeLifts(lifts);
        lift1.callLift(new Call(2, Direction.UP));
        lift2.callLift(new Call(3, Direction.DOWN));
        lift3.callLift(new Call(4, Direction.UP));
        try {
            lift1.doStep();
            lift2.doStep();
            lift3.doStep();
            lift1.doStep();
            lift2.doStep();
            lift3.doStep();
            lift1.doStep();
            lift2.doStep();
            lift3.doStep();
            lift1.doStep();
            lift2.doStep();
            lift3.doStep();
        } catch (TooMuchWeightException e) {
            e.printStackTrace();
        }
        assertEquals(lift1.getCurrFloor(), 2);
        assertEquals(lift2.getCurrFloor(), 3);
        assertEquals(lift3.getCurrFloor(), 4);
        assertEquals(lift1.getDirection(), Direction.NONE);
        assertEquals(lift2.getDirection(), Direction.UP);
        assertEquals(lift3.getDirection(), Direction.UP);
    }
}
