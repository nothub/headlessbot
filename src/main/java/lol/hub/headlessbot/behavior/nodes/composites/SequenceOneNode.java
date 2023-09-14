package lol.hub.headlessbot.behavior.nodes.composites;

import lol.hub.headlessbot.behavior.State;
import lol.hub.headlessbot.behavior.nodes.Node;

import static lol.hub.headlessbot.behavior.State.*;

/**
 * AND
 * Execute children in order, one per tick,
 * until a child returns FAILURE.
 */
public final class SequenceOneNode extends CompositeNode {
    private int index = 0;

    public SequenceOneNode(Node... children) {
        super(children);
    }

    @Override
    public synchronized State tick() {
        switch (children().get(index++).tick()) {
            case SUCCESS -> {
                if (index >= children().size()) {
                    index = 0;
                    return SUCCESS;
                }
            }
            case FAILURE -> {
                index = 0;
                return FAILURE;
            }
        }
        return RUNNING;
    }
}
