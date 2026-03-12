package me.SuperRonanCraft.BetterRTP.player.commands;

import java.util.List;
import me.SuperRonanCraft.BetterRTP.references.PermissionCheck;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public interface RTPCommand {
   void execute(CommandSender var1, String var2, String[] var3);

   List<String> tabComplete(CommandSender var1, String[] var2);

   @NotNull
   PermissionCheck permission();

   String getName();

   default boolean isDebugOnly() {
      return false;
   }

   default boolean enabled() {
      return true;
   }
}
