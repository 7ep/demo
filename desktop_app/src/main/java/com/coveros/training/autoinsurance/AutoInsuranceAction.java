package com.coveros.training.autoinsurance;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;


public final class AutoInsuranceAction {

    final int premiumIncreaseDollars;
    final WarningLetterEnum warningLetterEnum;
    final boolean isPolicyCanceled;
    final boolean isError;

    public AutoInsuranceAction(int premiumIncreaseDollars, WarningLetterEnum warningLetterEnum, boolean isPolicyCanceled, boolean isError) {

        this.premiumIncreaseDollars = premiumIncreaseDollars;
        this.warningLetterEnum = warningLetterEnum;
        this.isPolicyCanceled = isPolicyCanceled;
        this.isError = isError;
    }


    public final boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        AutoInsuranceAction rhs = (AutoInsuranceAction) obj;
        return new EqualsBuilder()
                .append(premiumIncreaseDollars, rhs.premiumIncreaseDollars)
                .append(warningLetterEnum, rhs.warningLetterEnum)
                .append(isPolicyCanceled, rhs.isPolicyCanceled)
                .append(isError, rhs.isError)
                .isEquals();
    }

    public final int hashCode() {
        // you pick a hard-coded, randomly chosen, non-zero, odd number
        // ideally different for each class
        return new HashCodeBuilder(99, 5).
                append(premiumIncreaseDollars).
                append(warningLetterEnum).
                append(isPolicyCanceled).
                append(isError).
                toHashCode();
    }

    public static AutoInsuranceAction createEmpty() {
        return new AutoInsuranceAction(-1, WarningLetterEnum.NONE, false, false);
    }

    public static AutoInsuranceAction createErrorResponse() {
        return new AutoInsuranceAction(-1, WarningLetterEnum.NONE, false, true);
    }

    public boolean isEmpty() {
        return this.equals(createEmpty());
    }

    public final String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
