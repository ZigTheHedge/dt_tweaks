package com.cwelth.dt_tweaks;

import com.cwelth.dt_tweaks.event_handlers.GCWorldGenExtender;
import com.cwelth.dt_tweaks.event_handlers.commandHandler;
import com.cwelth.dt_tweaks.event_handlers.playerSpawn;
import com.cwelth.dt_tweaks.event_handlers.torchesPrevent;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;


/**
 * Created by zth on 05/10/15.
 */
@Mod(modid = modMain.MODID, name = modMain.NAME, version = modMain.VERSION, dependencies = "required-after:GalacticraftCore;")
public class modMain {

    public static final String NAME = "Cwelth \"Double Trouble\" tweaks";
    public static final String MODID = "dt_tweaks";
    public static final String VERSION = "1.02";

    public int dimID;
    public int X;
    public int Y;
    public int Z;
    public boolean isEnabled;
    public String itemsNotToBeDropped;
    public String itemsToGiveAtFirstLogon;
    public boolean isSKEnabled;
    public Configuration config;
    public int genChance;
    public boolean isWGEnabled;
    public String tplPath;
    public int moonDimID;

    public void saveConfig()
    {
        config.load();
        config.get("spawn_settings", "isSpawnEnabled", false).set(isEnabled);
        config.get("spawn_settings", "dimID", -28).set(dimID);
        config.get("spawn_settings", "X", 0).set(X);
        config.get("spawn_settings", "Y", 60).set(Y);
        config.get("spawn_settings", "Z", 0).set(Z);
        config.get("inventory_settings", "itemsNotToBeDropped", "").set(itemsNotToBeDropped);
        config.get("inventory_settings", "itemsToGiveAtFirstLogon", "").set(itemsToGiveAtFirstLogon);
        config.get("inventory_settings", "isSKEnabled", false).set(isSKEnabled);
        config.get("GC_worldgen", "isWGEnabled", true).set(isWGEnabled);
        config.get("GC_worldgen", "genChance", 1000).set(genChance);
        config.get("GC_worldgen", "tplPath", "./mods/resources/ruins/moon").set(tplPath);
        config.get("preventers", "moonDimID", -28).set(moonDimID);

        config.save();
    }

    public void loadConfig()
    {
        config.load();
        isEnabled = config.get("spawn_settings", "isSpawnEnabled", false).getBoolean();
        dimID = config.get("spawn_settings", "dimID", -28).getInt();
        X = config.get("spawn_settings", "X", 0).getInt();
        Y = config.get("spawn_settings", "Y", 60).getInt();
        Z = config.get("spawn_settings", "Z", 0).getInt();
        itemsNotToBeDropped = config.get("inventory_settings", "itemsNotToBeDropped", "").getString();
        itemsToGiveAtFirstLogon = config.get("inventory_settings", "itemsToGiveAtFirstLogon", "").getString();
        isSKEnabled = config.get("inventory_settings", "isSKEnabled", false).getBoolean();
        isWGEnabled = config.get("GC_worldgen", "isWGEnabled", true).getBoolean();
        genChance = config.get("GC_worldgen", "genChance", 1000).getInt();
        tplPath = config.get("GC_worldgen", "tplPath", "./mods/resources/ruins/moon").getString();
        moonDimID = config.get("preventers", "moonDimID", -28).getInt();

        config.save();
    }

    @Mod.Instance("dt_tweaks")
    public static modMain instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e)
    {
        config = new Configuration(e.getSuggestedConfigurationFile());

        loadConfig();

        Items.init();
        Blocks.init();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e)
    {
        MinecraftForge.EVENT_BUS.register(new playerSpawn());
        MinecraftForge.EVENT_BUS.register(new GCWorldGenExtender());
        MinecraftForge.EVENT_BUS.register(new torchesPrevent());

        MinecraftForge.addGrassSeed(new ItemStack((Item)Item.itemRegistry.getObject("harvestcraft:cottonseedItem")), 10);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e)
    {
    }

    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new commandHandler());
    }

}
