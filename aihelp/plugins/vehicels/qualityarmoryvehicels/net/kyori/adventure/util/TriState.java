/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.kyori.adventure.util;

import java.util.function.BooleanSupplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum TriState {
    NOT_SET,
    FALSE,
    TRUE;


    @Nullable
    public Boolean toBoolean() {
        switch (this) {
            case TRUE: {
                return Boolean.TRUE;
            }
            case FALSE: {
                return Boolean.FALSE;
            }
        }
        return null;
    }

    public boolean toBooleanOrElse(boolean bl) {
        switch (this) {
            case TRUE: {
                return true;
            }
            case FALSE: {
                return false;
            }
        }
        return bl;
    }

    public boolean toBooleanOrElseGet(@NotNull BooleanSupplier booleanSupplier) {
        switch (this) {
            case TRUE: {
                return true;
            }
            case FALSE: {
                return false;
            }
        }
        return booleanSupplier.getAsBoolean();
    }

    @NotNull
    public static TriState byBoolean(boolean bl) {
        return bl ? TRUE : FALSE;
    }

    @NotNull
    public static TriState byBoolean(@Nullable Boolean bl) {
        return bl == null ? NOT_SET : TriState.byBoolean((boolean)bl);
    }
}

