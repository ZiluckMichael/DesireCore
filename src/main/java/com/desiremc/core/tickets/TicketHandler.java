package com.desiremc.core.tickets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mongodb.morphia.dao.BasicDAO;

import com.desiremc.core.DesireCore;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.utils.PlayerUtils;

public class TicketHandler extends BasicDAO<Ticket, Integer> implements Runnable
{

    private static TicketHandler instance;

    public static HashMap<Integer, Ticket> tickets;

    private int openTickets;

    public TicketHandler()
    {
        super(Ticket.class, DesireCore.getInstance().getMongoWrapper().getDatastore());

        instance = this;

        tickets = new HashMap<>();
        
        List<Ticket> query = find(createQuery().where("status='OPEN'")).asList();
        for (Ticket t : query)
        {
            tickets.put(t.getId(), t);
        }
    }

    public static void openTicket(CommandSender sender, String text)
    {
        Ticket ticket = new Ticket(PlayerUtils.getUUIDFromSender(sender), text);
        ticket.setId(instance.getNextId());
        instance.save(ticket);
        tickets.put(ticket.getId(), ticket);
    }

    public static void closeTicket(CommandSender closer, Ticket ticket, String response)
    {
        ticket.setClosed(System.currentTimeMillis());
        ticket.setCloser(closer instanceof Player ? ((Player) closer).getUniqueId() : DesireCore.getConsoleUUID());
        ticket.setResponse(response);
        ticket.setStatus(Ticket.Status.CLOSED);
    }

    public static void deleteTicket(CommandSender closer, Ticket ticket, String response)
    {
        ticket.setClosed(System.currentTimeMillis());
        ticket.setCloser(closer instanceof Player ? ((Player) closer).getUniqueId() : DesireCore.getConsoleUUID());
        ticket.setResponse(response);
        ticket.setStatus(Ticket.Status.DELETED);
    }

    @Override
    public void run()
    {
        Bukkit.getScheduler().runTaskLater(DesireCore.getInstance(), this, 3600);
        for (Session s : SessionHandler.getInstance().getSessions())
        {
            if (s.getRank().getId() >= Rank.MODERATOR.getId())
            {
                DesireCore.getLangHandler().sendRenderMessage(s, "tickets.open", "{number}", String.valueOf(openTickets));
            }
        }
    }

    private int getNextId()
    {
        Ticket t = createQuery().order("-id").get();
        if (t != null)
        {
            return t.getId() + 1;
        }
        else
        {
            return 0;
        }
    }

    public static void initialize()
    {
        instance = new TicketHandler();
    }

    public static TicketHandler getInstance()
    {
        if (instance == null)
        {
            initialize();
        }
        return instance;
    }

    public static Ticket getTicket(int id)
    {
        return tickets.get(id);
    }

    public static List<Ticket> getAllTickets()
    {
        return new ArrayList<>(tickets.values());
    }

}
