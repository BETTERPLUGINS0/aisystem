/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.stackmap;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.BadBytecode;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.MethodInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.stackmap.BasicBlock;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.stackmap.TypeData;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.stackmap.TypeTag;

public class TypedBlock
extends BasicBlock {
    public int stackTop;
    public int numLocals;
    public TypeData[] localsTypes = null;
    public TypeData[] stackTypes;

    public static TypedBlock[] makeBlocks(MethodInfo methodInfo, CodeAttribute codeAttribute, boolean bl) {
        TypedBlock[] typedBlockArray = (TypedBlock[])new Maker().make(methodInfo);
        if (bl && typedBlockArray.length < 2 && (typedBlockArray.length == 0 || typedBlockArray[0].incoming == 0)) {
            return null;
        }
        ConstPool constPool = methodInfo.getConstPool();
        boolean bl2 = (methodInfo.getAccessFlags() & 8) != 0;
        typedBlockArray[0].initFirstBlock(codeAttribute.getMaxStack(), codeAttribute.getMaxLocals(), constPool.getClassName(), methodInfo.getDescriptor(), bl2, methodInfo.isConstructor());
        return typedBlockArray;
    }

    protected TypedBlock(int n) {
        super(n);
    }

    @Override
    protected void toString2(StringBuilder stringBuilder) {
        super.toString2(stringBuilder);
        stringBuilder.append(",\n stack={");
        this.printTypes(stringBuilder, this.stackTop, this.stackTypes);
        stringBuilder.append("}, locals={");
        this.printTypes(stringBuilder, this.numLocals, this.localsTypes);
        stringBuilder.append('}');
    }

    private void printTypes(StringBuilder stringBuilder, int n, TypeData[] typeDataArray) {
        if (typeDataArray == null) {
            return;
        }
        for (int i = 0; i < n; ++i) {
            TypeData typeData;
            if (i > 0) {
                stringBuilder.append(", ");
            }
            stringBuilder.append((typeData = typeDataArray[i]) == null ? "<>" : typeData.toString());
        }
    }

    public boolean alreadySet() {
        return this.localsTypes != null;
    }

    public void setStackMap(int n, TypeData[] typeDataArray, int n2, TypeData[] typeDataArray2) {
        this.stackTop = n;
        this.stackTypes = typeDataArray;
        this.numLocals = n2;
        this.localsTypes = typeDataArray2;
    }

    public void resetNumLocals() {
        if (this.localsTypes != null) {
            int n;
            for (n = this.localsTypes.length; !(n <= 0 || this.localsTypes[n - 1].isBasicType() != TypeTag.TOP || n > 1 && this.localsTypes[n - 2].is2WordType()); --n) {
            }
            this.numLocals = n;
        }
    }

    void initFirstBlock(int n, int n2, String string, String string2, boolean bl, boolean bl2) {
        if (string2.charAt(0) != '(') {
            throw new BadBytecode("no method descriptor: " + string2);
        }
        this.stackTop = 0;
        this.stackTypes = TypeData.make(n);
        TypeData[] typeDataArray = TypeData.make(n2);
        if (bl2) {
            typeDataArray[0] = new TypeData.UninitThis(string);
        } else if (!bl) {
            typeDataArray[0] = new TypeData.ClassName(string);
        }
        int n3 = bl ? -1 : 0;
        int n4 = 1;
        try {
            while ((n4 = TypedBlock.descToTag(string2, n4, ++n3, typeDataArray)) > 0) {
                if (!typeDataArray[n3].is2WordType()) continue;
                typeDataArray[++n3] = TypeTag.TOP;
            }
        } catch (StringIndexOutOfBoundsException stringIndexOutOfBoundsException) {
            throw new BadBytecode("bad method descriptor: " + string2);
        }
        this.numLocals = n3;
        this.localsTypes = typeDataArray;
    }

    private static int descToTag(String string, int n, int n2, TypeData[] typeDataArray) {
        int n3 = n;
        int n4 = 0;
        char c = string.charAt(n);
        if (c == ')') {
            return 0;
        }
        while (c == '[') {
            ++n4;
            c = string.charAt(++n);
        }
        if (c == 'L') {
            int n5 = string.indexOf(59, ++n);
            typeDataArray[n2] = n4 > 0 ? new TypeData.ClassName(string.substring(n3, ++n5)) : new TypeData.ClassName(string.substring(n3 + 1, ++n5 - 1).replace('/', '.'));
            return n5;
        }
        if (n4 > 0) {
            typeDataArray[n2] = new TypeData.ClassName(string.substring(n3, ++n));
            return n;
        }
        TypeData typeData = TypedBlock.toPrimitiveTag(c);
        if (typeData == null) {
            throw new BadBytecode("bad method descriptor: " + string);
        }
        typeDataArray[n2] = typeData;
        return n + 1;
    }

    private static TypeData toPrimitiveTag(char c) {
        switch (c) {
            case 'B': 
            case 'C': 
            case 'I': 
            case 'S': 
            case 'Z': {
                return TypeTag.INTEGER;
            }
            case 'J': {
                return TypeTag.LONG;
            }
            case 'F': {
                return TypeTag.FLOAT;
            }
            case 'D': {
                return TypeTag.DOUBLE;
            }
        }
        return null;
    }

    public static String getRetType(String string) {
        int n = string.indexOf(41);
        if (n < 0) {
            return "java.lang.Object";
        }
        char c = string.charAt(n + 1);
        if (c == '[') {
            return string.substring(n + 1);
        }
        if (c == 'L') {
            return string.substring(n + 2, string.length() - 1).replace('/', '.');
        }
        return "java.lang.Object";
    }

    public static class Maker
    extends BasicBlock.Maker {
        @Override
        protected BasicBlock makeBlock(int n) {
            return new TypedBlock(n);
        }

        @Override
        protected BasicBlock[] makeArray(int n) {
            return new TypedBlock[n];
        }
    }
}

