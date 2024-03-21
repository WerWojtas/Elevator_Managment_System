package manager.liftsystem;

import manager.utilities.*;

import java.util.PriorityQueue;

public class Lift implements FloorCompare {

    private final int maxLoad;
    protected int currentFloor;
    protected final PriorityQueue<Call> floorsUp;
    protected final PriorityQueue<Call> floorsDown;
    private final LiftManager observer;
    protected Direction direction;
    protected int load = 0;
    protected boolean malfuction = false;
    protected int doorsOpen = -1;
    private LiftScheduler manager;
    protected int target;
    protected int reserved = 0;

    public Lift(int load, LiftScheduler manager, LiftManager observer) {
        this.maxLoad = load;
        this.currentFloor = 1;
        this.direction = Direction.NONE;
        this.manager = manager;
        this.observer = observer;
        FloorComparator comparator = new FloorComparator();
        this.floorsDown = new PriorityQueue<>(comparator.reversed());
        this.floorsUp = new PriorityQueue<>(comparator);
    }

    public void doStep() throws TooMuchWeightException {
        if (this.doorsOpen >= 0){
            this.doorsOpen++;
            if (this.doorsOpen == 2){
                closeDoors();
            }
            return;
        }
        if (this.direction == Direction.UP){
            if (this.reserved > 0 && this.currentFloor == this.target){
                stop(this.floorsUp, true);
            }
            else if (this.reserved == 0 && this.currentFloor == this.floorsUp.peek().getFloor()) {
                stop(this.floorsUp, true);
            }
            else {
                this.currentFloor++;
            }
        }
        else if (this.direction== Direction.DOWN){
            if (this.reserved > 0 && this.currentFloor == this.target){
                stop(this.floorsDown, false);
            }
            if (this.reserved == 0 && this.currentFloor == this.floorsDown.peek().getFloor()) {
                stop(this.floorsDown, false);
            }
            else {
                this.currentFloor--;
            }
        }
    }

    public void stop (PriorityQueue<Call> collection, boolean up) throws TooMuchWeightException {
        collection.poll();
        openDoors(0, 0);
        this.observer.deleteFloor(this, up);
    }


    public void openDoors(int weightIn, int weightOut) throws TooMuchWeightException {
        this.doorsOpen = 0;
        if (this.load - weightIn + weightOut > this.maxLoad){
            throw new TooMuchWeightException("Too much weight in the lift: ");
        }
    }

    public void closeDoors(){
        this.doorsOpen = -1;
        if (this.reserved > 0){
            this.reserved = 0;

        }
        this.observer.changeDirection(this);
    }

    public int getCurrFloor(){
        return this.currentFloor;
    }

    public int getFloor(){
        return this.currentFloor + reserved;
    }

    public void callLift(Call call){
        if (this.reserved > 0){
            if (this.target > call.getFloor()){
                this.floorsDown.add(call);
            }
            else {
                this.floorsUp.add(call);
            }
        }
        else if (this.currentFloor == call.getFloor()){
            floorsUp.add(call);
        }
        else if (this.currentFloor < call.getFloor()){
            floorsUp.add(call);
            if (this.direction == Direction.NONE){
                this.observer.changeDirectionNone(this, call);
            }
        }
        else {
            floorsDown.add(call);
            if (this.direction == Direction.NONE){
                this.observer.changeDirectionNone(this, call);
            }
        }
    }

    public Direction getDirection(){
        return this.direction;
    }

    public void weightUpdate(int load){
        this.load = load;
    }

    public void killRevive(){
        if (this.malfuction){
            this.malfuction = false;
            this.observer.revive(this);
        }
        else {
            this.malfuction = true;
            this.observer.killLift(this);
            setMalfuction();
        }
    }

    public void setMalfuction(){
        this.floorsUp.clear();
        this.floorsDown.clear();
        this.direction = Direction.NONE;
        this.reserved = 0;
        this.target = 0;
    }

    public LiftStatus getStatus(){
        if (malfuction){
            return LiftStatus.MALFUNCTION;
        }
        if (this.doorsOpen >= 0){
            return LiftStatus.DOORS_OPEN;
        }
        if (this.direction == Direction.UP){
            return LiftStatus.MOVING_UP;
        }
        if (this.direction == Direction.DOWN){
            return LiftStatus.MOVING_DOWN;
        }
        return LiftStatus.STOPPED;
    }


}
