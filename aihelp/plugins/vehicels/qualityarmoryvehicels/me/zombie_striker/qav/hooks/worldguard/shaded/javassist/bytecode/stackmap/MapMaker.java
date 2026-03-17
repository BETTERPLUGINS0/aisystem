/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.stackmap;

import java.util.ArrayList;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.NotFoundException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.BadBytecode;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ByteArray;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.MethodInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.StackMap;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.StackMapTable;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.stackmap.BasicBlock;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.stackmap.Tracer;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.stackmap.TypeData;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.stackmap.TypedBlock;

public class MapMaker
extends Tracer {
    public static StackMapTable make(ClassPool classPool, MethodInfo methodInfo) {
        TypedBlock[] typedBlockArray;
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        if (codeAttribute == null) {
            return null;
        }
        try {
            typedBlockArray = TypedBlock.makeBlocks(methodInfo, codeAttribute, true);
        } catch (BasicBlock.JsrBytecode jsrBytecode) {
            return null;
        }
        if (typedBlockArray == null) {
            return null;
        }
        MapMaker mapMaker = new MapMaker(classPool, methodInfo, codeAttribute);
        try {
            mapMaker.make(typedBlockArray, codeAttribute.getCode());
        } catch (BadBytecode badBytecode) {
            throw new BadBytecode(methodInfo, (Throwable)badBytecode);
        }
        return mapMaker.toStackMap(typedBlockArray);
    }

    public static StackMap make2(ClassPool classPool, MethodInfo methodInfo) {
        TypedBlock[] typedBlockArray;
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        if (codeAttribute == null) {
            return null;
        }
        try {
            typedBlockArray = TypedBlock.makeBlocks(methodInfo, codeAttribute, true);
        } catch (BasicBlock.JsrBytecode jsrBytecode) {
            return null;
        }
        if (typedBlockArray == null) {
            return null;
        }
        MapMaker mapMaker = new MapMaker(classPool, methodInfo, codeAttribute);
        try {
            mapMaker.make(typedBlockArray, codeAttribute.getCode());
        } catch (BadBytecode badBytecode) {
            throw new BadBytecode(methodInfo, (Throwable)badBytecode);
        }
        return mapMaker.toStackMap2(methodInfo.getConstPool(), typedBlockArray);
    }

    public MapMaker(ClassPool classPool, MethodInfo methodInfo, CodeAttribute codeAttribute) {
        super(classPool, methodInfo.getConstPool(), codeAttribute.getMaxStack(), codeAttribute.getMaxLocals(), TypedBlock.getRetType(methodInfo.getDescriptor()));
    }

    protected MapMaker(MapMaker mapMaker) {
        super(mapMaker);
    }

    void make(TypedBlock[] typedBlockArray, byte[] byArray) {
        this.make(byArray, typedBlockArray[0]);
        this.findDeadCatchers(byArray, typedBlockArray);
        try {
            this.fixTypes(byArray, typedBlockArray);
        } catch (NotFoundException notFoundException) {
            throw new BadBytecode("failed to resolve types", (Throwable)notFoundException);
        }
    }

    private void make(byte[] byArray, TypedBlock typedBlock) {
        int n;
        MapMaker.copyTypeData(typedBlock.stackTop, typedBlock.stackTypes, this.stackTypes);
        this.stackTop = typedBlock.stackTop;
        MapMaker.copyTypeData(typedBlock.localsTypes.length, typedBlock.localsTypes, this.localsTypes);
        this.traceException(byArray, typedBlock.toCatch);
        int n2 = n + typedBlock.length;
        for (n = typedBlock.position; n < n2; n += this.doOpcode(n, byArray)) {
            this.traceException(byArray, typedBlock.toCatch);
        }
        if (typedBlock.exit != null) {
            for (int i = 0; i < typedBlock.exit.length; ++i) {
                TypedBlock typedBlock2 = (TypedBlock)typedBlock.exit[i];
                if (typedBlock2.alreadySet()) {
                    this.mergeMap(typedBlock2, true);
                    continue;
                }
                this.recordStackMap(typedBlock2);
                MapMaker mapMaker = new MapMaker(this);
                mapMaker.make(byArray, typedBlock2);
            }
        }
    }

    private void traceException(byte[] byArray, BasicBlock.Catch catch_) {
        while (catch_ != null) {
            TypedBlock typedBlock = (TypedBlock)catch_.body;
            if (typedBlock.alreadySet()) {
                this.mergeMap(typedBlock, false);
                if (typedBlock.stackTop < 1) {
                    throw new BadBytecode("bad catch clause: " + catch_.typeIndex);
                }
                typedBlock.stackTypes[0] = this.merge(this.toExceptionType(catch_.typeIndex), typedBlock.stackTypes[0]);
            } else {
                this.recordStackMap(typedBlock, catch_.typeIndex);
                MapMaker mapMaker = new MapMaker(this);
                mapMaker.make(byArray, typedBlock);
            }
            catch_ = catch_.next;
        }
    }

    private void mergeMap(TypedBlock typedBlock, boolean bl) {
        int n;
        int n2 = this.localsTypes.length;
        for (n = 0; n < n2; ++n) {
            typedBlock.localsTypes[n] = this.merge(MapMaker.validateTypeData(this.localsTypes, n2, n), typedBlock.localsTypes[n]);
        }
        if (bl) {
            n2 = this.stackTop;
            for (n = 0; n < n2; ++n) {
                typedBlock.stackTypes[n] = this.merge(this.stackTypes[n], typedBlock.stackTypes[n]);
            }
        }
    }

    private TypeData merge(TypeData typeData, TypeData typeData2) {
        if (typeData == typeData2) {
            return typeData2;
        }
        if (typeData2 instanceof TypeData.ClassName || typeData2 instanceof TypeData.BasicType) {
            return typeData2;
        }
        if (typeData2 instanceof TypeData.AbsTypeVar) {
            ((TypeData.AbsTypeVar)typeData2).merge(typeData);
            return typeData2;
        }
        throw new RuntimeException("fatal: this should never happen");
    }

    private void recordStackMap(TypedBlock typedBlock) {
        TypeData[] typeDataArray = TypeData.make(this.stackTypes.length);
        int n = this.stackTop;
        MapMaker.recordTypeData(n, this.stackTypes, typeDataArray);
        this.recordStackMap0(typedBlock, n, typeDataArray);
    }

    private void recordStackMap(TypedBlock typedBlock, int n) {
        TypeData[] typeDataArray = TypeData.make(this.stackTypes.length);
        typeDataArray[0] = this.toExceptionType(n).join();
        this.recordStackMap0(typedBlock, 1, typeDataArray);
    }

    private TypeData.ClassName toExceptionType(int n) {
        String string = n == 0 ? "java.lang.Throwable" : this.cpool.getClassInfo(n);
        return new TypeData.ClassName(string);
    }

    private void recordStackMap0(TypedBlock typedBlock, int n, TypeData[] typeDataArray) {
        int n2 = this.localsTypes.length;
        TypeData[] typeDataArray2 = TypeData.make(n2);
        int n3 = MapMaker.recordTypeData(n2, this.localsTypes, typeDataArray2);
        typedBlock.setStackMap(n, typeDataArray, n3, typeDataArray2);
    }

    protected static int recordTypeData(int n, TypeData[] typeDataArray, TypeData[] typeDataArray2) {
        int n2 = -1;
        for (int i = 0; i < n; ++i) {
            TypeData typeData = MapMaker.validateTypeData(typeDataArray, n, i);
            typeDataArray2[i] = typeData.join();
            if (typeData == TOP) continue;
            n2 = i + 1;
        }
        return n2 + 1;
    }

    protected static void copyTypeData(int n, TypeData[] typeDataArray, TypeData[] typeDataArray2) {
        System.arraycopy(typeDataArray, 0, typeDataArray2, 0, n);
    }

    private static TypeData validateTypeData(TypeData[] typeDataArray, int n, int n2) {
        TypeData typeData = typeDataArray[n2];
        if (typeData.is2WordType() && n2 + 1 < n && typeDataArray[n2 + 1] != TOP) {
            return TOP;
        }
        return typeData;
    }

    private void findDeadCatchers(byte[] byArray, TypedBlock[] typedBlockArray) {
        for (TypedBlock typedBlock : typedBlockArray) {
            TypedBlock typedBlock2;
            if (typedBlock.alreadySet()) continue;
            this.fixDeadcode(byArray, typedBlock);
            BasicBlock.Catch catch_ = typedBlock.toCatch;
            if (catch_ == null || (typedBlock2 = (TypedBlock)catch_.body).alreadySet()) continue;
            this.recordStackMap(typedBlock2, catch_.typeIndex);
            this.fixDeadcode(byArray, typedBlock2);
            typedBlock2.incoming = 1;
        }
    }

    private void fixDeadcode(byte[] byArray, TypedBlock typedBlock) {
        int n = typedBlock.position;
        int n2 = typedBlock.length - 3;
        if (n2 < 0) {
            if (n2 == -1) {
                byArray[n] = 0;
            }
            byArray[n + typedBlock.length - 1] = -65;
            typedBlock.incoming = 1;
            this.recordStackMap(typedBlock, 0);
            return;
        }
        typedBlock.incoming = 0;
        for (int i = 0; i < n2; ++i) {
            byArray[n + i] = 0;
        }
        byArray[n + n2] = -89;
        ByteArray.write16bit(-n2, byArray, n + n2 + 1);
    }

    private void fixTypes(byte[] byArray, TypedBlock[] typedBlockArray) {
        ArrayList<TypeData> arrayList = new ArrayList<TypeData>();
        int n = typedBlockArray.length;
        int n2 = 0;
        for (int i = 0; i < n; ++i) {
            int n3;
            TypedBlock typedBlock = typedBlockArray[i];
            if (!typedBlock.alreadySet()) continue;
            int n4 = typedBlock.localsTypes.length;
            for (n3 = 0; n3 < n4; ++n3) {
                n2 = typedBlock.localsTypes[n3].dfs(arrayList, n2, this.classPool);
            }
            n4 = typedBlock.stackTop;
            for (n3 = 0; n3 < n4; ++n3) {
                n2 = typedBlock.stackTypes[n3].dfs(arrayList, n2, this.classPool);
            }
        }
    }

    public StackMapTable toStackMap(TypedBlock[] typedBlockArray) {
        StackMapTable.Writer writer = new StackMapTable.Writer(32);
        int n = typedBlockArray.length;
        TypedBlock typedBlock = typedBlockArray[0];
        int n2 = typedBlock.length;
        if (typedBlock.incoming > 0) {
            writer.sameFrame(0);
            --n2;
        }
        for (int i = 1; i < n; ++i) {
            TypedBlock typedBlock2 = typedBlockArray[i];
            if (this.isTarget(typedBlock2, typedBlockArray[i - 1])) {
                typedBlock2.resetNumLocals();
                int n3 = MapMaker.stackMapDiff(typedBlock.numLocals, typedBlock.localsTypes, typedBlock2.numLocals, typedBlock2.localsTypes);
                this.toStackMapBody(writer, typedBlock2, n3, n2, typedBlock);
                n2 = typedBlock2.length - 1;
                typedBlock = typedBlock2;
                continue;
            }
            if (typedBlock2.incoming == 0) {
                writer.sameFrame(n2);
                n2 = typedBlock2.length - 1;
                continue;
            }
            n2 += typedBlock2.length;
        }
        return writer.toStackMapTable(this.cpool);
    }

    private boolean isTarget(TypedBlock typedBlock, TypedBlock typedBlock2) {
        int n = typedBlock.incoming;
        if (n > 1) {
            return true;
        }
        if (n < 1) {
            return false;
        }
        return typedBlock2.stop;
    }

    private void toStackMapBody(StackMapTable.Writer writer, TypedBlock typedBlock, int n, int n2, TypedBlock typedBlock2) {
        Object object;
        int n3 = typedBlock.stackTop;
        if (n3 == 0) {
            if (n == 0) {
                writer.sameFrame(n2);
                return;
            }
            if (0 > n && n >= -3) {
                writer.chopFrame(n2, -n);
                return;
            }
            if (0 < n && n <= 3) {
                int[] nArray = new int[n];
                int[] nArray2 = this.fillStackMap(typedBlock.numLocals - typedBlock2.numLocals, typedBlock2.numLocals, nArray, typedBlock.localsTypes);
                writer.appendFrame(n2, nArray2, nArray);
                return;
            }
        } else {
            if (n3 == 1 && n == 0) {
                TypeData typeData = typedBlock.stackTypes[0];
                writer.sameLocals(n2, typeData.getTypeTag(), typeData.getTypeData(this.cpool));
                return;
            }
            if (n3 == 2 && n == 0 && ((TypeData)(object = (Object)typedBlock.stackTypes[0])).is2WordType()) {
                writer.sameLocals(n2, ((TypeData)object).getTypeTag(), ((TypeData)object).getTypeData(this.cpool));
                return;
            }
        }
        object = new int[n3];
        int[] nArray = this.fillStackMap(n3, 0, (int[])object, typedBlock.stackTypes);
        int[] nArray3 = new int[typedBlock.numLocals];
        int[] nArray4 = this.fillStackMap(typedBlock.numLocals, 0, nArray3, typedBlock.localsTypes);
        writer.fullFrame(n2, nArray4, nArray3, nArray, (int[])object);
    }

    private int[] fillStackMap(int n, int n2, int[] nArray, TypeData[] typeDataArray) {
        int n3 = MapMaker.diffSize(typeDataArray, n2, n2 + n);
        ConstPool constPool = this.cpool;
        int[] nArray2 = new int[n3];
        int n4 = 0;
        for (int i = 0; i < n; ++i) {
            TypeData typeData = typeDataArray[n2 + i];
            nArray2[n4] = typeData.getTypeTag();
            nArray[n4] = typeData.getTypeData(constPool);
            if (typeData.is2WordType()) {
                ++i;
            }
            ++n4;
        }
        return nArray2;
    }

    private static int stackMapDiff(int n, TypeData[] typeDataArray, int n2, TypeData[] typeDataArray2) {
        int n3 = n2 - n;
        int n4 = n3 > 0 ? n : n2;
        if (MapMaker.stackMapEq(typeDataArray, typeDataArray2, n4)) {
            if (n3 > 0) {
                return MapMaker.diffSize(typeDataArray2, n4, n2);
            }
            return -MapMaker.diffSize(typeDataArray, n4, n);
        }
        return -100;
    }

    private static boolean stackMapEq(TypeData[] typeDataArray, TypeData[] typeDataArray2, int n) {
        for (int i = 0; i < n; ++i) {
            if (typeDataArray[i].eq(typeDataArray2[i])) continue;
            return false;
        }
        return true;
    }

    private static int diffSize(TypeData[] typeDataArray, int n, int n2) {
        int n3 = 0;
        while (n < n2) {
            TypeData typeData = typeDataArray[n++];
            ++n3;
            if (!typeData.is2WordType()) continue;
            ++n;
        }
        return n3;
    }

    public StackMap toStackMap2(ConstPool constPool, TypedBlock[] typedBlockArray) {
        int n;
        StackMap.Writer writer = new StackMap.Writer();
        int n2 = typedBlockArray.length;
        boolean[] blArray = new boolean[n2];
        TypedBlock typedBlock = typedBlockArray[0];
        blArray[0] = typedBlock.incoming > 0;
        int n3 = blArray[0] ? 1 : 0;
        for (n = 1; n < n2; ++n) {
            TypedBlock typedBlock2 = typedBlockArray[n];
            blArray[n] = this.isTarget(typedBlock2, typedBlockArray[n - 1]);
            if (!blArray[n]) continue;
            typedBlock2.resetNumLocals();
            typedBlock = typedBlock2;
            ++n3;
        }
        if (n3 == 0) {
            return null;
        }
        writer.write16bit(n3);
        for (n = 0; n < n2; ++n) {
            if (!blArray[n]) continue;
            this.writeStackFrame(writer, constPool, typedBlockArray[n].position, typedBlockArray[n]);
        }
        return writer.toStackMap(constPool);
    }

    private void writeStackFrame(StackMap.Writer writer, ConstPool constPool, int n, TypedBlock typedBlock) {
        writer.write16bit(n);
        this.writeVerifyTypeInfo(writer, constPool, typedBlock.localsTypes, typedBlock.numLocals);
        this.writeVerifyTypeInfo(writer, constPool, typedBlock.stackTypes, typedBlock.stackTop);
    }

    private void writeVerifyTypeInfo(StackMap.Writer writer, ConstPool constPool, TypeData[] typeDataArray, int n) {
        TypeData typeData;
        int n2;
        int n3 = 0;
        for (n2 = 0; n2 < n; ++n2) {
            typeData = typeDataArray[n2];
            if (typeData == null || !typeData.is2WordType()) continue;
            ++n3;
            ++n2;
        }
        writer.write16bit(n - n3);
        for (n2 = 0; n2 < n; ++n2) {
            typeData = typeDataArray[n2];
            writer.writeVerifyTypeInfo(typeData.getTypeTag(), typeData.getTypeData(constPool));
            if (!typeData.is2WordType()) continue;
            ++n2;
        }
    }
}

