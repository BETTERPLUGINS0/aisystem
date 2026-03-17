/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.convert;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CannotCompileException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CodeConverter;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClass;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.BadBytecode;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeIterator;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Descriptor;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.MethodInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.analysis.Analyzer;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.analysis.Frame;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.convert.Transformer;

public final class TransformAccessArrayField
extends Transformer {
    private final String methodClassname;
    private final CodeConverter.ArrayAccessReplacementMethodNames names;
    private Frame[] frames;
    private int offset;

    public TransformAccessArrayField(Transformer transformer, String string, CodeConverter.ArrayAccessReplacementMethodNames arrayAccessReplacementMethodNames) {
        super(transformer);
        this.methodClassname = string;
        this.names = arrayAccessReplacementMethodNames;
    }

    @Override
    public void initialize(ConstPool constPool, CtClass ctClass, MethodInfo methodInfo) {
        CodeIterator codeIterator = methodInfo.getCodeAttribute().iterator();
        while (codeIterator.hasNext()) {
            try {
                int n = codeIterator.next();
                int n2 = codeIterator.byteAt(n);
                if (n2 == 50) {
                    this.initFrames(ctClass, methodInfo);
                }
                if (n2 == 50 || n2 == 51 || n2 == 52 || n2 == 49 || n2 == 48 || n2 == 46 || n2 == 47 || n2 == 53) {
                    n = this.replace(constPool, codeIterator, n, n2, this.getLoadReplacementSignature(n2));
                    continue;
                }
                if (n2 != 83 && n2 != 84 && n2 != 85 && n2 != 82 && n2 != 81 && n2 != 79 && n2 != 80 && n2 != 86) continue;
                n = this.replace(constPool, codeIterator, n, n2, this.getStoreReplacementSignature(n2));
            } catch (Exception exception) {
                throw new CannotCompileException(exception);
            }
        }
    }

    @Override
    public void clean() {
        this.frames = null;
        this.offset = -1;
    }

    @Override
    public int transform(CtClass ctClass, int n, CodeIterator codeIterator, ConstPool constPool) {
        return n;
    }

    private Frame getFrame(int n) {
        return this.frames[n - this.offset];
    }

    private void initFrames(CtClass ctClass, MethodInfo methodInfo) {
        if (this.frames == null) {
            this.frames = new Analyzer().analyze(ctClass, methodInfo);
            this.offset = 0;
        }
    }

    private int updatePos(int n, int n2) {
        if (this.offset > -1) {
            this.offset += n2;
        }
        return n + n2;
    }

    private String getTopType(int n) {
        Frame frame = this.getFrame(n);
        if (frame == null) {
            return null;
        }
        CtClass ctClass = frame.peek().getCtClass();
        return ctClass != null ? Descriptor.toJvmName(ctClass) : null;
    }

    private int replace(ConstPool constPool, CodeIterator codeIterator, int n, int n2, String string) {
        String string2 = null;
        String string3 = this.getMethodName(n2);
        if (string3 != null) {
            if (n2 == 50) {
                string2 = this.getTopType(codeIterator.lookAhead());
                if (string2 == null) {
                    return n;
                }
                if ("java/lang/Object".equals(string2)) {
                    string2 = null;
                }
            }
            codeIterator.writeByte(0, n);
            CodeIterator.Gap gap = codeIterator.insertGapAt(n, string2 != null ? 5 : 2, false);
            n = gap.position;
            int n3 = constPool.addClassInfo(this.methodClassname);
            int n4 = constPool.addMethodrefInfo(n3, string3, string);
            codeIterator.writeByte(184, n);
            codeIterator.write16bit(n4, n + 1);
            if (string2 != null) {
                int n5 = constPool.addClassInfo(string2);
                codeIterator.writeByte(192, n + 3);
                codeIterator.write16bit(n5, n + 4);
            }
            n = this.updatePos(n, gap.length);
        }
        return n;
    }

    private String getMethodName(int n) {
        String string = null;
        switch (n) {
            case 50: {
                string = this.names.objectRead();
                break;
            }
            case 51: {
                string = this.names.byteOrBooleanRead();
                break;
            }
            case 52: {
                string = this.names.charRead();
                break;
            }
            case 49: {
                string = this.names.doubleRead();
                break;
            }
            case 48: {
                string = this.names.floatRead();
                break;
            }
            case 46: {
                string = this.names.intRead();
                break;
            }
            case 53: {
                string = this.names.shortRead();
                break;
            }
            case 47: {
                string = this.names.longRead();
                break;
            }
            case 83: {
                string = this.names.objectWrite();
                break;
            }
            case 84: {
                string = this.names.byteOrBooleanWrite();
                break;
            }
            case 85: {
                string = this.names.charWrite();
                break;
            }
            case 82: {
                string = this.names.doubleWrite();
                break;
            }
            case 81: {
                string = this.names.floatWrite();
                break;
            }
            case 79: {
                string = this.names.intWrite();
                break;
            }
            case 86: {
                string = this.names.shortWrite();
                break;
            }
            case 80: {
                string = this.names.longWrite();
            }
        }
        if ("".equals(string)) {
            string = null;
        }
        return string;
    }

    private String getLoadReplacementSignature(int n) {
        switch (n) {
            case 50: {
                return "(Ljava/lang/Object;I)Ljava/lang/Object;";
            }
            case 51: {
                return "(Ljava/lang/Object;I)B";
            }
            case 52: {
                return "(Ljava/lang/Object;I)C";
            }
            case 49: {
                return "(Ljava/lang/Object;I)D";
            }
            case 48: {
                return "(Ljava/lang/Object;I)F";
            }
            case 46: {
                return "(Ljava/lang/Object;I)I";
            }
            case 53: {
                return "(Ljava/lang/Object;I)S";
            }
            case 47: {
                return "(Ljava/lang/Object;I)J";
            }
        }
        throw new BadBytecode(n);
    }

    private String getStoreReplacementSignature(int n) {
        switch (n) {
            case 83: {
                return "(Ljava/lang/Object;ILjava/lang/Object;)V";
            }
            case 84: {
                return "(Ljava/lang/Object;IB)V";
            }
            case 85: {
                return "(Ljava/lang/Object;IC)V";
            }
            case 82: {
                return "(Ljava/lang/Object;ID)V";
            }
            case 81: {
                return "(Ljava/lang/Object;IF)V";
            }
            case 79: {
                return "(Ljava/lang/Object;II)V";
            }
            case 86: {
                return "(Ljava/lang/Object;IS)V";
            }
            case 80: {
                return "(Ljava/lang/Object;IJ)V";
            }
        }
        throw new BadBytecode(n);
    }
}

