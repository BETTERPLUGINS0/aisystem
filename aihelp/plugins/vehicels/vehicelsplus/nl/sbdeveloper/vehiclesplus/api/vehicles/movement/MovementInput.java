/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package nl.sbdeveloper.vehiclesplus.api.vehicles.movement;

import lombok.Generated;

public class MovementInput {
    private boolean w;
    private boolean a;
    private boolean s;
    private boolean d;
    private boolean space;
    private boolean shift;

    @Generated
    public MovementInput(boolean bl, boolean bl2, boolean bl3, boolean bl4, boolean bl5, boolean bl6) {
        this.w = bl;
        this.a = bl2;
        this.s = bl3;
        this.d = bl4;
        this.space = bl5;
        this.shift = bl6;
    }

    @Generated
    public boolean isW() {
        return this.w;
    }

    @Generated
    public boolean isA() {
        return this.a;
    }

    @Generated
    public boolean isS() {
        return this.s;
    }

    @Generated
    public boolean isD() {
        return this.d;
    }

    @Generated
    public boolean isSpace() {
        return this.space;
    }

    @Generated
    public boolean isShift() {
        return this.shift;
    }

    @Generated
    public void setW(boolean bl) {
        this.w = bl;
    }

    @Generated
    public void setA(boolean bl) {
        this.a = bl;
    }

    @Generated
    public void setS(boolean bl) {
        this.s = bl;
    }

    @Generated
    public void setD(boolean bl) {
        this.d = bl;
    }

    @Generated
    public void setSpace(boolean bl) {
        this.space = bl;
    }

    @Generated
    public void setShift(boolean bl) {
        this.shift = bl;
    }

    @Generated
    public String toString() {
        return "MovementInput(w=" + this.isW() + ", a=" + this.isA() + ", s=" + this.isS() + ", d=" + this.isD() + ", space=" + this.isSpace() + ", shift=" + this.isShift() + ")";
    }
}

