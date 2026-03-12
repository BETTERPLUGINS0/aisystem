package ac.grim.grimac.platform.bukkit.player;

import ac.grim.grimac.platform.api.entity.GrimEntity;
import ac.grim.grimac.platform.api.player.PlatformInventory;
import ac.grim.grimac.platform.api.player.PlatformPlayer;
import ac.grim.grimac.platform.api.sender.Sender;
import ac.grim.grimac.platform.bukkit.GrimACBukkitLoaderPlugin;
import ac.grim.grimac.platform.bukkit.entity.BukkitGrimEntity;
import ac.grim.grimac.platform.bukkit.utils.anticheat.MultiLibUtil;
import ac.grim.grimac.platform.bukkit.utils.convert.BukkitConversionUtils;
import ac.grim.grimac.platform.bukkit.utils.reflection.PaperUtils;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.GameMode;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.User;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.SpigotConversionUtil;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.platform.bukkit.BukkitAudiences;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.utils.common.arguments.CommonGrimArguments;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.Generated;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class BukkitPlatformPlayer extends BukkitGrimEntity implements PlatformPlayer {
   private static final BukkitAudiences audiences;
   private final Player bukkitPlayer;
   private final PlatformInventory inventory;
   @Nullable
   private final User user;

   public BukkitPlatformPlayer(Player bukkitPlayer) {
      super(bukkitPlayer);
      this.bukkitPlayer = bukkitPlayer;
      this.inventory = new BukkitPlatformInventory(bukkitPlayer);
      if ((Boolean)CommonGrimArguments.USE_CHAT_FAST_BYPASS.value()) {
         Object channel = PacketEvents.getAPI().getProtocolManager().getChannel(bukkitPlayer.getUniqueId());
         this.user = PacketEvents.getAPI().getProtocolManager().getUser(channel);
      } else {
         this.user = null;
      }

   }

   public void kickPlayer(String textReason) {
      this.bukkitPlayer.kickPlayer(textReason);
   }

   public boolean hasPermission(String s) {
      return this.bukkitPlayer.hasPermission(s);
   }

   public boolean hasPermission(String s, boolean defaultIfUnset) {
      return this.bukkitPlayer.hasPermission(new Permission(s, defaultIfUnset ? PermissionDefault.TRUE : PermissionDefault.FALSE));
   }

   public boolean isSneaking() {
      return this.bukkitPlayer.isSneaking();
   }

   public void setSneaking(boolean isSneaking) {
      this.bukkitPlayer.setSneaking(isSneaking);
   }

   public void sendMessage(String message) {
      if ((Boolean)CommonGrimArguments.USE_CHAT_FAST_BYPASS.value() && this.user != null) {
         this.user.sendMessage(message);
      } else {
         this.bukkitPlayer.sendMessage(message);
      }

   }

   public void sendMessage(Component message) {
      if ((Boolean)CommonGrimArguments.USE_CHAT_FAST_BYPASS.value() && this.user != null) {
         this.user.sendMessage(message);
      } else {
         audiences.player(this.bukkitPlayer).sendMessage(message);
      }

   }

   public boolean isOnline() {
      return this.bukkitPlayer.isOnline();
   }

   public String getName() {
      return this.bukkitPlayer.getName();
   }

   public void updateInventory() {
      this.bukkitPlayer.updateInventory();
   }

   public Vector3d getPosition() {
      if (CAN_USE_DIRECT_GETTERS) {
         return new Vector3d(this.bukkitPlayer.getX(), this.bukkitPlayer.getY(), this.bukkitPlayer.getZ());
      } else {
         Location location = this.bukkitPlayer.getLocation();
         return new Vector3d(location.getX(), location.getY(), location.getZ());
      }
   }

   @Nullable
   public GrimEntity getVehicle() {
      return this.bukkitPlayer.getVehicle() == null ? null : new BukkitGrimEntity(this.bukkitPlayer.getVehicle());
   }

   public GameMode getGameMode() {
      return SpigotConversionUtil.fromBukkitGameMode(this.bukkitPlayer.getGameMode());
   }

   public void setGameMode(GameMode gameMode) {
      this.bukkitPlayer.setGameMode(SpigotConversionUtil.toBukkitGameMode(gameMode));
   }

   public World getBukkitWorld() {
      return this.bukkitPlayer.getWorld();
   }

   public UUID getUniqueId() {
      return this.bukkitPlayer.getUniqueId();
   }

   public boolean eject() {
      return this.bukkitPlayer.eject();
   }

   public CompletableFuture<Boolean> teleportAsync(ac.grim.grimac.utils.math.Location location) {
      Location bLoc = BukkitConversionUtils.toBukkitLocation(location);
      return PaperUtils.teleportAsync(this.bukkitPlayer, bLoc);
   }

   public boolean isExternalPlayer() {
      return MultiLibUtil.isExternalPlayer(this.bukkitPlayer);
   }

   public void sendPluginMessage(String channelName, byte[] byteArray) {
      this.bukkitPlayer.sendPluginMessage(GrimACBukkitLoaderPlugin.LOADER, channelName, byteArray);
   }

   public Sender getSender() {
      return GrimACBukkitLoaderPlugin.LOADER.getBukkitSenderFactory().map((CommandSender)this.bukkitPlayer);
   }

   @NotNull
   public Player getNative() {
      return this.bukkitPlayer;
   }

   @Generated
   public Player getBukkitPlayer() {
      return this.bukkitPlayer;
   }

   @Generated
   public PlatformInventory getInventory() {
      return this.inventory;
   }

   static {
      audiences = BukkitAudiences.create(GrimACBukkitLoaderPlugin.LOADER);
   }
}
