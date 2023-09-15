package lol.hub.headlessbot.behavior.nodes.leafs;

import lol.hub.headlessbot.behavior.State;

import static lol.hub.headlessbot.behavior.State.FAILURE;

public final class FailNode extends LeafNode {
    @Override
    public State tick() {
        return FAILURE;
    }
}
