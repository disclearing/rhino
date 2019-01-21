package com.sergivb01.base.command;

import com.sergivb01.base.BasePlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class SimpleCommandManager implements CommandManager{
	private static final String PERMISSION_MESSAGE;

	static{
		PERMISSION_MESSAGE = ChatColor.RED + "You do not have permission to execute this command.";
	}

	private final Map<String, BaseCommand> commandMap;

	public SimpleCommandManager(final BasePlugin plugin){
		this.commandMap = new HashMap<String, BaseCommand>();
		final ConsoleCommandSender console = plugin.getServer().getConsoleSender();
		new BukkitRunnable(){
			public void run(){
				final Collection<BaseCommand> commands = SimpleCommandManager.this.commandMap.values();
				for(final BaseCommand command : commands){
					final String commandName = command.getName();
					final PluginCommand pluginCommand = plugin.getCommand(commandName);
					if(pluginCommand == null){
						Bukkit.broadcastMessage(commandName);
						console.sendMessage('[' + plugin.getName() + "] " + ChatColor.YELLOW + "Failed to register command '" + commandName + "'.");
						console.sendMessage('[' + plugin.getName() + "] " + ChatColor.YELLOW + "Reason: Undefined in plugin.yml.");
					}else{
						pluginCommand.setAliases(Arrays.asList(command.getAliases()));
						pluginCommand.setDescription(command.getDescription());
						pluginCommand.setExecutor(command);
						pluginCommand.setTabCompleter(command);
						pluginCommand.setUsage(command.getUsage());
						pluginCommand.setPermission("base.command." + command.getName());
						pluginCommand.setPermissionMessage(SimpleCommandManager.PERMISSION_MESSAGE);
					}
				}
			}
		}.runTask(plugin);
	}

	@Override
	public boolean containsCommand(final BaseCommand command){
		return this.commandMap.containsValue(command);
	}

	@Override
	public void registerAll(final BaseCommandModule module){
		if(module.isEnabled()){
			final Set<BaseCommand> commands = module.getCommands();
			for(final BaseCommand command : commands){
				this.commandMap.put(command.getName(), command);
			}
		}
	}

	@Override
	public void registerCommand(final BaseCommand command){
		this.commandMap.put(command.getName(), command);
	}

	@Override
	public void registerCommands(final BaseCommand[] commands){
		for(final BaseCommand command : commands){
			this.commandMap.put(command.getName(), command);
		}
	}

	@Override
	public void unregisterCommand(final BaseCommand command){
		this.commandMap.values().remove(command);
	}

	@Override
	public BaseCommand getCommand(final String id){
		return this.commandMap.get(id);
	}
}