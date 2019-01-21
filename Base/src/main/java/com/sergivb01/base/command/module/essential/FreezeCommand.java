package com.sergivb01.base.command.module.essential;

import com.sergivb01.base.BaseConstants;
import com.sergivb01.base.BasePlugin;
import com.sergivb01.base.command.BaseCommand;
import com.sergivb01.base.event.PlayerFreezeEvent;
import com.sergivb01.util.BukkitUtils;
import com.sergivb01.util.ParticleEffect;
import com.sergivb01.util.chat.ClickAction;
import com.sergivb01.util.chat.Text;
import net.minecraft.util.gnu.trove.map.TObjectLongMap;
import net.minecraft.util.gnu.trove.map.hash.TObjectLongHashMap;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class FreezeCommand extends BaseCommand implements Listener{
	private static final String FREEZE_BYPASS = "base.freeze.bypass";
	public static boolean freezeALL;
	private final TObjectLongMap<UUID> frozenPlayers;
	public Inventory inv;
	private long defaultFreezeDuration;
	private long serverFrozenMillis;
	private HashSet<String> frozen;

	public FreezeCommand(final BasePlugin plugin){
		super("freeze", "Freezes a player from moving");
		this.frozen = new HashSet<String>();
		this.frozenPlayers = (TObjectLongMap<UUID>) new TObjectLongHashMap();
		this.setUsage("/(command) <all|player>");
		this.setAliases(new String[]{"ss"});
		this.defaultFreezeDuration = TimeUnit.MINUTES.toMillis(60L);
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
	}

	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args){
		if(args.length < 1){
			sender.sendMessage(this.getUsage(label));
			return true;
		}
		final String reason = null;
		Long freezeTicks = this.defaultFreezeDuration;
		final long millis = System.currentTimeMillis();
		if(args[0].equalsIgnoreCase("all") && sender.hasPermission(command.getPermission() + ".all")){
			if(this.serverFrozenMillis == -1){
				this.serverFrozenMillis = millis + freezeTicks;
			}else{
				this.serverFrozenMillis = -1;
			}
			for(final Player on : Bukkit.getOnlinePlayers()){
				if(this.serverFrozenMillis == -1){
					this.frozenPlayers.remove(on.getUniqueId());
				}else{
					this.frozenPlayers.put(on.getUniqueId(), this.serverFrozenMillis);
				}
			}
			Command.broadcastCommandMessage(sender, ChatColor.translateAlternateColorCodes('&', "&eYou have frozen the server."));
			Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + "The server is " + ((serverFrozenMillis != -1) ? ("now frozen for " + DurationFormatUtils.formatDurationWords(freezeTicks, true, true)) : "no longer frozen") + ((reason == null) ? "" : (" with reason " + reason)) + '.');
			return true;
		}
		final Player target = Bukkit.getServer().getPlayer(args[0]);
		if(target == null || !BaseCommand.canSee(sender, target)){
			sender.sendMessage(String.format(BaseConstants.PLAYER_WITH_NAME_OR_UUID_NOT_FOUND, args[0]));
			return true;
		}
		if(target.equals(sender) && target.hasPermission("base.freeze.bypass")){
			sender.sendMessage(ChatColor.RED + "You cannot freeze yourself.");
			return true;
		}
		final UUID targetUUID = target.getUniqueId();
		final boolean shouldFreeze = this.getRemainingPlayerFrozenMillis(targetUUID) > 0L;
		final PlayerFreezeEvent playerFreezeEvent = new PlayerFreezeEvent(target, shouldFreeze);
		Bukkit.getServer().getPluginManager().callEvent(playerFreezeEvent);
		if(playerFreezeEvent.isCancelled()){
			sender.sendMessage(ChatColor.RED + "Unable to freeze " + target.getName() + '.');
			return false;
		}
		if(shouldFreeze){
			this.frozen.remove(target.getName());
			this.frozenPlayers.remove(targetUUID);
			target.closeInventory();
			target.sendMessage(ChatColor.GREEN + "You have been unfrozen.");
			Command.broadcastCommandMessage(sender, ChatColor.YELLOW + target.getName() + " is no longer frozen");
		}else{
			ParticleEffect.LAVA_SPARK.sphere(target.getPlayer(), target.getLocation(), 4.0f);
			this.frozen.add(target.getName());
			this.frozenPlayers.put(targetUUID, millis + freezeTicks);
			final String timeString = DurationFormatUtils.formatDurationWords(freezeTicks, true, true);
			this.Message(target.getName());


			this.inv = Bukkit.createInventory(null, 9, "Frozen");
			ItemStack freezeEng = new ItemStack(Material.PAPER, 1, (short) 3);
			ItemMeta freezeEngmeta = freezeEng.getItemMeta();
			freezeEngmeta.setLore((Arrays.asList((ChatColor.GRAY + " "), (ChatColor.RED + "You are now frozen"), (ChatColor.YELLOW + "You have 5 minutes"), (ChatColor.YELLOW + "to join Teamspeak: "), BasePlugin.getPlugin().getServerHandler().teamspeak)));
			freezeEngmeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "FROZEN");
			freezeEng.setItemMeta(freezeEngmeta);
			inv.setItem(4, freezeEng);


			ItemStack admit = new ItemStack(Material.BOOK, 1, (short) 3);
			ItemMeta admitmeta = freezeEng.getItemMeta();
			admitmeta.setLore((Arrays.asList((ChatColor.GRAY + " "), (ChatColor.RED + "Click to admit"))));
			admitmeta.setDisplayName(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Admit?");
			admit.setItemMeta(admitmeta);
			inv.setItem(0, admit);
			target.openInventory(inv);

			ItemStack ts = new ItemStack(Material.WOOL, 1, (short) 5);
			ItemMeta tsMeta = ts.getItemMeta();
			tsMeta.setLore((Arrays.asList((ChatColor.GRAY + " "), (ChatColor.GREEN + "Inform staff that you are joining teamspeak"), (ChatColor.YELLOW + BasePlugin.getPlugin().getServerHandler().teamspeak))));
			tsMeta.setDisplayName(ChatColor.DARK_GREEN.toString() + ChatColor.BOLD + "Teamspeak");
			ts.setItemMeta(tsMeta);
			inv.setItem(8, ts);

			Command.broadcastCommandMessage(sender, ChatColor.YELLOW + target.getName() + " is now frozen");
		}
		return true;
	}

	private void Message(final String name){
		final HashMap<String, Long> timer = new HashMap<String, Long>();
		final Player p = Bukkit.getPlayer(name);
		final BukkitTask task = new BukkitRunnable(){
			public void run(){
				if(FreezeCommand.this.frozen.contains(name)){
					p.sendMessage(ChatColor.WHITE + "\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588");
					p.sendMessage(ChatColor.WHITE + "\u2588\u2588\u2588\u2588" + ChatColor.RED + "\u2588" + ChatColor.WHITE + "\u2588\u2588\u2588\u2588");
					p.sendMessage(ChatColor.WHITE + "\u2588\u2588\u2588" + ChatColor.RED + "\u2588" + ChatColor.GOLD + ChatColor.DARK_RED + "\u2588" + ChatColor.GOLD + ChatColor.RED + "\u2588" + ChatColor.WHITE + "\u2588\u2588\u2588");
					p.sendMessage(ChatColor.WHITE + "\u2588\u2588" + ChatColor.RED + "\u2588" + ChatColor.GOLD + "\u2588" + ChatColor.DARK_RED + "\u2588" + ChatColor.GOLD + "\u2588" + ChatColor.RED + "\u2588" + ChatColor.WHITE + "\u2588\u2588");
					p.sendMessage(ChatColor.WHITE + "\u2588\u2588" + ChatColor.RED + "\u2588" + ChatColor.GOLD + "\u2588" + ChatColor.DARK_RED + "\u2588" + ChatColor.GOLD + "\u2588" + ChatColor.RED + "\u2588" + ChatColor.WHITE + "\u2588\u2588 " + ChatColor.YELLOW + "You have been frozen by a staff member.");
					p.sendMessage(ChatColor.WHITE + "\u2588\u2588" + ChatColor.RED + "\u2588" + ChatColor.GOLD + "\u2588" + ChatColor.DARK_RED + "\u2588" + ChatColor.GOLD + "\u2588" + ChatColor.RED + "\u2588" + ChatColor.WHITE + "\u2588\u2588 " + ChatColor.YELLOW + "If you logout you will be " + ChatColor.DARK_RED + ChatColor.BOLD + "BANNED" + ChatColor.YELLOW + '.');
					p.sendMessage(ChatColor.WHITE + "\u2588" + ChatColor.RED + "\u2588" + ChatColor.GOLD + "\u2588\u2588\u2588" + ChatColor.DARK_RED + ChatColor.GOLD + "\u2588\u2588" + ChatColor.RED + "\u2588" + ChatColor.WHITE + "\u2588 " + ChatColor.YELLOW + "Please connect to our Teamspeak" + ChatColor.YELLOW + '.');
					new Text(ChatColor.RED + "\u2588" + ChatColor.GOLD + "\u2588\u2588\u2588" + ChatColor.DARK_RED + "\u2588" + ChatColor.GOLD + "\u2588\u2588\u2588" + ChatColor.RED + "\u2588" + ChatColor.WHITE + ChatColor.GRAY + BasePlugin.getPlugin().getServerHandler().teamspeak + ChatColor.ITALIC + "Click me to connect" + ChatColor.GRAY + '.').setClick(ClickAction.OPEN_URL, "https://teamspeak.com").send(p);
					p.sendMessage(ChatColor.RED + "\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588");
					p.sendMessage(ChatColor.WHITE + "\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588");
				}else{
					this.cancel();
				}
			}
		}.runTaskTimerAsynchronously(BasePlugin.getPlugin(), 0L, 200L);
	}

	public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args){
		return (args.length == 1) ? null : Collections.emptyList();
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onEntityDamageByEntity(final EntityDamageByEntityEvent event){
		final Entity entity = event.getEntity();
		if(entity instanceof Player){
			final Player attacker = BukkitUtils.getFinalAttacker(event, false);
			if(attacker == null){
				return;
			}
			final Player player = (Player) entity;
			if((this.getRemainingServerFrozenMillis() > 0L || this.getRemainingPlayerFrozenMillis(player.getUniqueId()) > 0L) && !player.hasPermission("base.freeze.bypass")){
				attacker.sendMessage(ChatColor.RED + player.getName() + " is currently frozen, you can not attack them.");
				event.setCancelled(true);
				return;
			}
			if((this.getRemainingServerFrozenMillis() > 0L || this.getRemainingPlayerFrozenMillis(attacker.getUniqueId()) > 0L) && !attacker.hasPermission("base.freeze.bypass")){
				event.setCancelled(true);
				attacker.sendMessage(ChatColor.RED + "You may not attack players whilst frozen.");
			}
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPreCommandProcess(final PlayerCommandPreprocessEvent event){
		final Player player = event.getPlayer();
		if((this.getRemainingServerFrozenMillis() > 0L || this.getRemainingPlayerFrozenMillis(player.getUniqueId()) > 0L) && !player.hasPermission("base.freeze.bypass")){
			event.setCancelled(true);
			player.sendMessage(ChatColor.RED + "You may not use commands whilst frozen.");
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onDrop(final PlayerDropItemEvent e){
		final Player player = e.getPlayer();
		if((this.getRemainingServerFrozenMillis() > 0L || this.getRemainingPlayerFrozenMillis(player.getUniqueId()) > 0L) && !player.hasPermission("base.freeze.bypass")){
			e.setCancelled(true);
			return;
		}
	}


	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onChestOpen(PlayerInteractEvent event){
		if((this.getRemainingServerFrozenMillis() > 0L || this.getRemainingPlayerFrozenMillis(event.getPlayer().getUniqueId()) > 0L) && !event.getPlayer().hasPermission("base.freeze.bypass")){
			if(event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.CHEST || event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.CHEST){
				event.setCancelled(true);
			}
		}
	}


	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPlayerMove(final PlayerMoveEvent event){
		final Location from = event.getFrom();
		final Location to = event.getTo();
		if(from.getBlockX() == to.getBlockX() && from.getBlockZ() == to.getBlockZ()){
			return;
		}
		final Player player = event.getPlayer();
		if((this.getRemainingServerFrozenMillis() > 0L || this.getRemainingPlayerFrozenMillis(player.getUniqueId()) > 0L) && !player.hasPermission("base.freeze.bypass")){
			event.setTo(event.getFrom());
		}
	}

	@EventHandler
	public void onPlayerQuit(final PlayerQuitEvent e){
		if(this.frozen.contains(e.getPlayer().getName())){
			for(final Player online : Bukkit.getOnlinePlayers()){
				if(online.hasPermission("base.command.freeze")){
					Player p = e.getPlayer();
					this.frozen.remove(p.getName());
					this.frozenPlayers.remove(p.getUniqueId());
					online.sendMessage(" ");
					online.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l" + p.getName() + " has logged out while frozen."));
					// new Text(ChatColor.YELLOW + p.getName() + ChatColor.RED + " has logged out while frozen. " + ChatColor.GRAY + "(Click to ban)").setHoverText(ChatColor.YELLOW + "Click to ban " + p.getName()).setClick(ClickAction.RUN_COMMAND, "/ban " + p.getName() + " Disconnected while frozen").send(online);
					online.sendMessage(" ");

				}
			}
		}
	}

	public long getRemainingServerFrozenMillis(){
		return this.serverFrozenMillis - System.currentTimeMillis();
	}

	public long getRemainingPlayerFrozenMillis(final UUID uuid){
		final long remaining = this.frozenPlayers.get(uuid);
		if(remaining == this.frozenPlayers.getNoEntryValue()){
			return 0L;
		}
		return remaining - System.currentTimeMillis();
	}

	@EventHandler
	public void onInvClose(InventoryCloseEvent e){

		Player player = (Player) e.getPlayer();

		if((this.getRemainingServerFrozenMillis() > 0L || this.getRemainingPlayerFrozenMillis(player.getUniqueId()) > 0L) && !player.hasPermission("base.freeze.bypass")){

			//Cant reopen an inventory in the same tick as the InventoryCloseEvent so we wait 1 tick to open it.
			new BukkitRunnable(){
				@Override
				public void run(){
					player.openInventory(inv);
				}
			}.runTaskLater(BasePlugin.getPlugin(), 1);

		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event){
		Player player = (Player) event.getWhoClicked();
		ItemStack clicked = event.getCurrentItem();
		Inventory inventory = event.getInventory();
		if(inventory.getName().equals("Frozen")){
			if(clicked.getType() == Material.BOOK){
				for(final Player online : Bukkit.getOnlinePlayers()){
					if(online.hasPermission("base.command.freeze")){
						online.sendMessage(" ");
						online.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l" + player.getName() + " has clicked to admit."));
						//  new Text(ChatColor.RED + player.getName() + " has clicked to admit").send(online);
						online.sendMessage(" ");
						online.playSound(player.getLocation(), Sound.ANVIL_BREAK, 1.25F, 1.25F);
						inv.setItem(0, new ItemStack(Material.AIR));
					}
				}
			}
			if(clicked.getType() == Material.WOOL){
				for(final Player online : Bukkit.getOnlinePlayers()){
					if(online.hasPermission("base.command.freeze")){
						online.sendMessage(" ");
						online.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&l" + player.getName() + " has said he/she is joining teamspeak."));
						online.sendMessage(" ");
						online.playSound(player.getLocation(), Sound.LEVEL_UP, 1.25F, 1.25F);
						inv.setItem(8, new ItemStack(Material.AIR));
					}
				}
			}
			event.setCancelled(true);

		}

	}

}