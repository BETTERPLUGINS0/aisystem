package org.terraform.command;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Stack;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.biome.BiomeType;
import org.terraform.command.contants.InvalidArgumentException;
import org.terraform.command.contants.TerraCommand;
import org.terraform.command.contants.TerraCommandArgument;
import org.terraform.data.TerraformWorld;
import org.terraform.main.LangOpt;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.utils.GenUtils;
import org.terraform.utils.Vector2f;

public class LocateBiomeCommand extends TerraCommand {
   public LocateBiomeCommand(TerraformGeneratorPlugin plugin, String... aliases) {
      super(plugin, aliases);
      this.parameters.add(new LocateBiomeCommand.LocateBiomeTypeArgument("biomeType", false));
   }

   @NotNull
   public String getDefaultDescription() {
      return "Tries to locate a certain biome";
   }

   public boolean canConsoleExec() {
      return false;
   }

   public boolean hasPermission(@NotNull CommandSender sender) {
      return sender.isOp() || sender.hasPermission("terraformgenerator.locatebiome");
   }

   public void execute(@NotNull CommandSender sender, @NotNull Stack<String> args) throws InvalidArgumentException {
      Player p = (Player)sender;
      if (!args.isEmpty()) {
         try {
            (new LocateBiomeCommand.Task(p.getUniqueId(), TerraformWorld.get(p.getWorld()), p.getLocation().getBlockX(), p.getLocation().getBlockZ(), (BiomeBank)this.parseArguments(sender, args).get(0))).runTaskAsynchronously(TerraformGeneratorPlugin.get());
         } catch (IllegalArgumentException var12) {
            sender.sendMessage(LangOpt.COMMAND_LOCATEBIOME_INVALIDBIOME.parse());
            StringBuilder types = new StringBuilder();
            boolean b = true;
            BiomeBank[] var7 = BiomeBank.values();
            int var8 = var7.length;

            for(int var9 = 0; var9 < var8; ++var9) {
               BiomeBank type = var7[var9];
               ChatColor col = ChatColor.RED;
               if (b) {
                  col = ChatColor.DARK_RED;
               }

               b = !b;
               types.append(col).append(type).append(' ');
            }

            sender.sendMessage(types.toString());
         }
      }

   }

   private void syncSendMessage(UUID uuid, String msg) {
      super.syncSendMessage(uuid, "Locate Biome", msg);
   }

   private void syncSendMessage(UUID uuid, String msg, int x, int y, int z) {
      super.syncSendMessageTP(uuid, "Locate Biome", msg, x, y, z);
   }

   private static class LocateBiomeTypeArgument extends TerraCommandArgument<BiomeBank> {
      public LocateBiomeTypeArgument(String name, boolean isOptional) {
         super(name, isOptional);
      }

      @NotNull
      public BiomeBank parse(CommandSender sender, @NotNull String value) {
         return BiomeBank.valueOf(value.toUpperCase(Locale.ENGLISH));
      }

      @NotNull
      public String validate(CommandSender sender, @NotNull String value) {
         try {
            this.parse(sender, value);
            return "";
         } catch (IllegalArgumentException var4) {
            return "That biome type does not exist!";
         }
      }

      @NotNull
      public ArrayList<String> getTabOptions(@NotNull String[] args) {
         if (args.length != 2) {
            return new ArrayList();
         } else {
            ArrayList<String> values = new ArrayList();
            BiomeBank[] var3 = BiomeBank.values();
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               BiomeBank bank = var3[var5];
               if (bank.name().startsWith(args[1].toUpperCase(Locale.ENGLISH))) {
                  values.add(bank.name());
               }
            }

            return values;
         }
      }
   }

   private class Task extends BukkitRunnable {
      final UUID p;
      final BiomeBank b;
      final TerraformWorld tw;
      final int x;
      final int z;

      public Task(UUID param2, TerraformWorld param3, int param4, int param5, BiomeBank param6) {
         this.p = player;
         this.b = targetBiome;
         this.tw = tw;
         this.x = x;
         this.z = z;
      }

      public void run() {
         Vector2f location;
         if (this.b.getType() != BiomeType.BEACH && this.b.getType() != BiomeType.RIVER) {
            location = GenUtils.locateHeightIndependentBiome(this.tw, this.b, new Vector2f((float)this.x, (float)this.z));
         } else {
            location = GenUtils.locateHeightDependentBiome(this.tw, this.b, new Vector2f((float)this.x, (float)this.z), 5000, 25);
            if (location == null) {
               LocateBiomeCommand.this.syncSendMessage(this.p, LangOpt.COMMAND_LOCATEBIOME_NOT_IN_5000.parse());
            }
         }

         if (location != null) {
            int x = (int)location.x;
            int z = (int)location.y;
            String message = LangOpt.COMMAND_LOCATE_LOCATE_COORDS.parse("%x%", x.makeConcatWithConstants<invokedynamic>(x), "%z%", z.makeConcatWithConstants<invokedynamic>(z));
            LocateBiomeCommand.this.syncSendMessage(this.p, message, x, LocateBiomeCommand.this.getHighestY(this.tw, x, z), z);
         } else {
            LocateBiomeCommand.this.syncSendMessage(this.p, LangOpt.COMMAND_LOCATEBIOME_DISABLED.parse());
         }

      }
   }
}
