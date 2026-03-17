package com.bergerkiller.bukkit.tc.signactions.mutex;

import com.bergerkiller.bukkit.common.bases.IntVector3;
import com.bergerkiller.bukkit.common.offline.OfflineBlock;
import com.bergerkiller.bukkit.common.offline.OfflineWorld;
import com.bergerkiller.bukkit.common.offline.OfflineWorldMap;
import com.bergerkiller.bukkit.common.utils.StreamUtil;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.offline.sign.OfflineSign;
import com.bergerkiller.bukkit.tc.offline.sign.OfflineSignMetadataHandler;
import com.bergerkiller.bukkit.tc.offline.sign.OfflineSignStore;
import com.bergerkiller.bukkit.tc.offline.train.format.OfflineDataBlock;
import com.bergerkiller.bukkit.tc.rails.RailLookup;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.UnaryOperator;
import java.util.logging.Level;

public class MutexZoneCache {
   private static final OfflineWorldMap<MutexZoneCacheWorld> cachesByWorld = new OfflineWorldMap();
   private static final Map<String, MutexZoneSlot> slotsByName = new HashMap();
   private static final List<MutexZoneSlot> slotsList = new ArrayList();

   public static void init(TrainCarts plugin) {
      plugin.getOfflineSigns().registerHandler(MutexSignMetadata.class, new OfflineSignMetadataHandler<MutexSignMetadata>() {
         public void onAdded(OfflineSignStore store, OfflineSign sign, MutexSignMetadata metadata) {
            MutexZoneCache.addMutexSign(sign.getWorld(), sign.getPosition(), sign.isFrontText(), metadata);
         }

         public void onRemoved(OfflineSignStore store, OfflineSign sign, MutexSignMetadata metadata) {
            MutexZoneCache.removeMutexSign(sign.getWorld(), sign.getPosition(), sign.isFrontText());
         }

         public void onUpdated(OfflineSignStore store, OfflineSign sign, MutexSignMetadata oldValue, MutexSignMetadata newValue) {
            this.onRemoved(store, sign, oldValue);
            this.onAdded(store, sign, newValue);
         }

         public void onEncode(DataOutputStream stream, OfflineSign sign, MutexSignMetadata value) throws IOException {
            stream.writeUTF(value.name);
            value.start.write(stream);
            value.end.write(stream);
            stream.writeUTF(value.statement);
         }

         public MutexSignMetadata onDecode(DataInputStream stream, OfflineSign sign) throws IOException {
            String name = stream.readUTF();
            IntVector3 start = IntVector3.read(stream);
            IntVector3 end = IntVector3.read(stream);
            String statement = stream.readUTF();
            String typeName = sign.getLine(1).toLowerCase(Locale.ENGLISH);
            MutexZoneSlotType type = !typeName.startsWith("smartmutex") && !typeName.startsWith("smutex") ? MutexZoneSlotType.NORMAL : MutexZoneSlotType.SMART;
            return new MutexSignMetadata(type, name, start, end, statement);
         }

         public boolean isUnloadedWorldsIgnored() {
            return false;
         }
      });
   }

   public static void saveState(TrainCarts plugin, OfflineDataBlock root) {
      OfflineDataBlock stateData = root.addChild("mutex-zones-state");
      Iterator var3 = cachesByWorld.values().iterator();

      while(var3.hasNext()) {
         MutexZoneCacheWorld world = (MutexZoneCacheWorld)var3.next();
         world.byPathingKey.values().forEach((p) -> {
            p.writeTo(stateData);
         });
      }

      var3 = slotsList.iterator();

      while(true) {
         OfflineDataBlock slotData;
         MutexZoneSlot slot;
         while(true) {
            do {
               do {
                  if (!var3.hasNext()) {
                     return;
                  }

                  slot = (MutexZoneSlot)var3.next();
               } while(slot.getEnteredGroups().isEmpty());
            } while(slot.isAnonymous() && !slot.hasZones());

            try {
               slotData = stateData.addChildOrAbort("mutex-zone-slot", (stream) -> {
                  if (!slot.isAnonymous()) {
                     Util.writeVariableLengthInt(stream, 0);
                     stream.writeUTF(slot.getName());
                  } else {
                     MutexZone zone = (MutexZone)slot.getZones().get(0);
                     if (zone instanceof MutexZonePath) {
                        Util.writeVariableLengthInt(stream, 2);
                        MutexZonePath pathMutex = (MutexZonePath)zone;
                        StreamUtil.writeUUID(stream, pathMutex.signBlock.getWorldUUID());
                        if (!pathMutex.key.writeTo(plugin, stream)) {
                           throw new OfflineDataBlock.AbortChildException();
                        }
                     } else {
                        Util.writeVariableLengthInt(stream, 1);
                        OfflineBlock.writeTo(stream, zone.signBlock);
                        stream.writeBoolean(zone.signFront);
                     }
                  }

               });
               if (slotData == null) {
                  continue;
               }
               break;
            } catch (Throwable var8) {
               plugin.getLogger().log(Level.SEVERE, "Failed to save mutex zone slot '" + slot.getName() + "'", var8);
            }
         }

         Iterator var6 = slot.getEnteredGroups().iterator();

         while(var6.hasNext()) {
            MutexZoneSlot.EnteredGroup group = (MutexZoneSlot.EnteredGroup)var6.next();
            group.unload().save(plugin, slotData);
         }
      }
   }

   public static void loadState(TrainCarts plugin, OfflineDataBlock root) {
      root.findChild("mutex-zones-state").ifPresent((stateData) -> {
         Iterator var2 = MutexZonePath.readAll(plugin, stateData).iterator();

         while(var2.hasNext()) {
            MutexZonePath pathMutex = (MutexZonePath)var2.next();
            System.out.println("Load path mutex: " + pathMutex);
            forWorld(pathMutex.signBlock.getWorld()).add(pathMutex);
         }

         var2 = stateData.findChildren("mutex-zone-slot").iterator();

         while(var2.hasNext()) {
            OfflineDataBlock slotData = (OfflineDataBlock)var2.next();

            MutexZoneSlot slot;
            try {
               DataInputStream stream = slotData.readData();

               label115: {
                  label114: {
                     label113: {
                        label112: {
                           label111: {
                              label110: {
                                 try {
                                    int mode = Util.readVariableLengthInt(stream);
                                    if (mode != 0) {
                                       if (mode != 1) {
                                          if (mode != 2) {
                                             break label114;
                                          }

                                          OfflineWorld world = OfflineWorld.of(StreamUtil.readUUID(stream));
                                          Optional<MutexZoneCacheWorld.PathingSignKey> key = MutexZoneCacheWorld.PathingSignKey.readFrom(plugin, stream);
                                          if (!key.isPresent()) {
                                             break label112;
                                          }

                                          MutexZonePath pathMutexx = (MutexZonePath)forWorld(world).byPathingKey.get(key.get());
                                          if (pathMutexx == null) {
                                             break label113;
                                          }

                                          slot = pathMutexx.slot;
                                          break label115;
                                       }

                                       OfflineBlock mutexSignBlock = OfflineBlock.readFrom(stream);
                                       boolean mutexSignFront = stream.readBoolean();
                                       MutexZoneCacheWorld cacheWorld = (MutexZoneCacheWorld)cachesByWorld.get(mutexSignBlock.getWorld());
                                       if (cacheWorld == null) {
                                          break label110;
                                       }

                                       MutexZone zone = cacheWorld.findBySign(mutexSignBlock.getPosition(), mutexSignFront);
                                       if (zone == null) {
                                          break label111;
                                       }

                                       slot = zone.slot;
                                       break label115;
                                    }

                                    slot = (MutexZoneSlot)slotsByName.get(stream.readUTF());
                                    if (slot != null) {
                                       break label115;
                                    }
                                 } catch (Throwable var12) {
                                    if (stream != null) {
                                       try {
                                          stream.close();
                                       } catch (Throwable var11) {
                                          var12.addSuppressed(var11);
                                       }
                                    }

                                    throw var12;
                                 }

                                 if (stream != null) {
                                    stream.close();
                                 }
                                 continue;
                              }

                              if (stream != null) {
                                 stream.close();
                              }
                              continue;
                           }

                           if (stream != null) {
                              stream.close();
                           }
                           continue;
                        }

                        if (stream != null) {
                           stream.close();
                        }
                        continue;
                     }

                     if (stream != null) {
                        stream.close();
                     }
                     continue;
                  }

                  if (stream != null) {
                     stream.close();
                  }
                  continue;
               }

               if (stream != null) {
                  stream.close();
               }
            } catch (Throwable var13) {
               plugin.getLogger().log(Level.SEVERE, "Failed to read data of mutex zone slot", var13);
               continue;
            }

            slot.getEnteredGroups().clear();
            slot.getEnteredGroups().addAll(MutexZoneSlot.UnloadedEnteredGroup.loadAll(plugin, slotData));
         }

      });
   }

   public static MutexZoneCacheWorld forWorld(OfflineWorld world) {
      return (MutexZoneCacheWorld)cachesByWorld.computeIfAbsent(world, MutexZoneCacheWorld::new);
   }

   public static void deinit(TrainCarts plugin) {
      plugin.getOfflineSigns().unregisterHandler(MutexSignMetadata.class);
   }

   public static MutexZonePath getOrCreatePathingMutex(RailLookup.TrackedSign sign, MinecartGroup group, IntVector3 initialBlock, UnaryOperator<MutexZonePath.OptionsBuilder> optionsBuilder) {
      return forWorld(OfflineWorld.of(sign.sign.getWorld())).getOrCreatePathingMutex(sign, group, initialBlock, optionsBuilder);
   }

   private static void addMutexSign(OfflineWorld world, IntVector3 signPosition, boolean isFrontText, MutexSignMetadata metadata) {
      forWorld(world).add(MutexZone.createCuboid(world, signPosition, isFrontText, metadata));
   }

   private static void removeMutexSign(OfflineWorld world, IntVector3 signPosition, boolean frontText) {
      MutexZone zone = forWorld(world).removeAtSign(signPosition, frontText);
      if (zone != null) {
         removeMutexZone(zone);
      }

   }

   public static MutexZone find(OfflineBlock block) {
      return forWorld(block.getWorld()).find(block.getPosition());
   }

   public static MutexZone find(OfflineWorld world, IntVector3 block) {
      return forWorld(world).find(block);
   }

   public static boolean isMutexZoneNearby(OfflineWorld world, IntVector3 block, int radius) {
      return forWorld(world).isMutexZoneNearby(block, radius);
   }

   public static List<MutexZone> findNearbyZones(OfflineWorld world, IntVector3 block, int radius) {
      return forWorld(world).findNearbyZones(block, radius);
   }

   public static synchronized MutexZoneSlot findSlot(String name, MutexZone zone) {
      if (name == null) {
         throw new IllegalArgumentException("Name is null");
      } else {
         MutexZoneSlot slot;
         if (name.isEmpty()) {
            slot = new MutexZoneSlot("");
            slotsList.add(slot);
         } else {
            slot = (MutexZoneSlot)slotsByName.computeIfAbsent(name, (n) -> {
               MutexZoneSlot newSlot = new MutexZoneSlot(n);
               slotsList.add(newSlot);
               return newSlot;
            });
         }

         return slot.addZone(zone);
      }
   }

   private static synchronized void removeMutexZone(MutexZone zone) {
      zone.slot.removeZone(zone);
      if (!zone.slot.hasZones()) {
         if (!zone.slot.isAnonymous()) {
            slotsByName.remove(zone.slot.getName());
         }

         slotsList.remove(zone.slot);
      }

   }

   public static synchronized void refreshAll() {
      if (!slotsList.isEmpty()) {
         for(int i = 0; i < slotsList.size(); ++i) {
            ((MutexZoneSlot)slotsList.get(i)).onTick();
         }
      }

      cachesByWorld.values().forEach(MutexZoneCacheWorld::onTick);
   }

   public static synchronized void unloadGroupInSlots(MinecartGroup group) {
      slotsList.forEach((s) -> {
         s.unload(group);
      });
   }
}
