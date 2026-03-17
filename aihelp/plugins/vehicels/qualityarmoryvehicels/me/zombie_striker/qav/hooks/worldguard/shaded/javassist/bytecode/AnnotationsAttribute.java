/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.AttributeInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ByteArray;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Descriptor;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.Annotation;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.AnnotationMemberValue;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.AnnotationsWriter;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.ArrayMemberValue;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.BooleanMemberValue;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.ByteMemberValue;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.CharMemberValue;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.ClassMemberValue;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.DoubleMemberValue;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.EnumMemberValue;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.FloatMemberValue;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.IntegerMemberValue;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.LongMemberValue;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.MemberValue;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.ShortMemberValue;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.StringMemberValue;

public class AnnotationsAttribute
extends AttributeInfo {
    public static final String visibleTag = "RuntimeVisibleAnnotations";
    public static final String invisibleTag = "RuntimeInvisibleAnnotations";

    public AnnotationsAttribute(ConstPool constPool, String string, byte[] byArray) {
        super(constPool, string, byArray);
    }

    public AnnotationsAttribute(ConstPool constPool, String string) {
        this(constPool, string, new byte[]{0, 0});
    }

    AnnotationsAttribute(ConstPool constPool, int n, DataInputStream dataInputStream) {
        super(constPool, n, dataInputStream);
    }

    public int numAnnotations() {
        return ByteArray.readU16bit(this.info, 0);
    }

    @Override
    public AttributeInfo copy(ConstPool constPool, Map<String, String> map) {
        Copier copier = new Copier(this.info, this.constPool, constPool, map);
        try {
            copier.annotationArray();
            return new AnnotationsAttribute(constPool, this.getName(), copier.close());
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public Annotation getAnnotation(String string) {
        Annotation[] annotationArray = this.getAnnotations();
        for (int i = 0; i < annotationArray.length; ++i) {
            if (!annotationArray[i].getTypeName().equals(string)) continue;
            return annotationArray[i];
        }
        return null;
    }

    public void addAnnotation(Annotation annotation) {
        String string = annotation.getTypeName();
        Annotation[] annotationArray = this.getAnnotations();
        for (int i = 0; i < annotationArray.length; ++i) {
            if (!annotationArray[i].getTypeName().equals(string)) continue;
            annotationArray[i] = annotation;
            this.setAnnotations(annotationArray);
            return;
        }
        Annotation[] annotationArray2 = new Annotation[annotationArray.length + 1];
        System.arraycopy(annotationArray, 0, annotationArray2, 0, annotationArray.length);
        annotationArray2[annotationArray.length] = annotation;
        this.setAnnotations(annotationArray2);
    }

    public boolean removeAnnotation(String string) {
        Annotation[] annotationArray = this.getAnnotations();
        for (int i = 0; i < annotationArray.length; ++i) {
            if (!annotationArray[i].getTypeName().equals(string)) continue;
            Annotation[] annotationArray2 = new Annotation[annotationArray.length - 1];
            System.arraycopy(annotationArray, 0, annotationArray2, 0, i);
            if (i < annotationArray.length - 1) {
                System.arraycopy(annotationArray, i + 1, annotationArray2, i, annotationArray.length - i - 1);
            }
            this.setAnnotations(annotationArray2);
            return true;
        }
        return false;
    }

    public Annotation[] getAnnotations() {
        try {
            return new Parser(this.info, this.constPool).parseAnnotations();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public void setAnnotations(Annotation[] annotationArray) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        AnnotationsWriter annotationsWriter = new AnnotationsWriter(byteArrayOutputStream, this.constPool);
        try {
            int n = annotationArray.length;
            annotationsWriter.numAnnotations(n);
            for (int i = 0; i < n; ++i) {
                annotationArray[i].write(annotationsWriter);
            }
            annotationsWriter.close();
        } catch (IOException iOException) {
            throw new RuntimeException(iOException);
        }
        this.set(byteArrayOutputStream.toByteArray());
    }

    public void setAnnotation(Annotation annotation) {
        this.setAnnotations(new Annotation[]{annotation});
    }

    @Override
    void renameClass(String string, String string2) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put(string, string2);
        this.renameClass(hashMap);
    }

    @Override
    void renameClass(Map<String, String> map) {
        Renamer renamer = new Renamer(this.info, this.getConstPool(), map);
        try {
            renamer.annotationArray();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    void getRefClasses(Map<String, String> map) {
        this.renameClass(map);
    }

    public String toString() {
        Annotation[] annotationArray = this.getAnnotations();
        StringBuilder stringBuilder = new StringBuilder();
        int n = 0;
        while (n < annotationArray.length) {
            stringBuilder.append(annotationArray[n++].toString());
            if (n == annotationArray.length) continue;
            stringBuilder.append(", ");
        }
        return stringBuilder.toString();
    }

    static class Copier
    extends Walker {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        AnnotationsWriter writer;
        ConstPool srcPool;
        ConstPool destPool;
        Map<String, String> classnames;

        Copier(byte[] byArray, ConstPool constPool, ConstPool constPool2, Map<String, String> map) {
            this(byArray, constPool, constPool2, map, true);
        }

        Copier(byte[] byArray, ConstPool constPool, ConstPool constPool2, Map<String, String> map, boolean bl) {
            super(byArray);
            if (bl) {
                this.writer = new AnnotationsWriter(this.output, constPool2);
            }
            this.srcPool = constPool;
            this.destPool = constPool2;
            this.classnames = map;
        }

        byte[] close() {
            this.writer.close();
            return this.output.toByteArray();
        }

        @Override
        void parameters(int n, int n2) {
            this.writer.numParameters(n);
            super.parameters(n, n2);
        }

        @Override
        int annotationArray(int n, int n2) {
            this.writer.numAnnotations(n2);
            return super.annotationArray(n, n2);
        }

        @Override
        int annotation(int n, int n2, int n3) {
            this.writer.annotation(this.copyType(n2), n3);
            return super.annotation(n, n2, n3);
        }

        @Override
        int memberValuePair(int n, int n2) {
            this.writer.memberValuePair(this.copy(n2));
            return super.memberValuePair(n, n2);
        }

        @Override
        void constValueMember(int n, int n2) {
            this.writer.constValueIndex(n, this.copy(n2));
            super.constValueMember(n, n2);
        }

        @Override
        void enumMemberValue(int n, int n2, int n3) {
            this.writer.enumConstValue(this.copyType(n2), this.copy(n3));
            super.enumMemberValue(n, n2, n3);
        }

        @Override
        void classMemberValue(int n, int n2) {
            this.writer.classInfoIndex(this.copyType(n2));
            super.classMemberValue(n, n2);
        }

        @Override
        int annotationMemberValue(int n) {
            this.writer.annotationValue();
            return super.annotationMemberValue(n);
        }

        @Override
        int arrayMemberValue(int n, int n2) {
            this.writer.arrayValue(n2);
            return super.arrayMemberValue(n, n2);
        }

        int copy(int n) {
            return this.srcPool.copy(n, this.destPool, this.classnames);
        }

        int copyType(int n) {
            String string = this.srcPool.getUtf8Info(n);
            String string2 = Descriptor.rename(string, this.classnames);
            return this.destPool.addUtf8Info(string2);
        }
    }

    static class Parser
    extends Walker {
        ConstPool pool;
        Annotation[][] allParams;
        Annotation[] allAnno;
        Annotation currentAnno;
        MemberValue currentMember;

        Parser(byte[] byArray, ConstPool constPool) {
            super(byArray);
            this.pool = constPool;
        }

        Annotation[][] parseParameters() {
            this.parameters();
            return this.allParams;
        }

        Annotation[] parseAnnotations() {
            this.annotationArray();
            return this.allAnno;
        }

        MemberValue parseMemberValue() {
            this.memberValue(0);
            return this.currentMember;
        }

        @Override
        void parameters(int n, int n2) {
            Annotation[][] annotationArrayArray = new Annotation[n][];
            for (int i = 0; i < n; ++i) {
                n2 = this.annotationArray(n2);
                annotationArrayArray[i] = this.allAnno;
            }
            this.allParams = annotationArrayArray;
        }

        @Override
        int annotationArray(int n, int n2) {
            Annotation[] annotationArray = new Annotation[n2];
            for (int i = 0; i < n2; ++i) {
                n = this.annotation(n);
                annotationArray[i] = this.currentAnno;
            }
            this.allAnno = annotationArray;
            return n;
        }

        @Override
        int annotation(int n, int n2, int n3) {
            this.currentAnno = new Annotation(n2, this.pool);
            return super.annotation(n, n2, n3);
        }

        @Override
        int memberValuePair(int n, int n2) {
            n = super.memberValuePair(n, n2);
            this.currentAnno.addMemberValue(n2, this.currentMember);
            return n;
        }

        @Override
        void constValueMember(int n, int n2) {
            MemberValue memberValue;
            ConstPool constPool = this.pool;
            switch (n) {
                case 66: {
                    memberValue = new ByteMemberValue(n2, constPool);
                    break;
                }
                case 67: {
                    memberValue = new CharMemberValue(n2, constPool);
                    break;
                }
                case 68: {
                    memberValue = new DoubleMemberValue(n2, constPool);
                    break;
                }
                case 70: {
                    memberValue = new FloatMemberValue(n2, constPool);
                    break;
                }
                case 73: {
                    memberValue = new IntegerMemberValue(n2, constPool);
                    break;
                }
                case 74: {
                    memberValue = new LongMemberValue(n2, constPool);
                    break;
                }
                case 83: {
                    memberValue = new ShortMemberValue(n2, constPool);
                    break;
                }
                case 90: {
                    memberValue = new BooleanMemberValue(n2, constPool);
                    break;
                }
                case 115: {
                    memberValue = new StringMemberValue(n2, constPool);
                    break;
                }
                default: {
                    throw new RuntimeException("unknown tag:" + n);
                }
            }
            this.currentMember = memberValue;
            super.constValueMember(n, n2);
        }

        @Override
        void enumMemberValue(int n, int n2, int n3) {
            this.currentMember = new EnumMemberValue(n2, n3, this.pool);
            super.enumMemberValue(n, n2, n3);
        }

        @Override
        void classMemberValue(int n, int n2) {
            this.currentMember = new ClassMemberValue(n2, this.pool);
            super.classMemberValue(n, n2);
        }

        @Override
        int annotationMemberValue(int n) {
            Annotation annotation = this.currentAnno;
            n = super.annotationMemberValue(n);
            this.currentMember = new AnnotationMemberValue(this.currentAnno, this.pool);
            this.currentAnno = annotation;
            return n;
        }

        @Override
        int arrayMemberValue(int n, int n2) {
            ArrayMemberValue arrayMemberValue = new ArrayMemberValue(this.pool);
            MemberValue[] memberValueArray = new MemberValue[n2];
            for (int i = 0; i < n2; ++i) {
                n = this.memberValue(n);
                memberValueArray[i] = this.currentMember;
            }
            arrayMemberValue.setValue(memberValueArray);
            this.currentMember = arrayMemberValue;
            return n;
        }
    }

    static class Renamer
    extends Walker {
        ConstPool cpool;
        Map<String, String> classnames;

        Renamer(byte[] byArray, ConstPool constPool, Map<String, String> map) {
            super(byArray);
            this.cpool = constPool;
            this.classnames = map;
        }

        @Override
        int annotation(int n, int n2, int n3) {
            this.renameType(n - 4, n2);
            return super.annotation(n, n2, n3);
        }

        @Override
        void enumMemberValue(int n, int n2, int n3) {
            this.renameType(n + 1, n2);
            super.enumMemberValue(n, n2, n3);
        }

        @Override
        void classMemberValue(int n, int n2) {
            this.renameType(n + 1, n2);
            super.classMemberValue(n, n2);
        }

        private void renameType(int n, int n2) {
            String string;
            String string2 = this.cpool.getUtf8Info(n2);
            if (!string2.equals(string = Descriptor.rename(string2, this.classnames))) {
                int n3 = this.cpool.addUtf8Info(string);
                ByteArray.write16bit(n3, this.info, n);
            }
        }
    }

    static class Walker {
        byte[] info;

        Walker(byte[] byArray) {
            this.info = byArray;
        }

        final void parameters() {
            int n = this.info[0] & 0xFF;
            this.parameters(n, 1);
        }

        void parameters(int n, int n2) {
            for (int i = 0; i < n; ++i) {
                n2 = this.annotationArray(n2);
            }
        }

        final void annotationArray() {
            this.annotationArray(0);
        }

        final int annotationArray(int n) {
            int n2 = ByteArray.readU16bit(this.info, n);
            return this.annotationArray(n + 2, n2);
        }

        int annotationArray(int n, int n2) {
            for (int i = 0; i < n2; ++i) {
                n = this.annotation(n);
            }
            return n;
        }

        final int annotation(int n) {
            int n2 = ByteArray.readU16bit(this.info, n);
            int n3 = ByteArray.readU16bit(this.info, n + 2);
            return this.annotation(n + 4, n2, n3);
        }

        int annotation(int n, int n2, int n3) {
            for (int i = 0; i < n3; ++i) {
                n = this.memberValuePair(n);
            }
            return n;
        }

        final int memberValuePair(int n) {
            int n2 = ByteArray.readU16bit(this.info, n);
            return this.memberValuePair(n + 2, n2);
        }

        int memberValuePair(int n, int n2) {
            return this.memberValue(n);
        }

        final int memberValue(int n) {
            int n2 = this.info[n] & 0xFF;
            if (n2 == 101) {
                int n3 = ByteArray.readU16bit(this.info, n + 1);
                int n4 = ByteArray.readU16bit(this.info, n + 3);
                this.enumMemberValue(n, n3, n4);
                return n + 5;
            }
            if (n2 == 99) {
                int n5 = ByteArray.readU16bit(this.info, n + 1);
                this.classMemberValue(n, n5);
                return n + 3;
            }
            if (n2 == 64) {
                return this.annotationMemberValue(n + 1);
            }
            if (n2 == 91) {
                int n6 = ByteArray.readU16bit(this.info, n + 1);
                return this.arrayMemberValue(n + 3, n6);
            }
            int n7 = ByteArray.readU16bit(this.info, n + 1);
            this.constValueMember(n2, n7);
            return n + 3;
        }

        void constValueMember(int n, int n2) {
        }

        void enumMemberValue(int n, int n2, int n3) {
        }

        void classMemberValue(int n, int n2) {
        }

        int annotationMemberValue(int n) {
            return this.annotation(n);
        }

        int arrayMemberValue(int n, int n2) {
            for (int i = 0; i < n2; ++i) {
                n = this.memberValue(n);
            }
            return n;
        }
    }
}

