package lol.hub.headlessbot.behaviour.nodes.leaf;

import lol.hub.headlessbot.behaviour.BehaviourTree;
import lol.hub.headlessbot.behaviour.State;
import lol.hub.headlessbot.behaviour.nodes.Node;

public class SubTreeNode extends LeafNode {
    private final BehaviourTree tree;

    public SubTreeNode(Node parent, BehaviourTree tree) {
        super(parent);
        this.tree = tree;
    }

    @Override
    public State run() {
        return tree.run();
    }
}
