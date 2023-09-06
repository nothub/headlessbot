package lol.hub.headlessbot.behaviour;

import lol.hub.headlessbot.behaviour.nodes.RootNode;

public class BehaviourTree {

    /* https://www.gamedeveloper.com/programming/behavior-trees-for-ai-how-they-work */

    // TODO: tick based executor

    private final RootNode root;

    public BehaviourTree(RootNode node) {
        this.root = node;
    }

    public State run() {
        return this.root.run();
    }
}
