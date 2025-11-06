package dev.behindthescenery.core.system.user_interface.render.ui_component.animator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class UiAnimator {

    private final List<Step> steps = new ArrayList<>();
    private int currentStepIndex = 0;
    private boolean playing = false;
    private boolean loop = false;
    private boolean yoyo = false;
    private int repeatCount = 0;
    private int currentRepeat = 0;

    private long lastTime = -1;

    public static UiAnimator create() {
        return new UiAnimator();
    }

    public UiAnimator then(UiAnimation<?>... animations) {
        steps.add(new AnimationStep(Arrays.asList(animations)));
        return this;
    }

    public UiAnimator with(UiAnimation<?> animation) {
        if (steps.isEmpty()) {
            steps.add(new AnimationStep());
        }

        if(steps.getLast() instanceof AnimationStep animationStep) {
            animationStep.animations.add(animation);
        }
        return this;
    }

    public UiAnimator loop() {
        this.loop = true;
        return this;
    }

    public UiAnimator repeat(int count) {
        this.repeatCount = count;
        return this;
    }

    public UiAnimator yoyo() {
        this.yoyo = true;
        return this;
    }

    public UiAnimator sleep(long ms) {
        this.steps.add(new WaitStep(ms));
        return this;
    }

    public void play() {
        reset();
        playing = true;
        lastTime = System.currentTimeMillis();
    }

    public void stop() {
        playing = false;
        lastTime = -1;
    }

    public void reset() {
        currentStepIndex = 0;
        currentRepeat = 0;

        for (Step step : steps) {
            step.reset();
        }
    }

    public void tick() {
        if (!playing || steps.isEmpty()) return;

        long now = System.currentTimeMillis();
        if (lastTime == -1) {
            lastTime = now;
            return;
        }
        long deltaMs = now - lastTime;
        lastTime = now;

        Step step = steps.get(currentStepIndex);
        step.update(deltaMs);

        if (step.isComplete()) {
            currentStepIndex++;

            if (currentStepIndex >= steps.size()) {
                if (loop || currentRepeat < repeatCount) {
                    currentRepeat++;
                    if (yoyo) reverseSteps();
                    reset();
                    playing = true;
                    lastTime = System.currentTimeMillis();
                    return;
                }
                playing = false;
            }
        }
    }

    private void reverseSteps() {
        for (Step step : steps) {
            step.reverse();
        }
        Collections.reverse(steps);
    }

    private interface Step {
        void update(long deltaMs);

        boolean isComplete();

        void reset();

        void reverse();
    }

    private static class AnimationStep implements Step {
        private final List<UiAnimation<?>> animations;

        public AnimationStep() {
            this.animations = new ArrayList<>();
        }

        public AnimationStep(List<UiAnimation<?>> animations) {
            this.animations = new ArrayList<>(animations);
        }

        public void update(long deltaMs) {
            for (UiAnimation<?> anim : animations) {
                anim.update(deltaMs);
            }
        }

        public boolean isComplete() {
            for (UiAnimation<?> anim : animations) {
                if (!anim.isFinished()) return false;
            }
            return true;
        }

        public void reset() {
            for (UiAnimation<?> anim : animations) {
                anim.reset();
            }
        }

        @Override
        public void reverse() {
            for (UiAnimation<?> anim : animations) {
                anim.reverse();
            }
        }
    }

    private static class WaitStep implements Step {

        private final long waitTime;
        private long elapsed = 0;
        private boolean complete = false;

        private WaitStep(long waitTime) {
            this.waitTime = waitTime;
        }

        @Override
        public void update(long deltaMs) {
            if(complete) return;
            elapsed += deltaMs;
            if(elapsed >= waitTime) complete = true;
        }

        @Override
        public boolean isComplete() {
            return complete;
        }

        @Override
        public void reset() {
            elapsed = 0;
            complete = false;
        }

        @Override
        public void reverse() {}
    }
}
