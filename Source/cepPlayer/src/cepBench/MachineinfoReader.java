package cepBench; /**
 * Created by mohammadreza on 8/5/2016.
 */

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by mohammadreza on 7/12/2016.
 */
public class MachineinfoReader {


    private DocumentBuilderFactory factory ;
    private DocumentBuilder builder ;
    private Document document;

    // create the objects neccessary to parse the xml files
    public MachineinfoReader(){
        try {
            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();

        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }


    /**
     * Reads the machineinfo.xml file and generates cepBench.MachineP objects. later appending them into MachineList
     * @param machinePList the MachineList to append the cepBench.MachineP objects into
     * @param fAddress the adress of Config Directory
     */
    public void readMachinesInfo(ArrayList<MachineP> machinePList, String fAddress){
        try {
            document = builder.parse(new File(fAddress));
            //Normalize the XML Structure; It's just too important !!
            document.getDocumentElement().normalize();

            NodeList nList = document.getElementsByTagName("machine");
            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node node = nList.item(temp);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element mElement = (Element) node;
                    MachineP tempMachineP = new MachineP();
                    tempMachineP.name = mElement.getElementsByTagName("name").item(0).getTextContent().toString();
                    tempMachineP.ip = mElement.getElementsByTagName("ip").item(0).getTextContent().toString();
                    tempMachineP.port= Integer.parseInt( mElement.getElementsByTagName("port").item(0).getTextContent().toString());
                    tempMachineP.eventFiles= mElement.getElementsByTagName("eventfiles").item(0).getTextContent().toString();;
                    machinePList.add(tempMachineP);
                }

            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }

    }

}
