package services;

import model.InputData;
import model.Rate;
import model.ReferentialAmounts;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class RateAmountCalculationImpl {

    public Rate rate(InputData inputData, BigDecimal creditCapitalResidual, BigDecimal creditMonthsResidual, ReferentialAmounts referentialAmounts) {
        switch (inputData.getRateType()) {
            case CONSTANT -> {
                return constantRateCalculation(inputData, creditCapitalResidual, referentialAmounts);
            }
            case DECREASING -> {
                return decreasingRateCalculation(inputData, creditCapitalResidual, creditMonthsResidual, referentialAmounts);
            }
            default -> throw new RuntimeException("Rate type error");
        }
    }

    private Rate decreasingRateCalculation(InputData inputData, BigDecimal creditCapitalResidual,
                                           BigDecimal creditMonthsResidual, ReferentialAmounts referentialAmounts) {

        BigDecimal capitalAmount = referentialAmounts.getReferentialCapitalAmount()
                .divide(BigDecimal.valueOf(referentialAmounts.getReferentialMonthsDuration().intValue()),
                        10, RoundingMode.HALF_UP);
        BigDecimal interestAmount = creditCapitalResidual.multiply(inputData.getPercentAmountToCalculate())
                .divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);
        BigDecimal rateAmount = capitalAmount.add(interestAmount).setScale(10, RoundingMode.HALF_UP);
        return new Rate(capitalAmount, interestAmount, rateAmount);
    }

    private Rate constantRateCalculation(InputData inputData, BigDecimal creditCapitalResidual,
                                         ReferentialAmounts referentialAmounts) {
        BigDecimal q = calculateQ(inputData.getPercentAmountToCalculate());

        BigDecimal rateAmount = referentialAmounts.getReferentialCapitalAmount()
                .multiply(q.pow(referentialAmounts.getReferentialMonthsDuration().intValue()))
                .multiply(q.subtract(BigDecimal.ONE))
                .divide((q.pow(referentialAmounts.getReferentialMonthsDuration().intValue())).subtract(BigDecimal.ONE),
                        10, RoundingMode.HALF_UP);

        BigDecimal interestAmount = creditCapitalResidual.multiply(inputData.getPercentAmountToCalculate()).divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);
        BigDecimal capitalAmount = rateAmount.subtract(interestAmount).setScale(10, RoundingMode.HALF_UP);
        if(capitalAmount.compareTo(creditCapitalResidual)>=0){
            capitalAmount = creditCapitalResidual;
            rateAmount = capitalAmount.add(interestAmount);
        }

        return new Rate(capitalAmount, interestAmount, rateAmount);
    }
    private BigDecimal calculateQ(BigDecimal percentAmountToCalculate) {
        return BigDecimal.ONE.add(percentAmountToCalculate.divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP));
    }
}
