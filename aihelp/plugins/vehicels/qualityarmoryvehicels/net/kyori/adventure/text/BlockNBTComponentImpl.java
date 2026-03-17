/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.kyori.adventure.text;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import net.kyori.adventure.internal.Internals;
import net.kyori.adventure.text.AbstractNBTComponentBuilder;
import net.kyori.adventure.text.BlockNBTComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.NBTComponentImpl;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.util.ShadyPines;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class BlockNBTComponentImpl
extends NBTComponentImpl<BlockNBTComponent, BlockNBTComponent.Builder>
implements BlockNBTComponent {
    private final BlockNBTComponent.Pos pos;

    static BlockNBTComponent create(@NotNull List<? extends ComponentLike> list, @NotNull Style style, String string, boolean bl, @Nullable ComponentLike componentLike, @NotNull BlockNBTComponent.Pos pos) {
        return new BlockNBTComponentImpl(ComponentLike.asComponents(list, IS_NOT_EMPTY), Objects.requireNonNull(style, "style"), Objects.requireNonNull(string, "nbtPath"), bl, ComponentLike.unbox(componentLike), Objects.requireNonNull(pos, "pos"));
    }

    BlockNBTComponentImpl(@NotNull List<Component> list, @NotNull Style style, String string, boolean bl, @Nullable Component component, @NotNull BlockNBTComponent.Pos pos) {
        super(list, style, string, bl, component);
        this.pos = pos;
    }

    @Override
    @NotNull
    public BlockNBTComponent nbtPath(@NotNull String string) {
        if (Objects.equals(this.nbtPath, string)) {
            return this;
        }
        return BlockNBTComponentImpl.create(this.children, this.style, string, this.interpret, this.separator, this.pos);
    }

    @Override
    @NotNull
    public BlockNBTComponent interpret(boolean bl) {
        if (this.interpret == bl) {
            return this;
        }
        return BlockNBTComponentImpl.create(this.children, this.style, this.nbtPath, bl, this.separator, this.pos);
    }

    @Override
    @Nullable
    public Component separator() {
        return this.separator;
    }

    @Override
    @NotNull
    public BlockNBTComponent separator(@Nullable ComponentLike componentLike) {
        return BlockNBTComponentImpl.create(this.children, this.style, this.nbtPath, this.interpret, componentLike, this.pos);
    }

    @Override
    @NotNull
    public BlockNBTComponent.Pos pos() {
        return this.pos;
    }

    @Override
    @NotNull
    public BlockNBTComponent pos(@NotNull BlockNBTComponent.Pos pos) {
        return BlockNBTComponentImpl.create(this.children, this.style, this.nbtPath, this.interpret, this.separator, pos);
    }

    @Override
    @NotNull
    public BlockNBTComponent children(@NotNull List<? extends ComponentLike> list) {
        return BlockNBTComponentImpl.create(list, this.style, this.nbtPath, this.interpret, this.separator, this.pos);
    }

    @Override
    @NotNull
    public BlockNBTComponent style(@NotNull Style style) {
        return BlockNBTComponentImpl.create(this.children, style, this.nbtPath, this.interpret, this.separator, this.pos);
    }

    @Override
    public boolean equals(@Nullable Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof BlockNBTComponent)) {
            return false;
        }
        if (!super.equals(object)) {
            return false;
        }
        BlockNBTComponent blockNBTComponent = (BlockNBTComponent)object;
        return Objects.equals(this.pos, blockNBTComponent.pos());
    }

    @Override
    public int hashCode() {
        int n = super.hashCode();
        n = 31 * n + this.pos.hashCode();
        return n;
    }

    @Override
    public String toString() {
        return Internals.toString(this);
    }

    @Override
    public @NotNull BlockNBTComponent.Builder toBuilder() {
        return new BuilderImpl(this);
    }

    static final class Tokens {
        static final Pattern LOCAL_PATTERN = Pattern.compile("^\\^(-?\\d+(\\.\\d+)?) \\^(-?\\d+(\\.\\d+)?) \\^(-?\\d+(\\.\\d+)?)$");
        static final Pattern WORLD_PATTERN = Pattern.compile("^(~?)(-?\\d+) (~?)(-?\\d+) (~?)(-?\\d+)$");
        static final String LOCAL_SYMBOL = "^";
        static final String RELATIVE_SYMBOL = "~";
        static final String ABSOLUTE_SYMBOL = "";

        private Tokens() {
        }

        static BlockNBTComponent.WorldPos.Coordinate deserializeCoordinate(String string, String string2) {
            int n = Integer.parseInt(string2);
            if (string.equals(ABSOLUTE_SYMBOL)) {
                return BlockNBTComponent.WorldPos.Coordinate.absolute(n);
            }
            if (string.equals(RELATIVE_SYMBOL)) {
                return BlockNBTComponent.WorldPos.Coordinate.relative(n);
            }
            throw new AssertionError();
        }

        static String serializeLocal(double d) {
            return LOCAL_SYMBOL + d;
        }

        static String serializeCoordinate(BlockNBTComponent.WorldPos.Coordinate coordinate) {
            return (coordinate.type() == BlockNBTComponent.WorldPos.Coordinate.Type.RELATIVE ? RELATIVE_SYMBOL : ABSOLUTE_SYMBOL) + coordinate.value();
        }
    }

    static final class WorldPosImpl
    implements BlockNBTComponent.WorldPos {
        private final BlockNBTComponent.WorldPos.Coordinate x;
        private final BlockNBTComponent.WorldPos.Coordinate y;
        private final BlockNBTComponent.WorldPos.Coordinate z;

        WorldPosImpl(BlockNBTComponent.WorldPos.Coordinate coordinate, BlockNBTComponent.WorldPos.Coordinate coordinate2, BlockNBTComponent.WorldPos.Coordinate coordinate3) {
            this.x = Objects.requireNonNull(coordinate, "x");
            this.y = Objects.requireNonNull(coordinate2, "y");
            this.z = Objects.requireNonNull(coordinate3, "z");
        }

        @Override
        @NotNull
        public BlockNBTComponent.WorldPos.Coordinate x() {
            return this.x;
        }

        @Override
        @NotNull
        public BlockNBTComponent.WorldPos.Coordinate y() {
            return this.y;
        }

        @Override
        @NotNull
        public BlockNBTComponent.WorldPos.Coordinate z() {
            return this.z;
        }

        @Override
        @NotNull
        public Stream<? extends ExaminableProperty> examinableProperties() {
            return Stream.of(ExaminableProperty.of("x", this.x), ExaminableProperty.of("y", this.y), ExaminableProperty.of("z", this.z));
        }

        public boolean equals(@Nullable Object object) {
            if (this == object) {
                return true;
            }
            if (!(object instanceof BlockNBTComponent.WorldPos)) {
                return false;
            }
            BlockNBTComponent.WorldPos worldPos = (BlockNBTComponent.WorldPos)object;
            return this.x.equals(worldPos.x()) && this.y.equals(worldPos.y()) && this.z.equals(worldPos.z());
        }

        public int hashCode() {
            int n = this.x.hashCode();
            n = 31 * n + this.y.hashCode();
            n = 31 * n + this.z.hashCode();
            return n;
        }

        public String toString() {
            return this.x.toString() + ' ' + this.y.toString() + ' ' + this.z.toString();
        }

        @Override
        @NotNull
        public String asString() {
            return Tokens.serializeCoordinate(this.x()) + ' ' + Tokens.serializeCoordinate(this.y()) + ' ' + Tokens.serializeCoordinate(this.z());
        }

        static final class CoordinateImpl
        implements BlockNBTComponent.WorldPos.Coordinate {
            private final int value;
            private final BlockNBTComponent.WorldPos.Coordinate.Type type;

            CoordinateImpl(int n, @NotNull BlockNBTComponent.WorldPos.Coordinate.Type type) {
                this.value = n;
                this.type = Objects.requireNonNull(type, "type");
            }

            @Override
            public int value() {
                return this.value;
            }

            @Override
            @NotNull
            public BlockNBTComponent.WorldPos.Coordinate.Type type() {
                return this.type;
            }

            @Override
            @NotNull
            public Stream<? extends ExaminableProperty> examinableProperties() {
                return Stream.of(ExaminableProperty.of("value", this.value), ExaminableProperty.of("type", (Object)this.type));
            }

            public boolean equals(@Nullable Object object) {
                if (this == object) {
                    return true;
                }
                if (!(object instanceof BlockNBTComponent.WorldPos.Coordinate)) {
                    return false;
                }
                BlockNBTComponent.WorldPos.Coordinate coordinate = (BlockNBTComponent.WorldPos.Coordinate)object;
                return this.value() == coordinate.value() && this.type() == coordinate.type();
            }

            public int hashCode() {
                int n = this.value;
                n = 31 * n + this.type.hashCode();
                return n;
            }

            public String toString() {
                return (this.type == BlockNBTComponent.WorldPos.Coordinate.Type.RELATIVE ? "~" : "") + this.value;
            }
        }
    }

    static final class LocalPosImpl
    implements BlockNBTComponent.LocalPos {
        private final double left;
        private final double up;
        private final double forwards;

        LocalPosImpl(double d, double d2, double d3) {
            this.left = d;
            this.up = d2;
            this.forwards = d3;
        }

        @Override
        public double left() {
            return this.left;
        }

        @Override
        public double up() {
            return this.up;
        }

        @Override
        public double forwards() {
            return this.forwards;
        }

        @Override
        @NotNull
        public Stream<? extends ExaminableProperty> examinableProperties() {
            return Stream.of(ExaminableProperty.of("left", this.left), ExaminableProperty.of("up", this.up), ExaminableProperty.of("forwards", this.forwards));
        }

        public boolean equals(@Nullable Object object) {
            if (this == object) {
                return true;
            }
            if (!(object instanceof BlockNBTComponent.LocalPos)) {
                return false;
            }
            BlockNBTComponent.LocalPos localPos = (BlockNBTComponent.LocalPos)object;
            return ShadyPines.equals(localPos.left(), this.left()) && ShadyPines.equals(localPos.up(), this.up()) && ShadyPines.equals(localPos.forwards(), this.forwards());
        }

        public int hashCode() {
            int n = Double.hashCode(this.left);
            n = 31 * n + Double.hashCode(this.up);
            n = 31 * n + Double.hashCode(this.forwards);
            return n;
        }

        public String toString() {
            return String.format("^%f ^%f ^%f", this.left, this.up, this.forwards);
        }

        @Override
        @NotNull
        public String asString() {
            return Tokens.serializeLocal(this.left) + ' ' + Tokens.serializeLocal(this.up) + ' ' + Tokens.serializeLocal(this.forwards);
        }
    }

    static final class BuilderImpl
    extends AbstractNBTComponentBuilder<BlockNBTComponent, BlockNBTComponent.Builder>
    implements BlockNBTComponent.Builder {
        @Nullable
        private BlockNBTComponent.Pos pos;

        BuilderImpl() {
        }

        BuilderImpl(@NotNull BlockNBTComponent blockNBTComponent) {
            super(blockNBTComponent);
            this.pos = blockNBTComponent.pos();
        }

        @Override
        public @NotNull BlockNBTComponent.Builder pos(@NotNull BlockNBTComponent.Pos pos) {
            this.pos = Objects.requireNonNull(pos, "pos");
            return this;
        }

        @Override
        @NotNull
        public BlockNBTComponent build() {
            if (this.nbtPath == null) {
                throw new IllegalStateException("nbt path must be set");
            }
            if (this.pos == null) {
                throw new IllegalStateException("pos must be set");
            }
            return BlockNBTComponentImpl.create(this.children, this.buildStyle(), this.nbtPath, this.interpret, this.separator, this.pos);
        }
    }
}

