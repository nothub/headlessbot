package not.hub.headlessbot;

import not.hub.headlessbot.modules.*;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class ModuleManager {

    private static final Map<Class<? extends Module>, Module> modules = new ConcurrentHashMap<>();

    static {
        add(new AutoTotemModule());
        add(new BaritoneSettingsModule());
        add(new ChatCommandsModule());
        add(new ChatSpamModule());
        add(new RespawnModule());
        add(new StuckDetectorModule());
        add(new SpawnFagBehaviour());
    }

    public static Stream<? extends Module> getModules() {
        return modules.values().stream();
    }

    public static Module get(Class<? extends Module> module) {
        return Optional.of(modules.get(module)).orElseThrow(IllegalStateException::new);
    }

    private static void add(Module module) {
        modules.put(module.getClass(), module);
    }

}
