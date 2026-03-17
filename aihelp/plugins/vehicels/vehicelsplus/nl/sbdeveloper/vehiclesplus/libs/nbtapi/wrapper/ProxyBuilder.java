/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.libs.nbtapi.wrapper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTType;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NbtApiException;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.iface.NBTHandler;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.iface.ReadWriteNBT;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.wrapper.Casing;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.wrapper.DefaultMethodInvoker;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.wrapper.NBTProxy;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.wrapper.NBTTarget;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.wrapper.ProxiedList;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.wrapper.ProxyList;

public class ProxyBuilder<T extends NBTProxy>
implements InvocationHandler {
    private static final Map<Method, Function<Arguments, Object>> METHOD_CACHE = new ConcurrentHashMap<Method, Function<Arguments, Object>>();
    private final Class<T> target;
    private final ReadWriteNBT nbt;
    private boolean readOnly;

    public ProxyBuilder(ReadWriteNBT readWriteNBT, Class<T> clazz) {
        if (!clazz.isInterface()) {
            throw new NbtApiException("A proxy can only be built from an interface! Check the wiki for examples.");
        }
        this.target = clazz;
        this.nbt = readWriteNBT;
    }

    public ProxyBuilder<T> readOnly() {
        this.readOnly = true;
        return this;
    }

    public T build() {
        NBTProxy nBTProxy = (NBTProxy)Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{this.target}, this);
        nBTProxy.init();
        return (T)nBTProxy;
    }

    @Override
    public Object invoke(Object object, Method method2, Object[] objectArray) {
        METHOD_CACHE.computeIfAbsent(method2, method -> ProxyBuilder.createFunction((NBTProxy)object, method));
        return METHOD_CACHE.get(method2).apply(new Arguments(this.target, (NBTProxy)object, this.readOnly, this.nbt, objectArray));
    }

    private static Function<Arguments, Object> createFunction(NBTProxy nBTProxy, Method method) {
        if ("toString".equals(method.getName()) && method.getParameterCount() == 0 && method.getReturnType() == String.class) {
            return arguments -> arguments.nbt.toString();
        }
        if (method.isDefault()) {
            return arguments -> DefaultMethodInvoker.invokeDefault(arguments.target, arguments.proxy, method, arguments.args);
        }
        NBTTarget.Type type = ProxyBuilder.getAction(method);
        if (type == NBTTarget.Type.SET) {
            String string = ProxyBuilder.getNBTName(nBTProxy.getCasing(), method);
            return arguments -> {
                if (arguments.readOnly) {
                    throw new NbtApiException("Tried calling a set method on a read only object.");
                }
                return ProxyBuilder.setNBT(arguments.nbt, arguments.proxy, string, arguments.args[0]);
            };
        }
        if (type == NBTTarget.Type.GET) {
            NBTHandler<?> nBTHandler;
            Class<?> clazz = method.getReturnType();
            String string = ProxyBuilder.getNBTName(nBTProxy.getCasing(), method);
            if (clazz.isInterface() && NBTProxy.class.isAssignableFrom(clazz)) {
                return arguments -> {
                    if (arguments.nbt.hasTag(string) && arguments.nbt.getType(string) != NBTType.NBTTagCompound) {
                        throw new NbtApiException("Tried getting a '" + clazz + "' proxy from the field '" + string + "', but it's not a TagCompound!");
                    }
                    return new ProxyBuilder(arguments.nbt.getOrCreateCompound(string), clazz).build();
                };
            }
            if (clazz == ProxyList.class && (nBTHandler = (Class)((ParameterizedType)method.getGenericReturnType()).getActualTypeArguments()[0]) != null && ((Class)((Object)nBTHandler)).isInterface() && NBTProxy.class.isAssignableFrom((Class<?>)((Object)nBTHandler))) {
                return arg_0 -> ProxyBuilder.lambda$createFunction$5(string, (Class)((Object)nBTHandler), arg_0);
            }
            nBTHandler = nBTProxy.getHandler(clazz);
            if (nBTHandler != null) {
                return arguments -> nBTHandler.get(arguments.nbt, string);
            }
            return arguments -> arguments.nbt.getOrNull(string, clazz);
        }
        if (type == NBTTarget.Type.HAS) {
            String string = ProxyBuilder.getNBTName(nBTProxy.getCasing(), method);
            return arguments -> arguments.nbt.hasTag(string);
        }
        throw new IllegalArgumentException("The method '" + method.getName() + "' in '" + method.getDeclaringClass().getName() + "' can not be handled by the NBT-API. Please check the Wiki for examples!");
    }

    private static NBTTarget.Type getAction(Method method) {
        NBTTarget nBTTarget = method.getAnnotation(NBTTarget.class);
        if (nBTTarget != null) {
            if (nBTTarget.type() == NBTTarget.Type.HAS && method.getParameterCount() == 0 && method.getReturnType() == Boolean.TYPE) {
                return NBTTarget.Type.HAS;
            }
            if (nBTTarget.type() == NBTTarget.Type.GET && method.getParameterCount() == 0) {
                return NBTTarget.Type.GET;
            }
            if (nBTTarget.type() == NBTTarget.Type.SET && method.getParameterCount() == 1) {
                return NBTTarget.Type.SET;
            }
        }
        if (method.getName().startsWith("set") && method.getParameterCount() == 1) {
            return NBTTarget.Type.SET;
        }
        if (method.getName().startsWith("get") && method.getParameterCount() == 0) {
            return NBTTarget.Type.GET;
        }
        if (method.getName().startsWith("has") && method.getParameterCount() == 0 && method.getReturnType() == Boolean.TYPE) {
            return NBTTarget.Type.HAS;
        }
        return null;
    }

    private static String getNBTName(Casing casing, Method method) {
        NBTTarget nBTTarget = method.getAnnotation(NBTTarget.class);
        if (nBTTarget != null) {
            return nBTTarget.value();
        }
        return casing.convertString(method.getName().substring(3));
    }

    private static Object setNBT(ReadWriteNBT readWriteNBT, NBTProxy nBTProxy, String string, Object object) {
        if (object == null) {
            readWriteNBT.removeKey(string);
        } else if (object instanceof Boolean) {
            readWriteNBT.setBoolean(string, (Boolean)object);
        } else if (object instanceof Byte) {
            readWriteNBT.setByte(string, (Byte)object);
        } else if (object instanceof Short) {
            readWriteNBT.setShort(string, (Short)object);
        } else if (object instanceof Integer) {
            readWriteNBT.setInteger(string, (Integer)object);
        } else if (object instanceof Long) {
            readWriteNBT.setLong(string, (Long)object);
        } else if (object instanceof Float) {
            readWriteNBT.setFloat(string, (Float)object);
        } else if (object instanceof Double) {
            readWriteNBT.setDouble(string, (Double)object);
        } else if (object instanceof byte[]) {
            readWriteNBT.setByteArray(string, (byte[])object);
        } else if (object instanceof int[]) {
            readWriteNBT.setIntArray(string, (int[])object);
        } else if (object instanceof long[]) {
            readWriteNBT.setLongArray(string, (long[])object);
        } else if (object instanceof String) {
            readWriteNBT.setString(string, (String)object);
        } else if (object instanceof UUID) {
            readWriteNBT.setUUID(string, (UUID)object);
        } else if (object.getClass().isEnum()) {
            readWriteNBT.setEnum(string, (Enum)object);
        } else {
            NBTHandler<?> nBTHandler = nBTProxy.getHandler(object.getClass());
            if (nBTHandler != null) {
                nBTHandler.set(readWriteNBT, string, object);
            } else {
                for (NBTHandler<Object> nBTHandler2 : nBTProxy.getHandlers()) {
                    if (!nBTHandler2.fuzzyMatch(object)) continue;
                    nBTHandler2.set(readWriteNBT, string, object);
                    return null;
                }
                throw new IllegalArgumentException("Tried setting an object of type '" + object.getClass().getName() + "'. This is not a supported NBT value. Please check the Wiki for examples!");
            }
        }
        return null;
    }

    private static /* synthetic */ Object lambda$createFunction$5(String string, Class clazz, Arguments arguments) {
        return new ProxiedList(arguments.nbt.getCompoundList(string), clazz);
    }

    private static class Arguments {
        Class<?> target;
        NBTProxy proxy;
        ReadWriteNBT nbt;
        Object[] args;
        boolean readOnly;

        public Arguments(Class<?> clazz, NBTProxy nBTProxy, boolean bl, ReadWriteNBT readWriteNBT, Object[] objectArray) {
            this.target = clazz;
            this.proxy = nBTProxy;
            this.nbt = readWriteNBT;
            this.args = objectArray;
            this.readOnly = bl;
        }
    }
}

