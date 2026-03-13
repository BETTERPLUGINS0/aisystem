package org.terraform.command;

import java.util.HashMap;
import java.util.Stack;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.terraform.command.contants.TerraCommand;
import org.terraform.main.TerraformGeneratorPlugin;

public class OreDitCommand extends TerraCommand {
   public OreDitCommand(TerraformGeneratorPlugin plugin, String... aliases) {
      super(plugin, aliases);
   }

   @NotNull
   public String getDefaultDescription() {
      return "Calculates the percentages of each ore type within the chunk you're in";
   }

   public boolean canConsoleExec() {
      return false;
   }

   public boolean hasPermission(@NotNull CommandSender sender) {
      return sender.isOp();
   }

   public void execute(CommandSender sender, Stack<String> args) {
      Material[] auditMat = new Material[]{Material.DIORITE, Material.ANDESITE, Material.GRANITE, Material.GRAVEL, Material.COAL_ORE, Material.IRON_ORE, Material.GOLD_ORE, Material.DIAMOND_ORE, Material.LAPIS_ORE, Material.REDSTONE_ORE, Material.EMERALD_ORE, Material.DRIPSTONE_BLOCK, Material.DEEPSLATE, Material.TUFF, Material.COPPER_ORE};
      Player p = (Player)sender;
      Chunk c = p.getLocation().getChunk();
      HashMap<Material, Integer> ores = new HashMap();
      Material[] var7 = auditMat;
      int cz = auditMat.length;

      int var9;
      Material audit;
      for(var9 = 0; var9 < cz; ++var9) {
         audit = var7[var9];
         ores.put(audit, 0);
      }

      p.sendMessage("Sampling surrounding chunk radius of 3...");

      for(int cx = -3; cx <= 3; ++cx) {
         for(cz = -3; cz <= 3; ++cz) {
            Chunk target = p.getWorld().getChunkAt(c.getX() + cx, c.getZ() + cz);

            for(int x = 0; x < 16; ++x) {
               for(int z = 0; z < 16; ++z) {
                  for(int y = (int)p.getLocation().getY(); y > p.getWorld().getMinHeight(); --y) {
                     Material mat = target.getBlock(x, y, z).getType();
                     Material[] var14 = auditMat;
                     int var15 = auditMat.length;

                     for(int var16 = 0; var16 < var15; ++var16) {
                        Material audit = var14[var16];
                        if (mat == audit) {
                           ores.put(audit, (Integer)ores.get(audit) + 1);
                        }
                     }
                  }
               }
            }
         }
      }

      p.sendMessage("-----[Ore Count]-----");
      var7 = auditMat;
      cz = auditMat.length;

      for(var9 = 0; var9 < cz; ++var9) {
         audit = var7[var9];
         String var10001 = String.valueOf(audit);
         p.sendMessage(var10001 + " - " + String.valueOf(ores.get(audit)));
      }

   }
}
