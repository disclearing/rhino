package com.sergivb01.base.command;

import com.sergivb01.base.BasePlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandMap;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class ReflectionCommandManager
		implements CommandManager{
	private static final String PERMISSION_MESSAGE = ChatColor.RED + "You do not have permission for this command.";
	private final Map<String, BaseCommand> commandMap = new HashMap<String, BaseCommand>();

	public ReflectionCommandManager(final BasePlugin plugin){
		final ConsoleCommandSender console = Bukkit.getConsoleSender();
		final Server server = Bukkit.getServer();
		server.getScheduler().runTaskLater(plugin, new Runnable(){

			@Override
			public void run(){
				Optional optionalCommandMap = ReflectionCommandManager.this.getCommandMap(server);
				if(!optionalCommandMap.isPresent()){
					Bukkit.broadcastMessage("" + '[' + plugin.getDescription().getFullName() + "] Command map not found");
					console.sendMessage("" + '[' + plugin.getDescription().getFullName() + "] Command map not found");
					return;
				}
				CommandMap bukkitCommandMap = (CommandMap) optionalCommandMap.get();
				for(BaseCommand command : ReflectionCommandManager.this.commandMap.values()){
					String commandName = command.getName();
					Optional optional = ReflectionCommandManager.this.getPluginCommand(commandName, plugin);
					if(optional.isPresent()){
						PluginCommand pluginCommand = (PluginCommand) optional.get();
						pluginCommand.setAliases(Arrays.asList(command.getAliases()));
						pluginCommand.setDescription(command.getDescription());
						pluginCommand.setExecutor(command);
						pluginCommand.setTabCompleter(command);
						pluginCommand.setUsage(command.getUsage());
						pluginCommand.setPermission(command.getPermission());
						pluginCommand.setPermissionMessage(PERMISSION_MESSAGE);
						bukkitCommandMap.register(plugin.getDescription().getName(), pluginCommand);
						continue;
					}
					Bukkit.broadcastMessage("" + '[' + plugin.getName() + "] " + ChatColor.YELLOW + "Failed to register command '" + commandName + "'.");
					console.sendMessage("" + '[' + plugin.getName() + "] " + ChatColor.YELLOW + "Failed to register command '" + commandName + "'.");
				}
			}
		}, 1);
	}

	@Override
	public boolean containsCommand(BaseCommand command){
		return this.commandMap.containsValue(command);
	}

	@Override
	public void registerAll(BaseCommandModule module){
		if(module.isEnabled()){
			Set<BaseCommand> commands = module.getCommands();
			for(BaseCommand command : commands){
				this.commandMap.put(command.getName(), command);
			}
		}
	}

	@Override
	public void registerCommand(BaseCommand command){
		this.commandMap.put(command.getName(), command);
	}

	@Override
	public void registerCommands(BaseCommand[] commands){
		for(BaseCommand command : commands){
			this.commandMap.put(command.getName(), command);
		}
	}

	@Override
	public void unregisterCommand(BaseCommand command){
		this.commandMap.values().remove(command);
	}

	@Override
	public BaseCommand getCommand(final String id){
		return this.commandMap.get(id);
	}

	private Optional<PluginCommand> getPluginCommand(final String name, final Plugin plugin){
		try{
			final Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
			constructor.setAccessible(true);
			return Optional.of(constructor.newInstance(name, plugin));
		}catch(IllegalArgumentException | IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException | SecurityException ex){
			ex.printStackTrace();
			return Optional.empty();
		}
	}


	private Optional<CommandMap> getCommandMap(Server server){
		PluginManager pluginManager = server.getPluginManager();
		if(pluginManager instanceof SimplePluginManager){
			try{
				Field field = SimplePluginManager.class.getDeclaredField("commandMap");
				field.setAccessible(true);
				return Optional.of((CommandMap) field.get(pluginManager));
			}catch(IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException ex){
				ex.printStackTrace();
				return Optional.empty();
			}
		}
		return Optional.empty();
	}

}

