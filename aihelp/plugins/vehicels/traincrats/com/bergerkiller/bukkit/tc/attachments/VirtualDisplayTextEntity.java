package com.bergerkiller.bukkit.tc.attachments;

import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.bergerkiller.bukkit.common.wrappers.ChatText;
import com.bergerkiller.bukkit.common.wrappers.DataWatcher.Prototype;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentManager;
import com.bergerkiller.generated.net.minecraft.world.entity.DisplayHandle;
import com.bergerkiller.generated.net.minecraft.world.entity.DisplayHandle.TextDisplayHandle;
import org.bukkit.Color;

public class VirtualDisplayTextEntity extends VirtualDisplayEntity {
   private ChatText text = null;
   private int styleFlags = 0;
   private int backgroundColor = 1073741824;
   private double opacity = 1.0D;
   private byte opacityByte = -1;
   public static final Prototype TEXT_DISPLAY_METADATA;

   public VirtualDisplayTextEntity(AttachmentManager manager) {
      super(manager, TEXT_DISPLAY_ENTITY_TYPE, TEXT_DISPLAY_METADATA.create());
   }

   public double getOpacity() {
      return this.opacity;
   }

   public void setOpacity(double newOpacity) {
      byte newOpacityByte = (byte)((int)(newOpacity * 255.0D));
      if (this.opacityByte != newOpacityByte) {
         this.opacityByte = newOpacityByte;
         this.metadata.set(TextDisplayHandle.DATA_TEXT_OPACITY, newOpacityByte);
      }

   }

   public int getStyleFlags() {
      return this.styleFlags;
   }

   public void setStyleFlags(int newFlags) {
      if (this.styleFlags != newFlags) {
         this.styleFlags = newFlags;
         this.metadata.setByte(TextDisplayHandle.DATA_STYLE_FLAGS, newFlags);
      }

   }

   public void updateStyleFlags(int flagChanges, boolean set) {
      this.setStyleFlags(set ? this.styleFlags | flagChanges : this.styleFlags & ~flagChanges);
   }

   public void setBackgroundColor(Color color) {
      this.setBackgroundColor(color.asARGB());
   }

   public void setBackgroundColor(int colorRGB) {
      if (this.backgroundColor != colorRGB) {
         this.backgroundColor = colorRGB;
         this.metadata.set(TextDisplayHandle.DATA_BACKGROUND_COLOR, colorRGB);
      }

   }

   public ChatText getText() {
      return this.text;
   }

   public void setText(ChatText text) {
      if (!LogicUtil.bothNullOrEqual(this.text, text)) {
         this.text = text;
         this.metadata.set(TextDisplayHandle.DATA_TEXT, text);
         this.syncMeta();
      }

   }

   static {
      TEXT_DISPLAY_METADATA = BASE_DISPLAY_METADATA.modify().setClientDefault(TextDisplayHandle.DATA_TEXT, ChatText.empty()).setClientDefault(TextDisplayHandle.DATA_LINE_WIDTH, 200).setClientDefault(TextDisplayHandle.DATA_BACKGROUND_COLOR, 1073741824).setClientByteDefault(TextDisplayHandle.DATA_TEXT_OPACITY, -1).setClientByteDefault(TextDisplayHandle.DATA_STYLE_FLAGS, 0).setClientByteDefault(DisplayHandle.DATA_BILLBOARD_RENDER_CONSTRAINTS, 0).create();
   }
}
