package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;

public class DebugStickState {
   private Map<StateType, String> properties;

   public DebugStickState(Map<StateType, String> properties) {
      this.properties = properties;
   }

   public static DebugStickState read(PacketWrapper<?> wrapper) {
      NBTCompound compound = wrapper.readNBT();
      Map<StateType, String> properties = new HashMap(compound.size());
      Iterator var3 = compound.getTags().entrySet().iterator();

      while(var3.hasNext()) {
         Entry<String, NBT> tag = (Entry)var3.next();
         StateType stateType = StateTypes.getByName((String)tag.getKey());
         String property = ((NBTString)tag.getValue()).getValue();
         properties.put(stateType, property);
      }

      return new DebugStickState(properties);
   }

   public static void write(PacketWrapper<?> wrapper, DebugStickState state) {
      NBTCompound compound = new NBTCompound();
      Iterator var3 = state.properties.entrySet().iterator();

      while(var3.hasNext()) {
         Entry<StateType, String> property = (Entry)var3.next();
         compound.setTag(((StateType)property.getKey()).getName(), new NBTString((String)property.getValue()));
      }

      wrapper.writeNBT(compound);
   }

   @Nullable
   public String getProperty(StateType stateType) {
      return (String)this.properties.get(stateType);
   }

   public void setProperty(StateType stateType, @Nullable String property) {
      if (property != null) {
         this.properties.put(stateType, property);
      } else {
         this.properties.remove(stateType);
      }

   }

   public Map<StateType, String> getProperties() {
      return this.properties;
   }

   public void setProperties(Map<StateType, String> properties) {
      this.properties = properties;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (!(o instanceof DebugStickState)) {
         return false;
      } else {
         DebugStickState that = (DebugStickState)o;
         return this.properties.equals(that.properties);
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.properties);
   }
}
