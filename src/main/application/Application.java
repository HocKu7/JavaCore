package main.application;

import static java.util.Collections.singletonList;
import static main.cargo.domain.CargoField.NAME;
import static main.cargo.domain.CargoField.WEIGHT;
import static main.storage.initor.StorageInitorFactory.getStorageInitor;

import main.application.serviceholder.ServiceHolder;
import main.application.serviceholder.StorageType;
import main.cargo.search.CargoSearchCondition;
import main.cargo.service.CargoService;
import main.carrier.service.CarrierService;
import main.common.business.exception.checked.InitStorageException;
import main.common.solutions.search.OrderType;
import main.common.solutions.utils.CollectionUtils;
import main.storage.initor.InitStorageType;
import main.storage.initor.StorageInitor;
import main.transportation.service.TransportationService;
import main.cargo.domain.Cargo;
import main.cargo.domain.CargoField;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;

public class Application {

  private static final String SEPARATOR = "--------------";
  private static CargoService cargoService;
  private static CarrierService carrierService;
  private static TransportationService transportationService;

  public static void main(String[] args) throws InitStorageException {


    ServiceHolder.initServiceHolder(StorageType.COLLECTION);
    cargoService = ServiceHolder.getInstance().getCargoService();
    carrierService = ServiceHolder.getInstance().getCarrierService();
    transportationService = ServiceHolder.getInstance().getTransportationService();
    //StorageInitor storageInitor = getStorageInitor(InitStorageType.TEXT_FILE);
    StorageInitor storageInitor = getStorageInitor(InitStorageType.SAX_PARSE);
    storageInitor.initStorage();

//    ReportService reportService = new ReportDefaultService(cargoService,carrierService,transportationService);
//    reportService.exportData();
    printStorageData();
//    demoSearchOperations();
//    demoSortOperations();
//    demoExceptions();
  }

  private static void demoSearchOperations() {
    System.out.println("SEARCH CARGO BY ID = 1");
    System.out.println(cargoService.findById(1L));
    printSeparator();

    System.out.println("SEARCH CARRIER BY ID = 8");
    System.out.println(carrierService.findById(8L));
    printSeparator();

    System.out.println("SEARCH CARGOES BY NAME = 'Clothers_Name_1'");
    CollectionUtils.printCollection(cargoService.findByName("Clothers_Name_1"));
    printSeparator();

    System.out.println("SEARCH CARRIERS BY NAME = 'Carrier_Name'");
    CollectionUtils.printCollection(carrierService.findByName("Carrier_Name"));
  }

  private static void printStorageData() {
    System.out.println("ALL CARGOS");
    cargoService.printAll();
    printSeparator();

    System.out.println("ALL CARRIERS");
    carrierService.printAll();
    printSeparator();

    System.out.println("ALL TRANSPOORTATIONS");
    transportationService.printAll();
    printSeparator();
  }

  private static void printSeparator() {
    System.out.println(SEPARATOR);
  }

  private static void demoSortOperations() {
    demoCargoSorting(singletonList(NAME), OrderType.ASC);
    demoCargoSorting(singletonList(NAME), OrderType.DESC);

    demoCargoSorting(singletonList(WEIGHT), OrderType.ASC);
    demoCargoSorting(singletonList(WEIGHT), OrderType.DESC);

    demoCargoSorting(Arrays.asList(NAME, WEIGHT), OrderType.ASC);
    demoCargoSorting(Arrays.asList(NAME, WEIGHT), OrderType.DESC);
  }

  private static String getOrderingConditionsAsString(CargoSearchCondition condition) {
    StringBuilder result = new StringBuilder();
    result.append(" ORDER BY ");

    Iterator<CargoField> iter = condition.getSortFields().iterator();
    while (iter.hasNext()) {
      CargoField fld = iter.next();
      result.append(fld);

      if (iter.hasNext()) {
        result.append(",");
      }
    }

    result.append(" ").append(condition.getOrderType());

    return result.toString();
  }

  private static void demoCargoSorting(Collection<CargoField> sortFields, OrderType orderType) {
    CargoSearchCondition cargoSearchCondition = new CargoSearchCondition();
    cargoSearchCondition.setOrderType(orderType);
    cargoSearchCondition.setSortFields(new LinkedHashSet<>(sortFields));
    System.out.println(
        "---------Sorting '" + getOrderingConditionsAsString(cargoSearchCondition) + "'------");
    cargoService.search(cargoSearchCondition);
    cargoService.printAll();
    System.out.println();
  }

  private static void demoExceptions() {
    System.out.println("------Demo  exceptions------------");
    Long firstCargo = cargoService.getAll().get(0).getId();
    Cargo cargo = cargoService.getByIdFetchingTransportations(firstCargo);
    System.out.println("Try to delete main.cargo");
    System.out.println("Cargo details:");
    System.out.println("id: " + cargo.getId());
    System.out.println("name: " + cargo.getName());
    System.out.println("total transportations: " + (cargo.getTransportations() != null ? cargo
        .getTransportations().size() : 0));
    System.out.println();
    try {
      cargoService.deleteById(cargo.getId());
    } catch (Exception e) {
      System.out.println("OOPS, something went wrong!");
      System.out.println(e.getMessage());
    }
  }
}
