package main.cargo.repo.impl;


import static main.cargo.domain.CargoField.NAME;
import static main.cargo.domain.CargoField.WEIGHT;

import main.cargo.domain.Cargo;
import main.cargo.domain.CargoField;
import main.cargo.repo.CargoRepo;
import main.cargo.search.CargoSearchCondition;
import main.common.solutions.comparator.SimpleComparator;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public abstract class CommonCargoRepo implements CargoRepo {

    private static final List<CargoField> FIELDS_ORDER_TO_SORT_CARGOS = Arrays.asList(NAME, WEIGHT);

    private static final Comparator<Cargo> CARGO_NAME_COMPARATOR = (o1, o2) -> SimpleComparator.STRING_COMPARATOR.compare(o1.getName(), o2.getName());

    private static final Comparator<Cargo> CARGO_WEIGHT_COMPARATOR= Comparator.comparingInt(Cargo::getWeight);

    protected Comparator<Cargo> createCargoComparator(CargoSearchCondition searchCondition) {
        Comparator<Cargo> result = null;
        //NAME, WEIGHT
        for (CargoField cargoField : FIELDS_ORDER_TO_SORT_CARGOS) {
            if (searchCondition.shouldSortByField(cargoField)) {

                if (result == null) {
                    result = getComparatorForCargoField(cargoField);
                } else {
                    result = result.thenComparing(getComparatorForCargoField(cargoField));
                }
            }
        }


        return result;
    }


    private Comparator<Cargo> getComparatorForCargoField(CargoField cargoField) {
        switch (cargoField) {

            case NAME: {
                return CARGO_NAME_COMPARATOR;
            }
            case WEIGHT: {
                return CARGO_WEIGHT_COMPARATOR;
            }
        }

        return null;
    }


}
