package main.cargo.repo;

import main.cargo.domain.Cargo;
import main.cargo.search.CargoSearchCondition;
import main.common.business.repo.CommonRepo;

import java.util.List;

public interface CargoRepo extends CommonRepo<Cargo, Long> {

  Cargo getByIdFetchingTransportations(long id);

  Cargo[] findByName(String name);

  List<Cargo> search(CargoSearchCondition cargoSearchCondition);
}
