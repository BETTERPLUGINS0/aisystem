/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Map;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.AnnotationsAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.AttributeInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.AnnotationsWriter;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.MemberValue;

public class AnnotationDefaultAttribute
extends AttributeInfo {
    public static final String tag = "AnnotationDefault";

    public AnnotationDefaultAttribute(ConstPool constPool, byte[] byArray) {
        super(constPool, tag, byArray);
    }

    public AnnotationDefaultAttribute(ConstPool constPool) {
        this(constPool, new byte[]{0, 0});
    }

    AnnotationDefaultAttribute(ConstPool constPool, int n, DataInputStream dataInputStream) {
        super(constPool, n, dataInputStream);
    }

    @Override
    public AttributeInfo copy(ConstPool constPool, Map<String, String> map) {
        AnnotationsAttribute.Copier copier = new AnnotationsAttribute.Copier(this.info, this.constPool, constPool, map);
        try {
            copier.memberValue(0);
            return new AnnotationDefaultAttribute(constPool, copier.close());
        } catch (Exception exception) {
            throw new RuntimeException(exception.toString());
        }
    }

    @Override
    void renameClass(String string, String string2) {
        try {
            MemberValue memberValue = this.getDefaultValue();
            memberValue.renameClass(string, string2);
            this.setDefaultValue(memberValue);
        } catch (Exception exception) {
            // empty catch block
        }
    }

    @Override
    void renameClass(Map<String, String> map) {
        try {
            MemberValue memberValue = this.getDefaultValue();
            memberValue.renameClass(map);
            this.setDefaultValue(memberValue);
        } catch (Exception exception) {
            // empty catch block
        }
    }

    public MemberValue getDefaultValue() {
        try {
            return new AnnotationsAttribute.Parser(this.info, this.constPool).parseMemberValue();
        } catch (Exception exception) {
            throw new RuntimeException(exception.toString());
        }
    }

    public void setDefaultValue(MemberValue memberValue) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        AnnotationsWriter annotationsWriter = new AnnotationsWriter(byteArrayOutputStream, this.constPool);
        try {
            memberValue.write(annotationsWriter);
            annotationsWriter.close();
        } catch (IOException iOException) {
            throw new RuntimeException(iOException);
        }
        this.set(byteArrayOutputStream.toByteArray());
    }

    public String toString() {
        return this.getDefaultValue().toString();
    }
}

