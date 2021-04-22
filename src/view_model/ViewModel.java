package view_model;

import com.sun.webkit.Timer;
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
    public DoubleProperty Time;
    boolean csvLoaded=false;
    public ViewModel(Model m){
        this.m = m;
        m.addObserver(this);
        FeaturesLabel = new SimpleStringProperty();
        JoystickLabel = new SimpleStringProperty();
        Time = new SimpleDoubleProperty();

        Time.addListener((o,ov,nv)->m.setTime(Time.intValue()));

    }
    public void openCSV(){
        m.openFile(2);
        csvLoaded=true;
    }


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
                            "\nRudder: 0");
                case "time":
                    //JoystickLabel.set("Aileron: "+getFlightData(0));
                    if (csvLoaded) {
                        int[] features = {0, 1, 2};
                        JoystickLabel.set("Aileron: " + m.getFlightData(features)[0] +
                                "\nElevators: " + m.getFlightData(features)[1] +
                                "\nRudder: " + m.getFlightData(features)[2]);
                    }
                    break;

            }




        }
    }
}
