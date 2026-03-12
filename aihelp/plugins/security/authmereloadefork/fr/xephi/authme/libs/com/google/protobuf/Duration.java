package fr.xephi.authme.libs.com.google.protobuf;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public final class Duration extends GeneratedMessageV3 implements DurationOrBuilder {
   private static final long serialVersionUID = 0L;
   public static final int SECONDS_FIELD_NUMBER = 1;
   private long seconds_;
   public static final int NANOS_FIELD_NUMBER = 2;
   private int nanos_;
   private byte memoizedIsInitialized;
   private static final Duration DEFAULT_INSTANCE = new Duration();
   private static final Parser<Duration> PARSER = new AbstractParser<Duration>() {
      public Duration parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
         Duration.Builder builder = Duration.newBuilder();

         try {
            builder.mergeFrom(input, extensionRegistry);
         } catch (InvalidProtocolBufferException var5) {
            throw var5.setUnfinishedMessage(builder.buildPartial());
         } catch (UninitializedMessageException var6) {
            throw var6.asInvalidProtocolBufferException().setUnfinishedMessage(builder.buildPartial());
         } catch (IOException var7) {
            throw (new InvalidProtocolBufferException(var7)).setUnfinishedMessage(builder.buildPartial());
         }

         return builder.buildPartial();
      }
   };

   private Duration(GeneratedMessageV3.Builder<?> builder) {
      super(builder);
      this.memoizedIsInitialized = -1;
   }

   private Duration() {
      this.memoizedIsInitialized = -1;
   }

   protected Object newInstance(GeneratedMessageV3.UnusedPrivateParameter unused) {
      return new Duration();
   }

   public final UnknownFieldSet getUnknownFields() {
      return this.unknownFields;
   }

   public static final Descriptors.Descriptor getDescriptor() {
      return DurationProto.internal_static_google_protobuf_Duration_descriptor;
   }

   protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
      return DurationProto.internal_static_google_protobuf_Duration_fieldAccessorTable.ensureFieldAccessorsInitialized(Duration.class, Duration.Builder.class);
   }

   public long getSeconds() {
      return this.seconds_;
   }

   public int getNanos() {
      return this.nanos_;
   }

   public final boolean isInitialized() {
      byte isInitialized = this.memoizedIsInitialized;
      if (isInitialized == 1) {
         return true;
      } else if (isInitialized == 0) {
         return false;
      } else {
         this.memoizedIsInitialized = 1;
         return true;
      }
   }

   public void writeTo(CodedOutputStream output) throws IOException {
      if (this.seconds_ != 0L) {
         output.writeInt64(1, this.seconds_);
      }

      if (this.nanos_ != 0) {
         output.writeInt32(2, this.nanos_);
      }

      this.getUnknownFields().writeTo(output);
   }

   public int getSerializedSize() {
      int size = this.memoizedSize;
      if (size != -1) {
         return size;
      } else {
         size = 0;
         if (this.seconds_ != 0L) {
            size += CodedOutputStream.computeInt64Size(1, this.seconds_);
         }

         if (this.nanos_ != 0) {
            size += CodedOutputStream.computeInt32Size(2, this.nanos_);
         }

         size += this.getUnknownFields().getSerializedSize();
         this.memoizedSize = size;
         return size;
      }
   }

   public boolean equals(Object obj) {
      if (obj == this) {
         return true;
      } else if (!(obj instanceof Duration)) {
         return super.equals(obj);
      } else {
         Duration other = (Duration)obj;
         if (this.getSeconds() != other.getSeconds()) {
            return false;
         } else if (this.getNanos() != other.getNanos()) {
            return false;
         } else {
            return this.getUnknownFields().equals(other.getUnknownFields());
         }
      }
   }

   public int hashCode() {
      if (this.memoizedHashCode != 0) {
         return this.memoizedHashCode;
      } else {
         int hash = 41;
         int hash = 19 * hash + getDescriptor().hashCode();
         hash = 37 * hash + 1;
         hash = 53 * hash + Internal.hashLong(this.getSeconds());
         hash = 37 * hash + 2;
         hash = 53 * hash + this.getNanos();
         hash = 29 * hash + this.getUnknownFields().hashCode();
         this.memoizedHashCode = hash;
         return hash;
      }
   }

   public static Duration parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
      return (Duration)PARSER.parseFrom(data);
   }

   public static Duration parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (Duration)PARSER.parseFrom(data, extensionRegistry);
   }

   public static Duration parseFrom(ByteString data) throws InvalidProtocolBufferException {
      return (Duration)PARSER.parseFrom(data);
   }

   public static Duration parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (Duration)PARSER.parseFrom(data, extensionRegistry);
   }

   public static Duration parseFrom(byte[] data) throws InvalidProtocolBufferException {
      return (Duration)PARSER.parseFrom(data);
   }

   public static Duration parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (Duration)PARSER.parseFrom(data, extensionRegistry);
   }

   public static Duration parseFrom(InputStream input) throws IOException {
      return (Duration)GeneratedMessageV3.parseWithIOException(PARSER, input);
   }

   public static Duration parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return (Duration)GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
   }

   public static Duration parseDelimitedFrom(InputStream input) throws IOException {
      return (Duration)GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
   }

   public static Duration parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return (Duration)GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input, extensionRegistry);
   }

   public static Duration parseFrom(CodedInputStream input) throws IOException {
      return (Duration)GeneratedMessageV3.parseWithIOException(PARSER, input);
   }

   public static Duration parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return (Duration)GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
   }

   public Duration.Builder newBuilderForType() {
      return newBuilder();
   }

   public static Duration.Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
   }

   public static Duration.Builder newBuilder(Duration prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
   }

   public Duration.Builder toBuilder() {
      return this == DEFAULT_INSTANCE ? new Duration.Builder() : (new Duration.Builder()).mergeFrom(this);
   }

   protected Duration.Builder newBuilderForType(GeneratedMessageV3.BuilderParent parent) {
      Duration.Builder builder = new Duration.Builder(parent);
      return builder;
   }

   public static Duration getDefaultInstance() {
      return DEFAULT_INSTANCE;
   }

   public static Parser<Duration> parser() {
      return PARSER;
   }

   public Parser<Duration> getParserForType() {
      return PARSER;
   }

   public Duration getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
   }

   // $FF: synthetic method
   Duration(GeneratedMessageV3.Builder x0, Object x1) {
      this(x0);
   }

   public static final class Builder extends GeneratedMessageV3.Builder<Duration.Builder> implements DurationOrBuilder {
      private long seconds_;
      private int nanos_;

      public static final Descriptors.Descriptor getDescriptor() {
         return DurationProto.internal_static_google_protobuf_Duration_descriptor;
      }

      protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
         return DurationProto.internal_static_google_protobuf_Duration_fieldAccessorTable.ensureFieldAccessorsInitialized(Duration.class, Duration.Builder.class);
      }

      private Builder() {
      }

      private Builder(GeneratedMessageV3.BuilderParent parent) {
         super(parent);
      }

      public Duration.Builder clear() {
         super.clear();
         this.seconds_ = 0L;
         this.nanos_ = 0;
         return this;
      }

      public Descriptors.Descriptor getDescriptorForType() {
         return DurationProto.internal_static_google_protobuf_Duration_descriptor;
      }

      public Duration getDefaultInstanceForType() {
         return Duration.getDefaultInstance();
      }

      public Duration build() {
         Duration result = this.buildPartial();
         if (!result.isInitialized()) {
            throw newUninitializedMessageException(result);
         } else {
            return result;
         }
      }

      public Duration buildPartial() {
         Duration result = new Duration(this);
         result.seconds_ = this.seconds_;
         result.nanos_ = this.nanos_;
         this.onBuilt();
         return result;
      }

      public Duration.Builder clone() {
         return (Duration.Builder)super.clone();
      }

      public Duration.Builder setField(Descriptors.FieldDescriptor field, Object value) {
         return (Duration.Builder)super.setField(field, value);
      }

      public Duration.Builder clearField(Descriptors.FieldDescriptor field) {
         return (Duration.Builder)super.clearField(field);
      }

      public Duration.Builder clearOneof(Descriptors.OneofDescriptor oneof) {
         return (Duration.Builder)super.clearOneof(oneof);
      }

      public Duration.Builder setRepeatedField(Descriptors.FieldDescriptor field, int index, Object value) {
         return (Duration.Builder)super.setRepeatedField(field, index, value);
      }

      public Duration.Builder addRepeatedField(Descriptors.FieldDescriptor field, Object value) {
         return (Duration.Builder)super.addRepeatedField(field, value);
      }

      public Duration.Builder mergeFrom(Message other) {
         if (other instanceof Duration) {
            return this.mergeFrom((Duration)other);
         } else {
            super.mergeFrom(other);
            return this;
         }
      }

      public Duration.Builder mergeFrom(Duration other) {
         if (other == Duration.getDefaultInstance()) {
            return this;
         } else {
            if (other.getSeconds() != 0L) {
               this.setSeconds(other.getSeconds());
            }

            if (other.getNanos() != 0) {
               this.setNanos(other.getNanos());
            }

            this.mergeUnknownFields(other.getUnknownFields());
            this.onChanged();
            return this;
         }
      }

      public final boolean isInitialized() {
         return true;
      }

      public Duration.Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
         if (extensionRegistry == null) {
            throw new NullPointerException();
         } else {
            try {
               boolean done = false;

               while(!done) {
                  int tag = input.readTag();
                  switch(tag) {
                  case 0:
                     done = true;
                     break;
                  case 8:
                     this.seconds_ = input.readInt64();
                     break;
                  case 16:
                     this.nanos_ = input.readInt32();
                     break;
                  default:
                     if (!super.parseUnknownField(input, extensionRegistry, tag)) {
                        done = true;
                     }
                  }
               }
            } catch (InvalidProtocolBufferException var8) {
               throw var8.unwrapIOException();
            } finally {
               this.onChanged();
            }

            return this;
         }
      }

      public long getSeconds() {
         return this.seconds_;
      }

      public Duration.Builder setSeconds(long value) {
         this.seconds_ = value;
         this.onChanged();
         return this;
      }

      public Duration.Builder clearSeconds() {
         this.seconds_ = 0L;
         this.onChanged();
         return this;
      }

      public int getNanos() {
         return this.nanos_;
      }

      public Duration.Builder setNanos(int value) {
         this.nanos_ = value;
         this.onChanged();
         return this;
      }

      public Duration.Builder clearNanos() {
         this.nanos_ = 0;
         this.onChanged();
         return this;
      }

      public final Duration.Builder setUnknownFields(UnknownFieldSet unknownFields) {
         return (Duration.Builder)super.setUnknownFields(unknownFields);
      }

      public final Duration.Builder mergeUnknownFields(UnknownFieldSet unknownFields) {
         return (Duration.Builder)super.mergeUnknownFields(unknownFields);
      }

      // $FF: synthetic method
      Builder(Object x0) {
         this();
      }

      // $FF: synthetic method
      Builder(GeneratedMessageV3.BuilderParent x0, Object x1) {
         this(x0);
      }
   }
}
