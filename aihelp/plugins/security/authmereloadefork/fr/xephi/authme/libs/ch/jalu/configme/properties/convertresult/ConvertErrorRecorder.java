package fr.xephi.authme.libs.ch.jalu.configme.properties.convertresult;

public class ConvertErrorRecorder {
   private boolean hasError;

   public void setHasError(String reason) {
      this.hasError = true;
   }

   public boolean isFullyValid() {
      return !this.hasError;
   }
}
