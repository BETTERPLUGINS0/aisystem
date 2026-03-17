package com.bergerkiller.bukkit.tc.offline.train;

import com.bergerkiller.bukkit.common.offline.OfflineWorld;
import com.bergerkiller.bukkit.common.utils.StreamUtil;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class OfflineGroupFileFormatLegacy {
   public static void writeAllWorlds(DataOutputStream stream, List<OfflineGroupWorld> worlds) throws IOException {
      stream.writeInt(worlds.size());
      Iterator var2 = worlds.iterator();

      while(var2.hasNext()) {
         OfflineGroupWorld world = (OfflineGroupWorld)var2.next();
         StreamUtil.writeUUID(stream, world.getWorld().getUniqueId());
         stream.writeInt(world.totalGroupCount());
         Iterator var4 = world.iterator();

         while(var4.hasNext()) {
            OfflineGroup wg = (OfflineGroup)var4.next();
            writeGroup(stream, wg);
         }
      }

   }

   public static List<OfflineGroupWorld> readAllWorlds(DataInputStream stream) throws IOException {
      int worldCount = stream.readInt();
      List<OfflineGroupWorld> worlds = new ArrayList(worldCount);

      for(int worldIdx = 0; worldIdx < worldCount; ++worldIdx) {
         worlds.add(readWorld(stream));
      }

      return Collections.unmodifiableList(worlds);
   }

   public static OfflineGroupWorld readWorld(DataInputStream stream) throws IOException {
      UUID worldUUID = StreamUtil.readUUID(stream);
      int groupCount = stream.readInt();
      return readWorld(stream, worldUUID, groupCount);
   }

   public static OfflineGroupWorld readWorld(DataInputStream stream, UUID worldUUID, int groupCount) throws IOException {
      OfflineWorld world = OfflineWorld.of(worldUUID);
      List<OfflineGroup> groups = new ArrayList(groupCount);

      for(int groupIdx = 0; groupIdx < groupCount; ++groupIdx) {
         groups.add(readGroup(stream, world));
      }

      return OfflineGroupWorld.snapshot(world, groups);
   }

   public static void writeGroup(DataOutputStream stream, OfflineGroup group) throws IOException {
      stream.writeInt(group.members.length);
      OfflineMember[] var2 = group.members;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         OfflineMember member = var2[var4];
         writeMember(stream, member);
      }

      stream.writeUTF(group.name);
   }

   public static void writeMember(DataOutputStream stream, OfflineMember member) throws IOException {
      stream.writeLong(member.entityUID.getMostSignificantBits());
      stream.writeLong(member.entityUID.getLeastSignificantBits());
      stream.writeDouble(member.motX);
      stream.writeDouble(member.motZ);
      stream.writeInt(member.cx);
      stream.writeInt(member.cz);
   }

   public static OfflineGroup readGroup(DataInputStream stream, OfflineWorld world) throws IOException {
      OfflineGroupFileFormatLegacy.LegacyOfflineMemberData[] members = new OfflineGroupFileFormatLegacy.LegacyOfflineMemberData[stream.readInt()];

      for(int i = 0; i < members.length; ++i) {
         members[i] = OfflineGroupFileFormatLegacy.LegacyOfflineMemberData.read(stream);
      }

      String name = stream.readUTF();
      return new OfflineGroup(name, world, Collections.emptyList(), Collections.emptyList(), Arrays.asList(members), (offlineGroup, legacyMember) -> {
         return legacyMember.toOfflineMember(offlineGroup);
      });
   }

   private static class LegacyOfflineMemberData {
      public final UUID entityUID;
      public final int cx;
      public final int cz;
      public final double motX;
      public final double motZ;

      public static OfflineGroupFileFormatLegacy.LegacyOfflineMemberData read(DataInputStream stream) throws IOException {
         return new OfflineGroupFileFormatLegacy.LegacyOfflineMemberData(stream);
      }

      private LegacyOfflineMemberData(DataInputStream stream) throws IOException {
         this.entityUID = new UUID(stream.readLong(), stream.readLong());
         this.motX = stream.readDouble();
         this.motZ = stream.readDouble();
         this.cx = stream.readInt();
         this.cz = stream.readInt();
      }

      public OfflineMember toOfflineMember(OfflineGroup offlineGroup) {
         return new OfflineMember(offlineGroup, this.entityUID, this.cx, this.cz, this.motX, 0.0D, this.motZ, Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
      }
   }
}
