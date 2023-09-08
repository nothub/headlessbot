package lol.hub.headlessbot.behaviour.nodes.composites;

import lol.hub.headlessbot.behaviour.State;
import lol.hub.headlessbot.behaviour.nodes.Node;

import static lol.hub.headlessbot.behaviour.State.*;

/**
 * OR
 * Execute children in order until a child returns success.
 */
public final class FallbackNode extends CompositeNode {
    public FallbackNode(Node... children) {
        super(children);
    }

    @Override
    public State tick() {
        switch (next().tick()) {
            case SUCCESS -> {
                reset();
                return SUCCESS;
            }
            case FAILURE -> {
                if (isEnd()) {
                    reset();
                    return FAILURE;
                }
            }
        }
        return RUNNING;
    }
}
