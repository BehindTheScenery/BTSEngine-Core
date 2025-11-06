package dev.behindthescenery.core.utils.mixin;

public class MixinApplier {

    protected final String applyClass;

    /**
     * Конструктор MixinApplier.
     *
     * @param applyClass Имя класса миксина, который должен применяться.
     * @throws IllegalArgumentException если applyClass равен null или пустой.
     */
    public MixinApplier(String applyClass) {
        if (applyClass == null || applyClass.isEmpty()) {
            throw new IllegalArgumentException("applyClass не может быть null или пустым");
        }
        this.applyClass = applyClass;
    }

    public boolean isCurrent(String mixinClassName) {
        return applyClass.equals(mixinClassName);
    }

    /**
     * Проверяет, должен ли миксин применяться для указанного имени класса миксина.
     *
     * @param mixinClassName Имя класса миксина.
     * @return true, если миксин соответствует applyClass, иначе false.
     */
    public boolean shouldApply(String mixinClassName) {
        return applyClass.equals(mixinClassName);
    }

    /**
     * Возвращает список модов, необходимых для применения миксина.
     * Пустая реализация; переопределяется в подклассах, если требуется.
     *
     * @return Массив строк с идентификаторами модов.
     */
    public String[] getMods() {
        return new String[0];
    }
}
