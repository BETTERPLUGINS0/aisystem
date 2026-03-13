/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.magmaguy.betterstructures.modules;

import com.magmaguy.magmacore.util.Logger;
import javax.annotation.Nullable;
import org.joml.Vector3i;

public enum Direction {
    NORTH,
    SOUTH,
    EAST,
    WEST,
    UP,
    DOWN;


    @Nullable
    public static Direction fromString(String s) {
        for (Direction border : Direction.values()) {
            if (!border.name().equalsIgnoreCase(s)) continue;
            return border;
        }
        return null;
    }

    public Vector3i getOffsetVector() {
        return switch (this.ordinal()) {
            case 0 -> new Vector3i(0, 0, -1);
            case 1 -> new Vector3i(0, 0, 1);
            case 2 -> new Vector3i(1, 0, 0);
            case 3 -> new Vector3i(-1, 0, 0);
            case 4 -> new Vector3i(0, 1, 0);
            case 5 -> new Vector3i(0, -1, 0);
            default -> throw new IllegalArgumentException("Invalid BuildBorder");
        };
    }

    public Direction getOpposite() {
        return switch (this.ordinal()) {
            case 0 -> SOUTH;
            case 1 -> NORTH;
            case 2 -> WEST;
            case 3 -> EAST;
            case 4 -> DOWN;
            case 5 -> UP;
            default -> throw new IllegalArgumentException("Invalid BuildBorder");
        };
    }

    public static Direction transformDirection(Direction direction, Integer rotation) {
        if (rotation == null || rotation == 0) {
            return direction;
        }
        rotation = (rotation % 360 + 360) % 360;
        return switch (rotation) {
            case 90 -> {
                switch (direction.ordinal()) {
                    case 0: {
                        yield EAST;
                    }
                    case 2: {
                        yield SOUTH;
                    }
                    case 1: {
                        yield WEST;
                    }
                    case 3: {
                        yield NORTH;
                    }
                }
                yield direction;
            }
            case 180 -> {
                switch (direction.ordinal()) {
                    case 0: {
                        yield SOUTH;
                    }
                    case 2: {
                        yield WEST;
                    }
                    case 1: {
                        yield NORTH;
                    }
                    case 3: {
                        yield EAST;
                    }
                }
                yield direction;
            }
            case 270 -> {
                switch (direction.ordinal()) {
                    case 0: {
                        yield WEST;
                    }
                    case 2: {
                        yield NORTH;
                    }
                    case 1: {
                        yield EAST;
                    }
                    case 3: {
                        yield SOUTH;
                    }
                }
                yield direction;
            }
            default -> {
                Logger.warn("Invalid rotation detected! " + rotation);
                yield direction;
            }
        };
    }
}

