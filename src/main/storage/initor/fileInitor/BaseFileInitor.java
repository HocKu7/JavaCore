package main.storage.initor.fileInitor;

import main.application.serviceholder.ServiceHolder;
import main.cargo.domain.Cargo;
import main.carrier.domain.Carrier;
import main.storage.initor.StorageInitor;
import main.transportation.domain.Transportation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class BaseFileInitor implements StorageInitor {

    protected   class ParsedTransportation {
        public String cargoRef;
        public String carrierRef;
        public Transportation transportation;

        ParsedTransportation() {
            transportation = new Transportation();
        }

        public String getCargoRef() {
            return cargoRef;
        }

        public void setCargoRef(String cargoRef) {
            this.cargoRef = cargoRef;
        }

        public String getCarrierRef() {
            return carrierRef;
        }

        public void setCarrierRef(String carrierRef) {
            this.carrierRef = carrierRef;
        }

        public Transportation getTransportation() {
            return transportation;
        }

        public void setTransportation(Transportation transportation) {
            this.transportation = transportation;
        }
    }
    protected List<Transportation> getTransportationsFromParsedObject(List<ParsedTransportation> transportations){
        List<Transportation> result=new ArrayList<>();
        for(ParsedTransportation transportation:transportations){
            result.add(transportation.transportation);
        }
        return result;
    }

    protected void persistCargo(Collection<Cargo> cargos) {
        for (Cargo cargo : cargos) {
            ServiceHolder.getInstance().getCargoService().save(cargo);
        }
    }
    protected   void persistCarriers(Collection<Carrier> carriers) {
        for (Carrier carrier : carriers) {
            ServiceHolder.getInstance().getCarrierService().save(carrier);
        }
    }
    protected void persistTransportations(Collection<Transportation> transportations){
        for(Transportation transportation:transportations){
            ServiceHolder.getInstance().getTransportationService().save(transportation);
        }
    }
    protected   void setReffBetweenEntries(Map<String, Cargo> cargoMap, Map<String, Carrier> carrierMap,
                                        List<InTextFileStorageInitor.ParsedTransportation> parsedTransportationsList) {
        for (InTextFileStorageInitor.ParsedTransportation parsedTransportation : parsedTransportationsList) {
            Cargo cargo = cargoMap.get(parsedTransportation.cargoRef);
            Carrier carrier = carrierMap.get(parsedTransportation.carrierRef);
            Transportation transportation = parsedTransportation.transportation;

            if (cargo != null) {
                List<Transportation> transportations =
                        cargo.getTransportations() == null ? new ArrayList<>() : cargo.getTransportations();
                transportations.add(transportation);
                cargo.setTransportations(transportations);
                transportation.setCargo(cargo);

            }

            if (carrier != null) {
                List<Transportation> transportations =
                        carrier.getTransportations() == null ? new ArrayList<>() : carrier.getTransportations();
                transportations.add(transportation);
                carrier.setTransportations(transportations);
                transportation.setCarrier(carrier);
            }


        }

    }

}
