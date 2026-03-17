/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  dev.triumphteam.nova.MutableState
 *  dev.triumphteam.nova.State
 *  dev.triumphteam.nova.builtin.EmptyState
 *  dev.triumphteam.nova.builtin.SimpleMutableState
 *  dev.triumphteam.nova.policy.StateMutationPolicy
 *  dev.triumphteam.nova.policy.StateMutationPolicy$StructuralEquality
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package me.zombie_striker.qav.gui.component.functional;

import dev.triumphteam.nova.MutableState;
import dev.triumphteam.nova.State;
import dev.triumphteam.nova.builtin.EmptyState;
import dev.triumphteam.nova.builtin.SimpleMutableState;
import dev.triumphteam.nova.policy.StateMutationPolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import me.zombie_striker.qav.gui.click.handler.ClickHandler;
import me.zombie_striker.qav.gui.click.handler.CompletableFutureClickHandler;
import me.zombie_striker.qav.gui.click.handler.SimpleClickHandler;
import me.zombie_striker.qav.gui.component.functional.BaseFunctionalGuiComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractFunctionalGuiComponent<P>
implements BaseFunctionalGuiComponent<P> {
    private final List<State> states = new ArrayList<State>();
    private ClickHandler<P> clickHandler = null;

    @NotNull
    public State state() {
        return this.state((State)new EmptyState());
    }

    @NotNull
    public <S extends State> S state(@NotNull S s) {
        this.states.add(s);
        return s;
    }

    @NotNull
    public <T> @NotNull MutableState<@NotNull T> state(@NotNull T t) {
        return this.state(t, StateMutationPolicy.StructuralEquality.INSTANCE);
    }

    @NotNull
    public <T> @NotNull MutableState<@NotNull T> state(@NotNull T t, @NotNull StateMutationPolicy stateMutationPolicy) {
        SimpleMutableState simpleMutableState = new SimpleMutableState(t, stateMutationPolicy);
        this.states.add((State)simpleMutableState);
        return simpleMutableState;
    }

    public <T> @NotNull MutableState<@Nullable T> nullableState(@Nullable T t) {
        return this.nullableState(t, StateMutationPolicy.StructuralEquality.INSTANCE);
    }

    public <T> @NotNull MutableState<@Nullable T> nullableState(@Nullable T t, @NotNull StateMutationPolicy stateMutationPolicy) {
        SimpleMutableState simpleMutableState = new SimpleMutableState(t, stateMutationPolicy);
        this.states.add((State)simpleMutableState);
        return simpleMutableState;
    }

    @Override
    public void withClickHandler(@Nullable ClickHandler<P> clickHandler) {
        this.clickHandler = clickHandler;
    }

    @Override
    public void withSimpleClickHandler() {
        this.clickHandler = new SimpleClickHandler();
    }

    @Override
    public void withCompletableFutureClickHandler() {
        this.clickHandler = new CompletableFutureClickHandler();
    }

    @Override
    public void withCompletableFutureClickHandler(long l, @NotNull TimeUnit timeUnit) {
        this.clickHandler = new CompletableFutureClickHandler(l, timeUnit);
    }

    @Nullable
    public ClickHandler<P> getClickHandler() {
        return this.clickHandler;
    }

    @NotNull
    protected List<State> getStates() {
        return this.states;
    }
}

