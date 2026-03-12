package fr.xephi.authme.security;

import fr.xephi.authme.security.crypts.Argon2;
import fr.xephi.authme.security.crypts.BCrypt;
import fr.xephi.authme.security.crypts.BCrypt2y;
import fr.xephi.authme.security.crypts.CmwCrypt;
import fr.xephi.authme.security.crypts.CrazyCrypt1;
import fr.xephi.authme.security.crypts.DoubleMd5;
import fr.xephi.authme.security.crypts.EncryptionMethod;
import fr.xephi.authme.security.crypts.Ipb3;
import fr.xephi.authme.security.crypts.Ipb4;
import fr.xephi.authme.security.crypts.Joomla;
import fr.xephi.authme.security.crypts.Md5;
import fr.xephi.authme.security.crypts.Md5vB;
import fr.xephi.authme.security.crypts.MyBB;
import fr.xephi.authme.security.crypts.NoCrypt;
import fr.xephi.authme.security.crypts.Pbkdf2;
import fr.xephi.authme.security.crypts.Pbkdf2Django;
import fr.xephi.authme.security.crypts.PhpBB;
import fr.xephi.authme.security.crypts.PhpFusion;
import fr.xephi.authme.security.crypts.RoyalAuth;
import fr.xephi.authme.security.crypts.Salted2Md5;
import fr.xephi.authme.security.crypts.SaltedSha512;
import fr.xephi.authme.security.crypts.Sha1;
import fr.xephi.authme.security.crypts.Sha256;
import fr.xephi.authme.security.crypts.Sha512;
import fr.xephi.authme.security.crypts.Smf;
import fr.xephi.authme.security.crypts.TwoFactor;
import fr.xephi.authme.security.crypts.Wbb3;
import fr.xephi.authme.security.crypts.Wbb4;
import fr.xephi.authme.security.crypts.Whirlpool;
import fr.xephi.authme.security.crypts.Wordpress;
import fr.xephi.authme.security.crypts.XAuth;
import fr.xephi.authme.security.crypts.XfBCrypt;

public enum HashAlgorithm {
   ARGON2(Argon2.class),
   BCRYPT(BCrypt.class),
   BCRYPT2Y(BCrypt2y.class),
   CMW(CmwCrypt.class),
   CRAZYCRYPT1(CrazyCrypt1.class),
   IPB3(Ipb3.class),
   IPB4(Ipb4.class),
   JOOMLA(Joomla.class),
   MD5VB(Md5vB.class),
   MYBB(MyBB.class),
   PBKDF2(Pbkdf2.class),
   PBKDF2DJANGO(Pbkdf2Django.class),
   PHPBB(PhpBB.class),
   PHPFUSION(PhpFusion.class),
   ROYALAUTH(RoyalAuth.class),
   SALTED2MD5(Salted2Md5.class),
   SALTEDSHA512(SaltedSha512.class),
   SHA256(Sha256.class),
   SMF(Smf.class),
   TWO_FACTOR(TwoFactor.class),
   WBB3(Wbb3.class),
   WBB4(Wbb4.class),
   WORDPRESS(Wordpress.class),
   XAUTH(XAuth.class),
   XFBCRYPT(XfBCrypt.class),
   NOCRYPT(NoCrypt.class),
   CUSTOM((Class)null),
   /** @deprecated */
   @Deprecated
   DOUBLEMD5(DoubleMd5.class),
   /** @deprecated */
   @Deprecated
   MD5(Md5.class),
   /** @deprecated */
   @Deprecated
   PLAINTEXT((Class)null),
   /** @deprecated */
   @Deprecated
   SHA1(Sha1.class),
   /** @deprecated */
   @Deprecated
   SHA512(Sha512.class),
   /** @deprecated */
   @Deprecated
   WHIRLPOOL(Whirlpool.class);

   private final Class<? extends EncryptionMethod> clazz;

   private HashAlgorithm(Class<? extends EncryptionMethod> param3) {
      this.clazz = clazz;
   }

   public Class<? extends EncryptionMethod> getClazz() {
      return this.clazz;
   }

   // $FF: synthetic method
   private static HashAlgorithm[] $values() {
      return new HashAlgorithm[]{ARGON2, BCRYPT, BCRYPT2Y, CMW, CRAZYCRYPT1, IPB3, IPB4, JOOMLA, MD5VB, MYBB, PBKDF2, PBKDF2DJANGO, PHPBB, PHPFUSION, ROYALAUTH, SALTED2MD5, SALTEDSHA512, SHA256, SMF, TWO_FACTOR, WBB3, WBB4, WORDPRESS, XAUTH, XFBCRYPT, NOCRYPT, CUSTOM, DOUBLEMD5, MD5, PLAINTEXT, SHA1, SHA512, WHIRLPOOL};
   }
}
