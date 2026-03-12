package ac.grim.grimac.utils.latency;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.type.PositionCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.ComponentTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemUseCooldown;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.utils.anticheat.update.PositionUpdate;
import ac.grim.grimac.utils.data.CooldownData;
import java.util.Iterator;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class CompensatedCooldown extends Check implements PositionCheck {
   private final ConcurrentHashMap<ResourceLocation, CooldownData> itemCooldownMap = new ConcurrentHashMap();

   public CompensatedCooldown(GrimPlayer playerData) {
      super(playerData);
   }

   public void onPositionUpdate(PositionUpdate positionUpdate) {
      Iterator it = this.itemCooldownMap.entrySet().iterator();

      while(it.hasNext()) {
         Entry<ResourceLocation, CooldownData> entry = (Entry)it.next();
         if (((CooldownData)entry.getValue()).getTransaction() < this.player.lastTransactionReceived.get()) {
            ((CooldownData)entry.getValue()).tick();
         }

         if (((CooldownData)entry.getValue()).getTicksRemaining() <= 0) {
            it.remove();
         }
      }

   }

   public boolean hasItem(ItemStack item) {
      if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
         ItemUseCooldown cooldown = (ItemUseCooldown)item.getComponentOr(ComponentTypes.USE_COOLDOWN, (Object)null);
         if (cooldown != null) {
            Optional<ResourceLocation> cooldownGroup = cooldown.getCooldownGroup();
            if (cooldownGroup.isPresent()) {
               return this.itemCooldownMap.containsKey(cooldownGroup.get());
            }
         }
      }

      return this.itemCooldownMap.containsKey(item.getType().getName());
   }

   public void addCooldown(ResourceLocation location, int cooldown, int transaction) {
      if (cooldown == 0) {
         this.removeCooldown(location);
      } else {
         this.itemCooldownMap.put(location, new CooldownData(cooldown, transaction));
      }
   }

   public void removeCooldown(ResourceLocation location) {
      this.itemCooldownMap.remove(location);
   }
}
