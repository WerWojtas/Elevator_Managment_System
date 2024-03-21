package manager.presenter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Spinner;
import javafx.stage.Stage;
import manager.Engine;
import manager.liftsystem.LiftScheduler;

import java.io.IOException;

public class SimulationInitializer {

    @FXML
    private Spinner<Integer> liftNumber;

    @FXML
    private Spinner<Integer> maxFloor;

    @FXML
    private Spinner<Integer> maxLoad;

    @FXML
    protected void simulation(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("simulation.fxml"));
        SimulationPresenter simulationPresenter = new SimulationPresenter();
        loader.setController(simulationPresenter);
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.setTitle("Simulation Window");
        stage.show();
        Engine simulationEngine = new Engine(new LiftScheduler());
        simulationEngine.setLifts(liftNumber.getValue(), maxLoad.getValue());
        Thread simulationThread = new Thread(simulationEngine);
        simulationPresenter.initialize(maxFloor.getValue(), liftNumber.getValue(), simulationEngine, simulationThread);
        simulationEngine.setObserver(simulationPresenter);
        simulationThread.start();
        stage = (Stage) liftNumber.getScene().getWindow();
        stage.close();
        simulationPresenter.resumeSimulation();
    }
}
