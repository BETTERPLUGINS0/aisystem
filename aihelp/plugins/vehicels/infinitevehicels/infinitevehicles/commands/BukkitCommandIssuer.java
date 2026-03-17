package me.PM2.infinitevehicles.commands;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BukkitCommandIssuer implements CommandIssuer {
   private final BukkitCommandManager manager;
   private final CommandSender sender;

   protected BukkitCommandIssuer(BukkitCommandManager manager, CommandSender sender) {
      this.manager = var1;
      this.sender = var2;
   }

   public boolean isPlayer() {
      return this.sender instanceof Player;
   }

   public CommandSender getIssuer() {
      return this.sender;
   }

   public Player getPlayer() {
      return this.isPlayer() ? (Player)this.sender : null;
   }

   @NotNull
   public UUID getUniqueId() {
      return this.isPlayer() ? ((Player)this.sender).getUniqueId() : UUID.nameUUIDFromBytes(this.sender.getName().getBytes(StandardCharsets.UTF_8));
   }

   public CommandManager getManager() {
      return this.manager;
   }

   public void sendMessageInternal(String message) {
      this.sender.sendMessage(ACFBukkitUtil.color(var1));
   }

   public boolean hasPermission(String name) {
      return this.sender.hasPermission(var1);
   }

   public boolean equals(Object o) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         BukkitCommandIssuer var2 = (BukkitCommandIssuer)var1;
         return Objects.equals(this.sender, var2.sender);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.sender});
   }
}
