package com.cwelth.dt_tweaks.event_handlers;

import com.cwelth.dt_tweaks.modMain;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by zth on 06/10/15.
 */
public class playerSpawn {

    public HashMap<String, Boolean> isPlayerFirstJoined = new HashMap<String, Boolean>();
    public HashMap<String, ArrayList<ItemStack>> plKeepInv = new HashMap<String, ArrayList<ItemStack>>();

    @SubscribeEvent(priority=EventPriority.LOWEST)
    public void onEntityJoinWorldEvent(EntityJoinWorldEvent event)
    {
        if(!event.world.isRemote)
            if(event.entity instanceof EntityPlayer) {

                //Restore inventory part
                if(plKeepInv.containsKey(((EntityPlayer)event.entity).getUniqueID().toString()))
                {
                    for(ItemStack item : plKeepInv.get(((EntityPlayer)event.entity).getUniqueID().toString()))
                        ((EntityPlayer)event.entity).inventory.addItemStackToInventory(item);

                    plKeepInv.remove(((EntityPlayer)event.entity).getUniqueID().toString());
                }

                //Starter kit part
                if(isPlayerFirstJoined.get(((EntityPlayer)event.entity).getUniqueID().toString()).equals(Boolean.TRUE))
                {
                    if(!modMain.instance.isEnabled)isPlayerFirstJoined.put(((EntityPlayer) event.entity).getUniqueID().toString(), Boolean.FALSE);
                    String[] starterKit = modMain.instance.itemsToGiveAtFirstLogon.split(",");
                    for(String starterItem : starterKit)
                    {
                        String[] starterParams = starterItem.trim().split("\\*");
                        int qty = 1;
                        int meta = 0;
                        String finalItem = starterParams[0];
                        if(starterParams[0].matches("-?\\d+(\\.\\d+)?")){
                            qty = Integer.valueOf(starterParams[0]);
                            finalItem = starterParams[1];
                        }
                        String[] metaParams = finalItem.split("\\|");
                        if(metaParams.length > 1)
                        {
                            meta = Integer.valueOf(metaParams[1]);
                            finalItem = metaParams[0];
                        }

                        FMLLog.info("First join. Giving the following item: %dx%s:%d",qty,finalItem,meta);
                        ItemStack test = new ItemStack((Item)Item.itemRegistry.getObject(finalItem), qty, meta);
                        if(!((EntityPlayer)event.entity).inventory.addItemStackToInventory(test)) {
                            FMLLog.warning("addItemStackToInventory failed!! (Player %s)", ((EntityPlayer) event.entity).getDisplayName());
                            FMLLog.info("ItemStack != null :: %b",(test != null));
                            FMLLog.info("ItemStack.stackSize :: %d",test.stackSize);
                            FMLLog.info("ItemStack.getItem() != null :: %b",(test.getItem() != null));

                        }
                    }
                }

                //Teleport part
                if(!modMain.instance.isEnabled) return;
                if(isPlayerFirstJoined.get(((EntityPlayer)event.entity).getUniqueID().toString()).equals(Boolean.TRUE))
                {
                    isPlayerFirstJoined.put(((EntityPlayer) event.entity).getUniqueID().toString(), Boolean.FALSE);
                    EntityPlayerMP en = ((EntityPlayerMP) event.entity);
                    MinecraftServer mc = MinecraftServer.getServer();
                    //FMLLog.info("DT_TWEAKS: Trying to teleport player to the: %s", mc.worldServerForDimension(modMain.instance.dimID).getWorldInfo().getWorldName());
                    mc.getConfigurationManager().transferPlayerToDimension(en, modMain.instance.dimID, new spawnTeleporter(mc.worldServerForDimension(modMain.instance.dimID)));
                }

            }

    }

    @SubscribeEvent
    public void onPlayerLoadDataFileEvent(PlayerEvent.LoadFromFile event) {
        File testFile = new File(event.playerDirectory, event.playerUUID+".dat");
        if(!testFile.exists()) {
            if(!modMain.instance.isEnabled)FMLLog.warning("DT_TWEAKS: SetSpawn DISABLED!!!");
            isPlayerFirstJoined.put(event.playerUUID, Boolean.TRUE);
        }
        else {
            isPlayerFirstJoined.put(event.playerUUID, Boolean.FALSE);
        }
    }

    @SubscribeEvent(priority=EventPriority.NORMAL)
    public void onEntityDropsEvent(PlayerDropsEvent event)
    {
        if(event.entity instanceof EntityPlayerMP) {
            Iterator<EntityItem> sItem = event.drops.iterator();
            ArrayList<EntityItem> resultDrops = new ArrayList<EntityItem>();
            ArrayList<ItemStack> keptDrops = new ArrayList<ItemStack>();

            while(sItem.hasNext())
            {
                EntityItem iNext = sItem.next();
                String itemName = Item.itemRegistry.getNameForObject(iNext.getEntityItem().getItem());
                String itemNameNoMeta = Item.itemRegistry.getNameForObject(iNext.getEntityItem().getItem());
                if(iNext.getEntityItem().getItemDamage() > 0)itemName += "|" + iNext.getEntityItem().getItemDamage();
                if(modMain.instance.itemsNotToBeDropped.contains( itemName ) || modMain.instance.itemsNotToBeDropped.contains( itemNameNoMeta+"|*" ))
                {
                    keptDrops.add(iNext.getEntityItem());
                } else
                {
                    resultDrops.add(iNext);

                }
            }
            plKeepInv.put(event.entityPlayer.getUniqueID().toString(), keptDrops);
            event.drops.clear();
            event.drops.addAll(resultDrops);
        }
    }


}
