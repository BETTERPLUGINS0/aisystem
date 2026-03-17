/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Experimental
 *  org.jetbrains.annotations.ApiStatus$Internal
 */
package me.zombie_striker.qav.util.xseries.reflection.constraint;

import java.lang.reflect.Member;
import me.zombie_striker.qav.util.xseries.reflection.ReflectiveHandle;
import me.zombie_striker.qav.util.xseries.reflection.XAccessFlag;
import me.zombie_striker.qav.util.xseries.reflection.constraint.ReflectiveConstraint;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public enum VisibilityConstraint implements ReflectiveConstraint
{
    PUBLIC(XAccessFlag.PUBLIC),
    PRIVATE(XAccessFlag.PRIVATE),
    PROTECTED(XAccessFlag.PROTECTED);

    private final XAccessFlag accessFlag;

    private VisibilityConstraint(XAccessFlag xAccessFlag) {
        this.accessFlag = xAccessFlag;
    }

    @ApiStatus.Internal
    public XAccessFlag getAccessFlag() {
        return this.accessFlag;
    }

    @Override
    public ReflectiveConstraint.Result appliesTo(ReflectiveHandle<?> reflectiveHandle, Object object) {
        int n;
        if (object instanceof Class) {
            n = ((Class)object).getModifiers();
            if (this == PRIVATE) {
                return ReflectiveConstraint.Result.INCOMPATIBLE;
            }
            if (this == PROTECTED) {
                return ReflectiveConstraint.Result.of(!XAccessFlag.PUBLIC.isSet(n));
            }
        } else if (object instanceof Member) {
            n = ((Member)object).getModifiers();
        } else {
            return ReflectiveConstraint.Result.INCOMPATIBLE;
        }
        return ReflectiveConstraint.Result.of(this.accessFlag.isSet(n));
    }

    @Override
    public String category() {
        return "Visibility";
    }

    public String toString() {
        return this.getClass().getSimpleName() + "::" + this.name();
    }
}

