package fr.xephi.authme.process.register.executors;

import fr.xephi.authme.data.auth.PlayerAuth;

class ApiPasswordRegisterExecutor extends AbstractPasswordRegisterExecutor<ApiPasswordRegisterParams> {
   protected PlayerAuth createPlayerAuthObject(ApiPasswordRegisterParams params) {
      return PlayerAuthBuilderHelper.createPlayerAuth(params.getPlayer(), params.getHashedPassword(), (String)null);
   }

   protected boolean performLoginAfterRegister(ApiPasswordRegisterParams params) {
      return params.getLoginAfterRegister();
   }
}
