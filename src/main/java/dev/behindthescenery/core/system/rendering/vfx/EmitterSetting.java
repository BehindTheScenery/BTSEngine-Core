package dev.behindthescenery.core.system.rendering.vfx;

import net.minecraft.util.math.Box;

/**
 * Настройки излучателя
 */
public class EmitterSetting {


    /**
     * Минимальное время жизни частиц
     */
    public int minParticleLive = 0;

    /**
     * Максимальное время жизни частиц
     */
    public int maxParticleLive = 100;

    /**
     * Количество частиц которое может содержать излучатель
     */
    public int maxParticleCounts;

    /**
     * Дистанция рендера излучателя, если значение 0 то нет ограничений
     */
    public double renderDistance = 0;

    /**
     * Отображает зону излучателя
     */
    public boolean renderZone = false;

    /**
     * Зона излучателя
     */
    public Box emitterZone;

    /**
     * Использовать ли свет для рендера частиц
     */
    public boolean useLight = false;

    /**
     * Использовать ли маску глубины. </br>
     * Если она выключена, то нужно меньше просчётов, но в таком случае если вы используете
     * другие излучатели, то их частицы будут перекрыты частицами других излучателей </br> </br>
     *
     * Если вы не планируете использовать несколько излучателей в одном месте, то это можно и не включать
     */
    public boolean useDepthMask = true;

    /**
     * Использовать продвинутое отсечение. </br>
     * Если включён, то отсечение будет по частицам, а не по излучателю, что позволит отсекать только те частицы которые не
     * видно
     */
    public boolean useAdvancedCulling = false;

    public boolean useAnimation = false;

    public boolean loopAnimation = true;

    public int particleSpawnPerTick = 1;

}
