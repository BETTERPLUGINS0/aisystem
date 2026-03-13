package com.volmit.iris.core.commands;

import com.volmit.iris.Iris;
import com.volmit.iris.core.IrisSettings;
import com.volmit.iris.core.nms.INMS;
import com.volmit.iris.core.service.EditSVC;
import com.volmit.iris.core.service.StudioSVC;
import com.volmit.iris.core.tools.IrisToolbelt;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.object.IrisDimension;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.decree.DecreeExecutor;
import com.volmit.iris.util.decree.DecreeOrigin;
import com.volmit.iris.util.decree.annotations.Decree;
import com.volmit.iris.util.decree.annotations.Param;
import com.volmit.iris.util.decree.specialhandlers.NullablePlayerHandler;
import com.volmit.iris.util.format.C;
import com.volmit.iris.util.io.IO;
import com.volmit.iris.util.misc.ServerProperties;
import com.volmit.iris.util.plugin.VolmitSender;
import com.volmit.iris.util.scheduling.J;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.generator.WorldInfo;
import org.bukkit.scheduler.BukkitRunnable;

@Decree(
   name = "iris",
   aliases = {"ir", "irs"},
   description = "Basic Command"
)
public class CommandIris implements DecreeExecutor {
   private CommandUpdater updater;
   private CommandStudio studio;
   private CommandPregen pregen;
   private CommandSettings settings;
   private CommandObject object;
   private CommandJigsaw jigsaw;
   private CommandWhat what;
   private CommandEdit edit;
   private CommandFind find;
   private CommandDeveloper developer;
   public static boolean worldCreation = false;
   private static final AtomicReference<Thread> mainWorld = new AtomicReference();
   String WorldEngine;
   String worldNameToCheck = "YourWorldName";
   VolmitSender sender = Iris.getSender();

   @Decree(
      description = "Create a new world",
      aliases = {"+", "c"}
   )
   public void create(@Param(aliases = {"world-name"},description = "The name of the world to create") String name, @Param(aliases = {"dimension"},description = "The dimension type to create the world with",defaultValue = "default") IrisDimension type, @Param(description = "The seed to generate the world with",defaultValue = "1337") long seed, @Param(aliases = {"main-world"},description = "Whether or not to automatically use this world as the main world",defaultValue = "false") boolean main) {
      if (var1.equalsIgnoreCase("iris")) {
         this.sender().sendMessage(String.valueOf(C.RED) + "You cannot use the world name \"iris\" for creating worlds as Iris uses this directory for studio worlds.");
         this.sender().sendMessage(String.valueOf(C.RED) + "May we suggest the name \"IrisWorld\" instead?");
      } else if (var1.equalsIgnoreCase("benchmark")) {
         this.sender().sendMessage(String.valueOf(C.RED) + "You cannot use the world name \"benchmark\" for creating worlds as Iris uses this directory for Benchmarking Packs.");
         this.sender().sendMessage(String.valueOf(C.RED) + "May we suggest the name \"IrisWorld\" instead?");
      } else if ((new File(Bukkit.getWorldContainer(), var1)).exists()) {
         this.sender().sendMessage(String.valueOf(C.RED) + "That folder already exists!");
      } else {
         try {
            worldCreation = true;
            IrisToolbelt.createWorld().dimension(var2.getLoadKey()).name(var1).seed(var3).sender(this.sender()).studio(false).create();
            if (var5) {
               Runtime.getRuntime().addShutdownHook((Thread)mainWorld.updateAndGet((var2x) -> {
                  if (var2x != null) {
                     Runtime.getRuntime().removeShutdownHook(var2x);
                  }

                  return new Thread(() -> {
                     this.updateMainWorld(var1);
                  });
               }));
            }
         } catch (Throwable var7) {
            this.sender().sendMessage(String.valueOf(C.RED) + "Exception raised during creation. See the console for more details.");
            Iris.error("Exception raised during world creation: " + var7.getMessage());
            Iris.reportError(var7);
            worldCreation = false;
            return;
         }

         worldCreation = false;
         this.sender().sendMessage(String.valueOf(C.GREEN) + "Successfully created your world!");
         if (var5) {
            this.sender().sendMessage(String.valueOf(C.GREEN) + "Your world will automatically be set as the main world when the server restarts.");
         }

      }
   }

   private void updateMainWorld(String newName) {
      try {
         File var2 = Bukkit.getWorldContainer();
         Properties var3 = ServerProperties.DATA;
         FileInputStream var4 = new FileInputStream(ServerProperties.SERVER_PROPERTIES);

         try {
            var3.load(var4);
         } catch (Throwable var10) {
            try {
               var4.close();
            } catch (Throwable var8) {
               var10.addSuppressed(var8);
            }

            throw var10;
         }

         var4.close();
         Iterator var12 = List.of("datapacks", "playerdata", "advancements", "stats").iterator();

         while(var12.hasNext()) {
            String var5 = (String)var12.next();
            IO.copyDirectory((new File(var2, ServerProperties.LEVEL_NAME + "/" + var5)).toPath(), (new File(var2, var1 + "/" + var5)).toPath());
         }

         var3.setProperty("level-name", var1);
         FileOutputStream var13 = new FileOutputStream(ServerProperties.SERVER_PROPERTIES);

         try {
            var3.store(var13, (String)null);
         } catch (Throwable var9) {
            try {
               var13.close();
            } catch (Throwable var7) {
               var9.addSuppressed(var7);
            }

            throw var9;
         }

         var13.close();
      } catch (Throwable var11) {
         throw var11;
      }
   }

   @Decree(
      description = "Teleport to another world",
      aliases = {"tp"},
      sync = true
   )
   public void teleport(@Param(description = "World to teleport to") World world, @Param(description = "Player to teleport",defaultValue = "---",customHandler = NullablePlayerHandler.class) Player player) {
      if (var2 == null && this.sender().isPlayer()) {
         var2 = this.sender().player();
      }

      if (var2 == null) {
         this.sender().sendMessage(String.valueOf(C.RED) + "The specified player does not exist.");
      } else {
         (new BukkitRunnable(this) {
            public void run() {
               var2.teleport(var1.getSpawnLocation());
               VolmitSender var10000 = new VolmitSender(var2);
               String var10001 = String.valueOf(C.GREEN);
               var10000.sendMessage(var10001 + "You have been teleported to " + var1.getName() + ".");
            }
         }).runTask(Iris.instance);
      }
   }

   @Decree(
      description = "Print version information"
   )
   public void version() {
      VolmitSender var10000 = this.sender();
      String var10001 = String.valueOf(C.GREEN);
      var10000.sendMessage(var10001 + "Iris v" + Iris.instance.getDescription().getVersion() + " by Volmit Software");
   }

   @Decree(
      description = "Print world height information",
      origin = DecreeOrigin.PLAYER
   )
   public void height() {
      if (this.sender().isPlayer()) {
         VolmitSender var10000 = this.sender();
         String var10001 = String.valueOf(C.GREEN);
         var10000.sendMessage(var10001 + this.sender().player().getWorld().getMinHeight() + " to " + this.sender().player().getWorld().getMaxHeight());
         var10000 = this.sender();
         var10001 = String.valueOf(C.GREEN);
         var10000.sendMessage(var10001 + "Total Height: " + (this.sender().player().getWorld().getMaxHeight() - this.sender().player().getWorld().getMinHeight()));
      } else {
         World var1 = (World)Bukkit.getServer().getWorlds().get(0);
         String var2 = String.valueOf(C.GREEN);
         Iris.info(var2 + var1.getMinHeight() + " to " + var1.getMaxHeight());
         var2 = String.valueOf(C.GREEN);
         Iris.info(var2 + "Total Height: " + (var1.getMaxHeight() - var1.getMinHeight()));
      }

   }

   @Decree(
      description = "QOL command to open a overworld studio world.",
      sync = true
   )
   public void so() {
      this.sender().sendMessage(String.valueOf(C.GREEN) + "Opening studio for the \"Overworld\" pack (seed: 1337)");
      ((StudioSVC)Iris.service(StudioSVC.class)).open(this.sender(), 1337L, "overworld");
   }

   @Decree(
      description = "Check access of all worlds.",
      aliases = {"accesslist"}
   )
   public void worlds() {
      KList var1 = new KList();
      KList var2 = new KList();
      Iterator var3 = Bukkit.getServer().getWorlds().iterator();

      World var4;
      while(var3.hasNext()) {
         var4 = (World)var3.next();

         try {
            Engine var5 = IrisToolbelt.access(var4).getEngine();
            if (var5 != null) {
               var1.add((Object)var4);
            }
         } catch (Exception var6) {
            var2.add((Object)var4);
         }
      }

      if (this.sender().isPlayer()) {
         this.sender().sendMessage(String.valueOf(C.BLUE) + "Iris Worlds: ");
         var3 = var1.copy().iterator();

         VolmitSender var10000;
         String var10001;
         while(var3.hasNext()) {
            var4 = (World)var3.next();
            var10000 = this.sender();
            var10001 = String.valueOf(C.IRIS);
            var10000.sendMessage(var10001 + "- " + var4.getName());
         }

         this.sender().sendMessage(String.valueOf(C.GOLD) + "Bukkit Worlds: ");
         var3 = var2.copy().iterator();

         while(var3.hasNext()) {
            var4 = (World)var3.next();
            var10000 = this.sender();
            var10001 = String.valueOf(C.GRAY);
            var10000.sendMessage(var10001 + "- " + var4.getName());
         }
      } else {
         Iris.info(String.valueOf(C.BLUE) + "Iris Worlds: ");
         var3 = var1.copy().iterator();

         while(var3.hasNext()) {
            var4 = (World)var3.next();
            Iris.info(String.valueOf(C.IRIS) + "- " + var4.getName());
         }

         Iris.info(String.valueOf(C.GOLD) + "Bukkit Worlds: ");
         var3 = var2.copy().iterator();

         while(var3.hasNext()) {
            var4 = (World)var3.next();
            Iris.info(String.valueOf(C.GRAY) + "- " + var4.getName());
         }
      }

   }

   @Decree(
      description = "Remove an Iris world",
      aliases = {"del", "rm", "delete"},
      sync = true
   )
   public void remove(@Param(description = "The world to remove") World world, @Param(description = "Whether to also remove the folder (if set to false, just does not load the world)",defaultValue = "true") boolean delete) {
      VolmitSender var10000;
      String var10001;
      if (!IrisToolbelt.isIrisWorld(var1)) {
         var10000 = this.sender();
         var10001 = String.valueOf(C.RED);
         var10000.sendMessage(var10001 + "This is not an Iris world. Iris worlds: " + String.join(", ", Bukkit.getServer().getWorlds().stream().filter(IrisToolbelt::isIrisWorld).map(WorldInfo::getName).toList()));
      } else {
         var10000 = this.sender();
         var10001 = String.valueOf(C.GREEN);
         var10000.sendMessage(var10001 + "Removing world: " + var1.getName());
         if (!IrisToolbelt.evacuate(var1)) {
            var10000 = this.sender();
            var10001 = String.valueOf(C.RED);
            var10000.sendMessage(var10001 + "Failed to evacuate world: " + var1.getName());
         } else if (!Bukkit.unloadWorld(var1, false)) {
            var10000 = this.sender();
            var10001 = String.valueOf(C.RED);
            var10000.sendMessage(var10001 + "Failed to unload world: " + var1.getName());
         } else {
            try {
               if (IrisToolbelt.removeWorld(var1)) {
                  var10000 = this.sender();
                  var10001 = String.valueOf(C.GREEN);
                  var10000.sendMessage(var10001 + "Successfully removed " + var1.getName() + " from bukkit.yml");
               } else {
                  this.sender().sendMessage(String.valueOf(C.YELLOW) + "Looks like the world was already removed from bukkit.yml");
               }
            } catch (IOException var4) {
               var10000 = this.sender();
               var10001 = String.valueOf(C.RED);
               var10000.sendMessage(var10001 + "Failed to save bukkit.yml because of " + var4.getMessage());
               var4.printStackTrace();
            }

            IrisToolbelt.evacuate(var1, "Deleting world");
            EditSVC.deletingWorld = true;
            if (!var2) {
               EditSVC.deletingWorld = false;
            } else {
               VolmitSender var3 = this.sender();
               J.a(() -> {
                  int var2 = 12;
                  if (deleteDirectory(var1.getWorldFolder())) {
                     var3.sendMessage(String.valueOf(C.GREEN) + "Successfully removed world folder");
                  } else {
                     while(true) {
                        if (deleteDirectory(var1.getWorldFolder())) {
                           var3.sendMessage(String.valueOf(C.GREEN) + "Successfully removed world folder");
                           break;
                        }

                        --var2;
                        if (var2 == 0) {
                           var3.sendMessage(String.valueOf(C.RED) + "Failed to remove world folder");
                           break;
                        }

                        J.sleep(3000L);
                     }
                  }

                  EditSVC.deletingWorld = false;
               });
            }
         }
      }
   }

   public static boolean deleteDirectory(File dir) {
      if (var0.isDirectory()) {
         File[] var1 = var0.listFiles();

         for(int var2 = 0; var2 < var1.length; ++var2) {
            boolean var3 = deleteDirectory(var1[var2]);
            if (!var3) {
               return false;
            }
         }
      }

      return var0.delete();
   }

   @Decree(
      description = "Set aura spins"
   )
   public void aura(@Param(description = "The h color value",defaultValue = "-20") int h, @Param(description = "The s color value",defaultValue = "7") int s, @Param(description = "The b color value",defaultValue = "8") int b) {
      IrisSettings.get().getGeneral().setSpinh(var1);
      IrisSettings.get().getGeneral().setSpins(var2);
      IrisSettings.get().getGeneral().setSpinb(var3);
      IrisSettings.get().forceSave();
      this.sender().sendMessage("<rainbow>Aura Spins updated to " + var1 + " " + var2 + " " + var3);
   }

   @Decree(
      description = "Bitwise calculations"
   )
   public void bitwise(@Param(description = "The first value to run calculations on") int value1, @Param(description = "The operator: | & ^ ≺≺ ≻≻ ％") String operator, @Param(description = "The second value to run calculations on") int value2) {
      Integer var4 = null;
      byte var6 = -1;
      switch(var2.hashCode()) {
      case 37:
         if (var2.equals("%")) {
            var6 = 3;
         }
         break;
      case 38:
         if (var2.equals("&")) {
            var6 = 1;
         }
         break;
      case 94:
         if (var2.equals("^")) {
            var6 = 2;
         }
         break;
      case 124:
         if (var2.equals("|")) {
            var6 = 0;
         }
         break;
      case 1920:
         if (var2.equals("<<")) {
            var6 = 5;
         }
         break;
      case 1984:
         if (var2.equals(">>")) {
            var6 = 4;
         }
      }

      switch(var6) {
      case 0:
         var4 = var1 | var3;
         break;
      case 1:
         var4 = var1 & var3;
         break;
      case 2:
         var4 = var1 ^ var3;
         break;
      case 3:
         var4 = var1 % var3;
         break;
      case 4:
         var4 = var1 >> var3;
         break;
      case 5:
         var4 = var1 << var3;
      }

      VolmitSender var10000;
      String var10001;
      if (var4 == null) {
         var10000 = this.sender();
         var10001 = String.valueOf(C.RED);
         var10000.sendMessage(var10001 + "The operator you entered: (" + var2 + ") is invalid!");
      } else {
         var10000 = this.sender();
         var10001 = String.valueOf(C.GREEN);
         var10000.sendMessage(var10001 + var1 + " " + String.valueOf(C.GREEN) + var2.replaceAll("<", "≺").replaceAll(">", "≻").replaceAll("%", "％") + " " + String.valueOf(C.GREEN) + var3 + String.valueOf(C.GREEN) + " returns " + String.valueOf(C.GREEN) + var4);
      }
   }

   @Decree(
      description = "Toggle debug"
   )
   public void debug(@Param(name = "on",description = "Whether or not debug should be on",defaultValue = "other") Boolean on) {
      boolean var2 = var1 == null ? !IrisSettings.get().getGeneral().isDebug() : var1;
      IrisSettings.get().getGeneral().setDebug(var2);
      IrisSettings.get().forceSave();
      VolmitSender var10000 = this.sender();
      String var10001 = String.valueOf(C.GREEN);
      var10000.sendMessage(var10001 + "Set debug to: " + var2);
   }

   @Decree(
      description = "Download a project.",
      aliases = {"dl"}
   )
   public void download(@Param(name = "pack",description = "The pack to download",defaultValue = "overworld",aliases = {"project"}) String pack, @Param(name = "branch",description = "The branch to download from",defaultValue = "main") String branch, @Param(name = "overwrite",description = "Whether or not to overwrite the pack with the downloaded one",aliases = {"force"},defaultValue = "false") boolean overwrite) {
      boolean var4 = false;
      VolmitSender var10000 = this.sender();
      String var10001 = String.valueOf(C.GREEN);
      var10000.sendMessage(var10001 + "Downloading pack: " + var1 + "/" + var2 + (var4 ? " trimmed" : "") + (var3 ? " overwriting" : ""));
      if (var1.equals("overworld")) {
         String var5 = "https://github.com/IrisDimensions/overworld/releases/download/" + INMS.OVERWORLD_TAG + "/overworld.zip";
         ((StudioSVC)Iris.service(StudioSVC.class)).downloadRelease(this.sender(), var5, var4, var3);
      } else {
         ((StudioSVC)Iris.service(StudioSVC.class)).downloadSearch(this.sender(), "IrisDimensions/" + var1 + "/" + var2, var4, var3);
      }

   }

   @Decree(
      description = "Get metrics for your world",
      aliases = {"measure"},
      origin = DecreeOrigin.PLAYER
   )
   public void metrics() {
      if (!IrisToolbelt.isIrisWorld(this.world())) {
         this.sender().sendMessage(String.valueOf(C.RED) + "You must be in an Iris world");
      } else {
         this.sender().sendMessage(String.valueOf(C.GREEN) + "Sending metrics...");
         this.engine().printMetrics(this.sender());
      }
   }

   @Decree(
      description = "Reload configuration file (this is also done automatically)"
   )
   public void reload() {
      IrisSettings.invalidate();
      IrisSettings.get();
      this.sender().sendMessage(String.valueOf(C.GREEN) + "Hotloaded settings");
   }

   @Decree(
      description = "Update the pack of a world (UNSAFE!)",
      name = "^world",
      aliases = {"update-world"}
   )
   public void updateWorld(@Param(description = "The world to update",contextual = true) World world, @Param(description = "The pack to install into the world",contextual = true,aliases = {"dimension"}) IrisDimension pack, @Param(description = "Make sure to make a backup & read the warnings first!",defaultValue = "false",aliases = {"c"}) boolean confirm, @Param(description = "Should Iris download the pack again for you",defaultValue = "false",name = "fresh-download",aliases = {"fresh", "new"}) boolean freshDownload) {
      if (!var3) {
         VolmitSender var10000 = this.sender();
         String[] var10001 = new String[]{String.valueOf(C.RED) + "You should always make a backup before using this", String.valueOf(C.YELLOW) + "Issues caused by this can be, but are not limited to:", String.valueOf(C.YELLOW) + " - Broken chunks (cut-offs) between old and new chunks (before & after the update)", String.valueOf(C.YELLOW) + " - Regenerated chunks that do not fit in with the old chunks", String.valueOf(C.YELLOW) + " - Structures not spawning again when regenerating", String.valueOf(C.YELLOW) + " - Caves not lining up", String.valueOf(C.YELLOW) + " - Terrain layers not lining up", String.valueOf(C.RED) + "Now that you are aware of the risks, and have made a back-up:", null};
         String var10004 = String.valueOf(C.RED);
         var10001[8] = var10004 + "/iris ^world " + var1.getName() + " " + var2.getLoadKey() + " confirm=true";
         var10000.sendMessage(var10001);
      } else {
         File var5 = var1.getWorldFolder();
         var5.mkdirs();
         if (var4) {
            ((StudioSVC)Iris.service(StudioSVC.class)).downloadSearch(this.sender(), var2.getLoadKey(), false, true);
         }

         ((StudioSVC)Iris.service(StudioSVC.class)).installIntoWorld(this.sender(), var2.getLoadKey(), var5);
      }
   }

   @Decree(
      description = "Unload an Iris World",
      origin = DecreeOrigin.PLAYER,
      sync = true
   )
   public void unloadWorld(@Param(description = "The world to unload") World world) {
      VolmitSender var10000;
      String var10001;
      if (!IrisToolbelt.isIrisWorld(var1)) {
         var10000 = this.sender();
         var10001 = String.valueOf(C.RED);
         var10000.sendMessage(var10001 + "This is not an Iris world. Iris worlds: " + String.join(", ", Bukkit.getServer().getWorlds().stream().filter(IrisToolbelt::isIrisWorld).map(WorldInfo::getName).toList()));
      } else {
         var10000 = this.sender();
         var10001 = String.valueOf(C.GREEN);
         var10000.sendMessage(var10001 + "Unloading world: " + var1.getName());

         try {
            IrisToolbelt.evacuate(var1);
            Bukkit.unloadWorld(var1, false);
            this.sender().sendMessage(String.valueOf(C.GREEN) + "World unloaded successfully.");
         } catch (Exception var3) {
            var10000 = this.sender();
            var10001 = String.valueOf(C.RED);
            var10000.sendMessage(var10001 + "Failed to unload the world: " + var3.getMessage());
            var3.printStackTrace();
         }

      }
   }

   @Decree(
      description = "Load an Iris World",
      origin = DecreeOrigin.PLAYER,
      sync = true,
      aliases = {"import"}
   )
   public void loadWorld(@Param(description = "The name of the world to load") String world) {
      World var2 = Bukkit.getWorld(var1);
      this.worldNameToCheck = var1;
      boolean var3 = this.doesWorldExist(this.worldNameToCheck);
      this.WorldEngine = var1;
      VolmitSender var10000;
      String var10001;
      if (!var3) {
         var10000 = this.sender();
         var10001 = String.valueOf(C.YELLOW);
         var10000.sendMessage(var10001 + var1 + " Doesnt exist on the server.");
      } else {
         String var4 = var1 + File.separator + "iris" + File.separator + "pack" + File.separator + "dimensions" + File.separator;
         File var5 = new File(Bukkit.getWorldContainer(), var4);
         String var6 = null;
         if (var5.exists() && var5.isDirectory()) {
            File[] var7 = var5.listFiles();
            if (var7 != null) {
               File[] var8 = var7;
               int var9 = var7.length;

               for(int var10 = 0; var10 < var9; ++var10) {
                  File var11 = var8[var10];
                  if (var11.isFile()) {
                     String var12 = var11.getName();
                     if (var12.endsWith(".json")) {
                        var6 = var12.substring(0, var12.length() - 5);
                        var10000 = this.sender();
                        var10001 = String.valueOf(C.BLUE);
                        var10000.sendMessage(var10001 + "Generator: " + var6);
                     }
                  }
               }
            }

            var10000 = this.sender();
            var10001 = String.valueOf(C.GREEN);
            var10000.sendMessage(var10001 + "Loading world: " + var1);
            YamlConfiguration var14 = YamlConfiguration.loadConfiguration(ServerProperties.BUKKIT_YML);
            String var15 = "Iris:" + var6;
            ConfigurationSection var16 = var14.contains("worlds") ? var14.getConfigurationSection("worlds") : var14.createSection("worlds");
            if (!var16.contains(var1)) {
               var16.createSection(var1).set("generator", var15);

               try {
                  var14.save(ServerProperties.BUKKIT_YML);
                  Iris.info("Registered \"" + var1 + "\" in bukkit.yml");
               } catch (IOException var13) {
                  Iris.error("Failed to update bukkit.yml!");
                  var13.printStackTrace();
                  return;
               }
            }

            Iris var17 = Iris.instance;
            Objects.requireNonNull(var1);
            var17.checkForBukkitWorlds(var1::equals);
            var10000 = this.sender();
            var10001 = String.valueOf(C.GREEN);
            var10000.sendMessage(var10001 + var1 + " loaded successfully.");
         } else {
            var10000 = this.sender();
            var10001 = String.valueOf(C.GOLD);
            var10000.sendMessage(var10001 + var1 + " is not an iris world.");
         }
      }
   }

   @Decree(
      description = "Evacuate an iris world",
      origin = DecreeOrigin.PLAYER,
      sync = true
   )
   public void evacuate(@Param(description = "Evacuate the world") World world) {
      VolmitSender var10000;
      String var10001;
      if (!IrisToolbelt.isIrisWorld(var1)) {
         var10000 = this.sender();
         var10001 = String.valueOf(C.RED);
         var10000.sendMessage(var10001 + "This is not an Iris world. Iris worlds: " + String.join(", ", Bukkit.getServer().getWorlds().stream().filter(IrisToolbelt::isIrisWorld).map(WorldInfo::getName).toList()));
      } else {
         var10000 = this.sender();
         var10001 = String.valueOf(C.GREEN);
         var10000.sendMessage(var10001 + "Evacuating world" + var1.getName());
         IrisToolbelt.evacuate(var1);
      }
   }

   boolean doesWorldExist(String worldName) {
      File var2 = Bukkit.getWorldContainer();
      File var3 = new File(var2, var1);
      return var3.exists() && var3.isDirectory();
   }
}
