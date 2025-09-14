package dev.behindthescenery.core.utils.mixin;

import net.neoforged.fml.loading.FMLLoader;

public class MixinProdApplier extends MixinApplier {
    protected final String prodMixin;

    /**
     * Конструктор MixinProdApplier.
     *
     * @param prodMixin Имя класса миксина для production-окружения.
     * @param applyClass Имя класса миксина для development-окружения.
     * @throws IllegalArgumentException если prodMixin или applyClass равны null или пусты.
     */
    public MixinProdApplier(String prodMixin, String applyClass) {
        super(applyClass);
        if (prodMixin == null || prodMixin.isEmpty()) {
            throw new IllegalArgumentException("prodMixin не может быть null или пустым");
        }
        this.prodMixin = prodMixin;
    }

    /**
     * Проверяет, должен ли миксин применяться в зависимости от окружения.
     *
     * @param mixinClassName Имя класса миксина.
     * @return true, если миксин соответствует prodMixin в production или applyClass в development, иначе false.
     */
    @Override
    public boolean shouldApply(String mixinClassName) {
        return FMLLoader.isProduction() ? prodMixin.equals(mixinClassName) : applyClass.equals(mixinClassName);
    }

    @Override
    public boolean isCurrent(String mixinClassName) {
        return prodMixin.equals(mixinClassName) || applyClass.equals(mixinClassName);
    }

}
