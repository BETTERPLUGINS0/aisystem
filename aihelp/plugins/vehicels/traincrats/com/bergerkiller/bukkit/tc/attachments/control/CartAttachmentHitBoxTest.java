package com.bergerkiller.bukkit.tc.attachments.control;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.tc.attachments.api.Attachment;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentType;
import java.util.Iterator;
import org.bukkit.ChatColor;

public class CartAttachmentHitBoxTest extends CartAttachmentHitBox {
   public static final AttachmentType TYPE = new CartAttachmentHitBox.BaseHitBoxType() {
      public String getID() {
         return "HITBOX_TEST";
      }

      public Attachment createController(ConfigurationNode config) {
         return new CartAttachmentHitBoxTest();
      }
   };
   private CartAttachmentHitBoxTest.Mode mode;

   public CartAttachmentHitBoxTest() {
      this.mode = CartAttachmentHitBoxTest.Mode.IDLE;
   }

   public void onFocus() {
      this.setBoxColor(ChatColor.BLACK);
   }

   public void onBlur() {
      this.setBoxColor(this.mode.getColor());
   }

   public void onTick() {
      if (!this.isFocused()) {
         this.mode = CartAttachmentHitBoxTest.Mode.IDLE;
         Iterator var1 = this.getController().getNameLookup().allOfType(CartAttachmentHitBoxTest.class).iterator();

         while(var1.hasNext()) {
            CartAttachmentHitBoxTest other = (CartAttachmentHitBoxTest)var1.next();
            if (this != other) {
               if (this.getBoundingBox().isInside(other.getBoundingBox())) {
                  this.mode = CartAttachmentHitBoxTest.Mode.INSIDE;
                  break;
               }

               if (this.getBoundingBox().hasOverlap(other.getBoundingBox())) {
                  this.mode = CartAttachmentHitBoxTest.Mode.OVERLAP;
               }
            }
         }

         this.setBoxColor(this.mode.getColor());
      }

   }

   private static enum Mode {
      IDLE(ChatColor.RED),
      OVERLAP(ChatColor.YELLOW),
      INSIDE(ChatColor.GREEN);

      private final ChatColor color;

      private Mode(ChatColor color) {
         this.color = color;
      }

      public ChatColor getColor() {
         return this.color;
      }

      // $FF: synthetic method
      private static CartAttachmentHitBoxTest.Mode[] $values() {
         return new CartAttachmentHitBoxTest.Mode[]{IDLE, OVERLAP, INSIDE};
      }
   }
}
