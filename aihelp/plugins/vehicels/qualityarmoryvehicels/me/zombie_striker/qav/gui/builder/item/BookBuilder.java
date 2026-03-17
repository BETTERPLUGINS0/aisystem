/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.BookMeta
 *  org.bukkit.inventory.meta.BookMeta$Generation
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.jetbrains.annotations.Contract
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package me.zombie_striker.qav.gui.builder.item;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import me.zombie_striker.qav.gui.builder.item.BaseItemBuilder;
import me.zombie_striker.qav.gui.components.exception.GuiException;
import me.zombie_striker.qav.gui.components.util.Legacy;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BookBuilder
extends BaseItemBuilder<BookBuilder> {
    private static final EnumSet<Material> BOOKS = EnumSet.of(Material.WRITABLE_BOOK, Material.WRITTEN_BOOK);

    BookBuilder(@NotNull ItemStack itemStack) {
        super(itemStack);
        if (!BOOKS.contains(itemStack.getType())) {
            throw new GuiException("BookBuilder requires the material to be a WRITABLE_BOOK/WRITTEN_BOOK!");
        }
    }

    @NotNull
    @Contract(value="_ -> this")
    public BookBuilder author(@Nullable Component component) {
        BookMeta bookMeta = (BookMeta)this.getMeta();
        if (component == null) {
            bookMeta.setAuthor(null);
            this.setMeta((ItemMeta)bookMeta);
            return this;
        }
        bookMeta.setAuthor(Legacy.SERIALIZER.serialize(component));
        this.setMeta((ItemMeta)bookMeta);
        return this;
    }

    @NotNull
    @Contract(value="_ -> this")
    public BookBuilder generation(@Nullable BookMeta.Generation generation) {
        BookMeta bookMeta = (BookMeta)this.getMeta();
        bookMeta.setGeneration(generation);
        this.setMeta((ItemMeta)bookMeta);
        return this;
    }

    @NotNull
    @Contract(value="_ -> this")
    public BookBuilder page(@NotNull Component ... componentArray) {
        return this.page(Arrays.asList(componentArray));
    }

    @NotNull
    @Contract(value="_ -> this")
    public BookBuilder page(@NotNull List<Component> list) {
        BookMeta bookMeta = (BookMeta)this.getMeta();
        for (Component component : list) {
            bookMeta.addPage(new String[]{Legacy.SERIALIZER.serialize(component)});
        }
        this.setMeta((ItemMeta)bookMeta);
        return this;
    }

    @NotNull
    @Contract(value="_, _ -> this")
    public BookBuilder page(int n, @NotNull Component component) {
        BookMeta bookMeta = (BookMeta)this.getMeta();
        bookMeta.setPage(n, Legacy.SERIALIZER.serialize(component));
        this.setMeta((ItemMeta)bookMeta);
        return this;
    }

    @NotNull
    @Contract(value="_ -> this")
    public BookBuilder title(@Nullable Component component) {
        BookMeta bookMeta = (BookMeta)this.getMeta();
        if (component == null) {
            bookMeta.setTitle(null);
            this.setMeta((ItemMeta)bookMeta);
            return this;
        }
        bookMeta.setTitle(Legacy.SERIALIZER.serialize(component));
        this.setMeta((ItemMeta)bookMeta);
        return this;
    }
}

