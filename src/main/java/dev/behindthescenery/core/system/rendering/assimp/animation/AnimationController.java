package dev.behindthescenery.core.system.rendering.assimp.animation;


import dev.behindthescenery.core.system.rendering.assimp.animation.state.State;
import dev.behindthescenery.core.system.rendering.assimp.resource.model.basic.Model;
import dev.behindthescenery.core.system.rendering.assimp.resource.model.basic.Node;

public interface AnimationController<T extends Animatable> {
    State registerStates(T t);

    default void animate(T t, Model model, float partialTick) {
        AnimatableInstance instance = t.getAnimatableInstance();
        State state = instance.getState();
        if (state == null)
            instance.setState(state = registerStates(t));

        if (state == null)
            throw new NullPointerException("Animation state is NULL");

        state.updateTime(instance, partialTick);
        state.checkStateSwitch(instance);
        for (Node node : model.getNodes()) {
            processBoneTransform(node, instance);
        }
    }

    default void processBoneTransform(Node node, AnimatableInstance instance) {
        node.getRelativeTransform().set(instance.getState().getBoneTransform(node));
    }
}
