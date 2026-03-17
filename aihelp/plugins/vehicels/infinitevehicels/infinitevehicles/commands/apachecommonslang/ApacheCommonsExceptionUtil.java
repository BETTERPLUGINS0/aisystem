package me.PM2.infinitevehicles.commands.apachecommonslang;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class ApacheCommonsExceptionUtil {
   private static final String LINE_SEPARATOR = System.getProperty("line.separator");
   static final String WRAPPED_MARKER = " [wrapped] ";
   private static String[] CAUSE_METHOD_NAMES = new String[]{"getCause", "getNextException", "getTargetException", "getException", "getSourceException", "getRootCause", "getCausedByException", "getNested", "getLinkedException", "getNestedException", "getLinkedCause", "getThrowable"};
   private static final Method THROWABLE_CAUSE_METHOD;
   private static final Method THROWABLE_INITCAUSE_METHOD;

   public static void addCauseMethodName(String methodName) {
      if (var0 != null && !var0.isEmpty() && !isCauseMethodName(var0)) {
         ArrayList var1 = getCauseMethodNameList();
         if (var1.add(var0)) {
            CAUSE_METHOD_NAMES = toArray(var1);
         }
      }

   }

   public static void removeCauseMethodName(String methodName) {
      if (var0 != null && !var0.isEmpty()) {
         ArrayList var1 = getCauseMethodNameList();
         if (var1.remove(var0)) {
            CAUSE_METHOD_NAMES = toArray(var1);
         }
      }

   }

   public static boolean setCause(Throwable target, Throwable cause) {
      if (var0 == null) {
         throw new IllegalArgumentException("target");
      } else {
         Object[] var2 = new Object[]{var1};
         boolean var3 = false;
         if (THROWABLE_INITCAUSE_METHOD != null) {
            try {
               THROWABLE_INITCAUSE_METHOD.invoke(var0, var2);
               var3 = true;
            } catch (IllegalAccessException var8) {
            } catch (InvocationTargetException var9) {
            }
         }

         try {
            Method var4 = var0.getClass().getMethod("setCause", Throwable.class);
            var4.invoke(var0, var2);
            var3 = true;
         } catch (NoSuchMethodException var5) {
         } catch (IllegalAccessException var6) {
         } catch (InvocationTargetException var7) {
         }

         return var3;
      }
   }

   private static String[] toArray(List list) {
      return (String[])var0.toArray(new String[var0.size()]);
   }

   private static ArrayList getCauseMethodNameList() {
      return new ArrayList(Arrays.asList(CAUSE_METHOD_NAMES));
   }

   public static boolean isCauseMethodName(String methodName) {
      return ApacheCommonsLangUtil.indexOf(CAUSE_METHOD_NAMES, var0) >= 0;
   }

   public static Throwable getCause(Throwable throwable) {
      return getCause(var0, CAUSE_METHOD_NAMES);
   }

   public static Throwable getCause(Throwable throwable, String[] methodNames) {
      if (var0 == null) {
         return null;
      } else {
         Throwable var2 = getCauseUsingWellKnownTypes(var0);
         if (var2 == null) {
            if (var1 == null) {
               var1 = CAUSE_METHOD_NAMES;
            }

            for(int var3 = 0; var3 < var1.length; ++var3) {
               String var4 = var1[var3];
               if (var4 != null) {
                  var2 = getCauseUsingMethodName(var0, var4);
                  if (var2 != null) {
                     break;
                  }
               }
            }

            if (var2 == null) {
               var2 = getCauseUsingFieldName(var0, "detail");
            }
         }

         return var2;
      }
   }

   public static Throwable getRootCause(Throwable throwable) {
      List var1 = getThrowableList(var0);
      return var1.size() < 2 ? null : (Throwable)var1.get(var1.size() - 1);
   }

   private static Throwable getCauseUsingWellKnownTypes(Throwable throwable) {
      if (var0 instanceof ApacheCommonsExceptionUtil.Nestable) {
         return var0.getCause();
      } else if (var0 instanceof SQLException) {
         return ((SQLException)var0).getNextException();
      } else {
         return var0 instanceof InvocationTargetException ? ((InvocationTargetException)var0).getTargetException() : null;
      }
   }

   private static Throwable getCauseUsingMethodName(Throwable throwable, String methodName) {
      Method var2 = null;

      try {
         var2 = var0.getClass().getMethod(var1, (Class[])null);
      } catch (NoSuchMethodException var7) {
      } catch (SecurityException var8) {
      }

      if (var2 != null && Throwable.class.isAssignableFrom(var2.getReturnType())) {
         try {
            return (Throwable)var2.invoke(var0);
         } catch (IllegalAccessException var4) {
         } catch (IllegalArgumentException var5) {
         } catch (InvocationTargetException var6) {
         }
      }

      return null;
   }

   private static Throwable getCauseUsingFieldName(Throwable throwable, String fieldName) {
      Field var2 = null;

      try {
         var2 = var0.getClass().getField(var1);
      } catch (NoSuchFieldException var6) {
      } catch (SecurityException var7) {
      }

      if (var2 != null && Throwable.class.isAssignableFrom(var2.getType())) {
         try {
            return (Throwable)var2.get(var0);
         } catch (IllegalAccessException var4) {
         } catch (IllegalArgumentException var5) {
         }
      }

      return null;
   }

   public static boolean isThrowableNested() {
      return THROWABLE_CAUSE_METHOD != null;
   }

   public static boolean isNestedThrowable(Throwable throwable) {
      if (var0 == null) {
         return false;
      } else if (var0 instanceof ApacheCommonsExceptionUtil.Nestable) {
         return true;
      } else if (var0 instanceof SQLException) {
         return true;
      } else if (var0 instanceof InvocationTargetException) {
         return true;
      } else if (isThrowableNested()) {
         return true;
      } else {
         Class var1 = var0.getClass();
         int var2 = 0;

         for(int var3 = CAUSE_METHOD_NAMES.length; var2 < var3; ++var2) {
            try {
               Method var4 = var1.getMethod(CAUSE_METHOD_NAMES[var2], (Class[])null);
               if (var4 != null && Throwable.class.isAssignableFrom(var4.getReturnType())) {
                  return true;
               }
            } catch (NoSuchMethodException var7) {
            } catch (SecurityException var8) {
            }
         }

         try {
            Field var9 = var1.getField("detail");
            if (var9 != null) {
               return true;
            }
         } catch (NoSuchFieldException var5) {
         } catch (SecurityException var6) {
         }

         return false;
      }
   }

   public static int getThrowableCount(Throwable throwable) {
      return getThrowableList(var0).size();
   }

   public static Throwable[] getThrowables(Throwable throwable) {
      List var1 = getThrowableList(var0);
      return (Throwable[])var1.toArray(new Throwable[var1.size()]);
   }

   public static List getThrowableList(Throwable throwable) {
      ArrayList var1;
      for(var1 = new ArrayList(); var0 != null && !var1.contains(var0); var0 = getCause(var0)) {
         var1.add(var0);
      }

      return var1;
   }

   public static int indexOfThrowable(Throwable throwable, Class clazz) {
      return indexOf(var0, var1, 0, false);
   }

   public static int indexOfThrowable(Throwable throwable, Class clazz, int fromIndex) {
      return indexOf(var0, var1, var2, false);
   }

   public static int indexOfType(Throwable throwable, Class type) {
      return indexOf(var0, var1, 0, true);
   }

   public static int indexOfType(Throwable throwable, Class type, int fromIndex) {
      return indexOf(var0, var1, var2, true);
   }

   private static int indexOf(Throwable throwable, Class type, int fromIndex, boolean subclass) {
      if (var0 != null && var1 != null) {
         if (var2 < 0) {
            var2 = 0;
         }

         Throwable[] var4 = getThrowables(var0);
         if (var2 >= var4.length) {
            return -1;
         } else {
            int var5;
            if (var3) {
               for(var5 = var2; var5 < var4.length; ++var5) {
                  if (var1.isAssignableFrom(var4[var5].getClass())) {
                     return var5;
                  }
               }
            } else {
               for(var5 = var2; var5 < var4.length; ++var5) {
                  if (var1.equals(var4[var5].getClass())) {
                     return var5;
                  }
               }
            }

            return -1;
         }
      } else {
         return -1;
      }
   }

   public static void removeCommonFrames(List causeFrames, List wrapperFrames) {
      if (var0 != null && var1 != null) {
         int var2 = var0.size() - 1;

         for(int var3 = var1.size() - 1; var2 >= 0 && var3 >= 0; --var3) {
            String var4 = (String)var0.get(var2);
            String var5 = (String)var1.get(var3);
            if (var4.equals(var5)) {
               var0.remove(var2);
            }

            --var2;
         }

      } else {
         throw new IllegalArgumentException("The List must not be null");
      }
   }

   public static String getFullStackTrace(Throwable throwable) {
      StringWriter var1 = new StringWriter();
      PrintWriter var2 = new PrintWriter(var1, true);
      Throwable[] var3 = getThrowables(var0);

      for(int var4 = 0; var4 < var3.length; ++var4) {
         var3[var4].printStackTrace(var2);
         if (isNestedThrowable(var3[var4])) {
            break;
         }
      }

      return var1.getBuffer().toString();
   }

   public static String getStackTrace(Throwable throwable) {
      StringWriter var1 = new StringWriter();
      PrintWriter var2 = new PrintWriter(var1, true);
      var0.printStackTrace(var2);
      return var1.getBuffer().toString();
   }

   static List getStackFrameList(Throwable t) {
      String var1 = getStackTrace(var0);
      String var2 = LINE_SEPARATOR;
      StringTokenizer var3 = new StringTokenizer(var1, var2);
      ArrayList var4 = new ArrayList();
      boolean var5 = false;

      while(var3.hasMoreTokens()) {
         String var6 = var3.nextToken();
         int var7 = var6.indexOf("at");
         if (var7 != -1 && var6.substring(0, var7).trim().length() == 0) {
            var5 = true;
            var4.add(var6);
         } else if (var5) {
            break;
         }
      }

      return var4;
   }

   static {
      Method var0;
      try {
         var0 = Throwable.class.getMethod("getCause", (Class[])null);
      } catch (Exception var3) {
         var0 = null;
      }

      THROWABLE_CAUSE_METHOD = var0;

      try {
         var0 = Throwable.class.getMethod("initCause", Throwable.class);
      } catch (Exception var2) {
         var0 = null;
      }

      THROWABLE_INITCAUSE_METHOD = var0;
   }

   public interface Nestable {
      Throwable getCause();

      String getMessage();

      String getMessage(int index);

      String[] getMessages();

      Throwable getThrowable(int index);

      int getThrowableCount();

      Throwable[] getThrowables();

      int indexOfThrowable(Class type);

      int indexOfThrowable(Class type, int fromIndex);

      void printStackTrace(PrintWriter out);

      void printStackTrace(PrintStream out);

      void printPartialStackTrace(PrintWriter out);
   }
}
