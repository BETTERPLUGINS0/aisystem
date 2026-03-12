package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.body;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTByte;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class ItemDialogBody implements DialogBody {
   private final ItemStack item;
   @Nullable
   private final PlainMessage description;
   private final boolean showDecorations;
   private final boolean showTooltip;
   private final int width;
   private final int height;

   public ItemDialogBody(ItemStack item, @Nullable PlainMessage description, boolean showDecorations, boolean showTooltip, int width, int height) {
      this.item = item;
      this.description = description;
      this.showDecorations = showDecorations;
      this.showTooltip = showTooltip;
      this.width = width;
      this.height = height;
   }

   public static ItemDialogBody decode(NBTCompound compound, PacketWrapper<?> wrapper) {
      ItemStack item = (ItemStack)compound.getOrThrow("item", ItemStack::decode, wrapper);
      PlainMessage description = (PlainMessage)compound.getOrNull("description", PlainMessage::decode, wrapper);
      boolean showDecorations = compound.getBooleanOr("show_decorations", true);
      boolean showTooltip = compound.getBooleanOr("show_tooltip", true);
      int width = compound.getNumberTagValueOrDefault("width", 16).intValue();
      int height = compound.getNumberTagValueOrDefault("height", 16).intValue();
      return new ItemDialogBody(item, description, showDecorations, showTooltip, width, height);
   }

   public static void encode(NBTCompound compound, PacketWrapper<?> wrapper, ItemDialogBody body) {
      compound.set("item", body.item, ItemStack::encode, wrapper);
      if (body.description != null) {
         compound.set("description", body.description, PlainMessage::encode, wrapper);
      }

      if (!body.showDecorations) {
         compound.setTag("show_decorations", new NBTByte(false));
      }

      if (!body.showTooltip) {
         compound.setTag("show_tooltip", new NBTByte(false));
      }

      if (body.width != 16) {
         compound.setTag("width", new NBTInt(body.width));
      }

      if (body.height != 16) {
         compound.setTag("height", new NBTInt(body.height));
      }

   }

   public DialogBodyType<?> getType() {
      return DialogBodyTypes.ITEM;
   }

   public ItemStack getItem() {
      return this.item;
   }

   @Nullable
   public PlainMessage getDescription() {
      return this.description;
   }

   public boolean isShowDecorations() {
      return this.showDecorations;
   }

   public boolean isShowTooltip() {
      return this.showTooltip;
   }

   public int getWidth() {
      return this.width;
   }

   public int getHeight() {
      return this.height;
   }
}
