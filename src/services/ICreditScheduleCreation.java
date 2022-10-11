package services;

import model.CreditSchedule;
import model.InputData;

import java.util.List;

public interface ICreditScheduleCreation {
      List<CreditSchedule> creditSchedule(InputData inputData);
}
