package com.desiremc.core.validators;

import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.command.CommandValidator;

public class ItemBlockValidator extends CommandValidator
{

    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg)
    {
        MaterialData data = null;
        if (arg instanceof ItemStack)
        {
            ItemStack is = (ItemStack) arg;
            data = is.getData();
        }
        else if (arg instanceof MaterialData)
        {
            data = (MaterialData) arg;
        }
        
        if (!data.getItemType().isBlock()) 
        {
            DesireCore.getLangHandler().sendString(sender, "not_block");
            return false;
        }
        return true;
    }

}
