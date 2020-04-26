package main.transportation.repo.impl;


import static main.common.business.repo.CommonRepoHelper.findEntityIndexInArrayStorageById;

import main.storage.Storage;
import main.transportation.domain.Transportation;
import main.common.solutions.utils.ArrayUtils;
import main.storage.IdGenerator;
import main.transportation.repo.TransportationRepo;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TransportationArrayRepoImpl implements TransportationRepo {

  private static final Transportation[] EMPTY_TRANSPORTATION_ARRAY = new Transportation[0];

  @Override
  public void save(Transportation transportation) {
    if (Storage.transportationIndex == Storage.transportationArray.length) {
      Transportation[] newTransportations =
          new Transportation[Storage.transportationArray.length * 2];
      ArrayUtils.copyArray(Storage.transportationArray, newTransportations);
      Storage.transportationArray = newTransportations;
    }

    transportation.setId(IdGenerator.generateId());
    Storage.transportationArray[Storage.transportationIndex] = transportation;
    Storage.transportationIndex++;
  }

  @Override
  public Transportation findById(Long id) {
    for (Transportation transportation : Storage.transportationArray) {
      if (transportation != null && transportation.getId().equals(id)) {
        return transportation;
      }
    }

    return null;
  }

  @Override
  public List<Transportation> getAll() {
    Transportation[] transportations = excludeNullableElementsFromArray(Storage.transportationArray);
    return transportations.length == 0 ? Collections.emptyList()
        : Arrays.asList(Storage.transportationArray);
  }

  @Override
  public int countAll() {
    return getAll().size();
  }

  private Transportation[] excludeNullableElementsFromArray(Transportation[] transportations) {
    int sizeOfArrWithNotNullElems = 0;

    for (Transportation transportation : transportations) {
      if (transportation != null) {
        sizeOfArrWithNotNullElems++;
      }
    }

    if (sizeOfArrWithNotNullElems == 0) {
      return EMPTY_TRANSPORTATION_ARRAY;
    } else {
      Transportation[] result = new Transportation[sizeOfArrWithNotNullElems];
      int index = 0;
      for (Transportation transportation : transportations) {
        if (transportation != null) {
          result[index++] = transportation;
        }
      }

      return result;
    }
  }


  @Override
  public boolean update(Transportation transportation) {
    return true;
  }

  @Override
  public boolean deleteById(Long id) {
    Integer indexToDelete = findEntityIndexInArrayStorageById(Storage.transportationArray, id);

    if (indexToDelete == null) {
      return false;
    } else {
      ArrayUtils.removeElement(Storage.transportationArray, indexToDelete);
      return true;
    }
  }
}
