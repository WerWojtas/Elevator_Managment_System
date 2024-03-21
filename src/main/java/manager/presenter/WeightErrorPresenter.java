package manager.presenter;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class WeightErrorPresenter {

    @FXML
    private Label errorMessageLabel;

    private String errorMessage;
    private int liftIndex;
    private SimulationPresenter simulationPresenter;

    public void setErrorMessage(String errorMessage, int index) {
        this.errorMessage = errorMessage;
        this.liftIndex = index;
        errorMessageLabel.setText(errorMessage);
    }

    public void setSimulationPresenter(SimulationPresenter simulationPresenter){
        this.simulationPresenter = simulationPresenter;
        simulationPresenter.stopSimulation();
    }

    @FXML
    private void continueSimulation() {
        simulationPresenter.weightUpdate(liftIndex);
        Stage stage = (Stage) errorMessageLabel.getScene().getWindow();
        stage.close();
        simulationPresenter.resumeSimulation();
    }
}
