package services;

import model.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        ReferentialAmounts referentialAmounts = new ReferentialAmounts(inputData.getCreditAmount(), inputData.getCreditDuration());
        LocalDate rateDate = inputData.getCreditDisbursementDate();
        BigDecimal rateNumber = BigDecimal.ZERO;
        BigDecimal creditCapitalResidual = inputData.getCreditAmount();
        BigDecimal creditMonthsResidual = inputData.getCreditDuration();

        for (int i = 0; i < inputData.getCreditDuration().intValue(); i++) {
            if(creditCapitalResidual.compareTo(BigDecimal.ZERO)==0){
                break;
            }

            Rate rate = rateAmountCalculation.rate(inputData, creditCapitalResidual, creditMonthsResidual, referentialAmounts);
            rateNumber = rateNumber.add(BigDecimal.ONE);
            rateDate = rateDate.plusMonths(1);
            creditCapitalResidual = creditCapitalResidual.subtract(rate.getCapitalAmount());
            creditMonthsResidual = creditMonthsResidual.subtract(BigDecimal.ONE);

            if(creditCapitalResidual.compareTo(BigDecimal.ZERO)==0){ //To jest do zmiany bo nie wygląda dobrze w harmonogramie
                creditMonthsResidual = BigDecimal.ZERO;
            }

            ///// TU JEST WYWOŁANIE LAMBDA DOTYCZĄCA NADPŁATY
            BigDecimal overpaymentAmount =
                    overpaymentCalculation.creditOverpaymentCalculation(inputData.getOverpayments(), rateNumber, creditCapitalResidual);

            creditCapitalResidual = creditCapitalResidual.subtract(overpaymentAmount);
            if(overpaymentAmount.compareTo(BigDecimal.ZERO)>0){
                referentialAmounts = new ReferentialAmounts(creditCapitalResidual, creditMonthsResidual);
            }


            CreditSchedule rateDetails = buildRate(rateNumber, rateDate, rate.getRateAmount(),
                    rate.getCapitalAmount(), rate.getInterestAmount(), overpaymentAmount,
                    creditCapitalResidual, creditMonthsResidual);
            ratesSchedule.add(rateDetails);

        }
        InterestSummary interestSum = interestSummary.interestSummaryCalculation(ratesSchedule);
        printingService.printInterestSum(interestSum);
        printingService.printCreditSchedule(ratesSchedule);
        return ratesSchedule;
    }

    ICreditOverpaymentCalculation overpaymentCalculation =
            (Map<Integer, BigDecimal> overpayments, BigDecimal rateNumber, BigDecimal creditCapitalResidual) -> {
        for (Map.Entry<Integer, BigDecimal> overpaymentDetails : overpayments.entrySet()) {
            if(rateNumber.equals(BigDecimal.valueOf(overpaymentDetails.getKey()))){
                return overpaymentDetails.getValue();
            }
        }
        return BigDecimal.ZERO;
    };
    private CreditSchedule buildRate(
            BigDecimal rateNumber, LocalDate rateDate, BigDecimal rateAmount,
            BigDecimal capitalAmount, BigDecimal interestAmount, BigDecimal overpaymentAmount, BigDecimal creditCapitalResidual, BigDecimal creditMonthsResidual) {

        return new CreditSchedule(rateNumber, rateDate, rateAmount, capitalAmount, interestAmount, overpaymentAmount,
                creditCapitalResidual, creditMonthsResidual);
    }
}

