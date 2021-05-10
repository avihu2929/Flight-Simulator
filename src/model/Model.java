package model;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.stage.FileChooser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.FormatFlagsConversionMismatchException;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Model extends Observable {

    public void pauseTime() {
        run = false;
    }

    public  void stopTime(){
        run = false;
        time = 0;
        setChanged();
        notifyObservers("time");
    }

    private class TimeThread extends TimerTask{

        @Override
        public void run() {
            if (run) {
                setTime(time + 1);


            }

        }

    }
    boolean run = false;
    int row,col =0;
    String featuresList;
    float[][] flightData;
    int time = 0;
    TimeThread timeThread;
    Timer t;

    public void openFile(int type){

        //file chooser opens a window to choose xml file
        FileChooser fc = new FileChooser();
        fc.setTitle("Choose settings file");
        fc.setInitialDirectory(new File("./resources"));

        if (type==1){

            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter(".xml","*.xml"));
            File chosenFile = fc.showOpenDialog(null);

            if(chosenFile != null){
                readXML(chosenFile);
            }
        }else if(type==2){

            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter(".csv","*.csv"));
            File chosenFile = fc.showOpenDialog(null);

            if(chosenFile != null){
                try {
                    readCSV(chosenFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    public void initFlightData(File file) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String line = bufferedReader.readLine();
        for (int i=0;i<line.length();i++) {
            if (line.charAt(i) == ',') {
                col++;
            }
        }
        col++;
        while (line!=null){
                row++;
                line= bufferedReader.readLine();
        }

        flightData = new float[row][col];


        }



    public void readCSV(File file) throws IOException {
       // flightData = new float[2174][42];
        initFlightData(file);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        int prevComma=0;
        int countRow = 0;
        int countCol = 0;
        String line = bufferedReader.readLine();
        while (line!=null){
            for (int i=0;i<line.length();i++) {
                if (line.charAt(i) == ',') {


                    flightData[countRow][countCol] = Float.parseFloat(line.substring(prevComma, i));
                    prevComma = i + 1;
                    countCol++;
                }
            }
            prevComma=0;
            countCol=0;
            countRow++;
            line= bufferedReader.readLine();
        }
        setChanged();
        notifyObservers("csv");

    }

    public void readXML(File file){

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {

            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

            DocumentBuilder db = dbf.newDocumentBuilder();

            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();

            System.out.println("Root Element :" + doc.getDocumentElement().getNodeName());
            System.out.println("------");

            NodeList list = doc.getElementsByTagName("chunk");

            for (int temp = 0; temp < list.getLength(); temp++) {

                if (list.getLength()/2 == temp){
                    //label.setText(label.getText()+"\n==============================================\n\n");
                }

                Node node = list.item(temp);

                if (node.getNodeType() == Node.ELEMENT_NODE) {

                    Element element = (Element) node;
                    String name = element.getElementsByTagName("name").item(0).getTextContent();
                    System.out.println("name : " + name);
                    featuresList+=name+"\n";
                   // label.setText(label.getText()+name+"\n");

                    /*String type = element.getElementsByTagName("type").item(0).getTextContent();
                    System.out.println("type : " + type);
                    label.setText(label.getText()+type+"\n");

                    if (element.getElementsByTagName("format").item(0)!=null){
                        String format = element.getElementsByTagName("format").item(0).getTextContent();
                        System.out.println("format : " + format);
                        label.setText(label.getText()+format+"\n");
                    }

                    String in_node = element.getElementsByTagName("node").item(0).getTextContent();
                    System.out.println("node : " + in_node);
                    label.setText(label.getText()+in_node+"\n\n");*/
                    setChanged();
                    notifyObservers("xml");

                }
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

    }

    public void setTime(int time){
        if (time<row) {
            this.time = time;
            setChanged();
            notifyObservers("time");
        }

    }

    public void startTime(){
        //Time = new SimpleDoubleProperty();
        if (!run){
            run = true;
            timeThread = new TimeThread();
            t = new Timer();
            t.scheduleAtFixedRate(timeThread, 0,30);

        }


    }


    public void stopThreads(){
        timeThread.cancel();
        t.cancel();
    }


    public  float getFlightData(int feature){
        return flightData[time][feature];

    }

    public float[] getFlightData(int[] features){
        float[] data = new float[features.length];
        for (int i =0; i<features.length;i++){
            data[i]=flightData[time][features[i]];
        }
        return data;
    }

    public int getTime(){
        return time;
    }
    public String getFeaturesList(){
        return featuresList;
    }
}
