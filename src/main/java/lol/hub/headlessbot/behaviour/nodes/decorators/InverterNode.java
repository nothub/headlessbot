package lol.hub.headlessbot.behaviour.nodes.decorators;

import lol.hub.headlessbot.behaviour.State;
import lol.hub.headlessbot.behaviour.nodes.Node;

import static lol.hub.headlessbot.behaviour.State.*;

/**
 * NOT
 */
public final class InverterNode extends DecoratorNode {
    public InverterNode(Node child) {
        super(child);
    }

    @Override
    public State tick() {
        switch (child().tick()) {
            case SUCCESS -> {
                return FAILURE;
            }
            case FAILURE -> {
                return SUCCESS;
            }
        }
        return RUNNING;
    }
}
