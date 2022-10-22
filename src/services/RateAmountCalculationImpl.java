package services;

import model.InputData;
import model.Rate;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class RateAmountCalculationImpl {

    public Rate rate(InputData inputData, BigDecimal creditCapitalResidual, BigDecimal creditMonthsResidual) {
        switch (inputData.getRateType()) {
            case CONSTANT -> {
                return constantRateCalculation(inputData, creditCapitalResidual);
            }
            case DECREASING -> {
                return decreasingRateCalculation(inputData, creditCapitalResidual, creditMonthsResidual);
            }
            default -> throw new RuntimeException("Rate type error");
        }
    }

    private Rate decreasingRateCalculation(InputData inputData, BigDecimal creditCapitalResidual, BigDecimal creditMonthsResidual) {

        //BigDecimal capitalAmount = inputData.getCreditAmount().divide(BigDecimal.valueOf(inputData.getCreditDuration().intValue()), 10, RoundingMode.HALF_UP);
        BigDecimal capitalAmount = creditCapitalResidual.divide(BigDecimal.valueOf(creditMonthsResidual.intValue()), 10, RoundingMode.HALF_UP);
        BigDecimal interestAmount = creditCapitalResidual.multiply(inputData.getPercentAmountToCalculate()).divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);
        BigDecimal rateAmount = capitalAmount.add(interestAmount).setScale(10, RoundingMode.HALF_UP);
        return new Rate(capitalAmount, interestAmount, rateAmount);
    }

    private Rate constantRateCalculation(InputData inputData, BigDecimal creditCapitalResidual) {
        BigDecimal q = calculateQ(inputData.getPercentAmountToCalculate());

        BigDecimal rateAmount = inputData.getCreditAmount()
                .multiply(q.pow(inputData.getCreditDuration().intValue()))
                .multiply(q.subtract(BigDecimal.ONE))
                .divide((q.pow(inputData.getCreditDuration().intValue())).subtract(BigDecimal.ONE), 10, RoundingMode.HALF_UP);

        BigDecimal interestAmount = creditCapitalResidual.multiply(inputData.getPercentAmountToCalculate()).divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);
        BigDecimal capitalAmount = rateAmount.subtract(interestAmount).setScale(10, RoundingMode.HALF_UP);
        return new Rate(capitalAmount, interestAmount, rateAmount);
    }

    private BigDecimal calculateQ(BigDecimal percentAmountToCalculate) {
        return BigDecimal.ONE.add(percentAmountToCalculate.divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP));
    }


}
