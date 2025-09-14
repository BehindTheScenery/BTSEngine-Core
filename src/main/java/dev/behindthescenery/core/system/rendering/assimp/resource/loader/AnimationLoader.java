package dev.behindthescenery.core.system.rendering.assimp.resource.loader;

import dev.behindthescenery.core.system.rendering.assimp.resource.animation.Animation;
import dev.behindthescenery.core.system.rendering.assimp.resource.animation.Channel;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.lwjgl.assimp.*;

import java.util.ArrayList;
import java.util.List;

public class AnimationLoader {
    public static Animation loadAnimation(Animation animation, AIAnimation aiAnimation) {
        List<Channel> channels = new ArrayList<>();
        for (int i = 0; i < aiAnimation.mNumChannels(); i++) {
            AINodeAnim aiNodeAnim = AINodeAnim.create(aiAnimation.mChannels().get(i));

            List<Channel.Key<Vector3fc>> positionKeys = new ArrayList<>();
            List<Channel.Key<Quaternionfc>> rotationKeys = new ArrayList<>();
            List<Channel.Key<Vector3fc>> scalingKeys = new ArrayList<>();

            //Todo перенесите обработку позиционного ключа в отдельный метод
            for (AIVectorKey key : aiNodeAnim.mPositionKeys()) {
                AIVector3D vector = key.mValue();
                positionKeys.add(new Channel.Key<>((float) (key.mTime() / aiAnimation.mTicksPerSecond()),
                        new Vector3f(vector.x(), vector.y(), vector.z())));
            }

            //Todo перенесите обработку ключа поворота в отдельный метод
            for (AIQuatKey key : aiNodeAnim.mRotationKeys()) {
                AIQuaternion quaternion = key.mValue();
                rotationKeys.add(new Channel.Key<>((float) (key.mTime() / aiAnimation.mTicksPerSecond()),
                        new Quaternionf(quaternion.x(), quaternion.y(), quaternion.z(), quaternion.w())));
            }

            //Todo перенесите обработку ключа масштабирования в отдельный метод
            for (AIVectorKey key : aiNodeAnim.mScalingKeys()) {
                AIVector3D vector = key.mValue();
                scalingKeys.add(new Channel.Key<>((float) (key.mTime() / aiAnimation.mTicksPerSecond()),
                        new Vector3f(vector.x(), vector.y(), vector.z())));
            }

            channels.add(new Channel(aiNodeAnim.mNodeName().dataString(), positionKeys, rotationKeys, scalingKeys));
        }

        float duration = (float) (aiAnimation.mDuration() / aiAnimation.mTicksPerSecond());
        return animation.set(channels, duration);
    }
}
