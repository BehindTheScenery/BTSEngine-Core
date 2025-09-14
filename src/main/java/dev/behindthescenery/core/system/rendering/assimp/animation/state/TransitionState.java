package dev.behindthescenery.core.system.rendering.assimp.animation.state;

import dev.behindthescenery.core.system.rendering.assimp.resource.model.basic.Node;
import dev.behindthescenery.core.utils.MathUtils;
import org.joml.Matrix4f;

public class TransitionState extends State {
    private final State previousState;
    private final State nextState;
    private final float duration;

    public TransitionState(State previousState, State nextState, float duration) {
        this.previousState = previousState;
        this.nextState = nextState;
        this.duration = duration;

        nextState(this::isFinished, nextState);
    }

    @Override
    public Matrix4f getBoneTransform(Node node) {
        float alpha = this.getTime() / getDuration();
        Matrix4f previousMatrix = previousState.getBoneTransform(node);
        Matrix4f nextMatrix = nextState.getBoneTransform(node);
        return MathUtils.lerpMatrix(previousMatrix, nextMatrix, alpha);
    }

    @Override
    public void start(float startTick) {
        super.start(startTick);
        this.nextState.start(startTick);
    }

    @Override
    public float getDuration() {
        return this.duration;
    }
}