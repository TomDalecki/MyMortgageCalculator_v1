package services;

import model.CreditSchedule;
import model.InterestSummary;

import java.math.BigDecimal;
import java.util.List;

public class InterestSummaryCalculation {
    public static IInterestSummary createSummary(){
        return ratesSchedule -> {
            BigDecimal interestSum = calculateInterestSum(ratesSchedule);
            return new InterestSummary(interestSum);
        };
    }

    private static BigDecimal calculateInterestSum(List<CreditSchedule> ratesSchedule) {
        BigDecimal sum = BigDecimal.ZERO;
        for (CreditSchedule interestAmount : ratesSchedule) {
            sum = sum.add(interestAmount.getInterestAmount());
        }
       return sum;
    }
}
