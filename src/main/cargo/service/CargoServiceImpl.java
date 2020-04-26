package main.cargo.service;

import main.transportation.domain.Transportation;
import main.cargo.domain.Cargo;
import main.cargo.exception.unckecked.CargoDeleteConstraintViolationException;
import main.cargo.repo.CargoRepo;
import main.cargo.search.CargoSearchCondition;

import java.util.*;

public class CargoServiceImpl implements CargoService {

    private CargoRepo cargoRepo;

    public CargoServiceImpl(CargoRepo cargoRepo) {
        this.cargoRepo = cargoRepo;
    }

    @Override
    public void save(Cargo cargo) {
        cargoRepo.save(cargo);
    }

    @Override
    public Cargo findById(Long id) {
      return Optional.ofNullable(cargoRepo.findById(id)).orElse(null);
    }

    @Override
    public Cargo getByIdFetchingTransportations(Long id) {
     return Optional.ofNullable(cargoRepo.getByIdFetchingTransportations(id)).orElse(null);
    }

    @Override
    public List<Cargo> getAll() {
        return cargoRepo.getAll();
    }

    @Override
    public int countAll() {
        return this.cargoRepo.countAll();
    }

    @Override
    public List<Cargo> findByName(String name) {
        return Optional.ofNullable(cargoRepo.findByName(name)).map(cargo -> Arrays.asList(cargo))
                .orElse(Collections.emptyList());
//        return (found2.isPresent())
//        return (found == null || found.length == 0) ? Collections.emptyList() : Arrays.asList(found);
    }

    @Override
    public boolean deleteById(Long id) {


//      Optional.ofNullable(this.getByIdFetchingTransportations(id))
//              .ifPresent(cargo -> {
//                        Optional.ofNullable(cargo.getTransportations()).ifPresent(transportation->
//                        {
//                          if(transportation.size()>0) throw new CargoDeleteConstraintViolationException(cargo.getId());
//                        });
//              });
        Cargo cargo = this.getByIdFetchingTransportations(id);

        if (cargo != null) {
            List<Transportation> transportations = cargo.getTransportations();
            boolean hasTransportations = transportations != null && transportations.size() > 0;
            if (hasTransportations) {
                throw new CargoDeleteConstraintViolationException(id);
            }

            return cargoRepo.deleteById(id);
        } else {
            return false;
        }
    }

    @Override
    public void printAll() {
        Optional.ofNullable(cargoRepo.getAll()).ifPresent(cargos ->
                cargos.forEach(cargo-> System.out.println(cargo)));
    }

    @Override
    public boolean update(Cargo cargo) {
        if (cargo != null) {
            return cargoRepo.update(cargo);
        }

        return false;
    }

    @Override
    public List<Cargo> search(CargoSearchCondition cargoSearchCondition) {
        return cargoRepo.search(cargoSearchCondition);
    }
}
