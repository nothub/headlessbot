package lol.hub.headlessbot.behavior.nodes.leafs;

import lol.hub.headlessbot.behavior.BehaviorTree;
import lol.hub.headlessbot.behavior.State;

public final class SubTreeNode extends LeafNode {
    private final BehaviorTree tree;

    public SubTreeNode(BehaviorTree tree) {
        this.tree = tree;
    }

    @Override
    public State tick() {
        return tree.tick();
    }
}
