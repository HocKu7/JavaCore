package main.transportation.repo.impl;


import main.storage.Storage;
import main.transportation.domain.Transportation;
import main.storage.IdGenerator;
import main.transportation.repo.TransportationRepo;

import java.util.Iterator;
import java.util.List;

public class TransportationCollectionRepoImpl implements TransportationRepo {

  @Override
  public void save(Transportation transportation) {
    transportation.setId(IdGenerator.generateId());
    Storage.transportationCollection.add(transportation);
  }

  @Override
  public Transportation findById(Long id) {
    for (Transportation transportation : Storage.transportationCollection) {
      if (transportation.getId().equals(id)) {
        return transportation;
      }
    }

    return null;
  }

  @Override
  public List<Transportation> getAll() {
    return Storage.transportationCollection;
  }

  @Override
  public boolean update(Transportation transportation) {
    return true;
  }

  @Override
  public boolean deleteById(Long id) {
    boolean deleted = false;

    Iterator<Transportation> iter = Storage.transportationCollection.iterator();
    while (iter.hasNext()) {
      if (iter.next().getId().equals(id)) {
        iter.remove();
        deleted = true;
        break;
      }
    }
    return deleted;
  }

  @Override
  public int countAll() {
    return Storage.transportationCollection.size();
  }
}
