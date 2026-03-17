package com.bergerkiller.bukkit.tc.commands.selector;

import java.util.Collection;
import java.util.List;
import org.bukkit.command.CommandSender;

public interface SelectorHandler {
   Collection<String> handle(CommandSender var1, String var2, List<SelectorCondition> var3) throws SelectorException;

   List<SelectorHandlerConditionOption> options(CommandSender var1, String var2, List<SelectorCondition> var3);

   default boolean isCommandHandled(String command) {
      return true;
   }
}
