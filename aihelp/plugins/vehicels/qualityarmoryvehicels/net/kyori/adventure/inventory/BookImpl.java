/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package net.kyori.adventure.inventory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import net.kyori.adventure.internal.Internals;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;

final class BookImpl
implements Book {
    private final Component title;
    private final Component author;
    private final List<Component> pages;

    BookImpl(@NotNull Component component, @NotNull Component component2, @NotNull List<Component> list) {
        this.title = Objects.requireNonNull(component, "title");
        this.author = Objects.requireNonNull(component2, "author");
        this.pages = Collections.unmodifiableList(Objects.requireNonNull(list, "pages"));
    }

    @Override
    @NotNull
    public Component title() {
        return this.title;
    }

    @Override
    @NotNull
    public Book title(@NotNull Component component) {
        return new BookImpl(Objects.requireNonNull(component, "title"), this.author, this.pages);
    }

    @Override
    @NotNull
    public Component author() {
        return this.author;
    }

    @Override
    @NotNull
    public Book author(@NotNull Component component) {
        return new BookImpl(this.title, Objects.requireNonNull(component, "author"), this.pages);
    }

    @Override
    @NotNull
    public List<Component> pages() {
        return this.pages;
    }

    @Override
    @NotNull
    public Book pages(@NotNull List<Component> list) {
        return new BookImpl(this.title, this.author, new ArrayList<Component>((Collection)Objects.requireNonNull(list, "pages")));
    }

    @Override
    @NotNull
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("title", this.title), ExaminableProperty.of("author", this.author), ExaminableProperty.of("pages", this.pages));
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof BookImpl)) {
            return false;
        }
        BookImpl bookImpl = (BookImpl)object;
        return this.title.equals(bookImpl.title) && this.author.equals(bookImpl.author) && this.pages.equals(bookImpl.pages);
    }

    public int hashCode() {
        int n = this.title.hashCode();
        n = 31 * n + this.author.hashCode();
        n = 31 * n + this.pages.hashCode();
        return n;
    }

    public String toString() {
        return Internals.toString(this);
    }

    static final class BuilderImpl
    implements Book.Builder {
        private Component title = Component.empty();
        private Component author = Component.empty();
        private final List<Component> pages = new ArrayList<Component>();

        BuilderImpl() {
        }

        @Override
        @NotNull
        public Book.Builder title(@NotNull Component component) {
            this.title = Objects.requireNonNull(component, "title");
            return this;
        }

        @Override
        @NotNull
        public Book.Builder author(@NotNull Component component) {
            this.author = Objects.requireNonNull(component, "author");
            return this;
        }

        @Override
        @NotNull
        public Book.Builder addPage(@NotNull Component component) {
            this.pages.add(Objects.requireNonNull(component, "page"));
            return this;
        }

        @Override
        @NotNull
        public Book.Builder pages(@NotNull Collection<Component> collection) {
            this.pages.addAll(Objects.requireNonNull(collection, "pages"));
            return this;
        }

        @Override
        @NotNull
        public Book.Builder pages(@NotNull @NotNull Component @NotNull ... componentArray) {
            Collections.addAll(this.pages, componentArray);
            return this;
        }

        @Override
        @NotNull
        public Book build() {
            return new BookImpl(this.title, this.author, new ArrayList<Component>(this.pages));
        }
    }
}

