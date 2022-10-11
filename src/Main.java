import model.InputData;
import services.*;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        InputData inputData = new InputData()
                .withCreditAmount(BigDecimal.valueOf(298000))
                .withCreditDuration(BigDecimal.valueOf(360))
                .withPercentAmount(BigDecimal.valueOf(6.69));

        IPrintingService printingService = new PrintingServiceImpl();
        ICreditScheduleCreation creditScheduleCreation = new CreditScheduleCreationImpl(
                printingService, InterestSummaryCalculation.createSummary());

        creditScheduleCreation.creditSchedule(inputData);





    }
}