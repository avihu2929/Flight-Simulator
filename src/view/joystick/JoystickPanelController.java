package view.joystick;

import javafx.beans.property.DoubleProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Slider;

public class JoystickPanelController{
    public Slider rudder;
    public Slider throttle;
    public Canvas joystick;
    public DoubleProperty aileron, elevators;
}
