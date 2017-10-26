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

import net.daporkchop.pepsimod.accountswitcher.ias.IAS;
import net.daporkchop.pepsimod.clickgui.ClickGUI;
import net.daporkchop.pepsimod.command.CommandRegistry;
import net.daporkchop.pepsimod.command.impl.*;
import net.daporkchop.pepsimod.command.impl.waypoint.*;
import net.daporkchop.pepsimod.event.GuiRenderHandler;
import net.daporkchop.pepsimod.event.MiscEventHandler;
import net.daporkchop.pepsimod.gui.clickgui.*;
import net.daporkchop.pepsimod.key.KeyRegistry;
import net.daporkchop.pepsimod.module.ModuleManager;
import net.daporkchop.pepsimod.module.impl.combat.*;
import net.daporkchop.pepsimod.module.impl.misc.*;
import net.daporkchop.pepsimod.module.impl.movement.*;
import net.daporkchop.pepsimod.module.impl.player.*;
import net.daporkchop.pepsimod.module.impl.render.*;
import net.daporkchop.pepsimod.util.ImageUtils;
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.daporkchop.pepsimod.util.ReflectionStuff;
import net.daporkchop.pepsimod.util.config.Config;
import net.daporkchop.pepsimod.util.config.impl.HUDTranslator;
import net.daporkchop.pepsimod.util.misc.Default;
import net.daporkchop.pepsimod.wdl.WDL;
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
import org.apache.commons.io.IOUtils;
import org.lwjgl.input.Keyboard;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.TimerTask;

@Mod(modid = "pepsimoddev", name = "pepsimod - Dev", version = "11.1-dev")
public class PepsiMod {
    public static final String VERSION = "11.1";
    public static final String chatPrefix = PepsiUtils.COLOR_ESCAPE + "0" + PepsiUtils.COLOR_ESCAPE + "l[" + PepsiUtils.COLOR_ESCAPE + "c" + PepsiUtils.COLOR_ESCAPE + "lpepsi" + PepsiUtils.COLOR_ESCAPE + "9" + PepsiUtils.COLOR_ESCAPE + "lmod" + PepsiUtils.COLOR_ESCAPE + "0" + PepsiUtils.COLOR_ESCAPE + "l]" + PepsiUtils.COLOR_ESCAPE + "r ";
    public boolean isMcLeaksAccount = false;
    public Session originalSession = null;
    public boolean hasInitializedModules = false;
    public boolean isInitialized = false;
    private Minecraft mc;

    public static void registerModules(FMLStateEvent event) {
        ModuleManager.registerModule(new NoFallMod());
        ModuleManager.registerModule(new AntiHungerMod());
        ModuleManager.registerModule(new FullbrightMod());
        ModuleManager.registerModule(new CriticalsMod());
        ModuleManager.registerModule(new AuraMod());
        ModuleManager.registerModule(new VelocityMod());
        ModuleManager.registerModule(new TimerMod());
        ModuleManager.registerModule(new XrayMod());
        ModuleManager.registerModule(new AntiBlindMod());
        ModuleManager.registerModule(new StorageESPMod());
        ModuleManager.registerModule(new FreecamMod());
        ModuleManager.registerModule(new HealthTagsMod());
        ModuleManager.registerModule(new NameTagsMod());
        ModuleManager.registerModule(new NoHurtCamMod());
        ModuleManager.registerModule(new NoOverlayMod());
        ModuleManager.registerModule(new NoWeatherMod());
        ModuleManager.registerModule(new AntiInvisibleMod());
        ModuleManager.registerModule(new TrajectoriesMod());
        ModuleManager.registerModule(new TracersMod());
        ModuleManager.registerModule(new ClickGuiMod(false, Keyboard.KEY_RSHIFT));
        ModuleManager.registerModule(new HUDMod(true, -1));
        ModuleManager.registerModule(new ZoomMod(-1));
        ModuleManager.registerModule(new ElytraFlyMod());
        ModuleManager.registerModule(new AutoWalkMod());
        ModuleManager.registerModule(new EntitySpeedMod());
        ModuleManager.registerModule(new SafewalkMod());
        ModuleManager.registerModule(new InventoryMoveMod());
        ModuleManager.registerModule(new HorseJumpPowerMod());
        ModuleManager.registerModule(new AutoTotemMod());
        ModuleManager.registerModule(new CrystalAuraMod());
        ModuleManager.registerModule(new AntiTotemAnimationMod());
        ModuleManager.registerModule(new AnnouncerMod());
        ModuleManager.registerModule(new AutoRespawnMod());
        ModuleManager.registerModule(new JesusMod()); //test
        ModuleManager.registerModule(new SprintMod());
        ModuleManager.registerModule(new NoSlowdownMod());
        ModuleManager.registerModule(new FlightMod());
        ModuleManager.registerModule(new FastPlaceMod());
        ModuleManager.registerModule(new SpeedmineMod());
        ModuleManager.registerModule(new AutoEatMod());
        ModuleManager.registerModule(new StepMod());
        ModuleManager.registerModule(new AutoMineMod());
        ModuleManager.registerModule(new ScaffoldMod());
        ModuleManager.registerModule(new UnfocusedCPUMod());
        ModuleManager.registerModule(new ESPMod());
        ModuleManager.registerModule(new WaypointsMod());
        ModuleManager.registerModule(new NoClipMod());
        ModuleManager.registerModule(new BoatFlyMod());
        ModuleManager.registerModule(new NotificationsMod());
        ModuleManager.registerModule(new BedBomberMod());
        ModuleManager.registerModule(new AutoFishMod());
        ModuleManager.registerModule(new AutoToolMod());
        ModuleManager.registerModule(new AutoArmorMod());
        //ModuleManager.registerModule(new FastBowMod());
        //ModuleManager.registerModule(new BowAimBotMod());
        ModuleManager.registerModule(new FastLadderMod());
        ModuleManager.registerModule(new ParalyzeMod());
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
        CommandRegistry.registerCommand(new DamageCommand());
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
        MinecraftForge.EVENT_BUS.register(new KeyRegistry());
        mc = Minecraft.getMinecraft();
        Default.mc = mc;
        Default.pepsiMod = this;
        IAS.preInit(event);
        new WDL();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        new ClickGUI();
        loadConfig();
        registerModules(event);

        ClickGUI.INSTANCE.setWindows(
                new WindowRender(),
                new WindowCombat(),
                new WindowMisc(),
                new WindowMovement(),
                new WindowPlayer()
        );

        HUDTranslator.INSTANCE.parseConfigLate();

        registerCommands(event);
        IAS.init(event);
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
                Default.pepsiMod.saveConfig();
            }
        }, 360000, 360000);
        IAS.postInit(event);
    }

    public void loadConfig() {
        String json = null; //TODO
        if (json == null) {
            File file = new File(getWorkingFolder().getPath() + File.separatorChar + "pepsimodConf.json");
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }
                json = "{}";
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Config.loadConfig(json);
    }

    public void saveConfig() {
        //xd_finish_this; //TODO
        try {
            File file = new File(getWorkingFolder().getPath() + File.separatorChar + "pepsimodConf.json");
            if (!file.exists()) {
                file.createNewFile();
            }
            IOUtils.write(Config.saveConfig().getBytes(), new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
