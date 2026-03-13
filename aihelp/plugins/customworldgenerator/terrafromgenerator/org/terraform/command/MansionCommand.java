package org.terraform.command;

import java.util.Random;
import java.util.Stack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.terraform.command.contants.TerraCommand;
import org.terraform.coregen.populatordata.PopulatorDataPostGen;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.main.config.TConfig;
import org.terraform.structure.pillager.mansion.MansionJigsawBuilder;

public class MansionCommand extends TerraCommand {
   public MansionCommand(TerraformGeneratorPlugin plugin, String... aliases) {
      super(plugin, aliases);
   }

   @NotNull
   public String getDefaultDescription() {
      return "Mansion Test";
   }

   public boolean canConsoleExec() {
      return false;
   }

   public boolean hasPermission(@NotNull CommandSender sender) {
      return sender.isOp();
   }

   public void execute(CommandSender sender, Stack<String> args) {
      Player p = (Player)sender;
      PopulatorDataPostGen data = new PopulatorDataPostGen(p.getLocation().getChunk());
      int x = p.getLocation().getBlockX();
      int y = p.getLocation().getBlockY();
      int z = p.getLocation().getBlockZ();
      MansionJigsawBuilder builder = new MansionJigsawBuilder(TConfig.c.STRUCTURES_MANSION_SIZE, TConfig.c.STRUCTURES_MANSION_SIZE, data, x, y, z);
      builder.generate(new Random());
      builder.build(new Random());
      p.sendMessage("Complete.");
   }
}
