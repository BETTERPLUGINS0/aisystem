/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Internal
 */
package me.zombie_striker.qav.util.xseries.base.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import me.zombie_striker.qav.util.xseries.base.annotations.XChange;
import org.jetbrains.annotations.ApiStatus;

@Target(value={ElementType.FIELD})
@Retention(value=RetentionPolicy.SOURCE)
@Documented
@ApiStatus.Internal
public @interface XChanges {
    public XChange[] value();
}

