/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation;

import java.io.OutputStream;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.AnnotationsWriter;

public class TypeAnnotationsWriter
extends AnnotationsWriter {
    public TypeAnnotationsWriter(OutputStream outputStream, ConstPool constPool) {
        super(outputStream, constPool);
    }

    @Override
    public void numAnnotations(int n) {
        super.numAnnotations(n);
    }

    public void typeParameterTarget(int n, int n2) {
        this.output.write(n);
        this.output.write(n2);
    }

    public void supertypeTarget(int n) {
        this.output.write(16);
        this.write16bit(n);
    }

    public void typeParameterBoundTarget(int n, int n2, int n3) {
        this.output.write(n);
        this.output.write(n2);
        this.output.write(n3);
    }

    public void emptyTarget(int n) {
        this.output.write(n);
    }

    public void formalParameterTarget(int n) {
        this.output.write(22);
        this.output.write(n);
    }

    public void throwsTarget(int n) {
        this.output.write(23);
        this.write16bit(n);
    }

    public void localVarTarget(int n, int n2) {
        this.output.write(n);
        this.write16bit(n2);
    }

    public void localVarTargetTable(int n, int n2, int n3) {
        this.write16bit(n);
        this.write16bit(n2);
        this.write16bit(n3);
    }

    public void catchTarget(int n) {
        this.output.write(66);
        this.write16bit(n);
    }

    public void offsetTarget(int n, int n2) {
        this.output.write(n);
        this.write16bit(n2);
    }

    public void typeArgumentTarget(int n, int n2, int n3) {
        this.output.write(n);
        this.write16bit(n2);
        this.output.write(n3);
    }

    public void typePath(int n) {
        this.output.write(n);
    }

    public void typePathPath(int n, int n2) {
        this.output.write(n);
        this.output.write(n2);
    }
}

