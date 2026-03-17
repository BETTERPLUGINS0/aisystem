package com.bergerkiller.bukkit.tc.attachments.control.seat;

import com.bergerkiller.bukkit.tc.attachments.api.AttachmentViewer;
import com.bergerkiller.bukkit.tc.attachments.control.CartAttachmentSeat;

public class FirstPersonViewNone extends FirstPersonView {
   public FirstPersonViewNone(CartAttachmentSeat seat) {
      super(seat, (AttachmentViewer)null);
   }

   public void makeVisible(AttachmentViewer viewer, boolean isReload) {
   }

   public void makeHidden(AttachmentViewer viewer, boolean isReload) {
   }

   public void onTick() {
   }

   public void onMove(boolean absolute) {
   }
}
