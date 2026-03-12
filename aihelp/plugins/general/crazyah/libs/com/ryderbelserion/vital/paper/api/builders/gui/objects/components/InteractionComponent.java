package libs.com.ryderbelserion.vital.paper.api.builders.gui.objects.components;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public enum InteractionComponent {
   PREVENT_ITEM_PLACE,
   PREVENT_ITEM_TAKE,
   PREVENT_ITEM_SWAP,
   PREVENT_ITEM_DROP,
   PREVENT_OTHER_ACTIONS;

   public static final Set<InteractionComponent> VALUES = Collections.unmodifiableSet(EnumSet.allOf(InteractionComponent.class));

   // $FF: synthetic method
   private static InteractionComponent[] $values() {
      return new InteractionComponent[]{PREVENT_ITEM_PLACE, PREVENT_ITEM_TAKE, PREVENT_ITEM_SWAP, PREVENT_ITEM_DROP, PREVENT_OTHER_ACTIONS};
   }
}
