package me.casperge.realisticseasons.event;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers.ChatType;
import java.util.HashMap;
import java.util.UUID;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.Version;
import me.casperge.realisticseasons.temperature.TemperatureSettings;
import org.bukkit.entity.Player;

public class ActionbarListener {
   private HashMap<UUID, Long> lastReceivedActionbar = new HashMap();
   private HashMap<UUID, Boolean> waitingForTempBar = new HashMap();
   private TemperatureSettings tsettings;

   public ActionbarListener(RealisticSeasons var1) {
      this.tsettings = var1.getTemperatureManager().getTempData().getTempSettings();
      if (Version.is_1_19_3_or_up()) {
         ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(var1, new PacketType[]{Server.SYSTEM_CHAT}) {
            public void onPacketSending(PacketEvent var1) {
               if ((Boolean)var1.getPacket().getBooleans().read(0)) {
                  if (ActionbarListener.this.isWaitingForTemp(var1.getPlayer())) {
                     ActionbarListener.this.setWaitingForTempBar(var1.getPlayer(), false);
                  } else {
                     ActionbarListener.this.lastReceivedActionbar.put(var1.getPlayer().getUniqueId(), System.currentTimeMillis());
                  }
               }

            }
         });
      } else if (Version.is_1_19_or_up()) {
         ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(var1, new PacketType[]{Server.SYSTEM_CHAT}) {
            public void onPacketSending(PacketEvent var1) {
               if ((Integer)var1.getPacket().getIntegers().read(0) == 2) {
                  if (ActionbarListener.this.isWaitingForTemp(var1.getPlayer())) {
                     ActionbarListener.this.setWaitingForTempBar(var1.getPlayer(), false);
                  } else {
                     ActionbarListener.this.lastReceivedActionbar.put(var1.getPlayer().getUniqueId(), System.currentTimeMillis());
                  }
               }

            }
         });
      } else {
         ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(var1, new PacketType[]{Server.CHAT}) {
            public void onPacketSending(PacketEvent var1) {
               if (var1.getPacket().getChatTypes().read(0) == ChatType.GAME_INFO) {
                  if (ActionbarListener.this.isWaitingForTemp(var1.getPlayer())) {
                     ActionbarListener.this.setWaitingForTempBar(var1.getPlayer(), false);
                  } else {
                     ActionbarListener.this.lastReceivedActionbar.put(var1.getPlayer().getUniqueId(), System.currentTimeMillis());
                  }
               }

            }
         });
      }

      ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(var1, new PacketType[]{Server.SET_ACTION_BAR_TEXT}) {
         public void onPacketSending(PacketEvent var1) {
            ActionbarListener.this.lastReceivedActionbar.put(var1.getPlayer().getUniqueId(), System.currentTimeMillis());
         }
      });
   }

   public boolean isWaitingForTemp(Player var1) {
      return !this.waitingForTempBar.containsKey(var1.getUniqueId()) ? false : (Boolean)this.waitingForTempBar.get(var1.getUniqueId());
   }

   public void setWaitingForTempBar(Player var1, boolean var2) {
      this.waitingForTempBar.put(var1.getUniqueId(), var2);
   }

   public boolean canReceiveTempBar(Player var1) {
      if (this.tsettings.isOverwriteActionbar()) {
         return true;
      } else if (!this.lastReceivedActionbar.containsKey(var1.getUniqueId())) {
         return true;
      } else if ((Long)this.lastReceivedActionbar.get(var1.getUniqueId()) + 4000L > System.currentTimeMillis()) {
         return false;
      } else {
         this.lastReceivedActionbar.remove(var1.getUniqueId());
         return true;
      }
   }
}
