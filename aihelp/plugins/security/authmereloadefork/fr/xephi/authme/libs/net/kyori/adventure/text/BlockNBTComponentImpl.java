package fr.xephi.authme.libs.net.kyori.adventure.text;

import fr.xephi.authme.libs.net.kyori.adventure.internal.Internals;
import fr.xephi.authme.libs.net.kyori.adventure.text.format.Style;
import fr.xephi.authme.libs.net.kyori.adventure.util.ShadyPines;
import fr.xephi.authme.libs.net.kyori.examination.ExaminableProperty;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class BlockNBTComponentImpl extends NBTComponentImpl<BlockNBTComponent, BlockNBTComponent.Builder> implements BlockNBTComponent {
   private final BlockNBTComponent.Pos pos;

   static BlockNBTComponent create(@NotNull final List<? extends ComponentLike> children, @NotNull final Style style, final String nbtPath, final boolean interpret, @Nullable final ComponentLike separator, @NotNull final BlockNBTComponent.Pos pos) {
      return new BlockNBTComponentImpl(ComponentLike.asComponents(children, IS_NOT_EMPTY), (Style)Objects.requireNonNull(style, "style"), (String)Objects.requireNonNull(nbtPath, "nbtPath"), interpret, ComponentLike.unbox(separator), (BlockNBTComponent.Pos)Objects.requireNonNull(pos, "pos"));
   }

   BlockNBTComponentImpl(@NotNull final List<Component> children, @NotNull final Style style, final String nbtPath, final boolean interpret, @Nullable final Component separator, @NotNull final BlockNBTComponent.Pos pos) {
      super(children, style, nbtPath, interpret, separator);
      this.pos = pos;
   }

   @NotNull
   public BlockNBTComponent nbtPath(@NotNull final String nbtPath) {
      return (BlockNBTComponent)(Objects.equals(this.nbtPath, nbtPath) ? this : create(this.children, this.style, nbtPath, this.interpret, this.separator, this.pos));
   }

   @NotNull
   public BlockNBTComponent interpret(final boolean interpret) {
      return (BlockNBTComponent)(this.interpret == interpret ? this : create(this.children, this.style, this.nbtPath, interpret, this.separator, this.pos));
   }

   @Nullable
   public Component separator() {
      return this.separator;
   }

   @NotNull
   public BlockNBTComponent separator(@Nullable final ComponentLike separator) {
      return create(this.children, this.style, this.nbtPath, this.interpret, separator, this.pos);
   }

   @NotNull
   public BlockNBTComponent.Pos pos() {
      return this.pos;
   }

   @NotNull
   public BlockNBTComponent pos(@NotNull final BlockNBTComponent.Pos pos) {
      return create(this.children, this.style, this.nbtPath, this.interpret, this.separator, pos);
   }

   @NotNull
   public BlockNBTComponent children(@NotNull final List<? extends ComponentLike> children) {
      return create(children, this.style, this.nbtPath, this.interpret, this.separator, this.pos);
   }

   @NotNull
   public BlockNBTComponent style(@NotNull final Style style) {
      return create(this.children, style, this.nbtPath, this.interpret, this.separator, this.pos);
   }

   public boolean equals(@Nullable final Object other) {
      if (this == other) {
         return true;
      } else if (!(other instanceof BlockNBTComponent)) {
         return false;
      } else if (!super.equals(other)) {
         return false;
      } else {
         BlockNBTComponent that = (BlockNBTComponent)other;
         return Objects.equals(this.pos, that.pos());
      }
   }

   public int hashCode() {
      int result = super.hashCode();
      result = 31 * result + this.pos.hashCode();
      return result;
   }

   public String toString() {
      return Internals.toString(this);
   }

   @NotNull
   public BlockNBTComponent.Builder toBuilder() {
      return new BlockNBTComponentImpl.BuilderImpl(this);
   }

   static final class Tokens {
      static final Pattern LOCAL_PATTERN = Pattern.compile("^\\^(-?\\d+(\\.\\d+)?) \\^(-?\\d+(\\.\\d+)?) \\^(-?\\d+(\\.\\d+)?)$");
      static final Pattern WORLD_PATTERN = Pattern.compile("^(~?)(-?\\d+) (~?)(-?\\d+) (~?)(-?\\d+)$");
      static final String LOCAL_SYMBOL = "^";
      static final String RELATIVE_SYMBOL = "~";
      static final String ABSOLUTE_SYMBOL = "";

      private Tokens() {
      }

      static BlockNBTComponent.WorldPos.Coordinate deserializeCoordinate(final String prefix, final String value) {
         int i = Integer.parseInt(value);
         if (prefix.equals("")) {
            return BlockNBTComponent.WorldPos.Coordinate.absolute(i);
         } else if (prefix.equals("~")) {
            return BlockNBTComponent.WorldPos.Coordinate.relative(i);
         } else {
            throw new AssertionError();
         }
      }

      static String serializeLocal(final double value) {
         return "^" + value;
      }

      static String serializeCoordinate(final BlockNBTComponent.WorldPos.Coordinate coordinate) {
         return (coordinate.type() == BlockNBTComponent.WorldPos.Coordinate.Type.RELATIVE ? "~" : "") + coordinate.value();
      }
   }

   static final class WorldPosImpl implements BlockNBTComponent.WorldPos {
      private final BlockNBTComponent.WorldPos.Coordinate x;
      private final BlockNBTComponent.WorldPos.Coordinate y;
      private final BlockNBTComponent.WorldPos.Coordinate z;

      WorldPosImpl(final BlockNBTComponent.WorldPos.Coordinate x, final BlockNBTComponent.WorldPos.Coordinate y, final BlockNBTComponent.WorldPos.Coordinate z) {
         this.x = (BlockNBTComponent.WorldPos.Coordinate)Objects.requireNonNull(x, "x");
         this.y = (BlockNBTComponent.WorldPos.Coordinate)Objects.requireNonNull(y, "y");
         this.z = (BlockNBTComponent.WorldPos.Coordinate)Objects.requireNonNull(z, "z");
      }

      @NotNull
      public BlockNBTComponent.WorldPos.Coordinate x() {
         return this.x;
      }

      @NotNull
      public BlockNBTComponent.WorldPos.Coordinate y() {
         return this.y;
      }

      @NotNull
      public BlockNBTComponent.WorldPos.Coordinate z() {
         return this.z;
      }

      @NotNull
      public Stream<? extends ExaminableProperty> examinableProperties() {
         return Stream.of(ExaminableProperty.of("x", (Object)this.x), ExaminableProperty.of("y", (Object)this.y), ExaminableProperty.of("z", (Object)this.z));
      }

      public boolean equals(@Nullable final Object other) {
         if (this == other) {
            return true;
         } else if (!(other instanceof BlockNBTComponent.WorldPos)) {
            return false;
         } else {
            BlockNBTComponent.WorldPos that = (BlockNBTComponent.WorldPos)other;
            return this.x.equals(that.x()) && this.y.equals(that.y()) && this.z.equals(that.z());
         }
      }

      public int hashCode() {
         int result = this.x.hashCode();
         result = 31 * result + this.y.hashCode();
         result = 31 * result + this.z.hashCode();
         return result;
      }

      public String toString() {
         return this.x.toString() + ' ' + this.y.toString() + ' ' + this.z.toString();
      }

      @NotNull
      public String asString() {
         return BlockNBTComponentImpl.Tokens.serializeCoordinate(this.x()) + ' ' + BlockNBTComponentImpl.Tokens.serializeCoordinate(this.y()) + ' ' + BlockNBTComponentImpl.Tokens.serializeCoordinate(this.z());
      }

      static final class CoordinateImpl implements BlockNBTComponent.WorldPos.Coordinate {
         private final int value;
         private final BlockNBTComponent.WorldPos.Coordinate.Type type;

         CoordinateImpl(final int value, @NotNull final BlockNBTComponent.WorldPos.Coordinate.Type type) {
            this.value = value;
            this.type = (BlockNBTComponent.WorldPos.Coordinate.Type)Objects.requireNonNull(type, "type");
         }

         public int value() {
            return this.value;
         }

         @NotNull
         public BlockNBTComponent.WorldPos.Coordinate.Type type() {
            return this.type;
         }

         @NotNull
         public Stream<? extends ExaminableProperty> examinableProperties() {
            return Stream.of(ExaminableProperty.of("value", this.value), ExaminableProperty.of("type", (Object)this.type));
         }

         public boolean equals(@Nullable final Object other) {
            if (this == other) {
               return true;
            } else if (!(other instanceof BlockNBTComponent.WorldPos.Coordinate)) {
               return false;
            } else {
               BlockNBTComponent.WorldPos.Coordinate that = (BlockNBTComponent.WorldPos.Coordinate)other;
               return this.value() == that.value() && this.type() == that.type();
            }
         }

         public int hashCode() {
            int result = this.value;
            result = 31 * result + this.type.hashCode();
            return result;
         }

         public String toString() {
            return (this.type == BlockNBTComponent.WorldPos.Coordinate.Type.RELATIVE ? "~" : "") + this.value;
         }
      }
   }

   static final class LocalPosImpl implements BlockNBTComponent.LocalPos {
      private final double left;
      private final double up;
      private final double forwards;

      LocalPosImpl(final double left, final double up, final double forwards) {
         this.left = left;
         this.up = up;
         this.forwards = forwards;
      }

      public double left() {
         return this.left;
      }

      public double up() {
         return this.up;
      }

      public double forwards() {
         return this.forwards;
      }

      @NotNull
      public Stream<? extends ExaminableProperty> examinableProperties() {
         return Stream.of(ExaminableProperty.of("left", this.left), ExaminableProperty.of("up", this.up), ExaminableProperty.of("forwards", this.forwards));
      }

      public boolean equals(@Nullable final Object other) {
         if (this == other) {
            return true;
         } else if (!(other instanceof BlockNBTComponent.LocalPos)) {
            return false;
         } else {
            BlockNBTComponent.LocalPos that = (BlockNBTComponent.LocalPos)other;
            return ShadyPines.equals(that.left(), this.left()) && ShadyPines.equals(that.up(), this.up()) && ShadyPines.equals(that.forwards(), this.forwards());
         }
      }

      public int hashCode() {
         int result = Double.hashCode(this.left);
         result = 31 * result + Double.hashCode(this.up);
         result = 31 * result + Double.hashCode(this.forwards);
         return result;
      }

      public String toString() {
         return String.format("^%f ^%f ^%f", this.left, this.up, this.forwards);
      }

      @NotNull
      public String asString() {
         return BlockNBTComponentImpl.Tokens.serializeLocal(this.left) + ' ' + BlockNBTComponentImpl.Tokens.serializeLocal(this.up) + ' ' + BlockNBTComponentImpl.Tokens.serializeLocal(this.forwards);
      }
   }

   static final class BuilderImpl extends AbstractNBTComponentBuilder<BlockNBTComponent, BlockNBTComponent.Builder> implements BlockNBTComponent.Builder {
      @Nullable
      private BlockNBTComponent.Pos pos;

      BuilderImpl() {
      }

      BuilderImpl(@NotNull final BlockNBTComponent component) {
         super(component);
         this.pos = component.pos();
      }

      @NotNull
      public BlockNBTComponent.Builder pos(@NotNull final BlockNBTComponent.Pos pos) {
         this.pos = (BlockNBTComponent.Pos)Objects.requireNonNull(pos, "pos");
         return this;
      }

      @NotNull
      public BlockNBTComponent build() {
         if (this.nbtPath == null) {
            throw new IllegalStateException("nbt path must be set");
         } else if (this.pos == null) {
            throw new IllegalStateException("pos must be set");
         } else {
            return BlockNBTComponentImpl.create(this.children, this.buildStyle(), this.nbtPath, this.interpret, this.separator, this.pos);
         }
      }
   }
}
