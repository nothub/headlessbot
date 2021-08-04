package not.hub.headlessbot;

import not.hub.headlessbot.modules.*;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ModuleManager {

    private static final Map<Class<? extends Module>, Module> modules = new ConcurrentHashMap<>();

    static {
        add(new AutoTotemModule());
        add(new BaritoneSettingsModule());
        add(new ChatCommandsModule());
        add(new ChatSpamModule());
        add(new LoginModule());
        add(new ReconnectModule());
        add(new RespawnModule());
        add(new StuckDetectorModule());
        add(new WalkingSimulatorModule());
    }

    public static Stream<? extends Module> getModules() {
        return modules.values().stream();
    }

    public static Set<Module> getAll(Module.Type type) {
        return getModules().filter(m -> m.type == type).collect(Collectors.toSet());
    }

    public static Module get(Class<? extends Module> module) {
        return Optional.of(modules.get(module)).orElseThrow(IllegalStateException::new);
    }

    private static void add(Module module) {
        modules.put(module.getClass(), module);
    }

}
