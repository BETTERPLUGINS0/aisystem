package fr.xephi.authme.libs.com.mysql.cj.util;

import fr.xephi.authme.libs.com.mysql.cj.Constants;
import fr.xephi.authme.libs.com.mysql.cj.Messages;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.CJException;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.ExceptionFactory;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.ExceptionInterceptor;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.WrongArgumentException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class Util {
   private static int jvmVersion = 8;
   private static int jvmUpdateNumber = -1;
   private static final ConcurrentMap<Class<?>, Boolean> isJdbcInterfaceCache;
   private static final ConcurrentMap<Class<?>, Class<?>[]> implementedInterfacesCache;

   public static int getJVMVersion() {
      return jvmVersion;
   }

   public static boolean jvmMeetsMinimum(int version, int updateNumber) {
      return getJVMVersion() > version || getJVMVersion() == version && getJVMUpdateNumber() >= updateNumber;
   }

   public static int getJVMUpdateNumber() {
      return jvmUpdateNumber;
   }

   public static boolean isCommunityEdition(String serverVersion) {
      return !isEnterpriseEdition(serverVersion);
   }

   public static boolean isEnterpriseEdition(String serverVersion) {
      return serverVersion.contains("enterprise") || serverVersion.contains("commercial") || serverVersion.contains("advanced");
   }

   public static String stackTraceToString(Throwable ex) {
      StringBuilder traceBuf = new StringBuilder();
      traceBuf.append(Messages.getString("Util.1"));
      if (ex != null) {
         traceBuf.append(ex.getClass().getName());
         String message = ex.getMessage();
         if (message != null) {
            traceBuf.append(Messages.getString("Util.2"));
            traceBuf.append(message);
         }

         StringWriter out = new StringWriter();
         PrintWriter printOut = new PrintWriter(out);
         ex.printStackTrace(printOut);
         traceBuf.append(Messages.getString("Util.3"));
         traceBuf.append(out.toString());
      }

      traceBuf.append(Messages.getString("Util.4"));
      return traceBuf.toString();
   }

   public static <T> T getInstance(Class<T> returnType, String className, Class<?>[] argTypes, Object[] args, ExceptionInterceptor exceptionInterceptor) {
      try {
         Class<?> clazz = Class.forName(className, false, Util.class.getClassLoader());
         if (!returnType.isAssignableFrom(clazz)) {
            throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, Messages.getString("Util.WrongImplementation", new Object[]{className, returnType.getName()}), exceptionInterceptor);
         } else {
            return handleNewInstance(clazz.getConstructor(argTypes), args, exceptionInterceptor);
         }
      } catch (NoSuchMethodException | SecurityException | ClassNotFoundException var6) {
         throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, Messages.getString("Util.FailLoadClass", new Object[]{className}), var6, exceptionInterceptor);
      }
   }

   public static <T> T handleNewInstance(Constructor<T> ctor, Object[] args, ExceptionInterceptor exceptionInterceptor) {
      try {
         return ctor.newInstance(args);
      } catch (InstantiationException | IllegalAccessException | IllegalArgumentException var5) {
         throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, Messages.getString("Util.FailCreateInstance", new Object[]{ctor.getDeclaringClass().getName()}), var5, exceptionInterceptor);
      } catch (InvocationTargetException var6) {
         Throwable target = var6.getCause();
         if (target instanceof ExceptionInInitializerError) {
            target = target.getCause();
         } else if (target instanceof CJException) {
            throw (CJException)target;
         }

         throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, target.getMessage(), target, exceptionInterceptor);
      }
   }

   public static Map<Object, Object> calculateDifferences(Map<?, ?> map1, Map<?, ?> map2) {
      Map<Object, Object> diffMap = new HashMap();
      Iterator var3 = map1.entrySet().iterator();

      while(true) {
         Object key;
         Object value1;
         Object value2;
         while(true) {
            if (!var3.hasNext()) {
               return diffMap;
            }

            Entry<?, ?> entry = (Entry)var3.next();
            key = entry.getKey();
            value1 = null;
            value2 = null;
            if (entry.getValue() instanceof Number) {
               value1 = (Number)entry.getValue();
               value2 = (Number)map2.get(key);
               break;
            }

            try {
               value1 = new Double(entry.getValue().toString());
               value2 = new Double(map2.get(key).toString());
               break;
            } catch (NumberFormatException var9) {
            }
         }

         if (!value1.equals(value2)) {
            if (value1 instanceof Byte) {
               diffMap.put(key, (byte)((Byte)value2 - (Byte)value1));
            } else if (value1 instanceof Short) {
               diffMap.put(key, (short)((Short)value2 - (Short)value1));
            } else if (value1 instanceof Integer) {
               diffMap.put(key, (Integer)value2 - (Integer)value1);
            } else if (value1 instanceof Long) {
               diffMap.put(key, (Long)value2 - (Long)value1);
            } else if (value1 instanceof Float) {
               diffMap.put(key, (Float)value2 - (Float)value1);
            } else if (value1 instanceof Double) {
               diffMap.put(key, (double)(((Double)value2).shortValue() - ((Double)value1).shortValue()));
            } else if (value1 instanceof BigDecimal) {
               diffMap.put(key, ((BigDecimal)value2).subtract((BigDecimal)value1));
            } else if (value1 instanceof BigInteger) {
               diffMap.put(key, ((BigInteger)value2).subtract((BigInteger)value1));
            }
         }
      }
   }

   public static <T> List<T> loadClasses(Class<T> instancesType, String extensionClassNames, String errorMessageKey, ExceptionInterceptor exceptionInterceptor) {
      try {
         return (List)StringUtils.split(extensionClassNames, ",", true).stream().filter((s) -> {
            return !s.isEmpty();
         }).map((c) -> {
            return getInstance(instancesType, c, (Class[])null, (Object[])null, exceptionInterceptor);
         }).collect(Collectors.toCollection(LinkedList::new));
      } catch (Throwable var5) {
         throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, Messages.getString(errorMessageKey), var5, exceptionInterceptor);
      }
   }

   public static boolean isJdbcInterface(Class<?> clazz) {
      if (isJdbcInterfaceCache.containsKey(clazz)) {
         return (Boolean)isJdbcInterfaceCache.get(clazz);
      } else {
         if (clazz.isInterface()) {
            try {
               if (isJdbcPackage(clazz.getPackage().getName())) {
                  isJdbcInterfaceCache.putIfAbsent(clazz, true);
                  return true;
               }
            } catch (Exception var5) {
            }
         }

         Class[] var1 = clazz.getInterfaces();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            Class<?> iface = var1[var3];
            if (isJdbcInterface(iface)) {
               isJdbcInterfaceCache.putIfAbsent(clazz, true);
               return true;
            }
         }

         if (clazz.getSuperclass() != null && isJdbcInterface(clazz.getSuperclass())) {
            isJdbcInterfaceCache.putIfAbsent(clazz, true);
            return true;
         } else {
            isJdbcInterfaceCache.putIfAbsent(clazz, false);
            return false;
         }
      }
   }

   public static boolean isJdbcPackage(String packageName) {
      return packageName != null && (packageName.startsWith("java.sql") || packageName.startsWith("javax.sql") || packageName.startsWith("fr.xephi.authme.libs.com.mysql.cj.jdbc"));
   }

   public static Class<?>[] getImplementedInterfaces(Class<?> clazz) {
      Class<?>[] implementedInterfaces = (Class[])implementedInterfacesCache.get(clazz);
      if (implementedInterfaces != null) {
         return implementedInterfaces;
      } else {
         Set<Class<?>> interfaces = new LinkedHashSet();
         Class superClass = clazz;

         do {
            Collections.addAll(interfaces, superClass.getInterfaces());
         } while((superClass = superClass.getSuperclass()) != null);

         implementedInterfaces = (Class[])interfaces.toArray(new Class[interfaces.size()]);
         Class<?>[] oldValue = (Class[])implementedInterfacesCache.putIfAbsent(clazz, implementedInterfaces);
         if (oldValue != null) {
            implementedInterfaces = oldValue;
         }

         return implementedInterfaces;
      }
   }

   public static long secondsSinceMillis(long timeInMillis) {
      return (System.currentTimeMillis() - timeInMillis) / 1000L;
   }

   public static int truncateAndConvertToInt(long longValue) {
      return longValue > 2147483647L ? Integer.MAX_VALUE : (longValue < -2147483648L ? Integer.MIN_VALUE : (int)longValue);
   }

   public static int[] truncateAndConvertToInt(long[] longArray) {
      int[] intArray = new int[longArray.length];

      for(int i = 0; i < longArray.length; ++i) {
         intArray[i] = longArray[i] > 2147483647L ? Integer.MAX_VALUE : (longArray[i] < -2147483648L ? Integer.MIN_VALUE : (int)longArray[i]);
      }

      return intArray;
   }

   public static String getPackageName(Class<?> clazz) {
      String fqcn = clazz.getName();
      int classNameStartsAt = fqcn.lastIndexOf(46);
      return classNameStartsAt > 0 ? fqcn.substring(0, classNameStartsAt) : "";
   }

   public static boolean isRunningOnWindows() {
      return StringUtils.indexOfIgnoreCase(Constants.OS_NAME, "WINDOWS") != -1;
   }

   public static int readFully(Reader reader, char[] buf, int length) throws IOException {
      int numCharsRead;
      int count;
      for(numCharsRead = 0; numCharsRead < length; numCharsRead += count) {
         count = reader.read(buf, numCharsRead, length - numCharsRead);
         if (count < 0) {
            break;
         }
      }

      return numCharsRead;
   }

   public static final int readBlock(InputStream i, byte[] b, ExceptionInterceptor exceptionInterceptor) {
      try {
         return i.read(b);
      } catch (Throwable var4) {
         throw ExceptionFactory.createException(Messages.getString("Util.5") + var4.getClass().getName(), exceptionInterceptor);
      }
   }

   public static final int readBlock(InputStream i, byte[] b, int length, ExceptionInterceptor exceptionInterceptor) {
      try {
         int lengthToRead = length;
         if (length > b.length) {
            lengthToRead = b.length;
         }

         return i.read(b, 0, lengthToRead);
      } catch (Throwable var5) {
         throw ExceptionFactory.createException(Messages.getString("Util.5") + var5.getClass().getName(), exceptionInterceptor);
      }
   }

   static {
      int startPos = Constants.JVM_VERSION.indexOf(46);
      int endPos = startPos + 1;
      if (startPos != -1) {
         while(Character.isDigit(Constants.JVM_VERSION.charAt(endPos))) {
            ++endPos;
            if (endPos >= Constants.JVM_VERSION.length()) {
               break;
            }
         }
      }

      ++startPos;
      if (endPos > startPos) {
         jvmVersion = Integer.parseInt(Constants.JVM_VERSION.substring(startPos, endPos));
      }

      startPos = Constants.JVM_VERSION.indexOf("_");
      endPos = startPos + 1;
      if (startPos != -1) {
         while(Character.isDigit(Constants.JVM_VERSION.charAt(endPos))) {
            ++endPos;
            if (endPos >= Constants.JVM_VERSION.length()) {
               break;
            }
         }
      }

      ++startPos;
      if (endPos > startPos) {
         jvmUpdateNumber = Integer.parseInt(Constants.JVM_VERSION.substring(startPos, endPos));
      }

      isJdbcInterfaceCache = new ConcurrentHashMap();
      implementedInterfacesCache = new ConcurrentHashMap();
   }
}
