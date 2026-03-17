package advancedplugins.pm2.cv;

import advancedplugins.pm2.cv.api.InfiniteVehicles;
import advancedplugins.pm2.cv.api.InfiniteVehiclesPluginBase;
import advancedplugins.pm2.cv.api.configuration.Configuration;
import advancedplugins.pm2.cv.api.configuration.GuiConfiguration;
import advancedplugins.pm2.cv.api.configuration.LangConfiguration;
import advancedplugins.pm2.cv.api.configuration.LeaderboardGuiConfiguration;
import advancedplugins.pm2.cv.api.enums.MinecraftVersion;
import advancedplugins.pm2.cv.api.handler.PluginHandler;
import advancedplugins.pm2.cv.api.item.ClickableItems;
import advancedplugins.pm2.cv.api.item.ItemConfiguration;
import advancedplugins.pm2.cv.api.item.RecipeConfiguration;
import advancedplugins.pm2.cv.api.menu.MenuManager;
import advancedplugins.pm2.cv.api.registry.Registries;
import advancedplugins.pm2.cv.api.service.GuiBuilderService;
import advancedplugins.pm2.cv.api.service.Service;
import advancedplugins.pm2.cv.api.upgrade.UpgradeConfiguration;
import advancedplugins.pm2.cv.api.util.ConfigurationUtil;
import advancedplugins.pm2.cv.api.util.Constants;
import advancedplugins.pm2.cv.api.util.reflection.ClassReflection;
import advancedplugins.pm2.cv.api.vehicle.Vehicle;
import advancedplugins.pm2.cv.api.vehicle.configuration.VehicleConfiguration;
import advancedplugins.pm2.cv.command.InfiniteVehiclesCommand;
import advancedplugins.pm2.cv.command.LeaderboardCommand;
import advancedplugins.pm2.cv.command.PluginCommand;
import advancedplugins.pm2.cv.handler.PluginHandlerOptions;
import advancedplugins.pm2.cv.models.core.ModelAPIImpl;
import advancedplugins.pm2.cv.nms.NmsImplementations;
import advancedplugins.pm2.cv.service.PacketInjectionService;
import advancedplugins.pm2.cv.util.task.VehicleRightClickWorkaroundTask;
import advancedplugins.pm2.cv.vehicle.LeaderboardListener;
import advancedplugins.pm2.packetinjector.LightInjector;
import com.google.common.base.Preconditions;
import es.outlook.adriansrj.bstats.bukkit.Metrics;
import es.outlook.adriansrj.spigui.SpiGUI;
import io.netty.channel.Channel;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import me.PM2.infinitevehicles.commands.PaperCommandManager;
import me.PM2.infinitevehicles.libby.BukkitLibraryManager;
import me.PM2.infinitevehicles.libby.Library;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InfiniteVehiclesPlugin extends InfiniteVehiclesPluginBase {
   private PacketInjectionService packetInjectionService;
   private LightInjector packetInjectionHandle;
   private GuiBuilderService guiBuilderService;
   private SpiGUI guiBuilderServiceHandle;
   private final List<PluginCommand> commands = new ArrayList();
   private Metrics metrics;
   private VehicleRightClickWorkaroundTask vehicleRightClickWorkaroundTask;
   private MenuManager menuManager;

   public static InfiniteVehiclesPlugin getInstance() {
      return (InfiniteVehiclesPlugin)JavaPlugin.getPlugin(InfiniteVehiclesPlugin.class);
   }

   public void onLoad() {
      this.loadLibraries();
      ModelAPIImpl.load(this, false);
   }

   public void onEnable() {
      InfiniteVehicles.initialize(this);
      this.metrics = new Metrics(this, 22407);
      this.menuManager = new MenuManager(this);
      this.injectServices();
      this.setupHandlersAndCommands();
      this.extractExamples();
      Constants.Files.mkdirs();
      Registries.load();
      this.loadRecipes();

      try {
         ModelAPIImpl.enable();
      } catch (Throwable var2) {
         var2.printStackTrace();
         this.getLogger().severe("Failed to load the model API, custom models will not work.");
      }

      ClickableItems.init(this);
      this.loadClickableItems();
      this.registerUpdateVehiclePositionTask();
      this.registerVehicleRightClickWorkaround();
      Object v1 = new PaperCommandManager(this);
      v1.getCommandCompletions().registerCompletion("vehicles_lb", (var0) -> {
         return Registries.getRegistry(VehicleConfiguration.class).getEntries().stream().filter(VehicleConfiguration::isLeaderboard).map(VehicleConfiguration::getId).toList();
      });
      v1.getCommandCompletions().registerCompletion("vehicles", (var0) -> {
         return Registries.getRegistry(VehicleConfiguration.class).getEntries().stream().map(VehicleConfiguration::getId).toList();
      });
      v1.getCommandCompletions().registerCompletion("items", (var0) -> {
         return Registries.getRegistry(ItemConfiguration.class).getEntries().stream().map(ItemConfiguration::getId).toList();
      });
      v1.registerCommand(new LeaderboardCommand(), true);
      v1.registerCommand(new InfiniteVehiclesCommand(), true);
   }

   private void registerVehicleRightClickWorkaround() {
      this.vehicleRightClickWorkaroundTask = new VehicleRightClickWorkaroundTask();
      this.vehicleRightClickWorkaroundTask.runTaskTimer(this, 20L, 20L);
   }

   public void onDisable() {
      if (this.metrics != null) {
         this.metrics.shutdown();
      }

      if (this.packetInjectionHandle != null) {
         this.packetInjectionHandle.close();
      }

      if (this.vehicleRightClickWorkaroundTask != null) {
         this.vehicleRightClickWorkaroundTask.cancel();
      }

      InfiniteVehicles.disable();
      ModelAPIImpl.disable();
   }

   @NotNull
   public File getJarFile() {
      return this.getFile();
   }

   protected void loadLibraries() {
      BukkitLibraryManager var1 = new BukkitLibraryManager(this);
      Library var2 = Library.builder().groupId("org{}apache{}commons").artifactId("commons-math3").version("3.6.1").relocate("org{}apache{}commons{}math3", "me{}PM2{}infinitevehicles{}math").build();
      Library var3 = Library.builder().groupId("com{}github{}Querz").artifactId("NBT").version("6.1").relocate("net{}querz", "es{}outlook{}adriansrj{}nbt").isolatedLoad(true).build();
      var1.addMavenCentral();
      var1.addJitPack();
      var1.loadLibrary(var3);
      var1.loadLibrary(var2);
   }

   protected void registerUpdateVehiclePositionTask() {
      Bukkit.getScheduler().runTaskTimer(this, () -> {
         this.getServer().getOnlinePlayers().forEach((var0) -> {
            Vehicle var1 = InfiniteVehicles.getVehicleHandler().getVehicleByOperator(var0);
            if (var1 != null) {
               boolean var2 = var1.isMoving();
               if (var2) {
                  var1.setLocationAndRotation(var1.getX(), var1.getY(), var1.getZ(), var1.getRotation());
               }
            }
         });
      }, 20L, 20L);
   }

   protected void loadClickableItems() {
      Registries.getRegistry(UpgradeConfiguration.class).getEntries().forEach((var0) -> {
         var0.getUpgrades().forEach((var1) -> {
            var1.getUpgradeTiers().forEach((var2, var3) -> {
               ClickableItems.addClickableEvent("upgrade_" + var0.getId() + "_" + var1.getId() + "_" + var2, (var3x, var4) -> {
                  if (var4 == null) {
                     var3x.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cSorry but you can't do that!"));
                  } else if (!Objects.equals(var4.getOwnerUniqueId(), var3x.getUniqueId())) {
                     var3x.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cSorry but you can't do that!"));
                  } else if (var4.getUpgradeConfiguration() == null) {
                     var3x.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cSorry but you can't do that!"));
                  } else if (!Objects.equals(var4.getUpgradeConfiguration().getId(), var0.getId())) {
                     var3x.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cSorry but you can't do that!"));
                  } else {
                     int var5 = (Integer)var4.getUpgradeTier(var3x.getUniqueId()).getOrDefault(var1.getId(), -1);
                     if (var5 != -1 && var2 < var5) {
                        var3x.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cSorry but you can't do that!"));
                     } else {
                        var4.setUpgradeTier(var3x.getUniqueId(), var1.getId(), var2 + 1);
                        var3x.getInventory().getItemInMainHand().setAmount(var3x.getInventory().getItemInMainHand().getAmount() - 1);
                        var3x.updateInventory();
                        var3x.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Successfully upgraded the vehicle!"));
                     }
                  }
               });
            });
         });
      });
   }

   public void extractExamples() {
      HashSet var1 = new HashSet();

      try {
         JarFile var2 = new JarFile(this.getFile());

         try {
            Enumeration var3 = var2.entries();

            label62:
            while(true) {
               JarEntry var4;
               File var8;
               File var9;
               do {
                  do {
                     String var5;
                     do {
                        do {
                           do {
                              if (!var3.hasMoreElements()) {
                                 break label62;
                              }

                              var4 = (JarEntry)var3.nextElement();
                              var5 = var4.getName();
                           } while(!var5.startsWith("examples/"));
                        } while(!var5.toLowerCase().endsWith(".yml"));
                     } while(var5.indexOf(47) == var5.lastIndexOf(47));

                     String var6 = var5.substring(var5.indexOf(47) + 1, var5.lastIndexOf(47));
                     String var7 = var5.substring(var5.lastIndexOf(47) + 1);
                     var8 = new File(this.getDataFolder(), var6);
                     var9 = new File(var8, var7);
                  } while(var8.exists() && !var1.contains(var8.toPath()));

                  var1.add(var8.toPath());
               } while(!var8.exists() && !var8.mkdirs());

               Files.copy(var2.getInputStream(var4), var9.toPath(), new CopyOption[0]);
            }
         } catch (Throwable var11) {
            try {
               var2.close();
            } catch (Throwable var10) {
               var11.addSuppressed(var10);
            }

            throw var11;
         }

         var2.close();
      } catch (IOException var12) {
         var12.printStackTrace();
      }

   }

   public void reload() {
      InfiniteVehiclesPlugin var1 = getInstance();
      Configuration.load(var1);
      LangConfiguration.load(var1);

      try {
         GuiConfiguration.load(var1);
      } catch (InvalidConfigurationException var4) {
         var4.printStackTrace();
      }

      try {
         LeaderboardGuiConfiguration.load(var1);
      } catch (InvalidConfigurationException var3) {
         var3.printStackTrace();
      }

      var1.extractExamples();
      Constants.Files.mkdirs();
      Registries.reload();
      var1.loadRecipes();
   }

   public void loadRecipes() {
      File var1 = Constants.Files.RECIPES_CONFIGURATION_FILE;
      if (!var1.exists()) {
         var1.getParentFile().mkdirs();

         try {
            InputStream var2 = this.getResource("examples/RecipesConfiguration.yml");

            try {
               if (var2 == null) {
                  throw new IOException();
               }

               Files.copy(var2, var1.toPath(), new CopyOption[0]);
            } catch (Throwable var9) {
               if (var2 != null) {
                  try {
                     var2.close();
                  } catch (Throwable var8) {
                     var9.addSuppressed(var8);
                  }
               }

               throw var9;
            }

            if (var2 != null) {
               var2.close();
            }
         } catch (IOException var10) {
            this.getLogger().info("was not able to generate example recipes configuration");

            try {
               Files.createFile(var1.toPath());
            } catch (IOException var7) {
               throw new IllegalStateException("was not able to generate the recipes configuration file", var7);
            }
         }
      }

      int var11 = 0;
      Iterator var3 = ConfigurationUtil.getConfigurationSections(YamlConfiguration.loadConfiguration(var1), false).iterator();

      while(var3.hasNext()) {
         ConfigurationSection var4 = (ConfigurationSection)var3.next();

         try {
            RecipeConfiguration var5 = RecipeConfiguration.load(var4);
            Bukkit.addRecipe(var5.getRecipe());
            ++var11;
         } catch (InvalidConfigurationException var6) {
            var6.printStackTrace();
         }
      }

      InfiniteVehicles.getPlugin().getLogger().info(var11 > 0 ? var11 + " recipes were loaded!" : "No recipes were loaded!");
   }

   protected void injectServices() {
      this.packetInjectionService = new PacketInjectionService();
      this.packetInjectionHandle = new LightInjector(this) {
         @Nullable
         protected Object onPacketReceiveAsync(@Nullable Player sender, @NotNull Channel channel, @NotNull Object packet) {
            return InfiniteVehiclesPlugin.this.packetInjectionService.processClientPacket(var1, var2, var3);
         }

         @Nullable
         protected Object onPacketSendAsync(@Nullable Player receiver, @NotNull Channel channel, @NotNull Object packet) {
            return InfiniteVehiclesPlugin.this.packetInjectionService.processServerPacket(var1, var2, var3);
         }
      };
      this.guiBuilderServiceHandle = new SpiGUI(this);
      this.guiBuilderService = () -> {
         return this.guiBuilderServiceHandle;
      };
      InfiniteVehicles.registerService(PacketInjectionService.class, this.packetInjectionService);
      InfiniteVehicles.registerService(GuiBuilderService.class, this.guiBuilderService);
      Iterator var1 = NmsImplementations.getTypes().iterator();

      while(var1.hasNext()) {
         Class var2 = (Class)var1.next();
         if (Service.class.isAssignableFrom(var2)) {
            Class var3 = var2.asSubclass(Service.class);
            Service var4 = (Service)NmsImplementations.getSingleInstanceImplementation(var3);
            InfiniteVehicles.registerService(var3, var4);
         }
      }

   }

   private boolean isJava21OrHigher() {
      try {
         int var1 = Runtime.version().feature();
         return var1 >= 21;
      } catch (Exception var4) {
         String var2 = System.getProperty("java.version");
         if (var2.startsWith("1.")) {
            var2 = var2.substring(2, 3);
         } else {
            int var3 = var2.indexOf(".");
            if (var3 != -1) {
               var2 = var2.substring(0, var3);
            }
         }

         return Integer.parseInt(var2) >= 21;
      }
   }

   protected void setupHandlersAndCommands() {
      this.getServer().getPluginManager().registerEvents(new LeaderboardListener(), this);
      boolean var1 = this.isJava21OrHigher();
      Iterator var2 = ClassReflection.getClassNames(this.getFile(), (String)null).iterator();

      while(true) {
         Class var4;
         do {
            while(true) {
               String var3;
               do {
                  do {
                     do {
                        do {
                           do {
                              if (!var2.hasNext()) {
                                 return;
                              }

                              var3 = (String)var2.next();
                           } while(var3.startsWith("me.PM2.infinitevehicles.commands"));
                        } while(var3.contains("MenuModelIdPage"));
                     } while(var3.contains("advancedplugins.pm2.cv.models"));
                  } while(var3.contains("xseries"));

                  if (var3.endsWith("$1")) {
                     var3 = var3.replace("$1", "");
                  }

                  MinecraftVersion[] var5 = MinecraftVersion.values();
                  int var6 = var5.length;

                  for(int var7 = 0; var7 < var6; ++var7) {
                     MinecraftVersion var8 = var5[var7];
                     if (var8 != MinecraftVersion.getVersion() && var3.contains(var8.name().replace("MC", "v"))) {
                        var3 = var3.replace(var8.name().replace("MC", "v"), MinecraftVersion.getVersion().name().replace("MC", "v"));
                     }
                  }
               } while(!var1 && var3.contains("v1_21_R1"));

               try {
                  var4 = Class.forName(var3);
                  break;
               } catch (NoClassDefFoundError | ClassFormatError | NoSuchMethodError | ClassNotFoundException var12) {
               }
            }
         } while(Arrays.asList(var4.getInterfaces()).contains(PluginCommand.class) && this.isShouldEnableCommand(var4));

         if (!Modifier.isAbstract(var4.getModifiers()) && !Modifier.isInterface(var4.getModifiers()) && PluginHandler.class.isAssignableFrom(var4) && this.isShouldEnableHandler(var4)) {
            try {
               Object var13 = this.createPluginHandlerInstance(var4);
               Class var14 = var4.asSubclass(PluginHandler.class);
               if (var4.isAnnotationPresent(PluginHandlerOptions.class)) {
                  PluginHandlerOptions var15 = (PluginHandlerOptions)var4.getAnnotation(PluginHandlerOptions.class);
                  if (var15.apiClass() != PluginHandler.class) {
                     var14 = var15.apiClass();
                  }

                  boolean var10000;
                  String var10001;
                  if (var15.eventListener()) {
                     var10000 = Listener.class.isAssignableFrom(var4);
                     var10001 = var4.getName();
                     Preconditions.checkArgument(var10000, var10001 + " doesn't implement " + Listener.class.getName());
                     Bukkit.getPluginManager().registerEvents((Listener)var13, this);
                  }

                  if (var15.packetInjector()) {
                     var10000 = PacketInjectionService.Injector.class.isAssignableFrom(var4);
                     var10001 = var4.getName();
                     Preconditions.checkArgument(var10000, var10001 + " doesn't implement " + PacketInjectionService.Injector.class.getName());
                     ((PacketInjectionService)Objects.requireNonNull((PacketInjectionService)InfiniteVehicles.getService(PacketInjectionService.class))).register((PacketInjectionService.Injector)var13);
                  }
               }

               InfiniteVehicles.registerHandler(var14, (PluginHandler)var13);
            } catch (NoSuchMethodException var9) {
               throw new IllegalStateException(var4.getName() + " doesn't have the expected constructor");
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException var10) {
               throw new IllegalStateException("couldn't create instance of plugin handler " + var4.getName(), var10);
            } catch (IllegalArgumentException var11) {
            }
         }
      }
   }

   public void addCommand(PluginCommand command) {
      this.commands.add(var1);
      var1.register();
   }

   public void removeCommand(PluginCommand command) {
      this.commands.remove(var1);
      var1.unregister();
   }

   protected boolean isShouldEnableCommand(Class<?> commandClass) {
      return true;
   }

   protected boolean isShouldEnableHandler(Class<?> handlerClass) {
      return true;
   }

   protected Object createPluginHandlerInstance(Class<?> clazz) {
      try {
         Constructor var2 = var1.getConstructor(JavaPlugin.class);
         return var2.newInstance(this);
      } catch (NoSuchMethodException var3) {
         return var1.getDeclaredConstructor().newInstance();
      } catch (ReflectiveOperationException var4) {
         throw new RuntimeException("Failed to instantiate " + var1.getName(), var4);
      }
   }

   public PacketInjectionService getPacketInjectionService() {
      return this.packetInjectionService;
   }

   public LightInjector getPacketInjectionHandle() {
      return this.packetInjectionHandle;
   }

   public GuiBuilderService getGuiBuilderService() {
      return this.guiBuilderService;
   }

   public SpiGUI getGuiBuilderServiceHandle() {
      return this.guiBuilderServiceHandle;
   }

   public Metrics getMetrics() {
      return this.metrics;
   }

   public VehicleRightClickWorkaroundTask getVehicleRightClickWorkaroundTask() {
      return this.vehicleRightClickWorkaroundTask;
   }

   public MenuManager getMenuManager() {
      return this.menuManager;
   }

   public List<PluginCommand> getCommands() {
      return this.commands;
   }
}
