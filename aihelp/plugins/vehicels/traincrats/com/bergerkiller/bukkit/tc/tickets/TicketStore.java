package com.bergerkiller.bukkit.tc.tickets;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.config.FileConfiguration;
import com.bergerkiller.bukkit.common.inventory.CommonItemStack;
import com.bergerkiller.bukkit.common.nbt.CommonTagCompound;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.bergerkiller.bukkit.common.wrappers.HumanHand;
import com.bergerkiller.bukkit.tc.Localization;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class TicketStore {
   public static final Ticket DEFAULT = new Ticket("");
   private static final String saveFileName = "tickets.yml";
   private static boolean hasChanges = false;
   private static final HashMap<String, Ticket> ticketMap = new HashMap();
   private static final HashMap<UUID, Ticket> editingMap = new HashMap();
   protected static final String KEY_TICKET_NAME = "ticketName";
   protected static final String KEY_TICKET_CREATION_TIME = "ticketCreationTime";
   protected static final String KEY_TICKET_NUMBER_OF_USES = "ticketNumberOfUses";
   protected static final String KEY_TICKET_OWNER_UUID = "ticketOwner";
   protected static final String KEY_TICKET_OWNER_NAME = "ticketOwnerName";

   public static Ticket createTicket(Ticket baseTicket) {
      Ticket ticket = null;

      for(int i = 1; ticket == null; ++i) {
         ticket = createTicket(baseTicket, "ticket" + i);
      }

      return ticket;
   }

   public static Ticket createTicket(Ticket baseTicket, String name) {
      if (ticketMap.containsKey(name)) {
         return null;
      } else {
         Ticket ticket = new Ticket(name);
         ticket.setProperties(baseTicket.getProperties());
         ticket.setPlayerBound(baseTicket.isPlayerBound());
         ticketMap.put(name, ticket);
         markChanged();
         return ticket;
      }
   }

   public static Collection<Ticket> getAll() {
      return ticketMap.values();
   }

   public static Ticket getTicket(String ticketName) {
      return (Ticket)ticketMap.get(ticketName);
   }

   public static boolean removeTicket(String ticketName) {
      Ticket removed = (Ticket)ticketMap.remove(ticketName);
      if (removed == null) {
         return false;
      } else {
         Iterator editIter = editingMap.values().iterator();

         while(editIter.hasNext()) {
            if (editIter.next() == removed) {
               editIter.remove();
            }
         }

         markChanged();
         return true;
      }
   }

   public static boolean renameTicket(String oldTicketName, String newTicketName) {
      if (oldTicketName.equals(newTicketName)) {
         return true;
      } else if (ticketMap.containsKey(newTicketName)) {
         return false;
      } else {
         Ticket ticket = (Ticket)ticketMap.remove(oldTicketName);
         if (ticket == null) {
            return false;
         } else {
            ticket.setName(newTicketName);
            ticketMap.put(newTicketName, ticket);
            markChanged();
            return true;
         }
      }
   }

   public static Ticket getEditing(UUID playerUUID) {
      return (Ticket)editingMap.get(playerUUID);
   }

   public static Ticket getEditing(Player player) {
      return getEditing(player.getUniqueId());
   }

   public static void setEditing(UUID playerUUID, Ticket ticket) {
      editingMap.put(playerUUID, ticket);
   }

   public static void setEditing(Player player, Ticket ticket) {
      setEditing(player.getUniqueId(), ticket);
   }

   public static boolean isTicketItem(ItemStack item) {
      return isTicketItem(CommonItemStack.of(item));
   }

   public static boolean isTicketItem(CommonItemStack item) {
      CommonTagCompound tag = item.getCustomData();
      return tag.containsKey("ticketName") && ((String)tag.getValue("plugin", "")).equals("TrainCarts");
   }

   public static Ticket getTicketFromItem(ItemStack item) {
      return getTicketFromItem(CommonItemStack.of(item));
   }

   public static Ticket getTicketFromItem(CommonItemStack item) {
      CommonTagCompound nbt = item.getCustomData();
      return nbt.containsKey("ticketName") ? (Ticket)ticketMap.get(item.getCustomData().getValue("ticketName", "")) : null;
   }

   public static int getNumberOfUses(ItemStack item) {
      return getNumberOfUses(CommonItemStack.of(item));
   }

   public static int getNumberOfUses(CommonItemStack item) {
      return (Integer)item.getCustomData().getValue("ticketNumberOfUses", 0);
   }

   public static boolean isTicketExpired(ItemStack item) {
      return isTicketExpired(CommonItemStack.of(item));
   }

   public static boolean isTicketExpired(CommonItemStack item) {
      Ticket ticket = getTicketFromItem(item);
      if (ticket == null) {
         return true;
      } else {
         CommonTagCompound tag = item.getCustomData();
         if (ticket.getMaxNumberOfUses() >= 0) {
            int numberOfUses = (Integer)tag.getValue("ticketNumberOfUses", 0);
            if (numberOfUses >= ticket.getMaxNumberOfUses()) {
               return true;
            }
         }

         if (ticket.getExpirationTime() >= 0L) {
            long timeNow = System.currentTimeMillis();
            long timeCreated = (Long)tag.getValue("ticketCreationTime", timeNow);
            if (timeNow >= timeCreated + ticket.getExpirationTime()) {
               return true;
            }
         }

         return false;
      }
   }

   public static boolean isTicketOwner(Player player, ItemStack item) {
      return isTicketOwner(player, CommonItemStack.of(item));
   }

   public static boolean isTicketOwner(Player player, CommonItemStack item) {
      Ticket ticket = getTicketFromItem(item);
      if (ticket != null && ticket.isPlayerBound()) {
         CommonTagCompound tag = item.getCustomData();
         UUID ownerUUID = tag.getUUID("ticketOwner");
         return ownerUUID == null ? true : ownerUUID.equals(player.getUniqueId());
      } else {
         return true;
      }
   }

   public static boolean handleTickets(Player player, TrainProperties trainProperties) {
      if (trainProperties.getTickets().isEmpty()) {
         return true;
      } else {
         CommonItemStack mainHand = CommonItemStack.of(HumanHand.getItemInMainHand(player));
         CommonItemStack offHand = CommonItemStack.of(HumanHand.getItemInOffHand(player));
         if (isSuitableTicket(mainHand, trainProperties)) {
            if (isSuitableTicket(offHand, trainProperties)) {
               Localization.TICKET_CONFLICT.message(player, new String[0]);
               return false;
            } else if (preUseTicket(player, mainHand, trainProperties)) {
               HumanHand.setItemInMainHand(player, useTicketItem(mainHand).toBukkit());
               return true;
            } else {
               return false;
            }
         } else if (isSuitableTicket(offHand, trainProperties)) {
            if (preUseTicket(player, offHand, trainProperties)) {
               HumanHand.setItemInOffHand(player, useTicketItem(offHand).toBukkit());
               return true;
            } else {
               return false;
            }
         } else {
            Ticket mainHandTicket = getTicketFromItem(mainHand);
            Ticket offHandTicket = getTicketFromItem(offHand);
            if (mainHandTicket == null && offHandTicket == null) {
               TicketStore.TicketHandleResult result = handleTicketsInventory(player, true, trainProperties);
               if (result == TicketStore.TicketHandleResult.MISSING) {
                  result = handleTicketsInventory(player, false, trainProperties);
               }

               if (result == TicketStore.TicketHandleResult.MISSING) {
                  Localization.TICKET_REQUIRED.message(player, new String[0]);
               }

               return result == TicketStore.TicketHandleResult.OK;
            } else {
               if (mainHandTicket != null) {
                  Localization.TICKET_CONFLICT_TYPE.message(player, new String[]{mainHandTicket.getName()});
               }

               if (offHandTicket != null) {
                  Localization.TICKET_CONFLICT_TYPE.message(player, new String[]{offHandTicket.getName()});
               }

               return false;
            }
         }
      }
   }

   private static TicketStore.TicketHandleResult handleTicketsInventory(Player player, boolean quickbar, TrainProperties trainProperties) {
      PlayerInventory inventory = player.getInventory();
      int ticketInvIndex = -1;
      int start = quickbar ? 0 : 9;
      int end = quickbar ? 9 : inventory.getSize();

      for(int i = start; i < end; ++i) {
         CommonItemStack item = CommonItemStack.of(inventory.getItem(i));
         if (isSuitableTicket(item, trainProperties) && !isTicketExpired(item)) {
            if (ticketInvIndex != -1) {
               Localization.TICKET_CONFLICT.message(player, new String[0]);
               return TicketStore.TicketHandleResult.FAILURE;
            }

            ticketInvIndex = i;
         }
      }

      if (ticketInvIndex == -1) {
         return TicketStore.TicketHandleResult.MISSING;
      } else {
         CommonItemStack ticketItem = CommonItemStack.of(inventory.getItem(ticketInvIndex));
         if (preUseTicket(player, ticketItem, trainProperties)) {
            inventory.setItem(ticketInvIndex, useTicketItem(ticketItem).toBukkit());
            return TicketStore.TicketHandleResult.OK;
         } else {
            return TicketStore.TicketHandleResult.FAILURE;
         }
      }
   }

   private static boolean isSuitableTicket(CommonItemStack item, TrainProperties trainProperties) {
      Ticket ticket = getTicketFromItem(item);
      if (ticket != null) {
         Iterator var3 = trainProperties.getTickets().iterator();

         while(var3.hasNext()) {
            String allowed = (String)var3.next();
            if (ticket.getName().equals(allowed) || !LogicUtil.nullOrEmpty(ticket.getRealm()) && ticket.getRealm().equals(allowed)) {
               return true;
            }
         }
      }

      return false;
   }

   private static boolean preUseTicket(Player player, CommonItemStack item, TrainProperties trainProperties) {
      String ticketName = (String)item.getCustomData().getValue("ticketName", "UNKNOWN");
      if (!isTicketOwner(player, item)) {
         String ownerName = (String)item.getCustomData().getValue("ticketOwnerName", "UNKNOWN");
         Localization.TICKET_CONFLICT_OWNER.message(player, new String[]{ticketName, ownerName});
         return false;
      } else if (isTicketExpired(item)) {
         Localization.TICKET_EXPIRED.message(player, new String[]{ticketName});
         return false;
      } else {
         Localization.TICKET_USED.message(player, new String[]{ticketName});
         Ticket ticket = getTicketFromItem(item);
         if (ticket != null) {
            ConfigurationNode ticketTrainProperties = ticket.getProperties().clone();
            ticketTrainProperties.remove("carts");
            trainProperties.apply(ticketTrainProperties);
            MinecartGroup group = trainProperties.getHolder();
            if (group != null) {
               group.onPropertiesChanged();
            }
         }

         return true;
      }
   }

   private static CommonItemStack useTicketItem(CommonItemStack item) {
      Ticket ticket = getTicketFromItem(item);
      if (ticket == null) {
         return CommonItemStack.empty();
      } else {
         item = item.clone();
         if (ticket.getMaxNumberOfUses() >= 0 && item.getAmount() > 1) {
            item.setAmount(item.getAmount() - 1);
         } else {
            item.updateCustomData((tag) -> {
               tag.putValue("ticketNumberOfUses", (Integer)tag.getValue("ticketNumberOfUses", 0) + 1);
            });
         }

         return isTicketExpired(item) ? CommonItemStack.empty() : item;
      }
   }

   public static void markChanged() {
      hasChanges = true;
   }

   public static void load(TrainCarts traincarts) {
      FileConfiguration config = new FileConfiguration(traincarts, "tickets.yml");
      config.load();
      ticketMap.clear();
      editingMap.clear();
      Iterator var2 = config.getNodes().iterator();

      while(var2.hasNext()) {
         ConfigurationNode node = (ConfigurationNode)var2.next();
         Ticket ticket = new Ticket(node.getName());
         ticket.load(node);
         ticketMap.put(ticket.getName(), ticket);
      }

      hasChanges = false;
      traincarts.getDataFile(new String[]{"images"}).mkdirs();
   }

   public static void save(TrainCarts traincarts, boolean autosave) {
      if (!autosave || hasChanges) {
         FileConfiguration config = new FileConfiguration(traincarts, "tickets.yml");
         Iterator var3 = ticketMap.values().iterator();

         while(var3.hasNext()) {
            Ticket ticket = (Ticket)var3.next();
            ticket.save(config.getNode(ticket.getName()));
         }

         config.save();
         hasChanges = false;
      }
   }

   private static enum TicketHandleResult {
      MISSING,
      FAILURE,
      OK;

      // $FF: synthetic method
      private static TicketStore.TicketHandleResult[] $values() {
         return new TicketStore.TicketHandleResult[]{MISSING, FAILURE, OK};
      }
   }
}
