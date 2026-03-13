package github.nighter.smartspawner.libs.mariadb.plugin.authentication.standard.ed25519.math;

public interface ScalarOps {
   byte[] reduce(byte[] var1);

   byte[] multiplyAndAdd(byte[] var1, byte[] var2, byte[] var3);
}
