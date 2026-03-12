package ac.grim.grimac.platform.bukkit.events;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.platform.bukkit.utils.convert.BukkitConversionUtils;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockFace;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import ac.grim.grimac.utils.data.PistonData;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;

public class PistonEvent implements Listener {
   private final Material SLIME_BLOCK = Material.getMaterial("SLIME_BLOCK");
   private final Material HONEY_BLOCK = Material.getMaterial("HONEY_BLOCK");
   private static final double MAX_HORIZONTAL_DISTANCE = 24.0D;
   private static final double MAX_VERTICAL_DISTANCE = 64.0D;

   private static boolean isCloseEnough(Vector3i vectorA, Vector3d vectorB) {
      return Math.abs((double)vectorA.getX() - vectorB.getX()) <= 24.0D && Math.abs((double)vectorA.getY() - vectorB.getY()) <= 64.0D && Math.abs((double)vectorA.getZ() - vectorB.getZ()) <= 24.0D;
   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   public void onPistonPushEvent(BlockPistonExtendEvent event) {
      boolean hasSlimeBlock = false;
      boolean hasHoneyBlock = false;
      List<SimpleCollisionBox> boxes = new ArrayList();
      Iterator var5 = event.getBlocks().iterator();

      while(var5.hasNext()) {
         Block block = (Block)var5.next();
         boxes.add((new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D, true)).offset((double)block.getX(), (double)block.getY(), (double)block.getZ()));
         boxes.add((new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D, true)).offset((double)(block.getX() + event.getDirection().getModX()), (double)(block.getY() + event.getDirection().getModY()), (double)(block.getZ() + event.getDirection().getModZ())));
         if (block.getType() == this.SLIME_BLOCK) {
            hasSlimeBlock = true;
         }

         if (block.getType() == this.HONEY_BLOCK) {
            hasHoneyBlock = true;
         }
      }

      Block piston = event.getBlock();
      boxes.add((new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D, true)).offset((double)(piston.getX() + event.getDirection().getModX()), (double)(piston.getY() + event.getDirection().getModY()), (double)(piston.getZ() + event.getDirection().getModZ())));
      int chunkX = event.getBlock().getX() >> 4;
      int chunkZ = event.getBlock().getZ() >> 4;
      BlockFace blockFace = BukkitConversionUtils.fromBukkitFace(event.getDirection());
      Vector3i sourcePos = new Vector3i(piston.getX(), piston.getY(), piston.getZ());
      Iterator var10 = GrimAPI.INSTANCE.getPlayerDataManager().getEntries().iterator();

      while(var10.hasNext()) {
         GrimPlayer player = (GrimPlayer)var10.next();
         if (isCloseEnough(sourcePos, player.compensatedEntities.self.trackedServerPosition.getPos()) && player.compensatedWorld.isChunkLoaded(chunkX, chunkZ)) {
            int lastTrans = player.lastTransactionSent.get();
            PistonData data = new PistonData(blockFace, boxes, lastTrans, true, hasSlimeBlock, hasHoneyBlock);
            player.latencyUtils.addRealTimeTaskAsync(lastTrans, () -> {
               player.compensatedWorld.activePistons.add(data);
            });
         }
      }

   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   public void onPistonRetractEvent(BlockPistonRetractEvent event) {
      boolean hasSlimeBlock = false;
      boolean hasHoneyBlock = false;
      List<SimpleCollisionBox> boxes = new ArrayList();
      BlockFace face = BukkitConversionUtils.fromBukkitFace(event.getDirection());
      if (event.getBlocks().isEmpty()) {
         Block piston = event.getBlock();
         boxes.add((new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D, true)).offset((double)(piston.getX() + face.getModX()), (double)(piston.getY() + face.getModY()), (double)(piston.getZ() + face.getModZ())));
      }

      Iterator var13 = event.getBlocks().iterator();

      while(var13.hasNext()) {
         Block block = (Block)var13.next();
         boxes.add((new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D, true)).offset((double)block.getX(), (double)block.getY(), (double)block.getZ()));
         boxes.add((new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D, true)).offset((double)(block.getX() + face.getModX()), (double)(block.getY() + face.getModY()), (double)(block.getZ() + face.getModZ())));
         if (block.getType() == this.SLIME_BLOCK) {
            hasSlimeBlock = true;
         }

         if (block.getType() == this.HONEY_BLOCK) {
            hasHoneyBlock = true;
         }
      }

      int chunkX = event.getBlock().getX() >> 4;
      int chunkZ = event.getBlock().getZ() >> 4;
      Vector3i sourcePos = new Vector3i(event.getBlock().getX(), event.getBlock().getY(), event.getBlock().getZ());
      Iterator var9 = GrimAPI.INSTANCE.getPlayerDataManager().getEntries().iterator();

      while(var9.hasNext()) {
         GrimPlayer player = (GrimPlayer)var9.next();
         if (isCloseEnough(sourcePos, player.compensatedEntities.self.trackedServerPosition.getPos()) && player.compensatedWorld.isChunkLoaded(chunkX, chunkZ)) {
            int lastTrans = player.lastTransactionSent.get();
            PistonData data = new PistonData(face, boxes, lastTrans, false, hasSlimeBlock, hasHoneyBlock);
            player.latencyUtils.addRealTimeTaskAsync(lastTrans, () -> {
               player.compensatedWorld.activePistons.add(data);
            });
         }
      }

   }
}
