package me.gypopo.economyshopgui.providers.requirements;

import me.gypopo.economyshopgui.providers.requirements.items.TimeRequirement;
import me.gypopo.economyshopgui.providers.requirements.items.quest.QuestRequirement;
import me.gypopo.economyshopgui.providers.requirements.items.region.RegionRequirement;

public enum RequirementType {
   TIME(TimeRequirement.class, "time-requirement-not-met"),
   QUEST(QuestRequirement.class, "quest-requirement-not-met"),
   REGION(RegionRequirement.class, "region-requirement-not-met");

   private final String lorePath;
   private final Class<? extends ItemRequirement> clazz;

   private RequirementType(Class<? extends ItemRequirement> param3, String param4) {
      this.clazz = clazz;
      this.lorePath = "display-lore-layout.item-requirements." + lorePath;
   }

   public Class<? extends ItemRequirement> getClazz() {
      return this.clazz;
   }

   public String getLorePath() {
      return this.lorePath;
   }

   // $FF: synthetic method
   private static RequirementType[] $values() {
      return new RequirementType[]{TIME, QUEST, REGION};
   }
}
