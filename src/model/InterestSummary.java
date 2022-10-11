package model;

import java.math.BigDecimal;

public class InterestSummary {
    private final BigDecimal interestSum;

    public InterestSummary(BigDecimal interestSum) {
        this.interestSum = interestSum;
    }

    @Override
    public String toString() {
        return "ODSETKI DO ZAPŁATY ŁĄCZNIE: "
                + interestSum + "ZŁ";
    }
}
