package manager.presenter;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import manager.Engine;
import manager.utilities.Call;
import manager.liftsystem.Lift;
import manager.utilities.Direction;
import javafx.scene.control.Button;

import java.io.IOException;

public class SimulationPresenter implements MapChangeListener {

    @FXML
    private GridPane liftGrid;

    @FXML
    private GridPane callGrid;

    @FXML
    private GridPane buttonGrid;

    private Lift[] liftsList;
    private final GuiElement liftPrinter = new GuiElement();
    private int floors;
    private int lifts;
    private double cellSize;
    private Engine simulationEngine;
    private Thread simulationThread;

    public void initialize(int floors, int lifts, Engine engine, Thread simulationThread){
        simulationEngine = engine;
        this.floors = floors;
        this.lifts = lifts;
        this.liftsList = engine.getLifts();
        cellSize = Math.min(500 / (this.lifts + 1), 500 / (this.floors + 1));
        this.simulationThread = simulationThread;
        initializeElevatorGrid();
        initializeUpDown();
        initializeButtonGrid();
        printLifts();
    }

    public void mapChanged(){
        Platform.runLater(() -> {
            clearGrid();
            initializeElevatorGrid();
            printLifts();
        });
    }

    private void clearGrid() {
        liftGrid.getChildren().retainAll(liftGrid.getChildren().get(0));
        liftGrid.getColumnConstraints().clear();
        liftGrid.getRowConstraints().clear();
    }

    public void initializeElevatorGrid(){
        setColumnsOnGrid();
        setRowsOnGrid();
    }

    public void setColumnsOnGrid(){
        for (int i = 0; i <= this.lifts; i++){
            Label label = new Label(Integer.toString(i+1));
            GridPane.setHalignment(label, HPos.CENTER);
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setPrefWidth(this.cellSize);
            liftGrid.getColumnConstraints().add(columnConstraints);
            if (i != this.lifts){
                liftGrid.add(label, i + 1, this.floors);
            }
        }
    }

    public void setRowsOnGrid() {
        for (int i = 0; i <= this.floors; i++) {
            Label label = new Label(Integer.toString(this.floors - i));
            GridPane.setHalignment(label, HPos.CENTER);
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPrefHeight(this.cellSize);
            liftGrid.getRowConstraints().add(rowConstraints);
            if (i!=this.floors){
                liftGrid.add(label, 0, i);
            }
        }
    }

    public void initializeUpDown(){
        for (int i = 0; i <= this.floors; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPrefHeight(cellSize);
            callGrid.getRowConstraints().add(rowConstraints);
            Label label = new Label("    " + Integer.toString(this.floors - i) + "    ");
            GridPane.setHalignment(label, HPos.CENTER);
            callGrid.add(label, 0, i);
            addUpDown(i);
        }
    }

    public void printLifts(){
        for (int i=0; i<liftsList.length; i++){
            int floor = liftsList[i].getCurrFloor();
            liftGrid.add(liftPrinter.drawLift(liftsList[i].getStatus(), cellSize, cellSize), lifts-i, floors - floor);
        }
    }

    public void addUpDown(int row){
        if (row ==this.floors){
            return;
        }
        if (row != 0) { // Sprawdź, czy nie jest to pierwszy wiersz
            Button up = new Button("  Up  ");
            up.setOnAction(e -> simulationEngine.addCallOut(new Call(this.floors -row, Direction.UP)));
            callGrid.add(up, 1, row);
        }

        if (row != this.floors-1) { // Sprawdź, czy nie jest to pierwszy wiersz
            Button down = new Button("Down");
            down.setOnAction(e -> simulationEngine.addCallOut(new Call(this.floors - row, Direction.DOWN)));
            callGrid.add(down, 2, row);
        }
    }

    public void initializeButtonGrid(){
        for (int i = 0; i < lifts; i++){
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPrefHeight(cellSize);
            buttonGrid.getRowConstraints().add(rowConstraints);
            Label label = new Label("     " + Integer.toString(i+1) + "     ");
            GridPane.setHalignment(label, HPos.CENTER);
            buttonGrid.add(label, 0, i);
            addButtonInside(i);
        }
    }

    public void addButtonInside(int index){
        Spinner<Integer> spinner = new Spinner<>(1, floors, 0);
        Button button = new Button("Call");
        Button buttonKill = new Button("kill/revive");
        button.setOnAction(e -> simulationEngine.addCallIn(new Call(spinner.getValue(), liftsList[lifts-index-1])));
        buttonKill.setOnAction(e -> simulationEngine.killReviveLift(lifts-index-1));
        buttonGrid.add(spinner, 1, index);
        buttonGrid.add(button, 2, index);
        buttonGrid.add(buttonKill, 3, index);
    }

    public void setErrorMessage(String errorMessage, int index) {
        Platform .runLater(() -> {
            try {
                Stage stage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("weightError.fxml"));
                Parent root = loader.load();
                WeightErrorPresenter presenter = loader.getController();
                presenter.setErrorMessage(errorMessage, index);
                presenter.setSimulationPresenter(this);
                stage.setScene(new Scene(root));
                stage.setTitle("Simulation Window");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void weightUpdate(int index){
        simulationEngine.weightUpdate(index);
    }

    public void brakSimulation() {
        simulationEngine.breakSimulation();
        simulationThread.interrupt();
    }

    public void stopSimulation(){
        simulationEngine.pauseSimulation();
    }

    public void resumeSimulation(){
        simulationEngine.resumeSimulation();
    }
}
