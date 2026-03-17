/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.libs.xseries.base.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import nl.sbdeveloper.vehiclesplus.libs.xseries.base.annotations.XChanges;

@Target(value={ElementType.FIELD})
@Retention(value=RetentionPolicy.SOURCE)
@Repeatable(value=XChanges.class)
@Documented
public @interface XChange {
    public String version();

    public String from();

    public String to();

    public String comment() default "";
}

