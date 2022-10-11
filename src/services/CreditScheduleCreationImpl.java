package services;

import model.CreditSchedule;
import model.InputData;
import model.InterestSummary;
import model.Rate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CreditScheduleCreationImpl implements ICreditScheduleCreation {
    private final IPrintingService printingService;
    private final IInterestSummary interestSummary;
    private final List<CreditSchedule> ratesSchedule = new ArrayList<>();

    public CreditScheduleCreationImpl(IPrintingService printingService, IInterestSummary interestSummary) {
        this.interestSummary = interestSummary;
        this.printingService = printingService;
    }

    @Override
    public List<CreditSchedule> creditSchedule(InputData inputData) {

        printingService.printCreditParameters(inputData);

        RateAmountCalculationImpl rateAmountCalculation = new RateAmountCalculationImpl();
        LocalDate rateDate = inputData.getCreditDisbursementDate();
        BigDecimal rateNumber = BigDecimal.ZERO;
        BigDecimal creditCapitalResidual = inputData.getCreditAmount();
        BigDecimal creditMonthsResidual = inputData.getCreditDuration();

        for (int i = 0; i < inputData.getCreditDuration().intValue(); i++) {

            Rate rate = rateAmountCalculation.rate(inputData, creditCapitalResidual);
            rateNumber = rateNumber.add(BigDecimal.ONE);
            rateDate = rateDate.plusMonths(1);
            creditCapitalResidual = creditCapitalResidual.subtract(rate.getCapitalAmount());
            creditMonthsResidual = creditMonthsResidual.subtract(BigDecimal.ONE);

            CreditSchedule rateDetails = buildRate(rateNumber, rateDate, rate.getRateAmount(),
                    rate.getCapitalAmount(), rate.getInterestAmount(),
                    creditCapitalResidual, creditMonthsResidual);
            ratesSchedule.add(rateDetails);

        }
        InterestSummary interestSum = interestSummary.interestSummaryCalculation(ratesSchedule);
        printingService.printInterestSum(interestSum);
        printingService.printCreditSchedule(ratesSchedule);
        return ratesSchedule;
    }

    private CreditSchedule buildRate(
            BigDecimal rateNumber, LocalDate rateDate, BigDecimal rateAmount,
            BigDecimal capitalAmount, BigDecimal interestAmount, BigDecimal creditCapitalResidual, BigDecimal creditMonthsResidual) {

        return new CreditSchedule(rateNumber, rateDate, rateAmount, capitalAmount, interestAmount,
                creditCapitalResidual, creditMonthsResidual);
    }
}

