package test.Serialization;

import main.application.serviceholder.ServiceHolder;
import main.application.serviceholder.StorageType;
import main.cargo.domain.Cargo;
import main.cargo.domain.FoodCargo;
import main.cargo.service.CargoService;
import main.carrier.service.CarrierService;
import main.common.business.exception.checked.InitStorageException;
import main.storage.Storage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import main.storage.initor.InitStorageType;
import main.storage.initor.StorageInitor;
import main.transportation.service.TransportationService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static main.storage.initor.StorageInitorFactory.getStorageInitor;

public class StorageSerializationTest {
    CargoService cargoService;
    CarrierService carrierService;
    TransportationService transportationService;
    StorageInitor storageInitor;

    @Before
    public void init() throws InitStorageException {
        ServiceHolder.initServiceHolder(StorageType.COLLECTION);
        cargoService = ServiceHolder.getInstance().getCargoService();
        carrierService = ServiceHolder.getInstance().getCarrierService();
        transportationService = ServiceHolder.getInstance().getTransportationService();
        //StorageInitor storageInitor = getStorageInitor(InitStorageType.TEXT_FILE);
        storageInitor = getStorageInitor(InitStorageType.SAX_PARSE);
        storageInitor.initStorage();
    }

    @Test
    public void testStorageSerialize() throws Exception {
        Path file = null;
        Storage expected = Storage.INSTANCE;
        file = Files.createTempFile("HW_13", ".txt");
        serializeToFile(Storage.INSTANCE, file.toFile().getAbsolutePath());
        Storage actual =readObjectFromFile(file.toFile().getAbsolutePath());
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSerializationCargos() throws Exception {
        List<Cargo> cargoList;
        cargoList = cargoService.getAll();
        Path file = null;
        file = Files.createTempFile("HW_13", ".txt");
        serializeToFile(cargoList,file.toFile().getAbsolutePath());
        List<Cargo> cargoListActual = readObjectFromFile(file.toFile().getAbsolutePath());
        //overridee Equals or write func for equals
        Assert.assertEquals(cargoList, cargoListActual);
    }

    private static <T> void serializeToFile(T entity, String file) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file))) {
            objectOutputStream.writeObject(entity);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static <T> T readObjectFromFile(String file) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))) {
            Object o = inputStream.readObject();
            return (T) o;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}