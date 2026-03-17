/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.ASTree;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.DoubleConst;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.Visitor;

public class IntConst
extends ASTree {
    private static final long serialVersionUID = 1L;
    protected long value;
    protected int type;

    public IntConst(long l, int n) {
        this.value = l;
        this.type = n;
    }

    public long get() {
        return this.value;
    }

    public void set(long l) {
        this.value = l;
    }

    public int getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return Long.toString(this.value);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.atIntConst(this);
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

    private IntConst compute0(int n, IntConst intConst) {
        long l;
        int n2 = this.type;
        int n3 = intConst.type;
        int n4 = n2 == 403 || n3 == 403 ? 403 : (n2 == 401 && n3 == 401 ? 401 : 402);
        long l2 = this.value;
        long l3 = intConst.value;
        switch (n) {
            case 43: {
                l = l2 + l3;
                break;
            }
            case 45: {
                l = l2 - l3;
                break;
            }
            case 42: {
                l = l2 * l3;
                break;
            }
            case 47: {
                l = l2 / l3;
                break;
            }
            case 37: {
                l = l2 % l3;
                break;
            }
            case 124: {
                l = l2 | l3;
                break;
            }
            case 94: {
                l = l2 ^ l3;
                break;
            }
            case 38: {
                l = l2 & l3;
                break;
            }
            case 364: {
                l = this.value << (int)l3;
                n4 = n2;
                break;
            }
            case 366: {
                l = this.value >> (int)l3;
                n4 = n2;
                break;
            }
            case 370: {
                l = this.value >>> (int)l3;
                n4 = n2;
                break;
            }
            default: {
                return null;
            }
        }
        return new IntConst(l, n4);
    }

    private DoubleConst compute0(int n, DoubleConst doubleConst) {
        double d;
        double d2 = this.value;
        double d3 = doubleConst.value;
        switch (n) {
            case 43: {
                d = d2 + d3;
                break;
            }
            case 45: {
                d = d2 - d3;
                break;
            }
            case 42: {
                d = d2 * d3;
                break;
            }
            case 47: {
                d = d2 / d3;
                break;
            }
            case 37: {
                d = d2 % d3;
                break;
            }
            default: {
                return null;
            }
        }
        return new DoubleConst(d, doubleConst.type);
    }
}

