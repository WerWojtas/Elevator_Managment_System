package manager.utilities;


public class FloorComparator implements java.util.Comparator<FloorCompare> {

    @Override
    public int compare(FloorCompare o1, FloorCompare o2) {
        return (o1.getFloor() - o2.getFloor() < 0) ? -1 : 1;
    }
}
