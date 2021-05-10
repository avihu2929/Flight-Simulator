package view;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
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
    public Canvas joystick;
    public Double aileron;
    public Button start_btn;
    public Label time_label;
    ViewModel vm;

    public void setViewModel(ViewModel vm){
        this.vm = vm;
        //vm.FeaturesLabel.bind(FeaturesLabel.textProperty());
        features_label.textProperty().bind(vm.FeaturesLabel);
        joystick_label.textProperty().bind(vm.JoystickLabel);
        time_label.textProperty().bind(vm.TimeLabel);

       // time_slider.valueProperty().bind(vm.Time);
        //time_slider.valueProperty().bind(vm.Time);
        vm.Time.bindBidirectional(time_slider.valueProperty());
       // time_slider.setValue(200);
        vm.Time.addListener((o,ov,nv)->System.out.println(ov+" "+nv));
      //  vm.Time.addListener((o,ov,nv)->time_slider.setValue(vm.Time.getValue()));

       // time_slider.valueProperty().bind(vm.Time);

        paint();
    }

    void paint(){
        GraphicsContext gc = joystick.getGraphicsContext2D();

        //positon width height , size width height
        gc.strokeOval(joystick.getWidth()/3,joystick.getHeight()/4,joystick.getWidth()/3,joystick.getHeight()/2);
        gc.fillOval(vm.Aileron.doubleValue(),joystick.getHeight()/4+joystick.getHeight()/8,joystick.getWidth()/6,joystick.getHeight()/4);
        
  //joystick.getWidth()/3+joystick.getWidth()/12
    }


    @Override
    public void update(Observable o, Object arg) {

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
