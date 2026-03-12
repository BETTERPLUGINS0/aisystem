package fr.xephi.authme.libs.org.postgresql.util;

import fr.xephi.authme.libs.org.postgresql.core.EncodingPredictor;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ServerErrorMessage implements Serializable {
   private static final Logger LOGGER = Logger.getLogger(ServerErrorMessage.class.getName());
   private static final Character SEVERITY = 'S';
   private static final Character MESSAGE = 'M';
   private static final Character DETAIL = 'D';
   private static final Character HINT = 'H';
   private static final Character POSITION = 'P';
   private static final Character WHERE = 'W';
   private static final Character FILE = 'F';
   private static final Character LINE = 'L';
   private static final Character ROUTINE = 'R';
   private static final Character SQLSTATE = 'C';
   private static final Character INTERNAL_POSITION = 'p';
   private static final Character INTERNAL_QUERY = 'q';
   private static final Character SCHEMA = 's';
   private static final Character TABLE = 't';
   private static final Character COLUMN = 'c';
   private static final Character DATATYPE = 'd';
   private static final Character CONSTRAINT = 'n';
   private final Map<Character, String> mesgParts;

   public ServerErrorMessage(EncodingPredictor.DecodeResult serverError) {
      this(serverError.result);
      if (serverError.encoding != null) {
         this.mesgParts.put(MESSAGE, (String)this.mesgParts.get(MESSAGE) + GT.tr(" (pgjdbc: autodetected server-encoding to be {0}, if the message is not readable, please check database logs and/or host, port, dbname, user, password, pg_hba.conf)", serverError.encoding));
      }

   }

   public ServerErrorMessage(String serverError) {
      this.mesgParts = new HashMap();
      char[] chars = serverError.toCharArray();
      int pos = 0;

      for(int length = chars.length; pos < length; ++pos) {
         char mesgType = chars[pos];
         if (mesgType != 0) {
            ++pos;

            int startString;
            for(startString = pos; pos < length && chars[pos] != 0; ++pos) {
            }

            String mesgPart = new String(chars, startString, pos - startString);
            this.mesgParts.put(mesgType, mesgPart);
         }
      }

   }

   @Nullable
   public String getSQLState() {
      return (String)this.mesgParts.get(SQLSTATE);
   }

   @Nullable
   public String getMessage() {
      return (String)this.mesgParts.get(MESSAGE);
   }

   @Nullable
   public String getSeverity() {
      return (String)this.mesgParts.get(SEVERITY);
   }

   @Nullable
   public String getDetail() {
      return (String)this.mesgParts.get(DETAIL);
   }

   @Nullable
   public String getHint() {
      return (String)this.mesgParts.get(HINT);
   }

   public int getPosition() {
      return this.getIntegerPart(POSITION);
   }

   @Nullable
   public String getWhere() {
      return (String)this.mesgParts.get(WHERE);
   }

   @Nullable
   public String getSchema() {
      return (String)this.mesgParts.get(SCHEMA);
   }

   @Nullable
   public String getTable() {
      return (String)this.mesgParts.get(TABLE);
   }

   @Nullable
   public String getColumn() {
      return (String)this.mesgParts.get(COLUMN);
   }

   @Nullable
   public String getDatatype() {
      return (String)this.mesgParts.get(DATATYPE);
   }

   @Nullable
   public String getConstraint() {
      return (String)this.mesgParts.get(CONSTRAINT);
   }

   @Nullable
   public String getFile() {
      return (String)this.mesgParts.get(FILE);
   }

   public int getLine() {
      return this.getIntegerPart(LINE);
   }

   @Nullable
   public String getRoutine() {
      return (String)this.mesgParts.get(ROUTINE);
   }

   @Nullable
   public String getInternalQuery() {
      return (String)this.mesgParts.get(INTERNAL_QUERY);
   }

   public int getInternalPosition() {
      return this.getIntegerPart(INTERNAL_POSITION);
   }

   private int getIntegerPart(Character c) {
      String s = (String)this.mesgParts.get(c);
      return s == null ? 0 : Integer.parseInt(s);
   }

   String getNonSensitiveErrorMessage() {
      StringBuilder totalMessage = new StringBuilder();
      String message = (String)this.mesgParts.get(SEVERITY);
      if (message != null) {
         totalMessage.append(message).append(": ");
      }

      message = (String)this.mesgParts.get(MESSAGE);
      if (message != null) {
         totalMessage.append(message);
      }

      return totalMessage.toString();
   }

   public String toString() {
      StringBuilder totalMessage = new StringBuilder();
      String message = (String)this.mesgParts.get(SEVERITY);
      if (message != null) {
         totalMessage.append(message).append(": ");
      }

      message = (String)this.mesgParts.get(MESSAGE);
      if (message != null) {
         totalMessage.append(message);
      }

      message = (String)this.mesgParts.get(DETAIL);
      if (message != null) {
         totalMessage.append("\n  ").append(GT.tr("Detail: {0}", message));
      }

      message = (String)this.mesgParts.get(HINT);
      if (message != null) {
         totalMessage.append("\n  ").append(GT.tr("Hint: {0}", message));
      }

      message = (String)this.mesgParts.get(POSITION);
      if (message != null) {
         totalMessage.append("\n  ").append(GT.tr("Position: {0}", message));
      }

      message = (String)this.mesgParts.get(WHERE);
      if (message != null) {
         totalMessage.append("\n  ").append(GT.tr("Where: {0}", message));
      }

      if (LOGGER.isLoggable(Level.FINEST)) {
         String internalQuery = (String)this.mesgParts.get(INTERNAL_QUERY);
         if (internalQuery != null) {
            totalMessage.append("\n  ").append(GT.tr("Internal Query: {0}", internalQuery));
         }

         String internalPosition = (String)this.mesgParts.get(INTERNAL_POSITION);
         if (internalPosition != null) {
            totalMessage.append("\n  ").append(GT.tr("Internal Position: {0}", internalPosition));
         }

         String file = (String)this.mesgParts.get(FILE);
         String line = (String)this.mesgParts.get(LINE);
         String routine = (String)this.mesgParts.get(ROUTINE);
         if (file != null || line != null || routine != null) {
            totalMessage.append("\n  ").append(GT.tr("Location: File: {0}, Routine: {1}, Line: {2}", file, routine, line));
         }

         message = (String)this.mesgParts.get(SQLSTATE);
         if (message != null) {
            totalMessage.append("\n  ").append(GT.tr("Server SQLState: {0}", message));
         }
      }

      return totalMessage.toString();
   }
}
