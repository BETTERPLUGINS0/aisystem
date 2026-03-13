package org.terraform.command.contants;

import java.io.FileNotFoundException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.terraform.data.SimpleBlock;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.schematic.TerraSchematic;

public class SchematicArgument extends TerraCommandArgument<TerraSchematic> {
   public SchematicArgument(String name, boolean isOptional) {
      super(name, isOptional);
   }

   public TerraSchematic parse(@NotNull CommandSender sender, String value) {
      try {
         return TerraSchematic.load(value, new SimpleBlock(((Player)sender).getLocation()));
      } catch (FileNotFoundException var4) {
         TerraformGeneratorPlugin.logger.stackTrace(var4);
         return null;
      }
   }

   @NotNull
   public String validate(@NotNull CommandSender sender, String value) {
      try {
         TerraSchematic.load(value, new SimpleBlock(((Player)sender).getLocation()));
         return "";
      } catch (Throwable var4) {
         TerraformGeneratorPlugin.logger.stackTrace(var4);
         return "Problem loading schematic. Check console for error";
      }
   }
}
