package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTList;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.adventure.AdventureIndexUtil;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.ComponentLike;
import ac.grim.grimac.shaded.kyori.adventure.text.format.NamedTextColor;
import ac.grim.grimac.shaded.kyori.adventure.text.format.Style;
import ac.grim.grimac.shaded.kyori.adventure.text.format.TextDecoration;
import ac.grim.grimac.shaded.kyori.adventure.util.Index;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

public class ChatTypeDecoration {
   private final String translationKey;
   private final List<ChatTypeDecoration.Parameter> parameters;
   private final Style style;

   public ChatTypeDecoration(String translationKey, List<ChatTypeDecoration.Parameter> parameters, Style style) {
      this.translationKey = translationKey;
      this.parameters = Collections.unmodifiableList(new ArrayList(parameters));
      this.style = style;
   }

   public static ChatTypeDecoration read(PacketWrapper<?> wrapper) {
      String translationKey = wrapper.readString();
      List<ChatTypeDecoration.Parameter> parameters = wrapper.readList((ew) -> {
         return (ChatTypeDecoration.Parameter)ew.readEnum((Enum[])ChatTypeDecoration.Parameter.values());
      });
      Style style = wrapper.readStyle();
      return new ChatTypeDecoration(translationKey, parameters, style);
   }

   public static void write(PacketWrapper<?> wrapper, ChatTypeDecoration decoration) {
      wrapper.writeString(decoration.translationKey);
      wrapper.writeList(decoration.parameters, PacketWrapper::writeEnum);
      wrapper.writeStyle(decoration.style);
   }

   /** @deprecated */
   @Deprecated
   public static ChatTypeDecoration decode(NBT nbt, ClientVersion version) {
      return decode(nbt, PacketWrapper.createDummyWrapper(version));
   }

   public static ChatTypeDecoration decode(NBT nbt, PacketWrapper<?> wrapper) {
      NBTCompound compound = (NBTCompound)nbt;
      String translationKey = compound.getStringTagValueOrThrow("translation_key");
      NBT paramsTag = compound.getTagOrThrow("parameters");
      Object params;
      if (paramsTag instanceof NBTList) {
         params = new ArrayList();
         NBTList<?> paramsTagList = (NBTList)paramsTag;
         Iterator var7 = paramsTagList.getTags().iterator();

         while(var7.hasNext()) {
            NBT paramTag = (NBT)var7.next();
            String paramId = ((NBTString)paramTag).getValue();
            ((List)params).add((ChatTypeDecoration.Parameter)AdventureIndexUtil.indexValueOrThrow(ChatTypeDecoration.Parameter.ID_INDEX, paramId));
         }
      } else {
         String paramId = ((NBTString)paramsTag).getValue();
         params = Collections.singletonList((ChatTypeDecoration.Parameter)AdventureIndexUtil.indexValueOrThrow(ChatTypeDecoration.Parameter.ID_INDEX, paramId));
      }

      NBTCompound styleTag = compound.getCompoundTagOrNull("style");
      Style style = styleTag == null ? Style.empty() : wrapper.getSerializers().nbt().deserializeStyle(styleTag, wrapper);
      return new ChatTypeDecoration(translationKey, (List)params, style);
   }

   /** @deprecated */
   @Deprecated
   public static NBT encode(ChatTypeDecoration decoration, ClientVersion version) {
      return encode(decoration, PacketWrapper.createDummyWrapper(version));
   }

   public static NBT encode(ChatTypeDecoration decoration, PacketWrapper<?> wrapper) {
      NBTList<NBTString> paramsTag = NBTList.createStringList();
      Iterator var3 = decoration.parameters.iterator();

      while(var3.hasNext()) {
         ChatTypeDecoration.Parameter param = (ChatTypeDecoration.Parameter)var3.next();
         paramsTag.addTag(new NBTString(param.getId()));
      }

      NBTCompound compound = new NBTCompound();
      compound.setTag("translation_key", new NBTString(decoration.translationKey));
      compound.setTag("parameters", paramsTag);
      if (!decoration.style.isEmpty()) {
         compound.setTag("style", wrapper.getSerializers().nbt().serializeStyle(decoration.style, wrapper));
      }

      return compound;
   }

   public static ChatTypeDecoration withSender(String translationKey) {
      return new ChatTypeDecoration(translationKey, Arrays.asList(ChatTypeDecoration.Parameter.SENDER, ChatTypeDecoration.Parameter.CONTENT), Style.empty());
   }

   public static ChatTypeDecoration incomingDirectMessage(String translationKey) {
      return new ChatTypeDecoration(translationKey, Arrays.asList(ChatTypeDecoration.Parameter.SENDER, ChatTypeDecoration.Parameter.CONTENT), Style.style(NamedTextColor.GRAY, (TextDecoration[])(TextDecoration.ITALIC)));
   }

   public static ChatTypeDecoration outgoingDirectMessage(String translationKey) {
      return new ChatTypeDecoration(translationKey, Arrays.asList(ChatTypeDecoration.Parameter.TARGET, ChatTypeDecoration.Parameter.CONTENT), Style.style(NamedTextColor.GRAY, (TextDecoration[])(TextDecoration.ITALIC)));
   }

   public static ChatTypeDecoration teamMessage(String translationKey) {
      return new ChatTypeDecoration(translationKey, Arrays.asList(ChatTypeDecoration.Parameter.TARGET, ChatTypeDecoration.Parameter.SENDER, ChatTypeDecoration.Parameter.CONTENT), Style.empty());
   }

   public Component decorate(Component component, ChatType.Bound chatType) {
      ComponentLike[] components = new ComponentLike[this.parameters.size()];

      for(int i = 0; i < components.length; ++i) {
         ChatTypeDecoration.Parameter parameter = (ChatTypeDecoration.Parameter)this.parameters.get(i);
         components[i] = (ComponentLike)parameter.selector.apply(component, chatType);
      }

      return Component.translatable((String)this.translationKey, (String)null, (Style)this.style, (ComponentLike[])components);
   }

   public String getTranslationKey() {
      return this.translationKey;
   }

   public List<ChatTypeDecoration.Parameter> getParameters() {
      return this.parameters;
   }

   public Style getStyle() {
      return this.style;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof ChatTypeDecoration)) {
         return false;
      } else {
         ChatTypeDecoration that = (ChatTypeDecoration)obj;
         if (!this.translationKey.equals(that.translationKey)) {
            return false;
         } else {
            return !this.parameters.equals(that.parameters) ? false : this.style.equals(that.style);
         }
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.translationKey, this.parameters, this.style});
   }

   public static enum Parameter {
      SENDER("sender", (component, type) -> {
         return type.getName();
      }),
      TARGET("target", (component, type) -> {
         return (Component)(type.getTargetName() != null ? type.getTargetName() : Component.empty());
      }),
      CONTENT("content", (component, type) -> {
         return component;
      });

      public static final Index<String, ChatTypeDecoration.Parameter> ID_INDEX = Index.create(ChatTypeDecoration.Parameter.class, ChatTypeDecoration.Parameter::getId);
      private final String id;
      private final BiFunction<Component, ChatType.Bound, Component> selector;

      private Parameter(String id, BiFunction<Component, ChatType.Bound, Component> selector) {
         this.id = id;
         this.selector = selector;
      }

      public String getId() {
         return this.id;
      }

      /** @deprecated */
      @Deprecated
      @Nullable
      public static ChatTypeDecoration.Parameter valueByName(String id) {
         return (ChatTypeDecoration.Parameter)ID_INDEX.value(id);
      }

      // $FF: synthetic method
      private static ChatTypeDecoration.Parameter[] $values() {
         return new ChatTypeDecoration.Parameter[]{SENDER, TARGET, CONTENT};
      }
   }
}
