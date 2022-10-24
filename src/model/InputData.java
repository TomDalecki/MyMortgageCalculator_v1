package model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class InputData {
    private BigDecimal creditAmount = BigDecimal.valueOf(550000);
    private LocalDate creditDisbursementDate = LocalDate.of(2022, 10, 1);
    private BigDecimal percentAmount = BigDecimal.valueOf(3.63);
    private BigDecimal percentAmountToCalculate;
    private final RateType rateType = RateType.DECREASING;
    private BigDecimal creditDuration = BigDecimal.valueOf(360);
    private Map<Integer, BigDecimal> overpayments = Map.of(
            2, BigDecimal.valueOf(10000),
            5, BigDecimal.valueOf(10000)
    );


//    public InputData withOverpayments Map<BigDecimal, BigDecimal> {
//        this.overpayments = overpayments;
//        return this;
//    }

    public InputData withCreditAmount(BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
        return this;
    }

    public InputData withCreditDisbursementDate(LocalDate creditDisbursementDate) {
        this.creditDisbursementDate = creditDisbursementDate;
        return this;
    }

    public InputData withPercentAmount(BigDecimal percentAmount) {
        this.percentAmount = percentAmount;
        percentAmountToCalculate = percentAmount.divide(BigDecimal.valueOf(100), 5, RoundingMode.HALF_UP);
        return this;
    }

    public InputData withCreditDuration(BigDecimal creditDuration) {
        this.creditDuration = creditDuration;
        return this;
    }

    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    public LocalDate getCreditDisbursementDate() {
        return creditDisbursementDate;
    }

    public BigDecimal getPercentAmount() {
        return percentAmount;
    }

    public BigDecimal getPercentAmountToCalculate() {
        return percentAmountToCalculate;
    }

    public BigDecimal getCreditDuration() {
        return creditDuration;
    }

    public RateType getRateType() {
        return rateType;
    }

    public Map<Integer, BigDecimal> getOverpayments() {
        return overpayments;
    }
}
