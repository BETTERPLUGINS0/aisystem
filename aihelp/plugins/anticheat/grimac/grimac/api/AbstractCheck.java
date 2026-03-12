package ac.grim.grimac.api;

import ac.grim.grimac.api.common.BasicStatus;

public interface AbstractCheck extends AbstractProcessor, BasicStatus {
   String getCheckName();

   default String getAlternativeName() {
      return this.getCheckName();
   }

   default String getDescription() {
      return "No description provided";
   }

   double getViolations();

   long getLastViolationTime();

   double getDecay();

   double getSetbackVL();

   boolean isExperimental();
}
