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
        modules.put(AutoTotemModule.class, new AutoTotemModule());
        modules.put(ChatCommandsModule.class, new ChatCommandsModule());
        modules.put(ChatSpamModule.class, new ChatSpamModule());
        modules.put(LoginModule.class, new LoginModule());
        modules.put(RespawnModule.class, new RespawnModule());
        modules.put(WalkingSimulatorModule.class, new WalkingSimulatorModule());
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

}
