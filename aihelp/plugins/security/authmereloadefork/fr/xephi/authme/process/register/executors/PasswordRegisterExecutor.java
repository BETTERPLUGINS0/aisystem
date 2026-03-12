package fr.xephi.authme.process.register.executors;

import fr.xephi.authme.data.auth.PlayerAuth;

class PasswordRegisterExecutor extends AbstractPasswordRegisterExecutor<PasswordRegisterParams> {
   public synchronized PlayerAuth createPlayerAuthObject(PasswordRegisterParams params) {
      return PlayerAuthBuilderHelper.createPlayerAuth(params.getPlayer(), params.getHashedPassword(), params.getEmail());
   }
}
