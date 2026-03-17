/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.AnnotationsAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.AttributeInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.Annotation;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.AnnotationsWriter;

public class ParameterAnnotationsAttribute
extends AttributeInfo {
    public static final String visibleTag = "RuntimeVisibleParameterAnnotations";
    public static final String invisibleTag = "RuntimeInvisibleParameterAnnotations";

    public ParameterAnnotationsAttribute(ConstPool constPool, String string, byte[] byArray) {
        super(constPool, string, byArray);
    }

    public ParameterAnnotationsAttribute(ConstPool constPool, String string) {
        this(constPool, string, new byte[]{0});
    }

    ParameterAnnotationsAttribute(ConstPool constPool, int n, DataInputStream dataInputStream) {
        super(constPool, n, dataInputStream);
    }

    public int numParameters() {
        return this.info[0] & 0xFF;
    }

    @Override
    public AttributeInfo copy(ConstPool constPool, Map<String, String> map) {
        AnnotationsAttribute.Copier copier = new AnnotationsAttribute.Copier(this.info, this.constPool, constPool, map);
        try {
            copier.parameters();
            return new ParameterAnnotationsAttribute(constPool, this.getName(), copier.close());
        } catch (Exception exception) {
            throw new RuntimeException(exception.toString());
        }
    }

    public Annotation[][] getAnnotations() {
        try {
            return new AnnotationsAttribute.Parser(this.info, this.constPool).parseParameters();
        } catch (Exception exception) {
            throw new RuntimeException(exception.toString());
        }
    }

    public void setAnnotations(Annotation[][] annotationArray) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        AnnotationsWriter annotationsWriter = new AnnotationsWriter(byteArrayOutputStream, this.constPool);
        try {
            annotationsWriter.numParameters(annotationArray.length);
            for (Annotation[] annotationArray2 : annotationArray) {
                annotationsWriter.numAnnotations(annotationArray2.length);
                for (int i = 0; i < annotationArray2.length; ++i) {
                    annotationArray2[i].write(annotationsWriter);
                }
            }
            annotationsWriter.close();
        } catch (IOException iOException) {
            throw new RuntimeException(iOException);
        }
        this.set(byteArrayOutputStream.toByteArray());
    }

    @Override
    void renameClass(String string, String string2) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put(string, string2);
        this.renameClass(hashMap);
    }

    @Override
    void renameClass(Map<String, String> map) {
        AnnotationsAttribute.Renamer renamer = new AnnotationsAttribute.Renamer(this.info, this.getConstPool(), map);
        try {
            renamer.parameters();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    void getRefClasses(Map<String, String> map) {
        this.renameClass(map);
    }

    public String toString() {
        Annotation[][] annotationArray = this.getAnnotations();
        StringBuilder stringBuilder = new StringBuilder();
        Annotation[][] annotationArray2 = annotationArray;
        int n = annotationArray2.length;
        for (int i = 0; i < n; ++i) {
            Annotation[] annotationArray3;
            for (Annotation annotation : annotationArray3 = annotationArray2[i]) {
                stringBuilder.append(annotation.toString()).append(' ');
            }
            stringBuilder.append(", ");
        }
        return stringBuilder.toString().replaceAll(" (?=,)|, $", "");
    }
}

