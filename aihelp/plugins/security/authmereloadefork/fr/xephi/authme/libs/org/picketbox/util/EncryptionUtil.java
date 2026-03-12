package fr.xephi.authme.libs.org.picketbox.util;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionUtil {
   private String encryptionAlgorithm;
   private int keySize;

   public EncryptionUtil(String encryptionAlgorithm, int keySize) {
      this.encryptionAlgorithm = encryptionAlgorithm;
      this.keySize = keySize;
   }

   public SecretKey generateKey() throws NoSuchAlgorithmException {
      KeyGenerator kgen = KeyGenerator.getInstance(this.encryptionAlgorithm);
      kgen.init(this.keySize);
      SecretKey key = kgen.generateKey();
      return key;
   }

   public byte[] encrypt(byte[] data, PublicKey publicKey, SecretKey key) throws Exception {
      KeyGenerator kgen = KeyGenerator.getInstance(this.encryptionAlgorithm);
      kgen.init(this.keySize);
      byte[] publicKeyEncoded = publicKey.getEncoded();
      SecretKeySpec skeySpec = new SecretKeySpec(key.getEncoded(), this.encryptionAlgorithm);
      Cipher cipher = Cipher.getInstance(this.encryptionAlgorithm);
      cipher.init(1, skeySpec);
      byte[] encrypted = cipher.doFinal(data);
      return encrypted;
   }

   public byte[] decrypt(byte[] encryptedData, KeyPair keypair, SecretKeySpec keySpec) throws Exception {
      KeyGenerator kgen = KeyGenerator.getInstance(this.encryptionAlgorithm);
      kgen.init(this.keySize);
      byte[] publicKeyEncoded = keypair.getPrivate().getEncoded();
      Cipher cipher = Cipher.getInstance(this.encryptionAlgorithm);
      cipher.init(2, keySpec);
      byte[] original = cipher.doFinal(encryptedData);
      return original;
   }

   public byte[] decrypt(byte[] encryptedData, KeyPair keypair, SecretKey key) throws Exception {
      KeyGenerator kgen = KeyGenerator.getInstance(this.encryptionAlgorithm);
      kgen.init(this.keySize);
      byte[] publicKeyEncoded = keypair.getPrivate().getEncoded();
      SecretKeySpec skeySpec = new SecretKeySpec(key.getEncoded(), this.encryptionAlgorithm);
      Cipher cipher = Cipher.getInstance(this.encryptionAlgorithm);
      cipher.init(2, skeySpec);
      byte[] original = cipher.doFinal(encryptedData);
      return original;
   }

   public byte[] encrypt(byte[] data, SecretKey key) throws Exception {
      SecretKeySpec skeySpec = new SecretKeySpec(key.getEncoded(), this.encryptionAlgorithm);
      Cipher cipher = Cipher.getInstance(this.encryptionAlgorithm);
      cipher.init(1, skeySpec);
      byte[] encrypted = cipher.doFinal(data);
      return encrypted;
   }

   public byte[] decrypt(byte[] encryptedData, SecretKeySpec keySpec) throws Exception {
      Cipher cipher = Cipher.getInstance(this.encryptionAlgorithm);
      cipher.init(2, keySpec);
      byte[] original = cipher.doFinal(encryptedData);
      return original;
   }
}
