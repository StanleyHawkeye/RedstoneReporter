package me.hawkeye.minecraft.bukkitplugins.redstonereporter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * RedstoneReporter plugin for bukkit
 * @version 0.1
 *
 */
public class RedstoneReporter extends JavaPlugin implements Listener
{	
	public void onEnable() 
	{
		getServer().getPluginManager().registerEvents(this,this);
		log.info("RedstoneReporter enabled.");
	}
	
	public void onDisable() 
	{
		log.info("RedstoneReporter disabled.");
	}

	public final Logger log = Logger.getLogger("Minecraft");
	
	public final ArrayList<String> receiversList = new ArrayList<String>();
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		if(cmd.getName().equalsIgnoreCase("rsreport"))
		{
			if(sender instanceof Player)
			{
				if(this.receiversList.contains(sender.getName()))
				{
					this.receiversList.remove(sender.getName());
					sender.sendMessage(ChatColor.YELLOW+"You won't have redstone events reported anymore.");
				}
				else
				{
					this.receiversList.add(sender.getName());
					sender.sendMessage(ChatColor.YELLOW+"You will now get all redstone events reported.");
				}
				return true;
			}
			else
			{
				sender.sendMessage("This command can only be used by players.");
			}
		}
		return false;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onRedstoneEvent(BlockRedstoneEvent event)
	{
		Iterator<String> receiversIterator = receiversList.iterator();
		while(receiversIterator.hasNext())
		{
			Player player = getServer().getPlayerExact(receiversIterator.next());
			if(player == null)
			{
				receiversIterator.remove();
				continue;
			}

			player.sendMessage("RSE: "+event.getBlock().getTypeId()+", "+event.getBlock().getLocation().getWorld().getName()+"["+event.getBlock().getLocation().getBlockX()+", "+event.getBlock().getLocation().getBlockY()+", "+event.getBlock().getLocation().getBlockZ()+"] "+event.getOldCurrent()+"->"+event.getNewCurrent()+" "+System.currentTimeMillis());
		}
	}
		
}
