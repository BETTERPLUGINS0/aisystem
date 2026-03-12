package fr.xephi.authme.libs.net.kyori.adventure.audience;

import fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar;
import fr.xephi.authme.libs.net.kyori.adventure.chat.ChatType;
import fr.xephi.authme.libs.net.kyori.adventure.chat.SignedMessage;
import fr.xephi.authme.libs.net.kyori.adventure.identity.Identified;
import fr.xephi.authme.libs.net.kyori.adventure.identity.Identity;
import fr.xephi.authme.libs.net.kyori.adventure.inventory.Book;
import fr.xephi.authme.libs.net.kyori.adventure.pointer.Pointer;
import fr.xephi.authme.libs.net.kyori.adventure.pointer.Pointers;
import fr.xephi.authme.libs.net.kyori.adventure.resource.ResourcePackRequest;
import fr.xephi.authme.libs.net.kyori.adventure.sound.Sound;
import fr.xephi.authme.libs.net.kyori.adventure.sound.SoundStop;
import fr.xephi.authme.libs.net.kyori.adventure.text.Component;
import fr.xephi.authme.libs.net.kyori.adventure.title.TitlePart;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

@FunctionalInterface
public interface ForwardingAudience extends Audience {
   @ApiStatus.OverrideOnly
   @NotNull
   Iterable<? extends Audience> audiences();

   @NotNull
   default Pointers pointers() {
      return Pointers.empty();
   }

   @NotNull
   default Audience filterAudience(@NotNull final Predicate<? super Audience> filter) {
      List<Audience> audiences = null;
      Iterator var3 = this.audiences().iterator();

      while(var3.hasNext()) {
         Audience audience = (Audience)var3.next();
         if (filter.test(audience)) {
            Audience filtered = audience.filterAudience(filter);
            if (filtered != Audience.empty()) {
               if (audiences == null) {
                  audiences = new ArrayList();
               }

               audiences.add(filtered);
            }
         }
      }

      return (Audience)(audiences != null ? Audience.audience((Iterable)audiences) : Audience.empty());
   }

   default void forEachAudience(@NotNull final Consumer<? super Audience> action) {
      Iterator var2 = this.audiences().iterator();

      while(var2.hasNext()) {
         Audience audience = (Audience)var2.next();
         audience.forEachAudience(action);
      }

   }

   default void sendMessage(@NotNull final Component message) {
      Iterator var2 = this.audiences().iterator();

      while(var2.hasNext()) {
         Audience audience = (Audience)var2.next();
         audience.sendMessage(message);
      }

   }

   default void sendMessage(@NotNull final Component message, @NotNull final ChatType.Bound boundChatType) {
      Iterator var3 = this.audiences().iterator();

      while(var3.hasNext()) {
         Audience audience = (Audience)var3.next();
         audience.sendMessage(message, boundChatType);
      }

   }

   default void sendMessage(@NotNull final SignedMessage signedMessage, @NotNull final ChatType.Bound boundChatType) {
      Iterator var3 = this.audiences().iterator();

      while(var3.hasNext()) {
         Audience audience = (Audience)var3.next();
         audience.sendMessage(signedMessage, boundChatType);
      }

   }

   default void deleteMessage(@NotNull final SignedMessage.Signature signature) {
      Iterator var2 = this.audiences().iterator();

      while(var2.hasNext()) {
         Audience audience = (Audience)var2.next();
         audience.deleteMessage(signature);
      }

   }

   /** @deprecated */
   @Deprecated
   default void sendMessage(@NotNull final Identified source, @NotNull final Component message, @NotNull final MessageType type) {
      Iterator var4 = this.audiences().iterator();

      while(var4.hasNext()) {
         Audience audience = (Audience)var4.next();
         audience.sendMessage(source, message, type);
      }

   }

   /** @deprecated */
   @Deprecated
   default void sendMessage(@NotNull final Identity source, @NotNull final Component message, @NotNull final MessageType type) {
      Iterator var4 = this.audiences().iterator();

      while(var4.hasNext()) {
         Audience audience = (Audience)var4.next();
         audience.sendMessage(source, message, type);
      }

   }

   default void sendActionBar(@NotNull final Component message) {
      Iterator var2 = this.audiences().iterator();

      while(var2.hasNext()) {
         Audience audience = (Audience)var2.next();
         audience.sendActionBar(message);
      }

   }

   default void sendPlayerListHeader(@NotNull final Component header) {
      Iterator var2 = this.audiences().iterator();

      while(var2.hasNext()) {
         Audience audience = (Audience)var2.next();
         audience.sendPlayerListHeader(header);
      }

   }

   default void sendPlayerListFooter(@NotNull final Component footer) {
      Iterator var2 = this.audiences().iterator();

      while(var2.hasNext()) {
         Audience audience = (Audience)var2.next();
         audience.sendPlayerListFooter(footer);
      }

   }

   default void sendPlayerListHeaderAndFooter(@NotNull final Component header, @NotNull final Component footer) {
      Iterator var3 = this.audiences().iterator();

      while(var3.hasNext()) {
         Audience audience = (Audience)var3.next();
         audience.sendPlayerListHeaderAndFooter(header, footer);
      }

   }

   default <T> void sendTitlePart(@NotNull final TitlePart<T> part, @NotNull final T value) {
      Iterator var3 = this.audiences().iterator();

      while(var3.hasNext()) {
         Audience audience = (Audience)var3.next();
         audience.sendTitlePart(part, value);
      }

   }

   default void clearTitle() {
      Iterator var1 = this.audiences().iterator();

      while(var1.hasNext()) {
         Audience audience = (Audience)var1.next();
         audience.clearTitle();
      }

   }

   default void resetTitle() {
      Iterator var1 = this.audiences().iterator();

      while(var1.hasNext()) {
         Audience audience = (Audience)var1.next();
         audience.resetTitle();
      }

   }

   default void showBossBar(@NotNull final BossBar bar) {
      Iterator var2 = this.audiences().iterator();

      while(var2.hasNext()) {
         Audience audience = (Audience)var2.next();
         audience.showBossBar(bar);
      }

   }

   default void hideBossBar(@NotNull final BossBar bar) {
      Iterator var2 = this.audiences().iterator();

      while(var2.hasNext()) {
         Audience audience = (Audience)var2.next();
         audience.hideBossBar(bar);
      }

   }

   default void playSound(@NotNull final Sound sound) {
      Iterator var2 = this.audiences().iterator();

      while(var2.hasNext()) {
         Audience audience = (Audience)var2.next();
         audience.playSound(sound);
      }

   }

   default void playSound(@NotNull final Sound sound, final double x, final double y, final double z) {
      Iterator var8 = this.audiences().iterator();

      while(var8.hasNext()) {
         Audience audience = (Audience)var8.next();
         audience.playSound(sound, x, y, z);
      }

   }

   default void playSound(@NotNull final Sound sound, @NotNull final Sound.Emitter emitter) {
      Iterator var3 = this.audiences().iterator();

      while(var3.hasNext()) {
         Audience audience = (Audience)var3.next();
         audience.playSound(sound, emitter);
      }

   }

   default void stopSound(@NotNull final SoundStop stop) {
      Iterator var2 = this.audiences().iterator();

      while(var2.hasNext()) {
         Audience audience = (Audience)var2.next();
         audience.stopSound(stop);
      }

   }

   default void openBook(@NotNull final Book book) {
      Iterator var2 = this.audiences().iterator();

      while(var2.hasNext()) {
         Audience audience = (Audience)var2.next();
         audience.openBook(book);
      }

   }

   default void sendResourcePacks(@NotNull final ResourcePackRequest request) {
      Iterator var2 = this.audiences().iterator();

      while(var2.hasNext()) {
         Audience audience = (Audience)var2.next();
         audience.sendResourcePacks(request);
      }

   }

   default void removeResourcePacks(@NotNull final Iterable<UUID> ids) {
      Iterator var2 = this.audiences().iterator();

      while(var2.hasNext()) {
         Audience audience = (Audience)var2.next();
         audience.removeResourcePacks(ids);
      }

   }

   default void removeResourcePacks(@NotNull final UUID id, @NotNull final UUID... others) {
      Iterator var3 = this.audiences().iterator();

      while(var3.hasNext()) {
         Audience audience = (Audience)var3.next();
         audience.removeResourcePacks(id, others);
      }

   }

   default void clearResourcePacks() {
      Iterator var1 = this.audiences().iterator();

      while(var1.hasNext()) {
         Audience audience = (Audience)var1.next();
         audience.clearResourcePacks();
      }

   }

   public interface Single extends ForwardingAudience {
      @ApiStatus.OverrideOnly
      @NotNull
      Audience audience();

      /** @deprecated */
      @Deprecated
      @NotNull
      default Iterable<? extends Audience> audiences() {
         return Collections.singleton(this.audience());
      }

      @NotNull
      default <T> Optional<T> get(@NotNull final Pointer<T> pointer) {
         return this.audience().get(pointer);
      }

      @Contract("_, null -> null; _, !null -> !null")
      @Nullable
      default <T> T getOrDefault(@NotNull final Pointer<T> pointer, @Nullable final T defaultValue) {
         return this.audience().getOrDefault(pointer, defaultValue);
      }

      @UnknownNullability
      default <T> T getOrDefaultFrom(@NotNull final Pointer<T> pointer, @NotNull final Supplier<? extends T> defaultValue) {
         return this.audience().getOrDefaultFrom(pointer, defaultValue);
      }

      @NotNull
      default Audience filterAudience(@NotNull final Predicate<? super Audience> filter) {
         Audience audience = this.audience();
         return (Audience)(filter.test(audience) ? this : Audience.empty());
      }

      default void forEachAudience(@NotNull final Consumer<? super Audience> action) {
         this.audience().forEachAudience(action);
      }

      @NotNull
      default Pointers pointers() {
         return this.audience().pointers();
      }

      default void sendMessage(@NotNull final Component message) {
         this.audience().sendMessage(message);
      }

      default void sendMessage(@NotNull final Component message, @NotNull final ChatType.Bound boundChatType) {
         this.audience().sendMessage(message, boundChatType);
      }

      default void sendMessage(@NotNull final SignedMessage signedMessage, @NotNull final ChatType.Bound boundChatType) {
         this.audience().sendMessage(signedMessage, boundChatType);
      }

      default void deleteMessage(@NotNull final SignedMessage.Signature signature) {
         this.audience().deleteMessage(signature);
      }

      /** @deprecated */
      @Deprecated
      default void sendMessage(@NotNull final Identified source, @NotNull final Component message, @NotNull final MessageType type) {
         this.audience().sendMessage(source, message, type);
      }

      /** @deprecated */
      @Deprecated
      default void sendMessage(@NotNull final Identity source, @NotNull final Component message, @NotNull final MessageType type) {
         this.audience().sendMessage(source, message, type);
      }

      default void sendActionBar(@NotNull final Component message) {
         this.audience().sendActionBar(message);
      }

      default void sendPlayerListHeader(@NotNull final Component header) {
         this.audience().sendPlayerListHeader(header);
      }

      default void sendPlayerListFooter(@NotNull final Component footer) {
         this.audience().sendPlayerListFooter(footer);
      }

      default void sendPlayerListHeaderAndFooter(@NotNull final Component header, @NotNull final Component footer) {
         this.audience().sendPlayerListHeaderAndFooter(header, footer);
      }

      default <T> void sendTitlePart(@NotNull final TitlePart<T> part, @NotNull final T value) {
         this.audience().sendTitlePart(part, value);
      }

      default void clearTitle() {
         this.audience().clearTitle();
      }

      default void resetTitle() {
         this.audience().resetTitle();
      }

      default void showBossBar(@NotNull final BossBar bar) {
         this.audience().showBossBar(bar);
      }

      default void hideBossBar(@NotNull final BossBar bar) {
         this.audience().hideBossBar(bar);
      }

      default void playSound(@NotNull final Sound sound) {
         this.audience().playSound(sound);
      }

      default void playSound(@NotNull final Sound sound, final double x, final double y, final double z) {
         this.audience().playSound(sound, x, y, z);
      }

      default void playSound(@NotNull final Sound sound, @NotNull final Sound.Emitter emitter) {
         this.audience().playSound(sound, emitter);
      }

      default void stopSound(@NotNull final SoundStop stop) {
         this.audience().stopSound(stop);
      }

      default void openBook(@NotNull final Book book) {
         this.audience().openBook(book);
      }

      default void sendResourcePacks(@NotNull final ResourcePackRequest request) {
         this.audience().sendResourcePacks(request.callback(Audiences.unwrapCallback(this, this.audience(), request.callback())));
      }

      default void removeResourcePacks(@NotNull final Iterable<UUID> ids) {
         this.audience().removeResourcePacks(ids);
      }

      default void removeResourcePacks(@NotNull final UUID id, @NotNull final UUID... others) {
         this.audience().removeResourcePacks(id, others);
      }

      default void clearResourcePacks() {
         this.audience().clearResourcePacks();
      }
   }
}
