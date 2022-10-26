package model;

import java.math.BigDecimal;

public class ReferentialAmounts {
    private final BigDecimal referentialCapitalAmount;
    private final BigDecimal referentialMonthsDuration;

    public ReferentialAmounts(BigDecimal referentialCapitalAmount, BigDecimal referentialMonthsDuration) {
        this.referentialCapitalAmount = referentialCapitalAmount;
        this.referentialMonthsDuration = referentialMonthsDuration;
    }

    public BigDecimal getReferentialCapitalAmount() {
        return referentialCapitalAmount;
    }

    public BigDecimal getReferentialMonthsDuration() {
        return referentialMonthsDuration;
    }
}
