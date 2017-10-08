package com.desiremc.core.validators;

import org.bukkit.command.CommandSender;

import com.desiremc.core.api.command.CommandValidator;
import com.desiremc.core.session.SessionHandler;

public class SenderHasFriendsValidator extends CommandValidator
{

    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg)
    {
        if (SessionHandler.getSession(sender).getFriends().size() > 0)
        {
            return true;
        }
        else 
        {
            LANG.sendString(sender, "friend.no_friends");
            return false;
        }
    }

}
