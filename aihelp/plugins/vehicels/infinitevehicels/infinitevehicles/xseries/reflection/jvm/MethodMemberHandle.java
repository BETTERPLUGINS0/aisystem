package me.PM2.infinitevehicles.xseries.reflection.jvm;

import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;
import me.PM2.infinitevehicles.xseries.reflection.ReflectiveHandle;
import me.PM2.infinitevehicles.xseries.reflection.XAccessFlag;
import me.PM2.infinitevehicles.xseries.reflection.XReflection;
import me.PM2.infinitevehicles.xseries.reflection.jvm.classes.ClassHandle;
import me.PM2.infinitevehicles.xseries.reflection.jvm.objects.ReflectedObject;
import me.PM2.infinitevehicles.xseries.reflection.jvm.objects.ReflectedObjectHandle;
import me.PM2.infinitevehicles.xseries.reflection.minecraft.MinecraftMapping;
import me.PM2.infinitevehicles.xseries.reflection.parser.ReflectionParser;
import org.intellij.lang.annotations.Language;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus.Experimental;

public class MethodMemberHandle extends FlaggedNamedMemberHandle {
   protected ClassHandle[] parameterTypes = new ClassHandle[0];

   public MethodMemberHandle(ClassHandle var1) {
      super(var1);
   }

   public MethodMemberHandle parameters(ClassHandle... var1) {
      this.parameterTypes = var1;
      return this;
   }

   public MethodMemberHandle returns(Class<?> var1) {
      super.returns(var1);
      return this;
   }

   public MethodMemberHandle returns(ClassHandle var1) {
      super.returns(var1);
      return this;
   }

   public MethodMemberHandle asStatic() {
      super.asStatic();
      return this;
   }

   public MethodMemberHandle parameters(Class<?>... var1) {
      this.parameterTypes = (ClassHandle[])Arrays.stream(var1).map(XReflection::of).toArray((var0) -> {
         return new ClassHandle[var0];
      });
      return this;
   }

   public MethodMemberHandle signature(@Language(value = "Java",suffix = ";") String var1) {
      return (new ReflectionParser(var1)).imports(this.clazz.getNamespace()).parseMethod(this);
   }

   public MethodMemberHandle map(MinecraftMapping var1, @Pattern("\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*") String var2) {
      super.map(var1, var2);
      return this;
   }

   public MethodMemberHandle named(@Pattern("\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*") String... var1) {
      super.named(var1);
      return this;
   }

   public MethodType getMethodType() {
      return MethodType.methodType(this.getReturnType(), FlaggedNamedMemberHandle.getParameters(this, this.parameterTypes));
   }

   @Experimental
   public CallSite toLambda(Class<?> var1, String var2) {
      MethodType var3 = this.getMethodType();
      MethodType var4;
      if (this.accessFlags.contains(XAccessFlag.STATIC)) {
         var4 = MethodType.methodType(var1);
      } else {
         var4 = MethodType.methodType(var1, (Class)this.clazz.reflect());
      }

      return LambdaMetafactory.metafactory(this.clazz.getNamespace().getLookup(), var2, var4, var3, this.reflect(), var3);
   }

   public MethodHandle reflect() {
      return this.clazz.getNamespace().getLookup().unreflect(this.reflectJvm());
   }

   @NotNull
   public ReflectiveHandle<ReflectedObject> jvm() {
      return new ReflectedObjectHandle(() -> {
         return ReflectedObject.of(this.reflectJvm());
      });
   }

   public Method reflectJvm() {
      Objects.requireNonNull(this.returnType, "Return type not specified");
      if (this.names.isEmpty()) {
         throw new IllegalStateException("No names specified");
      } else {
         NoSuchMethodException var1 = null;
         Method var2 = null;
         Class var3 = (Class)this.clazz.reflect();
         Class[] var4 = FlaggedNamedMemberHandle.getParameters(this, this.parameterTypes);
         Class var5 = this.getReturnType();
         Iterator var6 = this.names.iterator();

         while(var6.hasNext()) {
            String var7 = (String)var6.next();
            if (var2 != null) {
               break;
            }

            try {
               var2 = var3.getDeclaredMethod(var7, var4);
               if (var2.getReturnType() != var5) {
                  throw new NoSuchMethodException("Method named '" + var7 + "' was found but the return types don't match: " + this.returnType + " != " + var2.getReturnType());
               }
            } catch (NoSuchMethodException var12) {
               try {
                  var2 = var3.getMethod(var7, var4);
                  if (var2.getReturnType() != var5) {
                     throw new NoSuchMethodException("Method named '" + var7 + "' was found but the return types don't match: " + this.returnType + " != " + var2.getReturnType());
                  }
               } catch (NoSuchMethodException var11) {
                  var2 = null;
                  if (var1 == null) {
                     var1 = new NoSuchMethodException("None of the methods were found for " + this);
                  }

                  var1.addSuppressed(var11);
               }
            }
         }

         if (var2 == null) {
            throw (NoSuchMethodException)XReflection.relativizeSuppressedExceptions(var1);
         } else {
            return (Method)this.handleAccessible(var2);
         }
      }
   }

   public MethodMemberHandle copy() {
      MethodMemberHandle var1 = new MethodMemberHandle(this.clazz);
      var1.returnType = this.returnType;
      var1.parameterTypes = this.parameterTypes;
      var1.accessFlags.addAll(this.accessFlags);
      var1.names.addAll(this.names);
      return var1;
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
      var1 = var1 + '(' + (String)Arrays.stream(this.parameterTypes).map(Object::toString).collect(Collectors.joining(", ")) + ')';
      return var1 + '}';
   }
}
