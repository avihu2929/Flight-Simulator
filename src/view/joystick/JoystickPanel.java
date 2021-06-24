package view.joystick;

import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class JoystickPanel extends BorderPane {

    public DoubleProperty aileron, elevators,rudder,throttle;
    public JoystickPanel(){
        super();
        FXMLLoader fxl = new FXMLLoader();
        try {
            BorderPane bp = fxl.load(getClass().getResource("joystick_panel.fxml").openStream());
            JoystickPanelController joystickPanelController = fxl.getController();

            aileron = joystickPanelController.aileron;
            elevators = joystickPanelController.elevators;
            rudder = joystickPanelController.rudder.valueProperty();
            throttle = joystickPanelController.throttle.valueProperty();

            this.getChildren().add(bp);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
