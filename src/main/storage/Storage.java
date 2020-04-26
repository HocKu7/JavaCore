package main.storage;


import main.cargo.domain.Cargo;
import main.carrier.domain.Carrier;
import main.transportation.domain.Transportation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Storage implements Serializable {

    public static final Storage INSTANCE = new Storage();
    private Storage(){

    }
  private static final int ARRAY_CAPACITY = 10;

  public static Cargo[] cargoArray = new Cargo[ARRAY_CAPACITY];
  public static int cargoIndex = 0;
  public static List<Cargo> cargoCollection = new ArrayList<>();

  public static Carrier[] carrierArray = new Carrier[ARRAY_CAPACITY];
  public static int carrierIndex = 0;
  public static List<Carrier> carrierCollection = new ArrayList<>();

  public static Transportation[] transportationArray = new Transportation[ARRAY_CAPACITY];
  public static int transportationIndex = 0;
  public static List<Transportation> transportationCollection = new ArrayList<>();

    private Object readResolve() {
        return INSTANCE;
    }


}
