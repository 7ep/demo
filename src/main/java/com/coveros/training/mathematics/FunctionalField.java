package com.coveros.training.mathematics;

@FunctionalInterface
public interface FunctionalField<F extends Enum<?>> {
    Object untypedField(F field);

    @SuppressWarnings("unchecked")
    default <V> V field(F field) {
        return (V) untypedField(field);
    }
}
