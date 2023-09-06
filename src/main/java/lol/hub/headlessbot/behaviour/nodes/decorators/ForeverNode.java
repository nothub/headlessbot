package lol.hub.headlessbot.behaviour.nodes.decorators;

import lol.hub.headlessbot.behaviour.State;
import lol.hub.headlessbot.behaviour.nodes.Node;

// WHILE TRUE
public class ForeverNode extends DecoratorNode {
    private final boolean exitOnFail;

    public ForeverNode(Node child, boolean exitOnFail) {
        super(child);
        this.exitOnFail = exitOnFail;
    }

    @Override
    public State run() {
        while (true) {
            var result = child().run();
            if (result == State.FAILURE && exitOnFail) {
                return State.FAILURE;
            }
        }
    }
}
