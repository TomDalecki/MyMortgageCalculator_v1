package services;

import model.InputData;
import model.Rate;

public interface IRateAmountCalculationImpl {
    Rate rate(InputData inputData);
}
