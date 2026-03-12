package fr.xephi.authme.libs.com.maxmind.db;

import fr.xephi.authme.libs.com.google.gson.JsonElement;
import fr.xephi.authme.libs.com.google.gson.JsonObject;
import fr.xephi.authme.libs.com.maxmind.db.cache.NoCache;
import fr.xephi.authme.libs.com.maxmind.db.cache.NodeCache;
import fr.xephi.authme.libs.com.maxmind.db.model.CountryResponse;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicReference;

public class Reader implements GeoIp2Provider, Closeable {
   private static final int DATA_SECTION_SEPARATOR_SIZE = 16;
   private static final byte[] METADATA_START_MARKER = new byte[]{-85, -51, -17, 77, 97, 120, 77, 105, 110, 100, 46, 99, 111, 109};
   private final int ipV4Start;
   private final Metadata metadata;
   private final AtomicReference<BufferHolder> bufferHolderReference;
   private final NodeCache cache;

   public Reader(File database) throws IOException {
      this((File)database, (NodeCache)NoCache.getInstance());
   }

   public Reader(File database, NodeCache cache) throws IOException {
      this(database, Reader.FileMode.MEMORY_MAPPED, cache);
   }

   public Reader(InputStream source) throws IOException {
      this((InputStream)source, (NodeCache)NoCache.getInstance());
   }

   public Reader(InputStream source, NodeCache cache) throws IOException {
      this(new BufferHolder(source), "<InputStream>", cache);
   }

   public Reader(File database, Reader.FileMode fileMode) throws IOException {
      this((File)database, (Reader.FileMode)fileMode, NoCache.getInstance());
   }

   public Reader(File database, Reader.FileMode fileMode, NodeCache cache) throws IOException {
      this(new BufferHolder(database, fileMode), database.getName(), cache);
   }

   private Reader(BufferHolder bufferHolder, String name, NodeCache cache) throws IOException {
      this.bufferHolderReference = new AtomicReference(bufferHolder);
      if (cache == null) {
         throw new NullPointerException("Cache cannot be null");
      } else {
         this.cache = cache;
         ByteBuffer buffer = bufferHolder.get();
         int start = this.findMetadataStart(buffer, name);
         Decoder metadataDecoder = new Decoder(this.cache, buffer, (long)start);
         this.metadata = new Metadata((JsonObject)metadataDecoder.decode(start));
         this.ipV4Start = this.findIpV4StartNode(buffer);
      }
   }

   public JsonElement get(InetAddress ipAddress) throws IOException {
      return this.getRecord(ipAddress).getData();
   }

   public Record getRecord(InetAddress ipAddress) throws IOException {
      ByteBuffer buffer = this.getBufferHolder().get();
      byte[] rawAddress = ipAddress.getAddress();
      int bitLength = rawAddress.length * 8;
      int record = this.startNode(bitLength);
      int nodeCount = this.metadata.getNodeCount();

      int pl;
      for(pl = 0; pl < bitLength && record < nodeCount; ++pl) {
         int b = 255 & rawAddress[pl / 8];
         int bit = 1 & b >> 7 - pl % 8;
         record = this.readNode(buffer, record, bit);
      }

      JsonElement dataRecord = null;
      if (record > nodeCount) {
         dataRecord = this.resolveDataPointer(buffer, record);
      }

      return new Record(dataRecord, ipAddress, pl);
   }

   public CountryResponse getCountry(InetAddress ipAddress) throws IOException {
      JsonElement jsonElement = this.get(ipAddress);
      return jsonElement == null ? null : CountryResponse.of(jsonElement);
   }

   private BufferHolder getBufferHolder() throws ClosedDatabaseException {
      BufferHolder bufferHolder = (BufferHolder)this.bufferHolderReference.get();
      if (bufferHolder == null) {
         throw new ClosedDatabaseException();
      } else {
         return bufferHolder;
      }
   }

   private int startNode(int bitLength) {
      return this.metadata.getIpVersion() == 6 && bitLength == 32 ? this.ipV4Start : 0;
   }

   private int findIpV4StartNode(ByteBuffer buffer) throws InvalidDatabaseException {
      if (this.metadata.getIpVersion() == 4) {
         return 0;
      } else {
         int node = 0;

         for(int i = 0; i < 96 && node < this.metadata.getNodeCount(); ++i) {
            node = this.readNode(buffer, node, 0);
         }

         return node;
      }
   }

   private int readNode(ByteBuffer buffer, int nodeNumber, int index) throws InvalidDatabaseException {
      int baseOffset = nodeNumber * this.metadata.getNodeByteSize();
      switch(this.metadata.getRecordSize()) {
      case 24:
         buffer.position(baseOffset + index * 3);
         return Decoder.decodeInteger(buffer, 0, 3);
      case 28:
         int middle = buffer.get(baseOffset + 3);
         int middle;
         if (index == 0) {
            middle = (240 & middle) >>> 4;
         } else {
            middle = 15 & middle;
         }

         buffer.position(baseOffset + index * 4);
         return Decoder.decodeInteger(buffer, middle, 3);
      case 32:
         buffer.position(baseOffset + index * 4);
         return Decoder.decodeInteger(buffer, 0, 4);
      default:
         throw new InvalidDatabaseException("Unknown record size: " + this.metadata.getRecordSize());
      }
   }

   private JsonElement resolveDataPointer(ByteBuffer buffer, int pointer) throws IOException {
      int resolved = pointer - this.metadata.getNodeCount() + this.metadata.getSearchTreeSize();
      if (resolved >= buffer.capacity()) {
         throw new InvalidDatabaseException("The MaxMind DB file's search tree is corrupt: contains pointer larger than the database.");
      } else {
         Decoder decoder = new Decoder(this.cache, buffer, (long)(this.metadata.getSearchTreeSize() + 16));
         return decoder.decode(resolved);
      }
   }

   private int findMetadataStart(ByteBuffer buffer, String databaseName) throws InvalidDatabaseException {
      int fileSize = buffer.capacity();

      label24:
      for(int i = 0; i < fileSize - METADATA_START_MARKER.length + 1; ++i) {
         for(int j = 0; j < METADATA_START_MARKER.length; ++j) {
            byte b = buffer.get(fileSize - i - j - 1);
            if (b != METADATA_START_MARKER[METADATA_START_MARKER.length - j - 1]) {
               continue label24;
            }
         }

         return fileSize - i;
      }

      throw new InvalidDatabaseException("Could not find a MaxMind DB metadata marker in this file (" + databaseName + "). Is this a valid MaxMind DB file?");
   }

   public Metadata getMetadata() {
      return this.metadata;
   }

   public void close() throws IOException {
      this.bufferHolderReference.set((Object)null);
   }

   public static enum FileMode {
      MEMORY_MAPPED,
      MEMORY;
   }
}
