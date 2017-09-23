/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2017 Team Pepsi
 *
 * Permission is hereby granted to any persons and/or organizations using this software to copy, modify, merge, publish, and distribute it.
 * Said persons and/or organizations are not allowed to use the software or any derivatives of the work for commercial use or any other means to generate income, nor are they allowed to claim this software as their own.
 *
 * The persons and/or organizations are also disallowed from sub-licensing and/or trademarking this software without explicit permission from Team Pepsi.
 *
 * Any persons and/or organizations using this software must disclose their source code and have it publicly available, include this license, provide sufficient credit to the original authors of the project (IE: Team Pepsi), as well as provide a link to the original project.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.daporkchop.pepsimod;

import net.daporkchop.pepsimod.clickgui.ClickGUI;
import net.daporkchop.pepsimod.clickgui.Window;
import net.daporkchop.pepsimod.clickgui.api.IEntry;
import net.daporkchop.pepsimod.command.CommandRegistry;
import net.daporkchop.pepsimod.command.impl.*;
import net.daporkchop.pepsimod.command.impl.waypoint.*;
import net.daporkchop.pepsimod.event.GuiRenderHandler;
import net.daporkchop.pepsimod.event.MiscEventHandler;
import net.daporkchop.pepsimod.gui.clickgui.*;
import net.daporkchop.pepsimod.key.KeyRegistry;
import net.daporkchop.pepsimod.module.ModuleManager;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.module.api.ModuleOptionSave;
import net.daporkchop.pepsimod.module.api.ModuleSortType;
import net.daporkchop.pepsimod.module.impl.combat.AuraMod;
import net.daporkchop.pepsimod.module.impl.combat.AutoTotemMod;
import net.daporkchop.pepsimod.module.impl.combat.CriticalsMod;
import net.daporkchop.pepsimod.module.impl.combat.CrystalAuraMod;
import net.daporkchop.pepsimod.module.impl.misc.*;
import net.daporkchop.pepsimod.module.impl.movement.*;
import net.daporkchop.pepsimod.module.impl.player.*;
import net.daporkchop.pepsimod.module.impl.render.*;
import net.daporkchop.pepsimod.util.*;
import net.daporkchop.pepsimod.util.datatag.DataTag;
import net.daporkchop.pepsimod.util.misc.waypoints.Waypoints;
import net.daporkchop.pepsimod.util.module.*;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLStateEvent;
import org.lwjgl.input.Keyboard;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimerTask;

@Mod(modid = "pepsimoddev", name = "pepsimod - Dev", version = "11.1-dev")
public class PepsiMod {
    public static final String VERSION = "11.1";
    public static final String chatPrefix = PepsiUtils.COLOR_ESCAPE + "0" + PepsiUtils.COLOR_ESCAPE + "l[" + PepsiUtils.COLOR_ESCAPE + "c" + PepsiUtils.COLOR_ESCAPE + "lpepsi" + PepsiUtils.COLOR_ESCAPE + "9" + PepsiUtils.COLOR_ESCAPE + "lmod" + PepsiUtils.COLOR_ESCAPE + "0" + PepsiUtils.COLOR_ESCAPE + "l]" + PepsiUtils.COLOR_ESCAPE + "r ";
    public static PepsiMod INSTANCE;
    public boolean isMcLeaksAccount = false;
    public Session originalSession = null;
    public Minecraft mc;
    public DataTag dataTag = null;
    public boolean hasInitializedModules = false;
    public TargetSettings targetSettings;
    public ESPSettings espSettings;
    public NoWeatherSettings noWeatherSettings;
    public TracerSettings tracerSettings;
    public MiscOptions miscOptions;
    public boolean isInitialized = false;
    public HUDSettings hudSettings;
    public ElytraFlySettings elytraFlySettings;
    public AnnouncerSettings announcerSettings;
    public Waypoints waypoints;

    public static void registerModules(FMLStateEvent event) {
        ModuleManager.registerModule(new NoFallMod(false, -1, false));
        ModuleManager.registerModule(new AntiHungerMod(false, -1, false));
        ModuleManager.registerModule(new FullbrightMod(false, -1, false));
        ModuleManager.registerModule(new CriticalsMod(false, -1, false));
        ModuleManager.registerModule(new AuraMod(false, -1, false));
        ModuleManager.registerModule(new VelocityMod(false, -1, false));
        ModuleManager.registerModule(new TimerMod(false, -1, false));
        ModuleManager.registerModule(new XrayMod(false, -1, false));
        ModuleManager.registerModule(new AntiBlindMod(false, -1, false));
        ModuleManager.registerModule(new StorageESPMod(false, -1, false));
        ModuleManager.registerModule(new FreecamMod(false, -1, false));
        ModuleManager.registerModule(new HealthTagsMod(false, -1, false));
        ModuleManager.registerModule(new NameTagsMod(false, -1, false));
        ModuleManager.registerModule(new NoHurtCamMod(false, -1, false));
        ModuleManager.registerModule(new NoOverlayMod(false, -1, false));
        ModuleManager.registerModule(new NoWeatherMod(false, -1, false));
        ModuleManager.registerModule(new AntiInvisibleMod(false, -1, false));
        ModuleManager.registerModule(new TrajectoriesMod(false, -1, false));
        ModuleManager.registerModule(new TracersMod(false, -1, false));
        ModuleManager.registerModule(new ClickGuiMod(false, Keyboard.KEY_RSHIFT, false));
        ModuleManager.registerModule(new HUDMod(true, -1, true));
        ModuleManager.registerModule(new ZoomMod(false, -1, true));
        ModuleManager.registerModule(new ElytraFlyMod(false, -1, false));
        ModuleManager.registerModule(new AutoWalkMod(false, -1, false));
        ModuleManager.registerModule(new EntitySpeedMod(false, -1, false));
        ModuleManager.registerModule(new SafewalkMod(false, -1, false));
        ModuleManager.registerModule(new InventoryMoveMod(false, -1, false)); //TODO: make this work
        ModuleManager.registerModule(new HorseJumpPowerMod(false, -1, false));
        ModuleManager.registerModule(new AutoTotemMod(false, -1, false));
        ModuleManager.registerModule(new CrystalAuraMod(false, -1, false));
        ModuleManager.registerModule(new AntiTotemAnimationMod(false, -1, false));
        ModuleManager.registerModule(new AnnouncerMod(false, -1, false));
        ModuleManager.registerModule(new AutoRespawnMod(false, -1, false));
        //ModuleManager.registerModule(new EntityStepMod(false, -1, false)); //TODO: fix or remove
        ModuleManager.registerModule(new JesusMod(false, -1, false)); //test
        ModuleManager.registerModule(new SprintMod(false, -1, false));
        ModuleManager.registerModule(new NoSlowdownMod(false, -1, false));
        ModuleManager.registerModule(new FlightMod(false, -1, false));
        ModuleManager.registerModule(new FastPlaceMod(false, -1, false));
        ModuleManager.registerModule(new SpeedmineMod(false, -1, false));
        ModuleManager.registerModule(new AutoEatMod(false, -1, false));
        ModuleManager.registerModule(new StepMod(false, -1, false));
        ModuleManager.registerModule(new AutoMineMod(false, -1, false));
        ModuleManager.registerModule(new ScaffoldMod(false, -1, false));
        ModuleManager.registerModule(new UnfocusedCPUMod(false, -1, false));
        ModuleManager.registerModule(new ESPMod(false, -1, false));
        ModuleManager.registerModule(new WaypointsMod(false, -1, false));
    }

    public static void registerCommands(FMLStateEvent event) {
        CommandRegistry.registerCommand(new HelpCommand());
        CommandRegistry.registerCommand(new SetRotCommand());
        CommandRegistry.registerCommand(new ToggleCommand());
        CommandRegistry.registerCommand(new SortModulesCommand());
        CommandRegistry.registerCommand(new SaveCommand());
        CommandRegistry.registerCommand(new ListCommand());
        CommandRegistry.registerCommand(new InvSeeCommand());
        CommandRegistry.registerCommand(new PeekCommand());
        CommandRegistry.registerCommand(new WaypointAddCommand());
        CommandRegistry.registerCommand(new WaypointClearCommand());
        CommandRegistry.registerCommand(new WaypointHardClearCommand());
        CommandRegistry.registerCommand(new WaypointHideCommand());
        CommandRegistry.registerCommand(new WaypointListCommand());
        CommandRegistry.registerCommand(new WaypointRemoveCommand());
        CommandRegistry.registerCommand(new WaypointShowCommand());
        CommandRegistry.registerCommand(new GoToCommand());
    }

    /**
     * Probably unneeded, as pepsimod is client-only
     *
     * @return a java.io.File with the .minecraft directory
     */
    public static File getWorkingFolder() {
        File toBeReturned;
        try {
            if (FMLCommonHandler.instance().getSide().isClient()) {
                toBeReturned = Minecraft.getMinecraft().mcDataDir;
            } else {
                toBeReturned = FMLCommonHandler.instance().getMinecraftServerInstance().getFile("");
            }
            return toBeReturned;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        event.getModLog().info("launching preInit");
        INSTANCE = this;
        MinecraftForge.EVENT_BUS.register(new KeyRegistry());
        event.getModLog().info("setting MC instance!");
        this.mc = Minecraft.getMinecraft();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        new ClickGUI();
        loadConfig();
        registerModules(event);

        initModules();

        ClickGUI.INSTANCE.setWindows(
                new WindowRender(),
                new WindowCombat(),
                new WindowMisc(),
                new WindowMovement(),
                new WindowPlayer()
        );

        for (Window window : ClickGUI.INSTANCE.windows) {
            int[] data = dataTag.getIntegerArray(window.text + "¦window", new int[]{window.getX(), window.getY(), 0});
            window.setX(data[0]);
            window.setY(data[1]);
            window.isOpen = (data[2] == 1);
            for (IEntry entry : window.entries) {
                data = dataTag.getIntegerArray(window.text + "¦window" + entry.getName(), new int[]{0});
                entry.setOpen(data[0] == 1);
            }
        }

        //save the tag in case new fields are added, this way they are saved right away
        dataTag.save();

        registerCommands(event);
        initModules();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        ReflectionStuff.init();
        if (ImageUtils.imgs == null) {
            ImageUtils.imgs = new HashMap<>();
            for (int i = 0; i < ImageUtils.names.length; i++) {
                String s = ImageUtils.names[i];
                ImageUtils.imgs.put(i, new ResourceLocation("pepsimod", "textures/" + s));
            }
        }
        MinecraftForge.EVENT_BUS.register(new GuiRenderHandler());
        MinecraftForge.EVENT_BUS.register(new MiscEventHandler());
        PepsiUtils.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                PepsiMod.INSTANCE.saveConfig();
            }
        }, 360000, 360000);
    }

    public void loadConfig() {
        File file = new File(PepsiMod.getWorkingFolder(), "/pepsimod.dat");

        if (!file.exists()) {
            dataTag = new DataTag(file);
            for (Module module : ModuleManager.AVALIBLE_MODULES) {
                ModuleOption[] options = module.defaultOptions();
                ModuleOptionSave[] saved = new ModuleOptionSave[options.length];
                for (int i = 0; i < options.length; i++) {
                    saved[i] = new ModuleOptionSave(options[i]);
                }
                dataTag.setSerializableArray("settings" + module.nameFull, saved);
            }
            dataTag.setSerializable("friends", new HashMap<String, Friend>());
            dataTag.save();
        } else {
            dataTag = new DataTag(file);
        }

        Friends.FRIENDS = (HashMap<String, Friend>) dataTag.getSerializable("friends", new HashMap<String, Friend>());
        ModuleManager.sortType = (ModuleSortType) dataTag.getSerializable("sortType", ModuleSortType.SIZE);
        targetSettings = (TargetSettings) dataTag.getSerializable("targetSettings", new TargetSettings());
        XrayUtils.target_blocks = (ArrayList<Integer>) dataTag.getSerializable("xrayBlocks", new ArrayList<Integer>());
        espSettings = (ESPSettings) dataTag.getSerializable("espSettings", new ESPSettings());
        FreecamMod.SPEED = dataTag.getFloat("Freecam_speed", 1.0f);
        noWeatherSettings = (NoWeatherSettings) dataTag.getSerializable("noweatherSettings", new NoWeatherSettings());
        tracerSettings = (TracerSettings) dataTag.getSerializable("tracerSettings", new TracerSettings());
        hudSettings = (HUDSettings) dataTag.getSerializable("hudSettings", new HUDSettings());
        elytraFlySettings = (ElytraFlySettings) dataTag.getSerializable("elytraFlySettings", new ElytraFlySettings());
        announcerSettings = (AnnouncerSettings) dataTag.getSerializable("announcerSettings", new AnnouncerSettings());
        waypoints = (Waypoints) dataTag.getSerializable("waypoints", new Waypoints());

        miscOptions = (MiscOptions) dataTag.getSerializable("miscOptions", new MiscOptions());
    }

    public void saveConfig() {
        for (Module module : ModuleManager.AVALIBLE_MODULES) {
            ModuleOptionSave[] options = new ModuleOptionSave[module.options.length];
            for (int i = 0; i < options.length; i++) {
                ModuleOption option = module.options[i];
                ModuleOptionSave save = new ModuleOptionSave(option);
                options[i] = save;
            }
            dataTag.setSerializableArray("settings" + module.nameFull, options);
            dataTag.setBoolean("enabled:" + module.name, module.isEnabled);
        }
        for (Window window : ClickGUI.INSTANCE.windows) {
            dataTag.setIntegerArray(window.text + "¦window", new int[]{window.getX(), window.getY(), window.isOpen ? 1 : 0});
            for (IEntry entry : window.entries) {
                dataTag.setIntegerArray(window.text + "¦window" + entry.getName(), new int[]{entry.isOpen() ? 1 : 0});
            }
        }
        dataTag.setSerializable("friends", Friends.FRIENDS);
        dataTag.setSerializable("sortType", ModuleManager.sortType);
        dataTag.setSerializable("targetSettings", targetSettings);
        dataTag.setSerializable("xrayBlocks", XrayUtils.target_blocks);
        dataTag.setSerializable("espSettings", espSettings);
        dataTag.setFloat("Freecam_speed", FreecamMod.SPEED);
        dataTag.setSerializable("noweatherSettings", noWeatherSettings);
        dataTag.setSerializable("tracerSettings", tracerSettings);
        dataTag.setSerializable("miscOptions", miscOptions);
        dataTag.setSerializable("hudSettings", hudSettings);
        dataTag.setSerializable("elytraFlySettings", elytraFlySettings);
        dataTag.setSerializable("announcerSettings", announcerSettings);
        dataTag.setSerializable("waypoints", waypoints);
        dataTag.save();
    }

    public void initModules() {
        for (Module module : ModuleManager.AVALIBLE_MODULES) {
            ModuleOption[] defaultOptions = module.defaultOptions();
            ModuleOptionSave[] loaded = (ModuleOptionSave[]) dataTag.getSerializableArray("settings" + module.nameFull, null);
            if (loaded == null) {
                module.options = defaultOptions;
            } else {
                module.options = new ModuleOption[defaultOptions.length];
                for (int i = 0; i < defaultOptions.length; i++) {
                    ModuleOption defaultOption = defaultOptions[i];
                    try {
                        ModuleOptionSave savedValue = loaded[i];
                        defaultOption.setValue(savedValue.getValue());
                        module.options[i] = defaultOption;
                    } catch (ArrayIndexOutOfBoundsException e) {
                        module.options[i] = defaultOption;
                    }
                }
            }

            if (Module.shouldBeEnabled(dataTag.getBoolean("enabled:" + module.name), module.getLaunchState())) {
                System.out.println("Default state for module " + module.nameFull + ": enabled");
                ModuleManager.enableModule(module);
            } else {
                System.out.println("Default state for module " + module.nameFull + ": disabled");
                ModuleManager.disableModule(module);
            }
            module.hide = ((boolean) module.getOptionByName("hidden").getValue());
        }
    }
}
