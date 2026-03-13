package org.terraform.command.contants;

import java.util.ArrayList;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class TerraCommandArgument<T> {
   private String name;
   private boolean isOptional;

   public TerraCommandArgument(String name, boolean isOptional) {
      this.name = name;
      this.isOptional = isOptional;
   }

   @Nullable
   public abstract T parse(CommandSender var1, String var2);

   public abstract String validate(CommandSender var1, String var2);

   @NotNull
   public ArrayList<String> getTabOptions(String[] args) {
      return new ArrayList();
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public boolean isOptional() {
      return this.isOptional;
   }

   public void setOptional(boolean isOptional) {
      this.isOptional = isOptional;
   }
}
