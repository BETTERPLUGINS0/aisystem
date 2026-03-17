/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation;

import java.io.OutputStream;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ByteArray;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;

public class AnnotationsWriter {
    protected OutputStream output;
    private ConstPool pool;

    public AnnotationsWriter(OutputStream outputStream, ConstPool constPool) {
        this.output = outputStream;
        this.pool = constPool;
    }

    public ConstPool getConstPool() {
        return this.pool;
    }

    public void close() {
        this.output.close();
    }

    public void numParameters(int n) {
        this.output.write(n);
    }

    public void numAnnotations(int n) {
        this.write16bit(n);
    }

    public void annotation(String string, int n) {
        this.annotation(this.pool.addUtf8Info(string), n);
    }

    public void annotation(int n, int n2) {
        this.write16bit(n);
        this.write16bit(n2);
    }

    public void memberValuePair(String string) {
        this.memberValuePair(this.pool.addUtf8Info(string));
    }

    public void memberValuePair(int n) {
        this.write16bit(n);
    }

    public void constValueIndex(boolean bl) {
        this.constValueIndex(90, this.pool.addIntegerInfo(bl ? 1 : 0));
    }

    public void constValueIndex(byte by) {
        this.constValueIndex(66, this.pool.addIntegerInfo(by));
    }

    public void constValueIndex(char c) {
        this.constValueIndex(67, this.pool.addIntegerInfo(c));
    }

    public void constValueIndex(short s) {
        this.constValueIndex(83, this.pool.addIntegerInfo(s));
    }

    public void constValueIndex(int n) {
        this.constValueIndex(73, this.pool.addIntegerInfo(n));
    }

    public void constValueIndex(long l) {
        this.constValueIndex(74, this.pool.addLongInfo(l));
    }

    public void constValueIndex(float f) {
        this.constValueIndex(70, this.pool.addFloatInfo(f));
    }

    public void constValueIndex(double d) {
        this.constValueIndex(68, this.pool.addDoubleInfo(d));
    }

    public void constValueIndex(String string) {
        this.constValueIndex(115, this.pool.addUtf8Info(string));
    }

    public void constValueIndex(int n, int n2) {
        this.output.write(n);
        this.write16bit(n2);
    }

    public void enumConstValue(String string, String string2) {
        this.enumConstValue(this.pool.addUtf8Info(string), this.pool.addUtf8Info(string2));
    }

    public void enumConstValue(int n, int n2) {
        this.output.write(101);
        this.write16bit(n);
        this.write16bit(n2);
    }

    public void classInfoIndex(String string) {
        this.classInfoIndex(this.pool.addUtf8Info(string));
    }

    public void classInfoIndex(int n) {
        this.output.write(99);
        this.write16bit(n);
    }

    public void annotationValue() {
        this.output.write(64);
    }

    public void arrayValue(int n) {
        this.output.write(91);
        this.write16bit(n);
    }

    protected void write16bit(int n) {
        byte[] byArray = new byte[2];
        ByteArray.write16bit(n, byArray, 0);
        this.output.write(byArray);
    }
}

