package lol.hub.headlessbot.behavior.nodes.leafs;

import lol.hub.headlessbot.Log;
import lol.hub.headlessbot.MC;
import lol.hub.headlessbot.Mod;

import static lol.hub.headlessbot.behavior.State.RUNNING;

public class RespawnNode extends McNode {
    public RespawnNode() {
        super(mc -> {
            // We do not want to spam the server,
            // so we just run this every 20 ticks.
            if (Mod.ticksOnline % 20 != 0) return RUNNING;

            // Respawn does not happen immediately so we return RUNNING
            Log.info("requesting respawn");
            MC.player().requestRespawn();
            return RUNNING;
        });
    }
}
