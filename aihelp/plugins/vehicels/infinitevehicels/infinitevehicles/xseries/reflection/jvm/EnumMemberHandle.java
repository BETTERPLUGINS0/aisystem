package me.PM2.infinitevehicles.xseries.reflection.jvm;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;
import java.util.Iterator;
import me.PM2.infinitevehicles.xseries.reflection.ReflectiveHandle;
import me.PM2.infinitevehicles.xseries.reflection.XReflection;
import me.PM2.infinitevehicles.xseries.reflection.jvm.classes.ClassHandle;
import me.PM2.infinitevehicles.xseries.reflection.jvm.objects.ReflectedObject;
import me.PM2.infinitevehicles.xseries.reflection.jvm.objects.ReflectedObjectHandle;
import me.PM2.infinitevehicles.xseries.reflection.minecraft.MinecraftMapping;
import org.intellij.lang.annotations.Language;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Obsolete;

public class EnumMemberHandle extends NamedMemberHandle {
   public EnumMemberHandle(ClassHandle var1) {
      super(var1);
   }

   public EnumMemberHandle map(MinecraftMapping var1, @Pattern("\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*") String var2) {
      super.map(var1, var2);
      return this;
   }

   public EnumMemberHandle named(@Pattern("\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*") String... var1) {
      super.named(var1);
      return this;
   }

   @Obsolete
   public MemberHandle signature(@Language(value = "Java",suffix = ";") String var1) {
      throw new UnsupportedOperationException();
   }

   @NotNull
   @Obsolete
   public MethodHandle unreflect() {
      return (MethodHandle)super.unreflect();
   }

   @Obsolete
   @Nullable
   public MethodHandle reflectOrNull() {
      return (MethodHandle)super.reflectOrNull();
   }

   @Obsolete
   @NotNull
   public ReflectiveHandle<ReflectedObject> jvm() {
      return new ReflectedObjectHandle(() -> {
         return ReflectedObject.of(this.reflectJvm());
      });
   }

   @Obsolete
   public MethodHandle reflect() {
      Field var1 = this.reflectJvm();
      return this.clazz.getNamespace().getLookup().unreflectGetter(var1);
   }

   @Nullable
   public Object getEnumConstant() {
      try {
         return this.reflectJvm().get((Object)null);
      } catch (ReflectiveOperationException var2) {
         throw XReflection.throwCheckedException(var2);
      }
   }

   public Field reflectJvm() {
      if (this.names.isEmpty()) {
         throw new IllegalStateException("No enum names specified");
      } else {
         NoSuchFieldException var1 = null;
         Field var2 = null;
         Class var3 = (Class)this.clazz.reflect();
         if (!var3.isEnum()) {
            throw new IllegalStateException("Class is not an enum: " + this.clazz + " -> " + var3);
         } else {
            Iterator var4 = this.names.iterator();

            while(var4.hasNext()) {
               String var5 = (String)var4.next();
               if (var2 != null) {
                  break;
               }

               try {
                  var2 = var3.getDeclaredField(var5);
                  if (!var2.isEnumConstant()) {
                     throw new NoSuchFieldException("Field named '" + var5 + "' was found but it's not an enum constant " + this);
                  }
               } catch (NoSuchFieldException var7) {
                  var2 = null;
                  if (var1 == null) {
                     var1 = new NoSuchFieldException("None of the enums were found for " + this);
                  }

                  var1.addSuppressed(var7);
               }
            }

            if (var2 == null) {
               throw (NoSuchFieldException)XReflection.relativizeSuppressedExceptions(var1);
            } else {
               return (Field)this.handleAccessible(var2);
            }
         }
      }
   }

   @Obsolete
   public EnumMemberHandle copy() {
      throw new UnsupportedOperationException();
   }
}
