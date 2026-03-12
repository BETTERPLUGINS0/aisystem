package ac.grim.grimac.shaded.io.github.retrooper.packetevents.adventure.serializer.gson;

import ac.grim.grimac.shaded.kyori.adventure.text.BlockNBTComponent;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

final class BlockNBTComponentPosSerializer extends TypeAdapter<BlockNBTComponent.Pos> {
   static final TypeAdapter<BlockNBTComponent.Pos> INSTANCE = (new BlockNBTComponentPosSerializer()).nullSafe();

   private BlockNBTComponentPosSerializer() {
   }

   public BlockNBTComponent.Pos read(final JsonReader in) throws IOException {
      String string = in.nextString();

      try {
         return BlockNBTComponent.Pos.fromString(string);
      } catch (IllegalArgumentException var4) {
         throw new JsonParseException("Don't know how to turn " + string + " into a Position");
      }
   }

   public void write(final JsonWriter out, final BlockNBTComponent.Pos value) throws IOException {
      out.value(value.asString());
   }
}
