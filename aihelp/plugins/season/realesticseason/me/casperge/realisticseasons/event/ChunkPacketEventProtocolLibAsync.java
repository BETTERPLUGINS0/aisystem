package me.casperge.realisticseasons.event;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.api.landplugins.LandPlugin;
import me.casperge.realisticseasons.biome.BlockReplacement;
import me.casperge.realisticseasons.biome.BlockReplacements;
import me.casperge.realisticseasons.biome.HeightAccessor;
import me.casperge.realisticseasons.commands.ToggleSeasonsCommand;
import me.casperge.realisticseasons.season.Season;
import me.casperge.realisticseasons.season.SubSeason;
import me.casperge.realisticseasons.seasonevent.buildin.DefaultEventType;
import me.casperge.realisticseasons1_19_R3.NmsCode_19_R3;
import me.casperge.weather.WeatherPlugin;
import me.casperge.weather.interfaces.BiomePrecipitation;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import world.ofunny.bpm.Floodgate.FloodgateAPI;

public class ChunkPacketEventProtocolLibAsync {
   private RealisticSeasons main;
   private ChunkPacketProcessor processor;
   boolean accessorRequired = true;
   HashMap<String, HeightAccessor> heightAccessors = new HashMap();
   public static AtomicInteger queue = new AtomicInteger(0);
   List<Integer> lastValues;
   HashMap<Material, ChunkPacketEventProtocolLibAsync.Replacement> replacements;
   boolean hasReplacements = false;
   boolean rwcheck = false;
   boolean rwpresent = false;

   public ChunkPacketEventProtocolLibAsync(final RealisticSeasons var1, ChunkPacketProcessor var2) {
      this.processor = var2;
      this.lastValues = new ArrayList();

      for(int var3 = 0; var3 < 30; ++var3) {
         this.lastValues.add(0);
      }

      if (!BlockReplacements.replacements.isEmpty()) {
         ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(var1, new PacketType[]{Server.BLOCK_CHANGE}) {
            public void onPacketSending(PacketEvent var1x) {
               if (var1x.getPlayer() != null) {
                  if (var1x.getPlayer().getWorld() != null) {
                     if (ChunkPacketEventProtocolLibAsync.this.replacements == null) {
                        ChunkPacketEventProtocolLibAsync.this.replacements = new HashMap();
                        Iterator var2 = BlockReplacements.replacements.iterator();

                        while(var2.hasNext()) {
                           BlockReplacement var3 = (BlockReplacement)var2.next();
                           ChunkPacketEventProtocolLibAsync.Replacement var4 = ChunkPacketEventProtocolLibAsync.this.new Replacement();
                           var4.replacement = var3.getReplacent();
                           var4.seasons = var3.getSeasons();
                           var4.phases = var3.getPhases();
                           ChunkPacketEventProtocolLibAsync.this.replacements.put(var3.getOriginal(), var4);
                        }
                     }

                     WrappedBlockData var5 = (WrappedBlockData)var1x.getPacket().getBlockData().read(0);
                     if (ChunkPacketEventProtocolLibAsync.this.replacements.containsKey(var5.getType())) {
                        ChunkPacketEventProtocolLibAsync.Replacement var6 = (ChunkPacketEventProtocolLibAsync.Replacement)ChunkPacketEventProtocolLibAsync.this.replacements.get(var5.getType());
                        if (var6.seasons.contains(var1.getSeasonManager().getSeason(var1x.getPlayer().getWorld()).intValue()) && var6.phases.contains(var1.getSeasonManager().getSubSeason(var1x.getPlayer().getWorld()).getPhase())) {
                           var5.setType(var6.replacement);
                           var1x.getPacket().getBlockData().write(0, var5);
                        }
                     }

                  }
               }
            }
         });
         ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(var1, new PacketType[]{Server.MULTI_BLOCK_CHANGE}) {
            public void onPacketSending(PacketEvent var1x) {
               if (var1x.getPlayer() != null) {
                  if (var1x.getPlayer().getWorld() != null) {
                     if (ChunkPacketEventProtocolLibAsync.this.replacements == null) {
                        ChunkPacketEventProtocolLibAsync.this.replacements = new HashMap();
                        Iterator var2 = BlockReplacements.replacements.iterator();

                        while(var2.hasNext()) {
                           BlockReplacement var3 = (BlockReplacement)var2.next();
                           ChunkPacketEventProtocolLibAsync.Replacement var4 = ChunkPacketEventProtocolLibAsync.this.new Replacement();
                           var4.replacement = var3.getReplacent();
                           var4.seasons = var3.getSeasons();
                           var4.phases = var3.getPhases();
                           ChunkPacketEventProtocolLibAsync.this.replacements.put(var3.getOriginal(), var4);
                        }
                     }

                     WrappedBlockData[] var8 = (WrappedBlockData[])var1x.getPacket().getBlockDataArrays().read(0);
                     int var9 = var1.getSeasonManager().getSeason(var1x.getPlayer().getWorld()).intValue();
                     int var10 = var1.getSeasonManager().getSubSeason(var1x.getPlayer().getWorld()).getPhase();
                     boolean var5 = false;

                     for(int var6 = 0; var6 < var8.length; ++var6) {
                        if (ChunkPacketEventProtocolLibAsync.this.replacements.containsKey(var8[var6].getType())) {
                           ChunkPacketEventProtocolLibAsync.Replacement var7 = (ChunkPacketEventProtocolLibAsync.Replacement)ChunkPacketEventProtocolLibAsync.this.replacements.get(var8[var6].getType());
                           if (var7.seasons.contains(var9) && var7.phases.contains(var10)) {
                              var8[var6].setType(var7.replacement);
                              if (!var5) {
                                 var5 = true;
                              }
                           }
                        }
                     }

                     if (var5) {
                        var1x.getPacket().getBlockDataArrays().write(0, var8);
                     }

                  }
               }
            }
         });
      }

      Bukkit.getScheduler().runTaskTimer(var1, new Runnable() {
         public void run() {
            int var1;
            if (ChunkPacketEventProtocolLibAsync.this.lastValues.isEmpty()) {
               for(var1 = 0; var1 < 30; ++var1) {
                  ChunkPacketEventProtocolLibAsync.this.lastValues.add(0);
               }
            }

            var1 = ChunkPacketEventProtocolLibAsync.queue.get();
            ChunkPacketEventProtocolLibAsync.this.lastValues.add(var1);
            ChunkPacketEventProtocolLibAsync.this.lastValues.remove(0);
            int var2 = ChunkPacketEventProtocolLibAsync.this.average(ChunkPacketEventProtocolLibAsync.this.lastValues);
            if (var2 > 200) {
               Bukkit.getLogger().warning("[RealisticSeasons] Warning: packet queue (" + String.valueOf(var2) + ") is larger than expected. Report this to the RealisticSeasons developer if you're also dealing with performance issues.");
            }

         }
      }, 200L, 200L);
      this.main = var1;
      ProtocolLibrary.getProtocolManager().getAsynchronousManager().registerAsyncHandler(new PacketAdapter(var1, ListenerPriority.LOWEST, new PacketType[]{Server.MAP_CHUNK}) {
         public void onPacketSending(final PacketEvent var1x) {
            final PacketContainer var2 = var1x.getPacket();
            final Player var3 = var1x.getPlayer();
            final int var4 = (Integer)var2.getIntegers().read(0);
            final int var5 = (Integer)var2.getIntegers().read(1);
            final World var6 = var1x.getPlayer().getWorld();
            var1x.getAsyncMarker().incrementProcessingDelay();
            ChunkPacketEventProtocolLibAsync.queue.incrementAndGet();
            Bukkit.getScheduler().runTask(var1, new Runnable() {
               public void run() {
                  ChunkPacketEventProtocolLibAsync.this.processSyncPreConditions(var3, var6, var4, var5, var2, var1x);
               }
            });
         }
      }).start();
   }

   public void processSyncPreConditions(Player var1, World var2, int var3, int var4, PacketContainer var5, PacketEvent var6) {
      byte var7 = -1;
      if (this.isRealisticWeatherPresent()) {
         BiomePrecipitation var8 = WeatherPlugin.getInstance().getPacketEvents().getPrecipitation(var3, var4, var2);
         if (var8 != null) {
            if (var8 == BiomePrecipitation.NONE) {
               var7 = 0;
            } else if (var8 == BiomePrecipitation.RAIN) {
               var7 = 1;
            } else if (var8 == BiomePrecipitation.SNOW) {
               var7 = 2;
            } else if (var8 == BiomePrecipitation.SANDSTORM) {
               var7 = 3;
            }
         }
      }

      if (this.main.getSettings().affectBiomes) {
         Season var13 = this.main.getSeasonManager().getSeason(var2);
         SubSeason var9 = SubSeason.MIDDLE;
         if (var13 == Season.FALL || var13 == Season.WINTER || var13 == Season.SPRING || var13 == Season.SUMMER) {
            var9 = this.main.getSeasonManager().getSubSeason(var2);
         }

         boolean var10;
         if (!this.main.hasSeasons(var3, var4, var2)) {
            if (!this.main.hasLandPlugin()) {
               if (var7 != -1) {
                  this.processBiomeData(var6, var5, var2, Season.SUMMER.intValue(), false, 0, false, var7, false);
               } else {
                  this.cancelPacketProcessing(var6);
               }

               return;
            }

            var10 = false;
            Iterator var11 = this.main.getLandPluginAPIs().iterator();

            while(var11.hasNext()) {
               LandPlugin var12 = (LandPlugin)var11.next();
               if (var12.getPermanentSeason(var3, var4, var2) != Season.DISABLED) {
                  var13 = var12.getPermanentSeason(var3, var4, var2);
                  var10 = true;
               }
            }

            if (!var10) {
               if (var7 != -1) {
                  this.processBiomeData(var6, var5, var2, Season.SUMMER.intValue(), false, 0, false, var7, false);
               } else {
                  this.cancelPacketProcessing(var6);
               }

               return;
            }
         }

         if (ToggleSeasonsCommand.disabled.contains(var1.getUniqueId())) {
            if (var7 != -1) {
               this.processBiomeData(var6, var5, var2, Season.SUMMER.intValue(), false, 0, false, var7, false);
            } else {
               this.cancelPacketProcessing(var6);
            }

            return;
         }

         if (this.main.getEventManager().getDefaultEvent(DefaultEventType.CHRISTMAS) != null) {
            var10 = this.main.getEventManager().getDefaultEvent(DefaultEventType.CHRISTMAS).isEnabled(var2);
         } else {
            var10 = false;
         }

         if (!this.main.christmasTreesEnabled) {
            var10 = false;
         }

         if (var13 == Season.WINTER) {
            if (!FloodgateAPI.get().isBedrockPlayer(var1) && this.main.supportsCustomBiomes(var1)) {
               this.processBiomeData(var6, var5, var2, Season.WINTER.intValue(), false, var9.getPhase(), var10, var7, true);
               return;
            }

            this.processBiomeData(var6, var5, var2, Season.WINTER.intValue(), true, var9.getPhase(), var10, var7, true);
            return;
         }

         if (var13 == Season.FALL) {
            if (!FloodgateAPI.get().isBedrockPlayer(var1) && this.main.supportsCustomBiomes(var1)) {
               this.processBiomeData(var6, var5, var2, Season.FALL.intValue(), false, var9.getPhase(), var10, var7, true);
               return;
            }

            this.processBiomeData(var6, var5, var2, Season.FALL.intValue(), true, var9.getPhase(), var10, var7, true);
            return;
         }

         if (var13 == Season.SPRING) {
            if (!FloodgateAPI.get().isBedrockPlayer(var1) && this.main.supportsCustomBiomes(var1)) {
               this.processBiomeData(var6, var5, var2, Season.SPRING.intValue(), false, var9.getPhase(), var10, var7, true);
               return;
            }

            if (var7 != -1) {
               this.processBiomeData(var6, var5, var2, Season.SUMMER.intValue(), false, 0, false, var7, false);
            } else {
               this.cancelPacketProcessing(var6);
            }

            return;
         }

         if (var13 == Season.SUMMER) {
            if (!FloodgateAPI.get().isBedrockPlayer(var1) && this.main.supportsCustomBiomes(var1)) {
               this.processBiomeData(var6, var5, var2, Season.SUMMER.intValue(), false, var9.getPhase(), var10, var7, true);
               return;
            }

            if (var7 != -1) {
               this.processBiomeData(var6, var5, var2, Season.SUMMER.intValue(), false, 0, false, var7, false);
            } else {
               this.cancelPacketProcessing(var6);
            }

            return;
         }
      } else if (var7 != -1) {
         this.processBiomeData(var6, var5, var2, Season.SUMMER.intValue(), false, 0, false, var7, false);
      }

      this.cancelPacketProcessing(var6);
   }

   public void processBiomeData(final PacketEvent var1, PacketContainer var2, World var3, final int var4, final boolean var5, final int var6, final boolean var7, final int var8, final boolean var9) {
      final int var10 = this.main.getNMSUtils().getSectionCount(var3);
      final HeightAccessor var11 = null;
      if (this.accessorRequired) {
         if (!this.heightAccessors.isEmpty()) {
            if (this.heightAccessors.containsKey(var3.getName())) {
               var11 = (HeightAccessor)this.heightAccessors.get(var3.getName());
            } else {
               var11 = this.generateAccessor(var3);
               this.heightAccessors.put(var3.getName(), var11);
            }
         } else if (this.main.getNMSUtils() instanceof NmsCode_19_R3) {
            var11 = this.generateAccessor(var3);
            this.heightAccessors.put(var3.getName(), var11);
         } else {
            this.accessorRequired = false;
         }
      }

      Bukkit.getScheduler().runTaskAsynchronously(this.main, new Runnable() {
         public void run() {
            ChunkPacketEventProtocolLibAsync.this.processor.addEntry(var1, var4, var5, var6, var10, var11, var7, var8, var9);
         }
      });
   }

   public void cancelPacketProcessing(PacketEvent var1) {
      ProtocolLibrary.getProtocolManager().getAsynchronousManager().signalPacketTransmission(var1);
      queue.decrementAndGet();
   }

   private HeightAccessor generateAccessor(World var1) {
      if (!(this.main.getNMSUtils() instanceof NmsCode_19_R3)) {
         return null;
      } else {
         HashMap var2 = ((NmsCode_19_R3)this.main.getNMSUtils()).generateHeightAccessor(var1);
         HeightAccessor var3 = new HeightAccessor();
         Iterator var4 = var2.keySet().iterator();

         while(var4.hasNext()) {
            Integer var5 = (Integer)var4.next();
            var3.add(var5, (Integer)var2.get(var5));
         }

         return var3;
      }
   }

   private int average(List<Integer> var1) {
      int var2 = 0;

      Integer var4;
      for(Iterator var3 = var1.iterator(); var3.hasNext(); var2 += var4) {
         var4 = (Integer)var3.next();
      }

      return var2 / var1.size();
   }

   private boolean isRealisticWeatherPresent() {
      if (!this.rwcheck) {
         try {
            Class var1 = Class.forName("me.casperge.weather.weather.Weather");
         } catch (ClassNotFoundException var2) {
            this.rwcheck = true;
            this.rwpresent = false;
            return false;
         }

         this.rwcheck = true;
         this.rwpresent = true;
         return true;
      } else {
         return this.rwpresent;
      }
   }

   private class Replacement {
      List<Integer> seasons;
      List<Integer> phases;
      Material replacement;

      private Replacement() {
      }

      // $FF: synthetic method
      Replacement(Object var2) {
         this();
      }
   }
}
