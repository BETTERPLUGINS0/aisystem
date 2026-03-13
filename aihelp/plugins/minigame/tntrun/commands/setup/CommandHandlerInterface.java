package tntrun.commands.setup;

import org.bukkit.entity.Player;

public interface CommandHandlerInterface {
   int getMinArgsLength();

   boolean handleCommand(Player var1, String[] var2);
}
