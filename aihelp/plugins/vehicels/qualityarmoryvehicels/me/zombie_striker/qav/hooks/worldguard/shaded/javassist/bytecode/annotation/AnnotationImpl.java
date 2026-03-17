/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClass;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.NotFoundException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.AnnotationDefaultAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ClassFile;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.MethodInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.Annotation;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.MemberValue;

public class AnnotationImpl
implements InvocationHandler {
    private static final String JDK_ANNOTATION_CLASS_NAME = "java.lang.annotation.Annotation";
    private static Method JDK_ANNOTATION_TYPE_METHOD = null;
    private Annotation annotation;
    private ClassPool pool;
    private ClassLoader classLoader;
    private transient Class<?> annotationType;
    private transient int cachedHashCode = Integer.MIN_VALUE;

    public static Object make(ClassLoader classLoader, Class<?> clazz, ClassPool classPool, Annotation annotation) {
        AnnotationImpl annotationImpl = new AnnotationImpl(annotation, classPool, classLoader);
        return Proxy.newProxyInstance(classLoader, new Class[]{clazz}, annotationImpl);
    }

    private AnnotationImpl(Annotation annotation, ClassPool classPool, ClassLoader classLoader) {
        this.annotation = annotation;
        this.pool = classPool;
        this.classLoader = classLoader;
    }

    public String getTypeName() {
        return this.annotation.getTypeName();
    }

    private Class<?> getAnnotationType() {
        if (this.annotationType == null) {
            String string = this.annotation.getTypeName();
            try {
                this.annotationType = this.classLoader.loadClass(string);
            } catch (ClassNotFoundException classNotFoundException) {
                NoClassDefFoundError noClassDefFoundError = new NoClassDefFoundError("Error loading annotation class: " + string);
                noClassDefFoundError.setStackTrace(classNotFoundException.getStackTrace());
                throw noClassDefFoundError;
            }
        }
        return this.annotationType;
    }

    public Annotation getAnnotation() {
        return this.annotation;
    }

    @Override
    public Object invoke(Object object, Method method, Object[] objectArray) {
        MemberValue memberValue;
        String string = method.getName();
        if (Object.class == method.getDeclaringClass()) {
            if ("equals".equals(string)) {
                Object object2 = objectArray[0];
                return this.checkEquals(object2);
            }
            if ("toString".equals(string)) {
                return this.annotation.toString();
            }
            if ("hashCode".equals(string)) {
                return this.hashCode();
            }
        } else if ("annotationType".equals(string) && method.getParameterTypes().length == 0) {
            return this.getAnnotationType();
        }
        if ((memberValue = this.annotation.getMemberValue(string)) == null) {
            return this.getDefault(string, method);
        }
        return memberValue.getValue(this.classLoader, this.pool, method);
    }

    private Object getDefault(String string, Method method) {
        String string2 = this.annotation.getTypeName();
        if (this.pool != null) {
            try {
                AnnotationDefaultAttribute annotationDefaultAttribute;
                CtClass ctClass = this.pool.get(string2);
                ClassFile classFile = ctClass.getClassFile2();
                MethodInfo methodInfo = classFile.getMethod(string);
                if (methodInfo != null && (annotationDefaultAttribute = (AnnotationDefaultAttribute)methodInfo.getAttribute("AnnotationDefault")) != null) {
                    MemberValue memberValue = annotationDefaultAttribute.getDefaultValue();
                    return memberValue.getValue(this.classLoader, this.pool, method);
                }
            } catch (NotFoundException notFoundException) {
                throw new RuntimeException("cannot find a class file: " + string2);
            }
        }
        throw new RuntimeException("no default value: " + string2 + "." + string + "()");
    }

    public int hashCode() {
        if (this.cachedHashCode == Integer.MIN_VALUE) {
            int n = 0;
            this.getAnnotationType();
            Method[] methodArray = this.annotationType.getDeclaredMethods();
            for (int i = 0; i < methodArray.length; ++i) {
                String string = methodArray[i].getName();
                int n2 = 0;
                MemberValue memberValue = this.annotation.getMemberValue(string);
                Object object = null;
                try {
                    if (memberValue != null) {
                        object = memberValue.getValue(this.classLoader, this.pool, methodArray[i]);
                    }
                    if (object == null) {
                        object = this.getDefault(string, methodArray[i]);
                    }
                } catch (RuntimeException runtimeException) {
                    throw runtimeException;
                } catch (Exception exception) {
                    throw new RuntimeException("Error retrieving value " + string + " for annotation " + this.annotation.getTypeName(), exception);
                }
                if (object != null) {
                    n2 = object.getClass().isArray() ? AnnotationImpl.arrayHashCode(object) : object.hashCode();
                }
                n += 127 * string.hashCode() ^ n2;
            }
            this.cachedHashCode = n;
        }
        return this.cachedHashCode;
    }

    private boolean checkEquals(Object object) {
        Object object2;
        if (object == null) {
            return false;
        }
        if (object instanceof Proxy && (object2 = Proxy.getInvocationHandler(object)) instanceof AnnotationImpl) {
            AnnotationImpl annotationImpl = (AnnotationImpl)object2;
            return this.annotation.equals(annotationImpl.annotation);
        }
        object2 = (Class)JDK_ANNOTATION_TYPE_METHOD.invoke(object, new Object[0]);
        if (!this.getAnnotationType().equals(object2)) {
            return false;
        }
        Method[] methodArray = this.annotationType.getDeclaredMethods();
        for (int i = 0; i < methodArray.length; ++i) {
            String string = methodArray[i].getName();
            MemberValue memberValue = this.annotation.getMemberValue(string);
            Object object3 = null;
            Object object4 = null;
            try {
                if (memberValue != null) {
                    object3 = memberValue.getValue(this.classLoader, this.pool, methodArray[i]);
                }
                if (object3 == null) {
                    object3 = this.getDefault(string, methodArray[i]);
                }
                object4 = methodArray[i].invoke(object, new Object[0]);
            } catch (RuntimeException runtimeException) {
                throw runtimeException;
            } catch (Exception exception) {
                throw new RuntimeException("Error retrieving value " + string + " for annotation " + this.annotation.getTypeName(), exception);
            }
            if (object3 == null && object4 != null) {
                return false;
            }
            if (object3 == null || object3.equals(object4)) continue;
            return false;
        }
        return true;
    }

    private static int arrayHashCode(Object object) {
        if (object == null) {
            return 0;
        }
        int n = 1;
        Object[] objectArray = (Object[])object;
        for (int i = 0; i < objectArray.length; ++i) {
            int n2 = 0;
            if (objectArray[i] != null) {
                n2 = objectArray[i].hashCode();
            }
            n = 31 * n + n2;
        }
        return n;
    }

    static {
        try {
            Class<?> clazz = Class.forName(JDK_ANNOTATION_CLASS_NAME);
            JDK_ANNOTATION_TYPE_METHOD = clazz.getMethod("annotationType", null);
        } catch (Exception exception) {
            // empty catch block
        }
    }
}

