package me.PM2.infinitevehicles.xseries.reflection.parser;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import me.PM2.infinitevehicles.xseries.reflection.ReflectiveHandle;
import me.PM2.infinitevehicles.xseries.reflection.ReflectiveNamespace;
import me.PM2.infinitevehicles.xseries.reflection.XReflection;
import me.PM2.infinitevehicles.xseries.reflection.jvm.ConstructorMemberHandle;
import me.PM2.infinitevehicles.xseries.reflection.jvm.FieldMemberHandle;
import me.PM2.infinitevehicles.xseries.reflection.jvm.FlaggedNamedMemberHandle;
import me.PM2.infinitevehicles.xseries.reflection.jvm.MemberHandle;
import me.PM2.infinitevehicles.xseries.reflection.jvm.MethodMemberHandle;
import me.PM2.infinitevehicles.xseries.reflection.jvm.classes.ClassHandle;
import me.PM2.infinitevehicles.xseries.reflection.jvm.classes.DynamicClassHandle;
import me.PM2.infinitevehicles.xseries.reflection.jvm.classes.PackageHandle;
import me.PM2.infinitevehicles.xseries.reflection.jvm.classes.StaticClassHandle;
import me.PM2.infinitevehicles.xseries.reflection.jvm.classes.UnknownClassHandle;
import me.PM2.infinitevehicles.xseries.reflection.minecraft.MinecraftPackage;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public final class ReflectionParser {
   private static final String[] DEFAULT_CHECKED_PACKAGES = new String[]{"java.util", "java.util.function", "java.lang", "java.io"};
   private final String declaration;
   private Pattern pattern;
   private Matcher matcher;
   private ReflectiveNamespace namespace;
   private Map<String, Class<?>> cachedImports;
   private String[] checkedPackages;
   private final Set<ReflectionParser.Flag> flags;
   private static final PackageHandle[] PACKAGE_HANDLES = MinecraftPackage.values();
   @Language("RegExp")
   private static final String GENERIC = "(?:\\s*<\\s*[.\\w<>\\[\\], ]+\\s*>)?";
   @Language("RegExp")
   private static final String ARRAY = "(?:(?:\\[])*)";
   @Language("RegExp")
   private static final String PACKAGE_REGEX = "(?:package\\s+(?<package>(\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*\\.)*\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*)\\s*;\\s*)?";
   @Language("RegExp")
   private static final String CLASS_TYPES = "(?<classType>class|interface|enum|record)";
   @Language("RegExp")
   private static final String PARAMETERS = "\\s*\\(\\s*(?<parameters>[\\w$_,.<?>\\[\\] ]+)?\\s*\\)";
   @Language("RegExp")
   private static final String THROWS = "(?:\\s*throws\\s+(?<throws>(?:" + type((String)null).array(false) + ")(?:\\s*,\\s*" + type((String)null).array(false) + ")*))?";
   @Language("RegExp")
   private static final String END_DECL = "\\s*;?\\s*";
   private static final Pattern CLASS;
   private static final Pattern METHOD;
   private static final Pattern CONSTRUCTOR;
   private static final Pattern FIELD;
   private static final Map<String, Class<?>> PREDEFINED_TYPES;

   public ReflectionParser(@Language("Java") String var1) {
      this.checkedPackages = DEFAULT_CHECKED_PACKAGES;
      this.flags = EnumSet.noneOf(ReflectionParser.Flag.class);
      this.declaration = var1;
   }

   public ReflectionParser checkedPackages(@org.intellij.lang.annotations.Pattern("(\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*\\.)*\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*") String... var1) {
      this.checkedPackages = var1;
      return this;
   }

   private static ReflectionParser.IDHandler id(@NotNull @Language("RegExp") String var0) {
      return new ReflectionParser.IDHandler(var0, false);
   }

   private static ReflectionParser.IDHandler type(@Language("RegExp") String var0) {
      return (new ReflectionParser.IDHandler(var0, true)).generic(true).array(true);
   }

   private ClassHandle[] parseTypes(String[] var1) {
      ClassHandle[] var2 = new ClassHandle[var1.length];

      for(int var3 = 0; var3 < var1.length; ++var3) {
         String var4 = var1[var3];
         var4 = var4.trim().substring(0, var4.lastIndexOf(32)).trim();
         var2[var3] = this.parseType(var4);
      }

      return var2;
   }

   private ClassHandle parseType(String var1) {
      if (this.cachedImports == null && this.namespace != null) {
         this.cachedImports = this.namespace.getImports();
      }

      String var2 = var1;
      var1 = var1.replace(" ", "");
      int var3 = 0;
      if (var1.endsWith("[]")) {
         String var4 = var1.replace("[]", "");
         var3 = (var1.length() - var4.length()) / 2;
         var1 = var4;
      }

      if (var1.endsWith(">")) {
         var1 = var1.substring(0, var1.indexOf(60));
      }

      Class var5 = this.stringToClass(var1);
      if (var5 == null) {
         return new UnknownClassHandle(this.getOrCreateNamespace(), var2 + " -> " + var1);
      } else {
         if (var3 != 0) {
            var5 = (Class)XReflection.of(var5).asArray(var3).unreflect();
         }

         return new StaticClassHandle(this.getOrCreateNamespace(), var5);
      }
   }

   @Nullable
   private Class<?> stringToClass(String var1) {
      Class var2 = null;
      if (!var1.contains(".")) {
         if (this.cachedImports != null) {
            var2 = (Class)this.cachedImports.get(var1);
         }

         if (var2 == null) {
            var2 = (Class)PREDEFINED_TYPES.get(var1);
         }

         if (var2 == null && this.checkedPackages != null) {
            String[] var3 = this.checkedPackages;
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               String var6 = var3[var5];
               boolean var7 = var6.endsWith("$");
               var2 = classNamed(var6 + (var7 ? "" : '.') + var1);
               if (var2 != null) {
                  break;
               }
            }
         }
      }

      if (var2 == null) {
         var2 = classNamed(var1);
      }

      return var2;
   }

   private static Class<?> classNamed(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         return null;
      }
   }

   private ReflectiveNamespace getOrCreateNamespace() {
      return this.namespace == null ? XReflection.namespaced() : this.namespace;
   }

   public ReflectionParser imports(ReflectiveNamespace var1) {
      this.namespace = var1;
      return this;
   }

   private void pattern(Pattern var1, ReflectiveHandle<?> var2) {
      this.pattern = var1;
      this.matcher = var1.matcher(this.declaration);
      this.start(var2);
   }

   public <T extends DynamicClassHandle> T parseClass(T var1) {
      this.pattern(CLASS, var1);
      String var2 = this.group("package");
      if (var2 != null && !var2.isEmpty()) {
         boolean var3 = false;
         PackageHandle[] var4 = PACKAGE_HANDLES;
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            PackageHandle var7 = var4[var6];
            String var8 = var7.packageId().toLowerCase(Locale.ENGLISH);
            if (var2.startsWith(var8)) {
               if (var2.indexOf(46) == -1) {
                  var1.inPackage(var7);
               } else {
                  var1.inPackage(var7, var2.substring(var8.length() + 1));
               }

               var3 = true;
               break;
            }
         }

         if (!var3) {
            var1.inPackage(var2);
         }
      }

      String var9 = this.group("className");
      if (var9.contains("<")) {
         var9 = var9.substring(0, var9.indexOf(60));
      }

      var1.named(var9);
      return var1;
   }

   private void includeInnerClassOf(MemberHandle var1) {
      Class var2 = (Class)var1.getClassHandle().reflectOrNull();
      if (var2 != null) {
         int var3 = DEFAULT_CHECKED_PACKAGES.length + 2;
         this.checkedPackages = (String[])Arrays.copyOf(DEFAULT_CHECKED_PACKAGES, var3);
         this.checkedPackages[var3 - 1] = var2.getName() + '$';
         this.checkedPackages[var3 - 2] = var2.getPackage().getName();
      }
   }

   public <T extends ConstructorMemberHandle> T parseConstructor(T var1) {
      this.includeInnerClassOf(var1);
      this.pattern(CONSTRUCTOR, var1);
      if (this.has("className") && !var1.getClassHandle().getPossibleNames().contains(this.group("className"))) {
         this.error("Wrong class name associated to constructor, possible names: " + var1.getClassHandle().getPossibleNames());
      }

      if (this.has("parameters")) {
         var1.parameters(this.parseTypes(this.group("parameters").split(",")));
      }

      return var1;
   }

   public <T extends MethodMemberHandle> T parseMethod(T var1) {
      this.includeInnerClassOf(var1);
      this.pattern(METHOD, var1);
      var1.named(this.group("methodName").split("\\$"));
      var1.returns(this.parseType(this.group("methodReturnType")));
      if (this.has("parameters")) {
         var1.parameters(this.parseTypes(this.group("parameters").split(",")));
      }

      return var1;
   }

   public <T extends FieldMemberHandle> T parseField(T var1) {
      this.includeInnerClassOf(var1);
      this.pattern(FIELD, var1);
      var1.named(this.group("fieldName").split("\\$"));
      var1.returns(this.parseType(this.group("fieldType")));
      return var1;
   }

   private String group(String var1) {
      return this.matcher.group(var1);
   }

   private boolean has(String var1) {
      String var2 = this.group(var1);
      return var2 != null && !var2.isEmpty();
   }

   private void start(ReflectiveHandle<?> var1) {
      if (!this.matcher.matches()) {
         this.error("Not a " + var1 + " declaration");
      }

      this.parseFlags();
      if (var1 instanceof MemberHandle) {
         MemberHandle var2 = (MemberHandle)var1;
         if (!hasOneOf(this.flags, ReflectionParser.Flag.PUBLIC, ReflectionParser.Flag.PROTECTED, ReflectionParser.Flag.PRIVATE)) {
            Class var3 = (Class)var2.getClassHandle().reflectOrNull();
            if (var3 != null && !var3.isInterface()) {
               var2.makeAccessible();
            }
         } else if (hasOneOf(this.flags, ReflectionParser.Flag.PRIVATE, ReflectionParser.Flag.PROTECTED)) {
            var2.makeAccessible();
         }

         if (var1 instanceof FieldMemberHandle && this.flags.contains(ReflectionParser.Flag.FINAL)) {
            ((FieldMemberHandle)var1).asFinal();
         }

         if (var1 instanceof FlaggedNamedMemberHandle && this.flags.contains(ReflectionParser.Flag.STATIC)) {
            ((FlaggedNamedMemberHandle)var1).asStatic();
         }
      }

   }

   private void parseFlags() {
      if (this.has("flags")) {
         String var1 = this.group("flags");
         String[] var2 = var1.split("\\s+");
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            String var5 = var2[var4];
            if (!this.flags.add(ReflectionParser.Flag.valueOf(var5.toUpperCase(Locale.ENGLISH)))) {
               this.error("Repeated flag: " + var5);
            }
         }

         if (containsDuplicates(this.flags, ReflectionParser.Flag.PUBLIC, ReflectionParser.Flag.PROTECTED, ReflectionParser.Flag.PRIVATE)) {
            this.error("Duplicate visibility flags");
         }

      }
   }

   @SafeVarargs
   private static <T> boolean containsDuplicates(Collection<T> var0, T... var1) {
      boolean var2 = false;
      Object[] var3 = var1;
      int var4 = var1.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Object var6 = var3[var5];
         if (var0.contains(var6)) {
            if (var2) {
               return true;
            }

            var2 = true;
         }
      }

      return false;
   }

   @SafeVarargs
   private static <T> boolean hasOneOf(Collection<T> var0, T... var1) {
      Stream var10000 = Arrays.stream(var1);
      Objects.requireNonNull(var0);
      return var10000.anyMatch(var0::contains);
   }

   private void error(String var1) {
      throw new ReflectionParser.ReflectionParserException(var1 + " in: " + this.declaration + " (RegEx: " + this.pattern.pattern() + "), (Imports: " + this.cachedImports + ')');
   }

   static {
      CLASS = Pattern.compile("(?:package\\s+(?<package>(\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*\\.)*\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*)\\s*;\\s*)?" + ReflectionParser.Flag.FLAGS_REGEX + "(?<classType>class|interface|enum|record)" + "\\s+" + type("className") + "(?:\\(\\))?(?:\\s+extends\\s+" + id("superclasses") + ")?(?:\\s+implements\\s+(?<interfaces>(?:" + type((String)null).array(false) + ")(?:\\s*,\\s*" + type((String)null).array(false) + ")*))?(?:\\s*\\{\\s*})?\\s*");
      METHOD = Pattern.compile(ReflectionParser.Flag.FLAGS_REGEX + type("methodReturnType") + "\\s+" + id("methodName") + "\\s*\\(\\s*(?<parameters>[\\w$_,.<?>\\[\\] ]+)?\\s*\\)" + THROWS + "\\s*;?\\s*");
      CONSTRUCTOR = Pattern.compile(ReflectionParser.Flag.FLAGS_REGEX + "\\s+" + id("className") + "\\s*\\(\\s*(?<parameters>[\\w$_,.<?>\\[\\] ]+)?\\s*\\)" + "\\s*;?\\s*");
      FIELD = Pattern.compile(ReflectionParser.Flag.FLAGS_REGEX + type("fieldType") + "\\s+" + id("fieldName") + "\\s*;?\\s*");
      PREDEFINED_TYPES = new HashMap();
      Arrays.asList(Byte.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE, Boolean.TYPE, Character.TYPE, Void.TYPE, Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, Boolean.class, Character.class, Void.class, Object.class, String.class, CharSequence.class, StringBuilder.class, StringBuffer.class, UUID.class, Optional.class, Map.class, HashMap.class, Date.class, Calendar.class, Duration.class, TimeUnit.class, Path.class, Files.class, ConcurrentHashMap.class, Callable.class, Future.class, CompletableFuture.class, Throwable.class, Error.class, Exception.class, IllegalArgumentException.class, IllegalStateException.class).forEach((var0) -> {
         PREDEFINED_TYPES.put(var0.getSimpleName(), var0);
      });
   }

   private static enum Flag {
      PUBLIC,
      PROTECTED,
      PRIVATE,
      FINAL,
      TRANSIENT,
      ABSTRACT,
      STATIC,
      NATIVE,
      SYNCHRONIZED,
      STRICTFP,
      VOLATILE;

      private static final String FLAGS_REGEX = "(?<flags>(?:(?:" + (String)Arrays.stream(values()).map(Enum::name).map((var0) -> {
         return var0.toLowerCase(Locale.ENGLISH);
      }).collect(Collectors.joining("|")) + ")\\s*)+)?";

      // $FF: synthetic method
      private static ReflectionParser.Flag[] $values() {
         return new ReflectionParser.Flag[]{PUBLIC, PROTECTED, PRIVATE, FINAL, TRANSIENT, ABSTRACT, STATIC, NATIVE, SYNCHRONIZED, STRICTFP, VOLATILE};
      }
   }

   private static final class IDHandler {
      private boolean generic;
      private boolean array;
      private final String groupName;
      private final boolean isFullyQualified;

      private IDHandler(String var1, boolean var2) {
         this.groupName = var1;
         this.isFullyQualified = var2;
      }

      public ReflectionParser.IDHandler generic(boolean var1) {
         this.generic = var1;
         return this;
      }

      public ReflectionParser.IDHandler array(boolean var1) {
         this.array = var1;
         return this;
      }

      public String toString() {
         String var1 = (this.isFullyQualified ? "(\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*\\.)*\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*" : "\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*") + (this.generic ? "(?:\\s*<\\s*[.\\w<>\\[\\], ]+\\s*>)?" : "") + (this.array ? "(?:(?:\\[])*)" : "");
         return this.groupName == null ? "(?:" + var1 + ')' : "(?<" + this.groupName + '>' + var1 + ')';
      }

      // $FF: synthetic method
      IDHandler(String var1, boolean var2, Object var3) {
         this(var1, var2);
      }
   }

   public static final class ReflectionParserException extends RuntimeException {
      public ReflectionParserException(String var1) {
         super(var1);
      }
   }
}
