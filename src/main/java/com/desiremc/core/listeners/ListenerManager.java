package com.desiremc.core.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import com.desiremc.core.DesireCore;

public class ListenerManager
{

    private static ListenerManager instance;

    private List<Listener> listeners;

    public ListenerManager()
    {
        listeners = new ArrayList<>();
    }

    public List<Listener> getListenerList()
    {
        return listeners;
    }

    public void addListener(Listener l)
    {
        listeners.add(l);
        Bukkit.getPluginManager().registerEvents(l, DesireCore.getInstance());
    }

    public static void initialize()
    {
        instance = new ListenerManager();
    }

    public static ListenerManager getInstace()
    {
        return instance;
    }

}
