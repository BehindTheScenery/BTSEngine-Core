package dev.behindthescenery.core.system.user_interface.render.ui_component.animator;

import java.util.Collection;

public interface UiAnimatorSupport {

    Collection<UiAnimator> getAnimators();

    default UiAnimator addAnimator(UiAnimator animator) {
        getAnimators().add(animator);
        return animator;
    }

    default boolean containsAnimator(UiAnimator animator) {
        return getAnimators().contains(animator);
    }
}
