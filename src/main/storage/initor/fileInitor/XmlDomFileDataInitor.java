package main.storage.initor.fileInitor;

import main.cargo.domain.Cargo;
import main.cargo.domain.CargoType;
import main.cargo.domain.ClothersCargo;
import main.cargo.domain.FoodCargo;
import main.carrier.domain.Carrier;
import main.carrier.domain.CarrierType;
import main.common.business.exception.checked.InitStorageException;
import main.common.solutions.utils.JavaUtilDateUtils;
import main.common.solutions.utils.xml.dom.XmlDomUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import main.transportation.domain.Transportation;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static main.common.solutions.utils.xml.dom.XmlDomUtils.getOnlyElement;
import static main.common.solutions.utils.xml.dom.XmlDomUtils.getOnlyElementTextContent;

public class XmlDomFileDataInitor extends BaseFileInitor {
    private static final String FILE = "resource/TextMemory/xmldata.xml";
    private Map<String, Cargo> cargoMap;
    private Map<String, Carrier> carrierMap;
    private List<ParsedTransportation> transportations;

    public Map<String, Cargo> getCargoMap() {
        return cargoMap;
    }

    public Map<String, Carrier> getCarrierMap() {
        return carrierMap;
    }

    public List<ParsedTransportation> getTransportations() {
        return transportations;
    }

    @Override
    public void initStorage() throws InitStorageException {
        File file = null;
        try {
            file = getFileWithInitData();
            Document document = XmlDomUtils.getDocument(file);
           cargoMap = parseCargos(document);
            carrierMap = parseCarriers(document);
            transportations = parseTransportationsData(document);

            setReffBetweenEntries(cargoMap, carrierMap, transportations);
            persistCargo(cargoMap.values());
            persistCarriers(carrierMap.values());
            List<Transportation> transportationList = getTransportationsFromParsedObject(transportations);
            persistTransportations(transportationList);
        } catch (Exception e) {
            e.printStackTrace();
            throw new InitStorageException(e.getMessage());
        } finally {
            if (file != null) {
                //file.delete();
            }
        }
    }

    private File getFileWithInitData() throws IOException {
        //return FileUtils.createFileFromResource("init-data", "lesson12", FILE);
        //this file will be delete :(
        return new File(FILE);

    }

    private Map<String, Cargo> parseCargos(Document doc) throws ParseException {
        Map<String, Cargo> cargos = new LinkedHashMap<>();

        Element root = getOnlyElement(doc, "cargos");
        NodeList xmlCargos = root.getElementsByTagName("cargo");

        for (int i = 0; i < xmlCargos.getLength(); i++) {
            Map.Entry<String, Cargo> parsedData = parseCargo(xmlCargos.item(i));
            cargos.put(parsedData.getKey(), parsedData.getValue());
        }

        return cargos;
    }

    private Map.Entry<String, Cargo> parseCargo(Node cargoXml) throws ParseException {
        Element cargoElem = ((Element) cargoXml);

        String id = cargoElem.getAttribute("id");
        CargoType cargoType = CargoType.valueOf(cargoElem.getAttribute("type"));

        Cargo cargo;
        if (CargoType.FOOD.equals(cargoType)) {
            FoodCargo foodCargo = new FoodCargo();
            Date expirationDate = JavaUtilDateUtils
                    .valueOf(getOnlyElementTextContent(cargoElem, "expirationDate"));
            foodCargo.setExpirationDate(expirationDate);
            foodCargo.setStoreTemperature(
                    Integer.parseInt(getOnlyElementTextContent(cargoElem, "storeTemperature")));
            cargo = foodCargo;
        } else {
            ClothersCargo clothersCargo = new ClothersCargo();
            clothersCargo.setMaterial(getOnlyElementTextContent(cargoElem, "material"));
            clothersCargo.setSize(getOnlyElementTextContent(cargoElem, "size"));
            cargo = clothersCargo;
        }

        cargo.setName(getOnlyElementTextContent(cargoElem, "name"));
        cargo.setWeight(Integer.parseInt(getOnlyElementTextContent(cargoElem, "weight")));

        return new AbstractMap.SimpleEntry<>(id, cargo);
    }

    private Map<String, Carrier> parseCarriers(Document doc) throws ParseException {
        Map<String, Carrier> carriers = new LinkedHashMap<>();

        Element root = getOnlyElement(doc, "carriers");
        NodeList xmlCarriers = root.getElementsByTagName("carrier");

        for (int i = 0; i < xmlCarriers.getLength(); i++) {
            Map.Entry<String, Carrier> parsedData = parseCarrier(xmlCarriers.item(i));
            carriers.put(parsedData.getKey(), parsedData.getValue());
        }

        return carriers;
    }

    private Map.Entry<String, Carrier> parseCarrier(Node cargoXml) {
        Element carrierElement = ((Element) cargoXml);

        String id = carrierElement.getAttribute("id");
        Carrier carrier = new Carrier();

        carrier.setName(getOnlyElementTextContent(carrierElement, "name"));
        carrier.setAddress(getOnlyElementTextContent(carrierElement, "address"));
        String carrierTypeStr = getOnlyElementTextContent(carrierElement, "type");
        carrier.setCarrierType(CarrierType.valueOf(carrierTypeStr));

        return new SimpleEntry<>(id, carrier);
    }

    private List<ParsedTransportation> parseTransportationsData(Document doc) throws ParseException {
        List<ParsedTransportation> result = new ArrayList<>();

        Element root = getOnlyElement(doc, "transportations");
        NodeList xmlTransportations = root.getElementsByTagName("transportation");

        for (int i = 0; i < xmlTransportations.getLength(); i++) {
            ParsedTransportation parsedData = parseTransportationData(xmlTransportations.item(i));
            result.add(parsedData);
        }

        return result;
    }

    private ParsedTransportation parseTransportationData(Node transportationXml)
            throws ParseException {
        Element transportationElement = ((Element) transportationXml);

        ParsedTransportation result = new ParsedTransportation();
        String cargoRef = transportationElement.getAttribute("cargoref");
        result.setCargoRef(cargoRef);
        String carrierRef = transportationElement.getAttribute("carrierref");
        result.setCarrierRef(carrierRef);

        Transportation transportation = new Transportation();
        transportation.setBillTo(getOnlyElementTextContent(transportationElement, "billto"));
        transportation.setDescription(getOnlyElementTextContent(transportationElement, "description"));
        String beginDataStr = getOnlyElementTextContent(transportationElement, "transportationBeginDate");
        transportation.setTransportationBeginDate(JavaUtilDateUtils.valueOf(beginDataStr));
        result.setTransportation(transportation);

        return result;
    }

}
