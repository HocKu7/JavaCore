package test.CargoService;

import main.application.serviceholder.ServiceHolder;
import main.application.serviceholder.StorageType;
import main.cargo.domain.Cargo;
import main.cargo.service.CargoService;
import main.carrier.service.CarrierService;
import main.common.business.exception.checked.InitStorageException;
import main.storage.initor.InitStorageType;
import main.storage.initor.StorageInitor;
import main.transportation.service.TransportationService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.ls.LSOutput;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static main.storage.initor.StorageInitorFactory.getStorageInitor;

public class CargoServiceTest {
    static CargoService cargoService;
    static CarrierService carrierService;
    static TransportationService transportationService;
    static StorageInitor storageInitor;

    @BeforeClass
    public static void init() throws InitStorageException {
        ServiceHolder.initServiceHolder(StorageType.COLLECTION);
        cargoService = ServiceHolder.getInstance().getCargoService();
        carrierService = ServiceHolder.getInstance().getCarrierService();
        transportationService = ServiceHolder.getInstance().getTransportationService();
        //StorageInitor storageInitor = getStorageInitor(InitStorageType.TEXT_FILE);
        storageInitor = getStorageInitor(InitStorageType.MEMORY);
        storageInitor.initStorage();
    }

    @Test
    public void findByIdValidTest() {
        Cargo cargo=cargoService.findById(1L);
        Long id=cargo.getId();
        Assert.assertTrue(id==1L);
    }
    @Test
    public void findByIdNullTest() {
        Cargo cargo=cargoService.findById(null);
        Assert.assertTrue(cargo==null);
    }
    @Test
    public void findByIdInvalidTest() {
        Cargo cargo=cargoService.findById(1231231231L);
        Assert.assertTrue(cargo==null);
    }
    @Test
    public void findByName(){
        List<Cargo> cargos=cargoService.findByName("Milk");
        cargos.forEach(System.out::println);
    }
    @Test
    public void findByInvalidName(){
        List<Cargo> cargos=cargoService.findByName("Milk1231");
        Assert.assertTrue(cargos.equals(Collections.emptyList()));
    }
}
