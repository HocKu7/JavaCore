package main.cargo.repo.impl;


import static main.common.business.repo.CommonRepoHelper.findEntityIndexInArrayStorageById;

import main.storage.Storage;
import main.cargo.domain.Cargo;
import main.cargo.search.CargoSearchCondition;
import main.common.solutions.utils.ArrayUtils;
import main.common.solutions.utils.CollectionUtils;
import main.storage.IdGenerator;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class CargoArrayRepoImpl extends CommonCargoRepo {

  private static final Cargo[] EMPTY_CARGO_ARRAY = new Cargo[0];

  @Override
  public Cargo getByIdFetchingTransportations(long id) {
    return findById(id);
  }

  @Override
  public Cargo[] findByName(String name) {
    Cargo[] searchResultWithNullableElems = getByNameIncludingNullElements(name);
    if (searchResultWithNullableElems == null
        || searchResultWithNullableElems.length == 0) {
      return EMPTY_CARGO_ARRAY;
    } else {
      return excludeNullableElementsFromArray(searchResultWithNullableElems);
    }
  }

  private Cargo[] getByNameIncludingNullElements(String name) {
    Cargo[] result = new Cargo[Storage.cargoArray.length];

    int curIndex = 0;
    for (Cargo carrier : Storage.cargoArray) {
      if (carrier != null && Objects.equals(carrier.getName(), name)) {
        result[curIndex++] = carrier;
      }
    }

    return result;
  }

  /**
   * [A,B,C, null, null] [A,B,C, null, null, null, D] [A,B,C]
   *
   * new String[3]
   */
  private Cargo[] excludeNullableElementsFromArray(Cargo[] cargos) {
    int sizeOfArrWithNotNullElems = 0;

    for (Cargo cargo : cargos) {
      if (cargo != null) {
        sizeOfArrWithNotNullElems++;
      }
    }

    if (sizeOfArrWithNotNullElems == 0) {
      return EMPTY_CARGO_ARRAY;
    } else {
      Cargo[] result = new Cargo[sizeOfArrWithNotNullElems];
      int index = 0;
      for (Cargo cargo : cargos) {
        if (cargo != null) {
          result[index++] = cargo;
        }
      }

      return result;
    }
  }

  @Override
  public List<Cargo> search(CargoSearchCondition searchCondition) {
    List<Cargo> cargos = getAll();

    if (CollectionUtils.isNotEmpty(cargos)) {
      if (searchCondition.needSorting()) {
        Comparator<Cargo> cargoComparator = createCargoComparator(searchCondition);
        cargos.sort(searchCondition.isAscOrdering() ? cargoComparator : cargoComparator.reversed());
      }
    }

    return cargos;
  }

  @Override
  public Cargo findById(Long id) {
    for (Cargo cargo : Storage.cargoArray) {
      if (cargo != null && id != null && id.equals(cargo.getId())) {
        return cargo;
      }
    }

    return null;
  }

  @Override
  public void save(Cargo cargo) {
    if (Storage.cargoIndex == Storage.cargoArray.length) {
      Cargo[] newCargos = new Cargo[Storage.cargoArray.length * 2];
      ArrayUtils.copyArray(Storage.cargoArray, newCargos);
      Storage.cargoArray = newCargos;
    }

    cargo.setId(IdGenerator.generateId());
    Storage.cargoArray[Storage.cargoIndex] = cargo;
    Storage.cargoIndex++;
  }

  @Override
  public boolean update(Cargo entity) {
    return true;
  }

  @Override
  public boolean deleteById(Long id) {
    Integer indexToDelete = findEntityIndexInArrayStorageById(Storage.cargoArray, id);

    if (indexToDelete == null) {
      return false;
    } else {
      ArrayUtils.removeElement(Storage.cargoArray, indexToDelete);
      return true;
    }
  }

  @Override
  public List<Cargo> getAll() {
    Cargo[] cargos = excludeNullableElementsFromArray(Storage.cargoArray);
    return cargos.length == 0 ? Collections.emptyList() : Arrays.asList(Storage.cargoArray);
  }

  @Override
  public int countAll() {
    return getAll().size();
  }

}
