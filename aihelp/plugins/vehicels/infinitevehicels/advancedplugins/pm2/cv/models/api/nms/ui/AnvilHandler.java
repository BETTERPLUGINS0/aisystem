package advancedplugins.pm2.cv.models.api.nms.ui;

import java.util.function.Function;
import java.util.function.Supplier;
import lombok.Generated;
import org.bukkit.entity.Player;

public interface AnvilHandler {
   void openAnvil(Player var1, Function<String, Boolean> var2, Supplier<Boolean> var3);

   void reopenAnvil(Player var1);

   void consumeConfirm(Player var1);

   void consumeClose(Player var1);

   void removeAnvil(Player var1);

   void closeAnvil(Player var1);

   boolean isListening(Player var1);

   AnvilHandler.Menu getAnvilMenu(Player var1);

   public static class Menu {
      private final int id;
      private final Function<String, Boolean> onInput;
      private final Supplier<Boolean> onClose;
      private String value;

      public boolean confirm() {
         return (Boolean)this.onInput.apply(this.value);
      }

      public boolean close() {
         return (Boolean)this.onClose.get();
      }

      @Generated
      public Menu(int var1, Function<String, Boolean> var2, Supplier<Boolean> var3) {
         this.id = var1;
         this.onInput = var2;
         this.onClose = var3;
      }

      @Generated
      public int getId() {
         return this.id;
      }

      @Generated
      public Function<String, Boolean> getOnInput() {
         return this.onInput;
      }

      @Generated
      public Supplier<Boolean> getOnClose() {
         return this.onClose;
      }

      @Generated
      public String getValue() {
         return this.value;
      }

      @Generated
      public void setValue(String var1) {
         this.value = var1;
      }
   }
}
