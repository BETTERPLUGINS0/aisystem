package com.bergerkiller.bukkit.tc.commands.selector;

import com.bergerkiller.bukkit.common.component.LibraryComponent;
import com.bergerkiller.bukkit.tc.Localization;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.utils.QuoteEscapedString;
import com.bergerkiller.generated.net.minecraft.server.network.PlayerConnectionHandle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.RemoteServerCommandEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class SelectorHandlerRegistry implements Listener, LibraryComponent {
   private static final Pattern CONDITIONS_PATTERN = Pattern.compile("^\\[([\\w\\d\\s\\-\\+=,\\*\\.\\!\\\"\\'\\\\]+)\\](?:\\s|$)");
   private final Map<String, SelectorHandler> handlers = new HashMap();
   private final JavaPlugin plugin;

   public SelectorHandlerRegistry(JavaPlugin plugin) {
      this.plugin = plugin;
   }

   public void enable() {
      Bukkit.getPluginManager().registerEvents(this, this.plugin);
   }

   public void disable() {
   }

   public synchronized void registerMultiple(List<String> selectorNames, SelectorHandler handler) {
      selectorNames.forEach((selectorName) -> {
         this.register(selectorName, handler);
      });
   }

   public synchronized void register(String selectorName, SelectorHandler handler) {
      this.handlers.put(selectorName.toLowerCase(Locale.ENGLISH), handler);
   }

   public synchronized SelectorHandler find(String selectorName) {
      return (SelectorHandler)this.handlers.get(selectorName.toLowerCase(Locale.ENGLISH));
   }

   public synchronized List<String> expandCommands(CommandSender sender, String command) throws SelectorException {
      int commandLength = command.length();
      int maxSelectorValues = 0;
      List<StringBuilder> resultBuilders = null;
      int postLastSelectorStart = 0;
      int postSelectorCommandStart = 0;
      char lastChar = ' ';
      Matcher conditionsMatcher = null;
      int searchIndex = 0;

      while(true) {
         int selectorStartIndex;
         String selector;
         SelectorHandler handler;
         List conditions;
         int valuesCount;
         while(true) {
            String conditionsString;
            while(true) {
               boolean hasConditions;
               int nameEndIndex;
               do {
                  do {
                     label129:
                     while(true) {
                        while(searchIndex < commandLength) {
                           char ch = command.charAt(searchIndex);
                           if (ch == '@' && (Character.isWhitespace(lastChar) || lastChar == '!' || lastChar == '=' || lastChar == '"')) {
                              hasConditions = false;
                              nameEndIndex = searchIndex + 1;

                              while(true) {
                                 if (nameEndIndex >= commandLength) {
                                    break label129;
                                 }

                                 char ch = command.charAt(nameEndIndex);
                                 if (!Character.isAlphabetic(ch) && !Character.isDigit(ch)) {
                                    if (ch == '[') {
                                       hasConditions = true;
                                       break label129;
                                    }

                                    if (Character.isWhitespace(ch)) {
                                       break label129;
                                    }

                                    searchIndex = nameEndIndex;
                                    break;
                                 }

                                 ++nameEndIndex;
                              }
                           } else {
                              lastChar = ch;
                              ++searchIndex;
                           }
                        }

                        if (resultBuilders == null) {
                           return Collections.singletonList(command);
                        }

                        List<String> results = new ArrayList(resultBuilders.size());
                        Iterator var24 = resultBuilders.iterator();

                        while(var24.hasNext()) {
                           StringBuilder builder = (StringBuilder)var24.next();
                           builder.append(command, postSelectorCommandStart, commandLength);
                           results.add(builder.toString());
                        }

                        return results;
                     }

                     selectorStartIndex = searchIndex;
                     searchIndex = nameEndIndex;
                     lastChar = ']';
                     selector = command.substring(selectorStartIndex + 1, nameEndIndex);
                     handler = (SelectorHandler)this.handlers.get(selector.toLowerCase(Locale.ENGLISH));
                  } while(handler == null);

                  for(valuesCount = selectorStartIndex - 1; valuesCount > 0 && command.charAt(valuesCount) == ' '; --valuesCount) {
                  }
               } while(!handler.isCommandHandled(command.substring(0, valuesCount + 1)));

               postSelectorCommandStart = selectorStartIndex + selector.length() + 1;
               if (hasConditions) {
                  if (conditionsMatcher != null) {
                     conditionsMatcher.reset(command.subSequence(nameEndIndex, commandLength));
                  } else {
                     conditionsMatcher = CONDITIONS_PATTERN.matcher(command.subSequence(nameEndIndex, commandLength));
                  }

                  if (!conditionsMatcher.lookingAt()) {
                     continue;
                  }

                  conditionsString = conditionsMatcher.group(1);
                  searchIndex = nameEndIndex + conditionsString.length() + 2;
                  postSelectorCommandStart += conditionsString.length() + 2;
                  break;
               }

               conditionsString = null;
               break;
            }

            if (conditionsString == null) {
               conditions = Collections.emptyList();
               break;
            }

            conditions = SelectorCondition.parseAll(conditionsString);
            if (conditions != null) {
               break;
            }

            if (this.plugin != null) {
               Localization.COMMAND_INPUT_SELECTOR_INVALID.message(sender, new String[]{conditionsString});
            }
         }

         if (maxSelectorValues == 0) {
            if (sender != null && !Permission.COMMAND_UNLIMITED_SELECTORS.has(sender)) {
               if (Permission.COMMAND_USE_SELECTORS.has(sender)) {
                  maxSelectorValues = TCConfig.maxCommandSelectorValues;
               }
            } else {
               maxSelectorValues = Integer.MAX_VALUE;
            }

            if (maxSelectorValues <= 0) {
               Localization.COMMAND_INPUT_SELECTOR_NOPERM.message(sender, new String[0]);
               return Collections.emptyList();
            }
         }

         Collection<String> values = handler.handle(sender, selector, conditions);
         valuesCount = values.size();
         if (valuesCount == 0) {
            return Collections.emptyList();
         }

         if (valuesCount * (resultBuilders == null ? 1 : resultBuilders.size()) > maxSelectorValues) {
            Localization.COMMAND_INPUT_SELECTOR_EXCEEDEDLIMIT.message(sender, new String[0]);
            return Collections.emptyList();
         }

         Iterator builderIter;
         StringBuilder builder;
         String value;
         if (resultBuilders == null) {
            StringBuilder builder = new StringBuilder(command.length());
            builder.append(command, 0, selectorStartIndex);
            resultBuilders = new ArrayList(values.size());
            resultBuilders.add(builder);
         } else {
            value = command.substring(postLastSelectorStart, selectorStartIndex);
            builderIter = resultBuilders.iterator();

            while(builderIter.hasNext()) {
               builder = (StringBuilder)builderIter.next();
               builder.append(value);
            }
         }

         if (valuesCount > 1) {
            int numResults = resultBuilders.size();

            for(int num = 1; num < valuesCount; ++num) {
               for(int i = 0; i < numResults; ++i) {
                  resultBuilders.add(new StringBuilder((CharSequence)resultBuilders.get(i)));
               }
            }

            builderIter = resultBuilders.iterator();
            Iterator var34 = values.iterator();

            while(var34.hasNext()) {
               String value = (String)var34.next();

               for(int i = 0; i < numResults; ++i) {
                  ((StringBuilder)builderIter.next()).append(QuoteEscapedString.quoteEscape(value).getEscaped());
               }
            }
         } else {
            value = (String)values.iterator().next();
            builderIter = resultBuilders.iterator();

            while(builderIter.hasNext()) {
               builder = (StringBuilder)builderIter.next();
               builder.append(QuoteEscapedString.quoteEscape(value).getEscaped());
            }
         }

         postLastSelectorStart = postSelectorCommandStart;
      }
   }

   @EventHandler(
      priority = EventPriority.LOWEST,
      ignoreCancelled = true
   )
   public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
      if (event.getMessage().startsWith("/") && event.getMessage().length() > 1) {
         String command = event.getMessage().substring(1);
         ServerCommandEvent wrapped = new ServerCommandEvent(event.getPlayer(), command);
         this.onServerCommand(wrapped);
         if (!isCancelled(wrapped) && !wrapped.getCommand().isEmpty()) {
            if (!command.equals(wrapped.getCommand())) {
               event.setMessage("/" + wrapped.getCommand());
            }
         } else {
            event.setCancelled(true);
         }
      }

   }

   @EventHandler(
      priority = EventPriority.LOWEST,
      ignoreCancelled = true
   )
   public void onRemoteServerCommand(RemoteServerCommandEvent event) {
      this.onServerCommandBase(event);
   }

   @EventHandler(
      priority = EventPriority.LOWEST,
      ignoreCancelled = true
   )
   public void onServerCommand(ServerCommandEvent event) {
      if (!(event instanceof RemoteServerCommandEvent)) {
         this.onServerCommandBase(event);
      }

   }

   private void onServerCommandBase(ServerCommandEvent event) {
      CommandSender sender = event.getSender();
      String inputCommand = event.getCommand();

      List commands;
      try {
         commands = this.expandCommands(sender, inputCommand);
      } catch (SelectorException var8) {
         sender.sendMessage(ChatColor.RED + "[TrainCarts] " + var8.getMessage());
         cancelCommand(event);
         return;
      }

      if (commands.size() == 1) {
         String replacement = (String)commands.iterator().next();
         if (!replacement.equals(inputCommand)) {
            event.setCommand(replacement);
         }
      } else if (commands.isEmpty()) {
         cancelCommand(event);
      } else {
         Iterator<String> iter = commands.iterator();
         event.setCommand((String)iter.next());

         while(iter.hasNext()) {
            try {
               Bukkit.getServer().dispatchCommand(sender, (String)iter.next());
            } catch (CommandException var7) {
               sender.sendMessage(ChatColor.RED + "An internal error occurred while attempting to perform this command");
               Logger.getLogger(PlayerConnectionHandle.T.getType().getName()).log(Level.SEVERE, (String)null, var7);
            }
         }
      }

   }

   private static boolean isCancelled(ServerCommandEvent event) {
      return event instanceof Cancellable ? event.isCancelled() : event.getCommand().isEmpty();
   }

   private static void cancelCommand(ServerCommandEvent event) {
      if (event instanceof Cancellable) {
         event.setCancelled(true);
      } else {
         event.setCommand("");
      }

   }
}
