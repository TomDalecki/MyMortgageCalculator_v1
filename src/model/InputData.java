package model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Map;

public class InputData {
    private final LocalDate creditDisbursementDate = LocalDate.of(2022, 10, 1);
    private final Map<Integer, BigDecimal> overpayments = Map.of(
            2, BigDecimal.valueOf(10000),
            10, BigDecimal.valueOf(10000),
            100, BigDecimal.valueOf(10000)
    );
    private BigDecimal creditAmount = BigDecimal.valueOf(30000);
    private BigDecimal percentAmount = BigDecimal.valueOf(3.63);
    private BigDecimal percentAmountToCalculate;
    private RateType rateType = RateType.DECREASING;
    private BigDecimal creditDuration = BigDecimal.valueOf(180);
    private OverpaymentType overpaymentType = OverpaymentType.RATE_DECREASE;

    public InputData withCreditAmount(BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
        return this;
    }

    public InputData withRateType(RateType rateType) {
        this.rateType = rateType;
        return this;
    }

    public InputData withOverpaymentType(OverpaymentType overpaymentType) {
        this.overpaymentType = overpaymentType;
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

    public OverpaymentType getOverpaymentType() {
        return overpaymentType;
    }
}
