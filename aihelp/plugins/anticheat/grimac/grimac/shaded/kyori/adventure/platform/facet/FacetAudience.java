package ac.grim.grimac.shaded.kyori.adventure.platform.facet;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.audience.Audience;
import ac.grim.grimac.shaded.kyori.adventure.audience.MessageType;
import ac.grim.grimac.shaded.kyori.adventure.bossbar.BossBar;
import ac.grim.grimac.shaded.kyori.adventure.chat.ChatType;
import ac.grim.grimac.shaded.kyori.adventure.chat.SignedMessage;
import ac.grim.grimac.shaded.kyori.adventure.identity.Identity;
import ac.grim.grimac.shaded.kyori.adventure.inventory.Book;
import ac.grim.grimac.shaded.kyori.adventure.pointer.Pointers;
import ac.grim.grimac.shaded.kyori.adventure.sound.Sound;
import ac.grim.grimac.shaded.kyori.adventure.sound.SoundStop;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.flattener.ComponentFlattener;
import ac.grim.grimac.shaded.kyori.adventure.title.Title;
import ac.grim.grimac.shaded.kyori.adventure.title.TitlePart;
import java.io.Closeable;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArraySet;

@ApiStatus.Internal
public class FacetAudience<V> implements Audience, Closeable {
   @NotNull
   protected final FacetAudienceProvider<V, FacetAudience<V>> provider;
   @NotNull
   private final Set<V> viewers;
   @Nullable
   private V viewer;
   private volatile Pointers pointers;
   @Nullable
   private final Facet.Chat<V, Object> chat;
   @Nullable
   private final Facet.ActionBar<V, Object> actionBar;
   @Nullable
   private final Facet.Title<V, Object, Object, Object> title;
   @Nullable
   private final Facet.Sound<V, Object> sound;
   @Nullable
   private final Facet.EntitySound<V, Object> entitySound;
   @Nullable
   private final Facet.Book<V, Object, Object> book;
   @Nullable
   private final Facet.BossBar.Builder<V, Facet.BossBar<V>> bossBar;
   @Nullable
   private final Map<BossBar, Facet.BossBar<V>> bossBars;
   @Nullable
   private final Facet.TabList<V, Object> tabList;
   @NotNull
   private final Collection<? extends Facet.Pointers<V>> pointerProviders;

   public FacetAudience(@NotNull final FacetAudienceProvider provider, @NotNull final Collection<? extends V> viewers, @Nullable final Collection<? extends Facet.Chat> chat, @Nullable final Collection<? extends Facet.ActionBar> actionBar, @Nullable final Collection<? extends Facet.Title> title, @Nullable final Collection<? extends Facet.Sound> sound, @Nullable final Collection<? extends Facet.EntitySound> entitySound, @Nullable final Collection<? extends Facet.Book> book, @Nullable final Collection<? extends Facet.BossBar.Builder> bossBar, @Nullable final Collection<? extends Facet.TabList> tabList, @Nullable final Collection<? extends Facet.Pointers> pointerProviders) {
      this.provider = (FacetAudienceProvider)Objects.requireNonNull(provider, "audience provider");
      this.viewers = new CopyOnWriteArraySet();
      Iterator var12 = ((Collection)Objects.requireNonNull(viewers, "viewers")).iterator();

      while(var12.hasNext()) {
         V viewer = var12.next();
         this.addViewer(viewer);
      }

      this.refresh();
      this.chat = (Facet.Chat)Facet.of(chat, this.viewer);
      this.actionBar = (Facet.ActionBar)Facet.of(actionBar, this.viewer);
      this.title = (Facet.Title)Facet.of(title, this.viewer);
      this.sound = (Facet.Sound)Facet.of(sound, this.viewer);
      this.entitySound = (Facet.EntitySound)Facet.of(entitySound, this.viewer);
      this.book = (Facet.Book)Facet.of(book, this.viewer);
      this.bossBar = (Facet.BossBar.Builder)Facet.of(bossBar, this.viewer);
      this.bossBars = this.bossBar == null ? null : Collections.synchronizedMap(new IdentityHashMap(4));
      this.tabList = (Facet.TabList)Facet.of(tabList, this.viewer);
      this.pointerProviders = (Collection)(pointerProviders == null ? Collections.emptyList() : pointerProviders);
   }

   public void addViewer(@NotNull final V viewer) {
      if (this.viewers.add(viewer) && this.viewer == null) {
         this.viewer = viewer;
         this.refresh();
      }

   }

   public void removeViewer(@NotNull final V viewer) {
      if (this.viewers.remove(viewer) && this.viewer == viewer) {
         this.viewer = this.viewers.isEmpty() ? null : this.viewers.iterator().next();
         this.refresh();
      }

      if (this.bossBars != null) {
         Iterator var2 = this.bossBars.values().iterator();

         while(var2.hasNext()) {
            Facet.BossBar<V> listener = (Facet.BossBar)var2.next();
            listener.removeViewer(viewer);
         }

      }
   }

   public void refresh() {
      synchronized(this) {
         this.pointers = null;
      }

      if (this.bossBars != null) {
         Iterator var1 = this.bossBars.entrySet().iterator();

         while(var1.hasNext()) {
            Entry<BossBar, Facet.BossBar<V>> entry = (Entry)var1.next();
            BossBar bar = (BossBar)entry.getKey();
            Facet.BossBar<V> listener = (Facet.BossBar)entry.getValue();
            listener.bossBarNameChanged(bar, bar.name(), bar.name());
         }

      }
   }

   public void sendMessage(@NotNull final Identity source, @NotNull final Component original, @NotNull final MessageType type) {
      if (this.chat != null) {
         Object message = this.createMessage(original, this.chat);
         if (message != null) {
            Iterator var5 = this.viewers.iterator();

            while(var5.hasNext()) {
               V viewer = var5.next();
               this.chat.sendMessage(viewer, source, message, type);
            }

         }
      }
   }

   public void sendMessage(@NotNull final Component original, @NotNull final ChatType.Bound boundChatType) {
      if (this.chat != null) {
         Object message = this.createMessage(original, this.chat);
         if (message != null) {
            Component name = this.provider.componentRenderer.render(boundChatType.name(), this);
            Component target = null;
            if (boundChatType.target() != null) {
               target = this.provider.componentRenderer.render(boundChatType.target(), this);
            }

            Object renderedType = boundChatType.type().bind(name, target);
            Iterator var7 = this.viewers.iterator();

            while(var7.hasNext()) {
               V viewer = var7.next();
               this.chat.sendMessage(viewer, Identity.nil(), message, renderedType);
            }

         }
      }
   }

   public void sendMessage(@NotNull final SignedMessage signedMessage, @NotNull final ChatType.Bound boundChatType) {
      if (signedMessage.isSystem()) {
         Component content = signedMessage.unsignedContent() != null ? signedMessage.unsignedContent() : Component.text(signedMessage.message());
         this.sendMessage((Component)content, boundChatType);
      } else {
         Audience.super.sendMessage(signedMessage, boundChatType);
      }

   }

   public void sendActionBar(@NotNull final Component original) {
      if (this.actionBar != null) {
         Object message = this.createMessage(original, this.actionBar);
         if (message != null) {
            Iterator var3 = this.viewers.iterator();

            while(var3.hasNext()) {
               V viewer = var3.next();
               this.actionBar.sendMessage(viewer, message);
            }

         }
      }
   }

   public void playSound(@NotNull final Sound original) {
      if (this.sound != null) {
         Iterator var2 = this.viewers.iterator();

         while(var2.hasNext()) {
            V viewer = var2.next();
            Object position = this.sound.createPosition(viewer);
            if (position != null) {
               this.sound.playSound(viewer, original, position);
            }
         }

      }
   }

   public void playSound(@NotNull final Sound sound, @NotNull final Sound.Emitter emitter) {
      if (this.entitySound != null) {
         Object message;
         if (emitter == Sound.Emitter.self()) {
            Iterator var3 = this.viewers.iterator();

            while(var3.hasNext()) {
               V viewer = var3.next();
               message = this.entitySound.createForSelf(viewer, sound);
               if (message != null) {
                  this.entitySound.playSound(viewer, message);
               }
            }
         } else {
            Object message = this.entitySound.createForEmitter(sound, emitter);
            if (message == null) {
               return;
            }

            Iterator var7 = this.viewers.iterator();

            while(var7.hasNext()) {
               message = var7.next();
               this.entitySound.playSound(message, message);
            }
         }

      }
   }

   public void playSound(@NotNull final Sound original, final double x, final double y, final double z) {
      if (this.sound != null) {
         Object position = this.sound.createPosition(x, y, z);
         Iterator var9 = this.viewers.iterator();

         while(var9.hasNext()) {
            V viewer = var9.next();
            this.sound.playSound(viewer, original, position);
         }

      }
   }

   public void stopSound(@NotNull final SoundStop original) {
      if (this.sound != null) {
         Iterator var2 = this.viewers.iterator();

         while(var2.hasNext()) {
            V viewer = var2.next();
            this.sound.stopSound(viewer, original);
         }

      }
   }

   public void openBook(@NotNull final Book original) {
      if (this.book != null) {
         String title = this.toPlain(original.title());
         String author = this.toPlain(original.author());
         List<Object> pages = new LinkedList();
         Iterator var5 = original.pages().iterator();

         Object viewer;
         while(var5.hasNext()) {
            Component originalPage = (Component)var5.next();
            viewer = this.createMessage(originalPage, this.book);
            if (viewer != null) {
               pages.add(viewer);
            }
         }

         if (title != null && author != null && !pages.isEmpty()) {
            Object book = this.book.createBook(title, author, pages);
            if (book != null) {
               Iterator var9 = this.viewers.iterator();

               while(var9.hasNext()) {
                  viewer = var9.next();
                  this.book.openBook(viewer, book);
               }

            }
         }
      }
   }

   private String toPlain(final Component comp) {
      if (comp == null) {
         return null;
      } else {
         StringBuilder builder = new StringBuilder();
         ComponentFlattener var10000 = ComponentFlattener.basic();
         Component var10001 = this.provider.componentRenderer.render(comp, this);
         Objects.requireNonNull(builder);
         var10000.flatten(var10001, builder::append);
         return builder.toString();
      }
   }

   public void showTitle(final Title original) {
      if (this.title != null) {
         Object mainTitle = this.createMessage(original.title(), this.title);
         Object subTitle = this.createMessage(original.subtitle(), this.title);
         Title.Times times = original.times();
         int inTicks = times == null ? -1 : this.title.toTicks(times.fadeIn());
         int stayTicks = times == null ? -1 : this.title.toTicks(times.stay());
         int outTicks = times == null ? -1 : this.title.toTicks(times.fadeOut());
         Object collection = this.title.createTitleCollection();
         if (inTicks != -1 || stayTicks != -1 || outTicks != -1) {
            this.title.contributeTimes(collection, inTicks, stayTicks, outTicks);
         }

         this.title.contributeSubtitle(collection, subTitle);
         this.title.contributeTitle(collection, mainTitle);
         Object title = this.title.completeTitle(collection);
         if (title != null) {
            Iterator var10 = this.viewers.iterator();

            while(var10.hasNext()) {
               V viewer = var10.next();
               this.title.showTitle(viewer, title);
            }

         }
      }
   }

   public <T> void sendTitlePart(@NotNull final TitlePart<T> part, @NotNull final T value) {
      if (this.title != null) {
         Objects.requireNonNull(value, "value");
         Object collection = this.title.createTitleCollection();
         Object title;
         if (part == TitlePart.TITLE) {
            title = this.createMessage((Component)value, this.title);
            if (title != null) {
               this.title.contributeTitle(collection, title);
            }
         } else if (part == TitlePart.SUBTITLE) {
            title = this.createMessage((Component)value, this.title);
            if (title != null) {
               this.title.contributeSubtitle(collection, title);
            }
         } else {
            if (part != TitlePart.TIMES) {
               throw new IllegalArgumentException("Unknown TitlePart '" + part + "'");
            }

            Title.Times times = (Title.Times)value;
            int inTicks = this.title.toTicks(times.fadeIn());
            int stayTicks = this.title.toTicks(times.stay());
            int outTicks = this.title.toTicks(times.fadeOut());
            if (inTicks != -1 || stayTicks != -1 || outTicks != -1) {
               this.title.contributeTimes(collection, inTicks, stayTicks, outTicks);
            }
         }

         title = this.title.completeTitle(collection);
         if (title != null) {
            Iterator var9 = this.viewers.iterator();

            while(var9.hasNext()) {
               V viewer = var9.next();
               this.title.showTitle(viewer, title);
            }

         }
      }
   }

   public void clearTitle() {
      if (this.title != null) {
         Iterator var1 = this.viewers.iterator();

         while(var1.hasNext()) {
            V viewer = var1.next();
            this.title.clearTitle(viewer);
         }

      }
   }

   public void resetTitle() {
      if (this.title != null) {
         Iterator var1 = this.viewers.iterator();

         while(var1.hasNext()) {
            V viewer = var1.next();
            this.title.resetTitle(viewer);
         }

      }
   }

   public void showBossBar(@NotNull final BossBar bar) {
      if (this.bossBar != null && this.bossBars != null) {
         Object listener;
         synchronized(this.bossBars) {
            listener = (Facet.BossBar)this.bossBars.get(bar);
            if (listener == null) {
               listener = new FacetBossBarListener(this.bossBar.createBossBar(this.viewers), (message) -> {
                  return this.provider.componentRenderer.render(message, this);
               });
               this.bossBars.put(bar, listener);
            }
         }

         if (((Facet.BossBar)listener).isEmpty()) {
            ((Facet.BossBar)listener).bossBarInitialized(bar);
            bar.addListener((BossBar.Listener)listener);
         }

         Iterator var3 = this.viewers.iterator();

         while(var3.hasNext()) {
            V viewer = var3.next();
            ((Facet.BossBar)listener).addViewer(viewer);
         }

      }
   }

   public void hideBossBar(@NotNull final BossBar bar) {
      if (this.bossBars != null) {
         Facet.BossBar<V> listener = (Facet.BossBar)this.bossBars.get(bar);
         if (listener != null) {
            Iterator var3 = this.viewers.iterator();

            while(var3.hasNext()) {
               V viewer = var3.next();
               listener.removeViewer(viewer);
            }

            if (listener.isEmpty() && this.bossBars.remove(bar) != null) {
               bar.removeListener(listener);
               listener.close();
            }

         }
      }
   }

   public void sendPlayerListHeader(@NotNull final Component header) {
      if (this.tabList != null) {
         Object headerFormatted = this.createMessage(header, this.tabList);
         if (headerFormatted == null) {
            return;
         }

         Iterator var3 = this.viewers.iterator();

         while(var3.hasNext()) {
            V viewer = var3.next();
            this.tabList.send(viewer, headerFormatted, (Object)null);
         }
      }

   }

   public void sendPlayerListFooter(@NotNull final Component footer) {
      if (this.tabList != null) {
         Object footerFormatted = this.createMessage(footer, this.tabList);
         if (footerFormatted == null) {
            return;
         }

         Iterator var3 = this.viewers.iterator();

         while(var3.hasNext()) {
            V viewer = var3.next();
            this.tabList.send(viewer, (Object)null, footerFormatted);
         }
      }

   }

   public void sendPlayerListHeaderAndFooter(@NotNull final Component header, @NotNull final Component footer) {
      if (this.tabList != null) {
         Object headerFormatted = this.createMessage(header, this.tabList);
         Object footerFormatted = this.createMessage(footer, this.tabList);
         if (headerFormatted == null || footerFormatted == null) {
            return;
         }

         Iterator var5 = this.viewers.iterator();

         while(var5.hasNext()) {
            V viewer = var5.next();
            this.tabList.send(viewer, headerFormatted, footerFormatted);
         }
      }

   }

   @NotNull
   public Pointers pointers() {
      if (this.pointers == null) {
         synchronized(this) {
            if (this.pointers == null) {
               V viewer = this.viewer;
               if (viewer == null) {
                  return Pointers.empty();
               }

               Pointers.Builder builder = Pointers.builder();
               this.contributePointers(builder);
               Iterator var4 = this.pointerProviders.iterator();

               while(var4.hasNext()) {
                  Facet.Pointers<V> provider = (Facet.Pointers)var4.next();
                  if (provider.isApplicable(viewer)) {
                     provider.contributePointers(viewer, builder);
                  }
               }

               return this.pointers = (Pointers)builder.build();
            }
         }
      }

      return this.pointers;
   }

   @ApiStatus.OverrideOnly
   protected void contributePointers(final Pointers.Builder builder) {
   }

   public void close() {
      Iterator var1;
      if (this.bossBars != null) {
         var1 = (new LinkedList(this.bossBars.keySet())).iterator();

         while(var1.hasNext()) {
            BossBar bar = (BossBar)var1.next();
            this.hideBossBar(bar);
         }

         this.bossBars.clear();
      }

      var1 = this.viewers.iterator();

      while(var1.hasNext()) {
         V viewer = var1.next();
         this.removeViewer(viewer);
      }

      this.viewers.clear();
   }

   @Nullable
   private Object createMessage(@NotNull final Component original, @NotNull final Facet.Message<V, Object> facet) {
      Component message = this.provider.componentRenderer.render(original, this);
      V viewer = this.viewer;
      return viewer == null ? null : facet.createMessage(viewer, message);
   }
}
