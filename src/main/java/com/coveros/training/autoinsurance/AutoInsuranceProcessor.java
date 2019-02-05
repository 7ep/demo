package com.coveros.training.autoinsurance;

public class AutoInsuranceProcessor {

  public static AutoInsuranceAction process(int claims, int age) {

    if (claims == 0 && age >= 16 && age <= 25) {
      return new AutoInsuranceAction(50, WarningLetterEnum.NONE, false);
    }

    if (claims == 0 && age >= 26 && age <= 85) {
      return new AutoInsuranceAction(25, WarningLetterEnum.NONE, false);
    }

    if (claims == 1 && age >= 16 && age <= 25) {
      return new AutoInsuranceAction(100, WarningLetterEnum.LTR1, false);
    }

    if (claims == 1 && age >= 26 && age <= 85) {
      return new AutoInsuranceAction(50, WarningLetterEnum.NONE, false);
    }

    if (claims >= 2 && claims <= 4 && age >= 16 && age <= 25) {
      return new AutoInsuranceAction(400, WarningLetterEnum.LTR2, false);
    }

    if (claims >= 2 && claims <= 4 && age >= 26 && age <= 85) {
      return new AutoInsuranceAction(200, WarningLetterEnum.LTR3, false);
    }

    if (claims >= 5) {
      return new AutoInsuranceAction(0, WarningLetterEnum.NONE, false);
    }

    // for any outside condition
    return AutoInsuranceAction.createEmpty();

  }


}
