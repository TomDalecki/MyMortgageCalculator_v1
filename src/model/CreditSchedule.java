package model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class CreditSchedule {
    private final BigDecimal rateNumber;
    private final LocalDate ratePaymentDate;
    private final BigDecimal rateAmount;
    private final BigDecimal capitalAmount;
    private final BigDecimal interestAmount;
    private final BigDecimal overpaymentAmount;
    private final BigDecimal capitalResidual;
    private final BigDecimal monthsResidual;

    public CreditSchedule(BigDecimal rateNumber, LocalDate ratePaymentDate, BigDecimal rateAmount,
                          BigDecimal capitalAmount, BigDecimal interestAmount, BigDecimal overpaymentAmount, BigDecimal capitalResidual, BigDecimal monthsResidual) {
        this.rateNumber = rateNumber;
        this.ratePaymentDate = ratePaymentDate;
        this.rateAmount = rateAmount.setScale(2, RoundingMode.HALF_UP);
        this.capitalAmount = capitalAmount.setScale(2, RoundingMode.HALF_UP);
        this.interestAmount = interestAmount.setScale(2, RoundingMode.HALF_UP);
        this.overpaymentAmount = overpaymentAmount.setScale(2, RoundingMode.HALF_UP);
        this.capitalResidual = capitalResidual.setScale(2, RoundingMode.HALF_UP);
        this.monthsResidual = monthsResidual;
    }

    public BigDecimal getInterestAmount() {
        return interestAmount;
    }

    @Override
    public String toString() {
        return "rateNo." + rateNumber +
                ", rateDate: " + ratePaymentDate +
                ", rateAmo: " + rateAmount +
                ", capitalAmo: " + capitalAmount +
                ", interestAmo: " + interestAmount +
                ", overpayment: " + overpaymentAmount +
                ", capitalResid: " + capitalResidual +
                ", monthsResid:" + monthsResidual;
    }
}
