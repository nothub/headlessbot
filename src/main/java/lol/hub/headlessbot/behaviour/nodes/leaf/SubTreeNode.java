package lol.hub.headlessbot.behaviour.nodes.leaf;

import lol.hub.headlessbot.behaviour.BehaviourTree;
import lol.hub.headlessbot.behaviour.State;

public final class SubTreeNode extends LeafNode {
    private final BehaviourTree tree;

    public SubTreeNode(BehaviourTree tree) {
        this.tree = tree;
    }

    @Override
    public State run() {
        return tree.run();
    }
}
