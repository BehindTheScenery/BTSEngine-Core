package dev.behindthescenery.core.system.rendering.assimp.animation.state;

import dev.behindthescenery.core.system.rendering.assimp.animation.AnimatableInstance;
import dev.behindthescenery.core.system.rendering.assimp.resource.AssimpResources;
import dev.behindthescenery.core.system.rendering.assimp.resource.animation.Animation;
import dev.behindthescenery.core.system.rendering.assimp.resource.animation.Channel;
import dev.behindthescenery.core.system.rendering.assimp.resource.model.basic.Node;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;

public class AnimationState extends State {
    private final Animation animation;
    private final boolean loop;

    public AnimationState(Identifier resourceLocation) {
        this(resourceLocation, false);
    }

    public AnimationState(Identifier resourceLocation, boolean loop) {
        this(AssimpResources.getAnimation(resourceLocation), loop);
    }

    public AnimationState(Animation animation, boolean loop) {
        this.animation = animation;
        this.loop = loop;
    }

    @Override
    public Matrix4f getBoneTransform(Node node) {
        Channel channel = this.animation.getChannel(node.getName());
        if (channel != null) {
            return channel.getMatrix(this.getTime());
        }
        return new Matrix4f(node.getInitialTransform());
    }

    @Override
    public void updateTime(AnimatableInstance instance, float partialTick) {
        super.updateTime(instance, partialTick);

        if (loop && this.isFinished()) {
            this.start(this.getStartTick() + (this.getDuration() * 20));
            this.updateTime(instance, partialTick);
        }
    }

    @Override
    public float getDuration() {
        return this.animation.getDuration();
    }
}
