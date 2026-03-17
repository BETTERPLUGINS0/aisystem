/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.DyeColor
 *  org.bukkit.Material
 *  org.bukkit.Tag
 *  org.bukkit.block.banner.Pattern
 *  org.bukkit.block.banner.PatternType
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.BannerMeta
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.jetbrains.annotations.Contract
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.gui.builder.item;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import me.zombie_striker.qav.gui.builder.item.BaseItemBuilder;
import me.zombie_striker.qav.gui.components.exception.GuiException;
import me.zombie_striker.qav.gui.components.util.VersionHelper;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class BannerBuilder
extends BaseItemBuilder<BannerBuilder> {
    private static final Material DEFAULT_BANNER;
    private static final EnumSet<Material> BANNERS;

    BannerBuilder() {
        super(new ItemStack(DEFAULT_BANNER));
    }

    BannerBuilder(@NotNull ItemStack itemStack) {
        super(itemStack);
        if (!BANNERS.contains(itemStack.getType())) {
            throw new GuiException("BannerBuilder requires the material to be a banner!");
        }
    }

    @NotNull
    @Contract(value="_ -> this")
    public BannerBuilder baseColor(@NotNull DyeColor dyeColor) {
        BannerMeta bannerMeta = (BannerMeta)this.getMeta();
        bannerMeta.setBaseColor(dyeColor);
        this.setMeta((ItemMeta)bannerMeta);
        return this;
    }

    @NotNull
    @Contract(value="_, _ -> this")
    public BannerBuilder pattern(@NotNull DyeColor dyeColor, @NotNull PatternType patternType) {
        BannerMeta bannerMeta = (BannerMeta)this.getMeta();
        bannerMeta.addPattern(new Pattern(dyeColor, patternType));
        this.setMeta((ItemMeta)bannerMeta);
        return this;
    }

    @NotNull
    @Contract(value="_ -> this")
    public BannerBuilder pattern(@NotNull Pattern ... patternArray) {
        return this.pattern(Arrays.asList(patternArray));
    }

    @NotNull
    @Contract(value="_ -> this")
    public BannerBuilder pattern(@NotNull List<Pattern> list) {
        BannerMeta bannerMeta = (BannerMeta)this.getMeta();
        for (Pattern pattern : list) {
            bannerMeta.addPattern(pattern);
        }
        this.setMeta((ItemMeta)bannerMeta);
        return this;
    }

    @NotNull
    @Contract(value="_, _, _ -> this")
    public BannerBuilder pattern(int n, @NotNull DyeColor dyeColor, @NotNull PatternType patternType) {
        return this.pattern(n, new Pattern(dyeColor, patternType));
    }

    @NotNull
    @Contract(value="_, _ -> this")
    public BannerBuilder pattern(int n, @NotNull Pattern pattern) {
        BannerMeta bannerMeta = (BannerMeta)this.getMeta();
        bannerMeta.setPattern(n, pattern);
        this.setMeta((ItemMeta)bannerMeta);
        return this;
    }

    @NotNull
    @Contract(value="_ -> this")
    public BannerBuilder setPatterns(@NotNull @NotNull List<@NotNull Pattern> list) {
        BannerMeta bannerMeta = (BannerMeta)this.getMeta();
        bannerMeta.setPatterns(list);
        this.setMeta((ItemMeta)bannerMeta);
        return this;
    }

    static {
        if (VersionHelper.IS_ITEM_LEGACY) {
            DEFAULT_BANNER = Material.valueOf((String)"BANNER");
            BANNERS = EnumSet.of(Material.valueOf((String)"BANNER"));
        } else {
            DEFAULT_BANNER = Material.WHITE_BANNER;
            BANNERS = EnumSet.copyOf(Tag.BANNERS.getValues());
        }
    }
}

