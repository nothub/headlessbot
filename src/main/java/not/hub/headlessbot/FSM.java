package not.hub.headlessbot;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import me.earth.headlessforge.util.NetworkUtil;
import net.minecraft.util.Session;
import net.minecraftforge.fml.common.FMLCommonHandler;
import not.hub.headlessbot.mixins.IMinecraft;
import not.hub.headlessbot.modules.Module;
import not.hub.headlessbot.util.MC;
import org.spongepowered.asm.mixin.MixinEnvironment;

import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static not.hub.headlessbot.Bot.CONFIG;

// TODO: implement not.hub.headlessbot.fsm.FSM
public class FSM implements MC {

    private static final Map<State, Consumer<Boolean>> transitions = new HashMap<>();
    private static State current = State.START;

    static {
        transitions.put(State.START, (success) -> {
            if (success) current = State.INIT_BOT;
            else current = State.PANIC;
            current.run();
        });
        transitions.put(State.INIT_BOT, (success) -> {
            if (success) current = State.LOGIN_ACCOUNT;
            else current = State.PANIC;
            current.run();
        });
        transitions.put(State.LOGIN_ACCOUNT, (success) -> {
            if (success) current = State.CONNECT_SERVER;
            else current = State.PANIC;
            current.run();
        });
        transitions.put(State.CONNECT_SERVER, (success) -> {
            if (success) current = State.ACTIVE;
            else current = State.PANIC;
            current.run();
        });
        transitions.put(State.ACTIVE, (success) -> {
            if (success) current = State.EXIT;
            else current = State.CONNECT_SERVER;
            current.run();
        });
    }

    private FSM() {
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
            Log.info(FSM.class, "Hello World!");
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
                ModuleManager.getModules().map(m -> "  - " + m.name).collect(Collectors.joining("\n")) + "\n" +
                "----------------------------------------------------------------");
        }),

        LOGIN_ACCOUNT(() -> {
            if (mc.getSession().getUsername().equals(CONFIG.username)) {
                Log.warn(FSM.class, "Already logged in!");
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
                Log.info(FSM.class, "Login Success! Username is " + mc.getSession().getUsername());
                transition(true);
            } catch (AuthenticationUnavailableException e) {
                Log.error(FSM.class, "Login Error! Authentication servers are unavailable (ratelimit/blacklist)! " + e.getMessage());
                transition(false);
            } catch (AuthenticationException e) {
                Log.error(FSM.class, "Login Error! " + e.getMessage());
                transition(false);
            }
        }),

        CONNECT_SERVER(() -> {
            CompletableFuture.supplyAsync(() -> {
                Log.info(FSM.class, "Awaiting connection cooldown for " + CONFIG.hostname);
                Cooldowns.await(Cooldowns.CONNECT, true);
                Log.info(FSM.class, "Starting connection with " + CONFIG.hostname);
                try {
                    mc.addScheduledTask(() -> NetworkUtil.INSTANCE.connect(CONFIG.hostname)).get(10, TimeUnit.SECONDS);
                } catch (InterruptedException | ExecutionException ex) {
                    Log.error(FSM.class, ex.getMessage());
                    return false;
                } catch (TimeoutException ex) {
                    Log.warn(FSM.class, "Timeout after 10 seconds: " + ex.getMessage());
                    return false;
                }
                if (mc.getCurrentServerData() != null) {
                    Log.info(FSM.class, "Connected to server " + mc.getCurrentServerData().serverIP);
                    return true;
                }
                Log.error(FSM.class, "Server connection failed!");
                return false;
            }).thenAccept(FSM::transition);
        }),

        ACTIVE(() -> {
            ModuleManager.getModules().forEach(Module::activate);
            CompletableFuture.supplyAsync(() -> {
                while (mc.getCurrentServerData() != null) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException ex) {
                        Log.error(FSM.class, ex.getMessage());
                    }
                    if (Bot.isShutdown()) return true;
                }
                Log.info(FSM.class, "Server connection lost...");
                return false;
            }).thenAccept(FSM::transition);
        }),

        PANIC(() -> {
            Log.error(FSM.class, "PANIC! cya...");
            Log.info(FSM.class, "Exiting with code 1");
            FMLCommonHandler.instance().exitJava(1, false);
        }),

        EXIT(() -> {
            Log.info(FSM.class, "Exiting with code 0");
            FMLCommonHandler.instance().exitJava(0, false);
        }),
        ;

        private final Runnable r;

        State(Runnable r) {
            this.r = r;
        }

        private void run() {
            Log.info(FSM.class, "Executing " + this.name());
            r.run();
        }

    }

}
