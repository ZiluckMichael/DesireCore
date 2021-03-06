package com.desiremc.core.listeners;

import com.desiremc.core.commands.staff.StaffReportsCommand;
import com.desiremc.core.staff.Gadget;
import com.desiremc.core.staff.GadgetHandler;
import com.desiremc.core.staff.StaffHandler;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class StaffListener implements Listener
{

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInteract(PlayerInteractEvent event)
    {
        Player p = event.getPlayer();

        if (event.getItem() == null || event.getItem().getType() == Material.AIR)
        {
            return;
        }

        if (!event.getAction().equals(Action.RIGHT_CLICK_AIR) && !event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
        {
            return;
        }

        if (!StaffHandler.getInstance().inStaffMode(p))
        {
            return;
        }

        event.setCancelled(true);
        handleInteraction(event.getItem(), event);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInteract(PlayerInteractEntityEvent event)
    {
        Player p = event.getPlayer();

        if (!StaffHandler.getInstance().inStaffMode(p))
        {
            return;
        }

        if (handleEntityInteraction(p.getItemInHand(), event))
        {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event)
    {
        StaffHandler.getInstance().saveInventory(event.getEntity());
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event)
    {
        Player p = event.getPlayer();

        if (StaffHandler.getInstance().inStaffMode(p))
        {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event)
    {
        Player p = event.getPlayer();

        if (StaffHandler.getInstance().inStaffMode(p))
        {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event)
    {
        Player p = event.getPlayer();

        if (StaffHandler.getInstance().inStaffMode(p))
        {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onSpawn(InventoryClickEvent event)
    {
        if (!(event.getWhoClicked() instanceof Player))
        {
            return;
        }

        Player p = (Player) event.getWhoClicked();

        if (StaffHandler.getInstance().inStaffMode(p))
        {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event)
    {
        if (!(event.getDamager() instanceof Player))
        {
            return;
        }

        Player player = (Player) event.getDamager();

        if (StaffHandler.getInstance().inStaffMode(player))
        {
            event.setCancelled(true);
        }
    }

    private boolean handleInteraction(ItemStack item, PlayerInteractEvent event)
    {
        for (Gadget gadget : GadgetHandler.getInstance().gadgets.values())
        {
            if (gadget.getType().equals(item.getType()))
            {
                switch (gadget.getName())
                {
                    case "launcher":
                        StaffHandler.getInstance().useLaunch(event);
                        event.setCancelled(true);
                        break;
                    case "random-teleport":
                        StaffHandler.getInstance().useTeleport(event);
                        event.setCancelled(true);
                        break;
                    case "vanish":
                        StaffHandler.getInstance().useInvisibility(event);
                        event.setCancelled(true);
                        break;
                    case "reports":
                        StaffHandler.getInstance().openReportsGUI(event.getPlayer(), StaffReportsCommand.getPage(event.getPlayer()));
                        event.setCancelled(true);
                        break;
                    case "teleporter":
                        StaffHandler.getInstance().useTeleport(event);
                        event.setCancelled(true);
                        break;
                    default:
                        return false;
                }
            }
        }
        return true;
    }

    private boolean handleEntityInteraction(ItemStack item, PlayerInteractEntityEvent event)
    {
        for (Gadget gadget : GadgetHandler.getInstance().gadgets.values())
        {
            if (gadget.getType().equals(item.getType()))
            {
                switch (gadget.getName())
                {
                    case "freezer":
                        StaffHandler.getInstance().useFreeze(event);
                        break;
                    case "clicker":
                        StaffHandler.getInstance().useClicksPerSecond(event);
                        break;
                    case "examine":
                        StaffHandler.getInstance().useOpenInventory(event);
                        break;
                    case "follow":
                        StaffHandler.getInstance().useMount(event);
                        break;
                    default:
                        return false;
                }
            }
        }
        return true;
    }
}
