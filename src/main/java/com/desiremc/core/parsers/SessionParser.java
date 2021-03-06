package com.desiremc.core.parsers;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.Parser;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class SessionParser implements Parser<Session>
{

    @Override
    public Session parseArgument(Session sender, String[] label, String rawArgument)
    {
        Session argument;
        Player player = Bukkit.getPlayerExact(rawArgument);
        if (player == null)
        {
            argument = SessionHandler.findOfflinePlayerByName(rawArgument);
        }
        else
        {
            argument = SessionHandler.getOnlineSession(player.getUniqueId());
        }
        if (argument == null)
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "player-not-found", true, false);
            return null;
        }

        return argument;
    }

    @Override
    public List<String> getRecommendations(Session sender, String str)
    {
        return null;
    }

}
