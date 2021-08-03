package not.hub.headlessbot;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import not.hub.headlessbot.modules.LoginModule;
import not.hub.headlessbot.modules.Module;
import org.spongepowered.asm.mixin.MixinEnvironment;

import java.util.stream.Collectors;


@Mod(modid = Bot.MODID, name = Bot.MODID, version = Bot.VERSION, clientSideOnly = true, acceptableRemoteVersions = "*")
public class Bot {

    public static final String MODID = "headlessbot";
    public static final String VERSION = "0.0.0-SNAPSHOT";
    public static final Config CONFIG = Config.loadConfig();

    @Instance(value = MODID)
    public static Bot INSTANCE;

    @Mod.EventHandler
    public void fmlInit(FMLPreInitializationEvent event) {
        Log.info("FML init state", "PRE_INIT");
        Log.setLogger(event.getModLog());
    }

    @Mod.EventHandler
    public void fmlInit(FMLInitializationEvent event) {
        Log.info("FML init state", "INIT");
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventHandler
    public void fmlInit(FMLPostInitializationEvent event) {
        Log.info("FML init state", "POST_INIT");

        System.out.println("\n" +
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
            "Account:     " + CONFIG.username + "\n" +
            "Server:      " + CONFIG.hostname + "\n" +
            "Obfuscation: " + MixinEnvironment.getDefaultEnvironment().getObfuscationContext() + "\n" +
            "Modules loaded: " + "\n" +
            ModuleManager.getModules().map(m -> "  - " + m.name).collect(Collectors.joining("\n")) + "\n" +
            "----------------------------------------------------------------");

        ModuleManager
            .getAll(Module.Type.ALWAYS_ACTIVE)
            .forEach(Module::activate);

        ModuleManager.get(LoginModule.class).activate();
    }

}
