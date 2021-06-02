package view;

import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import view_model.ViewModel;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public class MainWindowController implements Observer {

    ViewModel vm;

    public Label joystick_label;
    public Label clocks_label;
    public Label features_label;
    public Slider time_slider;
    public Canvas joystick;
    public Double aileron;
    public Button start_btn;
    public Label time_label;
    public TextField speed_text;
    public ListView features_list;
    @FXML
    LineChart LineChart;


    public void setViewModel(ViewModel vm){
        this.vm = vm;

        //features_label.textProperty().bind(vm.FeaturesLabel);
        features_list.setItems(vm.FeaturesList);
        features_list.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
             //   vm.setFeatureChart(features_list.getSelectionModel().getSelectedIndex());
              //  features_list.getSelectionModel().selectedItemProperty().toString()
                System.out.println(features_list.getSelectionModel().getSelectedIndex() );
                LineChart.getData().clear();
                LineChart.getData().add(vm.getFeatureChart(features_list.getSelectionModel().getSelectedIndex()));
            }
        });

        joystick_label.textProperty().bind(vm.JoystickLabel);
        clocks_label.textProperty().bind(vm.ClocksLabel);
        time_label.textProperty().bind(vm.TimeLabel);
        vm.Time.bindBidirectional(time_slider.valueProperty());
        vm.Speed.bindBidirectional(speed_text.textProperty());
        time_slider.maxProperty().bindBidirectional(vm.MaxSliderValue);



    }

    void paint(){
        //GraphicsContext gc = joystick.getGraphicsContext2D();
//
        //positon width height , size width height
     //   gc.strokeOval(joystick.getWidth()/3,joystick.getHeight()/4,joystick.getWidth()/3,joystick.getHeight()/2);
       // gc.fillOval(vm.Aileron.doubleValue(),joystick.getHeight()/4+joystick.getHeight()/8,joystick.getWidth()/6,joystick.getHeight()/4);
        
        //joystick.getWidth()/3+joystick.getWidth()/12
    }


    @Override
    public void update(Observable o, Object arg) {

    }

    public void openButton(ActionEvent actionEvent) { vm.openCSV(); }

    public void onStart(ActionEvent actionEvent) { vm.startTime(); }

    public void onPause(ActionEvent actionEvent) { vm.pauseTime(); }

    public void onStop(ActionEvent actionEvent) { vm.stopTime(); }

    public void connect(ActionEvent actionEvent) throws IOException, InterruptedException {
        vm.connect();
    }
}
