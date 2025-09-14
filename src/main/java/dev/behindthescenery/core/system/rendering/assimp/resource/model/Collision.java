package dev.behindthescenery.core.system.rendering.assimp.resource.model;

import dev.behindthescenery.core.system.rendering.UiEditingContext;
import dev.behindthescenery.core.system.rendering.UiEditingSupport;
import dev.behindthescenery.core.system.rendering.assimp.resource.buffers.BoundBoxBuffer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.loading.FMLEnvironment;
import org.joml.Vector3f;

public class Collision implements UiEditingSupport {

    protected Vector3f position;
    protected Vector3f scale;
    protected BoundingBox boundingBox;
    protected Vector3f offsetPosition = new Vector3f();

    @OnlyIn(Dist.CLIENT)
    private BoundBoxBuffer boundBoxBuffer;
    private VoxelShape voxelShape;

    public Collision(BoundingBox boundingBox, Vector3f position) {
        this(boundingBox, position, new Vector3f(1));
    }

    public Collision(BoundingBox boundingBox, Vector3f position, Vector3f scale) {
        this.position = position;
        this.scale = scale;
        setBoundingBox(boundingBox);
    }

    public Collision(Box boundingBox, Vector3f position) {
        this(boundingBox, position, new Vector3f(1));
    }

    public Collision(Box boundingBox, Vector3f position, Vector3f scale) {
        this.position = position;
        this.scale = scale;
        setBoundingBox(new BoundingBox(boundingBox));
    }

    public void setPosition(Vector3f position) {
        this.position = position;

        if(FMLEnvironment.dist.isClient()) {
            setBoundingBox(boundingBox);
        }
    }

    @OnlyIn(Dist.CLIENT)
    protected Collision recreateBoundBoxBuffer() {
        if (boundBoxBuffer != null) {
            boundBoxBuffer.cleanup();
        }
        boundBoxBuffer = new BoundBoxBuffer(boundingBox);
        return this;
    }

    @OnlyIn(Dist.CLIENT)
    protected Collision recreateBoundBoxBuffer(BoundingBox box) {
        if (boundBoxBuffer != null) {
            boundBoxBuffer.cleanup();
        }
        boundBoxBuffer = new BoundBoxBuffer(box);
        return this;
    }

    public Collision setBoundingBox(Box boundingBox) {
        return setBoundingBox(new BoundingBox(boundingBox));
    }

    public Collision setBoundingBox(BoundingBox boundingBox) {
        this.boundingBox = boundingBox;
        this.voxelShape = createFromBoundBox(boundingBox);
        if (FMLEnvironment.dist.isClient()) {
            recreateBoundBoxBuffer();
        }
        return this;
    }

    protected VoxelShape createFromBoundBox(Box box) {
        return createVoxelShape((float) box.minX, (float) box.minY,
                (float) box.minZ, (float) box.maxX, (float) box.maxY, (float) box.maxZ
        );
    }

    protected VoxelShape createFromBoundBox(BoundingBox box) {
        return createVoxelShape(box.min.x, box.min.y, box.min.z,
                box.max.x, box.max.y, box.max.z);
    }

    protected VoxelShape createVoxelShape(float x, float y, float z, float x1, float y1, float z1) {
        return VoxelShapes.cuboid(
                x *  scale.x + position.x + offsetPosition.x,
                y *  scale.y + position.y + offsetPosition.y,
                z *  scale.z + position.z + offsetPosition.z,
                x1 * scale.x + position.x + offsetPosition.x,
                y1 * scale.y + position.y + offsetPosition.y,
                z1 * scale.z + position.z + offsetPosition.z
        );
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    @OnlyIn(Dist.CLIENT)
    public BoundBoxBuffer getBoundBoxBuffer() {
        return boundBoxBuffer;
    }

    public VoxelShape getVoxelShape() {
        return voxelShape;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getScale() {
        return scale;
    }

    @Override
    public void editOnUi(UiEditingContext context) {
        final int space = (int) (context.getTextHeight() * 1.7);

        context.pushGroup("Collision");
        context.space(0, space);
        context.addText("BonudBox");
        context.space(0, space);
        context.editVector3f("Position", offsetPosition, s -> {
            offsetPosition = s;
            setBoundingBox(boundingBox);
        });
        context.editVector3f("Scale", scale, s -> {
            scale = s;
            setBoundingBox(boundingBox);
        });
        context.editBoundingBox("Size", boundingBox, s -> setBoundingBox(new Box(
                s.min.x,
                s.min.y,
                s.min.z,
                s.max.x,
                s.max.y,
                s.max.z)
        ));
        context.button("Teleport To Center", () -> {
            final Vector3f d = boundingBox.getCenter();
            MinecraftClient.getInstance().player.setPosition(position.x + d.x, position.y + d.y, position.z + d.z);
        });
        context.popGroup();
    }

    @Override
    public int getObjectHash() {
        return hashCode();
    }
}
