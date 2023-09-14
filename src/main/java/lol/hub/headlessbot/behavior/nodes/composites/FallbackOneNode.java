package lol.hub.headlessbot.behavior.nodes.composites;

import lol.hub.headlessbot.behavior.State;
import lol.hub.headlessbot.behavior.nodes.Node;

import static lol.hub.headlessbot.behavior.State.*;

/**
 * OR
 * Execute children in order, one per tick,
 * until a child returns SUCCESS.
 */
public final class FallbackOneNode extends CompositeNode {
    private int index = 0;

    public FallbackOneNode(Node... children) {
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
