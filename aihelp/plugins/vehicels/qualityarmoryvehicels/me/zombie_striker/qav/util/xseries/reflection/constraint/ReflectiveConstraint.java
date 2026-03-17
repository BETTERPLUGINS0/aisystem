/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Experimental
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.Contract
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.util.xseries.reflection.constraint;

import me.zombie_striker.qav.util.xseries.reflection.ReflectiveHandle;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Experimental
public interface ReflectiveConstraint {
    @Contract(pure=true)
    public String category();

    @Contract(pure=true)
    public String name();

    @NotNull
    @Contract(pure=true)
    public Result appliesTo(@NotNull ReflectiveHandle<?> var1, @NotNull Object var2);

    public static enum Result {
        INCOMPATIBLE,
        NOT_MATCHED,
        MATCHED;


        @ApiStatus.Internal
        public static Result of(boolean bl) {
            return bl ? MATCHED : NOT_MATCHED;
        }
    }
}

