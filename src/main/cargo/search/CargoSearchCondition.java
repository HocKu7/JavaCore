package main.cargo.search;

import main.common.solutions.search.OrderType;
import main.cargo.domain.CargoField;

import java.util.Set;

public class CargoSearchCondition {

  private OrderType orderType;
  private Set<CargoField> sortFields;

  public boolean needSorting() {
    return sortFields != null && sortFields.size() > 0;
  }

  public OrderType getOrderType() {
    return orderType;
  }

  public void setOrderType(
      OrderType orderType) {
    this.orderType = orderType;
  }

  public Set<CargoField> getSortFields() {
    return sortFields;
  }

  public void setSortFields(
      Set<CargoField> sortFields) {
    this.sortFields = sortFields;
  }

  public boolean shouldSortByField(CargoField cargoField) {
    return sortFields != null && sortFields.contains(cargoField);
  }

  public boolean isAscOrdering() {
    return orderType == null || OrderType.ASC.equals(orderType);
  }
}
