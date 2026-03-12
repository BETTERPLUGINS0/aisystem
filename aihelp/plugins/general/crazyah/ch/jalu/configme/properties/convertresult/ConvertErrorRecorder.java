package ch.jalu.configme.properties.convertresult;

import org.jetbrains.annotations.NotNull;

public class ConvertErrorRecorder {
   private boolean hasError;

   public void setHasError(@NotNull String reason) {
      this.hasError = true;
   }

   public boolean isFullyValid() {
      return !this.hasError;
   }
}
