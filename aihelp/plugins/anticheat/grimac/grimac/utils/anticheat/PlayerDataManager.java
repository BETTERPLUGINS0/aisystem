package ac.grim.grimac.utils.anticheat;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.api.event.events.GrimJoinEvent;
import ac.grim.grimac.api.event.events.GrimQuitEvent;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.channel.ChannelHelper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.User;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.utils.reflection.GeyserUtil;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerDataManager {
   public final Collection<User> exemptUsers = ConcurrentHashMap.newKeySet();
   private final ConcurrentHashMap<User, GrimPlayer> playerDataMap = new ConcurrentHashMap();

   @Nullable
   public GrimPlayer getPlayer(@NotNull UUID uuid) {
      Object channel = PacketEvents.getAPI().getProtocolManager().getChannel(uuid);
      User user = PacketEvents.getAPI().getProtocolManager().getUser(channel);
      return this.getPlayer(user);
   }

   @Nullable
   public GrimPlayer getPlayer(@NotNull User user) {
      GrimPlayer player = (GrimPlayer)this.playerDataMap.get(user);
      return player != null && player.platformPlayer != null && player.platformPlayer.isExternalPlayer() ? null : player;
   }

   public boolean shouldCheck(@NotNull User user) {
      if (this.exemptUsers.contains(user)) {
         return false;
      } else if (!ChannelHelper.isOpen(user.getChannel())) {
         return false;
      } else {
         if (user.getUUID() != null) {
            if (GeyserUtil.isBedrockPlayer(user.getUUID())) {
               this.exemptUsers.add(user);
               return false;
            }

            GrimPlayer grimPlayer = GrimAPI.INSTANCE.getPlayerDataManager().getPlayer(user);
            if (grimPlayer != null && grimPlayer.hasPermission("grim.exempt")) {
               this.exemptUsers.add(user);
               return false;
            }

            if (user.getUUID().toString().startsWith("00000000-0000-0000-0009")) {
               this.exemptUsers.add(user);
               return false;
            }
         }

         return true;
      }
   }

   public void addUser(@NotNull User user) {
      if (this.shouldCheck(user)) {
         GrimPlayer player = new GrimPlayer(user);
         this.playerDataMap.put(user, player);
         GrimAPI.INSTANCE.getEventBus().post(new GrimJoinEvent(player));
      }

   }

   public GrimPlayer remove(@NotNull User user) {
      return (GrimPlayer)this.playerDataMap.remove(user);
   }

   public void onDisconnect(User user) {
      GrimPlayer grimPlayer = this.remove(user);
      if (grimPlayer != null) {
         GrimAPI.INSTANCE.getEventBus().post(new GrimQuitEvent(grimPlayer));
      }

      this.exemptUsers.remove(user);
      UUID uuid = user.getProfile().getUUID();
      if (uuid != null) {
         GrimAPI.INSTANCE.getAlertManager().handlePlayerQuit(GrimAPI.INSTANCE.getPlatformPlayerFactory().getFromUUID(uuid));
         GrimAPI.INSTANCE.getSpectateManager().onQuit(uuid);
         GrimAPI.INSTANCE.getPlatformPlayerFactory().invalidatePlayer(uuid);
      }
   }

   public Collection<GrimPlayer> getEntries() {
      return this.playerDataMap.values();
   }

   public int size() {
      return this.playerDataMap.size();
   }
}
