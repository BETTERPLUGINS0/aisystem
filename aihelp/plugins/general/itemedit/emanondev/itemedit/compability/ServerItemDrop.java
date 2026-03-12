package emanondev.itemedit.compability;

import emanondev.itemedit.ItemEdit;
import emanondev.itemedit.utility.SchedulerUtils;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.adapters.AbstractItemStack;
import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.drops.DropMetadata;
import io.lumine.mythic.api.drops.IItemDrop;
import io.lumine.mythic.api.drops.ILocationDrop;
import io.lumine.mythic.api.skills.SkillCaster;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.adapters.item.ItemComponentBukkitItemStack;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

class ServerItemDrop implements IItemDrop, ILocationDrop {
   private final String id;
   private final int amount;

   public ServerItemDrop(MythicLineConfig config) {
      String value = config.getString(new String[]{"name", "id", "serveritem", "type"}, (String)null, new String[0]);
      if (value == null) {
         ItemEdit.get().log("&9[&fMythicMobs&9] &fMissing item id on mechanic, use {&eid&f='<your_id>' ....}");
         throw new IllegalArgumentException();
      } else if (ItemEdit.get().getServerStorage().getItem(value) == null) {
         ItemEdit.get().log("&9[&fMythicMobs&9] &fInvalid id, '" + value + "' is not a registered serveritem");
         throw new IllegalArgumentException();
      } else {
         int amount = config.getInteger(new String[]{"a", "amount"}, 1);
         if (amount < 1) {
            ItemEdit.get().log("&9[&fMythicMobs&9] &fInvalid amount, should be from [1 to +inf[");
            throw new IllegalArgumentException();
         } else {
            this.id = value;
            this.amount = amount;
         }
      }
   }

   public void drop(AbstractLocation abstractLocation, DropMetadata dropMetadata, double v) {
      ItemStack item = this.getItem(this.getPlayer(dropMetadata), v);
      if (item != null) {
         Location l = BukkitAdapter.adapt(abstractLocation);
         SchedulerUtils.run(ItemEdit.get(), (Location)l, () -> {
            l.getWorld().dropItem(l, item);
         });
      }
   }

   public AbstractItemStack getDrop(DropMetadata dropMetadata, double v) {
      try {
         ItemStack item = this.getItem(this.getPlayer(dropMetadata), v);
         return item == null ? null : new ItemComponentBukkitItemStack(item);
      } catch (Exception var5) {
         var5.printStackTrace();
         return null;
      }
   }

   public Player getPlayer(DropMetadata data) {
      Entity e;
      if (data.getCause().isPresent()) {
         e = BukkitAdapter.adapt((AbstractEntity)data.getCause().get());
         if (e instanceof Player) {
            return (Player)e;
         }
      }

      if (data.getCaster() != null && data.getCaster().getEntity() != null) {
         e = BukkitAdapter.adapt(data.getCaster().getEntity());
         if (e instanceof Player) {
            return (Player)e;
         }
      }

      if (data.getDropper().isPresent() && ((SkillCaster)data.getDropper().get()).getEntity() != null) {
         e = BukkitAdapter.adapt(((SkillCaster)data.getDropper().get()).getEntity());
         if (e instanceof Player) {
            return (Player)e;
         }
      }

      return null;
   }

   public ItemStack getItem(@Nullable Player p, double amountMultiplier) {
      ItemStack item = ItemEdit.get().getServerStorage().getItem(this.id, p);
      if (item == null) {
         ItemEdit.get().log("&9[&fMythicMobs&9] &fInvalid id, '" + this.id + "' is not a registered serveritem");
         return null;
      } else {
         int a = (int)((double)this.amount * amountMultiplier);
         if (a <= 0) {
            return null;
         } else {
            item.setAmount(a);
            return item;
         }
      }
   }
}
