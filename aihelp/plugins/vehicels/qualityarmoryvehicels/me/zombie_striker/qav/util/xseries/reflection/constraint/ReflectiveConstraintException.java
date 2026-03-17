/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.util.xseries.reflection.constraint;

import me.zombie_striker.qav.util.xseries.reflection.ReflectiveHandle;
import me.zombie_striker.qav.util.xseries.reflection.XAccessFlag;
import me.zombie_striker.qav.util.xseries.reflection.constraint.ReflectiveConstraint;

public class ReflectiveConstraintException
extends RuntimeException {
    private final ReflectiveConstraint constraint;
    private final ReflectiveConstraint.Result result;

    private ReflectiveConstraintException(ReflectiveConstraint reflectiveConstraint, ReflectiveConstraint.Result result, String string) {
        super(string);
        this.constraint = reflectiveConstraint;
        this.result = result;
    }

    public ReflectiveConstraint getConstraint() {
        return this.constraint;
    }

    public ReflectiveConstraint.Result getResult() {
        return this.result;
    }

    public static ReflectiveConstraintException create(ReflectiveConstraint reflectiveConstraint, ReflectiveConstraint.Result result, ReflectiveHandle<?> reflectiveHandle, Object object) {
        String string;
        switch (result) {
            case MATCHED: {
                throw new IllegalArgumentException("Cannot create an exception if results are successful: " + reflectiveConstraint + " -> MATCHED");
            }
            case INCOMPATIBLE: {
                string = "The constraint " + reflectiveConstraint + " cannot be applied to " + reflectiveHandle;
                break;
            }
            case NOT_MATCHED: {
                string = "Found " + reflectiveHandle + " with JVM " + object + ", however it doesn't match the constraint: " + reflectiveConstraint + " - " + XAccessFlag.getModifiers(object).map(XAccessFlag::toString).orElse("[NO MODIFIER]");
                break;
            }
            default: {
                throw new AssertionError((Object)("Unknown reflective constraint result: " + (Object)((Object)result)));
            }
        }
        return new ReflectiveConstraintException(reflectiveConstraint, result, string);
    }
}

