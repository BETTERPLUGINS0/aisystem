package ac.grim.grimac.shaded.kyori.adventure.text;

import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;

public interface NBTComponent<C extends NBTComponent<C, B>, B extends NBTComponentBuilder<C, B>> extends BuildableComponent<C, B> {
   @NotNull
   String nbtPath();

   @Contract(
      pure = true
   )
   @NotNull
   C nbtPath(@NotNull final String nbtPath);

   boolean interpret();

   @Contract(
      pure = true
   )
   @NotNull
   C interpret(final boolean interpret);

   @Nullable
   Component separator();

   @NotNull
   C separator(@Nullable final ComponentLike separator);

   @NotNull
   default Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.concat(Stream.of(ExaminableProperty.of("nbtPath", this.nbtPath()), ExaminableProperty.of("interpret", this.interpret()), ExaminableProperty.of("separator", (Object)this.separator())), BuildableComponent.super.examinableProperties());
   }
}
