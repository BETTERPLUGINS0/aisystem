package com.volmit.iris.core.tools;

import com.google.common.util.concurrent.AtomicDouble;
import com.volmit.iris.Iris;
import com.volmit.iris.core.IrisSettings;
import com.volmit.iris.core.ServerConfigurator;
import com.volmit.iris.core.gui.PregeneratorJob;
import com.volmit.iris.core.nms.INMS;
import com.volmit.iris.core.pregenerator.PregenTask;
import com.volmit.iris.core.service.StudioSVC;
import com.volmit.iris.engine.object.IrisDimension;
import com.volmit.iris.engine.platform.PlatformChunkGenerator;
import com.volmit.iris.util.exceptions.IrisException;
import com.volmit.iris.util.format.C;
import com.volmit.iris.util.format.Form;
import com.volmit.iris.util.misc.ServerProperties;
import com.volmit.iris.util.plugin.VolmitSender;
import com.volmit.iris.util.scheduling.J;
import com.volmit.iris.util.scheduling.O;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.IntSupplier;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class IrisCreator {
   private PregenTask pregen;
   private VolmitSender sender;
   private long seed = 1337L;
   private String dimension = IrisSettings.get().getGenerator().getDefaultWorldType();
   private String name = "irisworld";
   private boolean studio = false;
   private boolean benchmark = false;

   public static boolean removeFromBukkitYml(String name) {
      YamlConfiguration var1 = YamlConfiguration.loadConfiguration(ServerProperties.BUKKIT_YML);
      ConfigurationSection var2 = var1.getConfigurationSection("worlds");
      if (var2 == null) {
         return false;
      } else {
         var2.set(var0, (Object)null);
         if (var2.getValues(false).keySet().stream().noneMatch((var1x) -> {
            return var2.get(var1x) != null;
         })) {
            var1.set("worlds", (Object)null);
         }

         var1.save(ServerProperties.BUKKIT_YML);
         return true;
      }
   }

   public static boolean worldLoaded() {
      return true;
   }

   public World create() {
      if (Bukkit.isPrimaryThread()) {
         throw new IrisException("You cannot invoke create() on the main thread.");
      } else {
         IrisDimension var1 = IrisToolbelt.getDimension(this.dimension());
         if (var1 == null) {
            throw new IrisException("Dimension cannot be found null for id " + this.dimension());
         } else {
            if (this.sender == null) {
               this.sender = Iris.getSender();
            }

            if (!this.studio() || this.benchmark) {
               ((StudioSVC)Iris.service(StudioSVC.class)).installIntoWorld(this.sender, var1.getLoadKey(), new File(Bukkit.getWorldContainer(), this.name()));
            }

            AtomicDouble var2 = new AtomicDouble(0.0D);
            O var3 = new O();
            var3.set(false);
            WorldCreator var4 = (new IrisWorldCreator()).dimension(this.dimension).name(this.name).seed(this.seed).studio(this.studio).create();
            if (ServerConfigurator.installDataPacks(true)) {
               throw new IrisException("Datapacks were missing!");
            } else {
               PlatformChunkGenerator var5 = (PlatformChunkGenerator)var4.generator();
               if (var5 == null) {
                  throw new IrisException("Access is null. Something bad happened.");
               } else {
                  J.a(() -> {
                     IntSupplier var3x = () -> {
                        return var5.getEngine() == null ? 0 : var5.getEngine().getGenerated();
                     };
                     if (!this.benchmark) {
                        int var4 = (Integer)var5.getSpawnChunks().join();

                        for(int var5x = 0; var5x < var4 && !(Boolean)var3.get(); var5x = var3x.getAsInt()) {
                           double var6 = (double)var5x / (double)var4;
                           if (this.sender.isPlayer()) {
                              this.sender.sendProgress(var6, "Generating");
                              J.sleep(16L);
                           } else {
                              VolmitSender var10000 = this.sender;
                              String var10001 = String.valueOf(C.WHITE);
                              var10000.sendMessage(var10001 + "Generating " + Form.pc(var6) + String.valueOf(C.GRAY) + " (" + (var4 - var5x) + " Left)");
                              J.sleep(1000L);
                           }
                        }
                     }

                  });

                  World var6;
                  try {
                     var6 = (World)J.sfut(() -> {
                        return INMS.get().createWorld(var4);
                     }).get();
                  } catch (Throwable var10) {
                     var3.set(true);
                     throw new IrisException("Failed to create world!", var10);
                  }

                  var3.set(true);
                  if (this.sender.isPlayer() && !this.benchmark) {
                     J.s(() -> {
                        this.sender.player().teleport(new Location(var6, 0.0D, (double)(var6.getHighestBlockYAt(0, 0) + 1), 0.0D));
                     });
                  }

                  if (!this.studio && !this.benchmark) {
                     this.addToBukkitYml();
                     J.s(() -> {
                        Iris.linkMultiverseCore.updateWorld(var6, this.dimension);
                     });
                  } else {
                     J.s(() -> {
                        Iris.linkMultiverseCore.removeFromConfig(var6);
                        if (IrisSettings.get().getStudio().isDisableTimeAndWeather()) {
                           var6.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
                           var6.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
                           var6.setTime(6000L);
                        }

                     });
                  }

                  if (this.pregen != null) {
                     CompletableFuture var7 = new CompletableFuture();
                     PregeneratorJob var10000 = IrisToolbelt.pregenerate(this.pregen, var5);
                     Objects.requireNonNull(var2);
                     var10000.onProgress(var2::set).whenDone(() -> {
                        var7.complete(true);
                     });

                     try {
                        AtomicBoolean var8 = new AtomicBoolean(false);
                        J.a(() -> {
                           while(!var8.get()) {
                              if (this.sender.isPlayer()) {
                                 this.sender.sendProgress(var2.get(), "Pregenerating");
                                 J.sleep(16L);
                              } else {
                                 VolmitSender var10000 = this.sender;
                                 String var10001 = String.valueOf(C.WHITE);
                                 var10000.sendMessage(var10001 + "Pregenerating " + Form.pc(var2.get()));
                                 J.sleep(1000L);
                              }
                           }

                        });
                        var7.get();
                        var8.set(true);
                     } catch (Throwable var9) {
                        var9.printStackTrace();
                     }
                  }

                  return var6;
               }
            }
         }
      }
   }

   private void addToBukkitYml() {
      YamlConfiguration var1 = YamlConfiguration.loadConfiguration(ServerProperties.BUKKIT_YML);
      String var2 = "Iris:" + this.dimension;
      ConfigurationSection var3 = var1.contains("worlds") ? var1.getConfigurationSection("worlds") : var1.createSection("worlds");
      if (!var3.contains(this.name)) {
         var3.createSection(this.name).set("generator", var2);

         try {
            var1.save(ServerProperties.BUKKIT_YML);
            Iris.info("Registered \"" + this.name + "\" in bukkit.yml");
         } catch (IOException var5) {
            Iris.error("Failed to update bukkit.yml!");
            var5.printStackTrace();
         }
      }

   }

   @Generated
   public PregenTask pregen() {
      return this.pregen;
   }

   @Generated
   public VolmitSender sender() {
      return this.sender;
   }

   @Generated
   public long seed() {
      return this.seed;
   }

   @Generated
   public String dimension() {
      return this.dimension;
   }

   @Generated
   public String name() {
      return this.name;
   }

   @Generated
   public boolean studio() {
      return this.studio;
   }

   @Generated
   public boolean benchmark() {
      return this.benchmark;
   }

   @Generated
   public IrisCreator pregen(final PregenTask pregen) {
      this.pregen = var1;
      return this;
   }

   @Generated
   public IrisCreator sender(final VolmitSender sender) {
      this.sender = var1;
      return this;
   }

   @Generated
   public IrisCreator seed(final long seed) {
      this.seed = var1;
      return this;
   }

   @Generated
   public IrisCreator dimension(final String dimension) {
      this.dimension = var1;
      return this;
   }

   @Generated
   public IrisCreator name(final String name) {
      this.name = var1;
      return this;
   }

   @Generated
   public IrisCreator studio(final boolean studio) {
      this.studio = var1;
      return this;
   }

   @Generated
   public IrisCreator benchmark(final boolean benchmark) {
      this.benchmark = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisCreator)) {
         return false;
      } else {
         IrisCreator var2 = (IrisCreator)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.seed() != var2.seed()) {
            return false;
         } else if (this.studio() != var2.studio()) {
            return false;
         } else if (this.benchmark() != var2.benchmark()) {
            return false;
         } else {
            PregenTask var3 = this.pregen();
            PregenTask var4 = var2.pregen();
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            label59: {
               VolmitSender var5 = this.sender();
               VolmitSender var6 = var2.sender();
               if (var5 == null) {
                  if (var6 == null) {
                     break label59;
                  }
               } else if (var5.equals(var6)) {
                  break label59;
               }

               return false;
            }

            String var7 = this.dimension();
            String var8 = var2.dimension();
            if (var7 == null) {
               if (var8 != null) {
                  return false;
               }
            } else if (!var7.equals(var8)) {
               return false;
            }

            String var9 = this.name();
            String var10 = var2.name();
            if (var9 == null) {
               if (var10 != null) {
                  return false;
               }
            } else if (!var9.equals(var10)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisCreator;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      long var3 = this.seed();
      int var9 = var2 * 59 + (int)(var3 >>> 32 ^ var3);
      var9 = var9 * 59 + (this.studio() ? 79 : 97);
      var9 = var9 * 59 + (this.benchmark() ? 79 : 97);
      PregenTask var5 = this.pregen();
      var9 = var9 * 59 + (var5 == null ? 43 : var5.hashCode());
      VolmitSender var6 = this.sender();
      var9 = var9 * 59 + (var6 == null ? 43 : var6.hashCode());
      String var7 = this.dimension();
      var9 = var9 * 59 + (var7 == null ? 43 : var7.hashCode());
      String var8 = this.name();
      var9 = var9 * 59 + (var8 == null ? 43 : var8.hashCode());
      return var9;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.pregen());
      return "IrisCreator(pregen=" + var10000 + ", sender=" + String.valueOf(this.sender()) + ", seed=" + this.seed() + ", dimension=" + this.dimension() + ", name=" + this.name() + ", studio=" + this.studio() + ", benchmark=" + this.benchmark() + ")";
   }
}
