package manager.liftsystem;

import manager.utilities.Call;
import manager.utilities.Direction;
import manager.utilities.FloorComparator;

import java.util.*;

public class LiftScheduler {

    protected final List<Lift> liftsStay = new ArrayList<>();
    protected final List<Lift> liftsDown = new ArrayList<>();
    protected final List<Lift> liftsUp = new ArrayList<>();
    protected final HashSet<Integer> floorsUp = new HashSet<>();
    protected final HashSet<Integer> floorsDown = new HashSet<>();
    private final FloorComparator comparator = new FloorComparator();

    public void initializeLifts(Lift[] lifts){
        liftsStay.addAll(Arrays.asList(lifts));

    }

    public void call(Call call){
        schedule(call);
        updateLifts();
    }


    public void schedule(Call call){
        if ((call.isUp() && floorsUp.contains(call.getFloor())) || (!call.isUp() && floorsDown.contains(call.getFloor()))){
            return;
        }
        Optional<Lift> stayed = searchStayed(call.getFloor(), liftsStay);
        if (stayed.isPresent() && stayed.get().getCurrFloor() == call.getFloor()){
            stayed.get().callLift(call);
            return;
            }
        Optional <Lift> liftDirection = searchCollection(call, call.isUp() ? liftsUp : liftsDown);
        if (liftDirection.isPresent() && stayed.isPresent()){
            int floor1 = Math.abs(liftDirection.get().getCurrFloor() - call.getFloor());
            int floor2 = Math.abs(stayed.get().getCurrFloor() - call.getFloor());
            if (floor1 < floor2){
                liftDirection.get().callLift(call);
                addFloor(call);
                return;
            }
            stayed.get().callLift(call);
            addFloor(call);
            return;

        }
        if (stayed.isPresent()) {
            stayed.get().callLift(call);
            addFloor(call);
            return;
        }
        if (liftDirection.isPresent()){
            liftDirection.get().callLift(call);
            addFloor(call);
            return;
        }
        Lift lift = call.isUp() ? liftsDown.get(liftsDown.size()-1) : liftsUp.get(liftsUp.size()-1);
        lift.callLift(call);
        addFloor(call);
    }

    public Optional<Lift> searchCollection(Call call, List<Lift> collection) {
        if (collection.isEmpty()){
            return Optional.empty();
        }
        int i = 0;
        while (i < collection.size() && ((call.isUp() && collection.get(i).getCurrFloor() < call.getFloor()) || (!call.isUp() && collection.get(i).getCurrFloor() > call.getFloor()))) {
            i++;
        }
        return i < collection.size() ? Optional.of(collection.get(i)) : Optional.of(collection.get(i-1));
    }

    public Optional<Lift> searchStayed(int floor, List<Lift> collection){
        if (collection.isEmpty()){
            return Optional.empty();
        }
        int i = 0;
        while (i < collection.size() && collection.get(i).getCurrFloor() < floor){
            i++;
        }
        if (i < collection.size() && i!=0){
            int diff1 = collection.get(i).getCurrFloor() - floor;
            int diff2 = floor - collection.get(i-1).getCurrFloor();
            return Optional.of(diff1 < diff2 ? collection.get(i) : collection.get(i-1));
        }
        return Optional.of(i==0 ? collection.get(i) : collection.get(i-1));
    }

    public void addFloor(Call call){
        if (call.isUp()){
            floorsUp.add(call.getFloor());
        }
        else {
            floorsDown.add(call.getFloor());
        }
    }

    public void removeFloor(int floor, boolean isUp){
        if (isUp){
            floorsUp.remove(floor);
        }
        else {
            floorsDown.remove(floor);
        }
    }

    public void updateLifts(){
        liftsStay.sort(comparator);
        liftsDown.sort(comparator.reversed());
        liftsUp.sort(comparator);
    }

    public void callInside(Call call){
        if (call.getLift().currentFloor<=call.getFloor()){
            call.setDirection(Direction.UP);
            call.getLift().callLift(call);
        }
        else {
            call.setDirection(Direction.DOWN);
            call.getLift().callLift(call);
        }

    }
}
