package lol.hub.headlessbot.behavior;

import lol.hub.headlessbot.behavior.nodes.Node;
import lol.hub.headlessbot.behavior.nodes.composites.CompositeNode;
import lol.hub.headlessbot.behavior.nodes.decorators.DecoratorNode;
import lol.hub.headlessbot.behavior.nodes.leafs.LeafNode;

public class BehaviorTree implements Tickable {
    /* https://www.gamedeveloper.com/programming/behavior-trees-for-ai-how-they-work */

    private final Node root;

    public BehaviorTree(Node node) {
        this.root = node;
    }

    public Node root() {
        return root;
    }

    @Override
    public State tick() {
        return root.tick();
    }

    public void validate() {
        if (root == null) {
            throw new AssertionError("tree has no root node");
        }
        validate(root);
    }

    public void validate(Node node) {

        /* java 21 feature:
        switch (node) {
            case null -> {

            }
            case CompositeNode n -> {

            }
            case DecoratorNode n -> {

            }
            case LeafNode n -> {

            }
            default -> throw new NotImplementedException();
        }
        */

        if (node instanceof CompositeNode n) {
            if (n.children().size() < 2) {
                throw new AssertionError("composite node with less then 2 children");
            }
            for (Node c : n.children()) {
                if (c == null) {
                    throw new AssertionError("composite node has null child");
                }
                validate(c);
            }
        } else if (node instanceof DecoratorNode n) {
            if (n.child() == null) {
                throw new AssertionError("decorator node without child");
            }
            validate(n.child());
        } else if (node instanceof LeafNode n) {


        }
    }
}
