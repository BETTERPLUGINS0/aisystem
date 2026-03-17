/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.ASTree;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.IntConst;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.Visitor;

public class DoubleConst
extends ASTree {
    private static final long serialVersionUID = 1L;
    protected double value;
    protected int type;

    public DoubleConst(double d, int n) {
        this.value = d;
        this.type = n;
    }

    public double get() {
        return this.value;
    }

    public void set(double d) {
        this.value = d;
    }

    public int getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return Double.toString(this.value);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.atDoubleConst(this);
    }

    public ASTree compute(int n, ASTree aSTree) {
        if (aSTree instanceof IntConst) {
            return this.compute0(n, (IntConst)aSTree);
        }
        if (aSTree instanceof DoubleConst) {
            return this.compute0(n, (DoubleConst)aSTree);
        }
        return null;
    }

    private DoubleConst compute0(int n, DoubleConst doubleConst) {
        int n2 = this.type == 405 || doubleConst.type == 405 ? 405 : 404;
        return DoubleConst.compute(n, this.value, doubleConst.value, n2);
    }

    private DoubleConst compute0(int n, IntConst intConst) {
        return DoubleConst.compute(n, this.value, intConst.value, this.type);
    }

    private static DoubleConst compute(int n, double d, double d2, int n2) {
        double d3;
        switch (n) {
            case 43: {
                d3 = d + d2;
                break;
            }
            case 45: {
                d3 = d - d2;
                break;
            }
            case 42: {
                d3 = d * d2;
                break;
            }
            case 47: {
                d3 = d / d2;
                break;
            }
            case 37: {
                d3 = d % d2;
                break;
            }
            default: {
                return null;
            }
        }
        return new DoubleConst(d3, n2);
    }
}

