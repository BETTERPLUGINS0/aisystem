package fr.xephi.authme.data.captcha;

import org.bukkit.entity.Player;

public interface CaptchaManager {
   boolean isCaptchaRequired(String var1);

   String getCaptchaCodeOrGenerateNew(String var1);

   boolean checkCode(Player var1, String var2);
}
