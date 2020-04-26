package main.storage.initor.fileInitor;

import main.cargo.domain.Cargo;
import main.cargo.domain.ClothersCargo;
import main.cargo.domain.FoodCargo;
import main.carrier.domain.Carrier;
import main.carrier.domain.CarrierType;
import main.common.business.exception.checked.InitStorageException;
import main.common.solutions.utils.JavaUtilDateUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import main.transportation.domain.Transportation;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlSAXFileInitor extends BaseFileInitor {
    private Map<String, Cargo> cargoMap = new HashMap<>();
    private Map<String, Carrier> carrierMap = new HashMap<>();
    private List<ParsedTransportation> parsedTransportationList = new ArrayList<>();

    @Override
    public void initStorage() throws InitStorageException {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            XmlFileHandler xmlFileHandler = new XmlFileHandler();
            //File file = new File("TextMemory/xmldata.xml");
            File file = new File("resource/TextMemory/xmldata.xml");
            saxParser.parse(file, xmlFileHandler);

            setReffBetweenEntries(cargoMap, carrierMap, parsedTransportationList);
            persistCargo(cargoMap.values());
            persistCarriers(carrierMap.values());
            List<Transportation> transportationList = getTransportationsFromParsedObject(parsedTransportationList);
            persistTransportations(transportationList);

        } catch (Exception e) {
            throw new InitStorageException(e.getMessage());
        }
    }


    private class XmlFileHandler extends DefaultHandler {

        private Cargo cargo;
        private Carrier carrier;
        private ParsedTransportation parsedTransportation;


        private String id;
        private Object curr;

        private StringBuilder stringBuilder = new StringBuilder();

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            stringBuilder.setLength(0);
            switch (qName) {
                case "cargo": {
                    id = attributes.getValue("id");
                    String type = attributes.getValue("type");
                    switch (type) {
                        case "FOOD": {
                            cargo = new FoodCargo();
                            break;
                        }
                        case "CLOTHERS": {
                            cargo = new ClothersCargo();
                            break;
                        }
                    }
                    curr = cargo;
                    break;
                }
                case "carrier": {
                    id = attributes.getValue("id");
                    carrier = new Carrier();
                    curr = carrier;
                    break;
                }
                case "transportation": {
                    String cargoRef = attributes.getValue("cargoref");
                    String carrierRef = attributes.getValue("carrierref");
                    parsedTransportation = new ParsedTransportation();
                    Transportation transportation = new Transportation();
                    parsedTransportation.setTransportation(transportation);
                    parsedTransportation.setCargoRef(cargoRef);
                    parsedTransportation.setCarrierRef(carrierRef);
                }

            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            String data = stringBuilder.toString();
            switch (qName) {
                case "cargo": {
                    cargoMap.put(id, cargo);
                    id = null;
                    cargo = null;
                    break;
                }
                case "carrier": {
                    carrierMap.put(id, carrier);
                    id = null;
                    carrier = null;
                    break;
                }
                case "transportation": {
                    parsedTransportationList.add(parsedTransportation);
                    parsedTransportation = null;
                    break;
                }
                case "name": {
                    if (itCargo()) {
                        cargo.setName(data);
                    }
                    if (itCarrier()) {
                        carrier.setName(data);
                    }
                    break;
                }
                case "weight": {
                    int weight = Integer.parseInt(data);
                    cargo.setWeight(weight);
                    break;
                }
                case "expirationDate": {
                    FoodCargo foodCargo = (FoodCargo) cargo;
                    foodCargo.setExpirationDate(JavaUtilDateUtils.valueOf(data));
                    break;
                }
                case "storeTemperature": {
                    FoodCargo foodCargo = (FoodCargo) cargo;
                    foodCargo.setStoreTemperature(Integer.parseInt(data));
                    break;
                }
                case "size": {
                    ClothersCargo clothersCargo = (ClothersCargo) cargo;
                    clothersCargo.setSize(data);
                    break;
                }
                case "material": {
                    ClothersCargo clothersCargo = (ClothersCargo) cargo;
                    clothersCargo.setMaterial(data);
                    break;
                }
                case "address": {
                    carrier.setAddress(data);
                    break;
                }
                case "type": {
                    switch (data) {
                        case "PLANE": {
                            carrier.setCarrierType(CarrierType.PLANE);
                            break;
                        }
                        case "CAR": {
                            carrier.setCarrierType(CarrierType.CAR);
                            break;
                        }
                        case "SHIP": {
                            carrier.setCarrierType(CarrierType.SHIP);
                            break;
                        }
                        case "TRAIN": {
                            carrier.setCarrierType(CarrierType.TRAIN);
                            break;
                        }
                    }
                    break;
                }
                case "billto": {
                    parsedTransportation.transportation.setBillTo(data);
                    break;
                }
                case "transportationBeginDate": {
                    parsedTransportation.transportation.setTransportationBeginDate(JavaUtilDateUtils.valueOf(data));
                    break;
                }
                case "description": {
                    parsedTransportation.transportation.setDescription(data);
                    break;
                }


            }

        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            String data = new String(ch, start, length);
            stringBuilder.append(data);
        }

        private boolean itCargo() {
            if (curr == null) return false;
            return cargo != null && cargo.getClass().equals(curr.getClass());
        }

        private boolean itCarrier() {
            if (curr == null) return false;
            return carrier != null && carrier.getClass().equals(curr.getClass());
        }

        private boolean itTransportation() {
            if (curr == null) return false;
            return parsedTransportation != null && parsedTransportation.getClass().equals(curr.getClass());
        }
    }
}
