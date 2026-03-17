/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package nl.sbdeveloper.vehiclesplus.utils.math;

import lombok.Generated;

public class Quaternion {
    double x;
    double y;
    double z;
    double w;

    public Quaternion(double d, double d2, double d3) {
        this.x = 0.0;
        this.y = 0.0;
        this.z = 0.0;
        this.w = 1.0;
        this.rotateZ(-d3);
        this.rotateY(-d);
        this.rotateX(d2);
    }

    public void rotateX(double d) {
        double d2 = d / 2.0;
        float f = (float)Math.cos(d2);
        float f2 = (float)Math.sin(d2);
        this.set(this.x * (double)f + this.w * (double)f2, this.y * (double)f + this.z * (double)f2, this.z * (double)f - this.y * (double)f2, this.w * (double)f - this.x * (double)f2);
        this.normalize();
    }

    public void rotateY(double d) {
        double d2 = d / 2.0;
        float f = (float)Math.cos(d2);
        float f2 = (float)Math.sin(d2);
        this.set(this.x * (double)f - this.z * (double)f2, this.y * (double)f + this.w * (double)f2, this.z * (double)f + this.x * (double)f2, this.w * (double)f - this.y * (double)f2);
        this.normalize();
    }

    public void rotateZ(double d) {
        double d2 = d / 2.0;
        float f = (float)Math.cos(d2);
        float f2 = (float)Math.sin(d2);
        this.set(this.x * (double)f + this.y * (double)f2, this.y * (double)f - this.x * (double)f2, this.z * (double)f + this.w * (double)f2, this.w * (double)f - this.z * (double)f2);
        this.normalize();
    }

    public void normalize() {
        double d = 1.0 / Math.sqrt(this.dot(this));
        this.x *= d;
        this.y *= d;
        this.z *= d;
        this.w *= d;
    }

    public void set(double d, double d2, double d3, double d4) {
        this.x = d;
        this.y = d2;
        this.z = d3;
        this.w = d4;
    }

    public double dot(Quaternion quaternion) {
        return this.x * quaternion.x + this.y * quaternion.y + this.z * quaternion.z + this.w * quaternion.w;
    }

    @Generated
    public double getX() {
        return this.x;
    }

    @Generated
    public double getY() {
        return this.y;
    }

    @Generated
    public double getZ() {
        return this.z;
    }

    @Generated
    public double getW() {
        return this.w;
    }

    @Generated
    public Quaternion(double d, double d2, double d3, double d4) {
        this.x = d;
        this.y = d2;
        this.z = d3;
        this.w = d4;
    }
}

