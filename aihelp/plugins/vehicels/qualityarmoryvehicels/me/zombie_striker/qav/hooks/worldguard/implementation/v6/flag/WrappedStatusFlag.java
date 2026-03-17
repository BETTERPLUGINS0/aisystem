/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.sk89q.worldguard.protection.flags.Flag
 *  com.sk89q.worldguard.protection.flags.StateFlag$State
 */
package me.zombie_striker.qav.hooks.worldguard.implementation.v6.flag;

import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import java.util.Optional;
import me.zombie_striker.qav.hooks.worldguard.flag.IWrappedStatusFlag;
import me.zombie_striker.qav.hooks.worldguard.flag.WrappedState;
import me.zombie_striker.qav.hooks.worldguard.implementation.v6.flag.AbstractWrappedFlag;

public class WrappedStatusFlag
extends AbstractWrappedFlag<WrappedState>
implements IWrappedStatusFlag {
    public WrappedStatusFlag(Flag<StateFlag.State> flag) {
        super(flag);
    }

    @Override
    public Optional<WrappedState> fromWGValue(Object object2) {
        return Optional.ofNullable(object2).map(object -> object == StateFlag.State.ALLOW ? WrappedState.ALLOW : WrappedState.DENY);
    }

    @Override
    public Optional<Object> fromWrapperValue(WrappedState wrappedState2) {
        return Optional.ofNullable(wrappedState2).map(wrappedState -> wrappedState == WrappedState.ALLOW ? StateFlag.State.ALLOW : StateFlag.State.DENY);
    }
}

