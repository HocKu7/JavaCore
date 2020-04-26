package main.carrier.exception.unchecked;

import main.common.business.exception.unchecked.OurCompanyException;

public class CarrierDeleteConstraintViolationException extends OurCompanyException {

  private static final String MESSAGE = "Cant delete main.carrier with id '%s'. There are transportations which relates to it!";

  public CarrierDeleteConstraintViolationException(String message) {
    super(message);
  }

  public CarrierDeleteConstraintViolationException(long carrierId) {
    this(String.format(MESSAGE, carrierId));
  }
}
