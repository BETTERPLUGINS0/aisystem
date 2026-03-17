package advancedplugins.pm2.cv.models.api.model.rpc.generator.parser.blockbench;

import advancedplugins.pm2.cv.models.api.utils.data.GSONUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class BlockbenchDeserializer implements JsonDeserializer<BlockbenchModel> {
   private final Gson gson = (new GsonBuilder()).registerTypeAdapter(BlockbenchModel.Element.class, new BlockbenchDeserializer.ElementDeserializer()).registerTypeAdapter(BlockbenchModel.Group.class, new BlockbenchDeserializer.GroupDeserializer()).registerTypeAdapter(BlockbenchModel.Animation.class, new BlockbenchDeserializer.AnimationDeserializer()).registerTypeAdapter(BlockbenchModel.Animator.class, new BlockbenchDeserializer.AnimatorDeserializer()).registerTypeAdapter(BlockbenchModel.Face.class, new BlockbenchDeserializer.FaceDeserializer()).create();

   public BlockbenchModel deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) {
      JsonObject var4 = var1.getAsJsonObject();
      BlockbenchModel.Resolution var5 = (BlockbenchModel.Resolution)this.gson.fromJson(var4.get("resolution"), BlockbenchModel.Resolution.class);
      LinkedHashMap var6 = new LinkedHashMap();
      GSONUtils.ifArray(var4, "elements", (Consumer)((var2x) -> {
         BlockbenchModel.Element var3 = (BlockbenchModel.Element)this.gson.fromJson(var2x, BlockbenchModel.Element.class);
         var6.put(var3.uuid, var3);
      }));
      LinkedHashMap var7 = new LinkedHashMap();
      GSONUtils.ifArray(var4, "outliner", (Consumer)((var2x) -> {
         if (var2x.isJsonObject()) {
            BlockbenchModel.Group var3 = (BlockbenchModel.Group)this.gson.fromJson(var2x, BlockbenchModel.Group.class);
            var7.put(var3.uuid, var3);
         }

      }));
      ConcurrentHashMap var8 = new ConcurrentHashMap();
      ConcurrentHashMap var9 = new ConcurrentHashMap();
      GSONUtils.ifArray(var4, "textures", (BiConsumer)((var3x, var4x) -> {
         BlockbenchModel.Texture var5 = (BlockbenchModel.Texture)this.gson.fromJson(var4x, BlockbenchModel.Texture.class);
         var8.put(var3x, var5);
         var9.put(var5.uuid, var5);
      }));
      LinkedHashMap var10 = new LinkedHashMap();
      GSONUtils.ifArray(var4, "animations", (Consumer)((var2x) -> {
         BlockbenchModel.Animation var3 = (BlockbenchModel.Animation)this.gson.fromJson(var2x, BlockbenchModel.Animation.class);
         var10.put(var3.name, var3);
      }));
      GSONUtils.ifPresent(var4, "mcmetas", (var1x) -> {
         JsonObject var2 = var1x.getAsJsonObject();
         Iterator var3 = var2.entrySet().iterator();

         while(var3.hasNext()) {
            Entry var4 = (Entry)var3.next();
            UUID var5 = UUID.fromString((String)var4.getKey());
            BlockbenchModel.Texture var6 = (BlockbenchModel.Texture)var9.get(var5);
            if (var6 != null) {
               var6.raw_mcmeta = ((JsonElement)var4.getValue()).toString();
            }
         }

      });
      String var11 = (String)GSONUtils.get(var4, "animation_variable_placeholders", JsonElement::getAsString, "");
      return (new BlockbenchModel(var5, var6, var7, var8, var10, var11)).preprocess();
   }

   class ElementDeserializer implements JsonDeserializer<BlockbenchModel.Element> {
      public BlockbenchModel.Element deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) {
         JsonObject var4 = var1.getAsJsonObject();
         String var5 = (String)GSONUtils.get(var4, "type", JsonElement::getAsString, "cube");
         Gson var6 = BlockbenchDeserializer.this.gson;
         byte var7 = -1;
         switch(var5.hashCode()) {
         case -1367751899:
            if (var5.equals("camera")) {
               var7 = 3;
            }
            break;
         case 3064885:
            if (var5.equals("cube")) {
               var7 = 0;
            }
            break;
         case 338418838:
            if (var5.equals("locator")) {
               var7 = 2;
            }
            break;
         case 1680073975:
            if (var5.equals("null_object")) {
               var7 = 1;
            }
         }

         Class var8;
         switch(var7) {
         case 0:
            var8 = BlockbenchModel.Cube.class;
            break;
         case 1:
            var8 = BlockbenchModel.NullObject.class;
            break;
         case 2:
            var8 = BlockbenchModel.Locator.class;
            break;
         case 3:
            var8 = BlockbenchModel.Camera.class;
            break;
         default:
            var8 = BlockbenchModel.Unknown.class;
         }

         return (BlockbenchModel.Element)var6.fromJson(var1, var8);
      }
   }

   class GroupDeserializer implements JsonDeserializer<BlockbenchModel.Group> {
      public BlockbenchModel.Group deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) {
         BlockbenchModel.Group var4 = new BlockbenchModel.Group();
         var4.name = (String)GSONUtils.get(var1, "name", JsonElement::getAsString);
         var4.origin = (Float[])GSONUtils.get(var1, "origin", GSONUtils::getAsFloatArray);
         var4.rotation = (Float[])GSONUtils.get(var1, "rotation", GSONUtils::getAsFloatArray);
         var4.uuid = (UUID)GSONUtils.get(var1, "uuid", GSONUtils::getAsUUID);
         var4.export = (Boolean)GSONUtils.get(var1, "export", JsonElement::getAsBoolean, true);
         GSONUtils.ifArray(var1, "children", (var2x) -> {
            if (var2x.isJsonPrimitive()) {
               var4.element.add(GSONUtils.getAsUUID(var2x));
            } else if (var2x.isJsonObject()) {
               BlockbenchModel.Group var3 = (BlockbenchModel.Group)BlockbenchDeserializer.this.gson.fromJson(var2x, BlockbenchModel.Group.class);
               var4.childGroup.put(var3.uuid, var3);
               var3.parentGroup = var4.uuid;
            }

         });
         return var4;
      }
   }

   class AnimationDeserializer implements JsonDeserializer<BlockbenchModel.Animation> {
      public BlockbenchModel.Animation deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) {
         BlockbenchModel.Animation var4 = new BlockbenchModel.Animation();
         var4.uuid = (UUID)GSONUtils.get(var1, "uuid", GSONUtils::getAsUUID);
         var4.name = (String)GSONUtils.get(var1, "name", JsonElement::getAsString);
         var4.loop = (String)GSONUtils.get(var1, "loop", JsonElement::getAsString);
         var4.override = (Boolean)GSONUtils.get(var1, "override", JsonElement::getAsBoolean);
         var4.length = (Float)GSONUtils.get(var1, "length", JsonElement::getAsFloat);
         GSONUtils.ifPresent(var1, "animators", (var2x) -> {
            if (var2x.isJsonObject()) {
               JsonObject var3 = var2x.getAsJsonObject();
               Iterator var4x = var3.keySet().iterator();

               while(true) {
                  while(var4x.hasNext()) {
                     String var5 = (String)var4x.next();
                     BlockbenchModel.Animator var6 = (BlockbenchModel.Animator)BlockbenchDeserializer.this.gson.fromJson(var3.getAsJsonObject(var5), BlockbenchModel.Animator.class);
                     if (var5.equals("effects")) {
                        var4.effects = var6;
                     } else {
                        try {
                           UUID var7 = UUID.fromString(var5);
                           var6.uuid = var7;
                           var4.animators.put(var7, var6);
                        } catch (IllegalArgumentException var8) {
                           if (var4.unknownAnimators == null) {
                              var4.unknownAnimators = new ConcurrentHashMap();
                           }

                           var4.unknownAnimators.put(var5, var6);
                        }
                     }
                  }

                  return;
               }
            }
         });
         return var4;
      }
   }

   class AnimatorDeserializer implements JsonDeserializer<BlockbenchModel.Animator> {
      public BlockbenchModel.Animator deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) {
         BlockbenchModel.Animator var4 = new BlockbenchModel.Animator();
         var4.name = (String)GSONUtils.get(var1, "name", JsonElement::getAsString);
         var4.rotationGlobal = (Boolean)GSONUtils.get(var1, "rotation_global", JsonElement::getAsBoolean);
         GSONUtils.ifArray(var1, "keyframes", (var2x) -> {
            BlockbenchModel.Keyframe var3 = (BlockbenchModel.Keyframe)BlockbenchDeserializer.this.gson.fromJson(var2x, BlockbenchModel.Keyframe.class);
            ((Map)var4.channels.computeIfAbsent(var3.channel, (var0) -> {
               return new TreeMap();
            })).put(var3.time, var3);
         });
         return var4;
      }
   }

   class FaceDeserializer implements JsonDeserializer<BlockbenchModel.Face> {
      public BlockbenchModel.Face deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) {
         BlockbenchModel.Face var4 = new BlockbenchModel.Face();
         var4.uv = (Float[])GSONUtils.get(var1, "uv", (var1x) -> {
            return (Float[])BlockbenchDeserializer.this.gson.fromJson(var1x, Float[].class);
         });
         var4.rotation = (Integer)GSONUtils.get(var1, "rotation", JsonElement::getAsInt);
         var4.texture = (Integer)GSONUtils.get(var1, "texture", JsonElement::getAsInt);
         return var4;
      }
   }
}
