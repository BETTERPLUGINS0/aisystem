package me.PM2.infinitevehicles.xseries.reflection.jvm;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;
import me.PM2.infinitevehicles.xseries.reflection.ReflectiveHandle;
import me.PM2.infinitevehicles.xseries.reflection.XAccessFlag;
import me.PM2.infinitevehicles.xseries.reflection.XReflection;
import me.PM2.infinitevehicles.xseries.reflection.jvm.classes.ClassHandle;
import me.PM2.infinitevehicles.xseries.reflection.jvm.objects.ReflectedObject;
import me.PM2.infinitevehicles.xseries.reflection.jvm.objects.ReflectedObjectHandle;
import me.PM2.infinitevehicles.xseries.reflection.parser.ReflectionParser;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus.Internal;

public class ConstructorMemberHandle extends MemberHandle {
   protected ClassHandle[] parameterTypes = new ClassHandle[0];

   public ConstructorMemberHandle(ClassHandle var1) {
      super(var1);
   }

   @Internal
   public ClassHandle[] getParameterTypes() {
      return this.parameterTypes;
   }

   public ConstructorMemberHandle parameters(Class<?>... var1) {
      this.parameterTypes = (ClassHandle[])Arrays.stream(var1).map(XReflection::of).toArray((var0) -> {
         return new ClassHandle[var0];
      });
      return this;
   }

   public ConstructorMemberHandle parameters(ClassHandle... var1) {
      this.parameterTypes = var1;
      return this;
   }

   public MethodHandle reflect() {
      if (this.accessFlags.contains(XAccessFlag.FINAL)) {
         throw new UnsupportedOperationException("Constructor cannot be final: " + this);
      } else if (this.accessFlags.contains(XAccessFlag.PRIVATE)) {
         return this.clazz.getNamespace().getLookup().unreflectConstructor(this.reflectJvm());
      } else {
         Class[] var1 = FlaggedNamedMemberHandle.getParameters(this, this.parameterTypes);
         return this.clazz.getNamespace().getLookup().findConstructor((Class)this.clazz.unreflect(), MethodType.methodType(Void.TYPE, var1));
      }
   }

   public ConstructorMemberHandle signature(@Language(value = "Java",suffix = ";") String var1) {
      return (new ReflectionParser(var1)).imports(this.clazz.getNamespace()).parseConstructor(this);
   }

   @NotNull
   public ReflectiveHandle<ReflectedObject> jvm() {
      return new ReflectedObjectHandle(() -> {
         return ReflectedObject.of(this.reflectJvm());
      });
   }

   public Constructor<?> reflectJvm() {
      Class[] var1 = FlaggedNamedMemberHandle.getParameters(this, this.parameterTypes);
      return (Constructor)this.handleAccessible(((Class)this.clazz.unreflect()).getDeclaredConstructor(var1));
   }

   public ConstructorMemberHandle copy() {
      ConstructorMemberHandle var1 = new ConstructorMemberHandle(this.clazz);
      var1.parameterTypes = this.parameterTypes;
      var1.accessFlags.addAll(this.accessFlags);
      return var1;
   }

   public String toString() {
      String var1 = this.getClass().getSimpleName() + '{';
      var1 = var1 + (String)this.accessFlags.stream().map((var0) -> {
         return var0.name().toLowerCase(Locale.ENGLISH);
      }).collect(Collectors.joining(" "));
      var1 = var1 + this.clazz.toString() + ' ';
      var1 = var1 + '(' + (String)Arrays.stream(this.parameterTypes).map(Object::toString).collect(Collectors.joining(", ")) + ')';
      return var1 + '}';
   }
}
