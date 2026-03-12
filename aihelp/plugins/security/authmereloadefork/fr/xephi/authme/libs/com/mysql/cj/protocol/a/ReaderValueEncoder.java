package fr.xephi.authme.libs.com.mysql.cj.protocol.a;

import fr.xephi.authme.libs.com.mysql.cj.BindValue;
import fr.xephi.authme.libs.com.mysql.cj.conf.PropertyKey;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.CJOperationNotSupportedException;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.ExceptionFactory;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.ExceptionInterceptor;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.WrongArgumentException;
import fr.xephi.authme.libs.com.mysql.cj.protocol.Message;
import fr.xephi.authme.libs.com.mysql.cj.util.StringUtils;
import fr.xephi.authme.libs.com.mysql.cj.util.Util;
import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

public class ReaderValueEncoder extends AbstractValueEncoder {
   public byte[] getBytes(BindValue binding) {
      return this.readBytes((Reader)binding.getValue(), binding);
   }

   public String getString(BindValue binding) {
      return "'** STREAM DATA **'";
   }

   public void encodeAsBinary(Message msg, BindValue binding) {
      throw (CJOperationNotSupportedException)ExceptionFactory.createException(CJOperationNotSupportedException.class, "Not supported");
   }

   protected byte[] readBytes(Reader reader, BindValue binding) {
      try {
         char[] c = null;
         int len = false;
         boolean useLength = (Boolean)this.propertySet.getBooleanProperty(PropertyKey.useStreamLengthsInPrepStmts).getValue();
         String clobEncoding = binding.isNational() ? null : this.propertySet.getStringProperty(PropertyKey.clobCharacterEncoding).getStringValue();
         if (clobEncoding == null) {
            clobEncoding = this.charEncoding.getStringValue();
         }

         long scaleOrLength = binding.getScaleOrLength();
         byte[] bytes;
         char[] c;
         if (useLength && scaleOrLength != -1L) {
            c = new char[(int)scaleOrLength];
            int numCharsRead = Util.readFully(reader, c, (int)scaleOrLength);
            bytes = StringUtils.getBytes(new String(c, 0, numCharsRead), clobEncoding);
         } else {
            c = new char[4096];
            StringBuilder buf = new StringBuilder();

            int len;
            while((len = reader.read(c)) != -1) {
               buf.append(c, 0, len);
            }

            bytes = StringUtils.getBytes(buf.toString(), clobEncoding);
         }

         return this.escapeBytesIfNeeded(bytes);
      } catch (UnsupportedEncodingException var11) {
         throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, var11.toString(), var11, this.exceptionInterceptor);
      } catch (IOException var12) {
         throw ExceptionFactory.createException((String)var12.toString(), (Throwable)var12, (ExceptionInterceptor)this.exceptionInterceptor);
      }
   }
}
