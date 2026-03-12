package fr.xephi.authme.libs.net.kyori.adventure.platform.bukkit;

import fr.xephi.authme.libs.net.kyori.adventure.audience.MessageType;
import fr.xephi.authme.libs.net.kyori.adventure.identity.Identity;
import fr.xephi.authme.libs.net.kyori.adventure.platform.facet.Facet;
import fr.xephi.authme.libs.net.kyori.adventure.platform.facet.FacetBase;
import fr.xephi.authme.libs.net.kyori.adventure.platform.facet.FacetComponentFlattener;
import fr.xephi.authme.libs.net.kyori.adventure.platform.facet.Knob;
import fr.xephi.authme.libs.net.kyori.adventure.text.Component;
import fr.xephi.authme.libs.net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import java.util.Iterator;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.TranslationRegistry;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class SpigotFacet<V extends CommandSender> extends FacetBase<V> {
   private static final boolean SUPPORTED = Knob.isEnabled("spigot", true) && BungeeComponentSerializer.isNative();
   private static final Class<?> BUNGEE_CHAT_MESSAGE_TYPE = MinecraftReflection.findClass("net.md_5.bungee.api.ChatMessageType");
   static final Class<?> BUNGEE_COMPONENT_TYPE = MinecraftReflection.findClass("net.md_5.bungee.api.chat.BaseComponent");

   protected SpigotFacet(@Nullable final Class<? extends V> viewerClass) {
      super(viewerClass);
   }

   public boolean isSupported() {
      return super.isSupported() && SUPPORTED;
   }

   static class Translator extends FacetBase<Server> implements FacetComponentFlattener.Translator<Server> {
      private static final boolean SUPPORTED = MinecraftReflection.hasClass("net.md_5.bungee.chat.TranslationRegistry");

      Translator() {
         super(Server.class);
      }

      public boolean isSupported() {
         return super.isSupported() && SUPPORTED;
      }

      @NotNull
      public String valueOrDefault(@NotNull final Server game, @NotNull final String key) {
         return TranslationRegistry.INSTANCE.translate(key);
      }
   }

   static final class Book extends SpigotFacet.Message<Player> implements Facet.Book<Player, BaseComponent[], ItemStack> {
      private static final boolean SUPPORTED = MinecraftReflection.hasMethod(Player.class, "openBook", ItemStack.class);

      protected Book() {
         super(Player.class);
      }

      public boolean isSupported() {
         return super.isSupported() && SUPPORTED;
      }

      @NotNull
      public ItemStack createBook(@NotNull final String title, @NotNull final String author, @NotNull final Iterable<BaseComponent[]> pages) {
         ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
         ItemMeta meta = book.getItemMeta();
         if (meta instanceof BookMeta) {
            BookMeta spigot = (BookMeta)meta;
            Iterator var7 = pages.iterator();

            while(var7.hasNext()) {
               BaseComponent[] page = (BaseComponent[])var7.next();
               spigot.spigot().addPage(new BaseComponent[][]{page});
            }

            spigot.setTitle(title);
            spigot.setAuthor(author);
            book.setItemMeta(spigot);
         }

         return book;
      }

      public void openBook(@NotNull final Player viewer, @NotNull final ItemStack book) {
         viewer.openBook(book);
      }
   }

   static final class ActionBar extends SpigotFacet.ChatWithType implements Facet.ActionBar<Player, BaseComponent[]> {
      public void sendMessage(@NotNull final Player viewer, @NotNull final BaseComponent[] message) {
         viewer.spigot().sendMessage(ChatMessageType.ACTION_BAR, message);
      }
   }

   static class ChatWithType extends SpigotFacet.Message<Player> implements Facet.Chat<Player, BaseComponent[]> {
      private static final Class<?> PLAYER_CLASS = MinecraftReflection.findClass("org.bukkit.entity.Player$Spigot");
      private static final boolean SUPPORTED;

      protected ChatWithType() {
         super(Player.class);
      }

      public boolean isSupported() {
         return super.isSupported() && SUPPORTED;
      }

      @Nullable
      private ChatMessageType createType(@NotNull final MessageType type) {
         if (type == MessageType.CHAT) {
            return ChatMessageType.CHAT;
         } else if (type == MessageType.SYSTEM) {
            return ChatMessageType.SYSTEM;
         } else {
            Knob.logUnsupported(this, type);
            return null;
         }
      }

      public void sendMessage(@NotNull final Player viewer, @NotNull final Identity source, @NotNull final BaseComponent[] message, @NotNull final Object type) {
         ChatMessageType chat = type instanceof MessageType ? this.createType((MessageType)type) : ChatMessageType.SYSTEM;
         if (chat != null) {
            viewer.spigot().sendMessage(chat, message);
         }

      }

      static {
         SUPPORTED = MinecraftReflection.hasMethod(PLAYER_CLASS, "sendMessage", SpigotFacet.BUNGEE_CHAT_MESSAGE_TYPE, BUNGEE_COMPONENT_TYPE);
      }
   }

   static final class Chat extends SpigotFacet.Message<CommandSender> implements Facet.Chat<CommandSender, BaseComponent[]> {
      private static final boolean SUPPORTED = MinecraftReflection.hasClass("org.bukkit.command.CommandSender$Spigot");

      protected Chat() {
         super(CommandSender.class);
      }

      public boolean isSupported() {
         return super.isSupported() && SUPPORTED;
      }

      public void sendMessage(@NotNull final CommandSender viewer, @NotNull final Identity source, @NotNull final BaseComponent[] message, @NotNull final Object type) {
         viewer.spigot().sendMessage(message);
      }
   }

   static class Message<V extends CommandSender> extends SpigotFacet<V> implements Facet.Message<V, BaseComponent[]> {
      private static final BungeeComponentSerializer SERIALIZER = BungeeComponentSerializer.of(BukkitComponentSerializer.gson(), BukkitComponentSerializer.legacy());

      protected Message(@Nullable final Class<? extends V> viewerClass) {
         super(viewerClass);
      }

      @NotNull
      public BaseComponent[] createMessage(@NotNull final V viewer, @NotNull final Component message) {
         return SERIALIZER.serialize(message);
      }
   }
}
