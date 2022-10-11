package model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class InputData {
    private BigDecimal creditAmount = BigDecimal.valueOf(550000);
    private LocalDate creditDisbursementDate = LocalDate.of(2022, 10, 1);
    private BigDecimal percentAmount = BigDecimal.valueOf(3.63);
    private BigDecimal percentAmountToCalculate;
    private final RateType rateType = RateType.CONSTANT;
    private BigDecimal creditDuration = BigDecimal.valueOf(360);

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
        percentAmountToCalculate = percentAmount.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);
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
}
