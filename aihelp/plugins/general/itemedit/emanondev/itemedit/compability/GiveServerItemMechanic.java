package emanondev.itemedit.compability;

import emanondev.itemedit.ItemEdit;
import emanondev.itemedit.utility.InventoryUtils;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.adapters.AbstractPlayer;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ISkillMechanic;
import io.lumine.mythic.api.skills.ITargetedEntitySkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import io.lumine.mythic.api.skills.ThreadSafetyLevel;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.adapters.BukkitEntity;
import io.lumine.mythic.bukkit.adapters.BukkitPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

class GiveServerItemMechanic implements ISkillMechanic, ITargetedEntitySkill {
   private final String id;
   private final int amount;
   private final int diff;
   private final double chance;

   public GiveServerItemMechanic(MythicLineConfig mlc) {
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

   public SkillResult castAtEntity(SkillMetadata data, AbstractEntity target) {
      if (Math.random() > this.chance) {
         return SkillResult.CONDITION_FAILED;
      } else {
         final Object absPlayer;
         if (!(target instanceof AbstractPlayer)) {
            if (!(target instanceof BukkitEntity)) {
               return SkillResult.CONDITION_FAILED;
            }

            BukkitEntity entity = (BukkitEntity)target;
            if (!entity.isPlayer()) {
               return SkillResult.CONDITION_FAILED;
            }

            absPlayer = new BukkitPlayer(entity.getEntityAsPlayer());
         } else {
            absPlayer = (AbstractPlayer)target;
         }

         if (Bukkit.isPrimaryThread()) {
            this.give((AbstractPlayer)absPlayer);
         } else {
            (new BukkitRunnable() {
               public void run() {
                  GiveServerItemMechanic.this.give((AbstractPlayer)absPlayer);
               }
            }).runTask(ItemEdit.get());
         }

         return SkillResult.SUCCESS;
      }
   }

   public void give(AbstractPlayer target) {
      Player player = BukkitAdapter.adapt(target);
      ItemStack item = ItemEdit.get().getServerStorage().getItem(this.id);
      if (item == null) {
         ItemEdit.get().log("&9[&fMythicMobs&9] &fInvalid id, '" + this.id + "' is not a registered serveritem");
      } else {
         int finalAmount = this.amount + (int)(Math.random() * (double)(this.diff + 1));
         item.setAmount(finalAmount);
         InventoryUtils.giveAmount(player, item, finalAmount, InventoryUtils.ExcessMode.DROP_EXCESS);
      }
   }
}
