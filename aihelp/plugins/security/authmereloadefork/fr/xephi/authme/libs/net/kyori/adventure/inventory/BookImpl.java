package fr.xephi.authme.libs.net.kyori.adventure.inventory;

import fr.xephi.authme.libs.net.kyori.adventure.internal.Internals;
import fr.xephi.authme.libs.net.kyori.adventure.text.Component;
import fr.xephi.authme.libs.net.kyori.examination.ExaminableProperty;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;

final class BookImpl implements Book {
   private final Component title;
   private final Component author;
   private final List<Component> pages;

   BookImpl(@NotNull final Component title, @NotNull final Component author, @NotNull final List<Component> pages) {
      this.title = (Component)Objects.requireNonNull(title, "title");
      this.author = (Component)Objects.requireNonNull(author, "author");
      this.pages = Collections.unmodifiableList((List)Objects.requireNonNull(pages, "pages"));
   }

   @NotNull
   public Component title() {
      return this.title;
   }

   @NotNull
   public Book title(@NotNull final Component title) {
      return new BookImpl((Component)Objects.requireNonNull(title, "title"), this.author, this.pages);
   }

   @NotNull
   public Component author() {
      return this.author;
   }

   @NotNull
   public Book author(@NotNull final Component author) {
      return new BookImpl(this.title, (Component)Objects.requireNonNull(author, "author"), this.pages);
   }

   @NotNull
   public List<Component> pages() {
      return this.pages;
   }

   @NotNull
   public Book pages(@NotNull final List<Component> pages) {
      return new BookImpl(this.title, this.author, new ArrayList((Collection)Objects.requireNonNull(pages, "pages")));
   }

   @NotNull
   public Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(ExaminableProperty.of("title", (Object)this.title), ExaminableProperty.of("author", (Object)this.author), ExaminableProperty.of("pages", (Object)this.pages));
   }

   public boolean equals(final Object o) {
      if (this == o) {
         return true;
      } else if (!(o instanceof BookImpl)) {
         return false;
      } else {
         BookImpl that = (BookImpl)o;
         return this.title.equals(that.title) && this.author.equals(that.author) && this.pages.equals(that.pages);
      }
   }

   public int hashCode() {
      int result = this.title.hashCode();
      result = 31 * result + this.author.hashCode();
      result = 31 * result + this.pages.hashCode();
      return result;
   }

   public String toString() {
      return Internals.toString(this);
   }

   static final class BuilderImpl implements Book.Builder {
      private Component title = Component.empty();
      private Component author = Component.empty();
      private final List<Component> pages = new ArrayList();

      @NotNull
      public Book.Builder title(@NotNull final Component title) {
         this.title = (Component)Objects.requireNonNull(title, "title");
         return this;
      }

      @NotNull
      public Book.Builder author(@NotNull final Component author) {
         this.author = (Component)Objects.requireNonNull(author, "author");
         return this;
      }

      @NotNull
      public Book.Builder addPage(@NotNull final Component page) {
         this.pages.add((Component)Objects.requireNonNull(page, "page"));
         return this;
      }

      @NotNull
      public Book.Builder pages(@NotNull final Collection<Component> pages) {
         this.pages.addAll((Collection)Objects.requireNonNull(pages, "pages"));
         return this;
      }

      @NotNull
      public Book.Builder pages(@NotNull final Component... pages) {
         Collections.addAll(this.pages, pages);
         return this;
      }

      @NotNull
      public Book build() {
         return new BookImpl(this.title, this.author, new ArrayList(this.pages));
      }
   }
}
