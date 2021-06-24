package view.main;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Model;
import test.AnomalyReport;
import test.TimeSeries;
import test.TimeSeriesAnomalyDetector;
import view_model.ViewModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

public class MainWindow extends Application {



    @Override
    public void start(Stage primaryStage) throws Exception{

        Model m = new Model();
        ViewModel vm = new ViewModel(m);
        m.addObserver(vm);
        FXMLLoader fxl = new FXMLLoader();
      //  BorderPane root =fxl.load(getClass().getResource("main_window.fxml").openStream());
        Parent root = fxl.load(getClass().getResource("main_window.fxml").openStream());
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 515, 400));

        MainWindowController mwc = fxl.getController();
        mwc.setViewModel(vm);
        vm.addObserver(mwc);

        primaryStage.show();
        m.readXML(new File("resources/playback_small.xml"));
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                m.stopThreads();
            }
        });

        //------------------------


        //------------------------
    }


    public static void main(String[] args) {
        launch(args);
    }
}
