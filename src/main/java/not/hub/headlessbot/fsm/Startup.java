package not.hub.headlessbot.fsm;

import cc.neckbeard.utils.ExpiringFlag;
import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import me.earth.headlessforge.util.NetworkUtil;
import net.minecraft.util.Session;
import net.minecraftforge.fml.common.FMLCommonHandler;
import not.hub.headlessbot.Bot;
import not.hub.headlessbot.Cooldowns;
import not.hub.headlessbot.Log;
import not.hub.headlessbot.ModuleRegistry;
import not.hub.headlessbot.mixins.IMinecraft;
import not.hub.headlessbot.util.MC;
import org.spongepowered.asm.mixin.MixinEnvironment;

import java.net.Proxy;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static not.hub.headlessbot.Bot.CONFIG;

// TODO: implement FSM
public class Startup implements MC {

    private static final Map<State, Consumer<Boolean>> transitions = new HashMap<>();
    private static State current = State.START;

    static {
        transitions.put(State.START, (success) -> {
            if (success) current = State.INIT_BOT;
            else current = State.PANIC;
            Log.info(Startup.class, "State transition " + State.START.name() + " -> " + current.name());
            current.run();
        });
        transitions.put(State.INIT_BOT, (success) -> {
            if (success) current = State.LOGIN_ACCOUNT;
            else current = State.PANIC;
            Log.info(Startup.class, "State transition " + State.INIT_BOT.name() + " -> " + current.name());
            current.run();
        });
        transitions.put(State.LOGIN_ACCOUNT, (success) -> {
            if (success) current = State.CONNECT_SERVER;
            else current = State.PANIC;
            Log.info(Startup.class, "State transition " + State.LOGIN_ACCOUNT.name() + " -> " + current.name());
            current.run();
        });
        transitions.put(State.CONNECT_SERVER, (success) -> {
            if (success) current = State.QUEUE;
            else current = State.PANIC;
            Log.info(Startup.class, "State transition " + State.CONNECT_SERVER.name() + " -> " + current.name());
            current.run();
        });
        transitions.put(State.QUEUE, (success) -> {
            if (success) current = State.ACTIVE;
            else current = State.CONNECT_SERVER;
            Log.info(Startup.class, "State transition " + State.QUEUE.name() + " -> " + current.name());
            current.run();
        });
        transitions.put(State.ACTIVE, (success) -> {
            if (success) current = State.EXIT;
            else current = State.CONNECT_SERVER;
            Log.info(Startup.class, "State transition " + State.ACTIVE.name() + " -> " + current.name());
            current.run();
        });
    }

    private Startup() {
    }

    /**
     * @return current state
     */
    public static State getCurrent() {
        return current;
    }

    /**
     * Transition to next state
     *
     * @param success success of current state
     */
    public static void transition(boolean success) {
        transitions.get(current).accept(success);
    }

    /**
     * FSM States
     */
    public enum State {

        START(() -> {
            Log.info(Startup.class, "Hello World!");
        }),

        INIT_BOT(() -> {
            System.out.println("\n" +
                "----------------------------------------------------------------" + "\n" +
                "    _,_\n" +
                "  /7/Y/^\\\n" +
                "  vuVV|C)|                         __ _\n" +
                "    \\|^ /                       .'  Y '>,\n" +
                "    )| \\)                      / _   _   \\\n" +
                "   //)|\\\\                      )(_) (_)(|}\n" +
                "  / ^| \\ \\                     {  4A   } /\n" +
                " //^| || \\\\                     \\uLuJJ/\\l\n" +
                "| \"\"\"\"\"  7/>l__ _____ __       /nnm_n//\n" +
                "L>_   _-< 7/|_-__,__-)\\,__)(\".  \\_>-<_/D\n" +
                ")D\" Y \"c)  9)//V       \\_\"-._.__G G_c_.-jjs<\"/ ( \\\n" +
                " | | |  |(|               < \"-._\"> _.G_.___)\\   \\7\\\n" +
                "  \\\"=\" // |              (,\"-.__.|\\ \\<.__.-\" )   \\ \\\n" +
                "   '---'  |              |,\"-.__\"| \\!\"-.__.-\".\\   \\ \\\n" +
                "     |_;._/              (_\"-.__\"'\\ \\\"-.__.-\".|    \\_\\\n" +
                "     )(\" V                \\\"-.__\"'|\\ \\-.__.-\".)     \\ \\\n" +
                "        (                  \"-.__'\"\\_\\ \\.__.-\"./      \\ l\n" +
                "         )                  \".__\"\">>G\\ \\__.-\">        V )\n" +
                "----------------------------------------------------------------" + "\n" +
                "Account:        " + CONFIG.username + "\n" +
                "Server:         " + CONFIG.hostname + "\n" +
                "Obfuscation:    " + MixinEnvironment.getDefaultEnvironment().getObfuscationContext() + "\n" +
                "Modules loaded: " + "\n" +
                ModuleRegistry.getAll().map(m -> "  - " + m.name).collect(Collectors.joining("\n")) + "\n" +
                "----------------------------------------------------------------");
        }),

        LOGIN_ACCOUNT(() -> {
            if (mc.getSession().getUsername().equals(CONFIG.username)) {
                Log.warn(Startup.class, "Already logged in!");
                return;
            }
            // TODO: central place shared between workers for tracking accout login delays?
            YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication) new YggdrasilAuthenticationService(Proxy.NO_PROXY, "").createUserAuthentication(Agent.MINECRAFT);
            auth.setUsername(CONFIG.username);
            auth.setPassword(CONFIG.password);
            try {
                auth.logIn();
                final Session session = new Session(
                    auth.getSelectedProfile().getName(),
                    auth.getSelectedProfile().getId().toString(),
                    auth.getAuthenticatedToken(), "mojang");
                ((IMinecraft) mc).session(session);
                Log.info(Startup.class, "Login Success! Username is " + mc.getSession().getUsername());
                transition(true);
            } catch (AuthenticationUnavailableException e) {
                Log.error(Startup.class, "Login Error! Authentication servers are unavailable (ratelimit/blacklist)! " + e.getMessage());
                transition(false);
            } catch (AuthenticationException e) {
                Log.error(Startup.class, "Login Error! " + e.getMessage());
                transition(false);
            }
        }),

        CONNECT_SERVER(() -> {
            CompletableFuture.supplyAsync(() -> {
                Log.info(Startup.class, "Awaiting connection cooldown for " + CONFIG.hostname);
                Cooldowns.await(Cooldowns.CONNECT, true);
                Log.info(Startup.class, "Starting connection with " + CONFIG.hostname);
                try {
                    mc.addScheduledTask(() -> NetworkUtil.INSTANCE.connect(CONFIG.hostname)).get(10, TimeUnit.SECONDS);
                } catch (InterruptedException | ExecutionException ex) {
                    Log.error(Startup.class, ex.getMessage());
                    return false;
                } catch (TimeoutException ex) {
                    Log.warn(Startup.class, "Timeout after 10 seconds: " + ex.getMessage());
                    return false;
                }
                if (mc.getCurrentServerData() != null) {
                    Log.info(Startup.class, "Connected to server " + mc.getCurrentServerData().serverIP);
                    Bot.WEBHOOK.info("Connected to server " + mc.getCurrentServerData().serverIP);
                    return true;
                }
                Log.error(Startup.class, "Server connection failed!");
                return false;
            }).thenAccept(Startup::transition);
        }),

        QUEUE(() -> {
            Bot.CONTROLLER.deactivate();
            Log.info(Startup.class, "Checking for queue...");
            CompletableFuture.supplyAsync(() -> {
                ExpiringFlag notificationCooldown = new ExpiringFlag(1, ChronoUnit.MINUTES, false);
                while (true) {
                    Cooldowns.await(1, ChronoUnit.SECONDS);
                    if (mc.getCurrentServerData() == null) {
                        Log.warn(Startup.class, "Server connection lost...");
                        return false;
                    }
                    if (mc.player == null) continue;
                    if (mc.player.capabilities.isFlying) {
                        if (notificationCooldown.isExpired()) {
                            notificationCooldown.reset();
                            Log.info(Startup.class, CONFIG.hostname + " is full...");
                        }
                        continue;
                    }
                    Bot.WEBHOOK.info("Ready on " + mc.getCurrentServerData().serverIP);
                    return true;
                }
            }).thenAccept(Startup::transition);
        }),

        ACTIVE(Bot.CONTROLLER::activate),

        PANIC(() -> {
            Log.error(Startup.class, "PANIC! cya...");
            Log.info(Startup.class, "Exiting with code 1");
            FMLCommonHandler.instance().exitJava(1, false);
        }),

        EXIT(() -> {
            Log.info(Startup.class, "Exiting with code 0");
            FMLCommonHandler.instance().exitJava(0, false);
        }),
        ;

        private final Runnable r;

        State(Runnable r) {
            this.r = r;
        }

        private void run() {
            Log.info(Startup.class, "Executing " + this.name());
            r.run();
        }

    }

}
