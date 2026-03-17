package me.PM2.infinitevehicles.xseries.reflection.jvm.objects;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import me.PM2.infinitevehicles.xseries.reflection.XAccessFlag;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.jetbrains.annotations.ApiStatus.Experimental;

@Experimental
public interface ReflectedObject extends AnnotatedElement {
   @NotNull
   @Contract(
      value = "_ -> new",
      pure = true
   )
   static ReflectedObject of(@NotNull Class<?> clazz) {
      return new ReflectedObjectClass((Class)Objects.requireNonNull(clazz));
   }

   @NotNull
   @Contract(
      value = "_ -> new",
      pure = true
   )
   static ReflectedObject of(@NotNull Constructor<?> constructor) {
      return new ReflectedObjectConstructor((Constructor)Objects.requireNonNull(constructor));
   }

   @NotNull
   @Contract(
      value = "_ -> new",
      pure = true
   )
   static ReflectedObject of(@NotNull Method method) {
      return new ReflectedObjectMethod((Method)Objects.requireNonNull(method));
   }

   @NotNull
   @Contract(
      value = "_ -> new",
      pure = true
   )
   static ReflectedObject of(@NotNull Field field) {
      return new ReflectedObjectField((Field)Objects.requireNonNull(field));
   }

   @NotNull
   @Contract(
      pure = true
   )
   Object unreflect();

   @NotNull
   @Contract(
      pure = true
   )
   ReflectedObject.Type type();

   @NotNull
   @Contract(
      pure = true
   )
   String name();

   @Nullable
   @Contract(
      pure = true
   )
   Class<?> getDeclaringClass();

   @Contract(
      pure = true
   )
   int getModifiers();

   @NotNull
   @Contract(
      value = "-> new",
      pure = true
   )
   @Unmodifiable
   default Set<XAccessFlag> accessFlags() {
      return Collections.unmodifiableSet(XAccessFlag.of(this.getModifiers()));
   }

   public static enum Type {
      CLASS,
      CONSTRUCTOR,
      METHOD,
      FIELD;

      // $FF: synthetic method
      private static ReflectedObject.Type[] $values() {
         return new ReflectedObject.Type[]{CLASS, CONSTRUCTOR, METHOD, FIELD};
      }
   }
}
