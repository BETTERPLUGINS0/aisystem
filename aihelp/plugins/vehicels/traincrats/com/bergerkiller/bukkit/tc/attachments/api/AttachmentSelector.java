package com.bergerkiller.bukkit.tc.attachments.api;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.map.MapTexture;
import com.bergerkiller.bukkit.tc.TrainCarts;
import java.util.Optional;

public final class AttachmentSelector<T> {
   private final AttachmentSelector.SearchStrategy strategy;
   private final Optional<String> nameFilter;
   private final Class<T> typeFilter;
   private final boolean excludeSelf;
   private static final MapTexture SEARCH_STRATEGY_ICONS;

   public static AttachmentSelector<Attachment> none() {
      return AttachmentSelector.SearchStrategy.NONE.selectAll();
   }

   public static <T> AttachmentSelector<T> none(Class<T> typeFilter) {
      return new AttachmentSelector(AttachmentSelector.SearchStrategy.NONE, Optional.empty(), typeFilter, false);
   }

   public static <T> AttachmentSelector<T> all(Class<T> typeFilter) {
      return new AttachmentSelector(AttachmentSelector.SearchStrategy.ROOT_CHILDREN, Optional.empty(), typeFilter, false);
   }

   public static AttachmentSelector<Attachment> all(AttachmentSelector.SearchStrategy strategy) {
      return strategy.selectAll();
   }

   public static AttachmentSelector<Attachment> named(AttachmentSelector.SearchStrategy strategy, String nameFilter) {
      return strategy.selectNamed(nameFilter);
   }

   private AttachmentSelector(AttachmentSelector.SearchStrategy strategy, Optional<String> nameFilter, Class<T> typeFilter, boolean excludeSelf) {
      if (strategy == null) {
         throw new IllegalArgumentException("Search Strategy is null");
      } else if (typeFilter == null) {
         throw new IllegalArgumentException("Type Filter is null");
      } else {
         this.strategy = strategy;
         this.nameFilter = nameFilter;
         this.typeFilter = typeFilter;
         this.excludeSelf = excludeSelf;
      }
   }

   public AttachmentSelector.SearchStrategy strategy() {
      return this.strategy;
   }

   public Optional<String> nameFilter() {
      return this.nameFilter;
   }

   public Class<T> typeFilter() {
      return this.typeFilter;
   }

   public boolean usesTypeFilter() {
      return this.typeFilter != Attachment.class;
   }

   public boolean isExcludingSelf() {
      return this.excludeSelf;
   }

   public AttachmentSelector<T> withSelectAll() {
      return new AttachmentSelector(this.strategy, Optional.empty(), this.typeFilter, this.excludeSelf);
   }

   public AttachmentSelector<T> withName(String name) {
      return name != null && !name.isEmpty() ? new AttachmentSelector(this.strategy, Optional.of(name), this.typeFilter, this.excludeSelf) : new AttachmentSelector(AttachmentSelector.SearchStrategy.NONE, Optional.empty(), this.typeFilter, this.excludeSelf);
   }

   public AttachmentSelector<T> withStrategy(AttachmentSelector.SearchStrategy strategy) {
      return new AttachmentSelector(strategy, this.nameFilter, this.typeFilter, this.excludeSelf);
   }

   public <A> AttachmentSelector<A> withType(Class<A> typeFilter) {
      return new AttachmentSelector(this.strategy, this.nameFilter, typeFilter, this.excludeSelf);
   }

   public AttachmentSelector<T> excludingSelf() {
      return this.excludingSelf(true);
   }

   public AttachmentSelector<T> includingSelf() {
      return this.excludingSelf(false);
   }

   public AttachmentSelector<T> excludingSelf(boolean exclude) {
      return new AttachmentSelector(this.strategy, this.nameFilter, this.typeFilter, exclude);
   }

   public int hashCode() {
      return this.nameFilter.isPresent() ? ((String)this.nameFilter.get()).hashCode() : this.typeFilter.hashCode();
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof AttachmentSelector)) {
         return false;
      } else {
         AttachmentSelector<?> other = (AttachmentSelector)o;
         return this.strategy == other.strategy && this.nameFilter.equals(other.nameFilter) && this.typeFilter.equals(other.typeFilter) && this.excludeSelf == other.excludeSelf;
      }
   }

   public String toString() {
      return "AttachmentSelector{type=" + this.typeFilter.getSimpleName() + ", strategy=" + this.strategy + ", name=" + (String)this.nameFilter.orElse("<any>") + ", excludeSelf=" + this.excludeSelf + "}";
   }

   public boolean matches(Attachment attachment) {
      return this.matchesExceptName(attachment) && (!this.nameFilter.isPresent() || attachment.getNames().contains(this.nameFilter.get()));
   }

   public boolean matchesExceptName(Attachment attachment) {
      return this.typeFilter.isInstance(attachment);
   }

   public void writeToConfig(ConfigurationNode config, String key) {
      if (this.strategy == AttachmentSelector.SearchStrategy.NONE) {
         config.remove(key);
      } else if (this.strategy == AttachmentSelector.SearchStrategy.ROOT_CHILDREN && this.nameFilter.isPresent()) {
         config.set(key, this.nameFilter.get());
      } else {
         ConfigurationNode block = config.getNode(key);
         block.set("strategy", this.strategy);
         if (this.nameFilter.isPresent()) {
            block.set("name", this.nameFilter.get());
         } else {
            block.remove("name");
         }
      }

   }

   public static AttachmentSelector<Attachment> readFromConfig(ConfigurationNode config, String key) {
      ConfigurationNode block = config.getNodeIfExists(key);
      if (block != null) {
         AttachmentSelector.SearchStrategy strategy = (AttachmentSelector.SearchStrategy)block.getOrDefault("strategy", AttachmentSelector.SearchStrategy.CHILDREN);
         String nameFilter = (String)block.getOrDefault("name", String.class, (Object)null);
         return nameFilter != null ? strategy.selectNamed(nameFilter) : strategy.selectAll();
      } else {
         String nameFilter = (String)config.getOrDefault(key, String.class, (Object)null);
         return nameFilter != null ? AttachmentSelector.SearchStrategy.ROOT_CHILDREN.selectNamed(nameFilter) : none();
      }
   }

   // $FF: synthetic method
   AttachmentSelector(AttachmentSelector.SearchStrategy x0, Optional x1, Class x2, boolean x3, Object x4) {
      this(x0, x1, x2, x3);
   }

   static {
      SEARCH_STRATEGY_ICONS = MapTexture.loadPluginResource(TrainCarts.plugin, "com/bergerkiller/bukkit/tc/textures/attachments/search_strategies.png");
   }

   public static enum SearchStrategy {
      NONE("Disabled"),
      ROOT_CHILDREN("All of cart"),
      CHILDREN("Children"),
      PARENTS("Parents");

      private final String caption;
      private final AttachmentSelector<Attachment> all;
      private final MapTexture iconDefault;
      private final MapTexture iconFocused;

      private SearchStrategy(String caption) {
         this.caption = caption;
         this.all = new AttachmentSelector(this, Optional.empty(), Attachment.class, false);
         this.iconDefault = AttachmentSelector.SEARCH_STRATEGY_ICONS.getView(this.ordinal() * 11, 0, 11, 7).clone();
         this.iconFocused = AttachmentSelector.SEARCH_STRATEGY_ICONS.getView(this.ordinal() * 11, 7, 11, 7).clone();
      }

      public String getCaption() {
         return this.caption;
      }

      public MapTexture getIcon(boolean focused) {
         return focused ? this.iconFocused : this.iconDefault;
      }

      public AttachmentSelector<Attachment> selectAll() {
         return this.all;
      }

      public AttachmentSelector<Attachment> selectNamed(String nameFilter) {
         return nameFilter != null && !nameFilter.isEmpty() ? new AttachmentSelector(this, Optional.of(nameFilter), Attachment.class, false) : NONE.selectAll();
      }

      // $FF: synthetic method
      private static AttachmentSelector.SearchStrategy[] $values() {
         return new AttachmentSelector.SearchStrategy[]{NONE, ROOT_CHILDREN, CHILDREN, PARENTS};
      }
   }
}
