package me.gypopo.economyshopgui.providers.requirements.items.quest.hooks;

import java.util.Iterator;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.files.Lang;
import me.gypopo.economyshopgui.methodes.SendMessage;
import me.gypopo.economyshopgui.providers.requirements.items.quest.QuestManager;
import me.gypopo.economyshopgui.providers.requirements.items.quest.QuestProvider;
import me.pikamug.quests.Quests;
import me.pikamug.quests.player.Quester;
import me.pikamug.quests.quests.Quest;
import org.bukkit.entity.Player;

public class QuestsCIntegration implements QuestProvider {
   private final Quests questsAPI;

   public QuestsCIntegration(EconomyShopGUI plugin, QuestManager manager) throws NullPointerException {
      this.questsAPI = (Quests)plugin.getServer().getPluginManager().getPlugin("Quests");
   }

   public void init(EconomyShopGUI plugin, QuestManager manager) {
      if (!this.isReady()) {
         SendMessage.infoMessage(this.getName() + " found, waiting...");
         plugin.runTaskLater(() -> {
            if (this.isReady()) {
               SendMessage.infoMessage(Lang.ENABLED_PLUGIN_HOOK.get().replace("%plugin%", this.getName()));
            } else {
               SendMessage.warnMessage(Lang.FAILED_PLUGIN_INTEGRATION.get().replace("%plugin%", this.getName()));
            }

            manager.onLoad();
         }, 65L);
      } else {
         SendMessage.infoMessage(Lang.ENABLED_PLUGIN_HOOK.get().replace("%plugin%", this.getName()));
      }

   }

   public boolean isReady() {
      return !this.questsAPI.isLoading();
   }

   public String getName() {
      return "Quests by PikaMug";
   }

   public String getQuestName(String questID) {
      Iterator iterator = this.questsAPI.getLoadedQuests().iterator();

      Quest quest;
      do {
         if (!iterator.hasNext()) {
            return null;
         }

         quest = (Quest)iterator.next();
      } while(!quest.getId().equals(questID));

      return quest.getName();
   }

   public boolean isLoaded(String questID) {
      return this.questsAPI.getLoadedQuests().stream().anyMatch((q) -> {
         return q.getId().equals(questID);
      });
   }

   public boolean hasCompleted(Player p, String questID) {
      Quester quester = this.questsAPI.getQuester(p.getUniqueId());
      return quester.getCompletedQuests().stream().anyMatch((q) -> {
         return q.getId().equals(questID);
      });
   }
}
