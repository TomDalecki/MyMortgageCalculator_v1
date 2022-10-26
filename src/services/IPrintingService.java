package services;

import model.CreditSchedule;
import model.InputData;
import model.InterestSummary;

import java.util.List;

public interface IPrintingService {
    String NEW_LINE = "\n";
    String CAPITAL = "KAPITAŁ: ";
    String INTEREST = "OPROCENTOWANIE: ";
    String DURATION = "OKRES KREDYTOWANIA: ";
    String CURRENCY = "ZŁ ";
    String PERCENT = "% ";


    void printCreditParameters(InputData inputData);

    void printCreditSchedule(List<CreditSchedule> ratesSchedule);

    void printInterestSum(InterestSummary interestSummaryCalculation);
}
