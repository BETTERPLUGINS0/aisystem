package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.action;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class DynamicCustomAction implements Action {
   private final ResourceLocation id;
   @Nullable
   private final NBTCompound additions;

   public DynamicCustomAction(ResourceLocation id, @Nullable NBTCompound additions) {
      this.id = id;
      this.additions = additions;
   }

   public static DynamicCustomAction decode(NBTCompound compound, PacketWrapper<?> wrapper) {
      ResourceLocation id = (ResourceLocation)compound.getOrThrow("id", ResourceLocation::decode, wrapper);
      NBTCompound additions = compound.getCompoundTagOrNull("additions");
      return new DynamicCustomAction(id, additions);
   }

   public static void encode(NBTCompound compound, PacketWrapper<?> wrapper, DynamicCustomAction action) {
      compound.set("id", action.id, ResourceLocation::encode, wrapper);
      if (action.additions != null) {
         compound.setTag("additions", action.additions);
      }

   }

   public ActionType<?> getType() {
      return ActionTypes.DYNAMIC_CUSTOM;
   }

   public ResourceLocation getId() {
      return this.id;
   }

   @Nullable
   public NBTCompound getAdditions() {
      return this.additions;
   }
}
