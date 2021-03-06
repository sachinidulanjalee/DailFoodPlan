package com.example.dailyfoodplan.model.enums;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public abstract class Units {
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({IMPERIAL, METRIC})
    public @interface Interface {
    }

    public static final int IMPERIAL = 0;
    public static final int METRIC = 1;
}
