package fr.xephi.authme.libs.com.google.protobuf;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Type extends GeneratedMessageV3 implements TypeOrBuilder {
   private static final long serialVersionUID = 0L;
   public static final int NAME_FIELD_NUMBER = 1;
   private volatile Object name_;
   public static final int FIELDS_FIELD_NUMBER = 2;
   private List<Field> fields_;
   public static final int ONEOFS_FIELD_NUMBER = 3;
   private LazyStringList oneofs_;
   public static final int OPTIONS_FIELD_NUMBER = 4;
   private List<Option> options_;
   public static final int SOURCE_CONTEXT_FIELD_NUMBER = 5;
   private SourceContext sourceContext_;
   public static final int SYNTAX_FIELD_NUMBER = 6;
   private int syntax_;
   private byte memoizedIsInitialized;
   private static final Type DEFAULT_INSTANCE = new Type();
   private static final Parser<Type> PARSER = new AbstractParser<Type>() {
      public Type parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
         Type.Builder builder = Type.newBuilder();

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

   private Type(GeneratedMessageV3.Builder<?> builder) {
      super(builder);
      this.memoizedIsInitialized = -1;
   }

   private Type() {
      this.memoizedIsInitialized = -1;
      this.name_ = "";
      this.fields_ = Collections.emptyList();
      this.oneofs_ = LazyStringArrayList.EMPTY;
      this.options_ = Collections.emptyList();
      this.syntax_ = 0;
   }

   protected Object newInstance(GeneratedMessageV3.UnusedPrivateParameter unused) {
      return new Type();
   }

   public final UnknownFieldSet getUnknownFields() {
      return this.unknownFields;
   }

   public static final Descriptors.Descriptor getDescriptor() {
      return TypeProto.internal_static_google_protobuf_Type_descriptor;
   }

   protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
      return TypeProto.internal_static_google_protobuf_Type_fieldAccessorTable.ensureFieldAccessorsInitialized(Type.class, Type.Builder.class);
   }

   public String getName() {
      Object ref = this.name_;
      if (ref instanceof String) {
         return (String)ref;
      } else {
         ByteString bs = (ByteString)ref;
         String s = bs.toStringUtf8();
         this.name_ = s;
         return s;
      }
   }

   public ByteString getNameBytes() {
      Object ref = this.name_;
      if (ref instanceof String) {
         ByteString b = ByteString.copyFromUtf8((String)ref);
         this.name_ = b;
         return b;
      } else {
         return (ByteString)ref;
      }
   }

   public List<Field> getFieldsList() {
      return this.fields_;
   }

   public List<? extends FieldOrBuilder> getFieldsOrBuilderList() {
      return this.fields_;
   }

   public int getFieldsCount() {
      return this.fields_.size();
   }

   public Field getFields(int index) {
      return (Field)this.fields_.get(index);
   }

   public FieldOrBuilder getFieldsOrBuilder(int index) {
      return (FieldOrBuilder)this.fields_.get(index);
   }

   public ProtocolStringList getOneofsList() {
      return this.oneofs_;
   }

   public int getOneofsCount() {
      return this.oneofs_.size();
   }

   public String getOneofs(int index) {
      return (String)this.oneofs_.get(index);
   }

   public ByteString getOneofsBytes(int index) {
      return this.oneofs_.getByteString(index);
   }

   public List<Option> getOptionsList() {
      return this.options_;
   }

   public List<? extends OptionOrBuilder> getOptionsOrBuilderList() {
      return this.options_;
   }

   public int getOptionsCount() {
      return this.options_.size();
   }

   public Option getOptions(int index) {
      return (Option)this.options_.get(index);
   }

   public OptionOrBuilder getOptionsOrBuilder(int index) {
      return (OptionOrBuilder)this.options_.get(index);
   }

   public boolean hasSourceContext() {
      return this.sourceContext_ != null;
   }

   public SourceContext getSourceContext() {
      return this.sourceContext_ == null ? SourceContext.getDefaultInstance() : this.sourceContext_;
   }

   public SourceContextOrBuilder getSourceContextOrBuilder() {
      return this.getSourceContext();
   }

   public int getSyntaxValue() {
      return this.syntax_;
   }

   public Syntax getSyntax() {
      Syntax result = Syntax.valueOf(this.syntax_);
      return result == null ? Syntax.UNRECOGNIZED : result;
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
      if (!GeneratedMessageV3.isStringEmpty(this.name_)) {
         GeneratedMessageV3.writeString(output, 1, this.name_);
      }

      int i;
      for(i = 0; i < this.fields_.size(); ++i) {
         output.writeMessage(2, (MessageLite)this.fields_.get(i));
      }

      for(i = 0; i < this.oneofs_.size(); ++i) {
         GeneratedMessageV3.writeString(output, 3, this.oneofs_.getRaw(i));
      }

      for(i = 0; i < this.options_.size(); ++i) {
         output.writeMessage(4, (MessageLite)this.options_.get(i));
      }

      if (this.sourceContext_ != null) {
         output.writeMessage(5, this.getSourceContext());
      }

      if (this.syntax_ != Syntax.SYNTAX_PROTO2.getNumber()) {
         output.writeEnum(6, this.syntax_);
      }

      this.getUnknownFields().writeTo(output);
   }

   public int getSerializedSize() {
      int size = this.memoizedSize;
      if (size != -1) {
         return size;
      } else {
         size = 0;
         if (!GeneratedMessageV3.isStringEmpty(this.name_)) {
            size += GeneratedMessageV3.computeStringSize(1, this.name_);
         }

         int i;
         for(i = 0; i < this.fields_.size(); ++i) {
            size += CodedOutputStream.computeMessageSize(2, (MessageLite)this.fields_.get(i));
         }

         i = 0;

         for(int i = 0; i < this.oneofs_.size(); ++i) {
            i += computeStringSizeNoTag(this.oneofs_.getRaw(i));
         }

         size += i;
         size += 1 * this.getOneofsList().size();

         for(i = 0; i < this.options_.size(); ++i) {
            size += CodedOutputStream.computeMessageSize(4, (MessageLite)this.options_.get(i));
         }

         if (this.sourceContext_ != null) {
            size += CodedOutputStream.computeMessageSize(5, this.getSourceContext());
         }

         if (this.syntax_ != Syntax.SYNTAX_PROTO2.getNumber()) {
            size += CodedOutputStream.computeEnumSize(6, this.syntax_);
         }

         size += this.getUnknownFields().getSerializedSize();
         this.memoizedSize = size;
         return size;
      }
   }

   public boolean equals(Object obj) {
      if (obj == this) {
         return true;
      } else if (!(obj instanceof Type)) {
         return super.equals(obj);
      } else {
         Type other = (Type)obj;
         if (!this.getName().equals(other.getName())) {
            return false;
         } else if (!this.getFieldsList().equals(other.getFieldsList())) {
            return false;
         } else if (!this.getOneofsList().equals(other.getOneofsList())) {
            return false;
         } else if (!this.getOptionsList().equals(other.getOptionsList())) {
            return false;
         } else if (this.hasSourceContext() != other.hasSourceContext()) {
            return false;
         } else if (this.hasSourceContext() && !this.getSourceContext().equals(other.getSourceContext())) {
            return false;
         } else if (this.syntax_ != other.syntax_) {
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
         hash = 53 * hash + this.getName().hashCode();
         if (this.getFieldsCount() > 0) {
            hash = 37 * hash + 2;
            hash = 53 * hash + this.getFieldsList().hashCode();
         }

         if (this.getOneofsCount() > 0) {
            hash = 37 * hash + 3;
            hash = 53 * hash + this.getOneofsList().hashCode();
         }

         if (this.getOptionsCount() > 0) {
            hash = 37 * hash + 4;
            hash = 53 * hash + this.getOptionsList().hashCode();
         }

         if (this.hasSourceContext()) {
            hash = 37 * hash + 5;
            hash = 53 * hash + this.getSourceContext().hashCode();
         }

         hash = 37 * hash + 6;
         hash = 53 * hash + this.syntax_;
         hash = 29 * hash + this.getUnknownFields().hashCode();
         this.memoizedHashCode = hash;
         return hash;
      }
   }

   public static Type parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
      return (Type)PARSER.parseFrom(data);
   }

   public static Type parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (Type)PARSER.parseFrom(data, extensionRegistry);
   }

   public static Type parseFrom(ByteString data) throws InvalidProtocolBufferException {
      return (Type)PARSER.parseFrom(data);
   }

   public static Type parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (Type)PARSER.parseFrom(data, extensionRegistry);
   }

   public static Type parseFrom(byte[] data) throws InvalidProtocolBufferException {
      return (Type)PARSER.parseFrom(data);
   }

   public static Type parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (Type)PARSER.parseFrom(data, extensionRegistry);
   }

   public static Type parseFrom(InputStream input) throws IOException {
      return (Type)GeneratedMessageV3.parseWithIOException(PARSER, input);
   }

   public static Type parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return (Type)GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
   }

   public static Type parseDelimitedFrom(InputStream input) throws IOException {
      return (Type)GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
   }

   public static Type parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return (Type)GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input, extensionRegistry);
   }

   public static Type parseFrom(CodedInputStream input) throws IOException {
      return (Type)GeneratedMessageV3.parseWithIOException(PARSER, input);
   }

   public static Type parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return (Type)GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
   }

   public Type.Builder newBuilderForType() {
      return newBuilder();
   }

   public static Type.Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
   }

   public static Type.Builder newBuilder(Type prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
   }

   public Type.Builder toBuilder() {
      return this == DEFAULT_INSTANCE ? new Type.Builder() : (new Type.Builder()).mergeFrom(this);
   }

   protected Type.Builder newBuilderForType(GeneratedMessageV3.BuilderParent parent) {
      Type.Builder builder = new Type.Builder(parent);
      return builder;
   }

   public static Type getDefaultInstance() {
      return DEFAULT_INSTANCE;
   }

   public static Parser<Type> parser() {
      return PARSER;
   }

   public Parser<Type> getParserForType() {
      return PARSER;
   }

   public Type getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
   }

   // $FF: synthetic method
   Type(GeneratedMessageV3.Builder x0, Object x1) {
      this(x0);
   }

   public static final class Builder extends GeneratedMessageV3.Builder<Type.Builder> implements TypeOrBuilder {
      private int bitField0_;
      private Object name_;
      private List<Field> fields_;
      private RepeatedFieldBuilderV3<Field, Field.Builder, FieldOrBuilder> fieldsBuilder_;
      private LazyStringList oneofs_;
      private List<Option> options_;
      private RepeatedFieldBuilderV3<Option, Option.Builder, OptionOrBuilder> optionsBuilder_;
      private SourceContext sourceContext_;
      private SingleFieldBuilderV3<SourceContext, SourceContext.Builder, SourceContextOrBuilder> sourceContextBuilder_;
      private int syntax_;

      public static final Descriptors.Descriptor getDescriptor() {
         return TypeProto.internal_static_google_protobuf_Type_descriptor;
      }

      protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
         return TypeProto.internal_static_google_protobuf_Type_fieldAccessorTable.ensureFieldAccessorsInitialized(Type.class, Type.Builder.class);
      }

      private Builder() {
         this.name_ = "";
         this.fields_ = Collections.emptyList();
         this.oneofs_ = LazyStringArrayList.EMPTY;
         this.options_ = Collections.emptyList();
         this.syntax_ = 0;
      }

      private Builder(GeneratedMessageV3.BuilderParent parent) {
         super(parent);
         this.name_ = "";
         this.fields_ = Collections.emptyList();
         this.oneofs_ = LazyStringArrayList.EMPTY;
         this.options_ = Collections.emptyList();
         this.syntax_ = 0;
      }

      public Type.Builder clear() {
         super.clear();
         this.name_ = "";
         if (this.fieldsBuilder_ == null) {
            this.fields_ = Collections.emptyList();
         } else {
            this.fields_ = null;
            this.fieldsBuilder_.clear();
         }

         this.bitField0_ &= -2;
         this.oneofs_ = LazyStringArrayList.EMPTY;
         this.bitField0_ &= -3;
         if (this.optionsBuilder_ == null) {
            this.options_ = Collections.emptyList();
         } else {
            this.options_ = null;
            this.optionsBuilder_.clear();
         }

         this.bitField0_ &= -5;
         if (this.sourceContextBuilder_ == null) {
            this.sourceContext_ = null;
         } else {
            this.sourceContext_ = null;
            this.sourceContextBuilder_ = null;
         }

         this.syntax_ = 0;
         return this;
      }

      public Descriptors.Descriptor getDescriptorForType() {
         return TypeProto.internal_static_google_protobuf_Type_descriptor;
      }

      public Type getDefaultInstanceForType() {
         return Type.getDefaultInstance();
      }

      public Type build() {
         Type result = this.buildPartial();
         if (!result.isInitialized()) {
            throw newUninitializedMessageException(result);
         } else {
            return result;
         }
      }

      public Type buildPartial() {
         Type result = new Type(this);
         int from_bitField0_ = this.bitField0_;
         result.name_ = this.name_;
         if (this.fieldsBuilder_ == null) {
            if ((this.bitField0_ & 1) != 0) {
               this.fields_ = Collections.unmodifiableList(this.fields_);
               this.bitField0_ &= -2;
            }

            result.fields_ = this.fields_;
         } else {
            result.fields_ = this.fieldsBuilder_.build();
         }

         if ((this.bitField0_ & 2) != 0) {
            this.oneofs_ = this.oneofs_.getUnmodifiableView();
            this.bitField0_ &= -3;
         }

         result.oneofs_ = this.oneofs_;
         if (this.optionsBuilder_ == null) {
            if ((this.bitField0_ & 4) != 0) {
               this.options_ = Collections.unmodifiableList(this.options_);
               this.bitField0_ &= -5;
            }

            result.options_ = this.options_;
         } else {
            result.options_ = this.optionsBuilder_.build();
         }

         if (this.sourceContextBuilder_ == null) {
            result.sourceContext_ = this.sourceContext_;
         } else {
            result.sourceContext_ = (SourceContext)this.sourceContextBuilder_.build();
         }

         result.syntax_ = this.syntax_;
         this.onBuilt();
         return result;
      }

      public Type.Builder clone() {
         return (Type.Builder)super.clone();
      }

      public Type.Builder setField(Descriptors.FieldDescriptor field, Object value) {
         return (Type.Builder)super.setField(field, value);
      }

      public Type.Builder clearField(Descriptors.FieldDescriptor field) {
         return (Type.Builder)super.clearField(field);
      }

      public Type.Builder clearOneof(Descriptors.OneofDescriptor oneof) {
         return (Type.Builder)super.clearOneof(oneof);
      }

      public Type.Builder setRepeatedField(Descriptors.FieldDescriptor field, int index, Object value) {
         return (Type.Builder)super.setRepeatedField(field, index, value);
      }

      public Type.Builder addRepeatedField(Descriptors.FieldDescriptor field, Object value) {
         return (Type.Builder)super.addRepeatedField(field, value);
      }

      public Type.Builder mergeFrom(Message other) {
         if (other instanceof Type) {
            return this.mergeFrom((Type)other);
         } else {
            super.mergeFrom(other);
            return this;
         }
      }

      public Type.Builder mergeFrom(Type other) {
         if (other == Type.getDefaultInstance()) {
            return this;
         } else {
            if (!other.getName().isEmpty()) {
               this.name_ = other.name_;
               this.onChanged();
            }

            if (this.fieldsBuilder_ == null) {
               if (!other.fields_.isEmpty()) {
                  if (this.fields_.isEmpty()) {
                     this.fields_ = other.fields_;
                     this.bitField0_ &= -2;
                  } else {
                     this.ensureFieldsIsMutable();
                     this.fields_.addAll(other.fields_);
                  }

                  this.onChanged();
               }
            } else if (!other.fields_.isEmpty()) {
               if (this.fieldsBuilder_.isEmpty()) {
                  this.fieldsBuilder_.dispose();
                  this.fieldsBuilder_ = null;
                  this.fields_ = other.fields_;
                  this.bitField0_ &= -2;
                  this.fieldsBuilder_ = GeneratedMessageV3.alwaysUseFieldBuilders ? this.getFieldsFieldBuilder() : null;
               } else {
                  this.fieldsBuilder_.addAllMessages(other.fields_);
               }
            }

            if (!other.oneofs_.isEmpty()) {
               if (this.oneofs_.isEmpty()) {
                  this.oneofs_ = other.oneofs_;
                  this.bitField0_ &= -3;
               } else {
                  this.ensureOneofsIsMutable();
                  this.oneofs_.addAll(other.oneofs_);
               }

               this.onChanged();
            }

            if (this.optionsBuilder_ == null) {
               if (!other.options_.isEmpty()) {
                  if (this.options_.isEmpty()) {
                     this.options_ = other.options_;
                     this.bitField0_ &= -5;
                  } else {
                     this.ensureOptionsIsMutable();
                     this.options_.addAll(other.options_);
                  }

                  this.onChanged();
               }
            } else if (!other.options_.isEmpty()) {
               if (this.optionsBuilder_.isEmpty()) {
                  this.optionsBuilder_.dispose();
                  this.optionsBuilder_ = null;
                  this.options_ = other.options_;
                  this.bitField0_ &= -5;
                  this.optionsBuilder_ = GeneratedMessageV3.alwaysUseFieldBuilders ? this.getOptionsFieldBuilder() : null;
               } else {
                  this.optionsBuilder_.addAllMessages(other.options_);
               }
            }

            if (other.hasSourceContext()) {
               this.mergeSourceContext(other.getSourceContext());
            }

            if (other.syntax_ != 0) {
               this.setSyntaxValue(other.getSyntaxValue());
            }

            this.mergeUnknownFields(other.getUnknownFields());
            this.onChanged();
            return this;
         }
      }

      public final boolean isInitialized() {
         return true;
      }

      public Type.Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
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
                     this.name_ = input.readStringRequireUtf8();
                     break;
                  case 18:
                     Field m = (Field)input.readMessage(Field.parser(), extensionRegistry);
                     if (this.fieldsBuilder_ == null) {
                        this.ensureFieldsIsMutable();
                        this.fields_.add(m);
                     } else {
                        this.fieldsBuilder_.addMessage(m);
                     }
                     break;
                  case 26:
                     String s = input.readStringRequireUtf8();
                     this.ensureOneofsIsMutable();
                     this.oneofs_.add((Object)s);
                     break;
                  case 34:
                     Option m = (Option)input.readMessage(Option.parser(), extensionRegistry);
                     if (this.optionsBuilder_ == null) {
                        this.ensureOptionsIsMutable();
                        this.options_.add(m);
                     } else {
                        this.optionsBuilder_.addMessage(m);
                     }
                     break;
                  case 42:
                     input.readMessage((MessageLite.Builder)this.getSourceContextFieldBuilder().getBuilder(), extensionRegistry);
                     break;
                  case 48:
                     this.syntax_ = input.readEnum();
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

      public String getName() {
         Object ref = this.name_;
         if (!(ref instanceof String)) {
            ByteString bs = (ByteString)ref;
            String s = bs.toStringUtf8();
            this.name_ = s;
            return s;
         } else {
            return (String)ref;
         }
      }

      public ByteString getNameBytes() {
         Object ref = this.name_;
         if (ref instanceof String) {
            ByteString b = ByteString.copyFromUtf8((String)ref);
            this.name_ = b;
            return b;
         } else {
            return (ByteString)ref;
         }
      }

      public Type.Builder setName(String value) {
         if (value == null) {
            throw new NullPointerException();
         } else {
            this.name_ = value;
            this.onChanged();
            return this;
         }
      }

      public Type.Builder clearName() {
         this.name_ = Type.getDefaultInstance().getName();
         this.onChanged();
         return this;
      }

      public Type.Builder setNameBytes(ByteString value) {
         if (value == null) {
            throw new NullPointerException();
         } else {
            AbstractMessageLite.checkByteStringIsUtf8(value);
            this.name_ = value;
            this.onChanged();
            return this;
         }
      }

      private void ensureFieldsIsMutable() {
         if ((this.bitField0_ & 1) == 0) {
            this.fields_ = new ArrayList(this.fields_);
            this.bitField0_ |= 1;
         }

      }

      public List<Field> getFieldsList() {
         return this.fieldsBuilder_ == null ? Collections.unmodifiableList(this.fields_) : this.fieldsBuilder_.getMessageList();
      }

      public int getFieldsCount() {
         return this.fieldsBuilder_ == null ? this.fields_.size() : this.fieldsBuilder_.getCount();
      }

      public Field getFields(int index) {
         return this.fieldsBuilder_ == null ? (Field)this.fields_.get(index) : (Field)this.fieldsBuilder_.getMessage(index);
      }

      public Type.Builder setFields(int index, Field value) {
         if (this.fieldsBuilder_ == null) {
            if (value == null) {
               throw new NullPointerException();
            }

            this.ensureFieldsIsMutable();
            this.fields_.set(index, value);
            this.onChanged();
         } else {
            this.fieldsBuilder_.setMessage(index, value);
         }

         return this;
      }

      public Type.Builder setFields(int index, Field.Builder builderForValue) {
         if (this.fieldsBuilder_ == null) {
            this.ensureFieldsIsMutable();
            this.fields_.set(index, builderForValue.build());
            this.onChanged();
         } else {
            this.fieldsBuilder_.setMessage(index, builderForValue.build());
         }

         return this;
      }

      public Type.Builder addFields(Field value) {
         if (this.fieldsBuilder_ == null) {
            if (value == null) {
               throw new NullPointerException();
            }

            this.ensureFieldsIsMutable();
            this.fields_.add(value);
            this.onChanged();
         } else {
            this.fieldsBuilder_.addMessage(value);
         }

         return this;
      }

      public Type.Builder addFields(int index, Field value) {
         if (this.fieldsBuilder_ == null) {
            if (value == null) {
               throw new NullPointerException();
            }

            this.ensureFieldsIsMutable();
            this.fields_.add(index, value);
            this.onChanged();
         } else {
            this.fieldsBuilder_.addMessage(index, value);
         }

         return this;
      }

      public Type.Builder addFields(Field.Builder builderForValue) {
         if (this.fieldsBuilder_ == null) {
            this.ensureFieldsIsMutable();
            this.fields_.add(builderForValue.build());
            this.onChanged();
         } else {
            this.fieldsBuilder_.addMessage(builderForValue.build());
         }

         return this;
      }

      public Type.Builder addFields(int index, Field.Builder builderForValue) {
         if (this.fieldsBuilder_ == null) {
            this.ensureFieldsIsMutable();
            this.fields_.add(index, builderForValue.build());
            this.onChanged();
         } else {
            this.fieldsBuilder_.addMessage(index, builderForValue.build());
         }

         return this;
      }

      public Type.Builder addAllFields(Iterable<? extends Field> values) {
         if (this.fieldsBuilder_ == null) {
            this.ensureFieldsIsMutable();
            AbstractMessageLite.Builder.addAll(values, this.fields_);
            this.onChanged();
         } else {
            this.fieldsBuilder_.addAllMessages(values);
         }

         return this;
      }

      public Type.Builder clearFields() {
         if (this.fieldsBuilder_ == null) {
            this.fields_ = Collections.emptyList();
            this.bitField0_ &= -2;
            this.onChanged();
         } else {
            this.fieldsBuilder_.clear();
         }

         return this;
      }

      public Type.Builder removeFields(int index) {
         if (this.fieldsBuilder_ == null) {
            this.ensureFieldsIsMutable();
            this.fields_.remove(index);
            this.onChanged();
         } else {
            this.fieldsBuilder_.remove(index);
         }

         return this;
      }

      public Field.Builder getFieldsBuilder(int index) {
         return (Field.Builder)this.getFieldsFieldBuilder().getBuilder(index);
      }

      public FieldOrBuilder getFieldsOrBuilder(int index) {
         return this.fieldsBuilder_ == null ? (FieldOrBuilder)this.fields_.get(index) : (FieldOrBuilder)this.fieldsBuilder_.getMessageOrBuilder(index);
      }

      public List<? extends FieldOrBuilder> getFieldsOrBuilderList() {
         return this.fieldsBuilder_ != null ? this.fieldsBuilder_.getMessageOrBuilderList() : Collections.unmodifiableList(this.fields_);
      }

      public Field.Builder addFieldsBuilder() {
         return (Field.Builder)this.getFieldsFieldBuilder().addBuilder(Field.getDefaultInstance());
      }

      public Field.Builder addFieldsBuilder(int index) {
         return (Field.Builder)this.getFieldsFieldBuilder().addBuilder(index, Field.getDefaultInstance());
      }

      public List<Field.Builder> getFieldsBuilderList() {
         return this.getFieldsFieldBuilder().getBuilderList();
      }

      private RepeatedFieldBuilderV3<Field, Field.Builder, FieldOrBuilder> getFieldsFieldBuilder() {
         if (this.fieldsBuilder_ == null) {
            this.fieldsBuilder_ = new RepeatedFieldBuilderV3(this.fields_, (this.bitField0_ & 1) != 0, this.getParentForChildren(), this.isClean());
            this.fields_ = null;
         }

         return this.fieldsBuilder_;
      }

      private void ensureOneofsIsMutable() {
         if ((this.bitField0_ & 2) == 0) {
            this.oneofs_ = new LazyStringArrayList(this.oneofs_);
            this.bitField0_ |= 2;
         }

      }

      public ProtocolStringList getOneofsList() {
         return this.oneofs_.getUnmodifiableView();
      }

      public int getOneofsCount() {
         return this.oneofs_.size();
      }

      public String getOneofs(int index) {
         return (String)this.oneofs_.get(index);
      }

      public ByteString getOneofsBytes(int index) {
         return this.oneofs_.getByteString(index);
      }

      public Type.Builder setOneofs(int index, String value) {
         if (value == null) {
            throw new NullPointerException();
         } else {
            this.ensureOneofsIsMutable();
            this.oneofs_.set(index, (Object)value);
            this.onChanged();
            return this;
         }
      }

      public Type.Builder addOneofs(String value) {
         if (value == null) {
            throw new NullPointerException();
         } else {
            this.ensureOneofsIsMutable();
            this.oneofs_.add((Object)value);
            this.onChanged();
            return this;
         }
      }

      public Type.Builder addAllOneofs(Iterable<String> values) {
         this.ensureOneofsIsMutable();
         AbstractMessageLite.Builder.addAll(values, (List)this.oneofs_);
         this.onChanged();
         return this;
      }

      public Type.Builder clearOneofs() {
         this.oneofs_ = LazyStringArrayList.EMPTY;
         this.bitField0_ &= -3;
         this.onChanged();
         return this;
      }

      public Type.Builder addOneofsBytes(ByteString value) {
         if (value == null) {
            throw new NullPointerException();
         } else {
            AbstractMessageLite.checkByteStringIsUtf8(value);
            this.ensureOneofsIsMutable();
            this.oneofs_.add(value);
            this.onChanged();
            return this;
         }
      }

      private void ensureOptionsIsMutable() {
         if ((this.bitField0_ & 4) == 0) {
            this.options_ = new ArrayList(this.options_);
            this.bitField0_ |= 4;
         }

      }

      public List<Option> getOptionsList() {
         return this.optionsBuilder_ == null ? Collections.unmodifiableList(this.options_) : this.optionsBuilder_.getMessageList();
      }

      public int getOptionsCount() {
         return this.optionsBuilder_ == null ? this.options_.size() : this.optionsBuilder_.getCount();
      }

      public Option getOptions(int index) {
         return this.optionsBuilder_ == null ? (Option)this.options_.get(index) : (Option)this.optionsBuilder_.getMessage(index);
      }

      public Type.Builder setOptions(int index, Option value) {
         if (this.optionsBuilder_ == null) {
            if (value == null) {
               throw new NullPointerException();
            }

            this.ensureOptionsIsMutable();
            this.options_.set(index, value);
            this.onChanged();
         } else {
            this.optionsBuilder_.setMessage(index, value);
         }

         return this;
      }

      public Type.Builder setOptions(int index, Option.Builder builderForValue) {
         if (this.optionsBuilder_ == null) {
            this.ensureOptionsIsMutable();
            this.options_.set(index, builderForValue.build());
            this.onChanged();
         } else {
            this.optionsBuilder_.setMessage(index, builderForValue.build());
         }

         return this;
      }

      public Type.Builder addOptions(Option value) {
         if (this.optionsBuilder_ == null) {
            if (value == null) {
               throw new NullPointerException();
            }

            this.ensureOptionsIsMutable();
            this.options_.add(value);
            this.onChanged();
         } else {
            this.optionsBuilder_.addMessage(value);
         }

         return this;
      }

      public Type.Builder addOptions(int index, Option value) {
         if (this.optionsBuilder_ == null) {
            if (value == null) {
               throw new NullPointerException();
            }

            this.ensureOptionsIsMutable();
            this.options_.add(index, value);
            this.onChanged();
         } else {
            this.optionsBuilder_.addMessage(index, value);
         }

         return this;
      }

      public Type.Builder addOptions(Option.Builder builderForValue) {
         if (this.optionsBuilder_ == null) {
            this.ensureOptionsIsMutable();
            this.options_.add(builderForValue.build());
            this.onChanged();
         } else {
            this.optionsBuilder_.addMessage(builderForValue.build());
         }

         return this;
      }

      public Type.Builder addOptions(int index, Option.Builder builderForValue) {
         if (this.optionsBuilder_ == null) {
            this.ensureOptionsIsMutable();
            this.options_.add(index, builderForValue.build());
            this.onChanged();
         } else {
            this.optionsBuilder_.addMessage(index, builderForValue.build());
         }

         return this;
      }

      public Type.Builder addAllOptions(Iterable<? extends Option> values) {
         if (this.optionsBuilder_ == null) {
            this.ensureOptionsIsMutable();
            AbstractMessageLite.Builder.addAll(values, this.options_);
            this.onChanged();
         } else {
            this.optionsBuilder_.addAllMessages(values);
         }

         return this;
      }

      public Type.Builder clearOptions() {
         if (this.optionsBuilder_ == null) {
            this.options_ = Collections.emptyList();
            this.bitField0_ &= -5;
            this.onChanged();
         } else {
            this.optionsBuilder_.clear();
         }

         return this;
      }

      public Type.Builder removeOptions(int index) {
         if (this.optionsBuilder_ == null) {
            this.ensureOptionsIsMutable();
            this.options_.remove(index);
            this.onChanged();
         } else {
            this.optionsBuilder_.remove(index);
         }

         return this;
      }

      public Option.Builder getOptionsBuilder(int index) {
         return (Option.Builder)this.getOptionsFieldBuilder().getBuilder(index);
      }

      public OptionOrBuilder getOptionsOrBuilder(int index) {
         return this.optionsBuilder_ == null ? (OptionOrBuilder)this.options_.get(index) : (OptionOrBuilder)this.optionsBuilder_.getMessageOrBuilder(index);
      }

      public List<? extends OptionOrBuilder> getOptionsOrBuilderList() {
         return this.optionsBuilder_ != null ? this.optionsBuilder_.getMessageOrBuilderList() : Collections.unmodifiableList(this.options_);
      }

      public Option.Builder addOptionsBuilder() {
         return (Option.Builder)this.getOptionsFieldBuilder().addBuilder(Option.getDefaultInstance());
      }

      public Option.Builder addOptionsBuilder(int index) {
         return (Option.Builder)this.getOptionsFieldBuilder().addBuilder(index, Option.getDefaultInstance());
      }

      public List<Option.Builder> getOptionsBuilderList() {
         return this.getOptionsFieldBuilder().getBuilderList();
      }

      private RepeatedFieldBuilderV3<Option, Option.Builder, OptionOrBuilder> getOptionsFieldBuilder() {
         if (this.optionsBuilder_ == null) {
            this.optionsBuilder_ = new RepeatedFieldBuilderV3(this.options_, (this.bitField0_ & 4) != 0, this.getParentForChildren(), this.isClean());
            this.options_ = null;
         }

         return this.optionsBuilder_;
      }

      public boolean hasSourceContext() {
         return this.sourceContextBuilder_ != null || this.sourceContext_ != null;
      }

      public SourceContext getSourceContext() {
         if (this.sourceContextBuilder_ == null) {
            return this.sourceContext_ == null ? SourceContext.getDefaultInstance() : this.sourceContext_;
         } else {
            return (SourceContext)this.sourceContextBuilder_.getMessage();
         }
      }

      public Type.Builder setSourceContext(SourceContext value) {
         if (this.sourceContextBuilder_ == null) {
            if (value == null) {
               throw new NullPointerException();
            }

            this.sourceContext_ = value;
            this.onChanged();
         } else {
            this.sourceContextBuilder_.setMessage(value);
         }

         return this;
      }

      public Type.Builder setSourceContext(SourceContext.Builder builderForValue) {
         if (this.sourceContextBuilder_ == null) {
            this.sourceContext_ = builderForValue.build();
            this.onChanged();
         } else {
            this.sourceContextBuilder_.setMessage(builderForValue.build());
         }

         return this;
      }

      public Type.Builder mergeSourceContext(SourceContext value) {
         if (this.sourceContextBuilder_ == null) {
            if (this.sourceContext_ != null) {
               this.sourceContext_ = SourceContext.newBuilder(this.sourceContext_).mergeFrom(value).buildPartial();
            } else {
               this.sourceContext_ = value;
            }

            this.onChanged();
         } else {
            this.sourceContextBuilder_.mergeFrom(value);
         }

         return this;
      }

      public Type.Builder clearSourceContext() {
         if (this.sourceContextBuilder_ == null) {
            this.sourceContext_ = null;
            this.onChanged();
         } else {
            this.sourceContext_ = null;
            this.sourceContextBuilder_ = null;
         }

         return this;
      }

      public SourceContext.Builder getSourceContextBuilder() {
         this.onChanged();
         return (SourceContext.Builder)this.getSourceContextFieldBuilder().getBuilder();
      }

      public SourceContextOrBuilder getSourceContextOrBuilder() {
         if (this.sourceContextBuilder_ != null) {
            return (SourceContextOrBuilder)this.sourceContextBuilder_.getMessageOrBuilder();
         } else {
            return this.sourceContext_ == null ? SourceContext.getDefaultInstance() : this.sourceContext_;
         }
      }

      private SingleFieldBuilderV3<SourceContext, SourceContext.Builder, SourceContextOrBuilder> getSourceContextFieldBuilder() {
         if (this.sourceContextBuilder_ == null) {
            this.sourceContextBuilder_ = new SingleFieldBuilderV3(this.getSourceContext(), this.getParentForChildren(), this.isClean());
            this.sourceContext_ = null;
         }

         return this.sourceContextBuilder_;
      }

      public int getSyntaxValue() {
         return this.syntax_;
      }

      public Type.Builder setSyntaxValue(int value) {
         this.syntax_ = value;
         this.onChanged();
         return this;
      }

      public Syntax getSyntax() {
         Syntax result = Syntax.valueOf(this.syntax_);
         return result == null ? Syntax.UNRECOGNIZED : result;
      }

      public Type.Builder setSyntax(Syntax value) {
         if (value == null) {
            throw new NullPointerException();
         } else {
            this.syntax_ = value.getNumber();
            this.onChanged();
            return this;
         }
      }

      public Type.Builder clearSyntax() {
         this.syntax_ = 0;
         this.onChanged();
         return this;
      }

      public final Type.Builder setUnknownFields(UnknownFieldSet unknownFields) {
         return (Type.Builder)super.setUnknownFields(unknownFields);
      }

      public final Type.Builder mergeUnknownFields(UnknownFieldSet unknownFields) {
         return (Type.Builder)super.mergeUnknownFields(unknownFields);
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
