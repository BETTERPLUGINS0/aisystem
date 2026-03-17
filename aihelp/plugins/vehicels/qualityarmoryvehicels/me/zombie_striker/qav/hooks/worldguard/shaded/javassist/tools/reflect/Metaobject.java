/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.tools.reflect;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.tools.reflect.CannotInvokeException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.tools.reflect.ClassMetaobject;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.tools.reflect.Metalevel;

public class Metaobject
implements Serializable {
    private static final long serialVersionUID = 1L;
    protected ClassMetaobject classmetaobject;
    protected Metalevel baseobject;
    protected Method[] methods;

    public Metaobject(Object object, Object[] objectArray) {
        this.baseobject = (Metalevel)object;
        this.classmetaobject = this.baseobject._getClass();
        this.methods = this.classmetaobject.getReflectiveMethods();
    }

    protected Metaobject() {
        this.baseobject = null;
        this.classmetaobject = null;
        this.methods = null;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) {
        objectOutputStream.writeObject(this.baseobject);
    }

    private void readObject(ObjectInputStream objectInputStream) {
        this.baseobject = (Metalevel)objectInputStream.readObject();
        this.classmetaobject = this.baseobject._getClass();
        this.methods = this.classmetaobject.getReflectiveMethods();
    }

    public final ClassMetaobject getClassMetaobject() {
        return this.classmetaobject;
    }

    public final Object getObject() {
        return this.baseobject;
    }

    public final void setObject(Object object) {
        this.baseobject = (Metalevel)object;
        this.classmetaobject = this.baseobject._getClass();
        this.methods = this.classmetaobject.getReflectiveMethods();
        this.baseobject._setMetaobject(this);
    }

    public final String getMethodName(int n) {
        char c;
        String string = this.methods[n].getName();
        int n2 = 3;
        while ((c = string.charAt(n2++)) >= '0' && '9' >= c) {
        }
        return string.substring(n2);
    }

    public final Class<?>[] getParameterTypes(int n) {
        return this.methods[n].getParameterTypes();
    }

    public final Class<?> getReturnType(int n) {
        return this.methods[n].getReturnType();
    }

    public Object trapFieldRead(String string) {
        Class<?> clazz = this.getClassMetaobject().getJavaClass();
        try {
            return clazz.getField(string).get(this.getObject());
        } catch (NoSuchFieldException noSuchFieldException) {
            throw new RuntimeException(noSuchFieldException.toString());
        } catch (IllegalAccessException illegalAccessException) {
            throw new RuntimeException(illegalAccessException.toString());
        }
    }

    public void trapFieldWrite(String string, Object object) {
        Class<?> clazz = this.getClassMetaobject().getJavaClass();
        try {
            clazz.getField(string).set(this.getObject(), object);
        } catch (NoSuchFieldException noSuchFieldException) {
            throw new RuntimeException(noSuchFieldException.toString());
        } catch (IllegalAccessException illegalAccessException) {
            throw new RuntimeException(illegalAccessException.toString());
        }
    }

    public Object trapMethodcall(int n, Object[] objectArray) {
        try {
            return this.methods[n].invoke(this.getObject(), objectArray);
        } catch (InvocationTargetException invocationTargetException) {
            throw invocationTargetException.getTargetException();
        } catch (IllegalAccessException illegalAccessException) {
            throw new CannotInvokeException(illegalAccessException);
        }
    }
}

