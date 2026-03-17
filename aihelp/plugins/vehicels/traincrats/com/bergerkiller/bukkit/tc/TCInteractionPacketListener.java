package com.bergerkiller.bukkit.tc;

import com.bergerkiller.bukkit.common.bases.IntVector3;
import com.bergerkiller.bukkit.common.events.PacketReceiveEvent;
import com.bergerkiller.bukkit.common.events.PacketSendEvent;
import com.bergerkiller.bukkit.common.protocol.CommonPacket;
import com.bergerkiller.bukkit.common.protocol.PacketListener;
import com.bergerkiller.bukkit.common.protocol.PacketType;
import com.bergerkiller.bukkit.common.utils.PacketUtil;
import com.bergerkiller.bukkit.common.utils.WorldUtil;
import com.bergerkiller.bukkit.common.wrappers.HumanHand;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.MinecartMemberStore;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayInBlockDigHandle.EnumPlayerDigTypeHandle;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

class TCInteractionPacketListener implements PacketListener {
   private final TCPacketListener mainPacketListener;
   public static final PacketType[] TYPES;

   TCInteractionPacketListener(TCPacketListener mainPacketListener) {
      this.mainPacketListener = mainPacketListener;
   }

   private void cancelBlockChanges(Player player, IntVector3 pos) {
      if (WorldUtil.isLoaded(player.getWorld(), pos.x, pos.y, pos.z)) {
         CommonPacket bcPacket = PacketType.OUT_BLOCK_CHANGE.newInstance();
         bcPacket.write(PacketType.OUT_BLOCK_CHANGE.position, pos);
         bcPacket.write(PacketType.OUT_BLOCK_CHANGE.blockData, WorldUtil.getBlockData(player.getWorld(), pos));
         PacketUtil.sendPacket(player, bcPacket);
      }

   }

   public void onPacketSend(PacketSendEvent event) {
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (TCConfig.optimizeInteraction) {
         if (!event.getPlayer().isSneaking()) {
            if (event.getType() == PacketType.IN_USE_ITEM) {
               this.mainPacketListener.suppressAttacksFor(event.getPlayer(), 250);
            }

            if (event.getType() == PacketType.IN_BLOCK_DIG) {
               String status = ((EnumPlayerDigTypeHandle)event.getPacket().read(PacketType.IN_BLOCK_DIG.status)).toString();
               if (!status.equals("START_DESTROY_BLOCK")) {
                  return;
               }
            }

            boolean isAttackClick = false;
            if (event.getType() == PacketType.IN_BLOCK_DIG) {
               isAttackClick = true;
            } else if (event.getType() == PacketType.IN_ENTITY_ANIMATION) {
               HumanHand hand = PacketType.IN_ENTITY_ANIMATION.getHand(event.getPacket(), event.getPlayer());
               if (hand == HumanHand.getOffHand(event.getPlayer())) {
                  if (this.mainPacketListener.isAttackSuppressed(event.getPlayer())) {
                     event.setCancelled(true);
                     return;
                  }

                  isAttackClick = true;
               }
            }

            MinecartMember member;
            if (isAttackClick) {
               member = MinecartMemberStore.getFromHitTest(Util.getRealEyeLocation(event.getPlayer()));
               if (member != null) {
                  event.setCancelled(true);
                  TCPacketListener.fakeAttack(member, event.getPlayer());
                  if (event.getType() == PacketType.IN_BLOCK_DIG) {
                     IntVector3 pos = (IntVector3)event.getPacket().read(PacketType.IN_BLOCK_DIG.position);
                     this.cancelBlockChanges(event.getPlayer(), pos);
                  }
               }

            } else if (event.getType() == PacketType.IN_BLOCK_PLACE || event.getType() == PacketType.IN_USE_ITEM) {
               member = MinecartMemberStore.getFromHitTest(event.getPlayer().getEyeLocation());
               if (member != null) {
                  HumanHand hand;
                  if (event.getType() == PacketType.IN_BLOCK_PLACE) {
                     hand = PacketType.IN_BLOCK_PLACE.getHand(event.getPacket(), event.getPlayer());
                  } else {
                     hand = PacketType.IN_USE_ITEM.getHand(event.getPacket(), event.getPlayer());
                     IntVector3 pos = (IntVector3)event.getPacket().read(PacketType.IN_USE_ITEM.position);
                     BlockFace dir = (BlockFace)event.getPacket().read(PacketType.IN_USE_ITEM.direction);
                     this.cancelBlockChanges(event.getPlayer(), pos);
                     this.cancelBlockChanges(event.getPlayer(), pos.add(dir));
                  }

                  event.setCancelled(true);
                  this.mainPacketListener.fakeInteraction(member, event.getPlayer(), hand);
               }

            }
         }
      }
   }

   static {
      TYPES = new PacketType[]{PacketType.IN_USE_ITEM, PacketType.IN_BLOCK_PLACE, PacketType.IN_ENTITY_ANIMATION, PacketType.IN_BLOCK_DIG};
   }
}
