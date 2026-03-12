package fr.xephi.authme.libs.de.mkammerer.argon2.jna;

import com.sun.jna.Library;
import com.sun.jna.Native;

public interface Argon2Library extends Library {
   Argon2Library INSTANCE = (Argon2Library)Native.load("argon2", Argon2Library.class);
   int ARGON2_OK = 0;

   int argon2i_hash_encoded(JnaUint32 var1, JnaUint32 var2, JnaUint32 var3, byte[] var4, Size_t var5, byte[] var6, Size_t var7, Size_t var8, byte[] var9, Size_t var10);

   int argon2id_hash_encoded(JnaUint32 var1, JnaUint32 var2, JnaUint32 var3, byte[] var4, Size_t var5, byte[] var6, Size_t var7, Size_t var8, byte[] var9, Size_t var10);

   int argon2d_hash_encoded(JnaUint32 var1, JnaUint32 var2, JnaUint32 var3, byte[] var4, Size_t var5, byte[] var6, Size_t var7, Size_t var8, byte[] var9, Size_t var10);

   int argon2i_hash_raw(JnaUint32 var1, JnaUint32 var2, JnaUint32 var3, byte[] var4, Size_t var5, byte[] var6, Size_t var7, byte[] var8, Size_t var9);

   int argon2id_hash_raw(JnaUint32 var1, JnaUint32 var2, JnaUint32 var3, byte[] var4, Size_t var5, byte[] var6, Size_t var7, byte[] var8, Size_t var9);

   int argon2d_hash_raw(JnaUint32 var1, JnaUint32 var2, JnaUint32 var3, byte[] var4, Size_t var5, byte[] var6, Size_t var7, byte[] var8, Size_t var9);

   int argon2_hash(JnaUint32 var1, JnaUint32 var2, JnaUint32 var3, byte[] var4, Size_t var5, byte[] var6, Size_t var7, byte[] var8, Size_t var9, byte[] var10, Size_t var11, Argon2_type var12, Argon2_version var13);

   int argon2i_verify(byte[] var1, byte[] var2, Size_t var3);

   int argon2d_verify(byte[] var1, byte[] var2, Size_t var3);

   int argon2id_verify(byte[] var1, byte[] var2, Size_t var3);

   int argon2_verify(byte[] var1, byte[] var2, Size_t var3, Argon2_type var4);

   int argon2i_ctx(Argon2_context.ByReference var1);

   int argon2d_ctx(Argon2_context.ByReference var1);

   int argon2id_ctx(Argon2_context.ByReference var1);

   int argon2_ctx(Argon2_context.ByReference var1, Argon2_type var2);

   int argon2i_verify_ctx(Argon2_context.ByReference var1, byte[] var2);

   int argon2d_verify_ctx(Argon2_context.ByReference var1, byte[] var2);

   int argon2id_verify_ctx(Argon2_context.ByReference var1, byte[] var2);

   int argon2_verify_ctx(Argon2_context.ByReference var1, byte[] var2, Argon2_version var3);

   Size_t argon2_encodedlen(JnaUint32 var1, JnaUint32 var2, JnaUint32 var3, JnaUint32 var4, JnaUint32 var5, Argon2_type var6);

   String argon2_error_message(int var1);
}
