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

    public ViewModel(Model m){
        this.m = m;
        m.addObserver(this);
        FeaturesLabel = new SimpleStringProperty();


    }

    @Override
    public void update(java.util.Observable o, Object arg) {
        if(o==m){FeaturesLabel.set(m.getFeaturesList());}
    }
}
