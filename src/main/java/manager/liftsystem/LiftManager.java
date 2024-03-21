package manager.liftsystem;

import manager.utilities.Call;
import manager.utilities.Direction;

public class LiftManager {

    private final LiftScheduler liftScheduler;

    public LiftManager(LiftScheduler liftScheduler) {
        this.liftScheduler = liftScheduler;
    }

    public void deleteFloor(Lift lift, boolean up){
        if (lift.reserved > 0){
            liftScheduler.removeFloor(lift.currentFloor, !up);
        }
        else {
            liftScheduler.removeFloor(lift.currentFloor, up);
        }
    }

    public void changeDirection(Lift lift){
        if (lift.direction == Direction.UP && lift.floorsUp.isEmpty()){
            if (!lift.floorsDown.isEmpty()){
                lift.direction = Direction.DOWN;
                liftScheduler.liftsUp.remove(lift);
                liftScheduler.liftsDown.add(lift);
            }
            else{
                lift.direction = Direction.NONE;
                liftScheduler.liftsUp.remove(lift);
                liftScheduler.liftsStay.add(lift);
            }
        }
        else if (lift.direction == Direction.DOWN && lift.floorsDown.isEmpty()){
            if (!lift.floorsUp.isEmpty()){
                lift.direction = Direction.UP;
                liftScheduler.liftsDown.remove(lift);
                liftScheduler.liftsUp.add(lift);
            }
            else{
                lift.direction = Direction.NONE;
                liftScheduler.liftsDown.remove(lift);
                liftScheduler.liftsStay.add(lift);
            }
        }
    }

    public void changeDirectionNone(Lift lift, Call call){
        if (call.isUp()){
            liftScheduler.liftsStay.remove(lift);
            if (lift.currentFloor > call.getFloor()){
                lift.direction = Direction.DOWN;
                lift.reserved = lift.currentFloor-call.getFloor();
                lift.target = call.getFloor();
            }
            else {
                lift.direction = Direction.UP;
            }
            liftScheduler.liftsUp.add(lift);
        }
        else {
            liftScheduler.liftsStay.remove(lift);
            if (lift.currentFloor < call.getFloor()) {
                lift.reserved = call.getFloor() - lift.currentFloor;
                lift.direction = Direction.UP;
                lift.target = call.getFloor();
            }
            else {
                lift.direction = Direction.DOWN;
            }
            liftScheduler.liftsDown.add(lift);
        }
    }

    public void killLift(Lift lift){
        if (lift.direction== Direction.NONE){
            liftScheduler.liftsStay.remove(lift);
        }
        else if (lift.direction == Direction.UP){
            liftScheduler.liftsUp.remove(lift);
        }
        else {
            liftScheduler.liftsDown.remove(lift);
        }
        for (Call call : lift.floorsUp){
            liftScheduler.removeFloor(call.getFloor(), true);
            liftScheduler.call(call);
        }
        for (Call call : lift.floorsDown){
            liftScheduler.removeFloor(call.getFloor(), false);
            liftScheduler.call(call);
        }
    }

    public void revive(Lift lift){
        liftScheduler.liftsStay.add(lift);
    }
}
