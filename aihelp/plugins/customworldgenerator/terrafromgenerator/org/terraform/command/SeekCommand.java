package org.terraform.command;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.Stack;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.terraform.command.contants.InvalidArgumentException;
import org.terraform.command.contants.TerraCommand;
import org.terraform.command.contants.TerraCommandArgument;
import org.terraform.data.MegaChunk;
import org.terraform.data.TerraformWorld;
import org.terraform.main.LangOpt;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.structure.MultiMegaChunkStructurePopulator;
import org.terraform.structure.SingleMegaChunkStructurePopulator;
import org.terraform.structure.StructureLocator;
import org.terraform.structure.StructurePopulator;
import org.terraform.structure.StructureRegistry;
import org.terraform.structure.stronghold.StrongholdPopulator;

public class SeekCommand extends TerraCommand implements Listener {
   public SeekCommand(TerraformGeneratorPlugin plugin, String... aliases) {
      super(plugin, aliases);
      this.parameters.add(new SeekCommand.StructurePopulatorArgument("structureType", true));
   }

   @NotNull
   public String getDefaultDescription() {
      return "Locates the nearest structure to 0,0 and requests a chunk from its coords, forcing its generation from console. Operates on world \"world\"";
   }

   public boolean canConsoleExec() {
      return true;
   }

   public boolean hasPermission(@NotNull CommandSender sender) {
      return sender.isOp();
   }

   public void execute(@NotNull CommandSender sender, @NotNull Stack<String> args) throws InvalidArgumentException {
      ArrayList<Object> params = this.parseArguments(sender, args);
      if (!params.isEmpty()) {
         StructurePopulator spop = (StructurePopulator)params.get(0);
         if (!spop.isEnabled() && !(spop instanceof StrongholdPopulator)) {
            sender.sendMessage(LangOpt.COMMAND_LOCATE_STRUCTURE_NOT_ENABLED.parse());
         } else {
            World w = (World)Objects.requireNonNull(Bukkit.getWorld("world"));
            if (spop instanceof StrongholdPopulator) {
               int[] coords = ((StrongholdPopulator)spop).getNearestFeature(TerraformWorld.get((World)Objects.requireNonNull(Bukkit.getWorld("world"))), 0, 0);
               this.syncSendMessage(LangOpt.COMMAND_LOCATE_LOCATE_COORDS.parse("%x%", coords[0].makeConcatWithConstants<invokedynamic>(coords[0]), "%z%", coords[1].makeConcatWithConstants<invokedynamic>(coords[1])));
            } else {
               if (spop instanceof SingleMegaChunkStructurePopulator) {
                  this.generateSingleMegaChunkStructure(w, (SingleMegaChunkStructurePopulator)spop);
               } else {
                  this.generateMultiMegaChunkStructure(w, (MultiMegaChunkStructurePopulator)spop);
               }

            }
         }
      } else {
         sender.sendMessage(LangOpt.COMMAND_LOCATE_LIST_HEADER.parse());
         StructurePopulator[] var4 = StructureRegistry.getAllPopulators();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            StructurePopulator spop = var4[var6];
            sender.sendMessage(LangOpt.COMMAND_LOCATE_LIST_ENTRY.parse("%entry%", spop.getClass().getSimpleName().replace("Populator", "")));
         }

         sender.sendMessage(LangOpt.COMMAND_LOCATE_LIST_ENTRY.parse("%entry%", "Stronghold"));
      }
   }

   private void generateMultiMegaChunkStructure(@NotNull final World w, @NotNull final MultiMegaChunkStructurePopulator populator) {
      final MegaChunk center = new MegaChunk(0, 0, 0);
      final TerraformWorld tw = TerraformWorld.get(w);
      Bukkit.getConsoleSender().sendMessage(LangOpt.COMMAND_LOCATE_SEARCHING.parse());
      final long startTime = System.currentTimeMillis();
      BukkitRunnable runnable = new BukkitRunnable() {
         public void run() {
            int[] loc = StructureLocator.locateMultiMegaChunkStructure(tw, center, populator, -1);
            long timeTaken = System.currentTimeMillis() - startTime;
            SeekCommand.this.syncSendMessage(LangOpt.COMMAND_LOCATE_COMPLETED_TASK.parse("%time%", timeTaken.makeConcatWithConstants<invokedynamic>(timeTaken)));
            if (loc != null) {
               SeekCommand var10000 = SeekCommand.this;
               String var10001 = String.valueOf(ChatColor.GREEN);
               var10000.syncSendMessage(var10001 + "[" + populator.getClass().getSimpleName() + "] " + LangOpt.COMMAND_LOCATE_LOCATE_COORDS.parse("%x%", loc[0].makeConcatWithConstants<invokedynamic>(loc[0]), "%z%", loc[1].makeConcatWithConstants<invokedynamic>(loc[1])));
               w.getChunkAt(new Location(w, (double)loc[0], 0.0D, (double)loc[1]));
            } else {
               SeekCommand.this.syncSendMessage(String.valueOf(ChatColor.RED) + "Failed to find structure. Somehow.");
            }

         }
      };
      runnable.runTaskAsynchronously(this.plugin);
   }

   private void generateSingleMegaChunkStructure(@NotNull final World w, @NotNull final SingleMegaChunkStructurePopulator populator) {
      final MegaChunk center = new MegaChunk(0, 0, 0);
      final TerraformWorld tw = TerraformWorld.get(w);
      Bukkit.getConsoleSender().sendMessage(LangOpt.COMMAND_LOCATE_SEARCHING.parse());
      final long startTime = System.currentTimeMillis();
      BukkitRunnable runnable = new BukkitRunnable() {
         public void run() {
            int[] loc = StructureLocator.locateSingleMegaChunkStructure(tw, center, populator, -1);
            long timeTaken = System.currentTimeMillis() - startTime;
            SeekCommand.this.syncSendMessage(LangOpt.COMMAND_LOCATE_COMPLETED_TASK.parse("%time%", timeTaken.makeConcatWithConstants<invokedynamic>(timeTaken)));
            if (loc != null) {
               SeekCommand var10000 = SeekCommand.this;
               String var10001 = String.valueOf(ChatColor.GREEN);
               var10000.syncSendMessage(var10001 + "[" + populator.getClass().getSimpleName() + "] " + LangOpt.COMMAND_LOCATE_LOCATE_COORDS.parse("%x%", loc[0].makeConcatWithConstants<invokedynamic>(loc[0]), "%z%", loc[1].makeConcatWithConstants<invokedynamic>(loc[1])));
               w.getChunkAt(new Location(w, (double)loc[0], 0.0D, (double)loc[1]));
            } else {
               SeekCommand.this.syncSendMessage(String.valueOf(ChatColor.RED) + "Failed to find structure. Somehow.");
            }

         }
      };
      runnable.runTaskAsynchronously(this.plugin);
   }

   private void syncSendMessage(String message) {
      TerraformGeneratorPlugin.logger.info("[Seek Command] " + message);
   }

   public static class StructurePopulatorArgument extends TerraCommandArgument<StructurePopulator> {
      public StructurePopulatorArgument(String name, boolean isOptional) {
         super(name, isOptional);
      }

      public StructurePopulator parse(CommandSender arg0, @NotNull String arg1) {
         if (!arg1.equalsIgnoreCase("stronghold") && !arg1.equalsIgnoreCase("strongholdpopulator")) {
            StructurePopulator[] var3 = StructureRegistry.getAllPopulators();
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               StructurePopulator spop = var3[var5];
               if (spop.getClass().getSimpleName().equalsIgnoreCase(arg1) || spop.getClass().getSimpleName().equalsIgnoreCase(arg1 + "populator")) {
                  return spop;
               }
            }

            return null;
         } else {
            return new StrongholdPopulator();
         }
      }

      @NotNull
      public String validate(CommandSender arg0, @NotNull String arg1) {
         return this.parse(arg0, arg1) != null ? "" : "Structure type does not exist";
      }

      @NotNull
      public ArrayList<String> getTabOptions(@NotNull String[] args) {
         if (args.length != 2) {
            return new ArrayList();
         } else {
            ArrayList<String> values = new ArrayList();
            StructurePopulator[] var3 = StructureRegistry.getAllPopulators();
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               StructurePopulator spop = var3[var5];
               if (spop.getClass().getSimpleName().toUpperCase(Locale.ENGLISH).startsWith(args[1].toUpperCase(Locale.ENGLISH))) {
                  values.add(spop.getClass().getSimpleName());
               }
            }

            return values;
         }
      }
   }
}
