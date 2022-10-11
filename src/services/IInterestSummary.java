package services;

import model.CreditSchedule;
import model.InterestSummary;

import java.util.List;

public interface IInterestSummary {
InterestSummary interestSummaryCalculation(List<CreditSchedule> ratesSchedule);
}
