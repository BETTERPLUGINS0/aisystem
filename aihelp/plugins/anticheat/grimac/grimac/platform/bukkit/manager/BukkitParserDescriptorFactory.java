package ac.grim.grimac.platform.bukkit.manager;

import ac.grim.grimac.platform.api.command.PlayerSelector;
import ac.grim.grimac.platform.api.manager.cloud.CloudCommandAdapter;
import ac.grim.grimac.platform.api.sender.Sender;
import ac.grim.grimac.platform.bukkit.command.BukkitPlayerSelectorParser;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.BukkitCommandContextKeys;
import ac.grim.grimac.shaded.incendo.cloud.parser.ParserDescriptor;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.Suggestion;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.SuggestionProvider;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BukkitParserDescriptorFactory implements CloudCommandAdapter {
   private final BukkitPlayerSelectorParser<Sender> bukkitPlayerSelectorParser = new BukkitPlayerSelectorParser();

   public ParserDescriptor<Sender, PlayerSelector> singlePlayerSelectorParser() {
      return this.bukkitPlayerSelectorParser.descriptor();
   }

   public SuggestionProvider<Sender> onlinePlayerSuggestions() {
      return (context, input) -> {
         List<Suggestion> suggestions = new ArrayList();
         Iterator var3 = Bukkit.getOnlinePlayers().iterator();

         while(true) {
            Player player;
            CommandSender bukkit;
            do {
               if (!var3.hasNext()) {
                  return CompletableFuture.completedFuture(suggestions);
               }

               player = (Player)var3.next();
               bukkit = (CommandSender)context.get(BukkitCommandContextKeys.BUKKIT_COMMAND_SENDER);
            } while(bukkit instanceof Player && !((Player)bukkit).canSee(player));

            suggestions.add(Suggestion.suggestion(player.getName()));
         }
      };
   }
}
