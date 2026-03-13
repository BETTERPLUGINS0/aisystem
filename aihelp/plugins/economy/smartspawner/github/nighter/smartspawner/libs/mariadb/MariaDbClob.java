package github.nighter.smartspawner.libs.mariadb;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.StandardCharsets;
import java.sql.Clob;
import java.sql.NClob;
import java.sql.SQLException;

public class MariaDbClob extends MariaDbBlob implements Clob, NClob, Serializable {
   private static final long serialVersionUID = -3066501059817815286L;

   public MariaDbClob(byte[] bytes) {
      super(bytes);
   }

   public MariaDbClob(byte[] bytes, int offset, int length) {
      super(bytes, offset, length);
   }

   public MariaDbClob() {
   }

   public String toString() {
      return new String(this.data, this.offset, this.length, StandardCharsets.UTF_8);
   }

   public String getSubString(long pos, int length) throws SQLException {
      if (pos < 1L) {
         throw new SQLException("position must be >= 1");
      } else if (length < 0) {
         throw new SQLException("length must be > 0");
      } else {
         String val = this.toString();
         return val.substring((int)pos - 1, Math.min((int)pos - 1 + length, val.length()));
      }
   }

   public Reader getCharacterStream() {
      return new StringReader(this.toString());
   }

   public Reader getCharacterStream(long pos, long length) throws SQLException {
      String val = this.toString();
      if ((long)val.length() < (long)((int)pos - 1) + length) {
         throw new SQLException("pos + length is greater than the number of characters in the Clob");
      } else {
         String sub = val.substring((int)pos - 1, (int)pos - 1 + (int)length);
         return new StringReader(sub);
      }
   }

   public Writer setCharacterStream(long pos) throws SQLException {
      int bytePosition = this.utf8Position((int)pos - 1);
      OutputStream stream = this.setBinaryStream((long)(bytePosition + 1));
      return new OutputStreamWriter(stream, StandardCharsets.UTF_8);
   }

   public InputStream getAsciiStream() throws SQLException {
      return this.getBinaryStream();
   }

   public long position(String searchStr, long start) {
      return (long)(this.toString().indexOf(searchStr, (int)start - 1) + 1);
   }

   public long position(Clob searchStr, long start) {
      return this.position(searchStr.toString(), start);
   }

   private int utf8Position(int charPosition) {
      int pos = this.offset;

      for(int i = 0; i < charPosition; ++i) {
         int byteValue = this.data[pos] & 255;
         if (byteValue < 128) {
            ++pos;
         } else if (byteValue < 224) {
            pos += 2;
         } else if (byteValue < 240) {
            pos += 3;
         } else {
            pos += 4;
         }
      }

      return pos;
   }

   public int setString(long pos, String str) throws SQLException {
      if (str == null) {
         throw new SQLException("cannot add null string");
      } else if (pos < 0L) {
         throw new SQLException("position must be >= 0");
      } else {
         int bytePosition = this.utf8Position((int)pos - 1);
         super.setBytes((long)(bytePosition + 1 - this.offset), str.getBytes(StandardCharsets.UTF_8));
         return str.length();
      }
   }

   public int setString(long pos, String str, int offset, int len) throws SQLException {
      if (str == null) {
         throw new SQLException("cannot add null string");
      } else if (offset < 0) {
         throw new SQLException("offset must be >= 0");
      } else if (len < 0) {
         throw new SQLException("len must be > 0");
      } else {
         return this.setString(pos, str.substring(offset, Math.min(offset + len, str.length())));
      }
   }

   public OutputStream setAsciiStream(long pos) throws SQLException {
      return this.setBinaryStream((long)(this.utf8Position((int)pos - 1) + 1));
   }

   public long length() {
      long len = 0L;

      int pos;
      for(pos = this.offset; len < (long)this.length && this.data[pos] > 0; ++pos) {
         ++len;
      }

      while(true) {
         while(true) {
            while(pos < this.offset + this.length) {
               byte firstByte = this.data[pos++];
               if (firstByte < 0) {
                  if (firstByte >> 5 == -2 && (firstByte & 30) != 0) {
                     ++pos;
                     ++len;
                  } else if (firstByte >> 4 == -2) {
                     if (pos + 1 >= this.offset + this.length) {
                        throw new UncheckedIOException("invalid UTF8", new CharacterCodingException());
                     }

                     pos += 2;
                     ++len;
                  } else {
                     if (firstByte >> 3 != -2) {
                        throw new UncheckedIOException("invalid UTF8", new CharacterCodingException());
                     }

                     if (pos + 2 < this.offset + this.length) {
                        pos += 3;
                        len += 2L;
                     } else {
                        pos += this.offset + this.length;
                        ++len;
                     }
                  }
               } else {
                  ++len;
               }
            }

            return len;
         }
      }
   }

   public void truncate(long truncateLen) {
      long len = 0L;

      int pos;
      for(pos = this.offset; len < (long)this.length && len < truncateLen && this.data[pos] >= 0; ++pos) {
         ++len;
      }

      while(pos < this.offset + this.length && len < truncateLen) {
         byte firstByte = this.data[pos++];
         if (firstByte < 0) {
            if (firstByte >> 5 == -2 && (firstByte & 30) != 0) {
               ++pos;
               ++len;
            } else if (firstByte >> 4 == -2) {
               if (pos + 1 >= this.offset + this.length) {
                  throw new UncheckedIOException("invalid UTF8", new CharacterCodingException());
               }

               pos += 2;
               ++len;
            } else {
               if (firstByte >> 3 != -2) {
                  throw new UncheckedIOException("invalid UTF8", new CharacterCodingException());
               }

               if (pos + 2 >= this.offset + this.length) {
                  throw new UncheckedIOException("invalid UTF8", new CharacterCodingException());
               }

               if (len + 2L <= truncateLen) {
                  pos += 3;
                  len += 2L;
               } else {
                  ++pos;
                  len = truncateLen;
               }
            }
         } else {
            ++len;
         }
      }

      this.length = pos - this.offset;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         MariaDbClob that = (MariaDbClob)o;
         if (this.length != that.length) {
            return false;
         } else {
            for(int i = 0; i < this.length; ++i) {
               if (this.data[this.offset + i] != that.data[that.offset + i]) {
                  return false;
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }
}
