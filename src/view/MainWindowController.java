package view;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import view_model.ViewModel;

import java.util.Observable;
import java.util.Observer;

public class MainWindowController implements Observer {


    public Label joystick_label;
    public Label clocks_label;
    public Label features_label;
    public Slider time_slider;
    ViewModel vm;

    public void setViewModel(ViewModel vm){
        this.vm = vm;
        //vm.FeaturesLabel.bind(FeaturesLabel.textProperty());
        features_label.textProperty().bind(vm.FeaturesLabel);
        joystick_label.textProperty().bind(vm.JoystickLabel);
        vm.Time.bind(time_slider.valueProperty());

    }


    @Override
    public void update(Observable o, Object arg) {

    }

    public void openButton(ActionEvent actionEvent) {
        vm.openCSV();

    }



}
