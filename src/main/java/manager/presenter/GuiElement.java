package manager.presenter;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import manager.liftsystem.LiftStatus;
import manager.utilities.Direction;

public class GuiElement {
    public Circle drawLift(LiftStatus status, double width, double height) {
        Color color = Color.GREEN;
        if (status == LiftStatus.MOVING_UP) {
            color = Color.ORANGE;
        }
        else if (status == LiftStatus.MOVING_DOWN) {
            color = Color.BLUE;
        }
        else if (status == LiftStatus.MALFUNCTION) {
            color = Color.RED;
        }
        else if (status == LiftStatus.DOORS_OPEN) {
            color = Color.DEEPPINK;
        }
        return new Circle( Math.min(width, height) /2, color);
    }
}
