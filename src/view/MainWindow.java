package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Model;
import view_model.ViewModel;

import java.io.File;

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
        primaryStage.setScene(new Scene(root, 400, 275));

        MainWindowController mwc = fxl.getController();
        mwc.setViewModel(vm);
        vm.addObserver(mwc);

        primaryStage.show();
        m.loadXML(new File("resources/playback_small.xml"));
    }


    public static void main(String[] args) {
        launch(args);
    }
}
