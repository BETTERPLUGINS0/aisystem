package com.nisovin.shopkeepers.text;

import com.nisovin.shopkeepers.util.java.Validate;

public class ClickEventText extends TextBuilder {
   private final ClickEventText.Action action;
   private final String value;

   ClickEventText(ClickEventText.Action action, String value) {
      Validate.notNull(action, (String)"action is null");
      Validate.notNull(value, (String)"value is null");
      this.action = action;
      this.value = value;
   }

   public ClickEventText.Action getAction() {
      return this.action;
   }

   public String getValue() {
      return this.value;
   }

   public boolean isPlainText() {
      return false;
   }

   public Text copy() {
      ClickEventText copy = new ClickEventText(this.action, this.value);
      copy.copy(this, true);
      return copy.build();
   }

   protected void appendToStringFeatures(StringBuilder builder) {
      builder.append(", action=");
      builder.append(this.getAction());
      builder.append(", value=");
      builder.append(this.getValue());
      super.appendToStringFeatures(builder);
   }

   public static enum Action {
      OPEN_URL,
      OPEN_FILE,
      RUN_COMMAND,
      SUGGEST_COMMAND,
      CHANGE_PAGE;

      // $FF: synthetic method
      private static ClickEventText.Action[] $values() {
         return new ClickEventText.Action[]{OPEN_URL, OPEN_FILE, RUN_COMMAND, SUGGEST_COMMAND, CHANGE_PAGE};
      }
   }
}
