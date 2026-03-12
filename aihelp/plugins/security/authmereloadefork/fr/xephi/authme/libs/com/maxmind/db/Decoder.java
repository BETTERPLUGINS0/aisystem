package fr.xephi.authme.libs.com.maxmind.db;

import fr.xephi.authme.libs.com.google.gson.Gson;
import fr.xephi.authme.libs.com.google.gson.JsonArray;
import fr.xephi.authme.libs.com.google.gson.JsonElement;
import fr.xephi.authme.libs.com.google.gson.JsonObject;
import fr.xephi.authme.libs.com.google.gson.JsonPrimitive;
import fr.xephi.authme.libs.com.maxmind.db.cache.NodeCache;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;

final class Decoder {
   private static final Charset UTF_8;
   private static final Gson OBJECT_MAPPER;
   private static final int[] POINTER_VALUE_OFFSETS;
   boolean POINTER_TEST_HACK = false;
   private final NodeCache cache;
   private final long pointerBase;
   private final CharsetDecoder utfDecoder;
   private final ByteBuffer buffer;
   private final NodeCache.Loader cacheLoader;

   Decoder(NodeCache cache, ByteBuffer buffer, long pointerBase) {
      this.utfDecoder = UTF_8.newDecoder();
      this.cacheLoader = new NodeCache.Loader() {
         public JsonElement load(int key) throws IOException {
            return Decoder.this.decode(key);
         }
      };
      this.cache = cache;
      this.pointerBase = pointerBase;
      this.buffer = buffer;
   }

   JsonElement decode(int offset) throws IOException {
      if (offset >= this.buffer.capacity()) {
         throw new InvalidDatabaseException("The MaxMind DB file's data section contains bad data: pointer larger than the database.");
      } else {
         this.buffer.position(offset);
         return this.decode();
      }
   }

   private JsonElement decode() throws IOException {
      int ctrlByte = 255 & this.buffer.get();
      Decoder.Type type = Decoder.Type.fromControlByte(ctrlByte);
      int size;
      if (type.equals(Decoder.Type.POINTER)) {
         size = (ctrlByte >>> 3 & 3) + 1;
         int base = size == 4 ? 0 : (byte)(ctrlByte & 7);
         int packed = this.decodeInteger(base, size);
         long pointer = (long)packed + this.pointerBase + (long)POINTER_VALUE_OFFSETS[size];
         if (this.POINTER_TEST_HACK) {
            return new JsonPrimitive(pointer);
         } else {
            int targetOffset = (int)pointer;
            int position = this.buffer.position();
            JsonElement node = this.cache.get(targetOffset, this.cacheLoader);
            this.buffer.position(position);
            return node;
         }
      } else {
         if (type.equals(Decoder.Type.EXTENDED)) {
            int nextByte = this.buffer.get();
            int typeNum = nextByte + 7;
            if (typeNum < 8) {
               throw new InvalidDatabaseException("Something went horribly wrong in the decoder. An extended type resolved to a type number < 8 (" + typeNum + ")");
            }

            type = Decoder.Type.get(typeNum);
         }

         size = ctrlByte & 31;
         if (size >= 29) {
            switch(size) {
            case 29:
               size = 29 + (255 & this.buffer.get());
               break;
            case 30:
               size = 285 + this.decodeInteger(2);
               break;
            default:
               size = 65821 + this.decodeInteger(3);
            }
         }

         return this.decodeByType(type, size);
      }
   }

   private JsonElement decodeByType(Decoder.Type type, int size) throws IOException {
      switch(type) {
      case MAP:
         return this.decodeMap(size);
      case ARRAY:
         return this.decodeArray(size);
      case BOOLEAN:
         return decodeBoolean(size);
      case UTF8_STRING:
         return new JsonPrimitive(this.decodeString(size));
      case DOUBLE:
         return this.decodeDouble(size);
      case FLOAT:
         return this.decodeFloat(size);
      case BYTES:
         return OBJECT_MAPPER.toJsonTree(this.getByteArray(size));
      case UINT16:
         return this.decodeUint16(size);
      case UINT32:
         return this.decodeUint32(size);
      case INT32:
         return this.decodeInt32(size);
      case UINT64:
      case UINT128:
         return this.decodeBigInteger(size);
      default:
         throw new InvalidDatabaseException("Unknown or unexpected type: " + type.name());
      }
   }

   private String decodeString(int size) throws CharacterCodingException {
      int oldLimit = this.buffer.limit();
      this.buffer.limit(this.buffer.position() + size);
      String s = this.utfDecoder.decode(this.buffer).toString();
      this.buffer.limit(oldLimit);
      return s;
   }

   private JsonPrimitive decodeUint16(int size) {
      return new JsonPrimitive(this.decodeInteger(size));
   }

   private JsonPrimitive decodeInt32(int size) {
      return new JsonPrimitive(this.decodeInteger(size));
   }

   private long decodeLong(int size) {
      long integer = 0L;

      for(int i = 0; i < size; ++i) {
         integer = integer << 8 | (long)(this.buffer.get() & 255);
      }

      return integer;
   }

   private JsonPrimitive decodeUint32(int size) {
      return new JsonPrimitive(this.decodeLong(size));
   }

   private int decodeInteger(int size) {
      return this.decodeInteger(0, size);
   }

   private int decodeInteger(int base, int size) {
      return decodeInteger(this.buffer, base, size);
   }

   static int decodeInteger(ByteBuffer buffer, int base, int size) {
      int integer = base;

      for(int i = 0; i < size; ++i) {
         integer = integer << 8 | buffer.get() & 255;
      }

      return integer;
   }

   private JsonPrimitive decodeBigInteger(int size) {
      byte[] bytes = this.getByteArray(size);
      return new JsonPrimitive(new BigInteger(1, bytes));
   }

   private JsonPrimitive decodeDouble(int size) throws InvalidDatabaseException {
      if (size != 8) {
         throw new InvalidDatabaseException("The MaxMind DB file's data section contains bad data: invalid size of double.");
      } else {
         return new JsonPrimitive(this.buffer.getDouble());
      }
   }

   private JsonPrimitive decodeFloat(int size) throws InvalidDatabaseException {
      if (size != 4) {
         throw new InvalidDatabaseException("The MaxMind DB file's data section contains bad data: invalid size of float.");
      } else {
         return new JsonPrimitive(this.buffer.getFloat());
      }
   }

   private static JsonPrimitive decodeBoolean(int size) throws InvalidDatabaseException {
      switch(size) {
      case 0:
         return new JsonPrimitive(false);
      case 1:
         return new JsonPrimitive(true);
      default:
         throw new InvalidDatabaseException("The MaxMind DB file's data section contains bad data: invalid size of boolean.");
      }
   }

   private JsonArray decodeArray(int size) throws IOException {
      JsonArray array = new JsonArray();

      for(int i = 0; i < size; ++i) {
         JsonElement r = this.decode();
         array.add(r);
      }

      return array;
   }

   private JsonObject decodeMap(int size) throws IOException {
      JsonObject object = new JsonObject();

      for(int i = 0; i < size; ++i) {
         String key = this.decode().getAsString();
         JsonElement value = this.decode();
         object.add(key, value);
      }

      return object;
   }

   private byte[] getByteArray(int length) {
      return getByteArray(this.buffer, length);
   }

   private static byte[] getByteArray(ByteBuffer buffer, int length) {
      byte[] bytes = new byte[length];
      buffer.get(bytes);
      return bytes;
   }

   static {
      UTF_8 = StandardCharsets.UTF_8;
      OBJECT_MAPPER = new Gson();
      POINTER_VALUE_OFFSETS = new int[]{0, 0, 2048, 526336, 0};
   }

   static enum Type {
      EXTENDED,
      POINTER,
      UTF8_STRING,
      DOUBLE,
      BYTES,
      UINT16,
      UINT32,
      MAP,
      INT32,
      UINT64,
      UINT128,
      ARRAY,
      CONTAINER,
      END_MARKER,
      BOOLEAN,
      FLOAT;

      static final Decoder.Type[] values = values();

      static Decoder.Type get(int i) {
         return values[i];
      }

      private static Decoder.Type get(byte b) {
         return get(b & 255);
      }

      static Decoder.Type fromControlByte(int b) {
         return get((byte)((255 & b) >>> 5));
      }
   }
}
