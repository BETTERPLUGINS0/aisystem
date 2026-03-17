/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClass;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtField;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.Modifier;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.NotFoundException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ClassFile;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Descriptor;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.MethodInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.CompileError;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.NoFieldException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.TokenId;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.ASTList;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.ASTree;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.Declarator;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.Keyword;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.Symbol;

public class MemberResolver
implements TokenId {
    private ClassPool classPool;
    private static final int YES = 0;
    private static final int NO = -1;
    private static final String INVALID = "<invalid>";
    private static Map<ClassPool, Reference<Map<String, String>>> invalidNamesMap = new WeakHashMap<ClassPool, Reference<Map<String, String>>>();
    private Map<String, String> invalidNames = null;

    public MemberResolver(ClassPool classPool) {
        this.classPool = classPool;
    }

    public ClassPool getClassPool() {
        return this.classPool;
    }

    private static void fatal() {
        throw new CompileError("fatal");
    }

    public Method lookupMethod(CtClass ctClass, CtClass ctClass2, MethodInfo methodInfo, String string, int[] nArray, int[] nArray2, String[] stringArray) {
        Method method;
        int n;
        Method method2 = null;
        if (methodInfo != null && ctClass == ctClass2 && methodInfo.getName().equals(string) && (n = this.compareSignature(methodInfo.getDescriptor(), nArray, nArray2, stringArray)) != -1) {
            Method method3 = new Method(ctClass, methodInfo, n);
            if (n == 0) {
                return method3;
            }
            method2 = method3;
        }
        if ((method = this.lookupMethod(ctClass, string, nArray, nArray2, stringArray, method2 != null)) != null) {
            return method;
        }
        return method2;
    }

    private Method lookupMethod(CtClass ctClass, String string, int[] nArray, int[] nArray2, String[] stringArray, boolean bl) {
        Method method = null;
        ClassFile classFile = ctClass.getClassFile2();
        if (classFile != null) {
            List<MethodInfo> list = classFile.getMethods();
            for (MethodInfo object2 : list) {
                int n;
                if (!object2.getName().equals(string) || (object2.getAccessFlags() & 0x40) != 0 || (n = this.compareSignature(object2.getDescriptor(), nArray, nArray2, stringArray)) == -1) continue;
                Method method2 = new Method(ctClass, object2, n);
                if (n == 0) {
                    return method2;
                }
                if (method != null && method.notmatch <= n) continue;
                method = method2;
            }
        }
        if (bl) {
            method = null;
        } else if (method != null) {
            return method;
        }
        int n = ctClass.getModifiers();
        boolean bl2 = Modifier.isInterface(n);
        try {
            Method method3;
            CtClass notFoundException;
            if (!bl2 && (notFoundException = ctClass.getSuperclass()) != null && (method3 = this.lookupMethod(notFoundException, string, nArray, nArray2, stringArray, bl)) != null) {
                return method3;
            }
        } catch (NotFoundException notFoundException) {
            // empty catch block
        }
        try {
            Method method4;
            CtClass[] ctClassArray;
            Object object = ctClassArray = ctClass.getInterfaces();
            int n2 = ((CtClass[])object).length;
            for (int i = 0; i < n2; ++i) {
                CtClass ctClass2 = object[i];
                Method method5 = this.lookupMethod(ctClass2, string, nArray, nArray2, stringArray, bl);
                if (method5 == null) continue;
                return method5;
            }
            if (bl2 && (object = ctClass.getSuperclass()) != null && (method4 = this.lookupMethod((CtClass)object, string, nArray, nArray2, stringArray, bl)) != null) {
                return method4;
            }
        } catch (NotFoundException notFoundException) {
            // empty catch block
        }
        return method;
    }

    private int compareSignature(String string, int[] nArray, int[] nArray2, String[] stringArray) {
        int n = 0;
        int n2 = 1;
        int n3 = nArray.length;
        if (n3 != Descriptor.numOfParameters(string)) {
            return -1;
        }
        int n4 = string.length();
        int n5 = 0;
        while (n2 < n4) {
            int n6;
            char c;
            if ((c = string.charAt(n2++)) == ')') {
                return n5 == n3 ? n : -1;
            }
            if (n5 >= n3) {
                return -1;
            }
            int n7 = 0;
            while (c == '[') {
                ++n7;
                c = string.charAt(n2++);
            }
            if (nArray[n5] == 412) {
                if (n7 == 0 && c != 'L') {
                    return -1;
                }
                if (c == 'L') {
                    n2 = string.indexOf(59, n2) + 1;
                }
            } else if (nArray2[n5] != n7) {
                if (n7 != 0 || c != 'L' || !string.startsWith("java/lang/Object;", n2)) {
                    return -1;
                }
                n2 = string.indexOf(59, n2) + 1;
                ++n;
                if (n2 <= 0) {
                    return -1;
                }
            } else if (c == 'L') {
                block23: {
                    n6 = string.indexOf(59, n2);
                    if (n6 < 0 || nArray[n5] != 307) {
                        return -1;
                    }
                    String string2 = string.substring(n2, n6);
                    if (!string2.equals(stringArray[n5])) {
                        CtClass ctClass = this.lookupClassByJvmName(stringArray[n5]);
                        try {
                            if (ctClass.subtypeOf(this.lookupClassByJvmName(string2))) {
                                ++n;
                                break block23;
                            }
                            return -1;
                        } catch (NotFoundException notFoundException) {
                            ++n;
                        }
                    }
                }
                n2 = n6 + 1;
            } else {
                int n8;
                n6 = MemberResolver.descToType(c);
                if (n6 != (n8 = nArray[n5])) {
                    if (n6 == 324 && (n8 == 334 || n8 == 303 || n8 == 306)) {
                        ++n;
                    } else {
                        return -1;
                    }
                }
            }
            ++n5;
        }
        return -1;
    }

    public CtField lookupFieldByJvmName2(String string, Symbol symbol, ASTree aSTree) {
        String string2 = symbol.get();
        CtClass ctClass = null;
        try {
            ctClass = this.lookupClass(MemberResolver.jvmToJavaName(string), true);
        } catch (CompileError compileError) {
            throw new NoFieldException(string + "/" + string2, aSTree);
        }
        try {
            return ctClass.getField(string2);
        } catch (NotFoundException notFoundException) {
            string = MemberResolver.javaToJvmName(ctClass.getName());
            throw new NoFieldException(string + "$" + string2, aSTree);
        }
    }

    public CtField lookupFieldByJvmName(String string, Symbol symbol) {
        return this.lookupField(MemberResolver.jvmToJavaName(string), symbol);
    }

    public CtField lookupField(String string, Symbol symbol) {
        CtClass ctClass = this.lookupClass(string, false);
        try {
            return ctClass.getField(symbol.get());
        } catch (NotFoundException notFoundException) {
            throw new CompileError("no such field: " + symbol.get());
        }
    }

    public CtClass lookupClassByName(ASTList aSTList) {
        return this.lookupClass(Declarator.astToClassName(aSTList, '.'), false);
    }

    public CtClass lookupClassByJvmName(String string) {
        return this.lookupClass(MemberResolver.jvmToJavaName(string), false);
    }

    public CtClass lookupClass(Declarator declarator) {
        return this.lookupClass(declarator.getType(), declarator.getArrayDim(), declarator.getClassName());
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public CtClass lookupClass(int n, int n2, String string) {
        String string2 = "";
        if (n == 307) {
            CtClass ctClass = this.lookupClassByJvmName(string);
            if (n2 <= 0) return ctClass;
            string2 = ctClass.getName();
        } else {
            string2 = MemberResolver.getTypeName(n);
        }
        while (n2-- > 0) {
            string2 = string2 + "[]";
        }
        return this.lookupClass(string2, false);
    }

    static String getTypeName(int n) {
        String string = "";
        switch (n) {
            case 301: {
                string = "boolean";
                break;
            }
            case 306: {
                string = "char";
                break;
            }
            case 303: {
                string = "byte";
                break;
            }
            case 334: {
                string = "short";
                break;
            }
            case 324: {
                string = "int";
                break;
            }
            case 326: {
                string = "long";
                break;
            }
            case 317: {
                string = "float";
                break;
            }
            case 312: {
                string = "double";
                break;
            }
            case 344: {
                string = "void";
                break;
            }
            default: {
                MemberResolver.fatal();
            }
        }
        return string;
    }

    public CtClass lookupClass(String string, boolean bl) {
        Map<String, String> map = this.getInvalidNames();
        String string2 = map.get(string);
        if (string2 == INVALID) {
            throw new CompileError("no such class: " + string);
        }
        if (string2 != null) {
            try {
                return this.classPool.get(string2);
            } catch (NotFoundException notFoundException) {
                // empty catch block
            }
        }
        CtClass ctClass = null;
        try {
            ctClass = this.lookupClass0(string, bl);
        } catch (NotFoundException notFoundException) {
            ctClass = this.searchImports(string);
        }
        map.put(string, ctClass.getName());
        return ctClass;
    }

    public static int getInvalidMapSize() {
        return invalidNamesMap.size();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private Map<String, String> getInvalidNames() {
        Map<String, String> map = this.invalidNames;
        if (map != null) return map;
        Class<MemberResolver> clazz = MemberResolver.class;
        synchronized (MemberResolver.class) {
            Reference<Map<String, String>> reference = invalidNamesMap.get(this.classPool);
            if (reference != null) {
                map = reference.get();
            }
            if (map == null) {
                map = new Hashtable<String, String>();
                invalidNamesMap.put(this.classPool, new WeakReference<Map<String, String>>(map));
            }
            // ** MonitorExit[var2_2] (shouldn't be in output)
            this.invalidNames = map;
            return map;
        }
    }

    private CtClass searchImports(String string) {
        if (string.indexOf(46) < 0) {
            Iterator<String> iterator = this.classPool.getImportedPackages();
            while (iterator.hasNext()) {
                String string2 = iterator.next();
                String string3 = string2.replaceAll("\\.$", "") + "." + string;
                try {
                    return this.classPool.get(string3);
                } catch (NotFoundException notFoundException) {
                    try {
                        if (!string2.endsWith("." + string)) continue;
                        return this.classPool.get(string2);
                    } catch (NotFoundException notFoundException2) {
                    }
                }
            }
        }
        this.getInvalidNames().put(string, INVALID);
        throw new CompileError("no such class: " + string);
    }

    private CtClass lookupClass0(String string, boolean bl) {
        CtClass ctClass = null;
        do {
            try {
                ctClass = this.classPool.get(string);
            } catch (NotFoundException notFoundException) {
                int n = string.lastIndexOf(46);
                if (bl || n < 0) {
                    throw notFoundException;
                }
                StringBuilder stringBuilder = new StringBuilder(string);
                stringBuilder.setCharAt(n, '$');
                string = stringBuilder.toString();
            }
        } while (ctClass == null);
        return ctClass;
    }

    public String resolveClassName(ASTList aSTList) {
        if (aSTList == null) {
            return null;
        }
        return MemberResolver.javaToJvmName(this.lookupClassByName(aSTList).getName());
    }

    public String resolveJvmClassName(String string) {
        if (string == null) {
            return null;
        }
        return MemberResolver.javaToJvmName(this.lookupClassByJvmName(string).getName());
    }

    public static CtClass getSuperclass(CtClass ctClass) {
        try {
            CtClass ctClass2 = ctClass.getSuperclass();
            if (ctClass2 != null) {
                return ctClass2;
            }
        } catch (NotFoundException notFoundException) {
            // empty catch block
        }
        throw new CompileError("cannot find the super class of " + ctClass.getName());
    }

    public static CtClass getSuperInterface(CtClass ctClass, String string) {
        try {
            CtClass[] ctClassArray = ctClass.getInterfaces();
            for (int i = 0; i < ctClassArray.length; ++i) {
                if (!ctClassArray[i].getName().equals(string)) continue;
                return ctClassArray[i];
            }
        } catch (NotFoundException notFoundException) {
            // empty catch block
        }
        throw new CompileError("cannot find the super interface " + string + " of " + ctClass.getName());
    }

    public static String javaToJvmName(String string) {
        return string.replace('.', '/');
    }

    public static String jvmToJavaName(String string) {
        return string.replace('/', '.');
    }

    public static int descToType(char c) {
        switch (c) {
            case 'Z': {
                return 301;
            }
            case 'C': {
                return 306;
            }
            case 'B': {
                return 303;
            }
            case 'S': {
                return 334;
            }
            case 'I': {
                return 324;
            }
            case 'J': {
                return 326;
            }
            case 'F': {
                return 317;
            }
            case 'D': {
                return 312;
            }
            case 'V': {
                return 344;
            }
            case 'L': 
            case '[': {
                return 307;
            }
        }
        MemberResolver.fatal();
        return 344;
    }

    public static int getModifiers(ASTList aSTList) {
        int n = 0;
        while (aSTList != null) {
            Keyword keyword = (Keyword)aSTList.head();
            aSTList = aSTList.tail();
            switch (keyword.get()) {
                case 335: {
                    n |= 8;
                    break;
                }
                case 315: {
                    n |= 0x10;
                    break;
                }
                case 338: {
                    n |= 0x20;
                    break;
                }
                case 300: {
                    n |= 0x400;
                    break;
                }
                case 332: {
                    n |= 1;
                    break;
                }
                case 331: {
                    n |= 4;
                    break;
                }
                case 330: {
                    n |= 2;
                    break;
                }
                case 345: {
                    n |= 0x40;
                    break;
                }
                case 342: {
                    n |= 0x80;
                    break;
                }
                case 347: {
                    n |= 0x800;
                }
            }
        }
        return n;
    }

    public static class Method {
        public CtClass declaring;
        public MethodInfo info;
        public int notmatch;

        public Method(CtClass ctClass, MethodInfo methodInfo, int n) {
            this.declaring = ctClass;
            this.info = methodInfo;
            this.notmatch = n;
        }

        public boolean isStatic() {
            int n = this.info.getAccessFlags();
            return (n & 8) != 0;
        }
    }
}

