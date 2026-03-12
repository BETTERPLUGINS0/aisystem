package ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class SimpleTypesBuilderData extends TypesBuilderData {
   public SimpleTypesBuilderData(ResourceLocation name, int id) {
      super(name, new int[]{id});
   }

   public int getId(ClientVersion version) {
      return this.ids[0];
   }
}
