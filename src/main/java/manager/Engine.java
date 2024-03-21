package manager;

import manager.liftsystem.*;
import manager.presenter.MapChangeListener;
import manager.utilities.Call;
import manager.utilities.TooMuchWeightException;

import java.util.ArrayList;
import java.util.List;

public class Engine implements Runnable {

    private Lift[] lifts;
    private final LiftScheduler liftScheduler;
    private  MapChangeListener observer;
    private final List<Call> callsIn = new ArrayList<>();
    private final List<Call> callsOut = new ArrayList<>();
    private final LiftManager liftManager;
    private boolean breakSimulation = false;
    private final Object stop = new Object();
    private boolean isPaused = false;

    public Engine(LiftScheduler liftScheduler) {
        this.liftScheduler = liftScheduler;
        this.liftManager = new LiftManager(liftScheduler);

    }

    public void setLifts (int liftNumber, int maxLoad) {
        lifts = new Lift[liftNumber];
        for (int i = 0; i < liftNumber; i++) {
            lifts[i] = new Lift(maxLoad, liftScheduler, liftManager);
        }
        liftScheduler.initializeLifts(lifts);
    }

    public void setObserver(MapChangeListener observer) {
        this.observer = observer;
    }


    public void run(){
        while (true) {
            if (breakSimulation) break;
            synchronized (stop) {
                while (isPaused) {
                    try {
                        stop.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            doStep();
            observer.mapChanged();
        }
    }

    public void addCallIn(Call callIn) {
        callsIn.add(callIn);
    }

    public void addCallOut(Call callOut) {
        callsOut.add(callOut);
    }

    public Lift[] getLifts() {
        return lifts;
    }

    public void doStep(){
        for (Call call : callsOut) {
            liftScheduler.call(call);
        }
        for (Call call : callsIn) {
            liftScheduler.callInside(call);
        }
        callsOut.clear();
        callsIn.clear();
        for (int i = 0; i < lifts.length; i++) {
            try {
                lifts[i].doStep();
            } catch (TooMuchWeightException e) {
                String message = e.getMessage() + i;
                observer.setErrorMessage(message, i);
            }
        }
    }

    public void weightUpdate(int index) {
        lifts[index].weightUpdate(0);
    }

    public void pauseSimulation() {
        isPaused = true;
    }

    public void resumeSimulation() {
        isPaused = false;
        synchronized (stop) {
            stop.notify();
        }
    }

    public void breakSimulation(){
        breakSimulation = true;
    }

    public void killReviveLift(int index){
        lifts[index].killRevive();
    }

}
