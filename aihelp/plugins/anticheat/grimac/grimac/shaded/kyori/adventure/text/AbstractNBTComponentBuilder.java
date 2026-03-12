package ac.grim.grimac.shaded.kyori.adventure.text;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.Objects;

abstract class AbstractNBTComponentBuilder<C extends NBTComponent<C, B>, B extends NBTComponentBuilder<C, B>> extends AbstractComponentBuilder<C, B> implements NBTComponentBuilder<C, B> {
   @Nullable
   protected String nbtPath;
   protected boolean interpret = false;
   @Nullable
   protected Component separator;

   AbstractNBTComponentBuilder() {
   }

   AbstractNBTComponentBuilder(@NotNull final C component) {
      super(component);
      this.nbtPath = component.nbtPath();
      this.interpret = component.interpret();
      this.separator = component.separator();
   }

   @NotNull
   public B nbtPath(@NotNull final String nbtPath) {
      this.nbtPath = (String)Objects.requireNonNull(nbtPath, "nbtPath");
      return this;
   }

   @NotNull
   public B interpret(final boolean interpret) {
      this.interpret = interpret;
      return this;
   }

   @NotNull
   public B separator(@Nullable final ComponentLike separator) {
      this.separator = ComponentLike.unbox(separator);
      return this;
   }
}
