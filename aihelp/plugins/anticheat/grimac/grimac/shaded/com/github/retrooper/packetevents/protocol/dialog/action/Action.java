package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.action;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface Action {
   static Action decode(NBT nbt, PacketWrapper<?> wrapper) {
      NBTCompound compound = (NBTCompound)nbt;
      String typeName = compound.getStringTagValueOrThrow("type");
      ActionType<?> action = (ActionType)ActionTypes.getRegistry().getByNameOrThrow(typeName);
      return action.decode(compound, wrapper);
   }

   static NBT encode(PacketWrapper<?> wrapper, Action action) {
      NBTCompound compound = new NBTCompound();
      compound.set("type", action.getType().getName(), ResourceLocation::encode, wrapper);
      action.getType().encode(compound, wrapper, action);
      return compound;
   }

   ActionType<?> getType();
}
