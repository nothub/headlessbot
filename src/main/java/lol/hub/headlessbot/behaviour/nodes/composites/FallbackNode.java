package lol.hub.headlessbot.behaviour.nodes.composites;

import lol.hub.headlessbot.behaviour.State;
import lol.hub.headlessbot.behaviour.nodes.Node;

import static lol.hub.headlessbot.behaviour.State.*;

/**
 * OR
 * Execute children in order, one per tick, until a child returns success.
 */
public final class FallbackNode extends CompositeNode {
    private int index = 0;

    public FallbackNode(Node... children) {
        super(children);
    }

    @Override
    public synchronized State tick() {
        switch (children().get(index++).tick()) {
            case SUCCESS -> {
                index = 0;
                return SUCCESS;
            }
            case FAILURE -> {
                if (index >= children().size()) {
                    index = 0;
                    return FAILURE;
                }
            }
        }
        return RUNNING;
    }
}
