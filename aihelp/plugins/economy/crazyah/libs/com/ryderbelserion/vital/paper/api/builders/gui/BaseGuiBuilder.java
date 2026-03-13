package libs.com.ryderbelserion.vital.paper.api.builders.gui;

import java.util.EnumSet;
import java.util.Set;
import java.util.function.Consumer;
import libs.com.ryderbelserion.vital.paper.api.builders.gui.objects.components.InteractionComponent;
import libs.com.ryderbelserion.vital.paper.api.builders.gui.types.BaseGui;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BaseGuiBuilder<G extends BaseGui, B extends BaseGuiBuilder<G, B>> {
   private final EnumSet<InteractionComponent> components = EnumSet.noneOf(InteractionComponent.class);
   private String title = null;
   private int rows = 1;
   private Consumer<G> consumer;

   @NotNull
   public abstract G create();

   @NotNull
   public final B setRows(int rows) {
      this.rows = rows;
      return this;
   }

   @NotNull
   public final B setTitle(@NotNull String title) {
      this.title = title;
      return this;
   }

   public final B disableItemPlacement() {
      this.components.add(InteractionComponent.PREVENT_ITEM_PLACE);
      return this;
   }

   public final B disableItemTake() {
      this.components.add(InteractionComponent.PREVENT_ITEM_TAKE);
      return this;
   }

   public final B disableItemSwap() {
      this.components.add(InteractionComponent.PREVENT_ITEM_SWAP);
      return this;
   }

   public final B disableItemDrop() {
      this.components.add(InteractionComponent.PREVENT_ITEM_DROP);
      return this;
   }

   public final B disableInteractions() {
      this.components.addAll(InteractionComponent.VALUES);
      return this;
   }

   public final B enableInteractions() {
      this.components.clear();
      return this;
   }

   public final B enableItemPlacement() {
      this.components.remove(InteractionComponent.PREVENT_ITEM_PLACE);
      return this;
   }

   public final B enableItemTake() {
      this.components.remove(InteractionComponent.PREVENT_ITEM_TAKE);
      return this;
   }

   public final B enableItemSwap() {
      this.components.remove(InteractionComponent.PREVENT_ITEM_SWAP);
      return this;
   }

   public final B enableItemDrop() {
      this.components.remove(InteractionComponent.PREVENT_ITEM_DROP);
      return this;
   }

   @NotNull
   public final B apply(@NotNull Consumer<G> consumer) {
      this.consumer = consumer;
      return this;
   }

   @NotNull
   protected final Set<InteractionComponent> getInteractionComponents() {
      return this.components;
   }

   @Nullable
   protected final Consumer<G> getConsumer() {
      return this.consumer;
   }

   @NotNull
   protected final String getTitle() {
      return this.title;
   }

   protected final int getRows() {
      return this.rows;
   }
}
