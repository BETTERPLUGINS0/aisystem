/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.api.vehicles.statics;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import nl.sbdeveloper.vehiclesplus.api.vehicles.types.MovementType;

@Retention(value=RetentionPolicy.RUNTIME)
public @interface MovementTypeAssociation {
    public MovementType value();
}

