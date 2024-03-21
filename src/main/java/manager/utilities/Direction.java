package manager.utilities;

public enum Direction {
    UP,
    DOWN,
    NONE;

    public Direction opposite() {
        if (this == UP) {
            return DOWN;
        } else if (this == DOWN) {
            return UP;
        } else {
            return NONE;
        }
    }
}


