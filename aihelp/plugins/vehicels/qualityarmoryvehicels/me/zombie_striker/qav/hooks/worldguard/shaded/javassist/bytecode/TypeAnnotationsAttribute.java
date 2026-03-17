/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode;

import java.io.DataInputStream;
import java.util.HashMap;
import java.util.Map;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.AnnotationsAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.AttributeInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ByteArray;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.TypeAnnotationsWriter;

public class TypeAnnotationsAttribute
extends AttributeInfo {
    public static final String visibleTag = "RuntimeVisibleTypeAnnotations";
    public static final String invisibleTag = "RuntimeInvisibleTypeAnnotations";

    public TypeAnnotationsAttribute(ConstPool constPool, String string, byte[] byArray) {
        super(constPool, string, byArray);
    }

    TypeAnnotationsAttribute(ConstPool constPool, int n, DataInputStream dataInputStream) {
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
            return new TypeAnnotationsAttribute(constPool, this.getName(), copier.close());
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
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

    static class Copier
    extends AnnotationsAttribute.Copier {
        SubCopier sub;

        Copier(byte[] byArray, ConstPool constPool, ConstPool constPool2, Map<String, String> map) {
            super(byArray, constPool, constPool2, map, false);
            TypeAnnotationsWriter typeAnnotationsWriter = new TypeAnnotationsWriter(this.output, constPool2);
            this.writer = typeAnnotationsWriter;
            this.sub = new SubCopier(byArray, constPool, constPool2, map, typeAnnotationsWriter);
        }

        @Override
        int annotationArray(int n, int n2) {
            this.writer.numAnnotations(n2);
            for (int i = 0; i < n2; ++i) {
                int n3 = this.info[n] & 0xFF;
                n = this.sub.targetInfo(n + 1, n3);
                n = this.sub.typePath(n);
                n = this.annotation(n);
            }
            return n;
        }
    }

    static class Renamer
    extends AnnotationsAttribute.Renamer {
        SubWalker sub;

        Renamer(byte[] byArray, ConstPool constPool, Map<String, String> map) {
            super(byArray, constPool, map);
            this.sub = new SubWalker(byArray);
        }

        @Override
        int annotationArray(int n, int n2) {
            for (int i = 0; i < n2; ++i) {
                int n3 = this.info[n] & 0xFF;
                n = this.sub.targetInfo(n + 1, n3);
                n = this.sub.typePath(n);
                n = this.annotation(n);
            }
            return n;
        }
    }

    static class SubCopier
    extends SubWalker {
        ConstPool srcPool;
        ConstPool destPool;
        Map<String, String> classnames;
        TypeAnnotationsWriter writer;

        SubCopier(byte[] byArray, ConstPool constPool, ConstPool constPool2, Map<String, String> map, TypeAnnotationsWriter typeAnnotationsWriter) {
            super(byArray);
            this.srcPool = constPool;
            this.destPool = constPool2;
            this.classnames = map;
            this.writer = typeAnnotationsWriter;
        }

        @Override
        void typeParameterTarget(int n, int n2, int n3) {
            this.writer.typeParameterTarget(n2, n3);
        }

        @Override
        void supertypeTarget(int n, int n2) {
            this.writer.supertypeTarget(n2);
        }

        @Override
        void typeParameterBoundTarget(int n, int n2, int n3, int n4) {
            this.writer.typeParameterBoundTarget(n2, n3, n4);
        }

        @Override
        void emptyTarget(int n, int n2) {
            this.writer.emptyTarget(n2);
        }

        @Override
        void formalParameterTarget(int n, int n2) {
            this.writer.formalParameterTarget(n2);
        }

        @Override
        void throwsTarget(int n, int n2) {
            this.writer.throwsTarget(n2);
        }

        @Override
        int localvarTarget(int n, int n2, int n3) {
            this.writer.localVarTarget(n2, n3);
            return super.localvarTarget(n, n2, n3);
        }

        @Override
        void localvarTarget(int n, int n2, int n3, int n4, int n5) {
            this.writer.localVarTargetTable(n3, n4, n5);
        }

        @Override
        void catchTarget(int n, int n2) {
            this.writer.catchTarget(n2);
        }

        @Override
        void offsetTarget(int n, int n2, int n3) {
            this.writer.offsetTarget(n2, n3);
        }

        @Override
        void typeArgumentTarget(int n, int n2, int n3, int n4) {
            this.writer.typeArgumentTarget(n2, n3, n4);
        }

        @Override
        int typePath(int n, int n2) {
            this.writer.typePath(n2);
            return super.typePath(n, n2);
        }

        @Override
        void typePath(int n, int n2, int n3) {
            this.writer.typePathPath(n2, n3);
        }
    }

    static class SubWalker {
        byte[] info;

        SubWalker(byte[] byArray) {
            this.info = byArray;
        }

        final int targetInfo(int n, int n2) {
            switch (n2) {
                case 0: 
                case 1: {
                    int n3 = this.info[n] & 0xFF;
                    this.typeParameterTarget(n, n2, n3);
                    return n + 1;
                }
                case 16: {
                    int n4 = ByteArray.readU16bit(this.info, n);
                    this.supertypeTarget(n, n4);
                    return n + 2;
                }
                case 17: 
                case 18: {
                    int n5 = this.info[n] & 0xFF;
                    int n6 = this.info[n + 1] & 0xFF;
                    this.typeParameterBoundTarget(n, n2, n5, n6);
                    return n + 2;
                }
                case 19: 
                case 20: 
                case 21: {
                    this.emptyTarget(n, n2);
                    return n;
                }
                case 22: {
                    int n7 = this.info[n] & 0xFF;
                    this.formalParameterTarget(n, n7);
                    return n + 1;
                }
                case 23: {
                    int n8 = ByteArray.readU16bit(this.info, n);
                    this.throwsTarget(n, n8);
                    return n + 2;
                }
                case 64: 
                case 65: {
                    int n9 = ByteArray.readU16bit(this.info, n);
                    return this.localvarTarget(n + 2, n2, n9);
                }
                case 66: {
                    int n10 = ByteArray.readU16bit(this.info, n);
                    this.catchTarget(n, n10);
                    return n + 2;
                }
                case 67: 
                case 68: 
                case 69: 
                case 70: {
                    int n11 = ByteArray.readU16bit(this.info, n);
                    this.offsetTarget(n, n2, n11);
                    return n + 2;
                }
                case 71: 
                case 72: 
                case 73: 
                case 74: 
                case 75: {
                    int n12 = ByteArray.readU16bit(this.info, n);
                    int n13 = this.info[n + 2] & 0xFF;
                    this.typeArgumentTarget(n, n2, n12, n13);
                    return n + 3;
                }
            }
            throw new RuntimeException("invalid target type: " + n2);
        }

        void typeParameterTarget(int n, int n2, int n3) {
        }

        void supertypeTarget(int n, int n2) {
        }

        void typeParameterBoundTarget(int n, int n2, int n3, int n4) {
        }

        void emptyTarget(int n, int n2) {
        }

        void formalParameterTarget(int n, int n2) {
        }

        void throwsTarget(int n, int n2) {
        }

        int localvarTarget(int n, int n2, int n3) {
            for (int i = 0; i < n3; ++i) {
                int n4 = ByteArray.readU16bit(this.info, n);
                int n5 = ByteArray.readU16bit(this.info, n + 2);
                int n6 = ByteArray.readU16bit(this.info, n + 4);
                this.localvarTarget(n, n2, n4, n5, n6);
                n += 6;
            }
            return n;
        }

        void localvarTarget(int n, int n2, int n3, int n4, int n5) {
        }

        void catchTarget(int n, int n2) {
        }

        void offsetTarget(int n, int n2, int n3) {
        }

        void typeArgumentTarget(int n, int n2, int n3, int n4) {
        }

        final int typePath(int n) {
            int n2 = this.info[n++] & 0xFF;
            return this.typePath(n, n2);
        }

        int typePath(int n, int n2) {
            for (int i = 0; i < n2; ++i) {
                int n3 = this.info[n] & 0xFF;
                int n4 = this.info[n + 1] & 0xFF;
                this.typePath(n, n3, n4);
                n += 2;
            }
            return n;
        }

        void typePath(int n, int n2, int n3) {
        }
    }

    static class TAWalker
    extends AnnotationsAttribute.Walker {
        SubWalker subWalker;

        TAWalker(byte[] byArray) {
            super(byArray);
            this.subWalker = new SubWalker(byArray);
        }

        @Override
        int annotationArray(int n, int n2) {
            for (int i = 0; i < n2; ++i) {
                int n3 = this.info[n] & 0xFF;
                n = this.subWalker.targetInfo(n + 1, n3);
                n = this.subWalker.typePath(n);
                n = this.annotation(n);
            }
            return n;
        }
    }
}

