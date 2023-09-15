package lol.hub.headlessbot.behavior.nodes.leafs;

import lol.hub.headlessbot.Log;
import lol.hub.headlessbot.MC;
import lol.hub.headlessbot.race_conditions.Cooldowns;

import static lol.hub.headlessbot.behavior.State.RUNNING;
import static lol.hub.headlessbot.behavior.State.SUCCESS;

public class RespawnNode extends McNode {
    public RespawnNode() {
        super(mc -> {
            if (Cooldowns.respawn.isActive()) {
                return RUNNING;
            }

            Log.info("requesting respawn");
            MC.player().requestRespawn();
            Cooldowns.respawn.reset();
            return SUCCESS;
        });
    }
}
