package org.terraform.command.contants;

import java.io.File;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.schematic.TerraSchematic;

public class FilenameArgument extends TerraCommandArgument<String> {
   public FilenameArgument(String name, boolean isOptional) {
      super(name, isOptional);
   }

   public String parse(CommandSender sender, String value) {
      return value;
   }

   @NotNull
   public String validate(CommandSender sender, @NotNull String value) {
      File schematicFolder = new File(TerraformGeneratorPlugin.get().getDataFolder(), TerraSchematic.SCHEMATIC_FOLDER);
      File file = new File(schematicFolder, value);

      try {
         return !file.getName().endsWith(File.pathSeparator) && file.getCanonicalPath().startsWith(schematicFolder.getCanonicalPath()) ? "" : "Schematic name contained illegal characters (i.e. periods)";
      } catch (Exception var6) {
         return "Schematic name contained illegal characters (i.e. periods)";
      }
   }
}
