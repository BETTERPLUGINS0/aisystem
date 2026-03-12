package ac.grim.grimac.api.common;

public interface BasicStatus {
   boolean isEnabled();

   void setEnabled(boolean var1);

   default void toggle() {
      this.setEnabled(!this.isEnabled());
   }

   default boolean isDisabled() {
      return !this.isEnabled();
   }
}
