import model.InputData;
import model.OverpaymentType;
import model.RateType;
import services.*;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        InputData inputData = new InputData()
                .withCreditAmount(BigDecimal.valueOf(300000))
                .withCreditDuration(BigDecimal.valueOf(101))
                .withPercentAmount(BigDecimal.valueOf(6.69))
                .withRateType(RateType.CONSTANT)
                .withOverpaymentType(OverpaymentType.DURATION_DECREASE);

        IPrintingService printingService = new PrintingServiceImpl();
        ICreditScheduleCreation creditScheduleCreation = new CreditScheduleCreationImpl(
                printingService, InterestSummaryCalculation.createSummary());

        creditScheduleCreation.creditSchedule(inputData);
    }
}