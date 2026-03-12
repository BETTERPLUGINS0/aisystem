package ac.grim.grimac.events.packets;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.checks.impl.badpackets.BadPacketsW;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketListenerAbstract;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketListenerPriority;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.Attributes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.enchantment.type.EnchantmentTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import ac.grim.grimac.utils.data.packetentity.PacketEntity;
import ac.grim.grimac.utils.data.packetentity.PacketEntityHorse;

public class PacketPlayerAttack extends PacketListenerAbstract {
   public PacketPlayerAttack() {
      super(PacketListenerPriority.LOW);
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {
         WrapperPlayClientInteractEntity interact = new WrapperPlayClientInteractEntity(event);
         GrimPlayer player = GrimAPI.INSTANCE.getPlayerDataManager().getPlayer(event.getUser());
         if (player == null) {
            return;
         }

         if (!player.compensatedEntities.entityMap.containsKey(interact.getEntityId()) && !player.compensatedEntities.serverPositionsMap.containsKey(interact.getEntityId()) && (!player.compensatedEntities.entitiesRemovedThisTick.contains(interact.getEntityId()) || player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_14))) {
            BadPacketsW badPacketsW = (BadPacketsW)player.checkManager.getCheck(BadPacketsW.class);
            if (badPacketsW.flagAndAlert("entityId=" + interact.getEntityId()) && badPacketsW.shouldModifyPackets()) {
               event.setCancelled(true);
               player.onPacketCancel();
            }

            return;
         }

         if (interact.getAction() == WrapperPlayClientInteractEntity.InteractAction.ATTACK) {
            if (player.isResetItemUsageOnAttack()) {
               GrimAPI.INSTANCE.getItemResetHandler().resetItemUsage(player.platformPlayer);
            }

            if (player.compensatedEntities.self.getAttributeValue(Attributes.ATTACK_DAMAGE) <= 0.0D) {
               return;
            }

            ItemStack heldItem = player.inventory.getHeldItem();
            PacketEntity entity = player.compensatedEntities.getEntity(interact.getEntityId());
            if (entity != null && (!entity.isLivingEntity || entity.type == EntityTypes.PLAYER || entity.type == EntityTypes.PAINTING || entity.type == EntityTypes.ENDER_DRAGON && player.getClientVersion().isOlderThan(ClientVersion.V_1_21_2))) {
               int knockbackLevel = player.getClientVersion().isOlderThan(ClientVersion.V_1_21) && heldItem != null ? heldItem.getEnchantmentLevel(EnchantmentTypes.KNOCKBACK) : 0;
               boolean hasNegativeKB = knockbackLevel < 0;
               boolean isLegacyPlayer = player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_8);
               boolean noCooldown = isLegacyPlayer || PacketEvents.getAPI().getServerManager().getVersion().isOlderThan(ServerVersion.V_1_9);
               if (!isLegacyPlayer) {
                  knockbackLevel = Math.max(knockbackLevel, 0);
               }

               if ((!player.lastSprinting || hasNegativeKB || !noCooldown) && knockbackLevel <= 0) {
                  if (!isLegacyPlayer && player.lastSprinting) {
                     if (player.maxAttackSlow > 0 && PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_9) && player.compensatedEntities.self.getAttributeValue(Attributes.ATTACK_SPEED) < 16.0D) {
                        return;
                     }

                     ++player.maxAttackSlow;
                  }
               } else {
                  ++player.minAttackSlow;
                  ++player.maxAttackSlow;
                  if (knockbackLevel == 0) {
                     player.maxAttackSlow = player.minAttackSlow = 1;
                  }
               }
            }
         } else if (interact.getAction() == WrapperPlayClientInteractEntity.InteractAction.INTERACT && player.compensatedEntities.getEntity(interact.getEntityId()) instanceof PacketEntityHorse && player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_13)) {
            player.packetStateData.horseInteractCausedForcedRotation = true;
         }
      }

   }
}
