package org.terraform.command;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Stack;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.terraform.command.contants.InvalidArgumentException;
import org.terraform.command.contants.TerraCommand;
import org.terraform.command.contants.TerraCommandArgument;
import org.terraform.coregen.bukkit.TerraformGenerator;
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

public class LocateCommand extends TerraCommand implements Listener {
   public LocateCommand(@NotNull TerraformGeneratorPlugin plugin, String... aliases) {
      super(plugin, aliases);
      plugin.getServer().getPluginManager().registerEvents(this, plugin);
      this.parameters.add(new LocateCommand.StructurePopulatorArgument("structureType", true));
   }

   @EventHandler
   public void onLocateCommand(@NotNull PlayerCommandPreprocessEvent event) {
      if (event.getPlayer().getWorld().getGenerator() instanceof TerraformGenerator && event.getMessage().startsWith("/locate")) {
         event.getPlayer().sendMessage(LangOpt.COMMAND_LOCATE_NOVANILLA.parse());
         event.getPlayer().sendMessage("");
      }

   }

   @NotNull
   public String getDefaultDescription() {
      return "Locates nearest TerraformGenerator structures. Do /terra locate for all searchable structures.";
   }

   public boolean canConsoleExec() {
      return true;
   }

   public boolean hasPermission(@NotNull CommandSender sender) {
      return sender.isOp() || sender.hasPermission("terraformgenerator.locate");
   }

   public void execute(@NotNull CommandSender sender, @NotNull Stack<String> args) throws InvalidArgumentException {
      ArrayList<Object> params = this.parseArguments(sender, args);
      if (!params.isEmpty()) {
         if (sender instanceof Player) {
            Player p = (Player)sender;
            StructurePopulator spop = (StructurePopulator)params.get(0);
            if (!spop.isEnabled() && !(spop instanceof StrongholdPopulator)) {
               p.sendMessage(LangOpt.COMMAND_LOCATE_STRUCTURE_NOT_ENABLED.parse());
            } else if (spop instanceof StrongholdPopulator) {
               TerraformWorld tw = TerraformWorld.get(p.getWorld());
               int[] coords = ((StrongholdPopulator)spop).getNearestFeature(tw, p.getLocation().getBlockX(), p.getLocation().getBlockZ());
               this.syncSendMessage(p, spop, coords);
            } else {
               if (spop instanceof SingleMegaChunkStructurePopulator) {
                  this.locateSingleMegaChunkStructure(p, (SingleMegaChunkStructurePopulator)spop);
               } else {
                  this.locateMultiMegaChunkStructure(p, (MultiMegaChunkStructurePopulator)spop);
               }

            }
         } else {
            sender.sendMessage(LangOpt.fetchLang("permissions.console-cannot-exec"));
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

   private void locateMultiMegaChunkStructure(@NotNull final Player p, @NotNull final MultiMegaChunkStructurePopulator populator) {
      final MegaChunk center = new MegaChunk(p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ());
      final TerraformWorld tw = TerraformWorld.get(p.getWorld());
      p.sendMessage(LangOpt.COMMAND_LOCATE_SEARCHING.parse());
      final UUID uuid = p.getUniqueId();
      final long startTime = System.currentTimeMillis();
      BukkitRunnable runnable = new BukkitRunnable() {
         public void run() {
            int[] loc = StructureLocator.locateMultiMegaChunkStructure(tw, center, populator, -1);
            long timeTaken = System.currentTimeMillis() - startTime;
            LocateCommand.this.syncSendMessage(uuid, LangOpt.COMMAND_LOCATE_COMPLETED_TASK.parse("%time%", timeTaken.makeConcatWithConstants<invokedynamic>(timeTaken)));
            LocateCommand.this.syncSendMessage(p, populator, loc);
         }
      };
      runnable.runTaskAsynchronously(this.plugin);
   }

   private void locateSingleMegaChunkStructure(@NotNull final Player p, @NotNull final SingleMegaChunkStructurePopulator populator) {
      final MegaChunk center = new MegaChunk(p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ());
      final TerraformWorld tw = TerraformWorld.get(p.getWorld());
      p.sendMessage(LangOpt.COMMAND_LOCATE_SEARCHING.parse());
      final UUID uuid = p.getUniqueId();
      final long startTime = System.currentTimeMillis();
      BukkitRunnable runnable = new BukkitRunnable() {
         public void run() {
            int[] loc = StructureLocator.locateSingleMegaChunkStructure(tw, center, populator, -1);
            long timeTaken = System.currentTimeMillis() - startTime;
            LocateCommand.this.syncSendMessage(uuid, LangOpt.COMMAND_LOCATE_COMPLETED_TASK.parse("%time%", timeTaken.makeConcatWithConstants<invokedynamic>(timeTaken)));
            LocateCommand.this.syncSendMessage(p, populator, loc);
         }
      };
      runnable.runTaskAsynchronously(this.plugin);
   }

   private void syncSendMessage(UUID uuid, String msg) {
      super.syncSendMessage(uuid, "Locate", msg);
   }

   private void syncSendMessage(@NotNull Player p, @NotNull StructurePopulator populator, @Nullable int[] loc) {
      UUID uuid = p.getUniqueId();
      if (loc == null) {
         this.syncSendMessage(uuid, String.valueOf(ChatColor.RED) + "Failed to find structure. Somehow.");
      } else {
         this.syncSendMessageTP(uuid, "Locate", String.valueOf(ChatColor.GREEN) + "[" + populator.getClass().getSimpleName() + "] " + LangOpt.COMMAND_LOCATE_LOCATE_COORDS.parse("%x%", loc[0].makeConcatWithConstants<invokedynamic>(loc[0]), "%z%", loc[1].makeConcatWithConstants<invokedynamic>(loc[1])), loc[0], this.getHighestY(TerraformWorld.get(p.getWorld()), loc[0], loc[1]), loc[1]);
      }
   }

   static class StructurePopulatorArgument extends TerraCommandArgument<StructurePopulator> {
      public StructurePopulatorArgument(String name, boolean isOptional) {
         super(name, isOptional);
      }

      @Nullable
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
