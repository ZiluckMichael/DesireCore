package com.desiremc.core.tablist;

import java.lang.reflect.InvocationTargetException;

import com.comphenix.protocol.events.PacketContainer;

public class TabSlot
{

    TabSlot(TabList list, String prefix, String name, String suffix)
    {
        this.list = list;

        this.prefix = prefix.substring(0, Math.min(prefix.length(), 16)); //Limit to 16 chars to avoid client crash
        this.name = name.substring(0, Math.min(name.length(), 16)); //Limit to 16 chars to avoid client crash
        this.suffix = suffix.substring(0, Math.min(suffix.length(), 16)); //Limit to 16 chars to avoid client crash

        this.sent = false;
        this.ping = list.defaultPing;
    }

    TabSlot(TabList list, String name)
    {
        this.list = list;

        this.name = name.substring(0, Math.min(name.length(), 16)); //Limit to 16 chars to avoid client crash

        this.sent = false;
        this.ping = list.defaultPing;
    }

    TabList list;
    boolean sent, toRemove;

    String prefix, name, suffix;
    private int ping;

    public void setPing(int ping)
    {
        this.ping = ping;
    }

    public String getPrefix()
    {
        return prefix;
    }

    public String getName()
    {
        return name;
    }

    public String getSuffix()
    {
        return suffix;
    }

    public int getPing()
    {
        return ping;
    }

    public void createPrefixAndSuffix(String prefix, String suffix)
    {
        if (TabAPI.getProtocolManager().getProtocolVersion(list.player) >= 47)
        {
            return;
        }
        if (toRemove || !sent)
        { //2 teams with the same name causes client crash
            return;
        }
        if (this.prefix != null || this.suffix != null)
        {
            updatePrefixAndSuffix(prefix, suffix);
            return;
        }

        this.prefix = prefix.substring(0, Math.min(prefix.length(), 16)); //Limit to 16 chars to avoid client crash
        this.suffix = suffix.substring(0, Math.min(suffix.length(), 16)); //Limit to 16 chars to avoid client crash

        PacketContainer packet = TabAPI.buildTeamPacket(name, name, this.prefix, this.suffix, 0, name);
        try
        {
            TabAPI.getProtocolManager().sendServerPacket(list.player, packet);
        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace();
        }
    }

    public void updatePrefixAndSuffix(String prefix, String suffix)
    {
        if (TabAPI.getProtocolManager().getProtocolVersion(list.player) >= 47)
        {
            return;
        }
        if (toRemove || !sent)
        { //Updating prefix and suffix of team which doesn't exists causes client crash
            return;
        }
        if (this.prefix == null && this.suffix == null)
        {
            createPrefixAndSuffix(prefix, suffix);
            return;
        }

        this.prefix = prefix.substring(0, Math.min(prefix.length(), 16)); //Limit to 16 chars to avoid client crash
        this.suffix = suffix.substring(0, Math.min(suffix.length(), 16)); //Limit to 16 chars to avoid client crash

        PacketContainer packet = TabAPI.buildTeamPacket(name, name, this.prefix, this.suffix, 2, name);
        try
        {
            TabAPI.getProtocolManager().sendServerPacket(list.player, packet);
        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace();
        }
    }

    public void removePrefixAndSuffix()
    {
        if (TabAPI.getProtocolManager().getProtocolVersion(list.player) >= 47)
        {
            return;
        }
        if (toRemove || (this.prefix == null && this.suffix == null) || !sent)
        { //Removing team which doesn't exists causes client crash
            return;
        }

        PacketContainer packet = TabAPI.buildTeamPacket(name, name, null, null, 1, name);
        try
        {
            TabAPI.getProtocolManager().sendServerPacket(list.player, packet);
        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace();
        }
    }

}