package com.cwelth.dt_tweaks.event_handlers;

import com.cwelth.dt_tweaks.modMain;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.item.Item;
import net.minecraftforge.event.world.BlockEvent;

/**
 * Created by ZtH on 09.10.2015.
 */
public class torchesPrevent {

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onPlaveBlockEvent(BlockEvent.PlaceEvent event)
    {
        if(event.player.getEntityWorld().provider.dimensionId == modMain.instance.moonDimID)
        {
            String iName = Item.itemRegistry.getNameForObject(event.itemInHand.getItem());
            if((iName.contains("torch") || iName.contains("Torch")) && !iName.equals("GalacticraftCore:tile.glowstoneTorch") && !iName.equals("minecraft:torch"))event.setCanceled(true);
        }
    }
}
