package test;

import main.application.serviceholder.ServiceHolder;
import main.application.serviceholder.StorageType;
import main.cargo.domain.Cargo;
import main.cargo.service.CargoService;
import main.carrier.domain.Carrier;
import main.carrier.service.CarrierService;
import main.common.business.exception.checked.InitStorageException;
import main.storage.initor.InitStorageType;
import main.storage.initor.StorageInitor;
import main.transportation.domain.Transportation;
import main.transportation.service.TransportationService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static main.storage.initor.StorageInitorFactory.getStorageInitor;

public class XmlDomFileInitorTest {
    private static CargoService cargoService;
    private static CarrierService carrierService;
    private static TransportationService transportationService;

    @Before
    public void init() {
        ServiceHolder.initServiceHolder(StorageType.COLLECTION);
        cargoService = ServiceHolder.getInstance().getCargoService();
        carrierService = ServiceHolder.getInstance().getCarrierService();
        transportationService = ServiceHolder.getInstance().getTransportationService();
    }

    @Test
    public void notNullTest() throws InitStorageException {
        StorageInitor storageInitor = getStorageInitor(InitStorageType.XML_FILE);
        storageInitor.initStorage();

        Assert.assertNotNull(storageInitor);

    }

    @Test
    public void notNullMultiThreadTest() throws InitStorageException {
        StorageInitor storageInitor = getStorageInitor(InitStorageType.XML_MULTITHREAD);
        storageInitor.initStorage();
        Assert.assertNotNull(storageInitor);
    }

    @Test
    public void equalsOneThreadAndMultiThread() throws InitStorageException {
        long startTime = System.nanoTime();
        StorageInitor expected = getStorageInitor(InitStorageType.XML_FILE);
        expected.initStorage();
        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println("One thread: " + totalTime/1000000 +"ms");
        SnapshootStorage first = new SnapshootStorage();
        startTime = System.nanoTime();
        StorageInitor actual = getStorageInitor(InitStorageType.XML_MULTITHREAD);
        actual.initStorage();
        endTime   = System.nanoTime();
        totalTime = endTime - startTime;
        System.out.println("Multi thread: " + totalTime/1000000 +"ms");
        SnapshootStorage second = new SnapshootStorage();

        Assert.assertTrue(areEqualsSnapshot(first, second));

    }
    @Test
    public void aLotOfStart() throws InitStorageException {
        for(int i=0; i<10;i++) {
            StorageInitor actual = getStorageInitor(InitStorageType.XML_MULTITHREAD);
            actual.initStorage();
        }
        Assert.assertTrue(true);
    }
    @Test
    public void aLotOfStart_2() throws InitStorageException {
        for(int i=0; i<10;i++) {
            equalsOneThreadAndMultiThread();
        }
        Assert.assertTrue(true);
    }

    private boolean areEqualsSnapshot(SnapshootStorage first, SnapshootStorage second) {
        minusTwice(second, first);
        if(!equalsCargoList(first.getCargoList(),second.getCargoList())){
            return false;
        }
        //more equals for carriers and transportation
        //printData(second);
        return true;
    }

    private boolean equalsCargoList(List<Cargo> a, List<Cargo> b){
        Iterator<Cargo> iteratorB = b.listIterator();
        for(Cargo cargo:a){
            Cargo obj=null;
            if(iteratorB.hasNext()) {
                obj = iteratorB.next();
            }else{
                return false;
            }
            if(!cargo.getName().equals(obj.getName())){
                return false;
            }
            if (cargo.getWeight()!=obj.getWeight())
            {
                return false;
            }
            if(cargo.getCargoType()!=obj.getCargoType()){
                return false;
            }

        }
        return true;
    }
    private void minusTwice(SnapshootStorage from, SnapshootStorage what) {
        for(Cargo cargo:what.getCargoList()){
            from.getCargoList().remove(cargo);
        }
        for(Carrier carrier:what.getCarrierList()){
            from.getCarrierList().remove(carrier);
        }
        for(Transportation transportation:what.getTransportationList()){
            from.getTransportationList().remove(transportation);
        }
    }

    private void resetSingleton() throws NoSuchFieldException, IllegalAccessException {
        Field instance = ServiceHolder.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    private class SnapshootStorage {
        private List<Cargo> cargoList =new ArrayList<>();
        private List<Carrier> carrierList= new ArrayList<>();
        private List<Transportation> transportationList = new ArrayList<>();

        public List<Cargo> getCargoList() {
            return cargoList;
        }

        public List<Carrier> getCarrierList() {
            return carrierList;
        }

        public List<Transportation> getTransportationList() {
            return transportationList;
        }

        SnapshootStorage() {
            cargoList.addAll(cargoService.getAll());
            carrierList.addAll(carrierService.getAll());
            transportationList.addAll(transportationService.getAll());
        }
    }

    private void printData(SnapshootStorage sn) {
        for (Cargo cargo : sn.getCargoList()) {
            System.out.println(cargo);
        }
        for (Carrier carrier : sn.getCarrierList()) {
            System.out.println(carrier);
        }
        for (Transportation transportation : sn.getTransportationList()) {
            System.out.println(transportation);
        }
    }

}
