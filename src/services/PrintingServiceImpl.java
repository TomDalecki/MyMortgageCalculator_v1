package services;

import model.CreditSchedule;
import model.InputData;
import model.InterestSummary;

import java.util.List;

public class PrintingServiceImpl implements IPrintingService {
    @Override
    public void printCreditParameters(InputData inputData) {
        StringBuilder msg = new StringBuilder(); // przez StringBuilder trzeba rozwijać

        System.out.print(NEW_LINE);
        System.out.println(CAPITAL + inputData.getCreditAmount() + CURRENCY);
        System.out.println(INTEREST + inputData.getPercentAmount() + PERCENT);
        System.out.println(DURATION + inputData.getCreditDuration());
        System.out.print(NEW_LINE);

    }

    @Override
    public void printCreditSchedule(List<CreditSchedule> ratesSchedule) {
        System.out.println("HARMONOGRAM SPŁAT KREDYTU:");
        for (CreditSchedule creditSchedule : ratesSchedule) {
            System.out.println(creditSchedule);
        }
    }

    @Override
    public void printInterestSum(InterestSummary interestSummaryCalculation) {
        System.out.println(interestSummaryCalculation);
        System.out.print(NEW_LINE);
    }
}
