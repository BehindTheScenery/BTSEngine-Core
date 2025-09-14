package dev.behindthescenery.core.system.rendering.assimp.resource.model.basic;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.behindthescenery.core.BtsCore;
import dev.behindthescenery.core.system.rendering.assimp.render.BTSAssimpModelRenderer;
import dev.behindthescenery.core.system.rendering.assimp.resource.model.BoundingBox;
import dev.behindthescenery.core.system.rendering.color.Texture;
import dev.behindthescenery.core.system.rendering.model_render.model_data.Mesh;
import dev.behindthescenery.core.system.rendering.model_render.optimization.BtsMeshOptimization;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Model {

    public static final Codec<Model> CODEC_SINGLE = RecordCodecBuilder.create(instance ->
            instance.group(
                Identifier.CODEC.fieldOf("id").forGetter(Model::getModelID),
                Node.CODEC.fieldOf("root").forGetter(Model::getRootNode),
                Mesh.CODEC.listOf().fieldOf("meshes").forGetter(Model::getMeshes),
                Material.CODEC.listOf().fieldOf("materials").forGetter(Model::getMaterials)
            ).apply(instance, Model::new));

    public static final Codec<Model> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                Identifier.CODEC.fieldOf("id").forGetter(Model::getModelID),
                Node.CODEC.fieldOf("root").forGetter(Model::getRootNode),
                Mesh.CODEC.listOf().fieldOf("meshes").forGetter(Model::getMeshes),
                Node.CODEC.listOf().fieldOf("nodes").forGetter(Model::getNodes),
                Material.CODEC.listOf().fieldOf("materials").forGetter(Model::getMaterials)
            ).apply(instance, Model::new));

    public static Texture DEFAULT_TEXTURE;
    public static final Identifier DEFAULT_TEXTURE_LOCATION = BtsCore.location("textures/debug/debug_grid.png");

    private Node rootNode;
    private Identifier modelID;
    private List<Mesh> meshes = new ArrayList<>();
    private List<Node> nodes = new ArrayList<>();
    private List<Material> materials = new ArrayList<>();


    public Model() {}

    public Model(Node rootNode, List<Mesh> meshes, List<Node> nodes, List<Material> materials) {
        set(rootNode, meshes, nodes, materials);
    }

    public Model(Identifier identifier, Node node, List<Mesh> meshes, List<Node> nodes, List<Material> materials) {
        this(node, meshes, nodes, materials);
        setId(identifier);
    }

    public Model(Identifier identifier, Node node, List<Mesh> meshes, List<Material> materials) {
        this(identifier, node, meshes, new ArrayList<>(), materials);
    }


    public Model set(Node rootNode, List<Mesh> meshes, List<Node> nodes, List<Material> materials) {
        this.meshes = meshes;
        this.rootNode = rootNode;
        this.nodes = nodes;
        this.materials = materials;
        return this;
    }

    public Model setId(Identifier identifier) {
        this.modelID = identifier;
        return this;
    }

    public Identifier getModelID() {
        return modelID;
    }

    public Function<String, Texture> getTextures() {
        return (s) -> getDefaultImage();
    }

    public List<Mesh> getMeshes() {
        return meshes;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public Node getRootNode() {
        return rootNode;
    }

    public BoundingBox getBoundBox() {
        return BtsMeshOptimization.calculateBoundBox(meshes.getFirst());
    }

    public void setMaterial(Material material) {
        materials.removeIf(s -> s.getMaterialName().equals(material.getMaterialName()));
        materials.add(material);

        for (Mesh mesh : getMeshes()) {
            if(mesh.material().getMaterialName().equals(material.getMaterialName())) {
                mesh.setMaterial(material);
                return;
            }
        }
    }

    public List<Material> getMaterials() {
        return materials;
    }

    public static Texture getDefaultImage() {
        if(DEFAULT_TEXTURE == null) {
            DEFAULT_TEXTURE = new Texture(DEFAULT_TEXTURE_LOCATION);
        }
        return DEFAULT_TEXTURE;
    }

    public void draw(MatrixStack matrixStack) {
        draw(matrixStack, 0);
    }

    public void draw(MatrixStack matrixStack, int lightMap) {
        BTSAssimpModelRenderer.renderWorldModel(this, matrixStack, lightMap);
    }
}
