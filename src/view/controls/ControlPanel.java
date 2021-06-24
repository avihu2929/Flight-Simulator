package view.controls;

import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import view.joystick.JoystickPanelController;
import view_model.ViewModel;

import java.io.IOException;

public class ControlPanel extends AnchorPane {


    public  ControlPanelController controlPanelController;
    public ControlPanel(){
        super();
        FXMLLoader fxl = new FXMLLoader();
        try {
            AnchorPane ap = fxl.load(getClass().getResource("control_panel.fxml").openStream());
            controlPanelController = fxl.getController();
            fxl.setClassLoader(getClass().getClassLoader());
            //fxl.load();

            this.getChildren().add(ap);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
