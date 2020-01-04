package com.coveros.training;

@FunctionalInterface
public interface FunctionalField<F extends Enum<?>> {
    public Object untypedField(F field);

    @SuppressWarnings("unchecked")
    public default <V> V field(F field) {
        return (V) untypedField(field);
    }
}
