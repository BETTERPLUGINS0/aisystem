package github.nighter.smartspawner.spawner.gui.layout;

import java.util.Map;
import lombok.Generated;
import org.bukkit.Material;

public class GuiButton {
   private final String buttonType;
   private final int slot;
   private final Material material;
   private final boolean enabled;
   private final String condition;
   private final Map<String, String> actions;
   private final boolean infoButton;

   public GuiButton(String buttonType, int slot, Material material, boolean enabled, String condition, Map<String, String> actions, boolean infoButton) {
      this.buttonType = buttonType;
      this.slot = slot;
      this.material = material;
      this.enabled = enabled;
      this.condition = condition;
      this.actions = actions;
      this.infoButton = infoButton;
   }

   public String getAction(String clickType) {
      return this.actions != null ? (String)this.actions.get(clickType) : null;
   }

   public String getActionWithFallback(String clickType) {
      if (this.actions != null && !this.actions.isEmpty()) {
         String specificAction = (String)this.actions.get(clickType);
         return specificAction != null && !specificAction.isEmpty() ? specificAction : (String)this.actions.get("click");
      } else {
         return null;
      }
   }

   public String getDefaultAction() {
      if (this.actions != null && !this.actions.isEmpty()) {
         String clickAction = (String)this.actions.get("click");
         return clickAction != null && !clickAction.isEmpty() ? clickAction : (String)this.actions.get("default");
      } else {
         return null;
      }
   }

   public boolean hasCondition() {
      return this.condition != null && !this.condition.isEmpty();
   }

   @Generated
   public String getButtonType() {
      return this.buttonType;
   }

   @Generated
   public int getSlot() {
      return this.slot;
   }

   @Generated
   public Material getMaterial() {
      return this.material;
   }

   @Generated
   public boolean isEnabled() {
      return this.enabled;
   }

   @Generated
   public String getCondition() {
      return this.condition;
   }

   @Generated
   public Map<String, String> getActions() {
      return this.actions;
   }

   @Generated
   public boolean isInfoButton() {
      return this.infoButton;
   }
}
