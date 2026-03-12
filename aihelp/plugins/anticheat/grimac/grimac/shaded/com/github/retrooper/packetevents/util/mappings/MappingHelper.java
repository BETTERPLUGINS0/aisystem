package ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTLimiter;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTNumber;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.serializer.SequentialNBTReader;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import java.util.zip.GZIPInputStream;

@ApiStatus.Internal
public class MappingHelper {
   public static SequentialNBTReader.Compound decompress(final String path) {
      try {
         DataInputStream dataInput = new DataInputStream(new GZIPInputStream(new BufferedInputStream((InputStream)PacketEvents.getAPI().getSettings().getResourceProvider().apply("assets/" + path + ".nbt"))));
         return (SequentialNBTReader.Compound)SequentialNBTReader.INSTANCE.deserializeTag(NBTLimiter.noop(), dataInput);
      } catch (IOException var2) {
         throw new RuntimeException("Cannot find resource file " + path + ".nbt", var2);
      }
   }

   public static List<ListDiff<String>> createListDiff(final SequentialNBTReader.Compound compound) {
      List<ListDiff<String>> diffs = new ArrayList();
      SequentialNBTReader.List removals = (SequentialNBTReader.List)compound.next().getValue();
      Iterator var3 = removals.iterator();

      while(var3.hasNext()) {
         NBT entry = (NBT)var3.next();
         SequentialNBTReader.Compound c = (SequentialNBTReader.Compound)entry;
         diffs.add(new ListDiff.Removal(((NBTNumber)c.next().getValue()).getAsInt(), ((NBTNumber)c.next().getValue()).getAsInt()));
      }

      SequentialNBTReader.List additions = (SequentialNBTReader.List)compound.next().getValue();
      Iterator var9 = additions.iterator();

      while(var9.hasNext()) {
         NBT entry = (NBT)var9.next();
         SequentialNBTReader.Compound c = (SequentialNBTReader.Compound)entry;
         diffs.add(new ListDiff.Addition(((NBTNumber)c.next().getValue()).getAsInt(), (List)StreamSupport.stream(((SequentialNBTReader.List)c.next().getValue()).spliterator(), false).map((nbt) -> {
            return ((NBTString)nbt).getValue();
         }).collect(Collectors.toList())));
      }

      SequentialNBTReader.List changes = (SequentialNBTReader.List)compound.next().getValue();
      Iterator var12 = changes.iterator();

      while(var12.hasNext()) {
         NBT entry = (NBT)var12.next();
         SequentialNBTReader.Compound c = (SequentialNBTReader.Compound)entry;
         diffs.add(new ListDiff.Changed(((NBTNumber)c.next().getValue()).getAsInt(), ((NBTNumber)c.next().getValue()).getAsInt(), (List)StreamSupport.stream(((SequentialNBTReader.List)c.next().getValue()).spliterator(), false).map((nbt) -> {
            return ((NBTString)nbt).getValue();
         }).collect(Collectors.toList())));
      }

      diffs.sort(Comparator.comparingInt(ListDiff::getIndex));
      return diffs;
   }

   public static List<MapDiff<String, Integer>> createDiff(final SequentialNBTReader.Compound compound) {
      List<MapDiff<String, Integer>> diffs = new ArrayList();
      SequentialNBTReader.Compound removal = (SequentialNBTReader.Compound)compound.next().getValue();
      Iterator var3 = removal.iterator();

      while(var3.hasNext()) {
         Entry<String, NBT> entry = (Entry)var3.next();
         diffs.add(new MapDiff.Removal((String)entry.getKey()));
      }

      SequentialNBTReader.Compound additions = (SequentialNBTReader.Compound)compound.next().getValue();
      Iterator var7 = additions.iterator();

      while(var7.hasNext()) {
         Entry<String, NBT> entry = (Entry)var7.next();
         diffs.add(new MapDiff.Addition((String)entry.getKey(), ((NBTNumber)entry.getValue()).getAsInt()));
      }

      return diffs;
   }

   public static <T extends MappedEntity> void registerMapping(TypesBuilder builder, Map<String, T>[] typeNames, Map<Integer, T>[] typeIds, TypesBuilderData typeData, T type) {
      ClientVersion[] var5 = builder.getVersions();
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         ClientVersion version = var5[var7];
         if (typeData.getVersions().contains(version)) {
            int index = builder.getDataIndex(version);
            Map<String, T> nameMap = typeNames[index];
            if (nameMap == null) {
               typeNames[index] = (Map)(nameMap = new HashMap());
            }

            ((Map)nameMap).put(typeData.getName().toString(), type);
            Map<Integer, T> idMap = typeIds[index];
            if (idMap == null) {
               typeIds[index] = (Map)(idMap = new HashMap());
            }

            ((Map)idMap).put(typeData.getId(version), type);
         }
      }

   }

   public static int getId(ClientVersion version, TypesBuilder builder, TypesBuilderData data) {
      return data.getData()[builder.getDataIndex(version)];
   }
}
