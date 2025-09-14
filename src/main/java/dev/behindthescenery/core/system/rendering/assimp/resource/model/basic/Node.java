package dev.behindthescenery.core.system.rendering.assimp.resource.model.basic;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.behindthescenery.core.utils.ExternCodecs;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public class Node {

    public static final Codec<Node> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.STRING.fieldOf("name").forGetter(Node::getName),
                    ExternCodecs.MATRIX_4_F_CODEC.fieldOf("transformation").forGetter(Node::getTransform)
            ).apply(instance, Node::new));

    private final String name;
    private Node parent;

    private boolean visible = true;

    private final Matrix4f initialTransform;
    private final Matrix4f relativeTransform;

    private final List<Node> children = new ArrayList<>();
    private final List<Integer> meshIndexes = new ArrayList<>();

    public Node(String name, Matrix4f transformation, Node parent) {
        this.name = name;
        this.parent = parent;
        this.initialTransform = new Matrix4f(transformation);
        this.relativeTransform = new Matrix4f(transformation);

        if (parent != null)
            parent.children.add(this);
    }

    public Node(String name, Matrix4f transformation) {
        this.name = name;
//        this.parent = parents.getFirst();
        this.initialTransform = new Matrix4f(transformation);
        this.relativeTransform = new Matrix4f(transformation);

//        parent.children.addAll(parents);
    }

    public Node findNode(String name) {
        for (Node child : this.children) {
            if (child.name.equals(name)) {
                return child;
            }
        }

        for (Node child : this.children) {
            Node foundNode = child.findNode(name);
            if (foundNode != null) return foundNode;
        }
        return null;
    }

    protected Matrix4f getTransform() {
        return initialTransform;
    }

    public String getName() {
        return name;
    }

    public Node getParent() {
        return parent;
    }

    public List<Node> getChildren() {
        return children;
    }

    public List<Integer> getMeshIndexes() {
        return meshIndexes;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    /**
     * Возвращает начальное относительное преобразование этого узла.
     * Это преобразование — преобразование узла при его создании.
     *
     * @return первоначальное преобразование этого узла
     */
    public Matrix4f getInitialTransform() {
        return initialTransform;
    }

    /**
     * Возвращает относительное преобразование этого узла.
     * Это преобразование относительно родительского узла.
     *
     * @return относительное преобразование этого узла
     */
    public Matrix4f getRelativeTransform() {
        return relativeTransform;
    }

    /**
     * Возвращает начальное глобальное преобразование этого узла.
     * Это преобразование относительно корневого узла.
     *
     * @return первоначальное глобальное преобразование этого узла
     */
    public Matrix4f getInitialGlobalTransform() {
        Matrix4f globalTransform = new Matrix4f();
        if (getParent() != null) {
            globalTransform.mul(getParent().getInitialGlobalTransform());
        }
        globalTransform.mul(getInitialTransform());
        return globalTransform;
    }

    /**
     * Возвращает глобальное преобразование этого узла.
     * Это преобразование относительно корневого узла.
     *
     * @return глобальная трансформация этого узла
     */
    public Matrix4f getGlobalTransform() {
        Matrix4f globalTransform = new Matrix4f();
        if (getParent() != null) {
            globalTransform.mul(getParent().getGlobalTransform());
        }
        globalTransform.mul(getRelativeTransform());
        return globalTransform;
    }

    /**
     * Задаёт локальное преобразование этого узла.
     * Это преобразование относительно родительского узла.
     *
     * @param transform новая локальная трансформация
     */
    public void setLocalTransform(Matrix4f transform) {
        this.relativeTransform.set(transform);
    }

    public Matrix4f getLocalTransform() {
        return this.relativeTransform.mul(new Matrix4f(initialTransform).invert(), new Matrix4f());
    }
}
