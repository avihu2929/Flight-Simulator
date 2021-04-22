package model;

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

public class Model extends Observable {

    String FeaturesList;
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

    public void readCSV(File file) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String line = null;
        while ((line= bufferedReader.readLine())!=null){

        }

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
                    FeaturesList+=name+"\n";
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
                    notifyObservers();

                }
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

    }

    public String getFeaturesList(){
        return FeaturesList;
    }
}
