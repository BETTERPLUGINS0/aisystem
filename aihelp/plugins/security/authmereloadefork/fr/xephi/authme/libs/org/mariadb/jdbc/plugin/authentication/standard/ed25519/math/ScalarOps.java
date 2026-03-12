package fr.xephi.authme.libs.org.mariadb.jdbc.plugin.authentication.standard.ed25519.math;

public interface ScalarOps {
   byte[] reduce(byte[] var1);

   byte[] multiplyAndAdd(byte[] var1, byte[] var2, byte[] var3);
}
