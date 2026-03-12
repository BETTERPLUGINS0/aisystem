package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.color.AlphaColor;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.color.Color;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTByte;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTByteArray;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTDouble;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTFloat;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTIntArray;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTList;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTLong;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTLongArray;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTNumber;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Either;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.UniqueIdUtil;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.IRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ApiStatus.Experimental
public final class NbtCodecs {
   public static final NbtCodec<Integer> INT = new NbtCodec<Integer>() {
      public Integer decode(NBT nbt, PacketWrapper<?> wrapper) {
         return ((NBTNumber)nbt.castOrThrow(NBTNumber.class)).getAsInt();
      }

      public NBT encode(PacketWrapper<?> wrapper, Integer value) {
         return new NBTInt(value);
      }
   };
   public static final NbtCodec<Double> DOUBLE = new NbtCodec<Double>() {
      public Double decode(NBT nbt, PacketWrapper<?> wrapper) {
         return ((NBTNumber)nbt.castOrThrow(NBTNumber.class)).getAsDouble();
      }

      public NBT encode(PacketWrapper<?> wrapper, Double value) {
         return new NBTDouble(value);
      }
   };
   public static final NbtCodec<Float> FLOAT = new NbtCodec<Float>() {
      public Float decode(NBT nbt, PacketWrapper<?> wrapper) {
         return ((NBTNumber)nbt.castOrThrow(NBTNumber.class)).getAsFloat();
      }

      public NBT encode(PacketWrapper<?> wrapper, Float value) {
         return new NBTFloat(value);
      }
   };
   public static final NbtCodec<Boolean> BOOLEAN = new NbtCodec<Boolean>() {
      public Boolean decode(NBT nbt, PacketWrapper<?> wrapper) {
         return ((NBTNumber)nbt.castOrThrow(NBTNumber.class)).getAsByte() != 0;
      }

      public NBT encode(PacketWrapper<?> wrapper, Boolean value) {
         return new NBTByte(value);
      }
   };
   public static final NbtCodec<String> STRING = new NbtCodec<String>() {
      public String decode(NBT nbt, PacketWrapper<?> wrapper) {
         return ((NBTString)nbt.castOrThrow(NBTString.class)).getValue();
      }

      public NBT encode(PacketWrapper<?> wrapper, String value) {
         return new NBTString(value);
      }
   };
   public static final NbtCodec<List<String>> STRING_LIST;
   public static final NbtCodec<List<? extends NBT>> GENERIC_LIST;
   public static final NbtCodec<int[]> INT_ARRAY;
   public static final NbtCodec<UUID> UUID;
   public static final NbtCodec<UUID> STRING_UUID;
   public static final NbtCodec<UUID> LENIENT_UUID;
   public static final NbtCodec<Color> RGB_COLOR;
   public static final NbtCodec<AlphaColor> ARGB_COLOR;
   public static final NbtCodec<NBT> NOOP;
   private static final NbtCodec<?> ERROR_CODEC;

   private NbtCodecs() {
   }

   public static <T> NbtCodec<T> errorCodec() {
      return ERROR_CODEC;
   }

   public static <T extends Enum<T> & CodecNameable> NbtCodec<T> forEnum(T[] values) {
      return new NbtCodec<T>() {
         private final Map<String, T> map = new HashMap(values.length);

         {
            Enum[] var2 = values;
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               T value = var2[var4];
               T existingValue = (Enum)this.map.putIfAbsent(((CodecNameable)value).getCodecName(), value);
               if (existingValue != null) {
                  throw new IllegalStateException("Can't create codec for enum with duplicate names: " + existingValue);
               }
            }

         }

         public T decode(NBT nbt, PacketWrapper<?> wrapper) {
            String key = ((NBTString)nbt).getValue();
            T value = (Enum)this.map.get(key);
            if (value == null) {
               throw new NbtCodecException("Can't find " + key + " in " + this.map.keySet());
            } else {
               return value;
            }
         }

         public NBT encode(PacketWrapper<?> wrapper, T value) {
            return new NBTString(((CodecNameable)value).getCodecName());
         }
      };
   }

   public static <T extends MappedEntity> NbtCodec<T> forRegistry(IRegistry<T> registry) {
      return new NbtCodec<T>() {
         public T decode(NBT nbt, PacketWrapper<?> wrapper) {
            IRegistry<T> replacedRegistry = wrapper.replaceRegistry(registry);
            T entry = null;
            if (nbt instanceof NBTNumber) {
               ClientVersion version = wrapper.getServerVersion().toClientVersion();
               int id = ((NBTNumber)nbt).getAsInt();
               entry = replacedRegistry.getById(version, id);
            } else if (nbt instanceof NBTString) {
               entry = replacedRegistry.getByName(((NBTString)nbt).getValue());
            }

            if (entry == null) {
               throw new NbtCodecException("Can't decode registry " + registry.getRegistryKey());
            } else {
               return entry;
            }
         }

         public NBT encode(PacketWrapper<?> wrapper, T value) {
            if (!value.isRegistered()) {
               throw new NbtCodecException("Unregistered entry");
            } else {
               return ResourceLocation.CODEC.encode(wrapper, value.getName());
            }
         }
      };
   }

   public static <L, R> NbtCodec<Either<L, R>> either(NbtCodec<L> left, NbtCodec<R> right) {
      return new NbtCodec<Either<L, R>>() {
         public Either<L, R> decode(NBT nbt, PacketWrapper<?> wrapper) throws NbtCodecException {
            try {
               return Either.createLeft(left.decode(nbt, wrapper));
            } catch (NbtCodecException var6) {
               try {
                  return Either.createRight(right.decode(nbt, wrapper));
               } catch (NbtCodecException var5) {
                  var5.addSuppressed(var6);
                  throw var5;
               }
            }
         }

         public NBT encode(PacketWrapper<?> wrapper, Either<L, R> value) throws NbtCodecException {
            return (NBT)value.map((v) -> {
               return left.encode(wrapper, v);
            }, (v) -> {
               return right.encode(wrapper, v);
            });
         }
      };
   }

   static {
      STRING_LIST = STRING.applyList();
      GENERIC_LIST = new NbtCodec<List<? extends NBT>>() {
         public List<? extends NBT> decode(NBT nbt, PacketWrapper<?> wrapper) {
            if (nbt instanceof NBTList) {
               return ((NBTList)nbt).unwrapTags();
            } else {
               ArrayList list;
               int var6;
               int var7;
               if (nbt instanceof NBTIntArray) {
                  int[] arr = ((NBTIntArray)nbt).getValue();
                  list = new ArrayList(arr.length);
                  int[] var13 = arr;
                  var6 = arr.length;

                  for(var7 = 0; var7 < var6; ++var7) {
                     int numx = var13[var7];
                     list.add(new NBTInt(numx));
                  }

                  return list;
               } else if (nbt instanceof NBTByteArray) {
                  byte[] arrxx = ((NBTByteArray)nbt).getValue();
                  list = new ArrayList(arrxx.length);
                  byte[] var12 = arrxx;
                  var6 = arrxx.length;

                  for(var7 = 0; var7 < var6; ++var7) {
                     byte num = var12[var7];
                     list.add(new NBTByte(num));
                  }

                  return list;
               } else if (!(nbt instanceof NBTLongArray)) {
                  throw new NbtCodecException("Not a list: " + nbt);
               } else {
                  long[] arrx = ((NBTLongArray)nbt).getValue();
                  list = new ArrayList(arrx.length);
                  long[] var5 = arrx;
                  var6 = arrx.length;

                  for(var7 = 0; var7 < var6; ++var7) {
                     long numxx = var5[var7];
                     list.add(new NBTLong(numxx));
                  }

                  return list;
               }
            }
         }

         public NBT encode(PacketWrapper<?> wrapper, List<? extends NBT> value) {
            if (value.isEmpty()) {
               return new NBTList(NBTType.END, 0);
            } else {
               NBTType<?> type = NBTList.getCommonTagType(value);
               NBTList list;
               if (type == NBTType.COMPOUND) {
                  list = new NBTList(NBTType.COMPOUND, value.size());
                  Iterator var10 = value.iterator();

                  while(var10.hasNext()) {
                     NBT tag = (NBT)var10.next();
                     list.addTagOrWrap(tag);
                  }

                  return list;
               } else {
                  int i;
                  if (type == NBTType.INT) {
                     int[] arrx = new int[value.size()];

                     for(i = 0; i < arrx.length; ++i) {
                        arrx[i] = ((NBTInt)value.get(i)).getAsInt();
                     }

                     return new NBTIntArray(arrx);
                  } else if (type == NBTType.BYTE) {
                     byte[] arr = new byte[value.size()];

                     for(i = 0; i < arr.length; ++i) {
                        arr[i] = ((NBTByte)value.get(i)).getAsByte();
                     }

                     return new NBTByteArray(arr);
                  } else if (type != NBTType.LONG) {
                     list = new NBTList(type, value);
                     return list;
                  } else {
                     long[] arrxx = new long[value.size()];

                     for(i = 0; i < arrxx.length; ++i) {
                        arrxx[i] = ((NBTLong)value.get(i)).getAsLong();
                     }

                     return new NBTLongArray(arrxx);
                  }
               }
            }
         }
      };
      INT_ARRAY = new NbtCodec<int[]>() {
         public int[] decode(NBT nbt, PacketWrapper<?> wrapper) {
            if (nbt instanceof NBTIntArray) {
               return ((NBTIntArray)nbt).getValue();
            } else {
               List<? extends NBT> list = (List)NbtCodecs.GENERIC_LIST.decode(nbt, wrapper);
               int size = list.size();
               int[] array = new int[size];

               for(int i = 0; i < size; ++i) {
                  array[i] = ((NBTNumber)((NBT)list.get(i)).castOrThrow(NBTNumber.class)).getAsInt();
               }

               return array;
            }
         }

         public NBT encode(PacketWrapper<?> wrapper, int[] value) {
            return new NBTIntArray(value);
         }
      };
      UUID = INT_ARRAY.apply(UniqueIdUtil::fromIntArray, UniqueIdUtil::toIntArray);
      STRING_UUID = new NbtCodec<UUID>() {
         public UUID decode(NBT nbt, PacketWrapper<?> wrapper) throws NbtCodecException {
            String uuidStr = ((NBTString)nbt).getValue();
            if (uuidStr.length() == 36) {
               try {
                  return java.util.UUID.fromString(uuidStr);
               } catch (IllegalArgumentException var5) {
               }
            }

            throw new NbtCodecException("Invalid UUID " + uuidStr);
         }

         public NBT encode(PacketWrapper<?> wrapper, UUID value) throws NbtCodecException {
            return new NBTString(value.toString());
         }
      };
      LENIENT_UUID = UUID.withAlternative(STRING_UUID);
      RGB_COLOR = new NbtCodec<Color>() {
         public Color decode(NBT nbt, PacketWrapper<?> wrapper) {
            if (nbt instanceof NBTString) {
               String string = ((NBTString)nbt).getValue();
               if (!string.isEmpty() && string.charAt(0) == '#') {
                  if (string.length() - 1 != 6) {
                     throw new NbtCodecException("Hex color is wrong, expected 6 digits but got " + string);
                  } else {
                     try {
                        String digits = string.substring(1);
                        int rgb = Integer.parseInt(digits, 16);
                        return new Color(rgb);
                     } catch (NumberFormatException var6) {
                        throw new NbtCodecException(var6);
                     }
                  }
               } else {
                  throw new NbtCodecException("Hex color must begin with #");
               }
            } else {
               return nbt instanceof NBTNumber ? new Color(((NBTNumber)nbt).getAsInt()) : Color.WHITE;
            }
         }

         public NBT encode(PacketWrapper<?> wrapper, Color value) {
            return new NBTInt(value.asRGB());
         }
      };
      ARGB_COLOR = new NbtCodec<AlphaColor>() {
         public AlphaColor decode(NBT nbt, PacketWrapper<?> wrapper) {
            if (nbt instanceof NBTString) {
               String string = ((NBTString)nbt).getValue();
               if (!string.isEmpty() && string.charAt(0) == '#') {
                  if (string.length() - 1 != 8) {
                     throw new NbtCodecException("Hex color is wrong, expected 8 digits but got " + string);
                  } else {
                     try {
                        String digits = string.substring(1);
                        int rgb = Integer.parseUnsignedInt(digits, 16);
                        return new AlphaColor(rgb);
                     } catch (NumberFormatException var6) {
                        throw new NbtCodecException(var6);
                     }
                  }
               } else {
                  throw new NbtCodecException("Hex color must begin with #");
               }
            } else {
               return nbt instanceof NBTNumber ? new AlphaColor(((NBTNumber)nbt).getAsInt()) : AlphaColor.WHITE;
            }
         }

         public NBT encode(PacketWrapper<?> wrapper, AlphaColor value) {
            return new NBTInt(value.asRGB());
         }
      };
      NOOP = new NbtCodec<NBT>() {
         public NBT decode(NBT nbt, PacketWrapper<?> wrapper) {
            return nbt;
         }

         public NBT encode(PacketWrapper<?> wrapper, NBT value) {
            return value;
         }
      };
      ERROR_CODEC = new NbtCodec<Object>() {
         public Object decode(NBT nbt, PacketWrapper<?> wrapper) {
            throw new UnsupportedOperationException();
         }

         public NBT encode(PacketWrapper<?> wrapper, Object value) {
            throw new UnsupportedOperationException();
         }
      };
   }
}
