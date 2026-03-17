package me.PM2.infinitevehicles.xseries.reflection.jvm.classes;

import com.google.common.base.Strings;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import me.PM2.infinitevehicles.xseries.reflection.ReflectiveNamespace;
import me.PM2.infinitevehicles.xseries.reflection.XReflection;
import me.PM2.infinitevehicles.xseries.reflection.jvm.NameableReflectiveHandle;
import me.PM2.infinitevehicles.xseries.reflection.minecraft.MinecraftMapping;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;

public class DynamicClassHandle extends ClassHandle implements NameableReflectiveHandle {
   protected ClassHandle parent;
   protected String packageName;
   protected final Set<String> classNames = new HashSet(5);
   protected int array;

   public DynamicClassHandle(ReflectiveNamespace var1) {
      super(var1);
   }

   public DynamicClassHandle inPackage(@Pattern("(\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*\\.)*\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*") @NotNull String var1) {
      Objects.requireNonNull(var1, "Null package name");
      this.packageName = var1;
      return this;
   }

   public DynamicClassHandle inPackage(@NotNull PackageHandle var1) {
      return this.inPackage(var1, "");
   }

   public DynamicClassHandle inPackage(@NotNull PackageHandle var1, @Pattern("(\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*\\.)*\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*") @NotNull String var2) {
      Objects.requireNonNull(var1, "Null package handle type");
      Objects.requireNonNull(var2, "Null package handle name");
      if (this.parent != null) {
         throw new IllegalStateException("Cannot change package of an inner class: " + var1 + " -> " + var2);
      } else {
         this.packageName = var1.getPackage(var2);
         return this;
      }
   }

   public DynamicClassHandle map(MinecraftMapping var1, String var2) {
      return this.named(var2);
   }

   public DynamicClassHandle named(@Pattern("\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*") @NotNull String... var1) {
      Objects.requireNonNull(var1);
      Iterator var2 = this.classNames.iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         Objects.requireNonNull(var3, () -> {
            return "Cannot add null class name from: " + Arrays.toString(var1) + " to " + this;
         });
      }

      this.classNames.addAll(Arrays.asList(var1));
      return this;
   }

   public String[] reflectClassNames() {
      if (this.parent == null) {
         Objects.requireNonNull(this.packageName, "Package name is null");
      }

      String[] var1 = new String[this.classNames.size()];
      Class var2 = this.parent == null ? null : (Class)XReflection.of((Class)this.parent.unreflect()).asArray(0).unreflect();
      int var3 = 0;

      String var6;
      for(Iterator var4 = this.classNames.iterator(); var4.hasNext(); var1[var3++] = var6) {
         String var5 = (String)var4.next();
         if (var2 == null) {
            var6 = this.packageName + '.' + var5;
         } else {
            var6 = var2.getName() + '$' + var5;
         }

         if (this.array != 0) {
            var6 = Strings.repeat("[", this.array) + 'L' + var6 + ';';
         }
      }

      return var1;
   }

   public DynamicClassHandle copy() {
      DynamicClassHandle var1 = new DynamicClassHandle(this.namespace);
      var1.array = this.array;
      var1.parent = this.parent;
      var1.packageName = this.packageName;
      var1.classNames.addAll(this.classNames);
      return var1;
   }

   public Class<?> reflect() {
      String[] var1 = this.reflectClassNames();
      if (var1.length == 0) {
         throw new IllegalStateException("No class name specified for " + this);
      } else {
         ClassNotFoundException var2 = null;
         String[] var3 = var1;
         int var4 = var1.length;
         int var5 = 0;

         while(var5 < var4) {
            String var6 = var3[var5];

            try {
               return this.checkConstraints(Class.forName(var6));
            } catch (ClassNotFoundException var8) {
               if (var2 == null) {
                  var2 = new ClassNotFoundException("None of the classes were found");
               }

               var2.addSuppressed(var8);
               ++var5;
            }
         }

         throw (ClassNotFoundException)XReflection.relativizeSuppressedExceptions(var2);
      }
   }

   public DynamicClassHandle asArray(int var1) {
      if (var1 < 0) {
         throw new IllegalArgumentException("Array dimension cannot be negative: " + var1);
      } else {
         this.array = var1;
         return this;
      }
   }

   public boolean isArray() {
      return this.array > 0;
   }

   public Set<String> getPossibleNames() {
      return this.classNames;
   }

   public String toString() {
      return this.getClass().getSimpleName() + '{' + (this.parent == null ? "" : this.parent + " -> ") + (this.parent == null ? this.packageName : (this.packageName == null ? "" : this.packageName)) + '(' + String.join("|", this.classNames) + ')' + (this.array == 0 ? "" : "[" + this.array + ']') + " }";
   }
}
