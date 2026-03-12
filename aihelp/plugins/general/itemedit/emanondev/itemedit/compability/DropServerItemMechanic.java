package emanondev.itemedit.compability;

import emanondev.itemedit.ItemEdit;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ISkillMechanic;
import io.lumine.mythic.api.skills.ITargetedEntitySkill;
import io.lumine.mythic.api.skills.ITargetedLocationSkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import io.lumine.mythic.api.skills.ThreadSafetyLevel;
import io.lumine.mythic.bukkit.BukkitAdapter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

class DropServerItemMechanic implements ISkillMechanic, ITargetedEntitySkill, ITargetedLocationSkill {
   private final String id;
   private final int amount;
   private final int diff;
   private final double chance;

   public DropServerItemMechanic(MythicLineConfig mlc) {
      this.id = mlc.getString(new String[]{"id", "name", "type", "serveritem"}, (String)null, new String[0]);
      if (this.id == null) {
         ItemEdit.get().log("&9[&fMythicMobs&9] &fMissing item id on mechanic, use {&eid&f='<your_id>' ....}");
         throw new IllegalArgumentException();
      } else {
         ItemStack value = null;

         try {
            value = ItemEdit.get().getServerStorage().getItem(this.id);
         } catch (Exception var4) {
         }

         if (value == null) {
            ItemEdit.get().log("&9[&fMythicMobs&9] &fInvalid id, '" + this.id + "' is not a registered serveritem");
            throw new IllegalArgumentException();
         } else {
            this.amount = mlc.getInteger(new String[]{"amount", "a"}, 1);
            this.diff = mlc.getInteger(new String[]{"amountmax", "amount-max", "amax"}, this.amount) - this.amount;
            this.chance = mlc.getDouble(new String[]{"chance", "c"}, 1.0D);
            if (this.chance <= 0.0D) {
               ItemEdit.get().log("&9[&fMythicMobs&9] &fInvalid chance, should be from ]0 to 1]");
               throw new IllegalArgumentException();
            } else if (this.amount <= 0) {
               ItemEdit.get().log("&9[&fMythicMobs&9] &fInvalid amount, should be from [1 to +inf[");
               throw new IllegalArgumentException();
            } else if (this.diff < 0) {
               ItemEdit.get().log("&9[&fMythicMobs&9] &fInvalid max amount, should be from [amount to +inf[");
               throw new IllegalArgumentException();
            }
         }
      }
   }

   public ThreadSafetyLevel getThreadSafetyLevel() {
      return ThreadSafetyLevel.SYNC_ONLY;
   }

   public SkillResult castAtEntity(SkillMetadata data, final AbstractEntity target) {
      if (Math.random() > this.chance) {
         return SkillResult.CONDITION_FAILED;
      } else {
         if (Bukkit.isPrimaryThread()) {
            this.drop(target.getLocation());
         } else {
            (new BukkitRunnable() {
               public void run() {
                  DropServerItemMechanic.this.drop(target.getLocation());
               }
            }).runTask(ItemEdit.get());
         }

         return SkillResult.SUCCESS;
      }
   }

   public void drop(AbstractLocation location) {
      Location loc = BukkitAdapter.adapt(location);
      ItemStack item = ItemEdit.get().getServerStorage().getItem(this.id);
      if (item == null) {
         ItemEdit.get().log("&9[&fMythicMobs&9] &fInvalid id, '" + this.id + "' is not a registered serveritem");
      } else {
         item.setAmount((int)((double)this.amount + Math.random() * (double)this.diff));
         loc.getWorld().dropItem(loc, item);
      }
   }

   public SkillResult castAtLocation(SkillMetadata data, final AbstractLocation location) {
      if (Math.random() > this.chance) {
         return SkillResult.CONDITION_FAILED;
      } else {
         if (Bukkit.isPrimaryThread()) {
            this.drop(location);
         } else {
            (new BukkitRunnable() {
               public void run() {
                  DropServerItemMechanic.this.drop(location);
               }
            }).runTask(ItemEdit.get());
         }

         return SkillResult.SUCCESS;
      }
   }
}
