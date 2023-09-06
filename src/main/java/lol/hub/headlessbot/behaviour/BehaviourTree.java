package lol.hub.headlessbot.behaviour;

import lol.hub.headlessbot.behaviour.nodes.decorators.RootNode;

public class BehaviourTree {

    /* https://www.gamedeveloper.com/programming/behavior-trees-for-ai-how-they-work */

    private final RootNode root;

    public BehaviourTree(RootNode node) {
        this.root = node;
    }

    public State run() {
        return this.root.run();
    }
}
