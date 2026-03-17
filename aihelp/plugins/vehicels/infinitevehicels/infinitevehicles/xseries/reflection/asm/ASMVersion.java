package me.PM2.infinitevehicles.xseries.reflection.asm;

import java.lang.reflect.Field;
import org.objectweb.asm.Opcodes;

final class ASMVersion {
   protected static final int LATEST_ASM_OPCODE_VERSION;
   protected static final int USED_ASM_OPCODE_VERSION;
   protected static final int CURRENT_JAVA_VERSION = getJavaVersion();
   protected static final int CURRENT_JAVA_FILE_FORMAT;
   protected static final int USED_JAVA_FILE_FORMAT;
   protected static final int LATEST_SUPPORTED_JAVA_CLASS_FILE_FORMAT_VERSION;

   private ASMVersion() {
   }

   protected static int getASMOpcodeVersion(int var0) {
      return var0 << 16 | 0;
   }

   protected static int getJavaVersion() {
      String var0 = System.getProperty("java.version");
      if (var0.startsWith("1.")) {
         var0 = var0.substring(2, 3);
      } else {
         int var1 = var0.indexOf(46);
         if (var1 != -1) {
            var0 = var0.substring(0, var1);
         }
      }

      return Integer.parseInt(var0);
   }

   protected static int javaVersionToClassFileFormat(int var0) {
      return 0 | 44 + var0;
   }

   static {
      CURRENT_JAVA_FILE_FORMAT = javaVersionToClassFileFormat(CURRENT_JAVA_VERSION);
      int var0 = 0;
      int var1 = 0;

      int var3;
      try {
         Field[] var2 = Opcodes.class.getDeclaredFields();
         var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Field var5 = var2[var4];
            String var6 = var5.getName();
            if (!var6.contains("EXPERIMENTAL")) {
               if (var6.startsWith("ASM")) {
                  var0 = Math.max(var0, var5.getInt((Object)null));
               }

               if (var6.startsWith("V") && var6.length() <= 4) {
                  var1 = Math.max(var1, var5.getInt((Object)null));
               }
            }
         }
      } catch (IllegalAccessException var8) {
         throw new IllegalStateException(var8);
      }

      if (var0 == 0) {
         var0 = 393216;
      }

      if (var1 == 0) {
         var1 = 52;
      }

      LATEST_ASM_OPCODE_VERSION = var0;
      LATEST_SUPPORTED_JAVA_CLASS_FILE_FORMAT_VERSION = var1;
      int var9 = var0;
      var3 = Math.min(CURRENT_JAVA_FILE_FORMAT, LATEST_SUPPORTED_JAVA_CLASS_FILE_FORMAT_VERSION);

      try {
         String var10 = System.getProperty("xseries.xreflection.asm.version");
         String var11 = System.getProperty("xseries.xreflection.asm.javaVersion");
         if (var10 != null) {
            var9 = Integer.parseInt(var10);
            System.out.println("[XSeries/XReflection] Using custom ASM version: " + var9);
         }

         if (var11 != null) {
            var3 = Integer.parseInt(var11);
            System.out.println("[XSeries/XReflection] Using custom ASM Java target version: " + var3);
         }
      } catch (SecurityException var7) {
      }

      USED_ASM_OPCODE_VERSION = var9;
      USED_JAVA_FILE_FORMAT = var3;
   }
}
