package lol.hub.headlessbot.race_conditions;

import java.time.temporal.ChronoUnit;

public class Cooldowns {

    public static final ExpiringFlag baritone = new ExpiringFlag(3,
        ChronoUnit.SECONDS);

}
