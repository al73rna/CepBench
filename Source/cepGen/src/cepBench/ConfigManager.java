package cepBench;

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
public class ConfigManager {


    private DocumentBuilderFactory factory ;
    private DocumentBuilder builder ;
    private Document document;
    private String version;
    private String isTimestamp;
    private String serializer;
    private String timestampUnit;
    private String serializerVersion;
    //getter for the serializer version
    public String getSerializerVersion() {
        return serializerVersion;
    }
    //getter for the time stamp unit
    public String getTimestampUnit() {
        return timestampUnit;
    }


    //getter for the version
    public String getVersion(){
        return version;
    }
    // getter for the isTimestamp
    public String getIsTimestamp() {
        return isTimestamp;
    }
    //getter for the serializer type
    public String getSerializer() {
        return serializer;
    }
    // create the objects neccessary to parse the xml files
    public ConfigManager(){
        try {
            //eventTypeList = new ArrayList<EventType>();
            //machineList = new ArrayList<Machine>();
            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();

        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    /**
     * read config.xml and extract the general config along with the definitions of event types.
     * creates EventType Objects adding them into EventTypeList
     * @param eventTypeList eventTypeList
     * @param fAddress the adress of Config Directory
     */
    public void readEventTypes(ArrayList<EventType> eventTypeList,String fAddress){
            try {


                //Build Document
                document = builder.parse(new File(fAddress));
                //Normalize the XML Structure; It's just too important !!
                document.getDocumentElement().normalize();

                NodeList nList = document.getElementsByTagName("eventtype");
                isTimestamp = document.getElementsByTagName("istimestamp").item(0).getTextContent().toString();
                timestampUnit = document.getElementsByTagName("unit").item(0).getTextContent().toString();
                version = document.getElementsByTagName("version").item(0).getTextContent().toString();
                serializer = document.getElementsByTagName("serializer").item(0).getTextContent().toString();
                serializerVersion = document.getElementsByTagName("serializerVersion").item(0).getTextContent().toString();
                for (int temp = 0; temp < nList.getLength(); temp++) {

                    Node node = nList.item(temp);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) node;

                        EventType tempEventType = new EventType();
                        tempEventType.name = eElement.getElementsByTagName("name").item(0).getTextContent().toString();
                        tempEventType.rate = Double.parseDouble(eElement.getElementsByTagName("rate").item(0).getTextContent());
                        tempEventType.dispatchType = eElement.getElementsByTagName("dispatchtype").item(0).getTextContent().toString();


                        NodeList aList = eElement.getElementsByTagName("attribute");
                        for (int temp2 = 0; temp2 < aList.getLength(); temp2++) {
                            Node node2 = aList.item(temp2);
                            if (node2.getNodeType() == Node.ELEMENT_NODE) {
                                Element aElement = (Element) node2;
                                EventTypeAttribute tempEventTypeAttribute = new EventTypeAttribute(aElement.getElementsByTagName("name").item(0).getTextContent().toString(), aElement.getElementsByTagName("distribution").item(0).getTextContent().toString());
                                tempEventType.eventTypeAttributeList.add(tempEventTypeAttribute);
                            }
                        }


                        NodeList bList = eElement.getElementsByTagName("machine");
                        for (int temp3 = 0; temp3 < bList.getLength(); temp3++) {
                            Node node3 = bList.item(temp3);
                            if (node3.getNodeType() == Node.ELEMENT_NODE) {
                                Element bElement = (Element) node3;
                                EventTypeMachine tempEventTypeMachine = new EventTypeMachine();
                                tempEventTypeMachine.name = bElement.getElementsByTagName("name").item(0).getTextContent().toString();
                                tempEventTypeMachine.percentage = Double.parseDouble(bElement.getElementsByTagName("percentage").item(0).getTextContent().toString());
                                tempEventType.eventTypeMachineList.add(tempEventTypeMachine);
                            }
                        }

                        eventTypeList.add(tempEventType);


                    }
                }
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }


    }

    /**
     * Reads the machineinfo.xml file and generates Machine objects. later appending them into MachineList
     * @param machineList the MachineList to append the Machine objects into
     * @param fAddress the adress of Config Directory
     */
    public void readMachinesInfo(ArrayList<Machine> machineList , String fAddress){
        try {
            document = builder.parse(new File(fAddress));
            //Normalize the XML Structure; It's just too important !!
            document.getDocumentElement().normalize();

            NodeList nList = document.getElementsByTagName("machine");
            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node node = nList.item(temp);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element mElement = (Element) node;
                    Machine tempMachine = new Machine();
                    tempMachine.name = mElement.getElementsByTagName("name").item(0).getTextContent().toString();
                    tempMachine.ip = mElement.getElementsByTagName("ip").item(0).getTextContent().toString();
                    tempMachine.port= Integer.parseInt( mElement.getElementsByTagName("port").item(0).getTextContent().toString());
                    tempMachine.eventFiles= mElement.getElementsByTagName("eventfiles").item(0).getTextContent().toString();;
                    machineList.add(tempMachine);
                }

            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }

    }

}
