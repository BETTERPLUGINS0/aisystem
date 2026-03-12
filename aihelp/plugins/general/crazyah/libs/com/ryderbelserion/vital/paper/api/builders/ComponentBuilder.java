package libs.com.ryderbelserion.vital.paper.api.builders;

import libs.com.ryderbelserion.vital.common.util.AdvUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TextComponent.Builder;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.event.ClickEvent.Action;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ComponentBuilder {
   @NotNull
   private final Builder builder = Component.text();
   private final Player target;
   private String value;

   public ComponentBuilder(@NotNull Player target) {
      this.target = target;
   }

   @NotNull
   public final ComponentBuilder append(@NotNull Component component) {
      this.builder.append(component);
      return this;
   }

   @NotNull
   public final ComponentBuilder applyHover(@NotNull String text) {
      if (text.isEmpty()) {
         return this;
      } else {
         this.builder.hoverEvent(HoverEvent.showText(AdvUtil.parse(text)));
         return this;
      }
   }

   @NotNull
   public final ComponentBuilder applyClick(@Nullable Action action, @NotNull String text) {
      if (action != null && !text.isEmpty()) {
         this.builder.clickEvent(ClickEvent.clickEvent(action, text));
         return this;
      } else {
         return this;
      }
   }

   @NotNull
   public final TextComponent getComponent() {
      if (this.value.isEmpty()) {
         return Component.empty();
      } else {
         Component message = AdvUtil.parse(this.value);
         return (TextComponent)((Builder)this.builder.append(message)).build();
      }
   }

   public void send() {
      if (!this.getComponent().equals(Component.empty())) {
         this.target.sendMessage(this.getComponent());
      }
   }

   @NotNull
   public final Player getTarget() {
      return this.target;
   }

   public void applyValue(@NotNull String value) {
      if (!value.isEmpty()) {
         this.value = value;
      }
   }

   @NotNull
   public final String getValue() {
      return this.value;
   }
}
