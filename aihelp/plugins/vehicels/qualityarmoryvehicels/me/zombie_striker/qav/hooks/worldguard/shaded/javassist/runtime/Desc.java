/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.runtime;

public class Desc {
    public static boolean useContextClassLoader = false;
    private static final ThreadLocal<Boolean> USE_CONTEXT_CLASS_LOADER_LOCALLY = new ThreadLocal<Boolean>(){

        @Override
        protected Boolean initialValue() {
            return false;
        }
    };

    public static void setUseContextClassLoaderLocally() {
        USE_CONTEXT_CLASS_LOADER_LOCALLY.set(true);
    }

    public static void resetUseContextClassLoaderLocally() {
        USE_CONTEXT_CLASS_LOADER_LOCALLY.remove();
    }

    private static Class<?> getClassObject(String string) {
        if (useContextClassLoader || USE_CONTEXT_CLASS_LOADER_LOCALLY.get().booleanValue()) {
            return Class.forName(string, true, Thread.currentThread().getContextClassLoader());
        }
        return Class.forName(string);
    }

    public static Class<?> getClazz(String string) {
        try {
            return Desc.getClassObject(string);
        } catch (ClassNotFoundException classNotFoundException) {
            throw new RuntimeException("$class: internal error, could not find class '" + string + "' (Desc.useContextClassLoader: " + Boolean.toString(useContextClassLoader) + ")", classNotFoundException);
        }
    }

    public static Class<?>[] getParams(String string) {
        if (string.charAt(0) != '(') {
            throw new RuntimeException("$sig: internal error");
        }
        return Desc.getType(string, string.length(), 1, 0);
    }

    public static Class<?> getType(String string) {
        Class<?>[] classArray = Desc.getType(string, string.length(), 0, 0);
        if (classArray == null || classArray.length != 1) {
            throw new RuntimeException("$type: internal error");
        }
        return classArray[0];
    }

    private static Class<?>[] getType(String string, int n, int n2, int n3) {
        Class<Object> clazz;
        if (n2 >= n) {
            return new Class[n3];
        }
        char c = string.charAt(n2);
        switch (c) {
            case 'Z': {
                clazz = Boolean.TYPE;
                break;
            }
            case 'C': {
                clazz = Character.TYPE;
                break;
            }
            case 'B': {
                clazz = Byte.TYPE;
                break;
            }
            case 'S': {
                clazz = Short.TYPE;
                break;
            }
            case 'I': {
                clazz = Integer.TYPE;
                break;
            }
            case 'J': {
                clazz = Long.TYPE;
                break;
            }
            case 'F': {
                clazz = Float.TYPE;
                break;
            }
            case 'D': {
                clazz = Double.TYPE;
                break;
            }
            case 'V': {
                clazz = Void.TYPE;
                break;
            }
            case 'L': 
            case '[': {
                return Desc.getClassType(string, n, n2, n3);
            }
            default: {
                return new Class[n3];
            }
        }
        Class<?>[] classArray = Desc.getType(string, n, n2 + 1, n3 + 1);
        classArray[n3] = clazz;
        return classArray;
    }

    private static Class<?>[] getClassType(String string, int n, int n2, int n3) {
        int n4 = n2;
        while (string.charAt(n4) == '[') {
            ++n4;
        }
        if (string.charAt(n4) == 'L' && (n4 = string.indexOf(59, n4)) < 0) {
            throw new IndexOutOfBoundsException("bad descriptor");
        }
        String string2 = string.charAt(n2) == 'L' ? string.substring(n2 + 1, n4) : string.substring(n2, n4 + 1);
        Class<?>[] classArray = Desc.getType(string, n, n4 + 1, n3 + 1);
        try {
            classArray[n3] = Desc.getClassObject(string2.replace('/', '.'));
        } catch (ClassNotFoundException classNotFoundException) {
            throw new RuntimeException(classNotFoundException.getMessage());
        }
        return classArray;
    }
}

