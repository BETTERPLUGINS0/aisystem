package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.stats;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.serializer.SequentialNBTReader;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.MappingHelper;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class Statistics {
   private static final Map<String, Statistic> STATISTIC_MAP = new HashMap();

   public static Statistic getById(String id) {
      return (Statistic)STATISTIC_MAP.get(id);
   }

   static {
      ServerVersion version = PacketEvents.getAPI().getServerManager().getVersion();
      if (version.isOlderThan(ServerVersion.V_1_12_2)) {
         try {
            SequentialNBTReader.Compound rootMapping = MappingHelper.decompress("mappings/data/statistics");

            try {
               rootMapping.skipOne();
               SequentialNBTReader.Compound mapping = (SequentialNBTReader.Compound)rootMapping.next().getValue();
               if (version.isOlderThanOrEquals(ServerVersion.V_1_8_3)) {
                  mapping.skipOne();
               }

               SequentialNBTReader.Compound toLoad = (SequentialNBTReader.Compound)mapping.next().getValue();
               Iterator var4 = toLoad.iterator();

               while(var4.hasNext()) {
                  final Entry<String, NBT> entry = (Entry)var4.next();
                  final String value = ((NBTString)entry.getValue()).getValue();
                  Statistic statistic = new Statistic() {
                     private Component cachedDisplay;

                     public String getId() {
                        return (String)entry.getKey();
                     }

                     public Component display() {
                        if (this.cachedDisplay == null) {
                           this.cachedDisplay = AdventureSerializer.serializer().fromJson(value);
                        }

                        return this.cachedDisplay;
                     }

                     public boolean equals(Object obj) {
                        return obj instanceof Statistic ? ((Statistic)obj).getId().equals(this.getId()) : false;
                     }
                  };
                  STATISTIC_MAP.put((String)entry.getKey(), statistic);
               }
            } catch (Throwable var9) {
               if (rootMapping != null) {
                  try {
                     rootMapping.close();
                  } catch (Throwable var8) {
                     var9.addSuppressed(var8);
                  }
               }

               throw var9;
            }

            if (rootMapping != null) {
               rootMapping.close();
            }
         } catch (IOException var10) {
            throw new RuntimeException("Cannot load statistics mappings", var10);
         }
      }

   }
}
