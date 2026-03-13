package me.gypopo.economyshopgui.providers.requirements.items.quest;

import java.util.List;
import me.gypopo.economyshopgui.files.Lang;
import me.gypopo.economyshopgui.methodes.SendMessage;
import me.gypopo.economyshopgui.providers.RequirementManager;
import me.gypopo.economyshopgui.providers.requirements.ItemRequirement;
import me.gypopo.economyshopgui.util.exceptions.ItemRequirementLoadException;
import org.bukkit.entity.Player;

public class QuestRequirement implements ItemRequirement {
   private final String questID;
   private final QuestProvider questProvider;
   private final RequirementManager.RequirementLore lore;

   public QuestRequirement(QuestProvider questProvider, String questID, List<String> lore) throws ItemRequirementLoadException {
      this.questID = questID;
      this.questProvider = questProvider;
      this.lore = RequirementManager.getLore(lore, Lang.QUEST_REQUIREMENT_LORE.get().toString());
      if (this.questProvider.isReady()) {
         this.onLoad();
         if (!this.questProvider.isLoaded(questID)) {
            throw new ItemRequirementLoadException("Quest using ID '" + questID + "' isn't found");
         }
      }

   }

   public String getQuestID() {
      return this.questID;
   }

   public void onLoad() {
      this.lore.replace("%questName%", this.questProvider.getQuestName(this.questID));
   }

   public boolean isMet(Player p) {
      return this.questProvider.hasCompleted(p, this.questID);
   }

   public String[] getLore(Player p, boolean fast, boolean pr) {
      return this.lore.isReplaced() ? this.lore.get(p, fast, pr) : this.lore.get(p, fast, pr, "%questName%", this.questProvider.getQuestName(this.questID));
   }

   public void sendNotMetMessage(Player p) {
      SendMessage.chatToPlayer(p, Lang.QUEST_REQUIREMENT_NOT_MET.get().replace("%questName%", this.questProvider.getQuestName(this.questID)));
   }
}
