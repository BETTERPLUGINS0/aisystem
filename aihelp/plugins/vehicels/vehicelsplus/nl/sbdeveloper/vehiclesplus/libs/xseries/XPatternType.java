/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Registry
 *  org.bukkit.block.banner.PatternType
 */
package nl.sbdeveloper.vehiclesplus.libs.xseries;

import java.util.Collection;
import java.util.Optional;
import nl.sbdeveloper.vehiclesplus.libs.xseries.base.XModule;
import nl.sbdeveloper.vehiclesplus.libs.xseries.base.XRegistry;
import org.bukkit.Registry;
import org.bukkit.block.banner.PatternType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

public final class XPatternType
extends XModule<XPatternType, PatternType> {
    public static final XRegistry<XPatternType, PatternType> REGISTRY = new XRegistry<XPatternType, PatternType>(PatternType.class, XPatternType.class, () -> Registry.BANNER_PATTERN, XPatternType::new, XPatternType[]::new);
    public static final XPatternType BASE = XPatternType.std("base");
    public static final XPatternType SQUARE_BOTTOM_LEFT = XPatternType.std("square_bottom_left");
    public static final XPatternType SQUARE_BOTTOM_RIGHT = XPatternType.std("square_bottom_right");
    public static final XPatternType SQUARE_TOP_LEFT = XPatternType.std("square_top_left");
    public static final XPatternType SQUARE_TOP_RIGHT = XPatternType.std("square_top_right");
    public static final XPatternType STRIPE_BOTTOM = XPatternType.std("stripe_bottom");
    public static final XPatternType STRIPE_TOP = XPatternType.std("stripe_top");
    public static final XPatternType STRIPE_LEFT = XPatternType.std("stripe_left");
    public static final XPatternType STRIPE_RIGHT = XPatternType.std("stripe_right");
    public static final XPatternType STRIPE_CENTER = XPatternType.std("stripe_center");
    public static final XPatternType STRIPE_MIDDLE = XPatternType.std("stripe_middle");
    public static final XPatternType STRIPE_DOWNRIGHT = XPatternType.std("stripe_downright");
    public static final XPatternType STRIPE_DOWNLEFT = XPatternType.std("stripe_downleft");
    public static final XPatternType SMALL_STRIPES = XPatternType.std("small_stripes", "STRIPE_SMALL");
    public static final XPatternType CROSS = XPatternType.std("cross");
    public static final XPatternType STRAIGHT_CROSS = XPatternType.std("straight_cross");
    public static final XPatternType TRIANGLE_BOTTOM = XPatternType.std("triangle_bottom");
    public static final XPatternType TRIANGLE_TOP = XPatternType.std("triangle_top");
    public static final XPatternType TRIANGLES_BOTTOM = XPatternType.std("triangles_bottom");
    public static final XPatternType TRIANGLES_TOP = XPatternType.std("triangles_top");
    public static final XPatternType DIAGONAL_LEFT = XPatternType.std("diagonal_left");
    public static final XPatternType DIAGONAL_UP_RIGHT = XPatternType.std("diagonal_up_right", "DIAGONAL_RIGHT_MIRROR");
    public static final XPatternType DIAGONAL_UP_LEFT = XPatternType.std("diagonal_up_left", "DIAGONAL_LEFT_MIRROR");
    public static final XPatternType DIAGONAL_RIGHT = XPatternType.std("diagonal_right");
    public static final XPatternType CIRCLE = XPatternType.std("circle", "CIRCLE_MIDDLE");
    public static final XPatternType RHOMBUS = XPatternType.std("rhombus", "RHOMBUS_MIDDLE");
    public static final XPatternType HALF_VERTICAL = XPatternType.std("half_vertical");
    public static final XPatternType HALF_HORIZONTAL = XPatternType.std("half_horizontal");
    public static final XPatternType HALF_VERTICAL_RIGHT = XPatternType.std("half_vertical_right", "HALF_VERTICAL_MIRROR");
    public static final XPatternType HALF_HORIZONTAL_BOTTOM = XPatternType.std("half_horizontal_bottom", "HALF_HORIZONTAL_MIRROR");
    public static final XPatternType BORDER = XPatternType.std("border");
    public static final XPatternType CURLY_BORDER = XPatternType.std("curly_border");
    public static final XPatternType CREEPER = XPatternType.std("creeper");
    public static final XPatternType GRADIENT = XPatternType.std("gradient");
    public static final XPatternType GRADIENT_UP = XPatternType.std("gradient_up");
    public static final XPatternType BRICKS = XPatternType.std("bricks");
    public static final XPatternType SKULL = XPatternType.std("skull");
    public static final XPatternType FLOWER = XPatternType.std("flower");
    public static final XPatternType MOJANG = XPatternType.std("mojang");
    public static final XPatternType GLOBE = XPatternType.std("globe");
    public static final XPatternType PIGLIN = XPatternType.std("piglin");
    public static final XPatternType FLOW = XPatternType.std("flow");
    public static final XPatternType GUSTER = XPatternType.std("guster");

    private XPatternType(PatternType patternType, String[] stringArray) {
        super(patternType, stringArray);
    }

    public static XPatternType of(PatternType patternType) {
        return REGISTRY.getByBukkitForm(patternType);
    }

    public static Optional<XPatternType> of(String string) {
        return REGISTRY.getByName(string);
    }

    @NotNull
    public static @Unmodifiable Collection<XPatternType> getValues() {
        return REGISTRY.getValues();
    }

    private static XPatternType std(String ... stringArray) {
        return REGISTRY.std((XPatternType)stringArray);
    }
}

