package dev.behindthescenery.core.system.rendering.assimp.animation.state;

import com.mojang.datafixers.util.Pair;
import dev.behindthescenery.core.system.rendering.assimp.animation.AnimatableInstance;
import dev.behindthescenery.core.system.rendering.assimp.resource.model.basic.Node;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.joml.Matrix4f;

import java.util.List;
import java.util.function.Supplier;

public abstract class State {
    private final List<Pair<Supplier<Boolean>, State>> nextStates = new ObjectArrayList<>();
    private float startTick = 0;
    private float time = 0;

    public void start(float startTick) {
        this.startTick = startTick;
        this.time = 0;
    }

    public void checkStateSwitch(AnimatableInstance instance) {
        for (Pair<Supplier<Boolean>, State> nextState : this.nextStates) {
            if (nextState.getFirst().get()) {
                instance.setState(nextState.getSecond());
            }
        }
    }

    public abstract Matrix4f getBoneTransform(Node node);

    public void updateTime(AnimatableInstance instance, float partialTick) {
        this.time = (instance.getTick(partialTick) - this.startTick) / 20;
    }

    public float getStartTick() {
        return startTick;
    }

    public float getTime() {
        return time;
    }

    public boolean isFinished() {
        return this.time >= this.getDuration();
    }

    public abstract float getDuration();

    public void nextState(Supplier<Boolean> condition, State state) {
        this.nextStates.add(new Pair<>(condition, state));
    }

    public void nextTransitionState(Supplier<Boolean> condition, State state, float duration) {
        nextState(condition, new TransitionState(this, state, duration));
    }
}

