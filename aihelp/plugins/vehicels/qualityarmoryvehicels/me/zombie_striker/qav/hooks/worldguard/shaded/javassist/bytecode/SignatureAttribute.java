/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode;

import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClass;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.AttributeInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.BadBytecode;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ByteArray;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Descriptor;

public class SignatureAttribute
extends AttributeInfo {
    public static final String tag = "Signature";

    SignatureAttribute(ConstPool constPool, int n, DataInputStream dataInputStream) {
        super(constPool, n, dataInputStream);
    }

    public SignatureAttribute(ConstPool constPool, String string) {
        super(constPool, tag);
        int n = constPool.addUtf8Info(string);
        byte[] byArray = new byte[]{(byte)(n >>> 8), (byte)n};
        this.set(byArray);
    }

    public String getSignature() {
        return this.getConstPool().getUtf8Info(ByteArray.readU16bit(this.get(), 0));
    }

    public void setSignature(String string) {
        int n = this.getConstPool().addUtf8Info(string);
        ByteArray.write16bit(n, this.info, 0);
    }

    @Override
    public AttributeInfo copy(ConstPool constPool, Map<String, String> map) {
        return new SignatureAttribute(constPool, this.getSignature());
    }

    @Override
    void renameClass(String string, String string2) {
        String string3 = SignatureAttribute.renameClass(this.getSignature(), string, string2);
        this.setSignature(string3);
    }

    @Override
    void renameClass(Map<String, String> map) {
        String string = SignatureAttribute.renameClass(this.getSignature(), map);
        this.setSignature(string);
    }

    static String renameClass(String string, String string2, String string3) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put(string2, string3);
        return SignatureAttribute.renameClass(string, hashMap);
    }

    static String renameClass(String string, Map<String, String> map) {
        ArrayList<StringBuilder> arrayList;
        ArrayList<StringBuilder> arrayList2;
        int n;
        if (map == null || map.isEmpty()) {
            return string;
        }
        StringBuilder stringBuilder = new StringBuilder();
        int n2 = 0;
        int n3 = 0;
        while ((n = string.indexOf(76, n3)) >= 0 && (n3 = SignatureAttribute.parseClassName(arrayList2 = new ArrayList<StringBuilder>(), arrayList = new ArrayList<StringBuilder>(), string, n) + 1) >= 0) {
            String string2 = String.join((CharSequence)"$", arrayList2.toArray(new StringBuilder[0]));
            String string3 = map.get(string2);
            if (string3 != null) {
                if (!SignatureAttribute.makeNewClassName(string, map, string2, string3, stringBuilder, n2, n, arrayList2, arrayList)) continue;
                n2 = n3;
                continue;
            }
            if (!SignatureAttribute.replaceTypeArguments(string, map, stringBuilder, n2, n, arrayList2, arrayList)) continue;
            n2 = n3;
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

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static int parseClassName(ArrayList<StringBuilder> arrayList, ArrayList<StringBuilder> arrayList2, String string, int n) {
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder stringBuilder2 = new StringBuilder();
        int n2 = n;
        try {
            char c;
            while ((c = string.charAt(++n2)) != ';') {
                int n3;
                if (c == '<') {
                    stringBuilder2.append(c);
                    n3 = 1;
                } else {
                    if (c == '.') {
                        arrayList.add(stringBuilder);
                        arrayList2.add(stringBuilder2);
                        stringBuilder = new StringBuilder();
                        stringBuilder2 = new StringBuilder();
                        continue;
                    }
                    stringBuilder.append(c);
                    continue;
                }
                while (n3 > 0) {
                    c = string.charAt(++n2);
                    stringBuilder2.append(c);
                    if (c == '<') {
                        ++n3;
                        continue;
                    }
                    if (c != '>') continue;
                    --n3;
                }
            }
        } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            return -2;
        }
        arrayList.add(stringBuilder);
        arrayList2.add(stringBuilder2);
        return n2;
    }

    private static boolean makeNewClassName(String string, Map<String, String> map, String string2, String string3, StringBuilder stringBuilder, int n2, int n3, ArrayList<StringBuilder> arrayList, ArrayList<StringBuilder> arrayList2) {
        String[] stringArray;
        String[] stringArray2 = string2.split("\\$");
        if (stringArray2.length == (stringArray = string3.split("\\$")).length) {
            String[] stringArray3 = new String[arrayList.size()];
            int n4 = 0;
            for (int i = 0; i < arrayList.size(); ++i) {
                int n5 = (int)arrayList.get(i).chars().filter(n -> n == 36).count() + 1;
                String string4 = String.join((CharSequence)"$", Arrays.copyOfRange(stringArray, n4, n4 + n5));
                n4 += n5;
                stringArray3[i] = string4;
            }
            stringBuilder.append(string.substring(n2, n3));
            stringBuilder.append('L');
            for (n4 = 0; n4 < stringArray3.length; ++n4) {
                if (n4 > 0) {
                    stringBuilder.append('.');
                }
                stringBuilder.append(stringArray3[n4]);
                StringBuilder stringBuilder2 = arrayList2.get(n4);
                String string5 = stringBuilder2.length() > 0 ? "<" + SignatureAttribute.renameClass(stringBuilder2.substring(1, stringBuilder2.length() - 1), map) + ">" : stringBuilder2.toString();
                stringBuilder.append(string5);
            }
            stringBuilder.append(';');
            return true;
        }
        return false;
    }

    private static boolean replaceTypeArguments(String string, Map<String, String> map, StringBuilder stringBuilder, int n, int n2, ArrayList<StringBuilder> arrayList, ArrayList<StringBuilder> arrayList2) {
        int n3;
        ArrayList<String> arrayList3 = new ArrayList<String>();
        boolean bl = false;
        for (n3 = 0; n3 < arrayList2.size(); ++n3) {
            String string2;
            StringBuilder stringBuilder2 = arrayList2.get(n3);
            if (stringBuilder2.length() > 0) {
                string2 = "<" + SignatureAttribute.renameClass(stringBuilder2.substring(1, stringBuilder2.length() - 1), map) + ">";
                bl = bl || !stringBuilder2.toString().equals(string2);
            } else {
                string2 = stringBuilder2.toString();
            }
            arrayList3.add(string2);
        }
        if (bl) {
            stringBuilder.append(string.substring(n, n2));
            stringBuilder.append('L');
            for (n3 = 0; n3 < arrayList2.size(); ++n3) {
                if (n3 > 0) {
                    stringBuilder.append('.');
                }
                stringBuilder.append((CharSequence)arrayList.get(n3));
                stringBuilder.append((String)arrayList3.get(n3));
            }
            stringBuilder.append(';');
            return true;
        }
        return false;
    }

    private static boolean isNamePart(int n) {
        return n != 59 && n != 60;
    }

    public static ClassSignature toClassSignature(String string) {
        try {
            return SignatureAttribute.parseSig(string);
        } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            throw SignatureAttribute.error(string);
        }
    }

    public static MethodSignature toMethodSignature(String string) {
        try {
            return SignatureAttribute.parseMethodSig(string);
        } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            throw SignatureAttribute.error(string);
        }
    }

    public static ObjectType toFieldSignature(String string) {
        try {
            return SignatureAttribute.parseObjectType(string, new Cursor(), false);
        } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            throw SignatureAttribute.error(string);
        }
    }

    public static Type toTypeSignature(String string) {
        try {
            return SignatureAttribute.parseType(string, new Cursor());
        } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            throw SignatureAttribute.error(string);
        }
    }

    private static ClassSignature parseSig(String string) {
        Cursor cursor = new Cursor();
        TypeParameter[] typeParameterArray = SignatureAttribute.parseTypeParams(string, cursor);
        ClassType classType = SignatureAttribute.parseClassType(string, cursor);
        int n = string.length();
        ArrayList<ClassType> arrayList = new ArrayList<ClassType>();
        while (cursor.position < n && string.charAt(cursor.position) == 'L') {
            arrayList.add(SignatureAttribute.parseClassType(string, cursor));
        }
        ClassType[] classTypeArray = arrayList.toArray(new ClassType[arrayList.size()]);
        return new ClassSignature(typeParameterArray, classType, classTypeArray);
    }

    private static MethodSignature parseMethodSig(String string) {
        Object object;
        Type type;
        Cursor cursor = new Cursor();
        TypeParameter[] typeParameterArray = SignatureAttribute.parseTypeParams(string, cursor);
        if (string.charAt(cursor.position++) != '(') {
            throw SignatureAttribute.error(string);
        }
        ArrayList<Type> arrayList = new ArrayList<Type>();
        while (string.charAt(cursor.position) != ')') {
            type = SignatureAttribute.parseType(string, cursor);
            arrayList.add(type);
        }
        ++cursor.position;
        type = SignatureAttribute.parseType(string, cursor);
        int n = string.length();
        ArrayList<Type[]> arrayList2 = new ArrayList<Type[]>();
        while (cursor.position < n && string.charAt(cursor.position) == '^') {
            ++cursor.position;
            object = SignatureAttribute.parseObjectType(string, cursor, false);
            if (object instanceof ArrayType) {
                throw SignatureAttribute.error(string);
            }
            arrayList2.add((Type[])object);
        }
        object = arrayList.toArray(new Type[arrayList.size()]);
        ObjectType[] objectTypeArray = arrayList2.toArray(new ObjectType[arrayList2.size()]);
        return new MethodSignature(typeParameterArray, (Type[])object, type, objectTypeArray);
    }

    private static TypeParameter[] parseTypeParams(String string, Cursor cursor) {
        ArrayList<Object> arrayList = new ArrayList<Object>();
        if (string.charAt(cursor.position) == '<') {
            ++cursor.position;
            while (string.charAt(cursor.position) != '>') {
                Object object;
                int n = cursor.position;
                int n2 = cursor.indexOf(string, 58);
                ObjectType objectType = SignatureAttribute.parseObjectType(string, cursor, true);
                ArrayList<ObjectType> arrayList2 = new ArrayList<ObjectType>();
                while (string.charAt(cursor.position) == ':') {
                    ++cursor.position;
                    object = SignatureAttribute.parseObjectType(string, cursor, false);
                    arrayList2.add((ObjectType)object);
                }
                object = new TypeParameter(string, n, n2, objectType, arrayList2.toArray(new ObjectType[arrayList2.size()]));
                arrayList.add(object);
            }
            ++cursor.position;
        }
        return arrayList.toArray(new TypeParameter[arrayList.size()]);
    }

    private static ObjectType parseObjectType(String string, Cursor cursor, boolean bl) {
        int n = cursor.position;
        switch (string.charAt(n)) {
            case 'L': {
                return SignatureAttribute.parseClassType2(string, cursor, null);
            }
            case 'T': {
                int n2 = cursor.indexOf(string, 59);
                return new TypeVariable(string, n + 1, n2);
            }
            case '[': {
                return SignatureAttribute.parseArray(string, cursor);
            }
        }
        if (bl) {
            return null;
        }
        throw SignatureAttribute.error(string);
    }

    private static ClassType parseClassType(String string, Cursor cursor) {
        if (string.charAt(cursor.position) == 'L') {
            return SignatureAttribute.parseClassType2(string, cursor, null);
        }
        throw SignatureAttribute.error(string);
    }

    private static ClassType parseClassType2(String string, Cursor cursor, ClassType classType) {
        TypeArgument[] typeArgumentArray;
        char c;
        int n = ++cursor.position;
        while ((c = string.charAt(cursor.position++)) != '$' && c != '<' && c != ';') {
        }
        int n2 = cursor.position - 1;
        if (c == '<') {
            typeArgumentArray = SignatureAttribute.parseTypeArgs(string, cursor);
            c = string.charAt(cursor.position++);
        } else {
            typeArgumentArray = null;
        }
        ClassType classType2 = ClassType.make(string, n, n2, typeArgumentArray, classType);
        if (c == '$' || c == '.') {
            --cursor.position;
            return SignatureAttribute.parseClassType2(string, cursor, classType2);
        }
        return classType2;
    }

    private static TypeArgument[] parseTypeArgs(String string, Cursor cursor) {
        char c;
        ArrayList<TypeArgument> arrayList = new ArrayList<TypeArgument>();
        while ((c = string.charAt(cursor.position++)) != '>') {
            TypeArgument typeArgument;
            if (c == '*') {
                typeArgument = new TypeArgument(null, '*');
            } else {
                if (c != '+' && c != '-') {
                    c = ' ';
                    --cursor.position;
                }
                typeArgument = new TypeArgument(SignatureAttribute.parseObjectType(string, cursor, false), c);
            }
            arrayList.add(typeArgument);
        }
        return arrayList.toArray(new TypeArgument[arrayList.size()]);
    }

    private static ObjectType parseArray(String string, Cursor cursor) {
        int n = 1;
        while (string.charAt(++cursor.position) == '[') {
            ++n;
        }
        return new ArrayType(n, SignatureAttribute.parseType(string, cursor));
    }

    private static Type parseType(String string, Cursor cursor) {
        Type type = SignatureAttribute.parseObjectType(string, cursor, true);
        if (type == null) {
            type = new BaseType(string.charAt(cursor.position++));
        }
        return type;
    }

    private static BadBytecode error(String string) {
        return new BadBytecode("bad signature: " + string);
    }

    public static class ClassSignature {
        TypeParameter[] params;
        ClassType superClass;
        ClassType[] interfaces;

        public ClassSignature(TypeParameter[] typeParameterArray, ClassType classType, ClassType[] classTypeArray) {
            this.params = typeParameterArray == null ? new TypeParameter[]{} : typeParameterArray;
            this.superClass = classType == null ? ClassType.OBJECT : classType;
            this.interfaces = classTypeArray == null ? new ClassType[]{} : classTypeArray;
        }

        public ClassSignature(TypeParameter[] typeParameterArray) {
            this(typeParameterArray, null, null);
        }

        public TypeParameter[] getParameters() {
            return this.params;
        }

        public ClassType getSuperClass() {
            return this.superClass;
        }

        public ClassType[] getInterfaces() {
            return this.interfaces;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            TypeParameter.toString(stringBuilder, this.params);
            stringBuilder.append(" extends ").append(this.superClass);
            if (this.interfaces.length > 0) {
                stringBuilder.append(" implements ");
                Type.toString(stringBuilder, this.interfaces);
            }
            return stringBuilder.toString();
        }

        public String encode() {
            int n;
            StringBuilder stringBuilder = new StringBuilder();
            if (this.params.length > 0) {
                stringBuilder.append('<');
                for (n = 0; n < this.params.length; ++n) {
                    this.params[n].encode(stringBuilder);
                }
                stringBuilder.append('>');
            }
            this.superClass.encode(stringBuilder);
            for (n = 0; n < this.interfaces.length; ++n) {
                this.interfaces[n].encode(stringBuilder);
            }
            return stringBuilder.toString();
        }
    }

    public static class MethodSignature {
        TypeParameter[] typeParams;
        Type[] params;
        Type retType;
        ObjectType[] exceptions;

        public MethodSignature(TypeParameter[] typeParameterArray, Type[] typeArray, Type type, ObjectType[] objectTypeArray) {
            this.typeParams = typeParameterArray == null ? new TypeParameter[]{} : typeParameterArray;
            this.params = typeArray == null ? new Type[]{} : typeArray;
            this.retType = type == null ? new BaseType("void") : type;
            this.exceptions = objectTypeArray == null ? new ObjectType[]{} : objectTypeArray;
        }

        public TypeParameter[] getTypeParameters() {
            return this.typeParams;
        }

        public Type[] getParameterTypes() {
            return this.params;
        }

        public Type getReturnType() {
            return this.retType;
        }

        public ObjectType[] getExceptionTypes() {
            return this.exceptions;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            TypeParameter.toString(stringBuilder, this.typeParams);
            stringBuilder.append(" (");
            Type.toString(stringBuilder, this.params);
            stringBuilder.append(") ");
            stringBuilder.append(this.retType);
            if (this.exceptions.length > 0) {
                stringBuilder.append(" throws ");
                Type.toString(stringBuilder, this.exceptions);
            }
            return stringBuilder.toString();
        }

        public String encode() {
            int n;
            StringBuilder stringBuilder = new StringBuilder();
            if (this.typeParams.length > 0) {
                stringBuilder.append('<');
                for (n = 0; n < this.typeParams.length; ++n) {
                    this.typeParams[n].encode(stringBuilder);
                }
                stringBuilder.append('>');
            }
            stringBuilder.append('(');
            for (n = 0; n < this.params.length; ++n) {
                this.params[n].encode(stringBuilder);
            }
            stringBuilder.append(')');
            this.retType.encode(stringBuilder);
            if (this.exceptions.length > 0) {
                for (n = 0; n < this.exceptions.length; ++n) {
                    stringBuilder.append('^');
                    this.exceptions[n].encode(stringBuilder);
                }
            }
            return stringBuilder.toString();
        }
    }

    private static class Cursor {
        int position = 0;

        private Cursor() {
        }

        int indexOf(String string, int n) {
            int n2 = string.indexOf(n, this.position);
            if (n2 < 0) {
                throw SignatureAttribute.error(string);
            }
            this.position = n2 + 1;
            return n2;
        }
    }

    public static abstract class ObjectType
    extends Type {
        public String encode() {
            StringBuilder stringBuilder = new StringBuilder();
            this.encode(stringBuilder);
            return stringBuilder.toString();
        }
    }

    public static abstract class Type {
        abstract void encode(StringBuilder var1);

        static void toString(StringBuilder stringBuilder, Type[] typeArray) {
            for (int i = 0; i < typeArray.length; ++i) {
                if (i > 0) {
                    stringBuilder.append(", ");
                }
                stringBuilder.append(typeArray[i]);
            }
        }

        public String jvmTypeName() {
            return this.toString();
        }
    }

    public static class TypeParameter {
        String name;
        ObjectType superClass;
        ObjectType[] superInterfaces;

        TypeParameter(String string, int n, int n2, ObjectType objectType, ObjectType[] objectTypeArray) {
            this.name = string.substring(n, n2);
            this.superClass = objectType;
            this.superInterfaces = objectTypeArray;
        }

        public TypeParameter(String string, ObjectType objectType, ObjectType[] objectTypeArray) {
            this.name = string;
            this.superClass = objectType;
            this.superInterfaces = objectTypeArray == null ? new ObjectType[0] : objectTypeArray;
        }

        public TypeParameter(String string) {
            this(string, null, null);
        }

        public String getName() {
            return this.name;
        }

        public ObjectType getClassBound() {
            return this.superClass;
        }

        public ObjectType[] getInterfaceBound() {
            return this.superInterfaces;
        }

        public String toString() {
            int n;
            StringBuilder stringBuilder = new StringBuilder(this.getName());
            if (this.superClass != null) {
                stringBuilder.append(" extends ").append(this.superClass.toString());
            }
            if ((n = this.superInterfaces.length) > 0) {
                for (int i = 0; i < n; ++i) {
                    if (i > 0 || this.superClass != null) {
                        stringBuilder.append(" & ");
                    } else {
                        stringBuilder.append(" extends ");
                    }
                    stringBuilder.append(this.superInterfaces[i].toString());
                }
            }
            return stringBuilder.toString();
        }

        static void toString(StringBuilder stringBuilder, TypeParameter[] typeParameterArray) {
            stringBuilder.append('<');
            for (int i = 0; i < typeParameterArray.length; ++i) {
                if (i > 0) {
                    stringBuilder.append(", ");
                }
                stringBuilder.append(typeParameterArray[i]);
            }
            stringBuilder.append('>');
        }

        void encode(StringBuilder stringBuilder) {
            stringBuilder.append(this.name);
            if (this.superClass == null) {
                stringBuilder.append(":Ljava/lang/Object;");
            } else {
                stringBuilder.append(':');
                this.superClass.encode(stringBuilder);
            }
            for (int i = 0; i < this.superInterfaces.length; ++i) {
                stringBuilder.append(':');
                this.superInterfaces[i].encode(stringBuilder);
            }
        }
    }

    public static class ClassType
    extends ObjectType {
        String name;
        TypeArgument[] arguments;
        public static ClassType OBJECT = new ClassType("java.lang.Object", null);

        static ClassType make(String string, int n, int n2, TypeArgument[] typeArgumentArray, ClassType classType) {
            if (classType == null) {
                return new ClassType(string, n, n2, typeArgumentArray);
            }
            return new NestedClassType(string, n, n2, typeArgumentArray, classType);
        }

        ClassType(String string, int n, int n2, TypeArgument[] typeArgumentArray) {
            this.name = string.substring(n, n2).replace('/', '.');
            this.arguments = typeArgumentArray;
        }

        public ClassType(String string, TypeArgument[] typeArgumentArray) {
            this.name = string;
            this.arguments = typeArgumentArray;
        }

        public ClassType(String string) {
            this(string, null);
        }

        public String getName() {
            return this.name;
        }

        public TypeArgument[] getTypeArguments() {
            return this.arguments;
        }

        public ClassType getDeclaringClass() {
            return null;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            ClassType classType = this.getDeclaringClass();
            if (classType != null) {
                stringBuilder.append(classType.toString()).append('.');
            }
            return this.toString2(stringBuilder);
        }

        private String toString2(StringBuilder stringBuilder) {
            stringBuilder.append(this.name);
            if (this.arguments != null) {
                stringBuilder.append('<');
                int n = this.arguments.length;
                for (int i = 0; i < n; ++i) {
                    if (i > 0) {
                        stringBuilder.append(", ");
                    }
                    stringBuilder.append(this.arguments[i].toString());
                }
                stringBuilder.append('>');
            }
            return stringBuilder.toString();
        }

        @Override
        public String jvmTypeName() {
            StringBuilder stringBuilder = new StringBuilder();
            ClassType classType = this.getDeclaringClass();
            if (classType != null) {
                stringBuilder.append(classType.jvmTypeName()).append('$');
            }
            return this.toString2(stringBuilder);
        }

        @Override
        void encode(StringBuilder stringBuilder) {
            stringBuilder.append('L');
            this.encode2(stringBuilder);
            stringBuilder.append(';');
        }

        void encode2(StringBuilder stringBuilder) {
            ClassType classType = this.getDeclaringClass();
            if (classType != null) {
                classType.encode2(stringBuilder);
                stringBuilder.append('$');
            }
            stringBuilder.append(this.name.replace('.', '/'));
            if (this.arguments != null) {
                TypeArgument.encode(stringBuilder, this.arguments);
            }
        }
    }

    public static class ArrayType
    extends ObjectType {
        int dim;
        Type componentType;

        public ArrayType(int n, Type type) {
            this.dim = n;
            this.componentType = type;
        }

        public int getDimension() {
            return this.dim;
        }

        public Type getComponentType() {
            return this.componentType;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder(this.componentType.toString());
            for (int i = 0; i < this.dim; ++i) {
                stringBuilder.append("[]");
            }
            return stringBuilder.toString();
        }

        @Override
        void encode(StringBuilder stringBuilder) {
            for (int i = 0; i < this.dim; ++i) {
                stringBuilder.append('[');
            }
            this.componentType.encode(stringBuilder);
        }
    }

    public static class TypeVariable
    extends ObjectType {
        String name;

        TypeVariable(String string, int n, int n2) {
            this.name = string.substring(n, n2);
        }

        public TypeVariable(String string) {
            this.name = string;
        }

        public String getName() {
            return this.name;
        }

        public String toString() {
            return this.name;
        }

        @Override
        void encode(StringBuilder stringBuilder) {
            stringBuilder.append('T').append(this.name).append(';');
        }
    }

    public static class TypeArgument {
        ObjectType arg;
        char wildcard;

        TypeArgument(ObjectType objectType, char c) {
            this.arg = objectType;
            this.wildcard = c;
        }

        public TypeArgument(ObjectType objectType) {
            this(objectType, ' ');
        }

        public TypeArgument() {
            this(null, '*');
        }

        public static TypeArgument subclassOf(ObjectType objectType) {
            return new TypeArgument(objectType, '+');
        }

        public static TypeArgument superOf(ObjectType objectType) {
            return new TypeArgument(objectType, '-');
        }

        public char getKind() {
            return this.wildcard;
        }

        public boolean isWildcard() {
            return this.wildcard != ' ';
        }

        public ObjectType getType() {
            return this.arg;
        }

        public String toString() {
            if (this.wildcard == '*') {
                return "?";
            }
            String string = this.arg.toString();
            if (this.wildcard == ' ') {
                return string;
            }
            if (this.wildcard == '+') {
                return "? extends " + string;
            }
            return "? super " + string;
        }

        static void encode(StringBuilder stringBuilder, TypeArgument[] typeArgumentArray) {
            stringBuilder.append('<');
            for (int i = 0; i < typeArgumentArray.length; ++i) {
                TypeArgument typeArgument = typeArgumentArray[i];
                if (typeArgument.isWildcard()) {
                    stringBuilder.append(typeArgument.wildcard);
                }
                if (typeArgument.getType() == null) continue;
                typeArgument.getType().encode(stringBuilder);
            }
            stringBuilder.append('>');
        }
    }

    public static class BaseType
    extends Type {
        char descriptor;

        BaseType(char c) {
            this.descriptor = c;
        }

        public BaseType(String string) {
            this(Descriptor.of(string).charAt(0));
        }

        public char getDescriptor() {
            return this.descriptor;
        }

        public CtClass getCtlass() {
            return Descriptor.toPrimitiveClass(this.descriptor);
        }

        public String toString() {
            return Descriptor.toClassName(Character.toString(this.descriptor));
        }

        @Override
        void encode(StringBuilder stringBuilder) {
            stringBuilder.append(this.descriptor);
        }
    }

    public static class NestedClassType
    extends ClassType {
        ClassType parent;

        NestedClassType(String string, int n, int n2, TypeArgument[] typeArgumentArray, ClassType classType) {
            super(string, n, n2, typeArgumentArray);
            this.parent = classType;
        }

        public NestedClassType(ClassType classType, String string, TypeArgument[] typeArgumentArray) {
            super(string, typeArgumentArray);
            this.parent = classType;
        }

        @Override
        public ClassType getDeclaringClass() {
            return this.parent;
        }
    }
}

