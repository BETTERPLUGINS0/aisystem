package com.bergerkiller.bukkit.tc.offline.train;

import com.bergerkiller.bukkit.common.offline.OfflineWorld;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.bergerkiller.bukkit.common.utils.StreamUtil;
import com.bergerkiller.bukkit.tc.offline.train.format.OfflineDataBlock;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class OfflineGroupFileFormatModern {
   private static void bootstrap(Class<?>... classNames) {
      Class[] var1 = classNames;
      int var2 = classNames.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Class<?> clazz = var1[var3];
         CommonUtil.loadClass(clazz);
      }

   }

   public static void writeAll(DataOutputStream stream, OfflineGroupFileFormatModern.Data data) throws IOException {
      stream.writeInt(1);
      StreamUtil.writeUUID(stream, new UUID(0L, 0L));
      stream.writeInt(0);
      Iterator var2 = data.worlds.iterator();

      while(var2.hasNext()) {
         OfflineGroupWorld world = (OfflineGroupWorld)var2.next();
         writeWorldGroups(data.root, world);
      }

      data.root.writeTo(stream);
   }

   public static OfflineGroupFileFormatModern.Data readAll(DataInputStream stream) throws IOException {
      int worldCount = stream.readInt();
      if (worldCount == 0) {
         return new OfflineGroupFileFormatModern.Data(Collections.emptyList());
      } else {
         UUID firstWorldUUID = StreamUtil.readUUID(stream);
         int firstWorldGroupCount = stream.readInt();
         if (worldCount == 1 && firstWorldUUID.getMostSignificantBits() == 0L && firstWorldUUID.getLeastSignificantBits() == 0L && firstWorldGroupCount == 0) {
            OfflineDataBlock root = OfflineDataBlock.read(stream);
            List<OfflineDataBlock> worldDataList = root == null ? Collections.emptyList() : root.findChildren("world");
            if (worldDataList.isEmpty()) {
               return new OfflineGroupFileFormatModern.Data(Collections.emptyList(), root);
            } else {
               List<OfflineGroupWorld> worlds = new ArrayList(worldDataList.size());
               Iterator var7 = worldDataList.iterator();

               while(var7.hasNext()) {
                  OfflineDataBlock worldData = (OfflineDataBlock)var7.next();
                  worlds.add(readWorldGroups(worldData));
               }

               return new OfflineGroupFileFormatModern.Data(Collections.unmodifiableList(worlds), root);
            }
         } else {
            List<OfflineGroupWorld> worlds = new ArrayList(worldCount);
            worlds.add(OfflineGroupFileFormatLegacy.readWorld(stream, firstWorldUUID, firstWorldGroupCount));

            for(int worldIdx = 1; worldIdx < worldCount; ++worldIdx) {
               worlds.add(OfflineGroupFileFormatLegacy.readWorld(stream));
            }

            return new OfflineGroupFileFormatModern.Data(Collections.unmodifiableList(worlds));
         }
      }
   }

   public static void writeWorldGroups(OfflineDataBlock root, OfflineGroupWorld world) throws IOException {
      OfflineDataBlock worldData = root.addChild("world", (s) -> {
         StreamUtil.writeUUID(s, world.getWorld().getUniqueId());
      });
      Iterator var3 = world.getGroups().iterator();

      while(var3.hasNext()) {
         OfflineGroup group = (OfflineGroup)var3.next();
         writeGroup(worldData, group);
      }

   }

   public static OfflineGroupWorld readWorldGroups(OfflineDataBlock worldGroupData) throws IOException {
      DataInputStream stream = worldGroupData.readData();

      OfflineWorld world;
      try {
         world = OfflineWorld.of(StreamUtil.readUUID(stream));
      } catch (Throwable var8) {
         if (stream != null) {
            try {
               stream.close();
            } catch (Throwable var7) {
               var8.addSuppressed(var7);
            }
         }

         throw var8;
      }

      if (stream != null) {
         stream.close();
      }

      List<OfflineDataBlock> groupListData = worldGroupData.findChildren("group");
      List<OfflineGroup> groups = new ArrayList(groupListData.size());
      Iterator var4 = groupListData.iterator();

      while(var4.hasNext()) {
         OfflineDataBlock groupData = (OfflineDataBlock)var4.next();
         OfflineGroup group = readGroup(groupData, world);
         if (group != null) {
            groups.add(group);
         }
      }

      return OfflineGroupWorld.snapshot(world, groups);
   }

   public static void writeGroup(OfflineDataBlock root, OfflineGroup group) throws IOException {
      OfflineDataBlock groupData = root.addChild("group", (s) -> {
         s.writeUTF(group.name);
      });
      groupData.children.addAll(group.actions);
      groupData.children.addAll(group.skippedSigns);
      OfflineMember[] var3 = group.members;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         OfflineMember member = var3[var5];
         writeMember(groupData, member);
      }

   }

   public static OfflineGroup readGroup(OfflineDataBlock groupData, OfflineWorld world) throws IOException {
      DataInputStream stream = groupData.readData();

      String name;
      try {
         name = stream.readUTF();
      } catch (Throwable var7) {
         if (stream != null) {
            try {
               stream.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }
         }

         throw var7;
      }

      if (stream != null) {
         stream.close();
      }

      List<OfflineDataBlock> members = groupData.findChildren("member");
      return members.isEmpty() ? null : new OfflineGroup(name, world, groupData.findChildren("action"), groupData.findChildren("skipped-sign"), members, OfflineGroupFileFormatModern::readMember);
   }

   public static void writeMember(OfflineDataBlock root, OfflineMember member) throws IOException {
      OfflineDataBlock memberData = root.addChild("member", (s) -> {
         StreamUtil.writeUUID(s, member.entityUID);
         s.writeInt(member.cx);
         s.writeInt(member.cz);
         s.writeDouble(member.motX);
         s.writeDouble(member.motY);
         s.writeDouble(member.motZ);
      });
      memberData.children.addAll(member.actions);
      memberData.children.addAll(member.activeSigns);
      memberData.children.addAll(member.skippedSigns);
   }

   private static OfflineMember readMember(OfflineGroup group, OfflineDataBlock memberData) throws IOException {
      DataInputStream stream = memberData.readData();

      UUID entityUID;
      int cx;
      int cz;
      double motX;
      double motY;
      double motZ;
      try {
         entityUID = StreamUtil.readUUID(stream);
         cx = stream.readInt();
         cz = stream.readInt();
         motX = stream.readDouble();
         motY = stream.readDouble();
         motZ = stream.readDouble();
      } catch (Throwable var15) {
         if (stream != null) {
            try {
               stream.close();
            } catch (Throwable var14) {
               var15.addSuppressed(var14);
            }
         }

         throw var15;
      }

      if (stream != null) {
         stream.close();
      }

      return new OfflineMember(group, entityUID, cx, cz, motX, motY, motZ, memberData.findChildren("action"), memberData.findChildren("sign"), memberData.findChildren("skipped-sign"));
   }

   static {
      bootstrap(DataInputStream.class, DataOutputStream.class, OfflineGroupFileFormatModern.Data.class, OfflineWorld.class, StreamUtil.class, OfflineDataBlock.class, IOException.class, ArrayList.class, Collections.class, List.class, UUID.class);
   }

   public static final class Data {
      public final List<OfflineGroupWorld> worlds;
      public final OfflineDataBlock root;

      public Data(List<OfflineGroupWorld> worlds) {
         this.worlds = worlds;
         this.root = OfflineDataBlock.create("root");
      }

      public Data(List<OfflineGroupWorld> worlds, OfflineDataBlock root) {
         this.worlds = worlds;
         this.root = root;
      }
   }
}
