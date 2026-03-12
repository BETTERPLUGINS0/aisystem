package ac.grim.grimac.shaded.kyori.adventure.text;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.text.object.ObjectContents;
import ac.grim.grimac.shaded.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;

public interface ObjectComponent extends BuildableComponent<ObjectComponent, ObjectComponent.Builder>, ScopedComponent<ObjectComponent> {
   @NotNull
   ObjectContents contents();

   @NotNull
   ObjectComponent contents(@NotNull ObjectContents contents);

   @NotNull
   default Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.concat(Stream.of(ExaminableProperty.of("contents", (Object)this.contents())), BuildableComponent.super.examinableProperties());
   }

   public interface Builder extends ComponentBuilder<ObjectComponent, ObjectComponent.Builder> {
      @NotNull
      ObjectComponent.Builder contents(@NotNull ObjectContents objectContents);
   }
}
