package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateValue;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.Map;
import java.util.Objects;

public class ItemBlockStateProperties {
   private Map<String, String> properties;

   public ItemBlockStateProperties(Map<String, String> properties) {
      this.properties = properties;
   }

   public static ItemBlockStateProperties read(PacketWrapper<?> wrapper) {
      Map<String, String> properties = wrapper.readMap(PacketWrapper::readString, PacketWrapper::readString);
      return new ItemBlockStateProperties(properties);
   }

   public static void write(PacketWrapper<?> wrapper, ItemBlockStateProperties props) {
      wrapper.writeMap(props.properties, PacketWrapper::writeString, PacketWrapper::writeString);
   }

   @Nullable
   public Object getProperty(StateValue stateValue) {
      String value = this.getProperty(stateValue.getName());
      return value != null ? stateValue.getParser().apply(value) : null;
   }

   @Nullable
   public String getProperty(String key) {
      return (String)this.properties.get(key);
   }

   public void setProperty(StateValue stateValue, @Nullable Object value) {
      this.setProperty(stateValue.getName(), value == null ? null : value.toString());
   }

   public void setProperty(String key, @Nullable String value) {
      if (value == null) {
         this.properties.remove(key);
      } else {
         this.properties.put(key, value);
      }

   }

   public void unsetProperty(StateValue stateValue) {
      this.unsetProperty(stateValue.getName());
   }

   public void unsetProperty(String key) {
      this.setProperty((String)key, (String)null);
   }

   public Map<String, String> getProperties() {
      return this.properties;
   }

   public void setProperties(Map<String, String> properties) {
      this.properties = properties;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof ItemBlockStateProperties)) {
         return false;
      } else {
         ItemBlockStateProperties that = (ItemBlockStateProperties)obj;
         return this.properties.equals(that.properties);
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.properties);
   }
}
