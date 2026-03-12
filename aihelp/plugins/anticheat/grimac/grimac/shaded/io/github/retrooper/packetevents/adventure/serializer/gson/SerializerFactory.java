package ac.grim.grimac.shaded.io.github.retrooper.packetevents.adventure.serializer.gson;

import ac.grim.grimac.shaded.io.github.retrooper.packetevents.adventure.option.OptionState;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.adventure.serializer.json.JSONOptions;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.key.Key;
import ac.grim.grimac.shaded.kyori.adventure.text.BlockNBTComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.TranslationArgument;
import ac.grim.grimac.shaded.kyori.adventure.text.event.ClickEvent;
import ac.grim.grimac.shaded.kyori.adventure.text.event.HoverEvent;
import ac.grim.grimac.shaded.kyori.adventure.text.format.ShadowColor;
import ac.grim.grimac.shaded.kyori.adventure.text.format.Style;
import ac.grim.grimac.shaded.kyori.adventure.text.format.TextColor;
import ac.grim.grimac.shaded.kyori.adventure.text.format.TextDecoration;
import ac.grim.grimac.shaded.kyori.adventure.text.object.PlayerHeadObjectContents;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import java.util.UUID;

final class SerializerFactory implements TypeAdapterFactory {
   static final Class<Key> KEY_TYPE = Key.class;
   static final Class<Component> COMPONENT_TYPE = Component.class;
   static final Class<Style> STYLE_TYPE = Style.class;
   static final Class<ClickEvent.Action> CLICK_ACTION_TYPE = ClickEvent.Action.class;
   static final Class<HoverEvent.Action> HOVER_ACTION_TYPE = HoverEvent.Action.class;
   static final Class<HoverEvent.ShowItem> SHOW_ITEM_TYPE = HoverEvent.ShowItem.class;
   static final Class<HoverEvent.ShowEntity> SHOW_ENTITY_TYPE = HoverEvent.ShowEntity.class;
   static final Class<String> STRING_TYPE = String.class;
   static final Class<TextColorWrapper> COLOR_WRAPPER_TYPE = TextColorWrapper.class;
   static final Class<TextColor> COLOR_TYPE = TextColor.class;
   static final Class<?> SHADOW_COLOR_TYPE;
   static final Class<TextDecoration> TEXT_DECORATION_TYPE = TextDecoration.class;
   static final Class<BlockNBTComponent.Pos> BLOCK_NBT_POS_TYPE = BlockNBTComponent.Pos.class;
   static final Class<UUID> UUID_TYPE = UUID.class;
   static final Class<?> TRANSLATION_ARGUMENT_TYPE;
   static final Class<?> PROFILE_PROPERTY_TYPE;
   private final OptionState features;
   private final ac.grim.grimac.shaded.io.github.retrooper.packetevents.adventure.serializer.json.LegacyHoverEventSerializer legacyHoverSerializer;
   private final BackwardCompatUtil.ShowAchievementToComponent compatShowAchievement;

   SerializerFactory(OptionState features, @Nullable ac.grim.grimac.shaded.io.github.retrooper.packetevents.adventure.serializer.json.LegacyHoverEventSerializer legacyHoverSerializer, @Nullable BackwardCompatUtil.ShowAchievementToComponent compatShowAchievement) {
      this.features = features;
      this.legacyHoverSerializer = legacyHoverSerializer;
      this.compatShowAchievement = compatShowAchievement;
   }

   public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
      Class<? super T> rawType = type.getRawType();
      if (COMPONENT_TYPE.isAssignableFrom(rawType)) {
         return ComponentSerializerImpl.create(this.features, gson);
      } else if (KEY_TYPE.isAssignableFrom(rawType)) {
         return KeySerializer.INSTANCE;
      } else if (STYLE_TYPE.isAssignableFrom(rawType)) {
         return StyleSerializer.create(this.legacyHoverSerializer, this.compatShowAchievement, this.features, gson);
      } else if (CLICK_ACTION_TYPE.isAssignableFrom(rawType)) {
         return ClickEventActionSerializer.INSTANCE;
      } else if (HOVER_ACTION_TYPE.isAssignableFrom(rawType)) {
         return HoverEventActionSerializer.INSTANCE;
      } else if (SHOW_ITEM_TYPE.isAssignableFrom(rawType)) {
         return ShowItemSerializer.create(gson, this.features);
      } else if (SHOW_ENTITY_TYPE.isAssignableFrom(rawType)) {
         return ShowEntitySerializer.create(gson, this.features);
      } else if (COLOR_WRAPPER_TYPE.isAssignableFrom(rawType)) {
         return TextColorWrapper.Serializer.INSTANCE;
      } else if (COLOR_TYPE.isAssignableFrom(rawType)) {
         return (Boolean)this.features.value(JSONOptions.EMIT_RGB) ? TextColorSerializer.INSTANCE : TextColorSerializer.DOWNSAMPLE_COLOR;
      } else if (BackwardCompatUtil.IS_4_18_0_OR_NEWER && SHADOW_COLOR_TYPE.isAssignableFrom(rawType)) {
         return ShadowColorSerializer.create(this.features);
      } else if (TEXT_DECORATION_TYPE.isAssignableFrom(rawType)) {
         return TextDecorationSerializer.INSTANCE;
      } else if (BLOCK_NBT_POS_TYPE.isAssignableFrom(rawType)) {
         return BlockNBTComponentPosSerializer.INSTANCE;
      } else if (UUID_TYPE.isAssignableFrom(rawType)) {
         return UUIDSerializer.uuidSerializer(this.features);
      } else if (BackwardCompatUtil.IS_4_15_0_OR_NEWER && TRANSLATION_ARGUMENT_TYPE.isAssignableFrom(rawType)) {
         return TranslationArgumentSerializer.create(gson);
      } else {
         return BackwardCompatUtil.IS_4_25_0_OR_NEWER && PROFILE_PROPERTY_TYPE.isAssignableFrom(rawType) ? ProfilePropertySerializer.INSTANCE : null;
      }
   }

   static {
      TRANSLATION_ARGUMENT_TYPE = BackwardCompatUtil.IS_4_15_0_OR_NEWER ? TranslationArgument.class : null;
      SHADOW_COLOR_TYPE = BackwardCompatUtil.IS_4_18_0_OR_NEWER ? ShadowColor.class : null;
      PROFILE_PROPERTY_TYPE = BackwardCompatUtil.IS_4_25_0_OR_NEWER ? PlayerHeadObjectContents.ProfileProperty.class : null;
   }
}
