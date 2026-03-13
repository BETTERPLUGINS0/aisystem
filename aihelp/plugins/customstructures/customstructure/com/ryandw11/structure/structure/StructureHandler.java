package com.ryandw11.structure.structure;

import com.ryandw11.structure.CustomStructures;
import com.ryandw11.structure.exceptions.StructureConfigurationException;
import com.ryandw11.structure.io.StructureDatabaseHandler;
import com.ryandw11.structure.threading.CheckStructureList;
import com.ryandw11.structure.utils.Pair;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.bukkit.Location;

public class StructureHandler {
   private final SortedMap<Pair<Location, Long>, Structure> spawnedStructures = new TreeMap(Comparator.comparingDouble((o) -> {
      return ((Location)o.getLeft()).distance(new Location(((Location)o.getLeft()).getWorld(), 0.0D, 0.0D, 0.0D));
   }));
   private final List<Structure> structures = new ArrayList();
   private final List<String> names = new ArrayList();
   private final CheckStructureList checkStructureList;
   private StructureDatabaseHandler structureDatabaseHandler;

   public StructureHandler(List<String> stringStructs, CustomStructures cs) {
      cs.getLogger().info("Loading structures from files.");
      Iterator var3 = stringStructs.iterator();

      while(true) {
         while(var3.hasNext()) {
            String s = (String)var3.next();
            String var10002 = String.valueOf(cs.getDataFolder());
            File struct = new File(var10002 + File.separator + "structures" + File.separator + s.replace(".yml", "") + ".yml");
            if (!struct.exists()) {
               cs.getLogger().warning("Structure file: " + s + ".yml does not exist! Did you make a new structure file in the Structure folder?");
               cs.getLogger().warning("For more information please check to wiki.");
            } else {
               try {
                  Structure tempStruct = (new StructureBuilder(s.replace(".yml", ""), struct)).build();
                  this.structures.add(tempStruct);
                  this.names.add(tempStruct.getName());
               } catch (StructureConfigurationException var7) {
                  cs.getLogger().warning("The structure '" + s + "' has an invalid configuration file:");
                  cs.getLogger().warning(var7.getMessage());
               } catch (Exception var8) {
                  cs.getLogger().severe("An unexpected error has occurred when trying to load the structure: " + s + ".");
                  cs.getLogger().severe("Please ensure that your configuration file is valid!");
                  if (cs.isDebug()) {
                     var8.printStackTrace();
                  } else {
                     cs.getLogger().severe("Please enable debug mode to see the full error.");
                  }
               }
            }
         }

         this.checkStructureList = new CheckStructureList(this);
         this.checkStructureList.runTaskTimerAsynchronously(cs, 20L, 6000L);
         if (cs.getConfig().getBoolean("logStructures")) {
            this.structureDatabaseHandler = new StructureDatabaseHandler(cs);
            this.structureDatabaseHandler.runTaskTimerAsynchronously(cs, 20L, 300L);
         }

         return;
      }
   }

   public List<Structure> getStructures() {
      return Collections.unmodifiableList(this.structures);
   }

   public Structure getStructure(String name) {
      List<Structure> result = (List)this.structures.stream().filter((struct) -> {
         return struct.getName().equals(name);
      }).collect(Collectors.toList());
      return result.isEmpty() ? null : (Structure)result.get(0);
   }

   public Structure getStructure(int i) {
      return (Structure)this.structures.get(i);
   }

   public List<String> getStructureNames() {
      return this.names;
   }

   public SortedMap<Pair<Location, Long>, Structure> getSpawnedStructures() {
      return this.spawnedStructures;
   }

   public void putSpawnedStructure(Location loc, Structure struct) {
      synchronized(this.spawnedStructures) {
         if (this.structureDatabaseHandler != null) {
            this.structureDatabaseHandler.addStructure(loc, struct);
         }

         this.spawnedStructures.put(Pair.of(loc, System.currentTimeMillis()), struct);
      }
   }

   public boolean validDistance(Structure struct, Location location) {
      double closest = Double.MAX_VALUE;
      synchronized(this.spawnedStructures) {
         Iterator var6 = this.spawnedStructures.entrySet().iterator();

         while(var6.hasNext()) {
            Entry<Pair<Location, Long>, Structure> entry = (Entry)var6.next();
            if (((Location)((Pair)entry.getKey()).getLeft()).getWorld() == location.getWorld() && ((Location)((Pair)entry.getKey()).getLeft()).distance(location) < closest) {
               closest = ((Location)((Pair)entry.getKey()).getLeft()).distance(location);
            }
         }
      }

      return struct.getStructureLocation().getDistanceFromOthers() < closest;
   }

   public boolean validSameDistance(Structure struct, Location location) {
      synchronized(this.spawnedStructures) {
         double closest = Double.MAX_VALUE;
         Iterator var6 = this.spawnedStructures.entrySet().iterator();

         while(var6.hasNext()) {
            Entry<Pair<Location, Long>, Structure> entry = (Entry)var6.next();
            if (((Location)((Pair)entry.getKey()).getLeft()).getWorld() == location.getWorld() && Objects.equals(((Structure)entry.getValue()).getName(), struct.getName()) && ((Location)((Pair)entry.getKey()).getLeft()).distance(location) < closest) {
               closest = ((Location)((Pair)entry.getKey()).getLeft()).distance(location);
            }
         }

         return struct.getStructureLocation().getDistanceFromSame() < closest;
      }
   }

   public Optional<StructureDatabaseHandler> getStructureDatabaseHandler() {
      return Optional.ofNullable(this.structureDatabaseHandler);
   }

   public void cleanup() {
      this.checkStructureList.cancel();
      if (this.structureDatabaseHandler != null) {
         this.structureDatabaseHandler.cancel();
      }

      this.spawnedStructures.clear();
   }
}
