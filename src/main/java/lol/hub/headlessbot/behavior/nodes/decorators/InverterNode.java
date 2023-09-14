package lol.hub.headlessbot.behavior.nodes.decorators;

import lol.hub.headlessbot.behavior.State;
import lol.hub.headlessbot.behavior.nodes.Node;

import static lol.hub.headlessbot.behavior.State.*;

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
