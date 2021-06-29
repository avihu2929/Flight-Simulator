package view.main;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import view_model.ViewModel;

import java.io.IOException;
import java.util.*;

public class MainWindowController implements Observer {

    public view.controls.ControlPanel ControlPanel;
    public Label algo_label;
    //  public JoystickPanel joystick;
    ViewModel vm;

    public Label joystick_label;
    public Label clocks_label;
    public Label features_label;
    //public Canvas joystick;
    public Slider time_slider;
    public Double aileron;
    public Button start_btn;
    public Label time_label;
    public TextField speed_text;
    public ListView<String> features_list;
    @FXML
    LineChart LineChart;

    @FXML
    LineChart CorrelatedLineChart;


    public void setViewModel(ViewModel vm) {
        this.vm = vm;
        ControlPanel.controlPanelController.setViewModel(vm);
        features_list.setItems(vm.FeaturesList);

        //set new chart on change of selected item from feature list
        features_list.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                System.out.println(features_list.getSelectionModel().getSelectedIndex());
                LineChart.getData().clear();
                LineChart.getData().add(vm.getFeatureChart(features_list.getSelectionModel().getSelectedIndex()));
                // ============= Correlated Feature Chart =============
                String selectedFeatureName = features_list.getSelectionModel().getSelectedItem();
                CorrelatedLineChart.getData().clear();
                if (vm.getFeatureCorrelationMap()!=null) {
                    if (vm.getFeatureCorrelationMap().containsKey(selectedFeatureName)) {
                        String correlatedFeatureName = vm.getFeatureCorrelationMap().get(selectedFeatureName).get(0);
                        ArrayList<String> featuresNames = new ArrayList<String>();
                        featuresNames.addAll(features_list.getItems());
                        int correlatedFeatureIndex = featuresNames.indexOf(correlatedFeatureName);
                        CorrelatedLineChart.getData().clear();
                        if (correlatedFeatureIndex > 0) {
                            CorrelatedLineChart.getData().add(vm.getFeatureChart(correlatedFeatureIndex));
                        }
                    }
                }
            }
        });

        joystick_label.textProperty().bind(vm.JoystickLabel);
        clocks_label.textProperty().bind(vm.ClocksLabel);
        algo_label.textProperty().bind(vm.AlgoLabel);

    }


    @Override
    public void update(Observable o, Object arg) {

    }

    public void connect(ActionEvent actionEvent) throws IOException, InterruptedException {
        vm.connect();
    }

    public void algochoice(ActionEvent actionEvent) {
       vm.chooseAlgo();
    }
}
