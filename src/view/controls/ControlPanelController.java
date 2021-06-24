package view.controls;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import view.main.MainWindow;
import view.main.MainWindowController;
import view_model.ViewModel;

import javax.swing.text.View;

public class ControlPanelController {

    public ViewModel vm ;

    public Button start_btn;
    public Slider time_slider;
    public TextField speed_text;
    public Label time_label;

    public void setViewModel(ViewModel vm){
        this.vm = vm;
        time_label.textProperty().bind(vm.TimeLabel);
        vm.Time.bindBidirectional(time_slider.valueProperty());
        vm.Speed.bindBidirectional(speed_text.textProperty());
        time_slider.maxProperty().bindBidirectional(vm.MaxSliderValue);

    }


    public void openButton(ActionEvent actionEvent) {
        vm.openCSV();

    }

    public void onStart(ActionEvent actionEvent) {
        vm.startTime();
    }

    public void onPause(ActionEvent actionEvent) {
        vm.pauseTime();
    }

    public void onStop(ActionEvent actionEvent) {
        vm.stopTime();
    }
}
