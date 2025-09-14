package dev.behindthescenery.core.utils;


import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.enums.BlockFace;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.RotationAxis;

@SuppressWarnings("unused")
public class BlockUtils {

    public static void translateBlock(BlockState blockState, MatrixStack poseStack) {
        poseStack.translate(0.5f, 0.5f, 0.5f);
        float direction = getYRotation(blockState);
        poseStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(direction));

        if (blockState.contains(Properties.BLOCK_FACE)) {
            BlockFace face = blockState.get(Properties.BLOCK_FACE);
            switch (face) {
                case CEILING -> poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));
                case WALL -> poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90));
            }
        }
        poseStack.translate(0, -0.5f, 0);
    }

    public static float getYRotation(BlockState blockState) {
        if (blockState.contains(Properties.ROTATION))
            return (360f/16f) * blockState.get(Properties.ROTATION);

        if (blockState.contains(HorizontalFacingBlock.FACING))
            return -blockState.get(HorizontalFacingBlock.FACING).asRotation();

        if (blockState.contains(FacingBlock.FACING))
            return -blockState.get(FacingBlock.FACING).asRotation();

        return 0;
    }
}
