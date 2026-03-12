package fr.xephi.authme.command;

public enum FoundResultStatus {
   SUCCESS,
   INCORRECT_ARGUMENTS,
   UNKNOWN_LABEL,
   NO_PERMISSION,
   MISSING_BASE_COMMAND;

   // $FF: synthetic method
   private static FoundResultStatus[] $values() {
      return new FoundResultStatus[]{SUCCESS, INCORRECT_ARGUMENTS, UNKNOWN_LABEL, NO_PERMISSION, MISSING_BASE_COMMAND};
   }
}
