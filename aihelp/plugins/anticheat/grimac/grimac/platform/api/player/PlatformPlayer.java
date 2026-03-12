package ac.grim.grimac.platform.api.player;

import ac.grim.grimac.platform.api.entity.GrimEntity;
import ac.grim.grimac.platform.api.sender.Sender;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.GameMode;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;

public interface PlatformPlayer extends GrimEntity, OfflinePlatformPlayer {
   void kickPlayer(String var1);

   boolean isSneaking();

   void setSneaking(boolean var1);

   boolean hasPermission(String var1);

   boolean hasPermission(String var1, boolean var2);

   void sendMessage(String var1);

   void sendMessage(Component var1);

   void updateInventory();

   Vector3d getPosition();

   PlatformInventory getInventory();

   @Nullable
   GrimEntity getVehicle();

   GameMode getGameMode();

   void setGameMode(GameMode var1);

   boolean isExternalPlayer();

   void sendPluginMessage(String var1, byte[] var2);

   Sender getSender();

   default void replaceNativePlayer(Object nativePlayerObject) {
   }
}
