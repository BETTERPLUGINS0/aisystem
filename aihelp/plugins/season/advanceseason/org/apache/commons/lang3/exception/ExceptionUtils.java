/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.apache.commons.lang3.exception;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.function.Consumer;
import java.util.stream.Stream;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

public class ExceptionUtils {
    private static final String[] CAUSE_METHOD_NAMES = new String[]{"getCause", "getNextException", "getTargetException", "getException", "getSourceException", "getRootCause", "getCausedByException", "getNested", "getLinkedException", "getNestedException", "getLinkedCause", "getThrowable"};
    private static final int NOT_FOUND = -1;
    static final String WRAPPED_MARKER = " [wrapped] ";

    public static <T extends RuntimeException> T asRuntimeException(Throwable throwable) {
        return (T)((RuntimeException)ExceptionUtils.eraseType(throwable));
    }

    private static <R, T extends Throwable> R eraseType(Throwable throwable) {
        throw throwable;
    }

    public static void forEach(Throwable throwable, Consumer<Throwable> consumer) {
        ExceptionUtils.stream(throwable).forEach(consumer);
    }

    @Deprecated
    public static Throwable getCause(Throwable throwable) {
        return ExceptionUtils.getCause(throwable, null);
    }

    @Deprecated
    public static Throwable getCause(Throwable throwable, String[] stringArray) {
        if (throwable == null) {
            return null;
        }
        if (stringArray == null) {
            Throwable throwable2 = throwable.getCause();
            if (throwable2 != null) {
                return throwable2;
            }
            stringArray = CAUSE_METHOD_NAMES;
        }
        return Stream.of(stringArray).map(string -> ExceptionUtils.getCauseUsingMethodName(throwable, string)).filter(Objects::nonNull).findFirst().orElse(null);
    }

    private static Throwable getCauseUsingMethodName(Throwable throwable, String string) {
        Method method;
        if (string != null && (method = MethodUtils.getMethodObject(throwable.getClass(), string, new Class[0])) != null && Throwable.class.isAssignableFrom(method.getReturnType())) {
            try {
                return (Throwable)method.invoke(throwable, new Object[0]);
            } catch (ReflectiveOperationException reflectiveOperationException) {
                // empty catch block
            }
        }
        return null;
    }

    @Deprecated
    public static String[] getDefaultCauseMethodNames() {
        return ArrayUtils.clone(CAUSE_METHOD_NAMES);
    }

    public static String getMessage(Throwable throwable) {
        if (throwable == null) {
            return "";
        }
        String string = ClassUtils.getShortClassName(throwable, null);
        return string + ": " + StringUtils.defaultString(throwable.getMessage());
    }

    public static Throwable getRootCause(Throwable throwable) {
        List<Throwable> list = ExceptionUtils.getThrowableList(throwable);
        return list.isEmpty() ? null : list.get(list.size() - 1);
    }

    public static String getRootCauseMessage(Throwable throwable) {
        Throwable throwable2 = ExceptionUtils.getRootCause(throwable);
        return ExceptionUtils.getMessage(throwable2 == null ? throwable : throwable2);
    }

    public static String[] getRootCauseStackTrace(Throwable throwable) {
        return ExceptionUtils.getRootCauseStackTraceList(throwable).toArray(ArrayUtils.EMPTY_STRING_ARRAY);
    }

    public static List<String> getRootCauseStackTraceList(Throwable throwable) {
        if (throwable == null) {
            return Collections.emptyList();
        }
        Throwable[] throwableArray = ExceptionUtils.getThrowables(throwable);
        int n = throwableArray.length;
        ArrayList<String> arrayList = new ArrayList<String>();
        List<String> list = ExceptionUtils.getStackFrameList(throwableArray[n - 1]);
        int n2 = n;
        while (--n2 >= 0) {
            List<String> list2 = list;
            if (n2 != 0) {
                list = ExceptionUtils.getStackFrameList(throwableArray[n2 - 1]);
                ExceptionUtils.removeCommonFrames(list2, list);
            }
            if (n2 == n - 1) {
                arrayList.add(throwableArray[n2].toString());
            } else {
                arrayList.add(WRAPPED_MARKER + throwableArray[n2].toString());
            }
            arrayList.addAll(list2);
        }
        return arrayList;
    }

    static List<String> getStackFrameList(Throwable throwable) {
        String string = ExceptionUtils.getStackTrace(throwable);
        String string2 = System.lineSeparator();
        StringTokenizer stringTokenizer = new StringTokenizer(string, string2);
        ArrayList<String> arrayList = new ArrayList<String>();
        boolean bl = false;
        while (stringTokenizer.hasMoreTokens()) {
            String string3 = stringTokenizer.nextToken();
            int n = string3.indexOf("at");
            if (n != -1 && string3.substring(0, n).trim().isEmpty()) {
                bl = true;
                arrayList.add(string3);
                continue;
            }
            if (!bl) continue;
            break;
        }
        return arrayList;
    }

    static String[] getStackFrames(String string) {
        String string2 = System.lineSeparator();
        StringTokenizer stringTokenizer = new StringTokenizer(string, string2);
        ArrayList<String> arrayList = new ArrayList<String>();
        while (stringTokenizer.hasMoreTokens()) {
            arrayList.add(stringTokenizer.nextToken());
        }
        return arrayList.toArray(ArrayUtils.EMPTY_STRING_ARRAY);
    }

    public static String[] getStackFrames(Throwable throwable) {
        if (throwable == null) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        return ExceptionUtils.getStackFrames(ExceptionUtils.getStackTrace(throwable));
    }

    public static String getStackTrace(Throwable throwable) {
        if (throwable == null) {
            return "";
        }
        StringWriter stringWriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stringWriter, true));
        return stringWriter.toString();
    }

    public static int getThrowableCount(Throwable throwable) {
        return ExceptionUtils.getThrowableList(throwable).size();
    }

    public static List<Throwable> getThrowableList(Throwable throwable) {
        ArrayList<Throwable> arrayList = new ArrayList<Throwable>();
        while (throwable != null && !arrayList.contains(throwable)) {
            arrayList.add(throwable);
            throwable = throwable.getCause();
        }
        return arrayList;
    }

    public static Throwable[] getThrowables(Throwable throwable) {
        return ExceptionUtils.getThrowableList(throwable).toArray(ArrayUtils.EMPTY_THROWABLE_ARRAY);
    }

    public static boolean hasCause(Throwable throwable, Class<? extends Throwable> clazz) {
        if (throwable instanceof UndeclaredThrowableException) {
            throwable = throwable.getCause();
        }
        return clazz.isInstance(throwable);
    }

    private static int indexOf(Throwable throwable, Class<? extends Throwable> clazz, int n, boolean bl) {
        Throwable[] throwableArray;
        if (throwable == null || clazz == null) {
            return -1;
        }
        if (n < 0) {
            n = 0;
        }
        if (n >= (throwableArray = ExceptionUtils.getThrowables(throwable)).length) {
            return -1;
        }
        if (bl) {
            for (int i = n; i < throwableArray.length; ++i) {
                if (!clazz.isAssignableFrom(throwableArray[i].getClass())) continue;
                return i;
            }
        } else {
            for (int i = n; i < throwableArray.length; ++i) {
                if (!clazz.equals(throwableArray[i].getClass())) continue;
                return i;
            }
        }
        return -1;
    }

    public static int indexOfThrowable(Throwable throwable, Class<? extends Throwable> clazz) {
        return ExceptionUtils.indexOf(throwable, clazz, 0, false);
    }

    public static int indexOfThrowable(Throwable throwable, Class<? extends Throwable> clazz, int n) {
        return ExceptionUtils.indexOf(throwable, clazz, n, false);
    }

    public static int indexOfType(Throwable throwable, Class<? extends Throwable> clazz) {
        return ExceptionUtils.indexOf(throwable, clazz, 0, true);
    }

    public static int indexOfType(Throwable throwable, Class<? extends Throwable> clazz, int n) {
        return ExceptionUtils.indexOf(throwable, clazz, n, true);
    }

    public static boolean isChecked(Throwable throwable) {
        return throwable != null && !(throwable instanceof Error) && !(throwable instanceof RuntimeException);
    }

    public static boolean isUnchecked(Throwable throwable) {
        return throwable != null && (throwable instanceof Error || throwable instanceof RuntimeException);
    }

    public static void printRootCauseStackTrace(Throwable throwable) {
        ExceptionUtils.printRootCauseStackTrace(throwable, System.err);
    }

    public static void printRootCauseStackTrace(Throwable throwable, PrintStream printStream) {
        if (throwable == null) {
            return;
        }
        Objects.requireNonNull(printStream, "printStream");
        ExceptionUtils.getRootCauseStackTraceList(throwable).forEach(printStream::println);
        printStream.flush();
    }

    public static void printRootCauseStackTrace(Throwable throwable, PrintWriter printWriter) {
        if (throwable == null) {
            return;
        }
        Objects.requireNonNull(printWriter, "printWriter");
        ExceptionUtils.getRootCauseStackTraceList(throwable).forEach(printWriter::println);
        printWriter.flush();
    }

    public static void removeCommonFrames(List<String> list, List<String> list2) {
        Objects.requireNonNull(list, "causeFrames");
        Objects.requireNonNull(list2, "wrapperFrames");
        int n = list.size() - 1;
        for (int i = list2.size() - 1; n >= 0 && i >= 0; --n, --i) {
            String string;
            String string2 = list.get(n);
            if (!string2.equals(string = list2.get(i))) continue;
            list.remove(n);
        }
    }

    public static <T> T rethrow(Throwable throwable) {
        return (T)ExceptionUtils.eraseType(throwable);
    }

    public static Stream<Throwable> stream(Throwable throwable) {
        return ExceptionUtils.getThrowableList(throwable).stream();
    }

    private static <T extends Throwable> T throwableOf(Throwable throwable, Class<T> clazz, int n, boolean bl) {
        Throwable[] throwableArray;
        if (throwable == null || clazz == null) {
            return null;
        }
        if (n < 0) {
            n = 0;
        }
        if (n >= (throwableArray = ExceptionUtils.getThrowables(throwable)).length) {
            return null;
        }
        if (bl) {
            for (int i = n; i < throwableArray.length; ++i) {
                if (!clazz.isAssignableFrom(throwableArray[i].getClass())) continue;
                return (T)((Throwable)clazz.cast(throwableArray[i]));
            }
        } else {
            for (int i = n; i < throwableArray.length; ++i) {
                if (!clazz.equals(throwableArray[i].getClass())) continue;
                return (T)((Throwable)clazz.cast(throwableArray[i]));
            }
        }
        return null;
    }

    public static <T extends Throwable> T throwableOfThrowable(Throwable throwable, Class<T> clazz) {
        return ExceptionUtils.throwableOf(throwable, clazz, 0, false);
    }

    public static <T extends Throwable> T throwableOfThrowable(Throwable throwable, Class<T> clazz, int n) {
        return ExceptionUtils.throwableOf(throwable, clazz, n, false);
    }

    public static <T extends Throwable> T throwableOfType(Throwable throwable, Class<T> clazz) {
        return ExceptionUtils.throwableOf(throwable, clazz, 0, true);
    }

    public static <T extends Throwable> T throwableOfType(Throwable throwable, Class<T> clazz, int n) {
        return ExceptionUtils.throwableOf(throwable, clazz, n, true);
    }

    @Deprecated
    public static <T> T throwUnchecked(T t) {
        if (t instanceof RuntimeException) {
            throw (RuntimeException)t;
        }
        if (t instanceof Error) {
            throw (Error)t;
        }
        return t;
    }

    public static <T extends Throwable> T throwUnchecked(T t) {
        if (ExceptionUtils.isUnchecked(t)) {
            throw ExceptionUtils.asRuntimeException(t);
        }
        return t;
    }

    public static <R> R wrapAndThrow(Throwable throwable) {
        throw new UndeclaredThrowableException(ExceptionUtils.throwUnchecked(throwable));
    }

    @Deprecated
    public ExceptionUtils() {
    }
}

