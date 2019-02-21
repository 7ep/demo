package com.coveros.training.autoinsurance;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class AutoInsuranceAction {

    final int premiumIncreaseDollars;
    final WarningLetterEnum warningLetterEnum;
    final boolean isPolicyCanceled;

    public AutoInsuranceAction(int premiumIncreaseDollars, WarningLetterEnum warningLetterEnum, boolean isPolicyCanceled) {

        this.premiumIncreaseDollars = premiumIncreaseDollars;
        this.warningLetterEnum = warningLetterEnum;
        this.isPolicyCanceled = isPolicyCanceled;
    }


    public final boolean equals(@Nullable Object obj) {
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
                .isEquals();
    }

    public final int hashCode() {
        // you pick a hard-coded, randomly chosen, non-zero, odd number
        // ideally different for each class
        return new HashCodeBuilder(99, 5).
                append(premiumIncreaseDollars).
                append(warningLetterEnum).
                append(isPolicyCanceled).
                toHashCode();
    }

    public static AutoInsuranceAction createEmpty() {
        return new AutoInsuranceAction(-1, WarningLetterEnum.NONE, false);
    }

    public boolean isEmpty() {
        return this.equals(createEmpty());
    }

    public final String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
