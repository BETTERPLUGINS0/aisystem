package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.villager;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.villager.level.VillagerLevel;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.villager.profession.VillagerProfession;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.villager.profession.VillagerProfessions;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.villager.type.VillagerType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.villager.type.VillagerTypes;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;

public class VillagerData {
   private VillagerType type;
   private VillagerProfession profession;
   private int level;

   public VillagerData(VillagerType type, VillagerProfession profession, VillagerLevel level) {
      this(type, profession, level.getId());
   }

   public VillagerData(VillagerType type, VillagerProfession profession, int level) {
      this.type = type;
      this.profession = profession;
      this.level = level;
   }

   /** @deprecated */
   @Deprecated
   public VillagerData(int typeId, int professionId, int level) {
      this(VillagerTypes.getById(typeId), VillagerProfessions.getById(professionId), level);
   }

   public VillagerType getType() {
      return this.type;
   }

   public void setType(VillagerType type) {
      this.type = type;
   }

   public VillagerProfession getProfession() {
      return this.profession;
   }

   public void setProfession(VillagerProfession profession) {
      this.profession = profession;
   }

   public int getLevel() {
      return this.level;
   }

   @Nullable
   public VillagerLevel getVillagerLevel() {
      return VillagerLevel.getById(this.level);
   }

   public void setLevel(int level) {
      this.level = level;
   }

   public void setLevel(VillagerLevel level) {
      this.level = level.getId();
   }
}
