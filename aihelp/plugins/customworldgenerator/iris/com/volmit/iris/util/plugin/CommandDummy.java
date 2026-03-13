package com.volmit.iris.util.plugin;

import java.util.Set;
import java.util.UUID;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandSender.Spigot;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CommandDummy implements CommandSender {
   public void sendMessage(@NotNull String message) {
   }

   public void sendMessage(@NotNull String... messages) {
   }

   public void sendMessage(@Nullable UUID sender, @NotNull String message) {
   }

   public void sendMessage(@Nullable UUID sender, @NotNull String... messages) {
   }

   @NotNull
   public Server getServer() {
      return null;
   }

   @NotNull
   public String getName() {
      return null;
   }

   @NotNull
   public Spigot spigot() {
      return null;
   }

   public boolean isPermissionSet(@NotNull String name) {
      return false;
   }

   public boolean isPermissionSet(@NotNull org.bukkit.permissions.Permission perm) {
      return false;
   }

   public boolean hasPermission(@NotNull String name) {
      return false;
   }

   public boolean hasPermission(@NotNull org.bukkit.permissions.Permission perm) {
      return false;
   }

   @NotNull
   public PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String name, boolean value) {
      return null;
   }

   @NotNull
   public PermissionAttachment addAttachment(@NotNull Plugin plugin) {
      return null;
   }

   @Nullable
   public PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String name, boolean value, int ticks) {
      return null;
   }

   @Nullable
   public PermissionAttachment addAttachment(@NotNull Plugin plugin, int ticks) {
      return null;
   }

   public void removeAttachment(@NotNull PermissionAttachment attachment) {
   }

   public void recalculatePermissions() {
   }

   @NotNull
   public Set<PermissionAttachmentInfo> getEffectivePermissions() {
      return null;
   }

   public boolean isOp() {
      return false;
   }

   public void setOp(boolean value) {
   }
}
