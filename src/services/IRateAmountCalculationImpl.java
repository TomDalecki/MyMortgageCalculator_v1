package services;

import model.InputData;
import model.Rate;
import model.ReferentialAmounts;

import java.math.BigDecimal;

public interface IRateAmountCalculationImpl {
    Rate rate(InputData inputData, BigDecimal rateNumber, BigDecimal creditCapitalResidual, BigDecimal creditMonthsResidual,
              ReferentialAmounts referentialAmounts);
}
