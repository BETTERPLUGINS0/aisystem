/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package co.aikar.commands;

import co.aikar.commands.AnnotationLookups;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandManager;
import co.aikar.commands.LogLevel;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;

class Annotations<M extends CommandManager>
extends AnnotationLookups {
    public static final int NOTHING = 0;
    public static final int REPLACEMENTS = 1;
    public static final int LOWERCASE = 2;
    public static final int UPPERCASE = 4;
    public static final int NO_EMPTY = 8;
    public static final int DEFAULT_EMPTY = 16;
    private final M manager;
    private final Map<Class<? extends Annotation>, Method> valueMethods = new IdentityHashMap<Class<? extends Annotation>, Method>();
    private final Map<Class<? extends Annotation>, Void> noValueAnnotations = new IdentityHashMap<Class<? extends Annotation>, Void>();

    Annotations(M m) {
        this.manager = m;
    }

    @Override
    String getAnnotationValue(AnnotatedElement annotatedElement, Class<? extends Annotation> clazz, int n) {
        Annotation annotation = Annotations.getAnnotationRecursive(annotatedElement, clazz, new HashSet<Annotation>());
        if (annotation == null) {
            if (annotatedElement instanceof Class) {
                annotation = Annotations.getAnnotationFromParentClasses((Class)annotatedElement, clazz);
            } else if (annotatedElement instanceof Method) {
                annotation = Annotations.getAnnotationFromParentMethods((Method)annotatedElement, clazz);
            } else if (annotatedElement instanceof Parameter) {
                annotation = Annotations.getAnnotationFromParentParameters((Parameter)annotatedElement, clazz);
            }
        }
        String string = null;
        if (annotation != null) {
            Method method = this.valueMethods.get(clazz);
            if (this.noValueAnnotations.containsKey(clazz)) {
                string = "";
            } else {
                try {
                    if (method == null) {
                        method = clazz.getMethod("value", new Class[0]);
                        method.setAccessible(true);
                        this.valueMethods.put(clazz, method);
                    }
                    string = (String)method.invoke(annotation, new Object[0]);
                } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException reflectiveOperationException) {
                    if (!(reflectiveOperationException instanceof NoSuchMethodException)) {
                        ((CommandManager)this.manager).log(LogLevel.ERROR, "Error getting annotation value", reflectiveOperationException);
                    }
                    this.noValueAnnotations.put(clazz, null);
                    string = "";
                }
            }
        }
        if (string == null) {
            if (Annotations.hasOption(n, 16)) {
                string = "";
            } else {
                return null;
            }
        }
        if (Annotations.hasOption(n, 1)) {
            string = ((CommandManager)this.manager).getCommandReplacements().replace(string);
        }
        if (Annotations.hasOption(n, 2)) {
            string = string.toLowerCase(((CommandManager)this.manager).getLocales().getDefaultLocale());
        } else if (Annotations.hasOption(n, 4)) {
            string = string.toUpperCase(((CommandManager)this.manager).getLocales().getDefaultLocale());
        }
        if (string.isEmpty() && Annotations.hasOption(n, 8)) {
            string = null;
        }
        return string;
    }

    private static Annotation getAnnotationFromParentClasses(Class<?> clazz, Class<? extends Annotation> clazz2) {
        for (Class<?> clazz3 = clazz.getSuperclass(); clazz3 != null && !clazz3.equals(BaseCommand.class) && !clazz3.equals(Object.class); clazz3 = clazz3.getSuperclass()) {
            Annotation annotation = Annotations.getAnnotationRecursive(clazz3, clazz2, new HashSet<Annotation>());
            if (annotation == null) continue;
            return annotation;
        }
        return null;
    }

    private static Annotation getAnnotationFromParentMethods(Method method, Class<? extends Annotation> clazz) {
        for (Class<?> clazz2 = method.getDeclaringClass().getSuperclass(); clazz2 != null && !clazz2.equals(BaseCommand.class) && !clazz2.equals(Object.class); clazz2 = clazz2.getSuperclass()) {
            try {
                Method method2 = clazz2.getDeclaredMethod(method.getName(), method.getParameterTypes());
                Annotation annotation = Annotations.getAnnotationRecursive(method2, clazz, new HashSet<Annotation>());
                if (annotation == null) continue;
                return annotation;
            } catch (NoSuchMethodException noSuchMethodException) {
                return null;
            }
        }
        return null;
    }

    private static Annotation getAnnotationFromParentParameters(Parameter parameter3, Class<? extends Annotation> clazz) {
        for (Class<?> clazz2 = parameter3.getDeclaringExecutable().getDeclaringClass().getSuperclass(); clazz2 != null && !clazz2.equals(BaseCommand.class) && !clazz2.equals(Object.class); clazz2 = clazz2.getSuperclass()) {
            try {
                Method method = clazz2.getDeclaredMethod(parameter3.getDeclaringExecutable().getName(), parameter3.getDeclaringExecutable().getParameterTypes());
                Annotation annotation = Arrays.stream(method.getParameters()).filter(parameter2 -> parameter2.getName().equals(parameter3.getName()) && parameter2.getType().equals(parameter3.getType())).findFirst().map(parameter -> Annotations.getAnnotationRecursive(parameter, clazz, new HashSet<Annotation>())).orElse(null);
                if (annotation == null) continue;
                return annotation;
            } catch (NoSuchMethodException noSuchMethodException) {
                return null;
            }
        }
        return null;
    }

    private static Annotation getAnnotationRecursive(AnnotatedElement annotatedElement, Class<? extends Annotation> clazz, Collection<Annotation> collection) {
        if (annotatedElement.isAnnotationPresent(clazz)) {
            return annotatedElement.getAnnotation(clazz);
        }
        for (Annotation annotation : annotatedElement.getDeclaredAnnotations()) {
            if (annotation.annotationType().getPackage().getName().startsWith("java.")) continue;
            if (collection.contains(annotation)) {
                return null;
            }
            collection.add(annotation);
            Annotation annotation2 = Annotations.getAnnotationRecursive(annotation.annotationType(), clazz, collection);
            if (annotation2 == null) continue;
            return annotation2;
        }
        return null;
    }

    private static boolean hasOption(int n, int n2) {
        return (n & n2) == n2;
    }
}

