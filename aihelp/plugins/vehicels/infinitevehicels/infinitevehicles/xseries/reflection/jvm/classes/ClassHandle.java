package me.PM2.infinitevehicles.xseries.reflection.jvm.classes;

import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import me.PM2.infinitevehicles.xseries.reflection.ReflectiveHandle;
import me.PM2.infinitevehicles.xseries.reflection.ReflectiveNamespace;
import me.PM2.infinitevehicles.xseries.reflection.constraint.ReflectiveConstraint;
import me.PM2.infinitevehicles.xseries.reflection.constraint.ReflectiveConstraintException;
import me.PM2.infinitevehicles.xseries.reflection.jvm.ConstructorMemberHandle;
import me.PM2.infinitevehicles.xseries.reflection.jvm.EnumMemberHandle;
import me.PM2.infinitevehicles.xseries.reflection.jvm.FieldMemberHandle;
import me.PM2.infinitevehicles.xseries.reflection.jvm.MethodMemberHandle;
import me.PM2.infinitevehicles.xseries.reflection.jvm.NamedReflectiveHandle;
import me.PM2.infinitevehicles.xseries.reflection.jvm.objects.ReflectedObject;
import me.PM2.infinitevehicles.xseries.reflection.jvm.objects.ReflectedObjectHandle;
import me.PM2.infinitevehicles.xseries.reflection.parser.ReflectionParser;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import org.jetbrains.annotations.ApiStatus.Experimental;

public abstract class ClassHandle implements ReflectiveHandle<Class<?>>, NamedReflectiveHandle {
   protected final ReflectiveNamespace namespace;
   private final Map<Class<ReflectiveConstraint>, ReflectiveConstraint> constraints = new IdentityHashMap();

   protected ClassHandle(@NotNull ReflectiveNamespace var1) {
      this.namespace = var1;
      var1.link(this);
   }

   @Experimental
   @Contract(
      value = "_ -> this",
      mutates = "this"
   )
   public ClassHandle constraint(@NotNull ReflectiveConstraint var1) {
      this.constraints.put(var1.getClass(), var1);
      return this;
   }

   protected <T extends Class<?>> T checkConstraints(T var1) {
      Iterator var2 = this.constraints.values().iterator();

      ReflectiveConstraint var3;
      ReflectiveConstraint.Result var4;
      do {
         if (!var2.hasNext()) {
            return var1;
         }

         var3 = (ReflectiveConstraint)var2.next();
         var4 = var3.appliesTo(this, var1);
      } while(var4 == ReflectiveConstraint.Result.MATCHED);

      throw ReflectiveConstraintException.create(var3, var4, this, var1);
   }

   @NotNull
   @Contract("_ -> new")
   public abstract ClassHandle asArray(@Range(from = 1L,to = 2147483647L) int var1);

   @NotNull
   @Contract("-> new")
   public final ClassHandle asArray() {
      return this.asArray(1);
   }

   @Contract(
      pure = true
   )
   public abstract boolean isArray();

   @NotNull
   @Contract("_ -> new")
   public DynamicClassHandle inner(@Language(value = "Java",suffix = "{}") String var1) {
      return this.inner(this.namespace.classHandle(var1));
   }

   @NotNull
   @Contract("_ -> param1")
   public <T extends DynamicClassHandle> T inner(@NotNull T var1) {
      Objects.requireNonNull(var1, "Inner handle is null");
      if (this == var1) {
         throw new IllegalArgumentException("Same instance: " + this);
      } else {
         var1.parent = this;
         this.namespace.link(this);
         return var1;
      }
   }

   @Range(
      from = -1L,
      to = 2147483647L
   )
   public int getDimensionCount() {
      int var1 = -1;
      Class var2 = (Class)this.reflectOrNull();
      if (var2 == null) {
         return var1;
      } else {
         do {
            var2 = var2.getComponentType();
            ++var1;
         } while(var2 != null);

         return var1;
      }
   }

   @Contract(
      pure = true
   )
   public ReflectiveNamespace getNamespace() {
      return this.namespace;
   }

   @Contract(
      value = "-> new",
      pure = true
   )
   public MethodMemberHandle method() {
      return new MethodMemberHandle(this);
   }

   @Contract(
      value = "_ -> new",
      pure = true
   )
   public MethodMemberHandle method(@Language(value = "Java",suffix = ";") String var1) {
      return this.createParser(var1).parseMethod(this.method());
   }

   @Contract(
      value = "-> new",
      pure = true
   )
   public EnumMemberHandle enums() {
      return new EnumMemberHandle(this);
   }

   @Contract(
      value = "-> new",
      pure = true
   )
   public FieldMemberHandle field() {
      return new FieldMemberHandle(this);
   }

   @Contract(
      value = "_ -> new",
      pure = true
   )
   public FieldMemberHandle field(@Language(value = "Java",suffix = ";") String var1) {
      return this.createParser(var1).parseField(this.field());
   }

   @Contract(
      value = "_ -> new",
      pure = true
   )
   public ConstructorMemberHandle constructor(@Language(value = "Java",suffix = ";") String var1) {
      return this.createParser(var1).parseConstructor(this.constructor());
   }

   @Contract(
      value = "-> new",
      pure = true
   )
   public ConstructorMemberHandle constructor() {
      return new ConstructorMemberHandle(this);
   }

   @Contract(
      value = "_ -> new",
      pure = true
   )
   public ConstructorMemberHandle constructor(Class<?>... var1) {
      return this.constructor().parameters(var1);
   }

   @Contract(
      value = "_ -> new",
      pure = true
   )
   public ConstructorMemberHandle constructor(ClassHandle... var1) {
      return this.constructor().parameters(var1);
   }

   @Contract(
      value = "_ -> new",
      pure = true
   )
   private ReflectionParser createParser(@Language("Java") String var1) {
      return (new ReflectionParser(var1)).imports(this.namespace);
   }

   public abstract ClassHandle copy();

   @NotNull
   public ReflectiveHandle<ReflectedObject> jvm() {
      return new ReflectedObjectHandle(() -> {
         return ReflectedObject.of((Class)this.reflect());
      });
   }
}
