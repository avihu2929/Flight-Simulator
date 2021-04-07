package view;

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

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MainWindowController {

    public Label label;

    public void openFile(){

        FileChooser fc = new FileChooser();
        fc.setTitle("Choose flight data file");
        fc.setInitialDirectory(new File("./resources"));
        File chosenFile = fc.showOpenDialog(null);
        if(chosenFile != null){
            readXML(chosenFile);
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
                    label.setText(label.getText()+"==============================================\n\n");
                }
                Node node = list.item(temp);

                if (node.getNodeType() == Node.ELEMENT_NODE) {

                    Element element = (Element) node;
                    String name = element.getElementsByTagName("name").item(0).getTextContent();
                    System.out.println("name : " + name);
                    label.setText(label.getText()+name+"\n");

                    String type = element.getElementsByTagName("type").item(0).getTextContent();
                    System.out.println("type : " + type);
                    label.setText(label.getText()+type+"\n");

                    if (element.getElementsByTagName("format").item(0)!=null){
                        String format = element.getElementsByTagName("format").item(0).getTextContent();
                        System.out.println("format : " + format);
                        label.setText(label.getText()+format+"\n");
                    }

                    String in_node = element.getElementsByTagName("node").item(0).getTextContent();
                    System.out.println("node : " + in_node);
                    label.setText(label.getText()+in_node+"\n\n");
                }
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

    }

}
