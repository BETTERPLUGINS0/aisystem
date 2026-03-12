package ac.grim.grimac.manager.init.load;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEventsAPI;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.ChatTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.enchantment.type.EnchantmentTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.particle.type.ParticleTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import ac.grim.grimac.utils.anticheat.LogUtil;
import java.util.concurrent.Executors;

public class PacketEventsInit implements LoadableInitable {
   private final PacketEventsAPI<?> packetEventsAPI;

   public PacketEventsInit(PacketEventsAPI<?> packetEventsAPI) {
      this.packetEventsAPI = packetEventsAPI;
   }

   public void load() {
      LogUtil.info("Loading PacketEvents...");
      PacketEvents.setAPI(this.packetEventsAPI);
      PacketEvents.getAPI().getSettings().fullStackTrace(true).kickOnPacketException(true).preViaInjection(true).checkForUpdates(false).reEncodeByDefault(false).debug(false);
      PacketEvents.getAPI().load();
      Executors.defaultThreadFactory().newThread(() -> {
         StateTypes.AIR.getName();
         ItemTypes.AIR.getName();
         EntityTypes.PLAYER.getParent();
         EntityDataTypes.BOOLEAN.getName();
         ChatTypes.CHAT.getName();
         EnchantmentTypes.ALL_DAMAGE_PROTECTION.getName();
         ParticleTypes.DUST.getName();
      }).start();
   }
}
