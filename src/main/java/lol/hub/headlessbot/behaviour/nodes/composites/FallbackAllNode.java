package lol.hub.headlessbot.behaviour.nodes.composites;

import lol.hub.headlessbot.behaviour.State;
import lol.hub.headlessbot.behaviour.nodes.Node;

import static lol.hub.headlessbot.behaviour.State.FAILURE;
import static lol.hub.headlessbot.behaviour.State.SUCCESS;

/**
 * OR
 * Execute children in order, all in one tick, until a child returns success.
 */
public final class FallbackAllNode extends CompositeNode {
    public FallbackAllNode(Node... children) {
        super(children);
    }

    @Override
    public synchronized State tick() {
        for (Node child : children()) {
            if (child.tick() == SUCCESS) {
                return SUCCESS;
            }
        }
        return FAILURE;
    }
}
