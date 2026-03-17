package me.PM2.infinitevehicles.xseries.messages;

import java.lang.invoke.MethodHandle;
import java.util.Objects;
import me.PM2.infinitevehicles.xseries.reflection.XReflection;
import me.PM2.infinitevehicles.xseries.reflection.jvm.classes.ClassHandle;
import me.PM2.infinitevehicles.xseries.reflection.minecraft.MinecraftClassHandle;
import me.PM2.infinitevehicles.xseries.reflection.minecraft.MinecraftPackage;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.jetbrains.annotations.ApiStatus.Experimental;

public final class MessageComponents {
   private static final MethodHandle CraftChatMessage_fromJson;

   public static MessageComponents.MessageTextString ofNullable(String var0) {
      return var0 == null ? null : new MessageComponents.MessageTextString(var0);
   }

   public static MessageComponents.MessageTextComponent ofNullable(BaseComponent var0) {
      return var0 == null ? null : new MessageComponents.MessageTextComponent(var0);
   }

   @Experimental
   public static Object bungeeToVanilla(BaseComponent var0) {
      String var1 = ComponentSerializer.toString(var0);
      return CraftChatMessage_fromJson.invoke(var1);
   }

   public static BaseComponent fromLegacy(String var0) {
      return new TextComponent(TextComponent.fromLegacyText(var0));
   }

   static {
      MinecraftClassHandle var1 = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "network.chat").named("IChatBaseComponent");

      MethodHandle var0;
      try {
         var0 = XReflection.ofMinecraft().inPackage(MinecraftPackage.CB, "util").named("CraftChatMessage").method("public static IChatBaseComponent fromJSON(String jsonMessage)").returns((ClassHandle)var1).reflect();
      } catch (Throwable var3) {
         var0 = null;
      }

      CraftChatMessage_fromJson = var0;
   }

   public static final class MessageTextString implements MessageComponents.MessageText {
      private final String string;

      public MessageTextString(String var1) {
         this.string = (String)Objects.requireNonNull(var1, "Message cannot be null");
      }

      public String toString() {
         return this.getClass().getSimpleName() + '(' + this.string + ')';
      }

      public String asString() {
         return this.string;
      }

      public BaseComponent asComponent() {
         return MessageComponents.fromLegacy(this.string);
      }
   }

   public static final class MessageTextComponent implements MessageComponents.MessageText {
      private final BaseComponent component;

      public MessageTextComponent(BaseComponent var1) {
         this.component = (BaseComponent)Objects.requireNonNull(var1, "Message cannot be null");
      }

      public String toString() {
         return this.getClass().getSimpleName() + '(' + this.component + ')';
      }

      public String asString() {
         return this.component.toLegacyText();
      }

      public BaseComponent asComponent() {
         return this.component;
      }
   }

   public interface MessageText {
      String asString();

      BaseComponent asComponent();
   }
}
