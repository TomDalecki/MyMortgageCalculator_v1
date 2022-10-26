package services;

import model.InputData;
import model.OverpaymentType;
import model.Rate;
import model.ReferentialAmounts;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

public class RateAmountCalculationImpl {

    ICreditOverpaymentCalculation overpaymentCalculation =
            (Map<Integer, BigDecimal> overpayments, BigDecimal rateNumber) -> {
                for (Map.Entry<Integer, BigDecimal> overpaymentDetails : overpayments.entrySet()) {
                    if (rateNumber.equals(BigDecimal.valueOf(overpaymentDetails.getKey()))) {
                        return overpaymentDetails.getValue();
                    }
                }
                return BigDecimal.ZERO;
            };

    public Rate rate(InputData inputData, BigDecimal rateNumber, BigDecimal creditCapitalResidual, BigDecimal creditMonthsResidual,
                     ReferentialAmounts referentialAmounts) {

        switch (inputData.getRateType()) {
            case CONSTANT -> {
                if (inputData.getOverpaymentType() == OverpaymentType.RATE_DECREASE) {
                    return constantRateCalculationWithRateDecreaseOverpayment(
                            inputData, rateNumber, creditCapitalResidual, creditMonthsResidual, referentialAmounts);
                }

                if (inputData.getOverpaymentType() == OverpaymentType.DURATION_DECREASE) {
                    return constantRateCalculationWithCreditDurationDecreaseOverpayment(
                            inputData, rateNumber, creditCapitalResidual, creditMonthsResidual, referentialAmounts);
                } else {
                    throw new RuntimeException("Overpayment type doesn't handle");
                }
            }
            case DECREASING -> {
                if (inputData.getOverpaymentType() == OverpaymentType.RATE_DECREASE) {
                    return decreasingRateCalculationWithRateDecreaseOverpayment(
                            inputData, rateNumber, creditCapitalResidual, creditMonthsResidual, referentialAmounts);
                }

                if (inputData.getOverpaymentType() == OverpaymentType.DURATION_DECREASE) {
                    return decreasingRateCalculationWithCreditDurationDecreaseOverpayment(
                            inputData, rateNumber, creditCapitalResidual, creditMonthsResidual, referentialAmounts);
                } else {
                    throw new RuntimeException("Overpayment type doesn't handle");
                }

            }
            default -> throw new RuntimeException("Rate type error");
        }
    }

    private Rate decreasingRateCalculationWithCreditDurationDecreaseOverpayment(
            InputData inputData, BigDecimal rateNumber, BigDecimal creditCapitalResidual, BigDecimal creditMonthsResidual,
            ReferentialAmounts referentialAmounts) {

        BigDecimal capitalAmount = inputData.getCreditAmount()
                .divide(BigDecimal.valueOf(inputData.getCreditDuration().intValue()),
                        10, RoundingMode.HALF_UP);
        if (capitalAmount.compareTo(creditCapitalResidual) >= 0) {
            capitalAmount = creditCapitalResidual;
        }

        BigDecimal interestAmount = creditCapitalResidual.multiply(inputData.getPercentAmountToCalculate())
                .divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);
        BigDecimal rateAmount = capitalAmount.add(interestAmount).setScale(10, RoundingMode.HALF_UP);

        ///// TU JEST WYWOŁANIE LAMBDA DOTYCZĄCA NADPŁATY
        BigDecimal overpaymentAmount =
                overpaymentCalculation.creditOverpaymentCalculation(inputData.getOverpayments(), rateNumber);

        if (overpaymentAmount.compareTo(BigDecimal.ZERO) > 0) {
            creditCapitalResidual = creditCapitalResidual.subtract(overpaymentAmount);
            referentialAmounts = new ReferentialAmounts(creditCapitalResidual, creditMonthsResidual);
        }

        creditMonthsResidual = creditCapitalResidual.divide(capitalAmount, 0, RoundingMode.HALF_UP).subtract(BigDecimal.ONE);

        return new Rate(capitalAmount, interestAmount, rateAmount, overpaymentAmount, creditMonthsResidual, referentialAmounts);
    }

    private Rate decreasingRateCalculationWithRateDecreaseOverpayment(
            InputData inputData, BigDecimal rateNumber, BigDecimal creditCapitalResidual, BigDecimal creditMonthsResidual,
            ReferentialAmounts referentialAmounts) {

        BigDecimal capitalAmount = referentialAmounts.getReferentialCapitalAmount()
                .divide(BigDecimal.valueOf(referentialAmounts.getReferentialMonthsDuration().intValue()),
                        10, RoundingMode.HALF_UP);
        if (capitalAmount.compareTo(creditCapitalResidual) >= 0) {
            capitalAmount = creditCapitalResidual;
        }

        BigDecimal interestAmount = creditCapitalResidual.multiply(inputData.getPercentAmountToCalculate())
                .divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);
        BigDecimal rateAmount = capitalAmount.add(interestAmount).setScale(10, RoundingMode.HALF_UP);

        creditMonthsResidual = creditMonthsResidual.subtract(BigDecimal.ONE);

        BigDecimal overpaymentAmount =
                overpaymentCalculation.creditOverpaymentCalculation(inputData.getOverpayments(), rateNumber);

        if (overpaymentAmount.compareTo(BigDecimal.ZERO) > 0) {
            creditCapitalResidual = creditCapitalResidual.subtract(overpaymentAmount);
            referentialAmounts = new ReferentialAmounts(creditCapitalResidual, creditMonthsResidual);
        }
        return new Rate(capitalAmount, interestAmount, rateAmount, overpaymentAmount, creditMonthsResidual, referentialAmounts);
    }
    private Rate constantRateCalculationWithCreditDurationDecreaseOverpayment(
            InputData inputData, BigDecimal rateNumber, BigDecimal creditCapitalResidual,
            BigDecimal creditMonthsResidual, ReferentialAmounts referentialAmounts) {
        BigDecimal q = calculateQ(inputData.getPercentAmountToCalculate());

        BigDecimal rateAmount = inputData.getCreditAmount()
                .multiply(q.pow(inputData.getCreditDuration().intValue()))
                .multiply(q.subtract(BigDecimal.ONE))
                .divide((q.pow(inputData.getCreditDuration().intValue())).subtract(BigDecimal.ONE),
                        10, RoundingMode.HALF_UP);

        BigDecimal interestAmount = creditCapitalResidual.multiply(inputData.getPercentAmountToCalculate()).divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);
        BigDecimal capitalAmount = rateAmount.subtract(interestAmount).setScale(10, RoundingMode.HALF_UP);
        if (capitalAmount.compareTo(creditCapitalResidual) >= 0) {
            capitalAmount = creditCapitalResidual;
            rateAmount = capitalAmount.add(interestAmount);
        }

        ///// TU JEST WYWOŁANIE LAMBDA DOTYCZĄCA NADPŁATY
        BigDecimal overpaymentAmount =
                overpaymentCalculation.creditOverpaymentCalculation(inputData.getOverpayments(), rateNumber);

        if (overpaymentAmount.compareTo(BigDecimal.ZERO) > 0) {
            creditCapitalResidual = creditCapitalResidual.subtract(capitalAmount).subtract(overpaymentAmount);
            referentialAmounts = new ReferentialAmounts(creditCapitalResidual, creditMonthsResidual);
            creditMonthsResidual = creditMonthsResidualCalculation(rateAmount, referentialAmounts, inputData);
        } else {
            creditMonthsResidual = creditMonthsResidual.subtract(BigDecimal.ONE);
        }
        return new Rate(capitalAmount, interestAmount, rateAmount, overpaymentAmount, creditMonthsResidual, referentialAmounts);
    }

    private Rate constantRateCalculationWithRateDecreaseOverpayment(
            InputData inputData, BigDecimal rateNumber, BigDecimal creditCapitalResidual, BigDecimal creditMonthsResidual,
            ReferentialAmounts referentialAmounts) {
        BigDecimal q = calculateQ(inputData.getPercentAmountToCalculate());

        BigDecimal rateAmount = referentialAmounts.getReferentialCapitalAmount()
                .multiply(q.pow(referentialAmounts.getReferentialMonthsDuration().intValue()))
                .multiply(q.subtract(BigDecimal.ONE))
                .divide((q.pow(referentialAmounts.getReferentialMonthsDuration().intValue())).subtract(BigDecimal.ONE),
                        10, RoundingMode.HALF_UP);

        BigDecimal interestAmount = creditCapitalResidual.multiply(inputData.getPercentAmountToCalculate()).divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);
        BigDecimal capitalAmount = rateAmount.subtract(interestAmount).setScale(10, RoundingMode.HALF_UP);
        if (capitalAmount.compareTo(creditCapitalResidual) >= 0) {
            capitalAmount = creditCapitalResidual;
            rateAmount = capitalAmount.add(interestAmount);
        }

        ///// TU JEST WYWOŁANIE LAMBDA DOTYCZĄCA NADPŁATY
        BigDecimal overpaymentAmount =
                overpaymentCalculation.creditOverpaymentCalculation(inputData.getOverpayments(), rateNumber);

        if (overpaymentAmount.compareTo(BigDecimal.ZERO) > 0) {
            creditCapitalResidual = creditCapitalResidual.subtract(overpaymentAmount);
            referentialAmounts = new ReferentialAmounts(creditCapitalResidual, creditMonthsResidual);
        }
        creditMonthsResidual = creditMonthsResidual.subtract(BigDecimal.ONE);

        return new Rate(capitalAmount, interestAmount, rateAmount, overpaymentAmount, creditMonthsResidual, referentialAmounts);
    }

    private BigDecimal calculateQ(BigDecimal percentAmountToCalculate) {
        return BigDecimal.ONE.add(percentAmountToCalculate.divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP));
    }

    private BigDecimal creditMonthsResidualCalculation(
            BigDecimal rateAmount, ReferentialAmounts referentialAmounts, InputData inputData) {
        double R = rateAmount.doubleValue();
        double A = referentialAmounts.getReferentialCapitalAmount().doubleValue();
        double b = inputData.getPercentAmountToCalculate().doubleValue();
        double m = 12;

        BigDecimal creditMonthsResidual = BigDecimal.valueOf(
                        (Math.log((R / (R - A * (b / m)))))
                                / (Math.log(1 + (b / m))))
                                    .setScale(0, RoundingMode.HALF_DOWN);
        return creditMonthsResidual;
    }
}
