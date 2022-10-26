package services;

import java.math.BigDecimal;
import java.util.Map;

@FunctionalInterface
public interface ICreditOverpaymentCalculation {
     BigDecimal creditOverpaymentCalculation(Map<Integer, BigDecimal> inputData, BigDecimal rateNumber);
}
