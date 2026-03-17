package com.bergerkiller.bukkit.tc.debug.particles;

import com.bergerkiller.bukkit.common.math.Quaternion;
import com.bergerkiller.bukkit.common.utils.EntityUtil;
import com.bergerkiller.bukkit.common.utils.MaterialUtil;
import com.bergerkiller.bukkit.common.utils.MathUtil;
import com.bergerkiller.bukkit.common.utils.PacketUtil;
import com.bergerkiller.bukkit.common.wrappers.BlockData;
import com.bergerkiller.bukkit.common.wrappers.Brightness;
import com.bergerkiller.bukkit.common.wrappers.DataWatcher;
import com.bergerkiller.bukkit.common.wrappers.DataWatcher.Prototype;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.attachments.VirtualDisplayEntity;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutEntityDestroyHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutEntityMetadataHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutSpawnEntityHandle;
import com.bergerkiller.generated.net.minecraft.world.entity.DisplayHandle;
import com.bergerkiller.generated.net.minecraft.world.entity.EntityHandle;
import com.bergerkiller.generated.net.minecraft.world.entity.DisplayHandle.BlockDisplayHandle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

class DebugParticlesDisplay extends DebugParticles {
   private static final int DURATION = 100;
   private static final int BRIGHTNESS_RANGE = 4;
   private static final int BRIGHTNESS_STEPS = 2;
   private final List<DebugParticlesDisplay.DisplayTask> displayTasks = new ArrayList();
   private static final Prototype LINE_METADATA;
   private static final Prototype POINT_METADATA;

   protected DebugParticlesDisplay(Player player) {
      super(player);
   }

   public void cube(Color color, double x1, double y1, double z1, double x2, double y2, double z2) {
      double minSize = 0.02D * Util.absMinAxis(new Vector(x2 - x1, y2 - y1, z2 - z1));
      double lineThickness = Math.min(0.3D, minSize);
      this.cube(color, DebugParticlesDisplay.ConcretePalette.getConcrete(color), x1, y1, z1, x2, y2, z2, lineThickness);
   }

   public void face(Color color, double x1, double y1, double z1, double x2, double y2, double z2) {
      double dist = MathUtil.distance(x1, y1, z1, x2, y2, z2);
      double minSize = 0.02D * dist;
      double lineThickness = Math.min(0.3D, minSize);
      this.face(color, DebugParticlesDisplay.ConcretePalette.getConcrete(color), x1, y1, z1, x2, y2, z2, lineThickness);
   }

   public void line(Color color, double x1, double y1, double z1, double x2, double y2, double z2) {
      double dist = MathUtil.distance(x1, y1, z1, x2, y2, z2);
      double minSize = 0.02D * dist;
      double lineThickness = Math.min(0.3D, minSize);
      this.line(color, DebugParticlesDisplay.ConcretePalette.getConcrete(color), x1, y1, z1, x2, y2, z2, lineThickness);
   }

   private void cube(Color color, BlockData concrete, double x1, double y1, double z1, double x2, double y2, double z2, double lineThickness) {
      this.face(color, concrete, x1, y1, z1, x2, y1, z2, lineThickness);
      this.face(color, concrete, x1, y2, z1, x2, y2, z2, lineThickness);
      this.line(color, concrete, x1, y1, z1, x1, y2, z1, lineThickness);
      this.line(color, concrete, x2, y1, z1, x2, y2, z1, lineThickness);
      this.line(color, concrete, x1, y1, z2, x1, y2, z2, lineThickness);
      this.line(color, concrete, x2, y1, z2, x2, y2, z2, lineThickness);
   }

   private void face(Color color, BlockData concrete, double x1, double y1, double z1, double x2, double y2, double z2, double lineThickness) {
      this.line(color, concrete, x1, y1, z1, x2, y1, z1, lineThickness);
      this.line(color, concrete, x1, y1, z1, x1, y2, z1, lineThickness);
      this.line(color, concrete, x1, y1, z1, x1, y1, z2, lineThickness);
      this.line(color, concrete, x1, y2, z2, x2, y2, z2, lineThickness);
      this.line(color, concrete, x2, y1, z2, x2, y2, z2, lineThickness);
      this.line(color, concrete, x2, y2, z1, x2, y2, z2, lineThickness);
   }

   private void line(Color color, BlockData concrete, double x1, double y1, double z1, double x2, double y2, double z2, double lineThickness) {
      double dist = MathUtil.distance(x1, y1, z1, x2, y2, z2);
      if (!(dist <= 1.0E-6D)) {
         Quaternion rotation = Quaternion.fromLookDirection(new Vector(x2 - x1, y2 - y1, z2 - z1), new Vector(0.0D, 1.0D, 0.0D));
         int entityId = EntityUtil.getUniqueEntityId();
         UUID entityUUID = UUID.randomUUID();
         DataWatcher metadata = LINE_METADATA.create();
         if (!TCConfig.debugMutexGlow) {
            metadata.setFlag(EntityHandle.DATA_FLAGS, 64, false);
         }

         Vector translation = new Vector(-0.5D * lineThickness, -0.5D * lineThickness, -0.5D * dist);
         rotation.transformPoint(translation);
         metadata.set(DisplayHandle.DATA_TRANSLATION, translation);
         metadata.set(DisplayHandle.DATA_LEFT_ROTATION, rotation);
         metadata.set(DisplayHandle.DATA_SCALE, new Vector(lineThickness, lineThickness, dist));
         metadata.set(DisplayHandle.DATA_GLOW_COLOR_OVERRIDE, color.asRGB());
         metadata.set(BlockDisplayHandle.DATA_BLOCK_STATE, concrete);
         DebugParticlesDisplay.DisplayTask task = new DebugParticlesDisplay.DisplayTask(entityId, color, metadata);
         task.applyBrightness();
         PacketPlayOutSpawnEntityHandle spawnPacket = PacketPlayOutSpawnEntityHandle.createNew();
         spawnPacket.setEntityId(entityId);
         spawnPacket.setEntityUUID(entityUUID);
         spawnPacket.setEntityType(VirtualDisplayEntity.BLOCK_DISPLAY_ENTITY_TYPE);
         spawnPacket.setPosX(0.5D * (x1 + x2));
         spawnPacket.setPosY(0.5D * (y1 + y2));
         spawnPacket.setPosZ(0.5D * (z1 + z2));
         spawnPacket.setMotX(0.0D);
         spawnPacket.setMotY(0.0D);
         spawnPacket.setMotZ(0.0D);
         spawnPacket.setYaw(0.0F);
         spawnPacket.setPitch(0.0F);
         PacketUtil.sendPacket(this.player, spawnPacket);
         PacketUtil.sendPacket(this.player, PacketPlayOutEntityMetadataHandle.createNew(entityId, metadata, true));
         this.displayTasks.add(task);
         this.startUpdating();
      }
   }

   public void point(Color color, double x, double y, double z) {
      int entityId = EntityUtil.getUniqueEntityId();
      UUID entityUUID = UUID.randomUUID();
      DataWatcher metadata = POINT_METADATA.create();
      if (!TCConfig.debugMutexGlow) {
         metadata.setFlag(EntityHandle.DATA_FLAGS, 64, false);
      }

      metadata.set(DisplayHandle.DATA_GLOW_COLOR_OVERRIDE, color.asRGB());
      metadata.set(BlockDisplayHandle.DATA_BLOCK_STATE, DebugParticlesDisplay.ConcretePalette.getConcrete(color));
      DebugParticlesDisplay.DisplayTask task = new DebugParticlesDisplay.DisplayTask(entityId, color, metadata);
      task.applyBrightness();
      PacketPlayOutSpawnEntityHandle spawnPacket = PacketPlayOutSpawnEntityHandle.createNew();
      spawnPacket.setEntityId(entityId);
      spawnPacket.setEntityUUID(entityUUID);
      spawnPacket.setEntityType(VirtualDisplayEntity.BLOCK_DISPLAY_ENTITY_TYPE);
      spawnPacket.setPosX(x);
      spawnPacket.setPosY(y);
      spawnPacket.setPosZ(z);
      spawnPacket.setMotX(0.0D);
      spawnPacket.setMotY(0.0D);
      spawnPacket.setMotZ(0.0D);
      spawnPacket.setYaw(0.0F);
      spawnPacket.setPitch(0.0F);
      PacketUtil.sendPacket(this.player, spawnPacket);
      PacketUtil.sendPacket(this.player, PacketPlayOutEntityMetadataHandle.createNew(entityId, metadata, true));
      this.displayTasks.add(task);
      this.startUpdating();
   }

   protected boolean update() {
      this.displayTasks.removeIf((d) -> {
         return d.update(this.player);
      });
      return this.displayTasks.isEmpty();
   }

   static {
      LINE_METADATA = VirtualDisplayEntity.BASE_DISPLAY_METADATA.modify().setByte(EntityHandle.DATA_FLAGS, 64).set(DisplayHandle.DATA_INTERPOLATION_DURATION, 0).set(DisplayHandle.DATA_BRIGHTNESS_OVERRIDE, Brightness.blockLight(15)).setClientDefault(DisplayHandle.DATA_GLOW_COLOR_OVERRIDE, -1).setClientDefault(BlockDisplayHandle.DATA_BLOCK_STATE, BlockData.AIR).create();
      double scale = 0.1D;
      POINT_METADATA = VirtualDisplayEntity.BASE_DISPLAY_METADATA.modify().set(DisplayHandle.DATA_TRANSLATION, new Vector(-0.5D * scale, -0.5D * scale, -0.5D * scale)).set(DisplayHandle.DATA_SCALE, new Vector(scale, scale, scale)).setByte(EntityHandle.DATA_FLAGS, 64).set(DisplayHandle.DATA_INTERPOLATION_DURATION, 0).set(DisplayHandle.DATA_BRIGHTNESS_OVERRIDE, Brightness.blockLight(15)).setClientDefault(DisplayHandle.DATA_GLOW_COLOR_OVERRIDE, -1).setClientDefault(BlockDisplayHandle.DATA_BLOCK_STATE, BlockData.AIR).create();
   }

   private static class ConcretePalette {
      public static final List<DebugParticlesDisplay.ConcretePalette.Entry> entries = Arrays.asList(new DebugParticlesDisplay.ConcretePalette.Entry("WHITE_CONCRETE", 255, 255, 255), new DebugParticlesDisplay.ConcretePalette.Entry("ORANGE_CONCRETE", 220, 95, 0), new DebugParticlesDisplay.ConcretePalette.Entry("MAGENTA_CONCRETE", 168, 49, 158), new DebugParticlesDisplay.ConcretePalette.Entry("LIGHT_BLUE_CONCRETE", 35, 134, 196), new DebugParticlesDisplay.ConcretePalette.Entry("YELLOW_CONCRETE", 237, 172, 21), new DebugParticlesDisplay.ConcretePalette.Entry("LIME_CONCRETE", 92, 165, 24), new DebugParticlesDisplay.ConcretePalette.Entry("PINK_CONCRETE", 211, 100, 141), new DebugParticlesDisplay.ConcretePalette.Entry("GRAY_CONCRETE", 53, 56, 60), new DebugParticlesDisplay.ConcretePalette.Entry("LIGHT_GRAY_CONCRETE", 125, 125, 115), new DebugParticlesDisplay.ConcretePalette.Entry("CYAN_CONCRETE", 21, 117, 133), new DebugParticlesDisplay.ConcretePalette.Entry("PURPLE_CONCRETE", 99, 31, 154), new DebugParticlesDisplay.ConcretePalette.Entry("BLUE_CONCRETE", 44, 46, 142), new DebugParticlesDisplay.ConcretePalette.Entry("BROWN_CONCRETE", 96, 59, 32), new DebugParticlesDisplay.ConcretePalette.Entry("GREEN_CONCRETE", 73, 91, 37), new DebugParticlesDisplay.ConcretePalette.Entry("RED_CONCRETE", 141, 35, 35), new DebugParticlesDisplay.ConcretePalette.Entry("BLACK_CONCRETE", 0, 0, 0));

      public static BlockData getConcrete(Color color) {
         BlockData best = ((DebugParticlesDisplay.ConcretePalette.Entry)entries.get(0)).data;
         long bestDistSq = Long.MAX_VALUE;
         Iterator var4 = entries.iterator();

         while(var4.hasNext()) {
            DebugParticlesDisplay.ConcretePalette.Entry e = (DebugParticlesDisplay.ConcretePalette.Entry)var4.next();
            long dist = calcColourDistanceSq(e.color, color);
            if (dist < bestDistSq) {
               bestDistSq = dist;
               best = e.data;
            }
         }

         return best;
      }

      private static long calcColourDistanceSq(Color c1, Color c2) {
         long rmean = ((long)c1.getRed() + (long)c2.getRed()) / 2L;
         long r = (long)c1.getRed() - (long)c2.getRed();
         long g = (long)c1.getGreen() - (long)c2.getGreen();
         long b = (long)c1.getBlue() - (long)c2.getBlue();
         return ((512L + rmean) * r * r >> 8) + 4L * g * g + ((767L - rmean) * b * b >> 8);
      }

      private static class Entry {
         public final BlockData data;
         public final Color color;

         public Entry(String name, int r, int g, int b) {
            this.data = BlockData.fromMaterial(MaterialUtil.getMaterial(name));
            this.color = Color.fromRGB(r, g, b);
         }
      }
   }

   private static class DisplayTask {
      public final int entityId;
      public final Color color;
      public final DataWatcher metadata;
      public int age;
      public int brightness;

      public DisplayTask(int entityId, Color color, DataWatcher metadata) {
         this.entityId = entityId;
         this.color = color;
         this.metadata = metadata;
         this.age = 0;
      }

      public void applyBrightness() {
         int brightness = this.age % 8;
         if (brightness > 4) {
            brightness = 8 - brightness;
         }

         brightness *= 2;
         brightness += 7;
         this.metadata.set(DisplayHandle.DATA_BRIGHTNESS_OVERRIDE, Brightness.blockLight(brightness));
         this.metadata.set(DisplayHandle.DATA_GLOW_COLOR_OVERRIDE, Color.fromRGB(this.color.getRed() * brightness / 15, this.color.getGreen() * brightness / 15, this.color.getBlue() * brightness / 15).asRGB());
      }

      public boolean update(Player viewer) {
         if (++this.age >= 100) {
            PacketUtil.sendPacket(viewer, PacketPlayOutEntityDestroyHandle.createNewSingle(this.entityId));
            return true;
         } else {
            this.applyBrightness();
            PacketUtil.sendPacket(viewer, PacketPlayOutEntityMetadataHandle.createNew(this.entityId, this.metadata, false));
            return false;
         }
      }
   }
}
