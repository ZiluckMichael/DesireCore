package com.desiremc.core.session;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;

import com.desiremc.core.DesireCore;
import com.desiremc.core.utils.StringUtils;

public enum Rank
{

    GUEST(1, "Guest", "", ChatColor.WHITE, ChatColor.GRAY),
    BRIGADIER(2, "Brigadier", "§a✯", ChatColor.WHITE, ChatColor.GREEN),
    COMMODORE(3, "Commodore", "§b❂", ChatColor.WHITE, ChatColor.AQUA),
    GRANDMASTER(4, "Grandmaster", "§d✹", ChatColor.WHITE, ChatColor.LIGHT_PURPLE),
    YOUTUBER(5, "YouTuber", "§6§lYT", ChatColor.WHITE, ChatColor.GOLD),
    HELPER(6, "Helper", "§d§lHELPER", ChatColor.WHITE, ChatColor.LIGHT_PURPLE, "helper", "help"),
    MODERATOR(7, "Moderator", "§2§lMOD", ChatColor.WHITE, ChatColor.BLUE, "mod"),
    SRMOD(8, "Senior_Mod", "§6§lSR.MOD", ChatColor.WHITE, ChatColor.LIGHT_PURPLE, "sr", "srmod", "sr_mod"),
    ADMIN(9, "Admin", "§c§lADMIN", ChatColor.GREEN, ChatColor.RED),
    MANAGER(10, "Manager", "§4§lMANAGER", ChatColor.GREEN, ChatColor.RED),
    DEVELOPER(11, "Developer", "§9§lDEV", ChatColor.GREEN, ChatColor.RED, "dev"),
    OWNER(12, "Owner", "§3§lOWNER", ChatColor.GREEN, ChatColor.RED);

    private final int id;
    private final String displayName;
    private final String prefix;
    private final ChatColor color;
    private final ChatColor main;
    private final String[] aliases;

    Rank(int id, String displayName, String prefix, ChatColor color, ChatColor main, String... aliases)
    {
        this.id = id;
        this.displayName = displayName;
        this.prefix = prefix;
        this.color = color;
        this.main = main;
        this.aliases = aliases;
    }

    public int getId()
    {
        return id;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public ChatColor getColor()
    {
        return color;
    }

    public ChatColor getMain()
    {
        return main;
    }

    public String getPrefix()
    {
        return prefix + " ";
    }

    public boolean isStaff()
    {
        return getId() >= HELPER.getId();
    }

    public boolean isDonor()
    {
        return getId() < YOUTUBER.getId() && getId() > GUEST.getId();
    }

    public boolean isManager()
    {
        return getId() >= ADMIN.getId();
    }

    public long getDeathBanTime()
    {
        if (isStaff())
        {
            return 0;
        }
        Long lookup = DesireCore.getConfigHandler().getLong("deathban.times." + getDisplayName().toLowerCase());
        if (lookup == null || lookup == 0)
        {
            lookup = DesireCore.getConfigHandler().getLong("deathban.times.default");
        }
        return lookup * 60_000;
    }

    public static Rank getRank(String value)
    {
        for (Rank v : values())
        {
            if (v.name().equalsIgnoreCase(value) || StringUtils.contains(v.aliases, value))
            {
                return v;
            }
        }
        return null;
    }

    public static List<String> getRanks()
    {
        List<String> names = new ArrayList<>();

        for (Rank rank : values())
        {
            names.add(StringUtils.capitalize(rank.name().toLowerCase().replace("_", " ")));
        }

        return names;
    }

}
