package dev.behindthescenery.core.utils.mixin;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public abstract class BtsMixinPlugin implements IMixinConfigPlugin {

    protected List<MixinApplier> appliers = new ArrayList<>();

    public BtsMixinPlugin() {
        addAppliers();
    }

    protected void addApplier(MixinApplier applier) {
        appliers.add(applier);
    }

    protected void addApplier(MixinApplier... applier) {
        appliers.addAll(Arrays.asList(applier));
    }

    public abstract void addAppliers();

    @Override
    public void onLoad(String mixinPackage) {

    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    /**
     * Проверяет, должен ли миксин применяться к целевому классу.
     * - Если миксин есть в списке апплаеров и условия апплаера выполнены (моды загружены и shouldApply возвращает true),
     *   миксин применяется (return true).
     * - Если миксин есть в списке апплаеров, но условия не выполнены, миксин не применяется (return false).
     * - Если миксина нет в списке апплаеров, он применяется по умолчанию (return true).
     *
     * @param targetClassName Имя целевого класса.
     * @param mixinClassName Имя класса миксина.
     * @return true, если миксин должен применяться, иначе false.
     */
    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (mixinClassName == null || targetClassName == null) {
            return false;
        }



        for (MixinApplier applier : appliers) {
            if(applier.isCurrent(mixinClassName)) {
                if(!applier.shouldApply(mixinClassName))
                    return false;
            }
        }

        return true;
    }


    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}
