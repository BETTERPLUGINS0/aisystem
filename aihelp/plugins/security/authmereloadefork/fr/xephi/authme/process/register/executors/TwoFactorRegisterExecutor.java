package fr.xephi.authme.process.register.executors;

import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.security.crypts.TwoFactor;
import fr.xephi.authme.service.CommonService;
import org.bukkit.Bukkit;

class TwoFactorRegisterExecutor extends AbstractPasswordRegisterExecutor<TwoFactorRegisterParams> {
   @Inject
   private CommonService commonService;

   public boolean isRegistrationAdmitted(TwoFactorRegisterParams params) {
      return true;
   }

   protected PlayerAuth createPlayerAuthObject(TwoFactorRegisterParams params) {
      return PlayerAuthBuilderHelper.createPlayerAuth(params.getPlayer(), params.getHashedPassword(), (String)null);
   }

   public void executePostPersistAction(TwoFactorRegisterParams params) {
      super.executePostPersistAction((AbstractPasswordRegisterParams)params);
      String hash = params.getHashedPassword().getHash();
      String qrCodeUrl = TwoFactor.getQrBarcodeUrl(params.getPlayerName(), Bukkit.getIp(), hash);
      this.commonService.send(params.getPlayer(), MessageKey.TWO_FACTOR_CREATE, hash, qrCodeUrl);
   }
}
