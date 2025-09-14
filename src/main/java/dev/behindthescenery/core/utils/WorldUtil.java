package dev.behindthescenery.core.utils;

import net.minecraft.world.HeightLimitView;
import net.minecraft.world.World;

public class WorldUtil {

    public static int getMaxSection(final HeightLimitView world) {
        return world.getTopSectionCoord() - 1;
    }

    public static int getMaxSection(final World world) {
        return world.getTopSectionCoord() - 1;
    }

    public static int getMinSection(final HeightLimitView world) {
        return world.getBottomSectionCoord();
    }

    public static int getMinSection(final World world) {
        return world.getBottomSectionCoord();
    }

    public static int getMaxLightSection(final HeightLimitView world) {
        return getMaxSection(world) + 1;
    }

    public static int getMinLightSection(final HeightLimitView world) {
        return getMinSection(world) - 1;
    }



    public static int getTotalSections(final HeightLimitView world) {
        return getMaxSection(world) - getMinSection(world) + 1;
    }

    public static int getTotalLightSections(final HeightLimitView world) {
        return getMaxLightSection(world) - getMinLightSection(world) + 1;
    }

    public static int getMinBlockY(final HeightLimitView world) {
        return getMinSection(world) << 4;
    }

    public static int getMaxBlockY(final HeightLimitView world) {
        return (getMaxSection(world) << 4) | 15;
    }

    public static String getWorldName(final World world) {
        if (world == null) {
            return "null world";
        }
        return world.getDimension().toString();
    }

    private WorldUtil() {
        throw new RuntimeException();
    }
}
