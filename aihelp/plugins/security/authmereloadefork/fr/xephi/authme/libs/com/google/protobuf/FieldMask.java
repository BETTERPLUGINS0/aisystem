package fr.xephi.authme.libs.com.google.protobuf;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;

public final class FieldMask extends GeneratedMessageV3 implements FieldMaskOrBuilder {
   private static final long serialVersionUID = 0L;
   public static final int PATHS_FIELD_NUMBER = 1;
   private LazyStringList paths_;
   private byte memoizedIsInitialized;
   private static final FieldMask DEFAULT_INSTANCE = new FieldMask();
   private static final Parser<FieldMask> PARSER = new AbstractParser<FieldMask>() {
      public FieldMask parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
         FieldMask.Builder builder = FieldMask.newBuilder();

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

   private FieldMask(GeneratedMessageV3.Builder<?> builder) {
      super(builder);
      this.memoizedIsInitialized = -1;
   }

   private FieldMask() {
      this.memoizedIsInitialized = -1;
      this.paths_ = LazyStringArrayList.EMPTY;
   }

   protected Object newInstance(GeneratedMessageV3.UnusedPrivateParameter unused) {
      return new FieldMask();
   }

   public final UnknownFieldSet getUnknownFields() {
      return this.unknownFields;
   }

   public static final Descriptors.Descriptor getDescriptor() {
      return FieldMaskProto.internal_static_google_protobuf_FieldMask_descriptor;
   }

   protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
      return FieldMaskProto.internal_static_google_protobuf_FieldMask_fieldAccessorTable.ensureFieldAccessorsInitialized(FieldMask.class, FieldMask.Builder.class);
   }

   public ProtocolStringList getPathsList() {
      return this.paths_;
   }

   public int getPathsCount() {
      return this.paths_.size();
   }

   public String getPaths(int index) {
      return (String)this.paths_.get(index);
   }

   public ByteString getPathsBytes(int index) {
      return this.paths_.getByteString(index);
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
      for(int i = 0; i < this.paths_.size(); ++i) {
         GeneratedMessageV3.writeString(output, 1, this.paths_.getRaw(i));
      }

      this.getUnknownFields().writeTo(output);
   }

   public int getSerializedSize() {
      int size = this.memoizedSize;
      if (size != -1) {
         return size;
      } else {
         int size = 0;
         int dataSize = 0;

         for(int i = 0; i < this.paths_.size(); ++i) {
            dataSize += computeStringSizeNoTag(this.paths_.getRaw(i));
         }

         size = size + dataSize;
         size += 1 * this.getPathsList().size();
         size += this.getUnknownFields().getSerializedSize();
         this.memoizedSize = size;
         return size;
      }
   }

   public boolean equals(Object obj) {
      if (obj == this) {
         return true;
      } else if (!(obj instanceof FieldMask)) {
         return super.equals(obj);
      } else {
         FieldMask other = (FieldMask)obj;
         if (!this.getPathsList().equals(other.getPathsList())) {
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
         if (this.getPathsCount() > 0) {
            hash = 37 * hash + 1;
            hash = 53 * hash + this.getPathsList().hashCode();
         }

         hash = 29 * hash + this.getUnknownFields().hashCode();
         this.memoizedHashCode = hash;
         return hash;
      }
   }

   public static FieldMask parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
      return (FieldMask)PARSER.parseFrom(data);
   }

   public static FieldMask parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (FieldMask)PARSER.parseFrom(data, extensionRegistry);
   }

   public static FieldMask parseFrom(ByteString data) throws InvalidProtocolBufferException {
      return (FieldMask)PARSER.parseFrom(data);
   }

   public static FieldMask parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (FieldMask)PARSER.parseFrom(data, extensionRegistry);
   }

   public static FieldMask parseFrom(byte[] data) throws InvalidProtocolBufferException {
      return (FieldMask)PARSER.parseFrom(data);
   }

   public static FieldMask parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (FieldMask)PARSER.parseFrom(data, extensionRegistry);
   }

   public static FieldMask parseFrom(InputStream input) throws IOException {
      return (FieldMask)GeneratedMessageV3.parseWithIOException(PARSER, input);
   }

   public static FieldMask parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return (FieldMask)GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
   }

   public static FieldMask parseDelimitedFrom(InputStream input) throws IOException {
      return (FieldMask)GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
   }

   public static FieldMask parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return (FieldMask)GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input, extensionRegistry);
   }

   public static FieldMask parseFrom(CodedInputStream input) throws IOException {
      return (FieldMask)GeneratedMessageV3.parseWithIOException(PARSER, input);
   }

   public static FieldMask parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return (FieldMask)GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
   }

   public FieldMask.Builder newBuilderForType() {
      return newBuilder();
   }

   public static FieldMask.Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
   }

   public static FieldMask.Builder newBuilder(FieldMask prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
   }

   public FieldMask.Builder toBuilder() {
      return this == DEFAULT_INSTANCE ? new FieldMask.Builder() : (new FieldMask.Builder()).mergeFrom(this);
   }

   protected FieldMask.Builder newBuilderForType(GeneratedMessageV3.BuilderParent parent) {
      FieldMask.Builder builder = new FieldMask.Builder(parent);
      return builder;
   }

   public static FieldMask getDefaultInstance() {
      return DEFAULT_INSTANCE;
   }

   public static Parser<FieldMask> parser() {
      return PARSER;
   }

   public Parser<FieldMask> getParserForType() {
      return PARSER;
   }

   public FieldMask getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
   }

   // $FF: synthetic method
   FieldMask(GeneratedMessageV3.Builder x0, Object x1) {
      this(x0);
   }

   public static final class Builder extends GeneratedMessageV3.Builder<FieldMask.Builder> implements FieldMaskOrBuilder {
      private int bitField0_;
      private LazyStringList paths_;

      public static final Descriptors.Descriptor getDescriptor() {
         return FieldMaskProto.internal_static_google_protobuf_FieldMask_descriptor;
      }

      protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
         return FieldMaskProto.internal_static_google_protobuf_FieldMask_fieldAccessorTable.ensureFieldAccessorsInitialized(FieldMask.class, FieldMask.Builder.class);
      }

      private Builder() {
         this.paths_ = LazyStringArrayList.EMPTY;
      }

      private Builder(GeneratedMessageV3.BuilderParent parent) {
         super(parent);
         this.paths_ = LazyStringArrayList.EMPTY;
      }

      public FieldMask.Builder clear() {
         super.clear();
         this.paths_ = LazyStringArrayList.EMPTY;
         this.bitField0_ &= -2;
         return this;
      }

      public Descriptors.Descriptor getDescriptorForType() {
         return FieldMaskProto.internal_static_google_protobuf_FieldMask_descriptor;
      }

      public FieldMask getDefaultInstanceForType() {
         return FieldMask.getDefaultInstance();
      }

      public FieldMask build() {
         FieldMask result = this.buildPartial();
         if (!result.isInitialized()) {
            throw newUninitializedMessageException(result);
         } else {
            return result;
         }
      }

      public FieldMask buildPartial() {
         FieldMask result = new FieldMask(this);
         int from_bitField0_ = this.bitField0_;
         if ((this.bitField0_ & 1) != 0) {
            this.paths_ = this.paths_.getUnmodifiableView();
            this.bitField0_ &= -2;
         }

         result.paths_ = this.paths_;
         this.onBuilt();
         return result;
      }

      public FieldMask.Builder clone() {
         return (FieldMask.Builder)super.clone();
      }

      public FieldMask.Builder setField(Descriptors.FieldDescriptor field, Object value) {
         return (FieldMask.Builder)super.setField(field, value);
      }

      public FieldMask.Builder clearField(Descriptors.FieldDescriptor field) {
         return (FieldMask.Builder)super.clearField(field);
      }

      public FieldMask.Builder clearOneof(Descriptors.OneofDescriptor oneof) {
         return (FieldMask.Builder)super.clearOneof(oneof);
      }

      public FieldMask.Builder setRepeatedField(Descriptors.FieldDescriptor field, int index, Object value) {
         return (FieldMask.Builder)super.setRepeatedField(field, index, value);
      }

      public FieldMask.Builder addRepeatedField(Descriptors.FieldDescriptor field, Object value) {
         return (FieldMask.Builder)super.addRepeatedField(field, value);
      }

      public FieldMask.Builder mergeFrom(Message other) {
         if (other instanceof FieldMask) {
            return this.mergeFrom((FieldMask)other);
         } else {
            super.mergeFrom(other);
            return this;
         }
      }

      public FieldMask.Builder mergeFrom(FieldMask other) {
         if (other == FieldMask.getDefaultInstance()) {
            return this;
         } else {
            if (!other.paths_.isEmpty()) {
               if (this.paths_.isEmpty()) {
                  this.paths_ = other.paths_;
                  this.bitField0_ &= -2;
               } else {
                  this.ensurePathsIsMutable();
                  this.paths_.addAll(other.paths_);
               }

               this.onChanged();
            }

            this.mergeUnknownFields(other.getUnknownFields());
            this.onChanged();
            return this;
         }
      }

      public final boolean isInitialized() {
         return true;
      }

      public FieldMask.Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
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
                  case 10:
                     String s = input.readStringRequireUtf8();
                     this.ensurePathsIsMutable();
                     this.paths_.add((Object)s);
                     break;
                  default:
                     if (!super.parseUnknownField(input, extensionRegistry, tag)) {
                        done = true;
                     }
                  }
               }
            } catch (InvalidProtocolBufferException var9) {
               throw var9.unwrapIOException();
            } finally {
               this.onChanged();
            }

            return this;
         }
      }

      private void ensurePathsIsMutable() {
         if ((this.bitField0_ & 1) == 0) {
            this.paths_ = new LazyStringArrayList(this.paths_);
            this.bitField0_ |= 1;
         }

      }

      public ProtocolStringList getPathsList() {
         return this.paths_.getUnmodifiableView();
      }

      public int getPathsCount() {
         return this.paths_.size();
      }

      public String getPaths(int index) {
         return (String)this.paths_.get(index);
      }

      public ByteString getPathsBytes(int index) {
         return this.paths_.getByteString(index);
      }

      public FieldMask.Builder setPaths(int index, String value) {
         if (value == null) {
            throw new NullPointerException();
         } else {
            this.ensurePathsIsMutable();
            this.paths_.set(index, (Object)value);
            this.onChanged();
            return this;
         }
      }

      public FieldMask.Builder addPaths(String value) {
         if (value == null) {
            throw new NullPointerException();
         } else {
            this.ensurePathsIsMutable();
            this.paths_.add((Object)value);
            this.onChanged();
            return this;
         }
      }

      public FieldMask.Builder addAllPaths(Iterable<String> values) {
         this.ensurePathsIsMutable();
         AbstractMessageLite.Builder.addAll(values, (List)this.paths_);
         this.onChanged();
         return this;
      }

      public FieldMask.Builder clearPaths() {
         this.paths_ = LazyStringArrayList.EMPTY;
         this.bitField0_ &= -2;
         this.onChanged();
         return this;
      }

      public FieldMask.Builder addPathsBytes(ByteString value) {
         if (value == null) {
            throw new NullPointerException();
         } else {
            AbstractMessageLite.checkByteStringIsUtf8(value);
            this.ensurePathsIsMutable();
            this.paths_.add(value);
            this.onChanged();
            return this;
         }
      }

      public final FieldMask.Builder setUnknownFields(UnknownFieldSet unknownFields) {
         return (FieldMask.Builder)super.setUnknownFields(unknownFields);
      }

      public final FieldMask.Builder mergeUnknownFields(UnknownFieldSet unknownFields) {
         return (FieldMask.Builder)super.mergeUnknownFields(unknownFields);
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
