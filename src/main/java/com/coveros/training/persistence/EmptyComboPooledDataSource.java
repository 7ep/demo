package com.coveros.training.persistence;

import com.mchange.v2.c3p0.AbstractComboPooledDataSource;

import javax.naming.Referenceable;
import java.io.Serializable;

public final class EmptyComboPooledDataSource extends AbstractComboPooledDataSource implements Serializable, Referenceable {

}
