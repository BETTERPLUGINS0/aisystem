package emanondev.itemedit.utility;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ReflectionUtils {
   private ReflectionUtils() {
      throw new UnsupportedOperationException();
   }

   @NotNull
   public static Field getDeclaredField(@NotNull Class<?> clazz, @NotNull String fieldName) {
      try {
         Field field = clazz.getDeclaredField(fieldName);
         field.setAccessible(true);
         return field;
      } catch (Exception var3) {
         throw new RuntimeException(var3);
      }
   }

   public static boolean isClassPresent(@NotNull String className) {
      try {
         Class.forName(className);
         return true;
      } catch (ClassNotFoundException var2) {
         return false;
      }
   }

   @NotNull
   public static Method getDeclaredMethod(@NotNull Class<?> clazz, @NotNull String methodName, @Nullable Class<?>... parameterTypes) {
      try {
         Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
         method.setAccessible(true);
         return method;
      } catch (Exception var4) {
         throw new RuntimeException("Method not found: " + methodName, var4);
      }
   }

   @NotNull
   public static Method getMethod(@NotNull Class<?> clazz, @NotNull String methodName, @Nullable Class<?>... parameterTypes) {
      try {
         return clazz.getMethod(methodName, parameterTypes);
      } catch (Exception var4) {
         throw new RuntimeException("Method not found: " + methodName, var4);
      }
   }

   @Nullable
   public static Object invokeMethod(@Nullable Object obj, @NotNull Method method, @Nullable Object... args) {
      try {
         method.setAccessible(true);
         return method.invoke(obj, args);
      } catch (Exception var4) {
         throw new RuntimeException("Failed to invoke method: " + method.getName(), var4);
      }
   }

   @Nullable
   public static Object invokeMethod(@NotNull Object obj, @NotNull String methodName) {
      try {
         Method method = obj.getClass().getMethod(methodName);
         method.setAccessible(true);
         return method.invoke(obj);
      } catch (Exception var3) {
         throw new RuntimeException(var3);
      }
   }

   @Nullable
   public static <P1> Object invokeMethod(@NotNull Object obj, @NotNull String methodName, @NotNull Class<P1> clazzOne, @Nullable P1 paramOne) {
      return invokeMethod(obj, methodName, new Class[]{clazzOne}, paramOne);
   }

   @Nullable
   public static <P1, P2> Object invokeMethod(@NotNull Object obj, @NotNull String methodName, @NotNull Class<P1> clazzOne, @Nullable P1 paramOne, @NotNull Class<P2> clazzTwo, @Nullable P2 paramTwo) {
      return invokeMethod(obj, methodName, new Class[]{clazzOne, clazzTwo}, paramOne, paramTwo);
   }

   @Nullable
   public static <P1, P2, P3> Object invokeMethod(@NotNull Object obj, @NotNull String methodName, @NotNull Class<P1> clazzOne, @Nullable P1 paramOne, @NotNull Class<P2> clazzTwo, @Nullable P2 paramTwo, @NotNull Class<P3> clazzThree, @Nullable P3 paramThree) {
      return invokeMethod(obj, methodName, new Class[]{clazzOne, clazzTwo, clazzThree}, paramOne, paramTwo, paramThree);
   }

   @Nullable
   public static <P1, P2, P3, P4> Object invokeMethod(@NotNull Object obj, @NotNull String methodName, @NotNull Class<P1> clazzOne, @Nullable P1 paramOne, @NotNull Class<P2> clazzTwo, @Nullable P2 paramTwo, @NotNull Class<P3> clazzThree, @Nullable P3 paramThree, @NotNull Class<P4> clazzFour, @Nullable P4 paramFour) {
      return invokeMethod(obj, methodName, new Class[]{clazzOne, clazzTwo, clazzThree, clazzFour}, paramOne, paramTwo, paramThree, paramFour);
   }

   @Nullable
   public static Object invokeMethod(@NotNull Object obj, @NotNull String methodName, @NotNull Class<?>[] parameterTypes, Object... args) {
      try {
         Method method = obj.getClass().getMethod(methodName, parameterTypes);
         method.setAccessible(true);
         return method.invoke(obj, args);
      } catch (Exception var5) {
         throw new RuntimeException(var5);
      }
   }

   @Nullable
   public static Object invokeStaticMethod(@NotNull Class<?> clazz, @NotNull String methodName, Object... args) {
      try {
         Class<?>[] argTypes = new Class[args.length];

         for(int i = 0; i < args.length; ++i) {
            argTypes[i] = args[i].getClass();
         }

         Method method = clazz.getMethod(methodName, argTypes);
         method.setAccessible(true);
         return method.invoke((Object)null, args);
      } catch (Exception var5) {
         throw new RuntimeException(var5);
      }
   }

   public static void setFieldValue(@NotNull Object obj, @NotNull String fieldName, @Nullable Object value) {
      try {
         Field field = getDeclaredField(obj.getClass(), fieldName);
         field.setAccessible(true);
         field.set(obj, value);
      } catch (Exception var4) {
         throw new RuntimeException(var4);
      }
   }

   @Nullable
   public static Object getFieldValue(@NotNull Object obj, @NotNull String fieldName) {
      try {
         Field field = getDeclaredField(obj.getClass(), fieldName);
         field.setAccessible(true);
         return field.get(obj);
      } catch (Exception var3) {
         throw new RuntimeException(var3);
      }
   }

   public static <T> T invokeConstructor(@NotNull Class<T> clazz) {
      try {
         Constructor<T> constructor = clazz.getConstructor();
         constructor.setAccessible(true);
         return constructor.newInstance();
      } catch (Exception var2) {
         throw new RuntimeException("Failed to invoke default constructor", var2);
      }
   }

   public static <T, P1> T invokeConstructor(@NotNull Class<T> clazz, @NotNull Class<P1> clazzOne, @Nullable P1 paramOne) {
      return invokeConstructor(clazz, new Class[]{clazzOne}, new Object[]{paramOne});
   }

   public static <T, P1, P2> T invokeConstructor(@NotNull Class<T> clazz, @NotNull Class<P1> clazzOne, @Nullable P1 paramOne, @NotNull Class<P2> clazzTwo, @Nullable P2 paramTwo) {
      return invokeConstructor(clazz, new Class[]{clazzOne, clazzTwo}, new Object[]{paramOne, paramTwo});
   }

   public static <T, P1, P2, P3> T invokeConstructor(@NotNull Class<T> clazz, @NotNull Class<P1> clazzOne, @Nullable P1 paramOne, @NotNull Class<P2> clazzTwo, @Nullable P2 paramTwo, @NotNull Class<P3> clazzThree, @Nullable P3 paramThree) {
      return invokeConstructor(clazz, new Class[]{clazzOne, clazzTwo, clazzThree}, new Object[]{paramOne, paramTwo, paramThree});
   }

   public static <T, P1, P2, P3, P4> T invokeConstructor(@NotNull Class<T> clazz, @NotNull Class<P1> clazzOne, @Nullable P1 paramOne, @NotNull Class<P2> clazzTwo, @Nullable P2 paramTwo, @NotNull Class<P3> clazzThree, @Nullable P3 paramThree, @NotNull Class<P4> clazzFour, @Nullable P4 paramFour) {
      return invokeConstructor(clazz, new Class[]{clazzOne, clazzTwo, clazzThree, clazzFour}, new Object[]{paramOne, paramTwo, paramThree, paramFour});
   }

   public static <T, P1, P2, P3, P4, P5> T invokeConstructor(@NotNull Class<T> clazz, @NotNull Class<P1> clazzOne, @Nullable P1 paramOne, @NotNull Class<P2> clazzTwo, @Nullable P2 paramTwo, @NotNull Class<P3> clazzThree, @Nullable P3 paramThree, @NotNull Class<P4> clazzFour, @Nullable P4 paramFour, @NotNull Class<P5> clazzFive, @Nullable P5 paramFive) {
      return invokeConstructor(clazz, new Class[]{clazzOne, clazzTwo, clazzThree, clazzFour, clazzFive}, new Object[]{paramOne, paramTwo, paramThree, paramFour, paramFive});
   }

   public static <T> T invokeConstructor(@NotNull Class<T> clazz, @NotNull Class<?>[] paramTypes, @Nullable Object[] params) {
      try {
         Constructor<T> constructor = clazz.getConstructor(paramTypes);
         constructor.setAccessible(true);
         return constructor.newInstance(params);
      } catch (Exception var4) {
         throw new RuntimeException("Failed to invoke parameterized constructor", var4);
      }
   }

   public static boolean isAnnotatedWith(@NotNull Class<?> clazz, @NotNull Class<? extends Annotation> annotationClass) {
      return clazz.isAnnotationPresent(annotationClass);
   }

   @NotNull
   public static Annotation[] getClassAnnotations(@NotNull Class<?> clazz) {
      return clazz.getAnnotations();
   }
}
