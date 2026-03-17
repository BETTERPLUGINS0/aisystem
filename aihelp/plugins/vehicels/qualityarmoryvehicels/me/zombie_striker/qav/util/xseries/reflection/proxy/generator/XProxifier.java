/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.Nullable
 */
package me.zombie_striker.qav.util.xseries.reflection.proxy.generator;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Repeatable;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.stream.Collectors;
import me.zombie_striker.qav.util.xseries.reflection.XAccessFlag;
import me.zombie_striker.qav.util.xseries.reflection.jvm.objects.ReflectedObject;
import me.zombie_striker.qav.util.xseries.reflection.proxy.ReflectiveProxyObject;
import me.zombie_striker.qav.util.xseries.reflection.proxy.annotations.Final;
import me.zombie_striker.qav.util.xseries.reflection.proxy.annotations.Ignore;
import me.zombie_striker.qav.util.xseries.reflection.proxy.annotations.Private;
import me.zombie_striker.qav.util.xseries.reflection.proxy.annotations.Protected;
import me.zombie_striker.qav.util.xseries.reflection.proxy.annotations.Proxify;
import me.zombie_striker.qav.util.xseries.reflection.proxy.annotations.Static;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public final class XProxifier {
    private static final String MEMBER_SPACES = "    ";
    private final StringBuilder writer = new StringBuilder(1000);
    private final Set<String> imports = new HashSet<String>(20);
    private final String proxifiedClassName;
    private final Class<?> clazz;
    private final boolean generateIntelliJAnnotations = true;
    private final boolean generateInaccessibleMembers = true;
    private final boolean copyAnnotations = true;
    private final boolean writeComments = true;
    private final boolean writeInfoAnnotationsAsComments = true;
    private boolean disableIDEFormatting;
    private Function<Class<?>, String> remapper;

    public XProxifier(Class<?> clazz) {
        this.clazz = clazz;
        this.proxifiedClassName = clazz.getSimpleName() + "Proxified";
        this.proxify();
    }

    private static Class<?> unwrapArrayType(Class<?> clazz) {
        while (clazz.isArray()) {
            clazz = clazz.getComponentType();
        }
        return clazz;
    }

    private void imports(Class<?> clazz) {
        if (!(clazz = XProxifier.unwrapArrayType(clazz)).isPrimitive() && !clazz.getPackage().getName().equals("java.lang")) {
            this.imports.add(clazz.getName().replace('$', '.'));
        }
    }

    private void writeComments(String ... stringArray) {
        boolean bl;
        boolean bl2 = bl = stringArray.length > 1;
        if (!bl) {
            this.writer.append("// ").append(stringArray[0]).append('\n');
        }
        this.writer.append("/**\n");
        for (String string : stringArray) {
            this.writer.append(" * ");
            this.writer.append(string);
            this.writer.append('\n');
        }
        this.writer.append(" */\n");
    }

    private void writeThrownExceptions(Class<?>[] classArray) {
        if (classArray == null || classArray.length == 0) {
            return;
        }
        this.writer.append(" throws ");
        StringJoiner stringJoiner = new StringJoiner(", ");
        for (Class<?> clazz : classArray) {
            this.imports(clazz);
            stringJoiner.add(clazz.getSimpleName());
        }
        this.writer.append(stringJoiner);
    }

    private void writeMember(ReflectedObject reflectedObject) {
        this.writeMember(reflectedObject, false);
    }

    private void writeMember(ReflectedObject reflectedObject, boolean bl) {
        Class<?>[] classArray;
        Object object;
        this.writer.append(this.annotationsToString(true, true, reflectedObject));
        Set<XAccessFlag> set = reflectedObject.accessFlags();
        if (set.contains((Object)XAccessFlag.PRIVATE)) {
            this.writeAnnotation(Private.class, new String[0]);
        }
        if (set.contains((Object)XAccessFlag.PROTECTED)) {
            this.writeAnnotation(Protected.class, new String[0]);
        }
        if (set.contains((Object)XAccessFlag.STATIC)) {
            this.writeAnnotation(Static.class, new String[0]);
        }
        if (set.contains((Object)XAccessFlag.FINAL)) {
            this.writeAnnotation(Final.class, new String[0]);
        }
        switch (reflectedObject.type()) {
            case CONSTRUCTOR: {
                this.writeAnnotation(me.zombie_striker.qav.util.xseries.reflection.proxy.annotations.Constructor.class, new String[0]);
                this.writeAnnotation("NotNull", new String[0]);
                object = (Constructor)reflectedObject.unreflect();
                classArray = Arrays.stream(((Constructor)object).getParameterTypes()).map(clazz -> "_").collect(Collectors.joining(", "));
                this.writeAnnotation("Contract", "value = \"" + classArray + " -> new\"", "pure = true");
                break;
            }
            case FIELD: {
                this.writeAnnotation(me.zombie_striker.qav.util.xseries.reflection.proxy.annotations.Field.class, new String[0]);
                if (bl) {
                    this.writeAnnotation("Contract", "pure = true");
                    break;
                }
                this.writeAnnotation("Contract", "mutates = \"this\"");
            }
        }
        object = new StringJoiner(", ", "(", ")");
        classArray = null;
        this.writer.append(MEMBER_SPACES);
        switch (reflectedObject.type()) {
            case CONSTRUCTOR: {
                Constructor constructor = (Constructor)reflectedObject.unreflect();
                classArray = constructor.getExceptionTypes();
                this.writer.append(this.proxifiedClassName).append(' ').append("construct");
                this.writeParameters((StringJoiner)object, constructor.getParameters());
                break;
            }
            case FIELD: {
                Field field = (Field)reflectedObject.unreflect();
                this.imports(field.getType());
                if (bl) {
                    this.writer.append(field.getType().getSimpleName());
                } else {
                    this.writer.append("void");
                    ((StringJoiner)object).add(field.getType().getSimpleName() + " value");
                }
                this.writer.append(' ');
                this.writer.append(reflectedObject.name());
                break;
            }
            case METHOD: {
                Method method = (Method)reflectedObject.unreflect();
                classArray = method.getExceptionTypes();
                this.imports(method.getReturnType());
                this.writer.append(method.getReturnType().getSimpleName());
                this.writer.append(' ');
                this.writer.append(reflectedObject.name());
                this.writeParameters((StringJoiner)object, method.getParameters());
            }
        }
        this.writer.append(object);
        this.writeThrownExceptions(classArray);
        this.writer.append(";\n\n");
    }

    private static Object[] getArray(Object object) {
        if (object instanceof Object[]) {
            return (Object[])object;
        }
        int n = Array.getLength(object);
        Object[] objectArray = new Object[n];
        for (int i = 0; i < n; ++i) {
            objectArray[i] = Array.get(object, i);
        }
        return objectArray;
    }

    private String constantToString(Object object) {
        if (object instanceof String) {
            return '\"' + object.toString() + '\"';
        }
        if (object instanceof Class) {
            Class clazz = (Class)object;
            this.imports(clazz);
            return clazz.getSimpleName() + ".class";
        }
        if (object instanceof Annotation) {
            Annotation annotation = (Annotation)object;
            return this.annotationToString(annotation);
        }
        if (object.getClass().isEnum()) {
            this.imports(object.getClass());
            return object.getClass().getSimpleName() + '.' + ((Enum)object).name();
        }
        if (object.getClass().isArray()) {
            Object[] objectArray = XProxifier.getArray(object);
            if (objectArray.length == 0) {
                return "{}";
            }
            StringJoiner stringJoiner = objectArray.length == 1 ? new StringJoiner(", ") : new StringJoiner(", ", "{", "}");
            for (Object object2 : objectArray) {
                stringJoiner.add(this.constantToString(object2));
            }
            return stringJoiner.toString();
        }
        return object.toString();
    }

    private String annotationsToString(boolean bl, boolean bl2, AnnotatedElement annotatedElement) {
        StringJoiner stringJoiner = new StringJoiner((bl2 ? Character.valueOf('\n') : "") + (bl ? MEMBER_SPACES : ""), bl ? MEMBER_SPACES : "", bl2 ? "\n" : "").setEmptyValue("");
        for (Annotation annotation : annotatedElement.getAnnotations()) {
            Annotation[] annotationArray = XProxifier.unwrapRepeatElement(annotation);
            if (annotationArray != null) {
                for (Annotation annotation2 : annotationArray) {
                    stringJoiner.add(this.annotationToString(annotation2));
                }
                continue;
            }
            stringJoiner.add(this.annotationToString(annotation));
        }
        return stringJoiner.toString();
    }

    private static Annotation[] unwrapRepeatElement(Annotation annotation) {
        try {
            Repeatable repeatable;
            Class<?> clazz;
            Method method = annotation.annotationType().getDeclaredMethod("value", new Class[0]);
            if (method.getReturnType().isArray() && (clazz = XProxifier.unwrapArrayType(method.getReturnType())).isAnnotation() && (repeatable = clazz.getAnnotation(Repeatable.class)) != null && repeatable.value() == annotation.annotationType()) {
                try {
                    return (Annotation[])method.invoke(annotation, new Object[0]);
                } catch (IllegalAccessException | InvocationTargetException reflectiveOperationException) {
                    throw new IllegalArgumentException(reflectiveOperationException);
                }
            }
        } catch (NoSuchMethodException noSuchMethodException) {
            // empty catch block
        }
        return null;
    }

    private String annotationToString(Annotation annotation) {
        Object object;
        ArrayList<String> arrayList = new ArrayList<String>();
        boolean bl = false;
        for (Method method : annotation.annotationType().getDeclaredMethods()) {
            try {
                method.setAccessible(true);
                String string = method.getName();
                Object object2 = method.invoke(annotation, new Object[0]);
                try {
                    @Nullable Object object3 = method.getDefaultValue();
                    if (object3 != null && (object3.getClass().isArray() ? Arrays.equals(XProxifier.getArray(object3), XProxifier.getArray(object2)) : object2.equals(object3))) {
                        continue;
                    }
                } catch (TypeNotPresentException typeNotPresentException) {
                    // empty catch block
                }
                if (string.equals("value")) {
                    bl = true;
                }
                arrayList.add(string + " = " + this.constantToString(object2));
            } catch (IllegalAccessException | InvocationTargetException reflectiveOperationException) {
                throw new IllegalStateException("Failed to get annotation value " + method, reflectiveOperationException);
            }
        }
        this.imports(annotation.annotationType());
        if (arrayList.isEmpty()) {
            object = "";
        } else if (arrayList.size() == 1 && bl) {
            object = (String)arrayList.get(0);
            int n = ((String)object).indexOf(61);
            object = '(' + ((String)object).substring(n + 2) + ')';
        } else {
            object = '(' + String.join((CharSequence)", ", arrayList) + ')';
        }
        return '@' + annotation.annotationType().getSimpleName() + (String)object;
    }

    private StringJoiner writeParameters(StringJoiner stringJoiner, Parameter[] parameterArray) {
        for (Parameter parameter : parameterArray) {
            this.imports(parameter.getType());
            String string = parameter.isVarArgs() ? parameter.getType().getSimpleName() + "... " : parameter.getType().getSimpleName();
            String string2 = this.annotationsToString(false, false, parameter);
            stringJoiner.add(string2 + (string2.isEmpty() ? "" : " ") + string + ' ' + parameter.getName());
        }
        return stringJoiner;
    }

    private void writeAnnotation(Class<?> clazz, String ... stringArray) {
        this.writeAnnotation(true, clazz, stringArray);
    }

    private void writeAnnotation(boolean bl, Class<?> clazz, String ... stringArray) {
        this.imports(clazz);
        this.writeAnnotation(bl, clazz.getSimpleName(), stringArray);
    }

    private void writeAnnotation(String string, String ... stringArray) {
        this.writeAnnotation(true, string, stringArray);
    }

    private void writeAnnotation(boolean bl, String string, String ... stringArray) {
        if (bl) {
            this.writer.append(MEMBER_SPACES);
        }
        this.writer.append('@').append(string);
        if (stringArray.length != 0) {
            StringJoiner stringJoiner = new StringJoiner(", ", "(", ")");
            for (String string2 : stringArray) {
                stringJoiner.add(string2);
            }
            this.writer.append(stringJoiner);
        }
        this.writer.append('\n');
    }

    private void proxify() {
        if (this.disableIDEFormatting) {
            this.writer.append("// ").append("@formatter:").append("OFF").append('\n');
        }
        this.writeComments("This is a generated proxified class for " + this.clazz.getSimpleName() + ". However, you might", "want to review each member and correct its annotations when needed.", "<p>", "It's also recommended to use your IDE's code formatter to adjust", "imports and spaces according to your settings.", "In IntelliJ, this can be done by with Ctrl+Alt+L", "<p>", "Full Target Class Path:", this.clazz.getName());
        this.writer.append(this.annotationsToString(false, true, this.clazz));
        this.writeAnnotation(false, Proxify.class, "target = " + this.clazz.getSimpleName() + ".class");
        if (!XAccessFlag.PUBLIC.isSet(this.clazz.getModifiers())) {
            this.writeAnnotation(false, Private.class, new String[0]);
        }
        if (XAccessFlag.FINAL.isSet(this.clazz.getModifiers())) {
            this.writeAnnotation(false, Final.class, new String[0]);
            this.writeAnnotation(false, "ApiStatus.NonExtendable", new String[0]);
        }
        this.writer.append("public interface ").append(this.proxifiedClassName).append(" extends ").append(ReflectiveProxyObject.class.getSimpleName()).append(" {\n");
        Field[] fieldArray = this.clazz.getDeclaredFields();
        AccessibleObject[] accessibleObjectArray = fieldArray;
        int n = accessibleObjectArray.length;
        for (int i = 0; i < n; ++i) {
            Field field = accessibleObjectArray[i];
            if (field.isSynthetic()) continue;
            if (!XAccessFlag.FINAL.isSet(field.getModifiers())) {
                this.writeMember(ReflectedObject.of(field), false);
            }
            this.writeMember(ReflectedObject.of(field), true);
        }
        if (fieldArray.length != 0) {
            this.writer.append('\n');
        }
        for (AccessibleObject accessibleObject : accessibleObjectArray = this.clazz.getDeclaredConstructors()) {
            if (((Constructor)accessibleObject).isSynthetic()) continue;
            this.writeMember(ReflectedObject.of(accessibleObject));
        }
        if (accessibleObjectArray.length != 0) {
            this.writer.append('\n');
        }
        for (AccessibleObject accessibleObject : this.clazz.getDeclaredMethods()) {
            if (((Method)accessibleObject).getDeclaringClass() == Object.class || ((Method)accessibleObject).isSynthetic() || ((Method)accessibleObject).isBridge()) continue;
            this.writeMember(ReflectedObject.of((Method)accessibleObject));
        }
        this.writer.append('\n');
        this.writeAnnotation(Ignore.class, new String[0]);
        this.writeAnnotation("NotNull", new String[0]);
        this.writeAnnotation("ApiStatus.OverrideOnly", new String[0]);
        this.writeAnnotation("Contract", "value = \"_ -> new\"", "pure = true");
        this.writer.append(MEMBER_SPACES).append(this.proxifiedClassName).append(" bindTo(@NotNull Object instance);\n");
        this.writer.append("}\n");
        this.finalizeString();
    }

    private void finalizeString() {
        StringBuilder stringBuilder = new StringBuilder(this.writer.length() + this.imports.size() * 100);
        stringBuilder.append("import org.jetbrains.annotations.*;\n");
        ArrayList<String> arrayList = new ArrayList<String>(this.imports);
        arrayList.sort(Comparator.naturalOrder());
        for (String string : arrayList) {
            stringBuilder.append("import ").append(string).append(";\n");
        }
        stringBuilder.append('\n');
        this.writer.insert(0, stringBuilder);
        this.imports(ReflectiveProxyObject.class);
    }

    public String getString() {
        if (this.writer.length() == 0) {
            this.proxify();
        }
        return this.writer.toString();
    }

    public void writeTo(Path path) {
        if (Files.isDirectory(path, new LinkOption[0])) {
            path = path.resolve(this.proxifiedClassName + ".java");
        }
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);){
            bufferedWriter.write(this.getString());
        } catch (IOException iOException) {
            throw new IllegalStateException(iOException);
        }
    }
}

