package advancedplugins.pm2.cv.models.core.model.rpc.generator.parser.blockbench.json;

import advancedplugins.pm2.cv.models.api.model.rpc.generator.assets.BlueprintTexture;
import advancedplugins.pm2.cv.models.api.utils.data.GSONUtils;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.lang.reflect.Type;
import java.util.function.Consumer;
import java.util.function.Function;

public class MCMetaDeserializer implements JsonDeserializer<BlueprintTexture.MCMeta> {
   public BlueprintTexture.MCMeta deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) {
      JsonObject var4 = (JsonObject)GSONUtils.get(var1, "animation", JsonElement::getAsJsonObject);
      if (var4 == null) {
         return null;
      } else {
         BlueprintTexture.MCMeta var5 = new BlueprintTexture.MCMeta();
         var5.setInterpolate((Boolean)GSONUtils.get(var4, (String)"interpolate", (Function)(JsonElement::getAsBoolean)));
         var5.setWidth((Integer)GSONUtils.get(var4, (String)"width", (Function)(JsonElement::getAsInt)));
         var5.setHeight((Integer)GSONUtils.get(var4, (String)"height", (Function)(JsonElement::getAsInt)));
         var5.setFrametime((Integer)GSONUtils.get(var4, (String)"frametime", (Function)(JsonElement::getAsInt)));
         GSONUtils.ifArray(var4, "frames", (Consumer)((var1x) -> {
            if (var1x.isJsonPrimitive()) {
               var5.addFrame(var1x.getAsInt());
            } else if (var1x.isJsonObject()) {
               JsonObject var2 = var1x.getAsJsonObject();
               Integer var3 = (Integer)GSONUtils.get(var2, (String)"index", (Function)(JsonElement::getAsInt));
               Integer var4 = (Integer)GSONUtils.get(var2, (String)"time", (Function)(JsonElement::getAsInt));
               if (var3 != null && var4 != null) {
                  var5.addFrame(var3, var4);
               }
            }

         }));
         return var5;
      }
   }
}
