package manager.utilities;

import manager.liftsystem.Lift;

public class Call implements FloorCompare {

    private final int floor;
    private Direction direction;
    private Lift lift;

    public Call(int floor, Direction direction) {
        this.floor = floor;
        this.direction = direction;
    }

    public Call(int floor, Lift lift) {
        this.floor = floor;
        this.lift = lift;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public int getFloor() {
        return floor;
    }

    public boolean isUp() {
        return direction == Direction.UP;
    }

    public Lift getLift() {
        return lift;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Call call = (Call) o;
        return floor == call.floor;
    }

    public int hashCode() {
        return floor*31;
    }

}
