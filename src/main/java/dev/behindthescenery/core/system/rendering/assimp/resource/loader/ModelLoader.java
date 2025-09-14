package dev.behindthescenery.core.system.rendering.assimp.resource.loader;


import dev.behindthescenery.core.system.rendering.TextureType;
import dev.behindthescenery.core.system.rendering.assimp.resource.model.*;
import dev.behindthescenery.core.system.rendering.assimp.resource.model.basic.Material;
import dev.behindthescenery.core.system.rendering.assimp.resource.model.basic.Model;
import dev.behindthescenery.core.system.rendering.assimp.resource.model.basic.Node;
import dev.behindthescenery.core.system.rendering.assimp.utils.AssimpUtils;
import dev.behindthescenery.core.system.rendering.model_render.model_data.Mesh;
import org.joml.Matrix4f;
import org.lwjgl.assimp.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.assimp.Assimp.*;

public class ModelLoader {
    public static Model loadScene(Model model, AIScene scene) {
        List<Mesh> meshes = new ArrayList<>();
        List<Material> materials = new ArrayList<>();
        List<Node> nodes = new ArrayList<>();

        // Build Node Tree
        Node rootNode = buildNodeTree(scene.mRootNode(), null, nodes);

        // Process Materials
        for (int i = 0; i < scene.mNumMaterials(); i++) {
            AIMaterial aiMaterial = AIMaterial.create(scene.mMaterials().get(i));
            Material material = processMaterial(aiMaterial);
            materials.add(material);
        }

        // Process Meshes
        for (int i = 0; i < scene.mNumMeshes(); i++) {
            AIMesh aiMesh = AIMesh.create(scene.mMeshes().get(i));


            Mesh mesh = processMesh(aiMesh, materials, nodes);
            meshes.add(mesh);
        }

        return model.set(rootNode, meshes, nodes, materials);
    }

    public static Node buildNodeTree(AINode aiNode, Node parent, List<Node> nodes) {
        String name = aiNode.mName().dataString();

        Matrix4f fix = new Matrix4f()
                .scale(0.1f);

        Node node = new Node(name, AssimpUtils.toMatrix4f(aiNode.mTransformation()).mul(fix), parent);
        nodes.add(node);

        for (int i = 0; i < aiNode.mNumMeshes(); i++) {
            node.getMeshIndexes().add(aiNode.mMeshes().get(i));
        }

        for (int i = 0; i < aiNode.mNumChildren(); i++) {
            AINode childNode = AINode.create(aiNode.mChildren().get(i));
            buildNodeTree(childNode, node, nodes);
        }
        return node;
    }

    public static Material processMaterial(AIMaterial aiMaterial) {
        Material material = new Material(TextureType.DIFFUSE, Model.DEFAULT_TEXTURE);

        AIString materialData = AIString.create();
        Assimp.aiGetMaterialString(aiMaterial, AI_MATKEY_NAME, aiTextureType_NONE, 0, materialData);
        material.setMaterialName(materialData.dataString());


        return material;
    }

    // Mesh Processing Section
    public static Mesh processMesh(AIMesh aiMesh, List<Material> materials, List<Node> nodes) {
        int[] indices = processIndices(aiMesh);
        float[] vertices = processVertices(aiMesh);
        float[] uvs = processUVCoords(aiMesh);
        float[] normals = processNormals(aiMesh);
        int[] boneIndices = new int[vertices.length / 3 * 4];
        float[] boneWeights = new float[vertices.length / 3 * 4];
        Material material = materials.get(aiMesh.mMaterialIndex());

        processBones(aiMesh, nodes, boneIndices, boneWeights);

        return Mesh.create(indices, vertices, uvs, normals, boneIndices, boneWeights, material);
    }

    private static int[] processIndices(AIMesh aiMesh) {
        List<Integer> indices = new ArrayList<>();
        for (AIFace face : aiMesh.mFaces()) {
            for (int i = 0; i < face.mNumIndices(); i++) {
                indices.add(face.mIndices().get(i));
            }
        }
        return indices.stream().mapToInt(Integer::intValue).toArray();
    }

    private static float[] processVertices(AIMesh aiMesh) {
        AIVector3D.Buffer buffer = aiMesh.mVertices();
        float[] data = new float[buffer.remaining() * 3];
        int pos = 0;
        while (buffer.remaining() > 0) {
            AIVector3D vector = buffer.get();
            data[pos++] = vector.x();
            data[pos++] = vector.y();
            data[pos++] = vector.z();
        }
        return data;
    }

    private static float[] processUVCoords(AIMesh aiMesh) {
        AIVector3D.Buffer buffer = aiMesh.mTextureCoords(0);
        float[] data = new float[buffer.remaining() * 2];
        int pos = 0;
        while (buffer.remaining() > 0) {
            AIVector3D vector = buffer.get();
            data[pos++] = vector.x();
            data[pos++] = 1 - vector.y();
        }
        return data;
    }

    private static float[] processNormals(AIMesh aiMesh) {
        AIVector3D.Buffer buffer = aiMesh.mNormals();
        float[] data = new float[buffer.remaining() * 3];
        int pos = 0;
        while (buffer.remaining() > 0) {
            AIVector3D vector = buffer.get();
            data[pos++] = vector.x();
            data[pos++] = vector.y();
            data[pos++] = vector.z();
        }
        return data;
    }

    private static void processBones(AIMesh aiMesh, List<Node> nodes, int[] boneIndices, float[] boneWeights) {
        Arrays.fill(boneIndices, -1);
        Arrays.fill(boneWeights, 0.0f);

        for (int i = 0; i < aiMesh.mNumBones(); i++) {
            AIBone aiBone = AIBone.create(aiMesh.mBones().get(i));
            Node boneNode = findNode(aiBone.mNode(), nodes);

            for (int j = 0; j < aiBone.mNumWeights(); j++) {
                AIVertexWeight aiVertexWeight = aiBone.mWeights().get(j);
                int vertexId = aiVertexWeight.mVertexId();
                float weight = aiVertexWeight.mWeight();

                for (int k = 0; k < 4; k++) {
                    if (boneWeights[vertexId * 4 + k] == 0) {
                        boneIndices[vertexId * 4 + k] = nodes.indexOf(boneNode);
                        boneWeights[vertexId * 4 + k] = weight;
                        break;
                    }
                }
            }
        }
    }

    private static Node findNode(AINode aiNode, List<Node> nodes) {
        for (Node node : nodes) {
            if (AssimpUtils.AINodeEqualsNode(aiNode, node)) {
                return node;
            }
        }
        return null;
    }
}
