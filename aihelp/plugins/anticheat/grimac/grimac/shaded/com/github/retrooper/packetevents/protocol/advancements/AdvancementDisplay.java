package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.advancements;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;

public final class AdvancementDisplay {
   public static final int FLAG_HAS_BACKGROUND = 1;
   public static final int FLAG_SHOW_TOAST = 2;
   public static final int FLAG_HIDDEN = 4;
   private Component title;
   private Component description;
   private ItemStack icon;
   private AdvancementType type;
   private boolean showToast;
   private boolean hidden;
   @Nullable
   private ResourceLocation background;
   private float x;
   private float y;

   public AdvancementDisplay(Component title, Component description, ItemStack icon, AdvancementType type, @Nullable ResourceLocation background, boolean showToast, boolean hidden, float x, float y) {
      this.title = title;
      this.description = description;
      this.icon = icon;
      this.type = type;
      this.showToast = showToast;
      this.hidden = hidden;
      this.background = background;
      this.x = x;
      this.y = y;
   }

   public static AdvancementDisplay read(PacketWrapper<?> wrapper) {
      Component title = wrapper.readComponent();
      Component description = wrapper.readComponent();
      ItemStack icon = wrapper.readItemStack();
      AdvancementType type = (AdvancementType)wrapper.readEnum(AdvancementType.class);
      int flags = wrapper.readInt();
      ResourceLocation background = (flags & 1) != 0 ? ResourceLocation.read(wrapper) : null;
      boolean showToast = (flags & 2) != 0;
      boolean hidden = (flags & 4) != 0;
      float x = wrapper.readFloat();
      float y = wrapper.readFloat();
      return new AdvancementDisplay(title, description, icon, type, background, showToast, hidden, x, y);
   }

   public static void write(PacketWrapper<?> wrapper, AdvancementDisplay display) {
      wrapper.writeComponent(display.title);
      wrapper.writeComponent(display.description);
      wrapper.writeItemStack(display.icon);
      wrapper.writeEnum(display.type);
      wrapper.writeInt(display.packFlags());
      if (display.background != null) {
         ResourceLocation.write(wrapper, display.background);
      }

      wrapper.writeFloat(display.x);
      wrapper.writeFloat(display.y);
   }

   public int packFlags() {
      int flags = 0;
      if (this.background != null) {
         flags |= 1;
      }

      if (this.showToast) {
         flags |= 2;
      }

      if (this.hidden) {
         flags |= 4;
      }

      return flags;
   }

   public Component getTitle() {
      return this.title;
   }

   public void setTitle(Component title) {
      this.title = title;
   }

   public Component getDescription() {
      return this.description;
   }

   public void setDescription(Component description) {
      this.description = description;
   }

   public ItemStack getIcon() {
      return this.icon;
   }

   public void setIcon(ItemStack icon) {
      this.icon = icon;
   }

   public AdvancementType getType() {
      return this.type;
   }

   public void setType(AdvancementType type) {
      this.type = type;
   }

   public boolean isShowToast() {
      return this.showToast;
   }

   public void setShowToast(boolean showToast) {
      this.showToast = showToast;
   }

   public boolean isHidden() {
      return this.hidden;
   }

   public void setHidden(boolean hidden) {
      this.hidden = hidden;
   }

   @Nullable
   public ResourceLocation getBackground() {
      return this.background;
   }

   public void setBackground(@Nullable ResourceLocation background) {
      this.background = background;
   }

   public float getX() {
      return this.x;
   }

   public void setX(float x) {
      this.x = x;
   }

   public float getY() {
      return this.y;
   }

   public void setY(float y) {
      this.y = y;
   }
}
