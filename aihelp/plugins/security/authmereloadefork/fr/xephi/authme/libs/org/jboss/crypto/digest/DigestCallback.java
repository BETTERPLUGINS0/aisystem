package fr.xephi.authme.libs.org.jboss.crypto.digest;

import java.security.MessageDigest;
import java.util.Map;

public interface DigestCallback {
   void init(Map<String, Object> var1);

   void preDigest(MessageDigest var1);

   void postDigest(MessageDigest var1);
}
