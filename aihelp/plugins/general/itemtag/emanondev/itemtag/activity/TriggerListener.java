package emanondev.itemtag.activity;

import emanondev.itemtag.ItemTag;
import emanondev.itemtag.ItemTagUtility;
import emanondev.itemtag.TagItem;
import java.util.Iterator;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class TriggerListener implements Listener {
   public static final TriggerType<PlayerItemConsumeEvent> CONSUME_ITEM = new TriggerType("consume_item", PlayerItemConsumeEvent.class);
   public static final TriggerType<PlayerInteractEvent> RIGHT_INTERACT = new TriggerType("right_interact", PlayerInteractEvent.class);
   public static final TriggerType<PlayerInteractEvent> LEFT_INTERACT = new TriggerType("left_interact", PlayerInteractEvent.class);
   public static final TriggerType<EntityDamageByEntityEvent> MELEE_HIT = new TriggerType("melee_hit", EntityDamageByEntityEvent.class);
   public static final TriggerType<EntityDamageByEntityEvent> RANGED_HIT = new TriggerType("ranged_hit", EntityDamageByEntityEvent.class);
   public static final TriggerType<EntityDamageByEntityEvent> HITTED = new TriggerType("hitted", EntityDamageByEntityEvent.class);

   @EventHandler
   private void event(PlayerItemConsumeEvent event) {
      ItemStack item = event.getItem();
      TagItem tagItem = ItemTag.getTagItem(item);
      if (tagItem.isValid()) {
         try {
            event.setItem(CONSUME_ITEM.handle(event, event.getPlayer(), item, event.getHand()));
         } catch (NoSuchMethodError var5) {
            event.setItem(CONSUME_ITEM.handle(event, event.getPlayer(), item, EquipmentSlot.HAND));
         }

      }
   }

   @EventHandler
   private void event(PlayerInteractEvent event) {
      switch(event.getAction()) {
      case RIGHT_CLICK_AIR:
      case RIGHT_CLICK_BLOCK:
         try {
            RIGHT_INTERACT.handle(event, event.getPlayer(), event.getItem(), event.getHand());
         } catch (NoSuchMethodError var4) {
            RIGHT_INTERACT.handle(event, event.getPlayer(), event.getItem(), EquipmentSlot.HAND);
         }
         break;
      case LEFT_CLICK_AIR:
      case LEFT_CLICK_BLOCK:
         try {
            LEFT_INTERACT.handle(event, event.getPlayer(), event.getItem(), event.getHand());
         } catch (NoSuchMethodError var3) {
            LEFT_INTERACT.handle(event, event.getPlayer(), event.getItem(), EquipmentSlot.HAND);
         }
      }

   }

   @EventHandler
   private void event(EntityDamageByEntityEvent event) {
      Player player;
      Iterator var3;
      EquipmentSlot slot;
      if (event.getDamager() instanceof Player) {
         player = (Player)event.getDamager();
         var3 = ItemTagUtility.getPlayerEquipmentSlots().iterator();

         while(var3.hasNext()) {
            slot = (EquipmentSlot)var3.next();
            MELEE_HIT.handle(event, player, player.getInventory().getItem(slot), slot);
         }
      }

      if (event.getDamager() instanceof Projectile) {
         Projectile prj = (Projectile)event.getDamager();
         if (prj.getShooter() instanceof Player) {
            Player player = (Player)prj.getShooter();
            Iterator var8 = ItemTagUtility.getPlayerEquipmentSlots().iterator();

            while(var8.hasNext()) {
               EquipmentSlot slot = (EquipmentSlot)var8.next();
               RANGED_HIT.handle(event, player, player.getInventory().getItem(slot), slot);
            }
         }
      }

      if (event.getEntity() instanceof Player) {
         player = (Player)event.getEntity();
         var3 = ItemTagUtility.getPlayerEquipmentSlots().iterator();

         while(var3.hasNext()) {
            slot = (EquipmentSlot)var3.next();
            HITTED.handle(event, player, player.getInventory().getItem(slot), slot);
         }
      }

   }
}
