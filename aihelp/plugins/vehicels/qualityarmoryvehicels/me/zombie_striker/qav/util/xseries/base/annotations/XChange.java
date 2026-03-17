/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Internal
 */
package me.zombie_striker.qav.util.xseries.base.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import me.zombie_striker.qav.util.xseries.base.annotations.XChanges;
import org.jetbrains.annotations.ApiStatus;

@Target(value={ElementType.FIELD})
@Retention(value=RetentionPolicy.SOURCE)
@Repeatable(value=XChanges.class)
@Documented
@ApiStatus.Internal
public @interface XChange {
    public String version();

    public String from();

    public String to();
}

