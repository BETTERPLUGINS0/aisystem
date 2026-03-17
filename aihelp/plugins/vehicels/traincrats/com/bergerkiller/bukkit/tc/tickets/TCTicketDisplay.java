package com.bergerkiller.bukkit.tc.tickets;

import com.bergerkiller.bukkit.common.map.MapDisplay;
import com.bergerkiller.bukkit.common.map.MapFont;
import com.bergerkiller.bukkit.common.map.MapSessionMode;
import com.bergerkiller.bukkit.common.map.MapTexture;
import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.bergerkiller.bukkit.tc.Localization;
import org.bukkit.entity.Player;

public class TCTicketDisplay extends MapDisplay {
   public void onAttached() {
      this.setSessionMode(MapSessionMode.VIEWING);
      this.setGlobal(false);
      this.renderBackground();
      this.renderTicket();
   }

   public void onMapItemChanged() {
      this.renderTicket();
   }

   public void renderBackground() {
      Ticket ticket = TicketStore.getTicketFromItem(this.getMapItem());
      MapTexture bg;
      if (ticket == null) {
         bg = Ticket.getDefaultBackgroundImage();
      } else {
         bg = ticket.loadBackgroundImage();
      }

      this.getLayer().draw(bg, 0, 0);
   }

   private void renderTicket() {
      this.getLayer(1).clear();
      Ticket ticket = TicketStore.getTicketFromItem(this.getMapItem());
      if (ticket == null) {
         this.getLayer(1).draw(MapFont.MINECRAFT, 10, 40, (byte)18, Localization.TICKET_MAP_INVALID.get());
      } else {
         this.getLayer(1).draw(MapFont.MINECRAFT, 10, 40, (byte)119, ticket.getName());
         if (TicketStore.isTicketExpired(this.getMapItem())) {
            this.getLayer(1).draw(MapFont.MINECRAFT, 10, 57, (byte)18, Localization.TICKET_MAP_EXPIRED.get());
         } else {
            int maxUses = ticket.getMaxNumberOfUses();
            int numUses = maxUses == 1 ? 0 : TicketStore.getNumberOfUses(this.getMapItem());
            if (maxUses < 0) {
               maxUses = -1;
            }

            String text = Localization.TICKET_MAP_USES.get(Integer.toString(maxUses), Integer.toString(numUses));
            this.getLayer(1).draw(MapFont.MINECRAFT, 10, 57, (byte)119, text);
         }

         String ownerName = (String)this.getCommonMapItem().getCustomData().getValue("ticketOwnerName", "Unknown Owner");
         ownerName = StringUtil.stripChatStyle(ownerName);
         if (TicketStore.isTicketOwner((Player)this.getOwners().get(0), this.getMapItem())) {
            this.getLayer(1).draw(MapFont.MINECRAFT, 10, 74, (byte)119, ownerName);
         } else {
            this.getLayer(1).draw(MapFont.MINECRAFT, 10, 74, (byte)18, ownerName);
         }
      }

   }
}
