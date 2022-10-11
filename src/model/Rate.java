package model;

import java.math.BigDecimal;

public class Rate {
    private final BigDecimal capitalAmount;
    private final BigDecimal interestAmount;
    private final BigDecimal rateAmount;

    public Rate(BigDecimal capitalAmount, BigDecimal interestAmount, BigDecimal rateAmount) {
        this.capitalAmount = capitalAmount;
        this.interestAmount = interestAmount;
        this.rateAmount = rateAmount;
    }

    public BigDecimal getCapitalAmount() {
        return capitalAmount;
    }

    public BigDecimal getInterestAmount() {
        return interestAmount;
    }

    public BigDecimal getRateAmount() {
        return rateAmount;
    }
}
