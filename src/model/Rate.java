package model;

import java.math.BigDecimal;

public class Rate {
    private final BigDecimal capitalAmount;
    private final BigDecimal interestAmount;
    private final BigDecimal rateAmount;
    private final BigDecimal creditMonthsResidual;
    private final BigDecimal overpaymentAmount;
    private final ReferentialAmounts referentialAmounts;

    public Rate(BigDecimal capitalAmount, BigDecimal interestAmount, BigDecimal rateAmount,
                BigDecimal overpaymentAmount, BigDecimal creditMonthsResidual, ReferentialAmounts referentialAmounts) {
        this.capitalAmount = capitalAmount;
        this.interestAmount = interestAmount;
        this.rateAmount = rateAmount;
        this.overpaymentAmount = overpaymentAmount;
        this.creditMonthsResidual = creditMonthsResidual;
        this.referentialAmounts = referentialAmounts;
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

    public BigDecimal getOverpaymentAmount() {
        return overpaymentAmount;
    }

    public BigDecimal getCreditMonthsResidual() {
        return creditMonthsResidual;
    }

    public ReferentialAmounts getReferentialAmounts() {
        return referentialAmounts;
    }
}
