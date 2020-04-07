package com.coveros.training.autoinsurance;

class AutoInsuranceProcessor {

    private AutoInsuranceProcessor() {
        // private constructor to hide the public one.
    }

    static AutoInsuranceAction process(int claims, int age) {

        if (claims == 0 && age >= 16 && age <= 25) {
            return new AutoInsuranceAction(50, WarningLetterEnum.NONE, false, false);
        }

        if (claims == 0 && age >= 26 && age <= 85) {
            return new AutoInsuranceAction(25, WarningLetterEnum.NONE, false, false);
        }

        if (claims == 1 && age >= 16 && age <= 25) {
            return new AutoInsuranceAction(100, WarningLetterEnum.LTR1, false, false);
        }

        if (claims == 1 && age >= 26 && age <= 85) {
            return new AutoInsuranceAction(50, WarningLetterEnum.NONE, false, false);
        }

        if (claims >= 2 && claims <= 4 && age >= 16 && age <= 25) {
            return new AutoInsuranceAction(400, WarningLetterEnum.LTR2, false, false);
        }

        if (claims >= 2 && claims <= 4 && age >= 26 && age <= 85) {
            return new AutoInsuranceAction(200, WarningLetterEnum.LTR3, false, false);
        }

        if (claims >= 5) {
            return new AutoInsuranceAction(0, WarningLetterEnum.NONE, true, false);
        }

        // for any outside condition
        return AutoInsuranceAction.createErrorResponse();

    }


}
