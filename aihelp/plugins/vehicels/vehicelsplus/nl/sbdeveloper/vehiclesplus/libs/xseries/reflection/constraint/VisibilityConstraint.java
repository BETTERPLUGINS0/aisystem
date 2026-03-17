/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.constraint;

import nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.ReflectiveHandle;
import nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.constraint.ReflectiveConstraint;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public enum VisibilityConstraint implements ReflectiveConstraint
{
    PUBLIC{

        @Override
        public boolean appliesTo(ReflectiveHandle<?> reflectiveHandle) {
            return false;
        }
    };


    @Override
    public String category() {
        return "Visibility";
    }
}

