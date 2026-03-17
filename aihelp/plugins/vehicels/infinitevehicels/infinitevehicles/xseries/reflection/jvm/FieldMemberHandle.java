package me.PM2.infinitevehicles.xseries.reflection.jvm;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.util.Iterator;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;
import me.PM2.infinitevehicles.xseries.reflection.ReflectiveHandle;
import me.PM2.infinitevehicles.xseries.reflection.XAccessFlag;
import me.PM2.infinitevehicles.xseries.reflection.XReflection;
import me.PM2.infinitevehicles.xseries.reflection.jvm.classes.ClassHandle;
import me.PM2.infinitevehicles.xseries.reflection.jvm.classes.DynamicClassHandle;
import me.PM2.infinitevehicles.xseries.reflection.jvm.objects.ReflectedObject;
import me.PM2.infinitevehicles.xseries.reflection.jvm.objects.ReflectedObjectHandle;
import me.PM2.infinitevehicles.xseries.reflection.minecraft.MinecraftMapping;
import me.PM2.infinitevehicles.xseries.reflection.parser.ReflectionParser;
import org.intellij.lang.annotations.Language;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FieldMemberHandle extends FlaggedNamedMemberHandle {
   public static final MethodHandle MODIFIERS_FIELD;
   public static final DynamicClassHandle VarHandle = XReflection.classHandle().inPackage("java.lang.invoke").named("VarHandle");
   public static final MethodHandle VAR_HANDLE_SET;
   private static final Object MODIFIERS_VAR_HANDLE;
   protected Boolean getter;

   public FieldMemberHandle(ClassHandle var1) {
      super(var1);
   }

   public boolean isGetter() {
      if (this.getter == null) {
         throw new IllegalStateException("Not specified whether the field handle is a getter or setter");
      } else {
         return this.getter;
      }
   }

   public FieldMemberHandle getter() {
      this.getter = true;
      return this;
   }

   public FieldMemberHandle asStatic() {
      super.asStatic();
      return this;
   }

   public FieldMemberHandle asFinal() {
      this.accessFlags.add(XAccessFlag.FINAL);
      return this;
   }

   public FieldMemberHandle makeAccessible() {
      super.makeAccessible();
      return this;
   }

   public FieldMemberHandle setter() {
      this.getter = false;
      return this;
   }

   public FieldMemberHandle returns(Class<?> var1) {
      super.returns(var1);
      return this;
   }

   public FieldMemberHandle returns(ClassHandle var1) {
      super.returns(var1);
      return this;
   }

   public FieldMemberHandle copy() {
      FieldMemberHandle var1 = new FieldMemberHandle(this.clazz);
      var1.returnType = this.returnType;
      var1.getter = this.getter;
      var1.accessFlags.addAll(this.accessFlags);
      var1.names.addAll(this.names);
      return var1;
   }

   public MethodHandle reflect() {
      Objects.requireNonNull(this.getter, "Not specified whether the field handle is a getter or setter");
      Field var1 = this.reflectJvm();
      return this.getter ? this.clazz.getNamespace().getLookup().unreflectGetter(var1) : this.clazz.getNamespace().getLookup().unreflectSetter(var1);
   }

   public boolean exists() {
      try {
         this.reflectJvm();
         return true;
      } catch (ReflectiveOperationException var2) {
         return false;
      }
   }

   public FieldMemberHandle signature(@Language(value = "Java",suffix = ";") String var1) {
      return (new ReflectionParser(var1)).imports(this.clazz.getNamespace()).parseField(this);
   }

   public FieldMemberHandle map(MinecraftMapping var1, @Pattern("\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*") String var2) {
      super.map(var1, var2);
      return this;
   }

   public FieldMemberHandle named(@Pattern("\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*") String... var1) {
      super.named(var1);
      return this;
   }

   protected <T extends AccessibleObject & Member> T handleAccessible(T var1) {
      var1 = super.handleAccessible(var1);
      if (var1 == null) {
         return null;
      } else {
         if (this.getter != null && !this.getter && this.accessFlags.contains(XAccessFlag.FINAL) && this.accessFlags.contains(XAccessFlag.STATIC)) {
            try {
               int var2 = XAccessFlag.FINAL.remove(((Member)var1).getModifiers());
               if (MODIFIERS_VAR_HANDLE != null) {
                  VAR_HANDLE_SET.invoke(MODIFIERS_VAR_HANDLE, var1, var2);
               } else {
                  if (MODIFIERS_FIELD == null) {
                     throw new IllegalAccessException("Current Java version doesn't support modifying final fields. " + this);
                  }

                  MODIFIERS_FIELD.invoke(var1, var2);
               }
            } catch (Throwable var3) {
               throw new ReflectiveOperationException("Cannot unfinal field " + this, var3);
            }
         }

         return var1;
      }
   }

   @Nullable
   public Object get(Object var1) {
      try {
         return this.getter().reflectJvm().get(var1);
      } catch (ReflectiveOperationException var3) {
         throw XReflection.throwCheckedException(var3);
      }
   }

   @Nullable
   public Object getStatic() {
      try {
         return this.asStatic().getter().reflectJvm().get((Object)null);
      } catch (ReflectiveOperationException var2) {
         throw XReflection.throwCheckedException(var2);
      }
   }

   @NotNull
   public ReflectiveHandle<ReflectedObject> jvm() {
      return new ReflectedObjectHandle(() -> {
         return ReflectedObject.of(this.reflectJvm());
      });
   }

   public Field reflectJvm() {
      Objects.requireNonNull(this.returnType, "Return type not specified");
      if (this.names.isEmpty()) {
         throw new IllegalStateException("No names specified");
      } else {
         NoSuchFieldException var1 = null;
         Field var2 = null;
         Class var3 = (Class)this.clazz.reflect();
         Class var4 = this.getReturnType();
         Iterator var5 = this.names.iterator();

         while(var5.hasNext()) {
            String var6 = (String)var5.next();
            if (var2 != null) {
               break;
            }

            try {
               var2 = var3.getDeclaredField(var6);
               if (var2.getType() != var4) {
                  throw new NoSuchFieldException("Field named '" + var6 + "' was found but the types don't match: " + var2 + " != " + this);
               }
            } catch (NoSuchFieldException var8) {
               var2 = null;
               if (var1 == null) {
                  var1 = new NoSuchFieldException("None of the fields were found for " + this);
               }

               var1.addSuppressed(var8);
            }
         }

         if (var2 == null) {
            throw (NoSuchFieldException)XReflection.relativizeSuppressedExceptions(var1);
         } else {
            return (Field)this.handleAccessible(var2);
         }
      }
   }

   public String toString() {
      String var1 = this.getClass().getSimpleName() + '{';
      var1 = var1 + (String)this.accessFlags.stream().map((var0) -> {
         return var0.name().toLowerCase(Locale.ENGLISH);
      }).collect(Collectors.joining(" "));
      if (this.returnType != null) {
         var1 = var1 + this.returnType + " ";
      }

      var1 = var1 + String.join("/", this.names);
      return var1 + '}';
   }

   static {
      VAR_HANDLE_SET = (MethodHandle)VarHandle.method().named("set").returns(Void.TYPE).parameters(Object[].class).reflectOrNull();
      Object var0 = null;
      MethodHandle var1 = null;

      try {
         var1 = (MethodHandle)XReflection.of(Field.class).field().setter().named("modifiers").returns(Integer.TYPE).unreflect();
      } catch (Exception var6) {
      }

      try {
         VarHandle.reflect();
         MethodHandle var2 = XReflection.of(MethodHandles.class).method().named("privateLookupIn").returns(Lookup.class).parameters(Class.class, Lookup.class).reflect();
         MethodHandle var3 = VarHandle.method().named("findVarHandle").returns(Lookup.class).parameters(Class.class, String.class, Class.class).reflect();
         Lookup var4 = var2.invoke(Field.class, MethodHandles.lookup());
         var3.invoke(var4, Field.class, "modifiers", Integer.TYPE);
      } catch (Throwable var5) {
      }

      MODIFIERS_VAR_HANDLE = var0;
      MODIFIERS_FIELD = var1;
   }
}
