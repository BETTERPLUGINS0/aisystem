/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClass;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ClassInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstInfoPadding;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Descriptor;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.DoubleInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.DynamicInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.FieldrefInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.FloatInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.IntegerInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.InterfaceMethodrefInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.InvokeDynamicInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.LongInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.LongVector;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.MemberrefInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.MethodHandleInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.MethodTypeInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.MethodrefInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ModuleInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.NameAndTypeInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.PackageInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.StringInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Utf8Info;

public final class ConstPool {
    LongVector items;
    int numOfItems;
    int thisClassInfo;
    Map<ConstInfo, ConstInfo> itemsCache;
    public static final int CONST_Class = 7;
    public static final int CONST_Fieldref = 9;
    public static final int CONST_Methodref = 10;
    public static final int CONST_InterfaceMethodref = 11;
    public static final int CONST_String = 8;
    public static final int CONST_Integer = 3;
    public static final int CONST_Float = 4;
    public static final int CONST_Long = 5;
    public static final int CONST_Double = 6;
    public static final int CONST_NameAndType = 12;
    public static final int CONST_Utf8 = 1;
    public static final int CONST_MethodHandle = 15;
    public static final int CONST_MethodType = 16;
    public static final int CONST_Dynamic = 17;
    public static final int CONST_DynamicCallSite = 18;
    public static final int CONST_InvokeDynamic = 18;
    public static final int CONST_Module = 19;
    public static final int CONST_Package = 20;
    public static final CtClass THIS = null;
    public static final int REF_getField = 1;
    public static final int REF_getStatic = 2;
    public static final int REF_putField = 3;
    public static final int REF_putStatic = 4;
    public static final int REF_invokeVirtual = 5;
    public static final int REF_invokeStatic = 6;
    public static final int REF_invokeSpecial = 7;
    public static final int REF_newInvokeSpecial = 8;
    public static final int REF_invokeInterface = 9;

    public ConstPool(String string) {
        this.items = new LongVector();
        this.itemsCache = null;
        this.numOfItems = 0;
        this.addItem0(null);
        this.thisClassInfo = this.addClassInfo(string);
    }

    public ConstPool(DataInputStream dataInputStream) {
        this.itemsCache = null;
        this.thisClassInfo = 0;
        this.read(dataInputStream);
    }

    void prune() {
        this.itemsCache = null;
    }

    public int getSize() {
        return this.numOfItems;
    }

    public String getClassName() {
        return this.getClassInfo(this.thisClassInfo);
    }

    public int getThisClassInfo() {
        return this.thisClassInfo;
    }

    void setThisClassInfo(int n) {
        this.thisClassInfo = n;
    }

    ConstInfo getItem(int n) {
        return this.items.elementAt(n);
    }

    public int getTag(int n) {
        return this.getItem(n).getTag();
    }

    public String getClassInfo(int n) {
        ClassInfo classInfo = (ClassInfo)this.getItem(n);
        if (classInfo == null) {
            return null;
        }
        return Descriptor.toJavaName(this.getUtf8Info(classInfo.name));
    }

    public String getClassInfoByDescriptor(int n) {
        ClassInfo classInfo = (ClassInfo)this.getItem(n);
        if (classInfo == null) {
            return null;
        }
        String string = this.getUtf8Info(classInfo.name);
        if (string.charAt(0) == '[') {
            return string;
        }
        return Descriptor.of(string);
    }

    public int getNameAndTypeName(int n) {
        NameAndTypeInfo nameAndTypeInfo = (NameAndTypeInfo)this.getItem(n);
        return nameAndTypeInfo.memberName;
    }

    public int getNameAndTypeDescriptor(int n) {
        NameAndTypeInfo nameAndTypeInfo = (NameAndTypeInfo)this.getItem(n);
        return nameAndTypeInfo.typeDescriptor;
    }

    public int getMemberClass(int n) {
        MemberrefInfo memberrefInfo = (MemberrefInfo)this.getItem(n);
        return memberrefInfo.classIndex;
    }

    public int getMemberNameAndType(int n) {
        MemberrefInfo memberrefInfo = (MemberrefInfo)this.getItem(n);
        return memberrefInfo.nameAndTypeIndex;
    }

    public int getFieldrefClass(int n) {
        FieldrefInfo fieldrefInfo = (FieldrefInfo)this.getItem(n);
        return fieldrefInfo.classIndex;
    }

    public String getFieldrefClassName(int n) {
        FieldrefInfo fieldrefInfo = (FieldrefInfo)this.getItem(n);
        if (fieldrefInfo == null) {
            return null;
        }
        return this.getClassInfo(fieldrefInfo.classIndex);
    }

    public int getFieldrefNameAndType(int n) {
        FieldrefInfo fieldrefInfo = (FieldrefInfo)this.getItem(n);
        return fieldrefInfo.nameAndTypeIndex;
    }

    public String getFieldrefName(int n) {
        FieldrefInfo fieldrefInfo = (FieldrefInfo)this.getItem(n);
        if (fieldrefInfo == null) {
            return null;
        }
        NameAndTypeInfo nameAndTypeInfo = (NameAndTypeInfo)this.getItem(fieldrefInfo.nameAndTypeIndex);
        if (nameAndTypeInfo == null) {
            return null;
        }
        return this.getUtf8Info(nameAndTypeInfo.memberName);
    }

    public String getFieldrefType(int n) {
        FieldrefInfo fieldrefInfo = (FieldrefInfo)this.getItem(n);
        if (fieldrefInfo == null) {
            return null;
        }
        NameAndTypeInfo nameAndTypeInfo = (NameAndTypeInfo)this.getItem(fieldrefInfo.nameAndTypeIndex);
        if (nameAndTypeInfo == null) {
            return null;
        }
        return this.getUtf8Info(nameAndTypeInfo.typeDescriptor);
    }

    public int getMethodrefClass(int n) {
        MemberrefInfo memberrefInfo = (MemberrefInfo)this.getItem(n);
        return memberrefInfo.classIndex;
    }

    public String getMethodrefClassName(int n) {
        MemberrefInfo memberrefInfo = (MemberrefInfo)this.getItem(n);
        if (memberrefInfo == null) {
            return null;
        }
        return this.getClassInfo(memberrefInfo.classIndex);
    }

    public int getMethodrefNameAndType(int n) {
        MemberrefInfo memberrefInfo = (MemberrefInfo)this.getItem(n);
        return memberrefInfo.nameAndTypeIndex;
    }

    public String getMethodrefName(int n) {
        MemberrefInfo memberrefInfo = (MemberrefInfo)this.getItem(n);
        if (memberrefInfo == null) {
            return null;
        }
        NameAndTypeInfo nameAndTypeInfo = (NameAndTypeInfo)this.getItem(memberrefInfo.nameAndTypeIndex);
        if (nameAndTypeInfo == null) {
            return null;
        }
        return this.getUtf8Info(nameAndTypeInfo.memberName);
    }

    public String getMethodrefType(int n) {
        MemberrefInfo memberrefInfo = (MemberrefInfo)this.getItem(n);
        if (memberrefInfo == null) {
            return null;
        }
        NameAndTypeInfo nameAndTypeInfo = (NameAndTypeInfo)this.getItem(memberrefInfo.nameAndTypeIndex);
        if (nameAndTypeInfo == null) {
            return null;
        }
        return this.getUtf8Info(nameAndTypeInfo.typeDescriptor);
    }

    public int getInterfaceMethodrefClass(int n) {
        MemberrefInfo memberrefInfo = (MemberrefInfo)this.getItem(n);
        return memberrefInfo.classIndex;
    }

    public String getInterfaceMethodrefClassName(int n) {
        MemberrefInfo memberrefInfo = (MemberrefInfo)this.getItem(n);
        return this.getClassInfo(memberrefInfo.classIndex);
    }

    public int getInterfaceMethodrefNameAndType(int n) {
        MemberrefInfo memberrefInfo = (MemberrefInfo)this.getItem(n);
        return memberrefInfo.nameAndTypeIndex;
    }

    public String getInterfaceMethodrefName(int n) {
        MemberrefInfo memberrefInfo = (MemberrefInfo)this.getItem(n);
        if (memberrefInfo == null) {
            return null;
        }
        NameAndTypeInfo nameAndTypeInfo = (NameAndTypeInfo)this.getItem(memberrefInfo.nameAndTypeIndex);
        if (nameAndTypeInfo == null) {
            return null;
        }
        return this.getUtf8Info(nameAndTypeInfo.memberName);
    }

    public String getInterfaceMethodrefType(int n) {
        MemberrefInfo memberrefInfo = (MemberrefInfo)this.getItem(n);
        if (memberrefInfo == null) {
            return null;
        }
        NameAndTypeInfo nameAndTypeInfo = (NameAndTypeInfo)this.getItem(memberrefInfo.nameAndTypeIndex);
        if (nameAndTypeInfo == null) {
            return null;
        }
        return this.getUtf8Info(nameAndTypeInfo.typeDescriptor);
    }

    public Object getLdcValue(int n) {
        ConstInfo constInfo = this.getItem(n);
        Object object = null;
        if (constInfo instanceof StringInfo) {
            object = this.getStringInfo(n);
        } else if (constInfo instanceof FloatInfo) {
            object = Float.valueOf(this.getFloatInfo(n));
        } else if (constInfo instanceof IntegerInfo) {
            object = this.getIntegerInfo(n);
        } else if (constInfo instanceof LongInfo) {
            object = this.getLongInfo(n);
        } else if (constInfo instanceof DoubleInfo) {
            object = this.getDoubleInfo(n);
        }
        return object;
    }

    public int getIntegerInfo(int n) {
        IntegerInfo integerInfo = (IntegerInfo)this.getItem(n);
        return integerInfo.value;
    }

    public float getFloatInfo(int n) {
        FloatInfo floatInfo = (FloatInfo)this.getItem(n);
        return floatInfo.value;
    }

    public long getLongInfo(int n) {
        LongInfo longInfo = (LongInfo)this.getItem(n);
        return longInfo.value;
    }

    public double getDoubleInfo(int n) {
        DoubleInfo doubleInfo = (DoubleInfo)this.getItem(n);
        return doubleInfo.value;
    }

    public String getStringInfo(int n) {
        StringInfo stringInfo = (StringInfo)this.getItem(n);
        return this.getUtf8Info(stringInfo.string);
    }

    public String getUtf8Info(int n) {
        Utf8Info utf8Info = (Utf8Info)this.getItem(n);
        return utf8Info.string;
    }

    public int getMethodHandleKind(int n) {
        MethodHandleInfo methodHandleInfo = (MethodHandleInfo)this.getItem(n);
        return methodHandleInfo.refKind;
    }

    public int getMethodHandleIndex(int n) {
        MethodHandleInfo methodHandleInfo = (MethodHandleInfo)this.getItem(n);
        return methodHandleInfo.refIndex;
    }

    public int getMethodTypeInfo(int n) {
        MethodTypeInfo methodTypeInfo = (MethodTypeInfo)this.getItem(n);
        return methodTypeInfo.descriptor;
    }

    public int getInvokeDynamicBootstrap(int n) {
        InvokeDynamicInfo invokeDynamicInfo = (InvokeDynamicInfo)this.getItem(n);
        return invokeDynamicInfo.bootstrap;
    }

    public int getInvokeDynamicNameAndType(int n) {
        InvokeDynamicInfo invokeDynamicInfo = (InvokeDynamicInfo)this.getItem(n);
        return invokeDynamicInfo.nameAndType;
    }

    public String getInvokeDynamicType(int n) {
        InvokeDynamicInfo invokeDynamicInfo = (InvokeDynamicInfo)this.getItem(n);
        if (invokeDynamicInfo == null) {
            return null;
        }
        NameAndTypeInfo nameAndTypeInfo = (NameAndTypeInfo)this.getItem(invokeDynamicInfo.nameAndType);
        if (nameAndTypeInfo == null) {
            return null;
        }
        return this.getUtf8Info(nameAndTypeInfo.typeDescriptor);
    }

    public int getDynamicBootstrap(int n) {
        DynamicInfo dynamicInfo = (DynamicInfo)this.getItem(n);
        return dynamicInfo.bootstrap;
    }

    public int getDynamicNameAndType(int n) {
        DynamicInfo dynamicInfo = (DynamicInfo)this.getItem(n);
        return dynamicInfo.nameAndType;
    }

    public String getDynamicType(int n) {
        DynamicInfo dynamicInfo = (DynamicInfo)this.getItem(n);
        if (dynamicInfo == null) {
            return null;
        }
        NameAndTypeInfo nameAndTypeInfo = (NameAndTypeInfo)this.getItem(dynamicInfo.nameAndType);
        if (nameAndTypeInfo == null) {
            return null;
        }
        return this.getUtf8Info(nameAndTypeInfo.typeDescriptor);
    }

    public String getModuleInfo(int n) {
        ModuleInfo moduleInfo = (ModuleInfo)this.getItem(n);
        return this.getUtf8Info(moduleInfo.name);
    }

    public String getPackageInfo(int n) {
        PackageInfo packageInfo = (PackageInfo)this.getItem(n);
        return this.getUtf8Info(packageInfo.name);
    }

    public int isConstructor(String string, int n) {
        return this.isMember(string, "<init>", n);
    }

    public int isMember(String string, String string2, int n) {
        MemberrefInfo memberrefInfo = (MemberrefInfo)this.getItem(n);
        if (this.getClassInfo(memberrefInfo.classIndex).equals(string)) {
            NameAndTypeInfo nameAndTypeInfo = (NameAndTypeInfo)this.getItem(memberrefInfo.nameAndTypeIndex);
            if (this.getUtf8Info(nameAndTypeInfo.memberName).equals(string2)) {
                return nameAndTypeInfo.typeDescriptor;
            }
        }
        return 0;
    }

    public String eqMember(String string, String string2, int n) {
        MemberrefInfo memberrefInfo = (MemberrefInfo)this.getItem(n);
        NameAndTypeInfo nameAndTypeInfo = (NameAndTypeInfo)this.getItem(memberrefInfo.nameAndTypeIndex);
        if (this.getUtf8Info(nameAndTypeInfo.memberName).equals(string) && this.getUtf8Info(nameAndTypeInfo.typeDescriptor).equals(string2)) {
            return this.getClassInfo(memberrefInfo.classIndex);
        }
        return null;
    }

    private int addItem0(ConstInfo constInfo) {
        this.items.addElement(constInfo);
        return this.numOfItems++;
    }

    private int addItem(ConstInfo constInfo) {
        ConstInfo constInfo2;
        if (this.itemsCache == null) {
            this.itemsCache = ConstPool.makeItemsCache(this.items);
        }
        if ((constInfo2 = this.itemsCache.get(constInfo)) != null) {
            return constInfo2.index;
        }
        this.items.addElement(constInfo);
        this.itemsCache.put(constInfo, constInfo);
        return this.numOfItems++;
    }

    public int copy(int n, ConstPool constPool, Map<String, String> map) {
        if (n == 0) {
            return 0;
        }
        ConstInfo constInfo = this.getItem(n);
        return constInfo.copy(this, constPool, map);
    }

    int addConstInfoPadding() {
        return this.addItem0(new ConstInfoPadding(this.numOfItems));
    }

    public int addClassInfo(CtClass ctClass) {
        if (ctClass == THIS) {
            return this.thisClassInfo;
        }
        if (!ctClass.isArray()) {
            return this.addClassInfo(ctClass.getName());
        }
        return this.addClassInfo(Descriptor.toJvmName(ctClass));
    }

    public int addClassInfo(String string) {
        int n = this.addUtf8Info(Descriptor.toJvmName(string));
        return this.addItem(new ClassInfo(n, this.numOfItems));
    }

    public int addNameAndTypeInfo(String string, String string2) {
        return this.addNameAndTypeInfo(this.addUtf8Info(string), this.addUtf8Info(string2));
    }

    public int addNameAndTypeInfo(int n, int n2) {
        return this.addItem(new NameAndTypeInfo(n, n2, this.numOfItems));
    }

    public int addFieldrefInfo(int n, String string, String string2) {
        int n2 = this.addNameAndTypeInfo(string, string2);
        return this.addFieldrefInfo(n, n2);
    }

    public int addFieldrefInfo(int n, int n2) {
        return this.addItem(new FieldrefInfo(n, n2, this.numOfItems));
    }

    public int addMethodrefInfo(int n, String string, String string2) {
        int n2 = this.addNameAndTypeInfo(string, string2);
        return this.addMethodrefInfo(n, n2);
    }

    public int addMethodrefInfo(int n, int n2) {
        return this.addItem(new MethodrefInfo(n, n2, this.numOfItems));
    }

    public int addInterfaceMethodrefInfo(int n, String string, String string2) {
        int n2 = this.addNameAndTypeInfo(string, string2);
        return this.addInterfaceMethodrefInfo(n, n2);
    }

    public int addInterfaceMethodrefInfo(int n, int n2) {
        return this.addItem(new InterfaceMethodrefInfo(n, n2, this.numOfItems));
    }

    public int addStringInfo(String string) {
        int n = this.addUtf8Info(string);
        return this.addItem(new StringInfo(n, this.numOfItems));
    }

    public int addIntegerInfo(int n) {
        return this.addItem(new IntegerInfo(n, this.numOfItems));
    }

    public int addFloatInfo(float f) {
        return this.addItem(new FloatInfo(f, this.numOfItems));
    }

    public int addLongInfo(long l) {
        int n = this.addItem(new LongInfo(l, this.numOfItems));
        if (n == this.numOfItems - 1) {
            this.addConstInfoPadding();
        }
        return n;
    }

    public int addDoubleInfo(double d) {
        int n = this.addItem(new DoubleInfo(d, this.numOfItems));
        if (n == this.numOfItems - 1) {
            this.addConstInfoPadding();
        }
        return n;
    }

    public int addUtf8Info(String string) {
        return this.addItem(new Utf8Info(string, this.numOfItems));
    }

    public int addMethodHandleInfo(int n, int n2) {
        return this.addItem(new MethodHandleInfo(n, n2, this.numOfItems));
    }

    public int addMethodTypeInfo(int n) {
        return this.addItem(new MethodTypeInfo(n, this.numOfItems));
    }

    public int addInvokeDynamicInfo(int n, int n2) {
        return this.addItem(new InvokeDynamicInfo(n, n2, this.numOfItems));
    }

    public int addDynamicInfo(int n, int n2) {
        return this.addItem(new DynamicInfo(n, n2, this.numOfItems));
    }

    public int addModuleInfo(int n) {
        return this.addItem(new ModuleInfo(n, this.numOfItems));
    }

    public int addPackageInfo(int n) {
        return this.addItem(new PackageInfo(n, this.numOfItems));
    }

    public Set<String> getClassNames() {
        HashSet<String> hashSet = new HashSet<String>();
        LongVector longVector = this.items;
        int n = this.numOfItems;
        for (int i = 1; i < n; ++i) {
            String string = longVector.elementAt(i).getClassName(this);
            if (string == null) continue;
            hashSet.add(string);
        }
        return hashSet;
    }

    public void renameClass(String string, String string2) {
        LongVector longVector = this.items;
        int n = this.numOfItems;
        for (int i = 1; i < n; ++i) {
            ConstInfo constInfo = longVector.elementAt(i);
            constInfo.renameClass(this, string, string2, this.itemsCache);
        }
    }

    public void renameClass(Map<String, String> map) {
        LongVector longVector = this.items;
        int n = this.numOfItems;
        for (int i = 1; i < n; ++i) {
            ConstInfo constInfo = longVector.elementAt(i);
            constInfo.renameClass(this, map, this.itemsCache);
        }
    }

    private void read(DataInputStream dataInputStream) {
        int n = dataInputStream.readUnsignedShort();
        this.items = new LongVector(n);
        this.numOfItems = 0;
        this.addItem0(null);
        while (--n > 0) {
            int n2 = this.readOne(dataInputStream);
            if (n2 != 5 && n2 != 6) continue;
            this.addConstInfoPadding();
            --n;
        }
    }

    private static Map<ConstInfo, ConstInfo> makeItemsCache(LongVector longVector) {
        ConstInfo constInfo;
        HashMap<ConstInfo, ConstInfo> hashMap = new HashMap<ConstInfo, ConstInfo>();
        int n = 1;
        while ((constInfo = longVector.elementAt(n++)) != null) {
            hashMap.put(constInfo, constInfo);
        }
        return hashMap;
    }

    private int readOne(DataInputStream dataInputStream) {
        ConstInfo constInfo;
        int n = dataInputStream.readUnsignedByte();
        switch (n) {
            case 1: {
                constInfo = new Utf8Info(dataInputStream, this.numOfItems);
                break;
            }
            case 3: {
                constInfo = new IntegerInfo(dataInputStream, this.numOfItems);
                break;
            }
            case 4: {
                constInfo = new FloatInfo(dataInputStream, this.numOfItems);
                break;
            }
            case 5: {
                constInfo = new LongInfo(dataInputStream, this.numOfItems);
                break;
            }
            case 6: {
                constInfo = new DoubleInfo(dataInputStream, this.numOfItems);
                break;
            }
            case 7: {
                constInfo = new ClassInfo(dataInputStream, this.numOfItems);
                break;
            }
            case 8: {
                constInfo = new StringInfo(dataInputStream, this.numOfItems);
                break;
            }
            case 9: {
                constInfo = new FieldrefInfo(dataInputStream, this.numOfItems);
                break;
            }
            case 10: {
                constInfo = new MethodrefInfo(dataInputStream, this.numOfItems);
                break;
            }
            case 11: {
                constInfo = new InterfaceMethodrefInfo(dataInputStream, this.numOfItems);
                break;
            }
            case 12: {
                constInfo = new NameAndTypeInfo(dataInputStream, this.numOfItems);
                break;
            }
            case 15: {
                constInfo = new MethodHandleInfo(dataInputStream, this.numOfItems);
                break;
            }
            case 16: {
                constInfo = new MethodTypeInfo(dataInputStream, this.numOfItems);
                break;
            }
            case 17: {
                constInfo = new DynamicInfo(dataInputStream, this.numOfItems);
                break;
            }
            case 18: {
                constInfo = new InvokeDynamicInfo(dataInputStream, this.numOfItems);
                break;
            }
            case 19: {
                constInfo = new ModuleInfo(dataInputStream, this.numOfItems);
                break;
            }
            case 20: {
                constInfo = new PackageInfo(dataInputStream, this.numOfItems);
                break;
            }
            default: {
                throw new IOException("invalid constant type: " + n + " at " + this.numOfItems);
            }
        }
        this.addItem0(constInfo);
        return n;
    }

    public void write(DataOutputStream dataOutputStream) {
        if (this.numOfItems < 0 || 65535 < this.numOfItems) {
            throw new IOException("too many constant pool items " + this.numOfItems);
        }
        dataOutputStream.writeShort(this.numOfItems);
        LongVector longVector = this.items;
        int n = this.numOfItems;
        for (int i = 1; i < n; ++i) {
            longVector.elementAt(i).write(dataOutputStream);
        }
    }

    public void print() {
        this.print(new PrintWriter(System.out, true));
    }

    public void print(PrintWriter printWriter) {
        int n = this.numOfItems;
        for (int i = 1; i < n; ++i) {
            printWriter.print(i);
            printWriter.print(" ");
            this.items.elementAt(i).print(printWriter);
        }
    }
}

