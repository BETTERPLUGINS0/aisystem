package org.terraform.main;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Stack;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.terraform.command.AncientCityCommand;
import org.terraform.command.AnimalFarmCommand;
import org.terraform.command.AnimalSpawnerCommand;
import org.terraform.command.BiomeConsoleCheckCommand;
import org.terraform.command.BiomeDistribCommand;
import org.terraform.command.BiomeVisualiserCommand;
import org.terraform.command.BlockDataTestCommand;
import org.terraform.command.CatacombsCommand;
import org.terraform.command.CaveCommand;
import org.terraform.command.CheckHeightCommand;
import org.terraform.command.CoconutCommand;
import org.terraform.command.CoralCommand;
import org.terraform.command.CrappyDebugStructureCommand;
import org.terraform.command.DesertWellCommand;
import org.terraform.command.DrownedDungeonCommand;
import org.terraform.command.FarmhouseCommand;
import org.terraform.command.FixerCacheFlushCommand;
import org.terraform.command.FractalTreeCommand;
import org.terraform.command.GiantPumpkinCommand;
import org.terraform.command.HelpCommand;
import org.terraform.command.IceSpikeCommand;
import org.terraform.command.IglooCommand;
import org.terraform.command.InjectorDebugTestCommand;
import org.terraform.command.JigsawBuilderTestCommand;
import org.terraform.command.LargeMonumentLampCommand;
import org.terraform.command.LocateBiomeCommand;
import org.terraform.command.LocateCommand;
import org.terraform.command.MansionCommand;
import org.terraform.command.MazeCommand;
import org.terraform.command.MineshaftCommand;
import org.terraform.command.MonumentCommand;
import org.terraform.command.MountainhouseCommand;
import org.terraform.command.MushroomCommand;
import org.terraform.command.NMSChunkPacketRefreshCommand;
import org.terraform.command.NMSChunkQueryCommand;
import org.terraform.command.NewTreeCommand;
import org.terraform.command.OreDitCommand;
import org.terraform.command.OutpostCommand;
import org.terraform.command.PlainsVillageCommand;
import org.terraform.command.PreviewCommand;
import org.terraform.command.PyramidCommand;
import org.terraform.command.RibCageCommand;
import org.terraform.command.RuinedPortalCommand;
import org.terraform.command.SchematicLoadCommand;
import org.terraform.command.SchematicSaveCommand;
import org.terraform.command.SeekCommand;
import org.terraform.command.ShipwreckCommand;
import org.terraform.command.SphereCommand;
import org.terraform.command.StrongholdCommand;
import org.terraform.command.TimingsCommand;
import org.terraform.command.TrailRuinsCommand;
import org.terraform.command.UndergroundDungeonCommand;
import org.terraform.command.ValuesCommand;
import org.terraform.command.WandCommand;
import org.terraform.command.WarmOceanRuinsCommand;
import org.terraform.command.WitchHutCommand;
import org.terraform.command.contants.InvalidArgumentException;
import org.terraform.command.contants.TerraCommand;
import org.terraform.command.contants.TerraCommandArgument;
import org.terraform.main.config.TConfig;

public class TerraformCommandManager implements TabExecutor {
   @NotNull
   public final ArrayList<String> bases = new ArrayList();
   @NotNull
   private final ArrayList<TerraCommand> commands = new ArrayList();
   private final TerraformGeneratorPlugin plugin;

   public TerraformCommandManager(@NotNull TerraformGeneratorPlugin plugin, @NotNull String... bases) {
      this.plugin = plugin;
      String[] var3 = bases;
      int var4 = bases.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String base = var3[var5];
         this.bases.add(base);
         plugin.getCommand(base).setExecutor(this);
      }

      this.registerCommand(new HelpCommand(plugin, this, new String[]{"help", "h", "?"}));
      if (TConfig.c.DEVSTUFF_EXTENDED_COMMANDS) {
         this.registerCommand(new PreviewCommand(plugin, new String[]{"preview"}));
         this.registerCommand(new TimingsCommand(plugin, new String[]{"timings", "lag"}));
         this.registerCommand(new CoconutCommand(plugin, new String[]{"coconut"}));
         this.registerCommand(new ValuesCommand(plugin, new String[]{"values"}));
         this.registerCommand(new BiomeDistribCommand(plugin, new String[]{"biomedistrib"}));
         this.registerCommand(new SphereCommand(plugin, new String[]{"sphere"}));
         this.registerCommand(new FractalTreeCommand(plugin, new String[]{"fractal", "fractaltree", "ftree"}));
         this.registerCommand(new CaveCommand(plugin, new String[]{"cave"}));
         this.registerCommand(new WarmOceanRuinsCommand(plugin, new String[]{"warmoceanruins", "wor"}));
         this.registerCommand(new TrailRuinsCommand(plugin, new String[]{"trailruins", "tr"}));
         this.registerCommand(new IceSpikeCommand(plugin, new String[]{"icespike"}));
         this.registerCommand(new CoralCommand(plugin, new String[]{"coral"}));
         this.registerCommand(new WitchHutCommand(plugin, new String[]{"hut"}));
         this.registerCommand(new GiantPumpkinCommand(plugin, new String[]{"giantpumpkin"}));
         this.registerCommand(new MonumentCommand(plugin, new String[]{"monument", "mon"}));
         this.registerCommand(new LargeMonumentLampCommand(plugin, new String[]{"lml"}));
         this.registerCommand(new StrongholdCommand(plugin, new String[]{"stronghold", "sh"}));
         this.registerCommand(new AnimalFarmCommand(plugin, new String[]{"animalfarm", "af"}));
         this.registerCommand(new FarmhouseCommand(plugin, new String[]{"farmhouse", "fh"}));
         this.registerCommand(new MountainhouseCommand(plugin, new String[]{"mountainhouse", "mh"}));
         this.registerCommand(new AnimalSpawnerCommand(plugin, new String[]{"animalspawner", "as"}));
         this.registerCommand(new MineshaftCommand(plugin, new String[]{"mineshaft", "ms"}));
         this.registerCommand(new CatacombsCommand(plugin, new String[]{"catacombs", "ccs"}));
         this.registerCommand(new IglooCommand(plugin, new String[]{"igloo"}));
         this.registerCommand(new ShipwreckCommand(plugin, new String[]{"shipwreck", "sw"}));
         this.registerCommand(new OutpostCommand(plugin, new String[]{"outpost"}));
         this.registerCommand(new NMSChunkPacketRefreshCommand(plugin, new String[]{"chunkrefresh"}));
         this.registerCommand(new NMSChunkQueryCommand(plugin, new String[]{"chunkquery"}));
         this.registerCommand(new BiomeVisualiserCommand(plugin, new String[]{"bv", "biomevisualiser"}));
         this.registerCommand(new BiomeConsoleCheckCommand(plugin, new String[]{"bcc", "biomeconsolecheck"}));
         this.registerCommand(new UndergroundDungeonCommand(plugin, new String[]{"ud", "undergrounddungeon"}));
         this.registerCommand(new InjectorDebugTestCommand(plugin, new String[]{"idt", "injectordebugtest"}));
         this.registerCommand(new DrownedDungeonCommand(plugin, new String[]{"dd", "drowneddungeon"}));
         this.registerCommand(new CheckHeightCommand(plugin, new String[]{"checkheight", "ch"}));
         this.registerCommand(new WandCommand(plugin, new String[]{"wand"}));
         this.registerCommand(new MushroomCommand(plugin, new String[]{"mushroom"}));
         this.registerCommand(new RuinedPortalCommand(plugin, new String[]{"ruinedportal"}));
         this.registerCommand(new MansionCommand(plugin, new String[]{"mansion"}));
         this.registerCommand(new SchematicSaveCommand(plugin, new String[]{"save"}));
         this.registerCommand(new AncientCityCommand(plugin, new String[]{"ancientcity", "ac"}));
         this.registerCommand(new SchematicLoadCommand(plugin, new String[]{"load"}));
         this.registerCommand(new PyramidCommand(plugin, new String[]{"pyramid"}));
         this.registerCommand(new MazeCommand(plugin, new String[]{"maze"}));
         this.registerCommand(new DesertWellCommand(plugin, new String[]{"desertwell"}));
         this.registerCommand(new BlockDataTestCommand(plugin, new String[]{"blockdatatest", "bdt"}));
         this.registerCommand(new JigsawBuilderTestCommand(plugin, new String[]{"jigsawbuildertest", "jbt"}));
         this.registerCommand(new PlainsVillageCommand(plugin, new String[]{"plainsvillage", "pv"}));
         this.registerCommand(new RibCageCommand(plugin, new String[]{"ribcage"}));
         this.registerCommand(new OreDitCommand(plugin, new String[]{"oredit"}));
         this.registerCommand(new NewTreeCommand(plugin, new String[]{"newtree", "nt"}));
         this.registerCommand(new CrappyDebugStructureCommand(plugin, new String[]{"cdsc"}));
         this.registerCommand(new SeekCommand(plugin, new String[]{"seek"}));
      }

      this.registerCommand(new LocateCommand(plugin, new String[]{"locate"}));
      this.registerCommand(new FixerCacheFlushCommand(plugin, new String[]{"fixercacheflush", "fcf"}));
      this.registerCommand(new LocateBiomeCommand(plugin, new String[]{"locatebiome", "lb"}));
   }

   public void unregisterCommand(@NotNull Class<?> clazz) {
      ArrayList var10000 = this.commands;
      Objects.requireNonNull(clazz);
      var10000.removeIf(clazz::isInstance);
   }

   public void unregisterCommand(String alias) {
      this.commands.removeIf((cmd) -> {
         return cmd.matchCommand(alias);
      });
   }

   @NotNull
   public ArrayList<TerraCommand> getCommands() {
      return this.commands;
   }

   public void registerCommand(@NotNull TerraCommand cmd) {
      this.commands.add(cmd);
      this.plugin.getLang().fetchLang("command." + (String)cmd.aliases.get(0) + ".desc", cmd.getDefaultDescription());
   }

   public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String arg2, @NotNull String[] args) {
      if (args.length == 0) {
         sender.sendMessage(this.plugin.getLang().fetchLang("command.unknown"));
         (new HelpCommand(this.plugin, this, new String[0])).execute(sender, new Stack());
         return false;
      } else {
         Iterator var5 = this.commands.iterator();

         TerraCommand command;
         do {
            if (!var5.hasNext()) {
               sender.sendMessage(this.plugin.getLang().fetchLang("command.unknown"));
               return false;
            }

            command = (TerraCommand)var5.next();
         } while(!command.matchCommand(args[0].toLowerCase(Locale.ENGLISH)));

         Stack<String> stack = new Stack();

         for(int i = args.length - 1; i >= 1; --i) {
            stack.push(args[i]);
         }

         if (!command.hasPermission(sender)) {
            sender.sendMessage(this.plugin.getLang().fetchLang("permissions.insufficient"));
            return false;
         } else if (!command.canConsoleExec() && !(sender instanceof Player)) {
            sender.sendMessage(this.plugin.getLang().fetchLang("permissions.console-cannot-exec"));
            return false;
         } else if (!command.isInAcceptedParamRange(stack)) {
            sender.sendMessage(this.plugin.getLang().fetchLang("command.wrong-arg-length"));
            return false;
         } else {
            try {
               command.execute(sender, stack);
               return true;
            } catch (InvalidArgumentException var9) {
               String var10001 = String.valueOf(ChatColor.RED);
               sender.sendMessage(var10001 + var9.getProblem());
               return false;
            }
         }
      }
   }

   @Nullable
   public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
      List<String> options = new ArrayList();
      Iterator var6;
      TerraCommand terraCommand;
      if (args.length == 0) {
         var6 = this.commands.iterator();

         while(var6.hasNext()) {
            terraCommand = (TerraCommand)var6.next();
            if (terraCommand.hasPermission(commandSender)) {
               options.add((String)terraCommand.aliases.get(0));
            }
         }
      } else {
         Iterator var8;
         if (args.length == 1) {
            var6 = this.commands.iterator();

            while(true) {
               while(true) {
                  do {
                     if (!var6.hasNext()) {
                        return options;
                     }

                     terraCommand = (TerraCommand)var6.next();
                  } while(!terraCommand.hasPermission(commandSender));

                  var8 = terraCommand.aliases.iterator();

                  while(var8.hasNext()) {
                     String a = (String)var8.next();
                     if (a.startsWith(args[0].toLowerCase(Locale.ENGLISH))) {
                        options.add((String)terraCommand.aliases.get(0));
                        break;
                     }
                  }
               }
            }
         } else {
            var6 = this.commands.iterator();

            while(var6.hasNext()) {
               terraCommand = (TerraCommand)var6.next();
               if (terraCommand.matchCommand(args[0].toLowerCase(Locale.ENGLISH))) {
                  var8 = terraCommand.parameters.iterator();

                  while(var8.hasNext()) {
                     TerraCommandArgument<?> arg = (TerraCommandArgument)var8.next();
                     options.addAll(arg.getTabOptions(args));
                  }

                  return options;
               }
            }
         }
      }

      return options;
   }
}
