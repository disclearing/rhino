package com.sergivb01.base;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.sergivb01.base.user.BaseUser;
import com.sergivb01.base.user.UserManager;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ProtocolHook{

	public static void hook(final BasePlugin basePlugin){
		final ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
		final UserManager userManager = basePlugin.getUserManager();
		protocolManager.addPacketListener(new PacketAdapter(basePlugin, PacketType.Play.Server.ENTITY_EQUIPMENT){
			public void onPacketSending(final PacketEvent event){
				if(!basePlugin.getServerHandler().useProtocolLib){
					return;
				}
				final Player player = event.getPlayer();
				final BaseUser baseUser = userManager.getUser(player.getUniqueId());
				if(!baseUser.isGlintEnabled()){
					final PacketContainer packet = event.getPacket();
					final StructureModifier<ItemStack> modifier = packet.getItemModifier();
					if(modifier.size() > 0){
						final ItemStack stack = modifier.read(0);
						if(stack != null && stack.getType() != Material.AIR){
							convert(stack);
						}
					}
				}
			}
		});

		protocolManager.addPacketListener(new PacketAdapter(basePlugin, PacketType.Play.Server.ENTITY_METADATA){
			public void onPacketSending(final PacketEvent event){
				if(!basePlugin.getServerHandler().useProtocolLib){
					return;
				}
				final Player player = event.getPlayer();
				final BaseUser baseUser = userManager.getUser(player.getUniqueId());
				if(!baseUser.isGlintEnabled()){
					final PacketContainer packet = event.getPacket();
					final StructureModifier<Entity> modifier = packet.getEntityModifier(event);
					if(modifier.size() > 0 && modifier.read(0) instanceof Item){
						final WrappedDataWatcher watcher = new WrappedDataWatcher(packet.getWatchableCollectionModifier().read(0));
						if(watcher.size() >= 10){
							final ItemStack stack = watcher.getItemStack(10).clone();
							if(stack != null && stack.getType() != Material.AIR){
								convert(stack);
							}
						}
					}
				}
			}
		});

	}

	private static void convert(final ItemStack origin){
		if(origin == null || origin.getType() == Material.AIR){
			return;
		}
		switch(origin.getType()){
			case POTION:
			case GOLDEN_APPLE:{
				if(origin.getDurability() > 0){
					origin.setDurability((short) 0);
					break;
				}
				break;
			}
			case ENCHANTED_BOOK:{
				origin.setType(Material.BOOK);
				break;
			}
			default:{
				origin.getEnchantments().keySet().forEach(origin::removeEnchantment);
				break;
			}
		}
	}

}