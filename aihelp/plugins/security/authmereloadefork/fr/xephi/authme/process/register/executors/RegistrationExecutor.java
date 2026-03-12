package fr.xephi.authme.process.register.executors;

import fr.xephi.authme.data.auth.PlayerAuth;

public interface RegistrationExecutor<P extends RegistrationParameters> {
   boolean isRegistrationAdmitted(P var1);

   PlayerAuth buildPlayerAuth(P var1);

   void executePostPersistAction(P var1);
}
