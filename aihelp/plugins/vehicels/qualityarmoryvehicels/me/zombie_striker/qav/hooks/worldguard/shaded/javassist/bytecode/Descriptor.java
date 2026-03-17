/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode;

import java.util.Map;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClass;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtPrimitiveType;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.NotFoundException;

public class Descriptor {
    public static String toJvmName(String string) {
        return string.replace('.', '/');
    }

    public static String toJavaName(String string) {
        return string.replace('/', '.');
    }

    public static String toJvmName(CtClass ctClass) {
        if (ctClass.isArray()) {
            return Descriptor.of(ctClass);
        }
        return Descriptor.toJvmName(ctClass.getName());
    }

    public static String toClassName(String string) {
        String string2;
        int n = 0;
        int n2 = 0;
        char c = string.charAt(0);
        while (c == '[') {
            ++n;
            c = string.charAt(++n2);
        }
        if (c == 'L') {
            int n3 = string.indexOf(59, n2++);
            string2 = string.substring(n2, n3).replace('/', '.');
            n2 = n3;
        } else if (c == 'V') {
            string2 = "void";
        } else if (c == 'I') {
            string2 = "int";
        } else if (c == 'B') {
            string2 = "byte";
        } else if (c == 'J') {
            string2 = "long";
        } else if (c == 'D') {
            string2 = "double";
        } else if (c == 'F') {
            string2 = "float";
        } else if (c == 'C') {
            string2 = "char";
        } else if (c == 'S') {
            string2 = "short";
        } else if (c == 'Z') {
            string2 = "boolean";
        } else {
            throw new RuntimeException("bad descriptor: " + string);
        }
        if (n2 + 1 != string.length()) {
            throw new RuntimeException("multiple descriptors?: " + string);
        }
        if (n == 0) {
            return string2;
        }
        StringBuilder stringBuilder = new StringBuilder(string2);
        do {
            stringBuilder.append("[]");
        } while (--n > 0);
        return stringBuilder.toString();
    }

    public static String of(String string) {
        if (string.equals("void")) {
            return "V";
        }
        if (string.equals("int")) {
            return "I";
        }
        if (string.equals("byte")) {
            return "B";
        }
        if (string.equals("long")) {
            return "J";
        }
        if (string.equals("double")) {
            return "D";
        }
        if (string.equals("float")) {
            return "F";
        }
        if (string.equals("char")) {
            return "C";
        }
        if (string.equals("short")) {
            return "S";
        }
        if (string.equals("boolean")) {
            return "Z";
        }
        return "L" + Descriptor.toJvmName(string) + ";";
    }

    public static String rename(String string, String string2, String string3) {
        int n;
        if (string.indexOf(string2) < 0) {
            return string;
        }
        StringBuilder stringBuilder = new StringBuilder();
        int n2 = 0;
        int n3 = 0;
        while ((n = string.indexOf(76, n3)) >= 0) {
            if (string.startsWith(string2, n + 1) && string.charAt(n + string2.length() + 1) == ';') {
                stringBuilder.append(string.substring(n2, n));
                stringBuilder.append('L');
                stringBuilder.append(string3);
                stringBuilder.append(';');
                n2 = n3 = n + string2.length() + 2;
                continue;
            }
            n3 = string.indexOf(59, n) + 1;
            if (n3 >= 1) continue;
            break;
        }
        if (n2 == 0) {
            return string;
        }
        n = string.length();
        if (n2 < n) {
            stringBuilder.append(string.substring(n2, n));
        }
        return stringBuilder.toString();
    }

    public static String rename(String string, Map<String, String> map) {
        int n;
        int n2;
        if (map == null) {
            return string;
        }
        StringBuilder stringBuilder = new StringBuilder();
        int n3 = 0;
        int n4 = 0;
        while ((n2 = string.indexOf(76, n4)) >= 0 && (n = string.indexOf(59, n2)) >= 0) {
            n4 = n + 1;
            String string2 = string.substring(n2 + 1, n);
            String string3 = map.get(string2);
            if (string3 == null) continue;
            stringBuilder.append(string.substring(n3, n2));
            stringBuilder.append('L');
            stringBuilder.append(string3);
            stringBuilder.append(';');
            n3 = n4;
        }
        if (n3 == 0) {
            return string;
        }
        n2 = string.length();
        if (n3 < n2) {
            stringBuilder.append(string.substring(n3, n2));
        }
        return stringBuilder.toString();
    }

    public static String of(CtClass ctClass) {
        StringBuilder stringBuilder = new StringBuilder();
        Descriptor.toDescriptor(stringBuilder, ctClass);
        return stringBuilder.toString();
    }

    private static void toDescriptor(StringBuilder stringBuilder, CtClass ctClass) {
        if (ctClass.isArray()) {
            stringBuilder.append('[');
            try {
                Descriptor.toDescriptor(stringBuilder, ctClass.getComponentType());
            } catch (NotFoundException notFoundException) {
                stringBuilder.append('L');
                String string = ctClass.getName();
                stringBuilder.append(Descriptor.toJvmName(string.substring(0, string.length() - 2)));
                stringBuilder.append(';');
            }
        } else if (ctClass.isPrimitive()) {
            CtPrimitiveType ctPrimitiveType = (CtPrimitiveType)ctClass;
            stringBuilder.append(ctPrimitiveType.getDescriptor());
        } else {
            stringBuilder.append('L');
            stringBuilder.append(ctClass.getName().replace('.', '/'));
            stringBuilder.append(';');
        }
    }

    public static String ofConstructor(CtClass[] ctClassArray) {
        return Descriptor.ofMethod(CtClass.voidType, ctClassArray);
    }

    public static String ofMethod(CtClass ctClass, CtClass[] ctClassArray) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('(');
        if (ctClassArray != null) {
            int n = ctClassArray.length;
            for (int i = 0; i < n; ++i) {
                Descriptor.toDescriptor(stringBuilder, ctClassArray[i]);
            }
        }
        stringBuilder.append(')');
        if (ctClass != null) {
            Descriptor.toDescriptor(stringBuilder, ctClass);
        }
        return stringBuilder.toString();
    }

    public static String ofParameters(CtClass[] ctClassArray) {
        return Descriptor.ofMethod(null, ctClassArray);
    }

    public static String appendParameter(String string, String string2) {
        int n = string2.indexOf(41);
        if (n < 0) {
            return string2;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(string2.substring(0, n));
        stringBuilder.append('L');
        stringBuilder.append(string.replace('.', '/'));
        stringBuilder.append(';');
        stringBuilder.append(string2.substring(n));
        return stringBuilder.toString();
    }

    public static String insertParameter(String string, String string2) {
        if (string2.charAt(0) != '(') {
            return string2;
        }
        return "(L" + string.replace('.', '/') + ';' + string2.substring(1);
    }

    public static String appendParameter(CtClass ctClass, String string) {
        int n = string.indexOf(41);
        if (n < 0) {
            return string;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(string.substring(0, n));
        Descriptor.toDescriptor(stringBuilder, ctClass);
        stringBuilder.append(string.substring(n));
        return stringBuilder.toString();
    }

    public static String insertParameter(CtClass ctClass, String string) {
        if (string.charAt(0) != '(') {
            return string;
        }
        return "(" + Descriptor.of(ctClass) + string.substring(1);
    }

    public static String changeReturnType(String string, String string2) {
        int n = string2.indexOf(41);
        if (n < 0) {
            return string2;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(string2.substring(0, n + 1));
        stringBuilder.append('L');
        stringBuilder.append(string.replace('.', '/'));
        stringBuilder.append(';');
        return stringBuilder.toString();
    }

    public static CtClass[] getParameterTypes(String string, ClassPool classPool) {
        if (string.charAt(0) != '(') {
            return null;
        }
        int n = Descriptor.numOfParameters(string);
        CtClass[] ctClassArray = new CtClass[n];
        int n2 = 0;
        int n3 = 1;
        while ((n3 = Descriptor.toCtClass(classPool, string, n3, ctClassArray, n2++)) > 0) {
        }
        return ctClassArray;
    }

    public static boolean eqParamTypes(String string, String string2) {
        if (string.charAt(0) != '(') {
            return false;
        }
        int n = 0;
        char c;
        while ((c = string.charAt(n)) == string2.charAt(n)) {
            if (c == ')') {
                return true;
            }
            ++n;
        }
        return false;
    }

    public static String getParamDescriptor(String string) {
        return string.substring(0, string.indexOf(41) + 1);
    }

    public static CtClass getReturnType(String string, ClassPool classPool) {
        int n = string.indexOf(41);
        if (n < 0) {
            return null;
        }
        CtClass[] ctClassArray = new CtClass[1];
        Descriptor.toCtClass(classPool, string, n + 1, ctClassArray, 0);
        return ctClassArray[0];
    }

    public static int numOfParameters(String string) {
        char c;
        int n = 0;
        int n2 = 1;
        while ((c = string.charAt(n2)) != ')') {
            while (c == '[') {
                c = string.charAt(++n2);
            }
            if (c == 'L') {
                if ((n2 = string.indexOf(59, n2) + 1) <= 0) {
                    throw new IndexOutOfBoundsException("bad descriptor");
                }
            } else {
                ++n2;
            }
            ++n;
        }
        return n;
    }

    public static CtClass toCtClass(String string, ClassPool classPool) {
        CtClass[] ctClassArray = new CtClass[1];
        int n = Descriptor.toCtClass(classPool, string, 0, ctClassArray, 0);
        if (n >= 0) {
            return ctClassArray[0];
        }
        return classPool.get(string.replace('/', '.'));
    }

    private static int toCtClass(ClassPool classPool, String string, int n, CtClass[] ctClassArray, int n2) {
        Object object;
        String string2;
        int n3;
        int n4 = 0;
        char c = string.charAt(n);
        while (c == '[') {
            ++n4;
            c = string.charAt(++n);
        }
        if (c == 'L') {
            n3 = string.indexOf(59, ++n);
            string2 = string.substring(n, n3++).replace('/', '.');
        } else {
            object = Descriptor.toPrimitiveClass(c);
            if (object == null) {
                return -1;
            }
            n3 = n + 1;
            if (n4 == 0) {
                ctClassArray[n2] = object;
                return n3;
            }
            string2 = ((CtClass)object).getName();
        }
        if (n4 > 0) {
            object = new StringBuilder(string2);
            while (n4-- > 0) {
                ((StringBuilder)object).append("[]");
            }
            string2 = ((StringBuilder)object).toString();
        }
        ctClassArray[n2] = classPool.get(string2);
        return n3;
    }

    static CtClass toPrimitiveClass(char c) {
        CtClass ctClass = null;
        switch (c) {
            case 'Z': {
                ctClass = CtClass.booleanType;
                break;
            }
            case 'C': {
                ctClass = CtClass.charType;
                break;
            }
            case 'B': {
                ctClass = CtClass.byteType;
                break;
            }
            case 'S': {
                ctClass = CtClass.shortType;
                break;
            }
            case 'I': {
                ctClass = CtClass.intType;
                break;
            }
            case 'J': {
                ctClass = CtClass.longType;
                break;
            }
            case 'F': {
                ctClass = CtClass.floatType;
                break;
            }
            case 'D': {
                ctClass = CtClass.doubleType;
                break;
            }
            case 'V': {
                ctClass = CtClass.voidType;
            }
        }
        return ctClass;
    }

    public static int arrayDimension(String string) {
        int n = 0;
        while (string.charAt(n) == '[') {
            ++n;
        }
        return n;
    }

    public static String toArrayComponent(String string, int n) {
        return string.substring(n);
    }

    public static int dataSize(String string) {
        return Descriptor.dataSize(string, true);
    }

    public static int paramSize(String string) {
        return -Descriptor.dataSize(string, false);
    }

    private static int dataSize(String string, boolean bl) {
        int n = 0;
        char c = string.charAt(0);
        if (c == '(') {
            int n2 = 1;
            while (true) {
                if ((c = string.charAt(n2)) == ')') {
                    c = string.charAt(n2 + 1);
                    break;
                }
                boolean bl2 = false;
                while (c == '[') {
                    bl2 = true;
                    c = string.charAt(++n2);
                }
                if (c == 'L') {
                    if ((n2 = string.indexOf(59, n2) + 1) <= 0) {
                        throw new IndexOutOfBoundsException("bad descriptor");
                    }
                } else {
                    ++n2;
                }
                if (!(bl2 || c != 'J' && c != 'D')) {
                    n -= 2;
                    continue;
                }
                --n;
            }
        }
        if (bl) {
            if (c == 'J' || c == 'D') {
                n += 2;
            } else if (c != 'V') {
                ++n;
            }
        }
        return n;
    }

    public static String toString(String string) {
        return PrettyPrinter.toString(string);
    }

    static class PrettyPrinter {
        PrettyPrinter() {
        }

        static String toString(String string) {
            StringBuilder stringBuilder = new StringBuilder();
            if (string.charAt(0) == '(') {
                int n = 1;
                stringBuilder.append('(');
                while (string.charAt(n) != ')') {
                    if (n > 1) {
                        stringBuilder.append(',');
                    }
                    n = PrettyPrinter.readType(stringBuilder, n, string);
                }
                stringBuilder.append(')');
            } else {
                PrettyPrinter.readType(stringBuilder, 0, string);
            }
            return stringBuilder.toString();
        }

        static int readType(StringBuilder stringBuilder, int n, String string) {
            char c = string.charAt(n);
            int n2 = 0;
            while (c == '[') {
                ++n2;
                c = string.charAt(++n);
            }
            if (c == 'L') {
                while ((c = string.charAt(++n)) != ';') {
                    if (c == '/') {
                        c = '.';
                    }
                    stringBuilder.append(c);
                }
            } else {
                CtClass ctClass = Descriptor.toPrimitiveClass(c);
                stringBuilder.append(ctClass.getName());
            }
            while (n2-- > 0) {
                stringBuilder.append("[]");
            }
            return n + 1;
        }
    }

    public static class Iterator {
        private String desc;
        private int index;
        private int curPos;
        private boolean param;

        public Iterator(String string) {
            this.desc = string;
            this.curPos = 0;
            this.index = 0;
            this.param = false;
        }

        public boolean hasNext() {
            return this.index < this.desc.length();
        }

        public boolean isParameter() {
            return this.param;
        }

        public char currentChar() {
            return this.desc.charAt(this.curPos);
        }

        public boolean is2byte() {
            char c = this.currentChar();
            return c == 'D' || c == 'J';
        }

        public int next() {
            int n;
            char c;
            if ((c = this.desc.charAt(n = this.index++)) == '(') {
                c = this.desc.charAt(++n);
                this.param = true;
            }
            if (c == ')') {
                ++this.index;
                c = this.desc.charAt(++n);
                this.param = false;
            }
            while (c == '[') {
                c = this.desc.charAt(++n);
            }
            if (c == 'L') {
                if ((n = this.desc.indexOf(59, n) + 1) <= 0) {
                    throw new IndexOutOfBoundsException("bad descriptor");
                }
            } else {
                ++n;
            }
            this.curPos = this.index;
            this.index = n;
            return this.curPos;
        }
    }
}

