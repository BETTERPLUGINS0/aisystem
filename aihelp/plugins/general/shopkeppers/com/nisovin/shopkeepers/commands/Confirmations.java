package com.nisovin.shopkeepers.commands;

import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.Nullable;

public class Confirmations {
   public static final int DEFAULT_CONFIRMATION_TICKS = 500;
   private final Plugin plugin;
   private final Map<Object, Confirmations.PendingConfirmation> pendingConfirmations = new HashMap();

   public Confirmations(Plugin plugin) {
      this.plugin = plugin;
   }

   public void onEnable() {
   }

   public void onDisable() {
      this.pendingConfirmations.clear();
   }

   private Object getSenderKey(CommandSender sender) {
      if (sender instanceof Player) {
         return ((Player)sender).getUniqueId();
      } else {
         return sender instanceof ProxiedCommandSender ? this.getSenderKey(((ProxiedCommandSender)sender).getCaller()) : sender.getClass();
      }
   }

   public void onPlayerQuit(Player player) {
      assert player != null;

      this.endConfirmation(player);
   }

   public void awaitConfirmation(CommandSender sender, Runnable action) {
      this.awaitConfirmation(sender, action, 500);
   }

   public void awaitConfirmation(CommandSender sender, Runnable action, int timeoutTicks) {
      Validate.notNull(sender, (String)"sender is null");
      Validate.notNull(action, (String)"action is null");
      Validate.isTrue(timeoutTicks > 0, "timeoutTicks has to be positive");
      int taskId = Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
         this.endConfirmation(sender);
         TextUtils.sendMessage(sender, Messages.confirmationExpired);
      }, (long)timeoutTicks).getTaskId();
      Confirmations.PendingConfirmation previousPendingConfirmation = (Confirmations.PendingConfirmation)this.pendingConfirmations.put(this.getSenderKey(sender), new Confirmations.PendingConfirmation(action, taskId));
      if (previousPendingConfirmation != null) {
         Bukkit.getScheduler().cancelTask(previousPendingConfirmation.getTaskId());
      }

   }

   @Nullable
   public Runnable endConfirmation(CommandSender sender) {
      Validate.notNull(sender, (String)"sender is null");
      Confirmations.PendingConfirmation pendingConfirmation = (Confirmations.PendingConfirmation)this.pendingConfirmations.remove(this.getSenderKey(sender));
      if (pendingConfirmation != null) {
         Bukkit.getScheduler().cancelTask(pendingConfirmation.getTaskId());
         return pendingConfirmation.getAction();
      } else {
         return null;
      }
   }

   public void handleConfirmation(CommandSender sender) {
      Validate.notNull(sender, (String)"sender is null");
      Runnable action = this.endConfirmation(sender);
      if (action != null) {
         action.run();
      } else {
         TextUtils.sendMessage(sender, Messages.nothingToConfirm);
      }

   }

   private static class PendingConfirmation {
      private final Runnable action;
      private final int taskId;

      public PendingConfirmation(Runnable action, int taskId) {
         this.taskId = taskId;
         this.action = action;
      }

      public int getTaskId() {
         return this.taskId;
      }

      public Runnable getAction() {
         return this.action;
      }
   }
}
