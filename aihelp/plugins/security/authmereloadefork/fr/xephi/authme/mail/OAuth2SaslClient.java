package fr.xephi.authme.mail;

import java.io.IOException;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.sasl.SaslClient;
import javax.security.sasl.SaslException;

class OAuth2SaslClient implements SaslClient {
   private final String oauthToken;
   private final CallbackHandler callbackHandler;
   private boolean isComplete = false;

   public OAuth2SaslClient(String oauthToken, CallbackHandler callbackHandler) {
      this.oauthToken = oauthToken;
      this.callbackHandler = callbackHandler;
   }

   public String getMechanismName() {
      return "XOAUTH2";
   }

   public boolean hasInitialResponse() {
      return true;
   }

   public byte[] evaluateChallenge(byte[] challenge) throws SaslException {
      if (this.isComplete) {
         return new byte[0];
      } else {
         NameCallback nameCallback = new NameCallback("Enter name");
         Callback[] callbacks = new Callback[]{nameCallback};

         try {
            this.callbackHandler.handle(callbacks);
         } catch (UnsupportedCallbackException var6) {
            throw new SaslException("Unsupported callback: " + var6);
         } catch (IOException var7) {
            throw new SaslException("Failed to execute callback: " + var7);
         }

         String email = nameCallback.getName();
         byte[] response = String.format("user=%s\u0001auth=Bearer %s\u0001\u0001", email, this.oauthToken).getBytes();
         this.isComplete = true;
         return response;
      }
   }

   public boolean isComplete() {
      return this.isComplete;
   }

   public byte[] unwrap(byte[] incoming, int offset, int len) throws SaslException {
      throw new IllegalStateException();
   }

   public byte[] wrap(byte[] outgoing, int offset, int len) throws SaslException {
      throw new IllegalStateException();
   }

   public Object getNegotiatedProperty(String propName) {
      if (!this.isComplete()) {
         throw new IllegalStateException();
      } else {
         return null;
      }
   }

   public void dispose() throws SaslException {
   }
}
