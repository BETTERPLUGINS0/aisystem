package me.PM2.infinitevehicles.xseries.reflection.jvm.classes;

import java.util.Collections;
import java.util.Set;
import me.PM2.infinitevehicles.xseries.reflection.ReflectiveNamespace;
import org.jetbrains.annotations.NotNull;

public class UnknownClassHandle extends ClassHandle {
   private final String name;

   public UnknownClassHandle(ReflectiveNamespace var1, String var2) {
      super(var1);
      this.name = var2;
   }

   public Set<String> getPossibleNames() {
      return Collections.singleton(this.name);
   }

   public UnknownClassHandle asArray(int var1) {
      return new UnknownClassHandle(this.namespace, this.name + "[]");
   }

   public boolean isArray() {
      return this.name.endsWith("[]");
   }

   public UnknownClassHandle copy() {
      return new UnknownClassHandle(this.namespace, this.name);
   }

   @NotNull
   public Class<?> reflect() {
      throw new ReflectiveOperationException("Unknown class: " + this.name);
   }

   public String toString() {
      return "UnknownClassHandle(" + this.name + ')';
   }
}
