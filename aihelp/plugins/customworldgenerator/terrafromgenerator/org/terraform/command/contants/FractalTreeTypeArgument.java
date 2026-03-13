package org.terraform.command.contants;

import java.util.ArrayList;
import java.util.Locale;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.terraform.tree.FractalTypes;

public class FractalTreeTypeArgument extends TerraCommandArgument<FractalTypes.Tree> {
   public FractalTreeTypeArgument(String name, boolean isOptional) {
      super(name, isOptional);
   }

   @NotNull
   public FractalTypes.Tree parse(CommandSender sender, @NotNull String value) {
      return FractalTypes.Tree.valueOf(value.toUpperCase(Locale.ENGLISH));
   }

   @NotNull
   public String validate(CommandSender sender, @NotNull String value) {
      try {
         this.parse(sender, value);
         return "";
      } catch (IllegalArgumentException var4) {
         return "Tree type does not exist!";
      }
   }

   @NotNull
   public ArrayList<String> getTabOptions(@NotNull String[] args) {
      if (args.length != 2) {
         return new ArrayList();
      } else {
         ArrayList<String> values = new ArrayList();
         FractalTypes.Tree[] var3 = FractalTypes.Tree.values();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            FractalTypes.Tree type = var3[var5];
            if (type.toString().startsWith(args[1].toUpperCase(Locale.ENGLISH))) {
               values.add(type.toString());
            }
         }

         return values;
      }
   }
}
