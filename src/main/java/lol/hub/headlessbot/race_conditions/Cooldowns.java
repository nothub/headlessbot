package lol.hub.headlessbot.race_conditions;

import java.time.temporal.ChronoUnit;

public class Cooldowns {

    public static final ExpiringFlag baritone = new ExpiringFlag(
        4,
        ChronoUnit.SECONDS,
        false
    );

    public static final ExpiringFlag respawn = new ExpiringFlag(
        1,
        ChronoUnit.SECONDS,
        false
    );

}
