package me.PM2.infinitevehicles.xseries.reflection.asm;

import java.io.PrintWriter;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.Analyzer;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.SimpleVerifier;
import org.objectweb.asm.util.CheckClassAdapter;

final class ASMAnalyzer {
   private static final MethodHandle CheckClassAdapter_printAnalyzerResult;

   private ASMAnalyzer() {
   }

   protected static Throwable findCause(Throwable var0, Predicate<Throwable> var1) {
      Set var2 = Collections.newSetFromMap(new IdentityHashMap(5));
      Throwable var3 = var0;

      while(var2.add(var3)) {
         if (var1.test(var3)) {
            return var3;
         }

         var3 = var3.getCause();
         if (var3 == null) {
            return null;
         }
      }

      return null;
   }

   protected static void verify(ClassReader var0, ClassLoader var1, boolean var2, PrintWriter var3) {
      ClassNode var4 = new ClassNode();
      var0.accept(new CheckClassAdapter(ASMVersion.LATEST_ASM_OPCODE_VERSION, var4, false) {
      }, 2);
      Type var5 = var4.superName == null ? null : Type.getObjectType(var4.superName);
      List var6 = var4.methods;
      ArrayList var7 = new ArrayList();
      Iterator var8 = var4.interfaces.iterator();

      while(var8.hasNext()) {
         String var9 = (String)var8.next();
         var7.add(Type.getObjectType(var9));
      }

      var8 = var6.iterator();

      while(true) {
         Analyzer var11;
         boolean var12;
         MethodNode var17;
         do {
            if (!var8.hasNext()) {
               var3.flush();
               return;
            }

            var17 = (MethodNode)var8.next();
            SimpleVerifier var10 = new SimpleVerifier(Type.getObjectType(var4.name), var5, var7, (var4.access & 512) != 0);
            var11 = new Analyzer(var10);
            if (var1 != null) {
               var10.setClassLoader(var1);
            }

            try {
               var11.analyze(var4.name, var17);
               var12 = false;
            } catch (AnalyzerException var16) {
               ClassNotFoundException var14 = (ClassNotFoundException)findCause(var16, (var0x) -> {
                  return var0x instanceof ClassNotFoundException;
               });
               if (var14 != null && var14.getMessage() != null && var14.getMessage().contains("XSeriesGen")) {
                  var12 = false;
               } else {
                  var12 = true;
                  var16.printStackTrace(var3);
               }
            }
         } while(!var2 && !var12);

         try {
            CheckClassAdapter_printAnalyzerResult.invokeExact(var17, var11, var3);
         } catch (Throwable var15) {
            throw new IllegalStateException("Cannot write bytecode instructions: ", var15);
         }
      }
   }

   static {
      Lookup var1 = MethodHandles.lookup();

      MethodHandle var0;
      try {
         Method var2 = CheckClassAdapter.class.getDeclaredMethod("printAnalyzerResult", MethodNode.class, Analyzer.class, PrintWriter.class);
         var2.setAccessible(true);
         var0 = var1.unreflect(var2);
      } catch (IllegalAccessException | NoSuchMethodException var3) {
         var0 = null;
      }

      CheckClassAdapter_printAnalyzerResult = var0;
   }
}
