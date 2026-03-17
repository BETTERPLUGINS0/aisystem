package me.PM2.infinitevehicles.xseries.reflection.jvm;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import me.PM2.infinitevehicles.xseries.reflection.jvm.classes.ClassHandle;
import me.PM2.infinitevehicles.xseries.reflection.minecraft.MinecraftMapping;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;

public abstract class NamedMemberHandle extends MemberHandle implements NameableReflectiveHandle {
   protected final Set<String> names = new HashSet(5);

   @NotNull
   public Set<String> getPossibleNames() {
      return this.names;
   }

   protected NamedMemberHandle(ClassHandle var1) {
      super(var1);
   }

   public NamedMemberHandle map(MinecraftMapping var1, @Pattern("\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*") String var2) {
      this.names.add(var2);
      return this;
   }

   public NamedMemberHandle named(@Pattern("\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*") String... var1) {
      this.names.addAll(Arrays.asList(var1));
      return this;
   }

   public abstract NamedMemberHandle copy();
}
