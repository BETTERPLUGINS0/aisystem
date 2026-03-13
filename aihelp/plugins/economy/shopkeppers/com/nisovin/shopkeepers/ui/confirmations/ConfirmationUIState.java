package com.nisovin.shopkeepers.ui.confirmations;

import com.nisovin.shopkeepers.ui.lib.UIState;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.List;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ConfirmationUIState implements UIState {
   private final String title;
   @Nullable
   private final List<? extends String> confirmationLore;
   private final Runnable action;
   private final Runnable onCancelled;

   public ConfirmationUIState(String title, @Nullable List<? extends String> confirmationLore, Runnable action, Runnable onCancelled) {
      Validate.notEmpty(title, "title is empty");
      Validate.notNull(action, (String)"action is null");
      Validate.notNull(onCancelled, (String)"onCancelled is null");
      this.title = title;
      this.confirmationLore = confirmationLore;
      this.action = action;
      this.onCancelled = onCancelled;
   }

   public String getTitle() {
      return this.title;
   }

   @Nullable
   public List<? extends String> getConfirmationLore() {
      return this.confirmationLore;
   }

   public Runnable getAction() {
      return this.action;
   }

   public Runnable getOnCancelled() {
      return this.onCancelled;
   }
}
