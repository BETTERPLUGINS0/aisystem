package fr.xephi.authme.process.register;

public enum RegisterSecondaryArgument {
   NONE,
   CONFIRMATION,
   EMAIL_MANDATORY,
   EMAIL_OPTIONAL;

   // $FF: synthetic method
   private static RegisterSecondaryArgument[] $values() {
      return new RegisterSecondaryArgument[]{NONE, CONFIRMATION, EMAIL_MANDATORY, EMAIL_OPTIONAL};
   }
}
