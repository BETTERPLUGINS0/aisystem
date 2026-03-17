/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.libs.xseries.base.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import nl.sbdeveloper.vehiclesplus.libs.xseries.base.annotations.XChange;

@Target(value={ElementType.FIELD})
@Retention(value=RetentionPolicy.SOURCE)
@Documented
public @interface XChanges {
    public XChange[] value();
}

