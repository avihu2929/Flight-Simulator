package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import view_model.ViewModel;

import javax.swing.text.View;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.security.cert.Extension;
import java.util.Observable;
import java.util.Observer;

public class MainWindowController implements Observer {


    ViewModel vm;
    public Label FeaturesLabel;

    public void setViewModel(ViewModel vm){
        this.vm = vm;
        //vm.FeaturesLabel.bind(FeaturesLabel.textProperty());
        FeaturesLabel.textProperty().bind(vm.FeaturesLabel);
    }


    @Override
    public void update(Observable o, Object arg) {

    }
}
