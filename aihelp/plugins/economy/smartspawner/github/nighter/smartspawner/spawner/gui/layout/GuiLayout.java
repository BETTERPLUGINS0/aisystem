package github.nighter.smartspawner.spawner.gui.layout;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class GuiLayout {
   private final Map<String, GuiButton> buttons = new HashMap();
   private final Map<Integer, String> slotToButtonType = new HashMap();

   public void addButton(String buttonType, GuiButton button) {
      GuiButton oldButton = (GuiButton)this.buttons.get(buttonType);
      if (oldButton != null) {
         this.slotToButtonType.remove(oldButton.getSlot());
      }

      this.buttons.put(buttonType, button);
      this.slotToButtonType.put(button.getSlot(), buttonType);
   }

   public GuiButton getButton(String buttonType) {
      return (GuiButton)this.buttons.get(buttonType);
   }

   public Optional<String> getButtonTypeAtSlot(int slot) {
      return Optional.ofNullable((String)this.slotToButtonType.get(slot));
   }

   public Optional<GuiButton> getButtonAtSlot(int slot) {
      String buttonType = (String)this.slotToButtonType.get(slot);
      return buttonType != null ? Optional.ofNullable((GuiButton)this.buttons.get(buttonType)) : Optional.empty();
   }

   public boolean hasButton(String buttonType) {
      return this.buttons.containsKey(buttonType) && ((GuiButton)this.buttons.get(buttonType)).isEnabled();
   }

   public Set<String> getButtonTypes() {
      return this.buttons.keySet();
   }

   public Map<String, GuiButton> getAllButtons() {
      return new HashMap(this.buttons);
   }

   public Set<Integer> getUsedSlots() {
      return this.slotToButtonType.keySet();
   }

   public boolean isSlotUsed(int slot) {
      return this.slotToButtonType.containsKey(slot);
   }
}
