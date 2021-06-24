package view_model;

import com.sun.webkit.Timer;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableDoubleValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import model.Model;

import java.io.IOException;
import java.util.*;

public class ViewModel extends Observable implements Observer {

    Model m;

    public StringProperty FeaturesLabel;
    public StringProperty JoystickLabel;
    public StringProperty AlgoLabel;
    public StringProperty ClocksLabel;
    public DoubleProperty Time;
    public StringProperty TimeLabel;
    public StringProperty Speed;
    public ObservableList<String> FeaturesList;
    public DoubleProperty MaxSliderValue;

    boolean csvLoaded=false;
    int featureCount = 0;

    public ViewModel(Model m){

        this.m = m;
        m.addObserver(this);

        FeaturesLabel = new SimpleStringProperty();
        JoystickLabel = new SimpleStringProperty();
        AlgoLabel = new SimpleStringProperty();
        ClocksLabel = new SimpleStringProperty();
        Time = new SimpleDoubleProperty();
        TimeLabel = new SimpleStringProperty();
        Speed = new SimpleStringProperty();
        MaxSliderValue = new SimpleDoubleProperty();
        FeaturesList = FXCollections.observableArrayList();
        //when vm.time changes its value set m.time to vm.time
        Time.addListener((o,ov,nv)->m.setTime(Time.intValue()));
        Speed.addListener((o,ov,nv)->m.setSpeed(Speed.getValue()));

    }

    public void openCSV(){
        // 2 for csv

        if(!csvLoaded){
            m.openFile(2);
            MaxSliderValue.set(m.row);

        }

        //a flag so ui button wont do anything unless the csv has loaded ( see vm.update)
        csvLoaded=true;
    }

    public XYChart.Series<Number, Number> getFeatureChart(int feature){
        return m.getFeatureChart(feature);

    }

    public void connect() throws IOException, InterruptedException {
        m.connect();
    }

    public void startTime(){ m.startTime(); }
    public void pauseTime() { m.pauseTime(); }
    public void stopTime() { m.stopTime(); }

    @Override
    public void update(java.util.Observable o, Object arg) {

        if(o==m){
            switch (arg.toString()){
                case "xml":
                    FeaturesList.add(m.getFeatureItem(featureCount));
                  //  FeaturesLabel.set(m.getFeaturesList());
                    featureCount++;
                    break;

                case "csv":
                    JoystickLabel.set(" Aileron: 0"+
                            "\n Elevator: 0"+
                            "\n Rudder: 0"+
                            "\n Throttle: 0");
                    ClocksLabel.set("Roll: 0"+
                            "\nPitch: 0");
                case "time":
                    if (csvLoaded) {
                        int[] features = {0, 1, 2, 6, 17, 18};
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                Time.set(m.getTime());
                                TimeLabel.set(m.getTime()+"");
                                JoystickLabel.set(" Aileron: " + m.getFlightData(features)[0] +
                                        "\n Elevator: " + m.getFlightData(features)[1] +
                                        "\n Rudder: " + m.getFlightData(features)[2]+
                                        "\n Throttle: "+ m.getFlightData(features)[3]);
                                ClocksLabel.set("Roll: "+m.getFlightData(features)[4]+
                                        "\nPitch: "+m.getFlightData(features)[5]);
                            }
                        });
                    }
                    break;
                case "algo":
                    AlgoLabel.set(m.getAnomaly());
                    break;
            }
        }
    }


    public void chooseAlgo() {
        m.chooseAlgo();
    }
}
