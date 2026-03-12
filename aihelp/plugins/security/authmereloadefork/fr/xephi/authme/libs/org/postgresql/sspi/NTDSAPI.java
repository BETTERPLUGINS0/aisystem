package fr.xephi.authme.libs.org.postgresql.sspi;

import com.sun.jna.LastErrorException;
import com.sun.jna.Native;
import com.sun.jna.WString;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;
import org.checkerframework.checker.nullness.qual.Nullable;

interface NTDSAPI extends StdCallLibrary {
   NTDSAPI instance = (NTDSAPI)Native.loadLibrary("NTDSAPI", NTDSAPI.class);
   int ERROR_SUCCESS = 0;
   int ERROR_INVALID_PARAMETER = 87;
   int ERROR_BUFFER_OVERFLOW = 111;

   int DsMakeSpnW(WString var1, WString var2, @Nullable WString var3, short var4, @Nullable WString var5, IntByReference var6, char[] var7) throws LastErrorException;
}
