package dev.behindthescenery.core.system.rendering.assimp.resource.model;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.behindthescenery.core.system.rendering.BtsRenderSystem;
import dev.behindthescenery.core.system.rendering.UiEditingContext;
import dev.behindthescenery.core.system.rendering.UiEditingSupport;
import dev.behindthescenery.core.system.rendering.assimp.render.BTSAssimpModelRenderer;
import dev.behindthescenery.core.system.rendering.assimp.resource.AssimpResources;
import dev.behindthescenery.core.system.rendering.assimp.resource.buffers.BoundBoxBuffer;
import dev.behindthescenery.core.system.rendering.assimp.resource.model.basic.Material;
import dev.behindthescenery.core.system.rendering.assimp.resource.model.basic.Model;
import dev.behindthescenery.core.utils.ExternCodecs;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс WorldModel представляет модель в игровом мире с параметрами рендеринга, позицией, масштабом и вращением.
 * Поддерживает управление материалами, ограничивающими коробками и окклюзией для оптимизации рендеринга.
 */
public class WorldModel implements UiEditingSupport {

    private static final int DEFAULT_LIGHTMAP_UV = 15000;
    private static final int DEFAULT_SCALE_VALUE = 1;
    private static final int DEFAULT_ROTATION_VALUE = 0;

    public static final Codec<WorldModel> CODEC_SERVER = RecordCodecBuilder.create(instance ->
        instance.group(
                Identifier.CODEC.fieldOf("modelId").forGetter(WorldModel::getModelId),
                ExternCodecs.VECTOR_3_F_CODEC.fieldOf("position").forGetter(WorldModel::getPosition),
                ExternCodecs.VECTOR_3_F_CODEC.fieldOf("scale").forGetter(WorldModel::getScale),
                ExternCodecs.VECTOR_4_F_CODEC.fieldOf("rotation").forGetter(WorldModel::getRotation),
                Codecs.strictUnboundedMap(Codec.STRING, Material.CODEC).fieldOf("materials").forGetter(WorldModel::getMaterialMap),
                WorldRenderParams.CODEC.fieldOf("renderParams").forGetter(WorldModel::getRenderParams),
                Codec.BOOL.fieldOf("visible").forGetter(WorldModel::isVisible),
                ExternCodecs.BOX_CODEC.fieldOf("boundingBox").forGetter(WorldModel::getBoundingBoxMinecraft)
        ).apply(instance, WorldModel::new));

    public static final Codec<WorldModel> CODEC_CLIENT = RecordCodecBuilder.create(instance ->
        instance.group(
                Model.CODEC.fieldOf("model").forGetter(WorldModel::getModel),
                ExternCodecs.VECTOR_3_F_CODEC.fieldOf("position").forGetter(WorldModel::getPosition),
                ExternCodecs.VECTOR_3_F_CODEC.fieldOf("scale").forGetter(WorldModel::getScale),
                ExternCodecs.VECTOR_4_F_CODEC.fieldOf("rotation").forGetter(WorldModel::getRotation),
                Codecs.strictUnboundedMap(Codec.STRING, Material.CODEC).fieldOf("materials").forGetter(WorldModel::getMaterialMap),
                WorldRenderParams.CODEC.fieldOf("renderParams").forGetter(WorldModel::getRenderParams),
                Codec.BOOL.fieldOf("visible").forGetter(WorldModel::isVisible),
                ExternCodecs.BOX_CODEC.fieldOf("boundingBox").forGetter(WorldModel::getBoundingBoxMinecraft)
        ).apply(instance, WorldModel::new));

    protected final Identifier modelId;
    protected Model model;
    protected Vector3f position;
    protected Vector3f scale;
    protected Vector4f rotation;
    protected Map<String, Material> materialMap = new HashMap<>();
    protected WorldRenderParams renderParams;

    protected boolean visible = true;
    private int occlusionQueryId;
    private int lastSamplesPassed = 1;


    protected Collision collision;

    /**
     * Создаёт модель с заданным идентификатором и позицией. Масштаб и вращение инициализируются значениями по умолчанию. <br> <br>
     * Для того чтобы моделью можно было управлять на сервере используем ID модели в место класса {@link Model}
     */
    public WorldModel(Identifier modelId, Vector3f position) {
        this(modelId, position, new Vector3f(DEFAULT_SCALE_VALUE, DEFAULT_SCALE_VALUE, DEFAULT_SCALE_VALUE),
                new Vector4f(DEFAULT_ROTATION_VALUE));
    }

    /**
     * Создаёт модель с заданными идентификатором, позицией, масштабом и вращением. <br> <br>
     * Для того чтобы моделью можно было управлять на сервере используем ID модели в место класса {@link Model}
     */
    public WorldModel(Identifier modelId, Vector3f position, Vector3f scale, Vector4f rotation) {
        this.modelId = modelId;
        this.position = position;
        this.scale = scale;
        this.rotation = rotation;
        this.renderParams = new WorldRenderParams(WorldRenderParams.USE_DEPTH_TEST, GL11.GL_LEQUAL);
        this.collision = new Collision(new BoundingBox(), position);
        if (FMLEnvironment.dist.isClient()) {
            initClientData();
        }
    }

    /**
     * Создаёт модель с заданной моделью и позицией (только для клиента).
     */
    @OnlyIn(Dist.CLIENT)
    public WorldModel(Model model, Vector3f position) {
        this(model, position, new Vector3f(DEFAULT_SCALE_VALUE, DEFAULT_SCALE_VALUE, DEFAULT_SCALE_VALUE),
                new Vector4f(DEFAULT_ROTATION_VALUE));
    }

    /**
     * Создаёт модель с заданной моделью, позицией, масштабом и вращением (только для клиента).
     */
    @OnlyIn(Dist.CLIENT)
    public WorldModel(Model model, Vector3f position, Vector3f scale, Vector4f rotation) {
        this.modelId = model.getModelID();
        this.model = model;
        this.position = position;
        this.scale = scale;
        this.rotation = rotation;
        this.renderParams = new WorldRenderParams(WorldRenderParams.USE_DEPTH_TEST, GL11.GL_LEQUAL);
        this.collision = new Collision(new BoundingBox(), position);
        initClientData();
    }

    protected WorldModel(Identifier identifier, Vector3f vector3f, Vector3f vector3f1, Vector4f vector4f, Map<String, Material> stringMaterialMap, WorldRenderParams worldRenderParams, Boolean aBoolean, Box box) {
        this.modelId = identifier;
        this.position = vector3f;
        this.scale = vector3f1;
        this.rotation = vector4f;
        this.materialMap = stringMaterialMap;
        this.renderParams = worldRenderParams;
        this.visible = aBoolean;
        this.collision = new Collision(box, position);
        if (FMLEnvironment.dist.isClient()) {
            initClientData();
        }
    }

    protected WorldModel(Model model, Vector3f vector3f, Vector3f vector3f1, Vector4f vector4f, Map<String, Material> stringMaterialMap, WorldRenderParams worldRenderParams, Boolean aBoolean, Box box) {
        this.modelId = model.getModelID();
        this.model = model;
        this.position = vector3f;
        this.scale = vector3f1;
        this.rotation = vector4f;
        this.materialMap = stringMaterialMap;
        this.renderParams = worldRenderParams;
        this.visible = aBoolean;
        this.collision = new Collision(box, position);
        initClientData();
    }

    /**
     * Инициализирует клиентские данные модели, включая загрузку модели и настройку окклюзии.
     */
    @OnlyIn(Dist.CLIENT)
    public WorldModel initClientData() {
        if (model == null) {
            model = AssimpResources.getModel(modelId);
        }
        BtsRenderSystem.exec(() -> {
            recreateOcclusionData(false);
            collision.recreateBoundBoxBuffer();
        });
        return this;
    }

    /**
     * Пересоздаёт данные для окклюзии, удаляя или создавая новый запрос окклюзии.
     */
    @OnlyIn(Dist.CLIENT)
    protected void recreateOcclusionData(boolean remove) {
        if (occlusionQueryId != 0) {
            GL15.glDeleteQueries(occlusionQueryId);
            occlusionQueryId = 0;
        }
        if (!remove) {
            occlusionQueryId = GL15.glGenQueries();
        }
    }


    /**
     * Устанавливает параметры рендеринга.
     */
    public WorldModel setRenderParams(WorldRenderParams renderParams) {
        this.renderParams = renderParams;
        return this;
    }

    /**
     * Добавляет биты к параметрам рендеринга, обновляя окклюзию при необходимости.
     */
    public WorldModel addBits(int bits) {
        renderParams.addBits(bits);
        if ((bits & WorldRenderParams.USE_OCCLUSION) != 0 && FMLEnvironment.dist.isClient()) {
            recreateOcclusionData(false);
        }
        return this;
    }

    /**
     * Удаляет биты из параметров рендеринга, очищая окклюзию при необходимости.
     */
    public WorldModel removeBits(int bits) {
        renderParams.removeBits(bits);
        if ((bits & WorldRenderParams.USE_OCCLUSION) != 0 && FMLEnvironment.dist.isClient()) {
            recreateOcclusionData(true);
        }
        return this;
    }

    /**
     * Устанавливает материал для указанного идентификатора.
     */
    public WorldModel setMaterial(String id, Material material) {
        materialMap.put(id, material);
        return this;
    }

    /**
     * Получает материал по идентификатору.
     */
    @Nullable
    public Material getMaterial(String id) {
        return materialMap.get(id);
    }

    public WorldRenderParams getRenderParams() {
        return renderParams;
    }

    public Box getBoundingBoxMinecraft() {
        return collision.getBoundingBox().toMinecraftBox();
    }

    public BoundingBox getBoundingBox() {
        return collision.getBoundingBox();
    }

    public int getBits() {
        return renderParams.getBits();
    }

    public void setRotation(Vector4f rotation) {
        this.rotation = rotation;
    }

    public void setScale(Vector3f scale) {
        this.scale = scale;
    }

    public void setPosition(Vector3f position) {
        if(this.position != position) {
            this.position = position;
            this.collision.setPosition(position);
        }
    }

    public Vector4f getRotation() {
        return rotation;
    }

    public Vector3f getScale() {
        return scale;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Model getModel() {
        return model;
    }

    public boolean isVisible() {
        return visible;
    }

    public int getOcclusionQueryId() {
        return occlusionQueryId;
    }

    public int getLastSamplesPassed() {
        return lastSamplesPassed;
    }

    public Identifier getModelId() {
        return modelId;
    }

    public Map<String, Material> getMaterialMap() {
        return materialMap;
    }

//    @OnlyIn(Dist.CLIENT)
    public Matrix4f getTransformMatrix() {
        return new Matrix4f()
                .translate(position)
                .rotate(new Quaternionf()
                        .rotateXYZ(
                                (float) Math.toRadians(rotation.x),
                                (float) Math.toRadians(rotation.y),
                                (float) Math.toRadians(rotation.z)))
                .scale(scale);
    }

    /**
     * Отрисовывает модель с использованием стека матриц и параметров освещения.
     */
    @OnlyIn(Dist.CLIENT)
    public void draw(MatrixStack poseStack) {
        draw(poseStack, DEFAULT_LIGHTMAP_UV);
    }

    /**
     * Отрисовывает модель с заданным значением освещения.
     */
    @OnlyIn(Dist.CLIENT)
    public void draw(MatrixStack poseStack, int lightmapUV) {
        if (visible) {
            BTSAssimpModelRenderer.renderWorldModel(this, poseStack, lightmapUV, renderParams.getBits());
        }
    }

    /**
     * Получает буфер ограничивающей коробки.
     */
    @OnlyIn(Dist.CLIENT)
    public BoundBoxBuffer getBoundBoxBuffer() {
        return collision.getBoundBoxBuffer();
    }

    /**
     * Получает воксельную форму для коллизий.
     */
    public VoxelShape getCollision() {
        return collision.getVoxelShape();
    }

    /**
     * Обновляет результат окклюзии, если запрос доступен.
     */
    @OnlyIn(Dist.CLIENT)
    public void updateOcclusionResult() {
        if (occlusionQueryId != 0 && GL15.glGetQueryObjecti(occlusionQueryId, GL15.GL_QUERY_RESULT_AVAILABLE) != 0) {
            lastSamplesPassed = GL15.glGetQueryObjecti(occlusionQueryId, GL15.GL_SAMPLES_PASSED);
        }
    }

    @Override
    public void editOnUi(UiEditingContext context) {
        final int space = (int) (context.getTextHeight() * 1.7);

        context.space(0, space);
        context.addText("Transform");
        context.space(0, space);
        context.editVector3f("Position", position, this::setPosition);
        context.editVector4f("Rotation", rotation, s -> rotation = s);
        context.editVector3f("Scale"   , scale   , s -> scale = s);
        collision.editOnUi(context);
    }

    @Override
    public int getObjectHash() {
        return hashCode();
    }
}