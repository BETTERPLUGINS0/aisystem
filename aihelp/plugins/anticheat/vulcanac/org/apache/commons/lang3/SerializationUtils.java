package org.apache.commons.lang3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SerializationUtils {
   public static <T extends Serializable> T clone(T var0) {
      if (var0 == null) {
         return null;
      } else {
         byte[] var1 = serialize(var0);
         ByteArrayInputStream var2 = new ByteArrayInputStream(var1);

         try {
            SerializationUtils.ClassLoaderAwareObjectInputStream var3 = new SerializationUtils.ClassLoaderAwareObjectInputStream(var2, var0.getClass().getClassLoader());
            Throwable var4 = null;

            Serializable var6;
            try {
               Serializable var5 = (Serializable)var3.readObject();
               var6 = var5;
            } catch (Throwable var17) {
               var4 = var17;
               throw var17;
            } finally {
               if (var3 != null) {
                  if (var4 != null) {
                     try {
                        var3.close();
                     } catch (Throwable var16) {
                        var4.addSuppressed(var16);
                     }
                  } else {
                     var3.close();
                  }
               }

            }

            return var6;
         } catch (ClassNotFoundException var19) {
            throw new SerializationException("ClassNotFoundException while reading cloned object data", var19);
         } catch (IOException var20) {
            throw new SerializationException("IOException while reading or closing cloned object data", var20);
         }
      }
   }

   public static <T> T deserialize(byte[] var0) {
      Validate.notNull(var0, "objectData");
      return deserialize((InputStream)(new ByteArrayInputStream(var0)));
   }

   public static <T> T deserialize(InputStream var0) {
      Validate.notNull(var0, "inputStream");

      try {
         ObjectInputStream var1 = new ObjectInputStream(var0);
         Throwable var2 = null;

         Object var4;
         try {
            Object var3 = var1.readObject();
            var4 = var3;
         } catch (Throwable var14) {
            var2 = var14;
            throw var14;
         } finally {
            if (var1 != null) {
               if (var2 != null) {
                  try {
                     var1.close();
                  } catch (Throwable var13) {
                     var2.addSuppressed(var13);
                  }
               } else {
                  var1.close();
               }
            }

         }

         return var4;
      } catch (IOException | ClassNotFoundException var16) {
         throw new SerializationException(var16);
      }
   }

   public static <T extends Serializable> T roundtrip(T var0) {
      return (Serializable)deserialize(serialize(var0));
   }

   public static byte[] serialize(Serializable var0) {
      ByteArrayOutputStream var1 = new ByteArrayOutputStream(512);
      serialize(var0, var1);
      return var1.toByteArray();
   }

   public static void serialize(Serializable var0, OutputStream var1) {
      Validate.notNull(var1, "outputStream");

      try {
         ObjectOutputStream var2 = new ObjectOutputStream(var1);
         Throwable var3 = null;

         try {
            var2.writeObject(var0);
         } catch (Throwable var13) {
            var3 = var13;
            throw var13;
         } finally {
            if (var2 != null) {
               if (var3 != null) {
                  try {
                     var2.close();
                  } catch (Throwable var12) {
                     var3.addSuppressed(var12);
                  }
               } else {
                  var2.close();
               }
            }

         }

      } catch (IOException var15) {
         throw new SerializationException(var15);
      }
   }

   static class ClassLoaderAwareObjectInputStream extends ObjectInputStream {
      private static final Map<String, Class<?>> primitiveTypes = new HashMap();
      private final ClassLoader classLoader;

      ClassLoaderAwareObjectInputStream(InputStream var1, ClassLoader var2) {
         super(var1);
         this.classLoader = var2;
      }

      protected Class<?> resolveClass(ObjectStreamClass var1) {
         String var2 = var1.getName();

         try {
            return Class.forName(var2, false, this.classLoader);
         } catch (ClassNotFoundException var7) {
            try {
               return Class.forName(var2, false, Thread.currentThread().getContextClassLoader());
            } catch (ClassNotFoundException var6) {
               Class var5 = (Class)primitiveTypes.get(var2);
               if (var5 != null) {
                  return var5;
               } else {
                  throw var6;
               }
            }
         }
      }

      static {
         primitiveTypes.put("byte", Byte.TYPE);
         primitiveTypes.put("short", Short.TYPE);
         primitiveTypes.put("int", Integer.TYPE);
         primitiveTypes.put("long", Long.TYPE);
         primitiveTypes.put("float", Float.TYPE);
         primitiveTypes.put("double", Double.TYPE);
         primitiveTypes.put("boolean", Boolean.TYPE);
         primitiveTypes.put("char", Character.TYPE);
         primitiveTypes.put("void", Void.TYPE);
      }
   }
}
