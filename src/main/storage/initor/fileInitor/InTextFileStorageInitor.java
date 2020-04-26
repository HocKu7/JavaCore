package main.storage.initor.fileInitor;

import main.cargo.domain.Cargo;
import main.cargo.domain.ClothersCargo;
import main.cargo.domain.FoodCargo;
import main.carrier.domain.Carrier;
import main.carrier.domain.CarrierType;
import main.common.business.exception.checked.InitStorageException;
import main.common.solutions.utils.JavaUtilDateUtils;
import main.transportation.domain.Transportation;
import main.common.business.domain.BaseEntity;

import java.io.*;
import java.util.*;


public class InTextFileStorageInitor extends BaseFileInitor {
    private static final String FILE_PATH = "resource/TextMemory/main.storage.txt";
    private static final String STOP_SYMBOL = "}";

    @Override
    public void initStorage() throws InitStorageException {

        if(readFileStorage(FILE_PATH))
        {
            System.out.println("Read file is Complete!");
        }
        else{
            System.out.println("Read from file is fail");
        }

    }

    public  boolean readFileStorage(String path) {

        File inputFile = new File(FILE_PATH);
        Map<String, BaseEntity> mapOfEntity = new HashMap<>();
        List<ParsedTransportation> listOfTransportation = null;
        Map<String, Cargo> mapOfCargo = null;
        Map<String, Carrier> mapOfCarrier = null;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                switch (line) {
                    case "Cargo{": {
                        mapOfCargo = readCargoFromFile(bufferedReader);
                        break;
                    }
                    case "Carriers{": {
                        mapOfCarrier = readCarriersFromFile(bufferedReader);
                        break;
                    }
                    case "Transportation{": {
                        listOfTransportation = readTransportationFromFile(bufferedReader);
                        break;
                    }
                }
            }
            setReffBetweenEntries(mapOfCargo, mapOfCarrier, listOfTransportation);
            persistCargo(mapOfCargo.values());
            persistCarriers(mapOfCarrier.values());
            List<Transportation> transportationList = getTransportationsFromParsedObject(listOfTransportation);
            persistTransportations(transportationList);
        } catch (IOException e) {
            System.out.println("General cycle");
            return false;
        }

        return true;
    }




    private  Map<String, Cargo> readCargoFromFile(BufferedReader bufferedReader) throws IOException {
        String line;
        Map<String, Cargo> mapOfCargo = new HashMap<>();
        while ((line = bufferedReader.readLine()) != null && !line.equals(STOP_SYMBOL)) {
            line = line.trim();
            switch (line) {
                case "Clother{": {
                    Map<String, ClothersCargo> mapOfClother;
                    if ((mapOfClother = readCargoClotherFromFile(bufferedReader)) != null) {
                        mapOfCargo.putAll(mapOfClother);

                    }
                    break;
                }
                case "Food{": {
                    Map<String, FoodCargo> mapOfFood;
                    if ((mapOfFood = readCargoFoodFromFile(bufferedReader)) != null) {
                        mapOfCargo.putAll(mapOfFood);
                    }
                    break;

                }
            }
        }
        return mapOfCargo;
    }

    private  Map<String, Carrier> readCarriersFromFile(BufferedReader bufferedReader) throws IOException {
        Map<String, Carrier> mapOfCarrier = new HashMap<>();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            line = line.trim();
            if (line.equals(STOP_SYMBOL))
                break;
            Carrier newCarrier = new Carrier();
            String[] components = line.split("#");

            String id = components[0];
            //newCarrier.setId(IdGenerator.generateId());
            newCarrier.setName(components[1]);
            newCarrier.setAddress(components[2]);
            //skip set main.transportation. Set it latter
            //skip cargoType. Set auto
            switch (components[3]) {
                case "car": {
                    newCarrier.setCarrierType(CarrierType.CAR);
                    break;
                }
                case "train": {
                    newCarrier.setCarrierType(CarrierType.TRAIN);
                    break;
                }
                case "plane": {
                    newCarrier.setCarrierType(CarrierType.PLANE);
                    break;
                }
                case "ship": {
                    newCarrier.setCarrierType(CarrierType.SHIP);
                    break;
                }
                default: {
                    newCarrier.setCarrierType(null);
                }
            }

            mapOfCarrier.put(id, newCarrier);

        }
        return mapOfCarrier;
    }

    private  List<ParsedTransportation> readTransportationFromFile(BufferedReader bufferedReader) throws IOException {
        List<ParsedTransportation> listOfTransportation = new ArrayList<>();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            line = line.trim();
            if (line.equals(STOP_SYMBOL))
                break;
            ParsedTransportation parsedTransportation = new ParsedTransportation();
            String[] components = line.split("#");

            parsedTransportation.cargoRef = components[0];
            parsedTransportation.carrierRef = components[1];
            parsedTransportation.transportation.setDescription(components[2]);
            parsedTransportation.transportation.setBillTo(components[3]);
            parsedTransportation.transportation.setTransportationBeginDate(JavaUtilDateUtils.valueOf(components[4]));
            listOfTransportation.add(parsedTransportation);
        }
        return listOfTransportation;
    }

    private  Map<String, FoodCargo> readCargoFoodFromFile(BufferedReader bufferedReader) throws IOException {
        Map<String, FoodCargo> mapOfFood = new HashMap<>();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            line = line.trim();
            if (line.equals(STOP_SYMBOL))
                break;
            FoodCargo newFoodCargo = new FoodCargo();
            String[] components = line.split("#");

            String id = components[0];
            //newFoodCargo.setId(IdGenerator.generateId());
            newFoodCargo.setName(components[1]);
            newFoodCargo.setWeight(Integer.parseInt(components[2]));
            //skip set main.transportation. Set it latter
            //skip cargoType. Set auto
            newFoodCargo.setExpirationDate(JavaUtilDateUtils.valueOf(components[5]));
            newFoodCargo.setStoreTemperature(Integer.parseInt(components[6]));
            mapOfFood.put(id, newFoodCargo);

        }
        return mapOfFood;

    }

    private  Map<String, ClothersCargo> readCargoClotherFromFile(BufferedReader bufferedReader) throws IOException {
        Map<String, ClothersCargo> mapOfClother = new HashMap<>();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            line = line.trim();
            if (line.equals(STOP_SYMBOL))
                break;
            ClothersCargo newClothersCargo = new ClothersCargo();
            String[] components = line.split("#");
            String id = components[0];
            //newClothersCargo.setId(IdGenerator.generateId());
            newClothersCargo.setName(components[1]);
            newClothersCargo.setWeight(Integer.parseInt(components[2]));
            //skip set main.transportation. Set it latter
            //skip cargoType. Set auto
            newClothersCargo.setSize(components[5]);
            newClothersCargo.setMaterial(components[6]);
            mapOfClother.put(id, newClothersCargo);
        }
        return mapOfClother;
    }



}
