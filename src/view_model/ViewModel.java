package view_model;

import com.sun.webkit.Timer;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.Model;

import java.util.Observable;
import java.util.Observer;

public class ViewModel extends Observable implements Observer {
    Model m;

    public StringProperty FeaturesLabel;
    public StringProperty JoystickLabel;
    public StringProperty ClocksLabel;
    public DoubleProperty Time;
    public StringProperty TimeLabel;
    public StringProperty Speed;
    public DoubleProperty MaxSliderValue;
    boolean csvLoaded=false;

    public ViewModel(Model m){

        this.m = m;
        m.addObserver(this);

        FeaturesLabel = new SimpleStringProperty();
        JoystickLabel = new SimpleStringProperty();
        ClocksLabel = new SimpleStringProperty();
        Time = new SimpleDoubleProperty();
        TimeLabel = new SimpleStringProperty();
        Speed = new SimpleStringProperty();
        MaxSliderValue = new SimpleDoubleProperty();

        //when vm.time changes its value set m.time to vm.time
        Time.addListener((o,ov,nv)->m.setTime(Time.intValue()));
        Speed.addListener((o,ov,nv)->m.setSpeed(Speed.getValue()));
    }

    public void openCSV(){
        // 2 for csv
        m.openFile(2);

        MaxSliderValue.set(m.row);

        //a flag so ui button wont do anything unless the csv has loaded ( see vm.update)
        csvLoaded=true;
    }

    public void startTime(){ m.startTime(); }
    public void pauseTime() { m.pauseTime(); }
    public void stopTime() { m.stopTime(); }

    @Override
    public void update(java.util.Observable o, Object arg) {

        if(o==m){
            switch (arg.toString()){
                case "xml":
                    FeaturesLabel.set(m.getFeaturesList());
                    break;
                case "csv":
                    JoystickLabel.set("Aileron: 0"+
                            "\nElevators: 0"+
                            "\nRudder: 0"+
                            "\nThrottle: 0");
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
                                JoystickLabel.set("Aileron: " + m.getFlightData(features)[0] +
                                        "\nElevators: " + m.getFlightData(features)[1] +
                                        "\nRudder: " + m.getFlightData(features)[2]+
                                        "\nThrottle: "+ m.getFlightData(features)[3]);
                                ClocksLabel.set("Roll: "+m.getFlightData(features)[4]+
                                        "\nPitch: "+m.getFlightData(features)[5]);
                            }
                        });
                    }
                    break;
            }
        }
    }


}
