package me.PM2.infinitevehicles.xseries.reflection.proxy.generator;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Repeatable;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.stream.Collectors;
import me.PM2.infinitevehicles.xseries.reflection.XAccessFlag;
import me.PM2.infinitevehicles.xseries.reflection.jvm.objects.ReflectedObject;
import me.PM2.infinitevehicles.xseries.reflection.proxy.ReflectiveProxyObject;
import me.PM2.infinitevehicles.xseries.reflection.proxy.annotations.Constructor;
import me.PM2.infinitevehicles.xseries.reflection.proxy.annotations.Field;
import me.PM2.infinitevehicles.xseries.reflection.proxy.annotations.Final;
import me.PM2.infinitevehicles.xseries.reflection.proxy.annotations.Ignore;
import me.PM2.infinitevehicles.xseries.reflection.proxy.annotations.Private;
import me.PM2.infinitevehicles.xseries.reflection.proxy.annotations.Protected;
import me.PM2.infinitevehicles.xseries.reflection.proxy.annotations.Proxify;
import me.PM2.infinitevehicles.xseries.reflection.proxy.annotations.Static;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public final class XProxifier {
   private static final String MEMBER_SPACES = "    ";
   private final StringBuilder writer = new StringBuilder(1000);
   private final Set<String> imports = new HashSet(20);
   private final String proxifiedClassName;
   private final Class<?> clazz;
   private final boolean generateIntelliJAnnotations = true;
   private final boolean generateInaccessibleMembers = true;
   private final boolean copyAnnotations = true;
   private final boolean writeComments = true;
   private final boolean writeInfoAnnotationsAsComments = true;
   private boolean disableIDEFormatting;
   private Function<Class<?>, String> remapper;

   public XProxifier(Class<?> var1) {
      this.clazz = var1;
      this.proxifiedClassName = var1.getSimpleName() + "Proxified";
      this.proxify();
   }

   private static Class<?> unwrapArrayType(Class<?> var0) {
      while(var0.isArray()) {
         var0 = var0.getComponentType();
      }

      return var0;
   }

   private void imports(Class<?> var1) {
      var1 = unwrapArrayType(var1);
      if (!var1.isPrimitive() && !var1.getPackage().getName().equals("java.lang")) {
         this.imports.add(var1.getName().replace('$', '.'));
      }

   }

   private void writeComments(String... var1) {
      boolean var2 = var1.length > 1;
      if (!var2) {
         this.writer.append("// ").append(var1[0]).append('\n');
      }

      this.writer.append("/**\n");
      String[] var3 = var1;
      int var4 = var1.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String var6 = var3[var5];
         this.writer.append(" * ");
         this.writer.append(var6);
         this.writer.append('\n');
      }

      this.writer.append(" */\n");
   }

   private void writeThrownExceptions(Class<?>[] var1) {
      if (var1 != null && var1.length != 0) {
         this.writer.append(" throws ");
         StringJoiner var2 = new StringJoiner(", ");
         Class[] var3 = var1;
         int var4 = var1.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Class var6 = var3[var5];
            this.imports(var6);
            var2.add(var6.getSimpleName());
         }

         this.writer.append(var2);
      }
   }

   private void writeMember(ReflectedObject var1) {
      this.writeMember(var1, false);
   }

   private void writeMember(ReflectedObject var1, boolean var2) {
      this.writer.append(this.annotationsToString(true, true, var1));
      Set var3 = var1.accessFlags();
      if (var3.contains(XAccessFlag.PRIVATE)) {
         this.writeAnnotation(Private.class);
      }

      if (var3.contains(XAccessFlag.PROTECTED)) {
         this.writeAnnotation(Protected.class);
      }

      if (var3.contains(XAccessFlag.STATIC)) {
         this.writeAnnotation(Static.class);
      }

      if (var3.contains(XAccessFlag.FINAL)) {
         this.writeAnnotation(Final.class);
      }

      switch(var1.type()) {
      case CONSTRUCTOR:
         this.writeAnnotation(Constructor.class);
         this.writeAnnotation("NotNull");
         java.lang.reflect.Constructor var4 = (java.lang.reflect.Constructor)var1.unreflect();
         String var5 = (String)Arrays.stream(var4.getParameterTypes()).map((var0) -> {
            return "_";
         }).collect(Collectors.joining(", "));
         this.writeAnnotation("Contract", "value = \"" + var5 + " -> new\"", "pure = true");
         break;
      case FIELD:
         this.writeAnnotation(Field.class);
         if (var2) {
            this.writeAnnotation("Contract", "pure = true");
         } else {
            this.writeAnnotation("Contract", "mutates = \"this\"");
         }
      }

      StringJoiner var9 = new StringJoiner(", ", "(", ")");
      Class[] var10 = null;
      this.writer.append("    ");
      switch(var1.type()) {
      case CONSTRUCTOR:
         java.lang.reflect.Constructor var6 = (java.lang.reflect.Constructor)var1.unreflect();
         var10 = var6.getExceptionTypes();
         this.writer.append(this.proxifiedClassName).append(' ').append("construct");
         this.writeParameters(var9, var6.getParameters());
         break;
      case FIELD:
         java.lang.reflect.Field var7 = (java.lang.reflect.Field)var1.unreflect();
         this.imports(var7.getType());
         if (var2) {
            this.writer.append(var7.getType().getSimpleName());
         } else {
            this.writer.append("void");
            var9.add(var7.getType().getSimpleName() + " value");
         }

         this.writer.append(' ');
         this.writer.append(var1.name());
         break;
      case METHOD:
         Method var8 = (Method)var1.unreflect();
         var10 = var8.getExceptionTypes();
         this.imports(var8.getReturnType());
         this.writer.append(var8.getReturnType().getSimpleName());
         this.writer.append(' ');
         this.writer.append(var1.name());
         this.writeParameters(var9, var8.getParameters());
      }

      this.writer.append(var9);
      this.writeThrownExceptions(var10);
      this.writer.append(";\n\n");
   }

   private static Object[] getArray(Object var0) {
      if (var0 instanceof Object[]) {
         return (Object[])var0;
      } else {
         int var1 = Array.getLength(var0);
         Object[] var2 = new Object[var1];

         for(int var3 = 0; var3 < var1; ++var3) {
            var2[var3] = Array.get(var0, var3);
         }

         return var2;
      }
   }

   private String constantToString(Object var1) {
      if (var1 instanceof String) {
         return '"' + var1.toString() + '"';
      } else if (var1 instanceof Class) {
         Class var9 = (Class)var1;
         this.imports(var9);
         return var9.getSimpleName() + ".class";
      } else if (var1 instanceof Annotation) {
         Annotation var8 = (Annotation)var1;
         return this.annotationToString(var8);
      } else if (var1.getClass().isEnum()) {
         this.imports(var1.getClass());
         return var1.getClass().getSimpleName() + '.' + ((Enum)var1).name();
      } else if (!var1.getClass().isArray()) {
         return var1.toString();
      } else {
         Object[] var2 = getArray(var1);
         if (var2.length == 0) {
            return "{}";
         } else {
            StringJoiner var3;
            if (var2.length == 1) {
               var3 = new StringJoiner(", ");
            } else {
               var3 = new StringJoiner(", ", "{", "}");
            }

            Object[] var4 = var2;
            int var5 = var2.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               Object var7 = var4[var6];
               var3.add(this.constantToString(var7));
            }

            return var3.toString();
         }
      }
   }

   private String annotationsToString(boolean var1, boolean var2, AnnotatedElement var3) {
      StringJoiner var4 = (new StringJoiner((var2 ? '\n' : "") + (var1 ? "    " : ""), var1 ? "    " : "", var2 ? "\n" : "")).setEmptyValue("");
      Annotation[] var5 = var3.getAnnotations();
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         Annotation var8 = var5[var7];
         Annotation[] var9 = unwrapRepeatElement(var8);
         if (var9 != null) {
            Annotation[] var10 = var9;
            int var11 = var9.length;

            for(int var12 = 0; var12 < var11; ++var12) {
               Annotation var13 = var10[var12];
               var4.add(this.annotationToString(var13));
            }
         } else {
            var4.add(this.annotationToString(var8));
         }
      }

      return var4.toString();
   }

   private static Annotation[] unwrapRepeatElement(Annotation var0) {
      try {
         Method var1 = var0.annotationType().getDeclaredMethod("value");
         if (var1.getReturnType().isArray()) {
            Class var2 = unwrapArrayType(var1.getReturnType());
            if (var2.isAnnotation()) {
               Repeatable var3 = (Repeatable)var2.getAnnotation(Repeatable.class);
               if (var3 != null && var3.value() == var0.annotationType()) {
                  try {
                     return (Annotation[])var1.invoke(var0);
                  } catch (InvocationTargetException | IllegalAccessException var5) {
                     throw new IllegalArgumentException(var5);
                  }
               }
            }
         }
      } catch (NoSuchMethodException var6) {
      }

      return null;
   }

   private String annotationToString(Annotation var1) {
      ArrayList var2 = new ArrayList();
      boolean var3 = false;
      Method[] var4 = var1.annotationType().getDeclaredMethods();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Method var7 = var4[var6];

         try {
            var7.setAccessible(true);
            String var8 = var7.getName();
            Object var9 = var7.invoke(var1);

            try {
               Object var10 = var7.getDefaultValue();
               if (var10 != null) {
                  if (var10.getClass().isArray()) {
                     if (Arrays.equals(getArray(var10), getArray(var9))) {
                        continue;
                     }
                  } else if (var9.equals(var10)) {
                     continue;
                  }
               }
            } catch (TypeNotPresentException var11) {
            }

            if (var8.equals("value")) {
               var3 = true;
            }

            var2.add(var8 + " = " + this.constantToString(var9));
         } catch (InvocationTargetException | IllegalAccessException var12) {
            throw new IllegalStateException("Failed to get annotation value " + var7, var12);
         }
      }

      this.imports(var1.annotationType());
      String var13;
      if (var2.isEmpty()) {
         var13 = "";
      } else if (var2.size() == 1 && var3) {
         var13 = (String)var2.get(0);
         var5 = var13.indexOf(61);
         var13 = '(' + var13.substring(var5 + 2) + ')';
      } else {
         var13 = '(' + String.join(", ", var2) + ')';
      }

      return '@' + var1.annotationType().getSimpleName() + var13;
   }

   private StringJoiner writeParameters(StringJoiner var1, Parameter[] var2) {
      Parameter[] var3 = var2;
      int var4 = var2.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Parameter var6 = var3[var5];
         this.imports(var6.getType());
         String var7;
         if (var6.isVarArgs()) {
            var7 = var6.getType().getSimpleName() + "... ";
         } else {
            var7 = var6.getType().getSimpleName();
         }

         String var8 = this.annotationsToString(false, false, var6);
         var1.add(var8 + (var8.isEmpty() ? "" : " ") + var7 + ' ' + var6.getName());
      }

      return var1;
   }

   private void writeAnnotation(Class<?> var1, String... var2) {
      this.writeAnnotation(true, var1, var2);
   }

   private void writeAnnotation(boolean var1, Class<?> var2, String... var3) {
      this.imports(var2);
      this.writeAnnotation(var1, var2.getSimpleName(), var3);
   }

   private void writeAnnotation(String var1, String... var2) {
      this.writeAnnotation(true, var1, var2);
   }

   private void writeAnnotation(boolean var1, String var2, String... var3) {
      if (var1) {
         this.writer.append("    ");
      }

      this.writer.append('@').append(var2);
      if (var3.length != 0) {
         StringJoiner var4 = new StringJoiner(", ", "(", ")");
         String[] var5 = var3;
         int var6 = var3.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            String var8 = var5[var7];
            var4.add(var8);
         }

         this.writer.append(var4);
      }

      this.writer.append('\n');
   }

   private void proxify() {
      if (this.disableIDEFormatting) {
         this.writer.append("// ").append("@formatter:").append("OFF").append('\n');
      }

      this.writeComments("This is a generated proxified class for " + this.clazz.getSimpleName() + ". However, you might", "want to review each member and correct its annotations when needed.", "<p>", "It's also recommended to use your IDE's code formatter to adjust", "imports and spaces according to your settings.", "In IntelliJ, this can be done by with Ctrl+Alt+L", "<p>", "Full Target Class Path:", this.clazz.getName());
      this.writer.append(this.annotationsToString(false, true, this.clazz));
      this.writeAnnotation(false, Proxify.class, "target = " + this.clazz.getSimpleName() + ".class");
      if (!XAccessFlag.PUBLIC.isSet(this.clazz.getModifiers())) {
         this.writeAnnotation(false, Private.class);
      }

      if (XAccessFlag.FINAL.isSet(this.clazz.getModifiers())) {
         this.writeAnnotation(false, Final.class);
         this.writeAnnotation(false, "ApiStatus.NonExtendable");
      }

      this.writer.append("public interface ").append(this.proxifiedClassName).append(" extends ").append(ReflectiveProxyObject.class.getSimpleName()).append(" {\n");
      java.lang.reflect.Field[] var1 = this.clazz.getDeclaredFields();
      java.lang.reflect.Field[] var2 = var1;
      int var3 = var1.length;

      int var4;
      for(var4 = 0; var4 < var3; ++var4) {
         java.lang.reflect.Field var5 = var2[var4];
         if (!var5.isSynthetic()) {
            if (!XAccessFlag.FINAL.isSet(var5.getModifiers())) {
               this.writeMember(ReflectedObject.of(var5), false);
            }

            this.writeMember(ReflectedObject.of(var5), true);
         }
      }

      if (var1.length != 0) {
         this.writer.append('\n');
      }

      java.lang.reflect.Constructor[] var7 = this.clazz.getDeclaredConstructors();
      java.lang.reflect.Constructor[] var8 = var7;
      var4 = var7.length;

      int var10;
      for(var10 = 0; var10 < var4; ++var10) {
         java.lang.reflect.Constructor var6 = var8[var10];
         if (!var6.isSynthetic()) {
            this.writeMember(ReflectedObject.of(var6));
         }
      }

      if (var7.length != 0) {
         this.writer.append('\n');
      }

      Method[] var9 = this.clazz.getDeclaredMethods();
      var4 = var9.length;

      for(var10 = 0; var10 < var4; ++var10) {
         Method var11 = var9[var10];
         if (var11.getDeclaringClass() != Object.class && !var11.isSynthetic() && !var11.isBridge()) {
            this.writeMember(ReflectedObject.of(var11));
         }
      }

      this.writer.append('\n');
      this.writeAnnotation(Ignore.class);
      this.writeAnnotation("NotNull");
      this.writeAnnotation("ApiStatus.OverrideOnly");
      this.writeAnnotation("Contract", "value = \"_ -> new\"", "pure = true");
      this.writer.append("    ").append(this.proxifiedClassName).append(" bindTo(@NotNull Object instance);\n");
      this.writer.append("}\n");
      this.finalizeString();
   }

   private void finalizeString() {
      StringBuilder var1 = new StringBuilder(this.writer.length() + this.imports.size() * 100);
      var1.append("import org.jetbrains.annotations.*;\n");
      ArrayList var2 = new ArrayList(this.imports);
      var2.sort(Comparator.naturalOrder());
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         var1.append("import ").append(var4).append(";\n");
      }

      var1.append('\n');
      this.writer.insert(0, var1);
      this.imports(ReflectiveProxyObject.class);
   }

   public String getString() {
      if (this.writer.length() == 0) {
         this.proxify();
      }

      return this.writer.toString();
   }

   public void writeTo(Path var1) {
      if (Files.isDirectory(var1, new LinkOption[0])) {
         var1 = var1.resolve(this.proxifiedClassName + ".java");
      }

      try {
         BufferedWriter var2 = Files.newBufferedWriter(var1, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);

         try {
            var2.write(this.getString());
         } catch (Throwable var6) {
            if (var2 != null) {
               try {
                  var2.close();
               } catch (Throwable var5) {
                  var6.addSuppressed(var5);
               }
            }

            throw var6;
         }

         if (var2 != null) {
            var2.close();
         }

      } catch (IOException var7) {
         throw new IllegalStateException(var7);
      }
   }
}
