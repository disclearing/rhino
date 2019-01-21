package com.sergivb01.hcf;

import com.google.common.base.Joiner;
import com.sergivb01.base.BasePlugin;
import com.sergivb01.hcf.balance.*;
import com.sergivb01.hcf.classes.PvpClassManager;
import com.sergivb01.hcf.classes.archer.ArcherClass;
import com.sergivb01.hcf.combatlog.CombatLogListener;
import com.sergivb01.hcf.combatlog.CustomEntityRegistration;
import com.sergivb01.hcf.commands.*;
import com.sergivb01.hcf.commands.crate.KeyListener;
import com.sergivb01.hcf.commands.crate.KeyManager;
import com.sergivb01.hcf.commands.crate.LootExecutor;
import com.sergivb01.hcf.commands.death.DeathExecutor;
import com.sergivb01.hcf.commands.lives.LivesExecutor;
import com.sergivb01.hcf.commands.spawn.SpawnCommand;
import com.sergivb01.hcf.commands.spawn.TokenExecutor;
import com.sergivb01.hcf.database.mongo.MongoManager;
import com.sergivb01.hcf.database.redis.RedisManager;
import com.sergivb01.hcf.deathban.Deathban;
import com.sergivb01.hcf.deathban.DeathbanListener;
import com.sergivb01.hcf.deathban.DeathbanManager;
import com.sergivb01.hcf.deathban.FlatFileDeathbanManager;
import com.sergivb01.hcf.deathban.hospital.HospitalListeners;
import com.sergivb01.hcf.events.CaptureZone;
import com.sergivb01.hcf.events.EventExecutor;
import com.sergivb01.hcf.events.EventScheduler;
import com.sergivb01.hcf.events.conquest.ConquestExecutor;
import com.sergivb01.hcf.events.eotw.EOTWHandler;
import com.sergivb01.hcf.events.eotw.EotwCommand;
import com.sergivb01.hcf.events.eotw.EotwListener;
import com.sergivb01.hcf.events.faction.CapturableFaction;
import com.sergivb01.hcf.events.faction.ConquestFaction;
import com.sergivb01.hcf.events.faction.KothFaction;
import com.sergivb01.hcf.events.koth.KothExecutor;
import com.sergivb01.hcf.faction.FactionExecutor;
import com.sergivb01.hcf.faction.FactionManager;
import com.sergivb01.hcf.faction.FactionMember;
import com.sergivb01.hcf.faction.FlatFileFactionManager;
import com.sergivb01.hcf.faction.claim.Claim;
import com.sergivb01.hcf.faction.claim.ClaimHandler;
import com.sergivb01.hcf.faction.claim.ClaimWandListener;
import com.sergivb01.hcf.faction.claim.Subclaim;
import com.sergivb01.hcf.faction.type.*;
import com.sergivb01.hcf.listeners.*;
import com.sergivb01.hcf.listeners.fixes.*;
import com.sergivb01.hcf.payloads.types.Payload;
import com.sergivb01.hcf.payloads.types.StatusPayload;
import com.sergivb01.hcf.scoreboard.ScoreboardHandler;
import com.sergivb01.hcf.tab.PlayerTab;
import com.sergivb01.hcf.timer.TimerExecutor;
import com.sergivb01.hcf.timer.TimerManager;
import com.sergivb01.hcf.timer.type.KeyAllTimer;
import com.sergivb01.hcf.timer.type.KeySaleTimer;
import com.sergivb01.hcf.timer.type.SaleTimer;
import com.sergivb01.hcf.timer.type.SotwTimer;
import com.sergivb01.hcf.user.FactionUser;
import com.sergivb01.hcf.user.UserManager;
import com.sergivb01.hcf.utils.*;
import com.sergivb01.hcf.utils.config.ConfigurationService;
import com.sergivb01.hcf.utils.config.PotionLimiterData;
import com.sergivb01.hcf.utils.runnables.AutoSaveRunnable;
import com.sergivb01.hcf.utils.runnables.DonorBroadcastRunnable;
import com.sergivb01.hcf.utils.runnables.StatusPayloadRunnable;
import com.sergivb01.hcf.visualise.ProtocolLibHook;
import com.sergivb01.hcf.visualise.VisualiseHandler;
import com.sergivb01.hcf.visualise.WallBorderListener;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import lombok.Getter;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Getter
public class HCF extends JavaPlugin {
	public static final Joiner SPACE_JOINER = Joiner.on(' ');
	public static final long HOUR = TimeUnit.HOURS.toMillis(1);
	private static final long MINUTE = TimeUnit.MINUTES.toMillis(1);
	public static Permission permission = null;
	public static Chat chat = null;
	public static Economy econ = null;
	public static List<UUID> cbUser;
	private static HCF plugin;
	private Message message;
	private WorldEditPlugin worldEdit;
	private ClaimHandler claimHandler;
	private SotwTimer sotwTimer;
	private KeySaleTimer keySaleTimer;
	private SaleTimer saleTimer;
	private KeyAllTimer keyAllTimer;
	private KeyManager keyManager;
	private DeathbanManager deathbanManager;
	private EconomyManager economyManager;
	private EOTWHandler eotwHandler;
	private FactionManager factionManager;
	private PvpClassManager pvpClassManager;
	private ScoreboardHandler scoreboardHandler;
	private TimerManager timerManager;
	private UserManager userManager;
	private VisualiseHandler visualiseHandler;
	private EventScheduler eventScheduler;

	public static String getRemaining(long millis, boolean milliseconds){
		return HCF.getRemaining(millis, milliseconds, true);
	}

	public static String getRemaining(long duration, boolean milliseconds, boolean trail){
		if(milliseconds && duration < MINUTE){
			return (trail ? DateTimeFormats.REMAINING_SECONDS_TRAILING : DateTimeFormats.REMAINING_SECONDS).get().format((double) duration * 0.001) + 's';
		}
		return org.apache.commons.lang.time.DurationFormatUtils.formatDuration(duration, (duration >= HOUR ? "HH:" : "") + "mm:ss");
	}

	public static HCF getInstance(){
		return plugin;
	}

	public static HCF getPlugin(){
		return plugin;
	}

	@Override
	public void onEnable(){
		if(this.getConfig().getBoolean("nucleus")) {
			getLogger().severe("Using nucleus as dependency");
		} else {
			if(!setupVault()){
				getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
				getServer().getPluginManager().disablePlugin(this);
				return;
			}
			setupPermissions();
		}

		plugin = this;


		cbUser = new ArrayList<>();
		registerClientCheck();

		new DonorBroadcastRunnable();


		CustomEntityRegistration.registerCustomEntities();
		ProtocolLibHook.hook(this);

		this.saveDefaultConfig();

		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		ConfigurationService.init(this.getConfig());
		PotionLimiterData.getInstance().setup(this);
		PotionLimitListener.reload();


		worldEdit = Bukkit.getPluginManager().getPlugin("WorldEdit") instanceof WorldEditPlugin && Bukkit.getPluginManager().getPlugin("WorldEdit").isEnabled() ? (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit") : null;
		eventScheduler = new EventScheduler(this);

		registerConfiguration();
		registerCommands();
		registerManagers();
		registerListeners();

		Cooldowns.createCooldown("revive_cooldown");
		Cooldowns.createCooldown("Assassin_item_cooldown");
		Cooldowns.createCooldown("Archer_item_cooldown");
		Cooldowns.createCooldown("Archer_jump_cooldown");

		timerManager.enable();

		initDatabases();

		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&d[Rhino-HCF] &7|- Plugin has been enabled"));
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&d[Rhino-HCF] &7|- &bVersion: &f" + HCF.getPlugin().getDescription().getVersion()));

		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new AutoSaveRunnable(), 300 * 20L, 900 * 20L);

		if(ConfigurationService.REDIS_ENABLED){
			Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new StatusPayloadRunnable(), 10L, 5 * 20L);
		}
	}

	private void initDatabases(){
		if(ConfigurationService.REDIS_ENABLED){
			new RedisManager(this);
		}
		if(ConfigurationService.MONGO_ENABLED){
			new MongoManager(this);
		}
	}

	public void saveData(){
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "save-all");
		BasePlugin.getPlugin().getServerHandler().saveServerData(); //Base data

		Bukkit.getOnlinePlayers().forEach(Player::saveData);//Save data

		deathbanManager.saveDeathbanData(); //Deathbans
		economyManager.saveEconomyData(); //Balance
		factionManager.saveFactionData(); //Factions
		userManager.saveUserData(); //User settings
		keyManager.saveKeyData(); //Key things
	}

	public void onDisable(){
		if(ConfigurationService.REDIS_ENABLED){
			Map<String, UUID> map = new HashMap<>(Bukkit.getOnlinePlayers().stream()
					.filter(p -> p.hasPermission("hcf.utils.staff"))
					.collect(Collectors.toMap(HumanEntity::getName, Entity::getUniqueId)));
			Payload payload = new StatusPayload(Bukkit.getOnlinePlayers().size(),
					Bukkit.getMaxPlayers(),
					Bukkit.hasWhitelist(),
					BasePlugin.getPlugin().getServerHandler().isDonorOnly(),
					false,
					map
			);
			payload.send();
		}

		Bukkit.getServer().savePlayers();
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "save-all");
		CustomEntityRegistration.unregisterCustomEntities();
		CombatLogListener.removeCombatLoggers();
		pvpClassManager.onDisable();
		scoreboardHandler.clearBoards();
		saveData();
		timerManager.disable();
	}

	private void registerClientCheck() {
		Bukkit.getPluginManager().registerEvents(new ClientAPI(), this);
	}

	private void registerConfiguration(){
		ConfigurationSerialization.registerClass(CaptureZone.class);
		ConfigurationSerialization.registerClass(Deathban.class);
		ConfigurationSerialization.registerClass(Claim.class);
		ConfigurationSerialization.registerClass(Subclaim.class);
		ConfigurationSerialization.registerClass(Deathban.class);
		ConfigurationSerialization.registerClass(FactionUser.class);
		ConfigurationSerialization.registerClass(ClaimableFaction.class);
		ConfigurationSerialization.registerClass(ConquestFaction.class);
		ConfigurationSerialization.registerClass(CapturableFaction.class);
		ConfigurationSerialization.registerClass(KothFaction.class);
		ConfigurationSerialization.registerClass(EndPortalFaction.class);
		ConfigurationSerialization.registerClass(Faction.class);
		ConfigurationSerialization.registerClass(FactionMember.class);
		ConfigurationSerialization.registerClass(PlayerFaction.class);
		ConfigurationSerialization.registerClass(RoadFaction.class);
		ConfigurationSerialization.registerClass(RoadFaction.class);
		ConfigurationSerialization.registerClass(SpawnFaction.class);
		ConfigurationSerialization.registerClass(GlowstoneFaction.class);
		ConfigurationSerialization.registerClass(RoadFaction.NorthRoadFaction.class);
		ConfigurationSerialization.registerClass(RoadFaction.EastRoadFaction.class);
		ConfigurationSerialization.registerClass(RoadFaction.SouthRoadFaction.class);
		ConfigurationSerialization.registerClass(RoadFaction.WestRoadFaction.class);
		ConfigurationSerialization.registerClass(GlowstoneFaction.class);
	}

	private void registerListeners(){
		if(ConfigurationService.TAB){
			new PlayerTab(this);
		}
		new PayloadsListener(this);
		new PotionLimitListener(this);
		new AutoRespawnListener(this);
		new PortalFixListener(this);
		new ElevatorListener(this);
		new EndPortalCommand(this);
		new PermissionsCommand(this);
		new ColonFix(this);
		new PotionListener(this);
		new PexCrashFix(this);
		new DupeGlitchFix(this);
		new DonorOnlyListener(this);
		new ArcherClass(this);
		new KeyListener(this);
		new WeatherFixListener(this);
		new EndermanFixListener(this);
		new MinecartElevatorListener(this);
		new StoreCommand(this);
		new AutoSmeltOreListener(this);
		new BlockHitFixListener(this);
		new BlockJumpGlitchFixListener(this);
		new HungerFixListener(this);
		new BoatGlitchFixListener(this);
		new BookDeenchantListener(this);
		new BorderListener(this);
		new BottledExpListener(this);
		new ChatListener(this);
		new ClaimWandListener(this);
		new CombatLogListener(this);
		new CoreListener(this);
		new CreeperFriendlyListener(this);
		new CrowbarListener(this);
		new DeathListener(this);
		new DeathMessageListener(this);
		new DeathSignListener(this);
		new DeathbanListener(this);
		new EnchantLimitListener(this);
		new EnderChestRemovalListener(this);
		new EntityLimitListener(this);

		//TODO: Switch between flatfile & mongodb
		new FlatFileFactionManager(this);
		new EndListener(this);
		new EotwListener(this);
		new EventSignListener(this);
		new ExpMultiplierListener(this);
		new EnchantSecurityListener(this);
		new FactionListener(this);
		new HitDetectionListener(this);
		new FoundDiamondsListener(this);
		new FurnaceSmeltSpeederListener(this);
		new InfinityArrowFixListener(this);
		new KitListener(this);
		new ItemStatTrackingListener(this);
		new PearlGlitchListener(this);
		new PotionLimitListener(this);
		new FactionsCoreListener(this);
		new SignSubclaimListener(this);
		new ShopSignListener(this);
		new SkullListener(this);
		new BookQuillFixListener(this);
		new BeaconStrengthFixListener(this);
		new VoidGlitchFixListener(this);
		new WallBorderListener(this);
		new WorldListener(this);
		new UnRepairableListener(this);
		new SotwListener(this);
		new HospitalListeners(this);
	}

	private void registerCommands(){
		getCommand("staffchat").setExecutor(new StaffChatCommand());
		getCommand("debug").setExecutor(new DebugCommand());
		getCommand("report").setExecutor(new ReportCommand());
		getCommand("request").setExecutor(new RequestCommand());
		getCommand("test").setExecutor(new TestCommand());
		getCommand("permissions").setExecutor(new PermissionsCommand(this));
		getCommand("reclaim").setExecutor(new ReclaimCommand(this));
		getCommand("platinum").setExecutor(new PlatinumReviveCommand(this));
		getCommand("teamspeak").setExecutor(new TeamspeakCommand());
		getCommand("supplydrop").setExecutor(new SupplydropCommand(this));
		getCommand("enderchest").setExecutor(new PlayerVaultCommand(this));
		getCommand("statreset").setExecutor(new StatResetCommand(this));
		getCommand("togglefd").setExecutor(new TogglefdCommand());
		getCommand("ffa").setExecutor(new FFACommand());
		getCommand("endportal").setExecutor(new EndPortalCommand(this));
		getCommand("toggleend").setExecutor(new ToggleEnd(this));
		getCommand("focus").setExecutor(new FactionFocusArgument(this));
		getCommand("sendcoords").setExecutor(new SendCoordsCommand(this));
		getCommand("spawner").setExecutor(new SpawnerCommand(this));
		getCommand("sotw").setExecutor(new SotwCommand(this));
		getCommand("conquest").setExecutor(new ConquestExecutor(this));
		getCommand("crowbar").setExecutor(new CrowbarCommand());
		getCommand("economy").setExecutor(new EconomyCommand(this));
		getCommand("eotw").setExecutor(new EotwCommand(this));
		getCommand("game").setExecutor(new EventExecutor(this));
		getCommand("help").setExecutor(new HelpCommand());
		getCommand("faction").setExecutor(new FactionExecutor(this));
		getCommand("gopple").setExecutor(new GoppleCommand(this));
		getCommand("stats").setExecutor(new PlayerStats());
		getCommand("koth").setExecutor(new KothExecutor(this));
		getCommand("check").setExecutor(new CheckCommand(this));
		getCommand("store").setExecutor(new StoreCommand(this));
		getCommand("lives").setExecutor(new LivesExecutor(this));
		getCommand("token").setExecutor(new TokenExecutor(this));
		getCommand("death").setExecutor(new DeathExecutor(this));
		getCommand("location").setExecutor(new LocationCommand(this));
		getCommand("logout").setExecutor(new LogoutCommand(this));
		getCommand("mapkit").setExecutor(new MapKitCommand(this));
		getCommand("pay").setExecutor(new PayCommand(this));
		getCommand("pvptimer").setExecutor(new PvpTimerCommand(this));
		getCommand("coords").setExecutor(new CoordsCommand(this));
		getCommand("servertime").setExecutor(new ServerTimeCommand());
		getCommand("spawn").setExecutor(new SpawnCommand(this));
		getCommand("timer").setExecutor(new TimerExecutor(this));
		getCommand("medic").setExecutor(new ReviveCommand(this));
		getCommand("savedata").setExecutor(new SaveDataCommand());
		getCommand("setborder").setExecutor(new SetBorderCommand());
		getCommand("loot").setExecutor(new LootExecutor(this));
		getCommand("safestop").setExecutor(new SafestopCommand());
		getCommand("nether").setExecutor(new NetherCommand(this));
		getCommand("cobble").setExecutor(new CobbleCommand(this));
		getCommand("ores").setExecutor(new OresCommand());
		getCommand("crowgive").setExecutor(new CrowbarGiveCommand());
		getCommand("user").setExecutor(new UserCommand(this));
		getCommand("keysale").setExecutor(new KeySaleCommand(this));
		getCommand("sale").setExecutor(new SaleCommand(this));
		getCommand("refund").setExecutor(new RefundCommand());
		getCommand("resetreclaim").setExecutor(new ResetReclaimCommand());
		getCommand("lff").setExecutor(new LFFCommand(this));
		getCommand("deathbanroom").setExecutor(new DeathbanRoomCommand(this));
		getCommand("keyall").setExecutor(new KeyAllCommand(this));

		final Map<String, Map<String, Object>> map = getDescription().getCommands();

		for(final Map.Entry<String, Map<String, Object>> entry : map.entrySet()){
			final PluginCommand command = getCommand(entry.getKey());
			command.setPermission("hcf.commands." + entry.getKey());
		}

	}

	private void registerManagers(){
		claimHandler = new ClaimHandler(this);
		deathbanManager = new FlatFileDeathbanManager(this);
		economyManager = new FlatFileEconomyManager(this);
		eotwHandler = new EOTWHandler(this);
		factionManager = new FlatFileFactionManager(this);
		pvpClassManager = new PvpClassManager(this);
		timerManager = new TimerManager(this);
		scoreboardHandler = new ScoreboardHandler(this);
		userManager = new UserManager(this);
		visualiseHandler = new VisualiseHandler();
		sotwTimer = new SotwTimer();
		keySaleTimer = new KeySaleTimer();
		saleTimer = new SaleTimer();
		keyAllTimer = new KeyAllTimer();
		keyManager = new KeyManager(this);
		message = new Message(this);
	}


	private boolean setupVault(){
		if(getServer().getPluginManager().getPlugin("Vault") == null){
			getLogger().severe("DB: Vault plugin = null");
			return false;
		}
		RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
		if(rsp == null){
			getLogger().severe("rsp = null");
			return false;
		}
		chat = rsp.getProvider();
		return chat != null;
	}

	public boolean setupPermissions() {
		RegisteredServiceProvider<Permission> rsp = Bukkit.getServer().getServicesManager().getRegistration(Permission.class);
		permission = rsp.getProvider();
		return permission != null;
	}
}