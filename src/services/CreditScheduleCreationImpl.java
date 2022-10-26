package services;

import model.*;

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
    public void creditSchedule(InputData inputData) {

        printingService.printCreditParameters(inputData);

        RateAmountCalculationImpl rateAmountCalculation = new RateAmountCalculationImpl();
        ReferentialAmounts referentialAmounts = new ReferentialAmounts(inputData.getCreditAmount(), inputData.getCreditDuration());
        LocalDate rateDate = inputData.getCreditDisbursementDate();
        BigDecimal rateNumber = BigDecimal.ZERO;
        BigDecimal creditCapitalResidual = inputData.getCreditAmount();
        BigDecimal creditMonthsResidual = inputData.getCreditDuration();

        for (int i = 0; i < inputData.getCreditDuration().intValue(); i++) {
            if (creditCapitalResidual.compareTo(BigDecimal.ZERO) == 0) {
                break;
            }
            rateNumber = rateNumber.add(BigDecimal.ONE);

            Rate rate = rateAmountCalculation.rate(inputData, rateNumber, creditCapitalResidual, creditMonthsResidual, referentialAmounts);
            rateDate = rateDate.plusMonths(1);
            creditCapitalResidual = creditCapitalResidual.subtract(rate.getCapitalAmount()).subtract(rate.getOverpaymentAmount());
            creditMonthsResidual = rate.getCreditMonthsResidual();
            referentialAmounts = rate.getReferentialAmounts();

            CreditSchedule rateDetails = buildRate(rateNumber, rateDate, rate.getRateAmount(),
                    rate.getCapitalAmount(), rate.getInterestAmount(), rate.getOverpaymentAmount(),
                    creditCapitalResidual, creditMonthsResidual);
            ratesSchedule.add(rateDetails);

        }
        InterestSummary interestSum = interestSummary.interestSummaryCalculation(ratesSchedule);
        printingService.printInterestSum(interestSum);
        printingService.printCreditSchedule(ratesSchedule);
    }

    private CreditSchedule buildRate(
            BigDecimal rateNumber, LocalDate rateDate, BigDecimal rateAmount,
            BigDecimal capitalAmount, BigDecimal interestAmount, BigDecimal overpaymentAmount, BigDecimal creditCapitalResidual, BigDecimal creditMonthsResidual) {

        return new CreditSchedule(rateNumber, rateDate, rateAmount, capitalAmount, interestAmount, overpaymentAmount,
                creditCapitalResidual, creditMonthsResidual);
    }
}

