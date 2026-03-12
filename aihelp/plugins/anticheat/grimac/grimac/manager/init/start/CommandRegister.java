package ac.grim.grimac.manager.init.start;

import ac.grim.grimac.platform.api.command.CommandService;
import ac.grim.grimac.utils.anticheat.LogUtil;

public record CommandRegister(CommandService service) implements StartableInitable {
   public CommandRegister(CommandService service) {
      this.service = service;
   }

   public void start() {
      try {
         if (this.service != null) {
            this.service.registerCommands();
         }
      } catch (Throwable var2) {
         LogUtil.error("Failed to register commands! Grim will run without command support.", var2);
      }

   }

   public CommandService service() {
      return this.service;
   }
}
