package advancedplugins.pm2.cv.command;

import me.PM2.infinitevehicles.commands.CommandAPI;
import me.PM2.infinitevehicles.commands.CommandAPICommand;

public interface PluginCommand {
   CommandAPICommand getCommand();

   default void register() {
      this.getCommand().register();
   }

   default void unregister() {
      CommandAPI.unregister(this.getCommand().getName(), true);
   }

   void setup();
}
