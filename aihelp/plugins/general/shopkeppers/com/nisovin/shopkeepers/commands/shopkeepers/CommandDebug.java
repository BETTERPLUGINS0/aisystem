package com.nisovin.shopkeepers.commands.shopkeepers;

import com.nisovin.shopkeepers.commands.lib.Command;
import com.nisovin.shopkeepers.commands.lib.CommandException;
import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.arguments.FixedValuesArgument;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.debug.DebugOptions;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.text.Text;
import java.util.LinkedHashMap;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

class CommandDebug extends Command {
   private static final String ARGUMENT_DEBUG_OPTION = "option";

   CommandDebug() {
      super("debug");
      this.setPermission("shopkeeper.debug");
      this.setDescription(Messages.commandDescriptionDebug);
      this.addArgument((new CommandDebug.DebugOptionArgument("option")).optional());
   }

   protected void execute(CommandInput input, CommandContextView context) throws CommandException {
      CommandSender sender = input.getSender();
      String debugOption = (String)context.getOrNull("option");
      if (debugOption == null) {
         Settings.debug = !Settings.debug;
         Settings.onSettingsChanged();
         String var10001 = String.valueOf(ChatColor.GREEN);
         sender.sendMessage(var10001 + "Debug mode " + (Settings.debug ? "enabled" : "disabled"));
      } else {
         assert DebugOptions.getAll().contains(debugOption);

         boolean enabled;
         if (Settings.debugOptions.contains(debugOption)) {
            Settings.debugOptions.remove(debugOption);
            enabled = false;
         } else {
            Settings.debugOptions.add(debugOption);
            enabled = true;
         }

         Settings.onSettingsChanged();
         sender.sendMessage(String.valueOf(ChatColor.GREEN) + "Debug option '" + debugOption + "' " + (enabled ? "enabled" : "disabled"));
      }

   }

   private static final class DebugOptionArgument extends FixedValuesArgument {
      private static final Text invalidArgumentMsg = Text.parse("&cUnknown debug option '{argument}'. Available options: " + String.join(", ", DebugOptions.getAll()));

      private static Map<? extends String, ? extends String> getDebugOptionsMap() {
         Map<String, String> debugOptionsMap = new LinkedHashMap();
         DebugOptions.getAll().forEach((debugOption) -> {
            debugOptionsMap.put(debugOption, debugOption);
         });
         return debugOptionsMap;
      }

      public DebugOptionArgument(String name) {
         super(name, getDebugOptionsMap());
      }

      protected Text getInvalidArgumentErrorMsgText() {
         return invalidArgumentMsg;
      }
   }
}
