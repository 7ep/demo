package com.coveros.training;

import com.google.auto.value.AutoValue;

@AutoValue
abstract class BorrowerData {

    static BorrowerData create(long id, String name) {
        return new AutoValue_BorrowerData(id, name);
    }

    abstract long id();
    abstract String name();
}
