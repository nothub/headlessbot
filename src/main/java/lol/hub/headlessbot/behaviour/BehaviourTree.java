package lol.hub.headlessbot.behaviour;

import lol.hub.headlessbot.behaviour.nodes.Node;
import lol.hub.headlessbot.behaviour.nodes.composites.CompositeNode;
import lol.hub.headlessbot.behaviour.nodes.decorators.DecoratorNode;
import lol.hub.headlessbot.behaviour.nodes.leafs.LeafNode;

public class BehaviourTree implements Tickable {
    /* https://www.gamedeveloper.com/programming/behavior-trees-for-ai-how-they-work */

    private final Node root;

    public BehaviourTree(Node node) {
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
