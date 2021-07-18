package de.suders.srperms.command;

import java.util.UUID;

import de.suders.srperms.system.AdaptiveSrGroup;
import de.suders.srperms.system.AdaptiveSrPlayer;
import de.suders.srperms.system.GroupManager;
import de.suders.srperms.system.SrGroup;
import de.suders.srperms.system.SrPlayerGroup;
import de.suders.srperms.utils.UUIDUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

/**
* Author: SrSuders aka. Mario-Angelo
* Date: 18.07.2021
* Project: srperms
*/
public class SrPerms extends Command {

	private final String prefix = "§cSrPerms §8| §7";
	
	public SrPerms() {
		super("srperms");
	}

	// /srperms group/player ad
	
	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender instanceof ProxiedPlayer) {
			final ProxiedPlayer proxiedPlayer = (ProxiedPlayer) sender;
			if(!proxiedPlayer.hasPermission("*")) {
				sender.sendMessage("§cSorry, but you don't have permission to execute that command.");
				return;
			}
		}
		
		if(args.length == 3) {
			if(args[0].equalsIgnoreCase("group")) {
				if(args[2].equalsIgnoreCase("info")) {
					final AdaptiveSrGroup group = GroupManager.getGroup(args[1]);
					if(groupCheck(group, sender)) return;
					final StringBuilder stringBuilder = new StringBuilder();
					for(AdaptiveSrGroup subGroup : group.getSubGroups()) 
						stringBuilder.append("§c" + subGroup.getIdentifier() + "§7, ");
					String subGroups = stringBuilder.toString();
					subGroups = subGroups.substring(0, subGroups.length()-4);
					sender.sendMessage(prefix + "\n"
							+ "Name: §c" + group.getIdentifier() + "\n"
							+ "§7Members: §c" + group.getMembers().size() + "\n"
							+ "§7Subgroups: " + subGroups);
					return;
				} else if(args[2].equalsIgnoreCase("create")) {
					final AdaptiveSrGroup group = GroupManager.getGroup(args[1]);
					if(group != null) {
						sender.sendMessage(prefix + "§cDie Gruppe existiert bereits.");
						return;
					}
					GroupManager.addGroup(new SrGroup(prefix));
					sender.sendMessage(prefix + "Du hast erfolgreich die Gruppe §e" + args[1] + " §7erstellt");
					return;
				} else if(args[2].equalsIgnoreCase("delete")) {
					final AdaptiveSrGroup group = GroupManager.getGroup(args[1]);
					if(groupCheck(group, sender)) return;
					if(group.isDefault()) {
						sender.sendMessage(prefix + "§cDu kannst keine default Gruppe löschen.");
						return;
					}
					GroupManager.deleteGroup(group);
					sender.sendMessage(prefix + "§cDu hast die Gruppe " + args[1] + " erfolgreich gelöscht.");
					return;
				} else if(args[2].equalsIgnoreCase("setdefault")) {
					final AdaptiveSrGroup group = GroupManager.getGroup(args[1]);
					if(groupCheck(group, sender)) return;
					GroupManager.setDefaultGroup(group);
					sender.sendMessage(prefix + "§cDu hast die Gruppe §e" + args[1] + " §cauf §c§ldefault §cgestellt.");
					return;
				}
			} else if(args[0].equalsIgnoreCase("player")) {
				if(args[2].equalsIgnoreCase("info")) {
					final UUID uuid = UUIDUtils.getUUID(args[1]);
					if(uuid == null) {
						sender.sendMessage(prefix + "§cDer Spieler existiert nicht.");
						return;
					}
					final AdaptiveSrPlayer player = GroupManager.getPlayer(uuid);
					
					final StringBuilder stringBuilder = new StringBuilder();
					for(SrPlayerGroup group : player.getGroups().values())
						stringBuilder.append("§c" + group.getIdentifier() + "§7, ");
					String groups = stringBuilder.toString();
					groups = groups.substring(0, groups.length()-4);
					sender.sendMessage(prefix + "\n"
							+ "Name: §c" + args[1] + "\n"
							+ "§7Gruppen: §c" + groups + "\n");
					return;
				}
			}
		} else if(args.length == 4) {
			if(args[0].equalsIgnoreCase("group")) {
				if(args[2].equalsIgnoreCase("add") || args[2].equalsIgnoreCase("remove")) {
					final AdaptiveSrGroup group = GroupManager.getGroup(args[1]);
					if(groupCheck(group, sender)) return;
					if(args[2].equalsIgnoreCase("add"))
						group.addPermission(args[3].toLowerCase());
					else group.removePermission(prefix);
					sender.sendMessage(prefix + "§cDu hast erfolgreich " + args[3] + " die Rechte " + (args[3].equalsIgnoreCase("add") ? "hinzugefügt" : "entfernt") + ".");
					return;
				}
			} else if(args[0].equalsIgnoreCase("player")) {
				if(args[2].equalsIgnoreCase("add") || args[2].equalsIgnoreCase("remove")) {
					final UUID uuid = UUIDUtils.getUUID(args[1]);
					if(uuid == null) {
						sender.sendMessage(prefix + "§cDer Spieler existiert nicht.");
						return;
					}
					final AdaptiveSrPlayer player = GroupManager.getPlayer(uuid);
					if(args[2].equalsIgnoreCase("add"))
						player.addPermision(args[3].toLowerCase());
					else 
						player.removePermission(args[3].toLowerCase());
					sender.sendMessage(prefix + "§cDu hast erfolgreich " + args[3] + " die Rechte " + (args[3].equalsIgnoreCase("add") ? "hinzugefügt" : "entfernt") + ".");
					return;
				} else if(args[2].equalsIgnoreCase("addgroup")) {
					final UUID uuid = UUIDUtils.getUUID(args[1]);
					if(uuid == null) {
						sender.sendMessage(prefix + "§cDer Spieler existiert nicht.");
						return;
					}
					final AdaptiveSrPlayer player = GroupManager.getPlayer(uuid);
					final AdaptiveSrGroup group = GroupManager.getGroup(args[3]);
					if(groupCheck(group, sender)) return;
					player.addGroup(group.getIdentifier(), -1L);
					sender.sendMessage(prefix + "§cDu hast dem Spieler die Gruppe erfolgreich hinzugefügt.");
					return;
				} else if(args[2].equalsIgnoreCase("setgroup")) {
					final UUID uuid = UUIDUtils.getUUID(args[1]);
					if(uuid == null) {
						sender.sendMessage(prefix + "§cDer Spieler existiert nicht.");
						return;
					}
					final AdaptiveSrPlayer player = GroupManager.getPlayer(uuid);
					final AdaptiveSrGroup group = GroupManager.getGroup(args[3]);
					if(groupCheck(group, sender)) return;
					player.getGroups().clear();
					player.addGroup(group.getIdentifier(), -1L);
					sender.sendMessage(prefix + "§cDer Spieler hat jetzt die von dir ausgewählte Gruppe erhalten.");
					return;
				} else if(args[2].equalsIgnoreCase("removegroup")) {
					final UUID uuid = UUIDUtils.getUUID(args[1]);
					if(uuid == null) {
						sender.sendMessage(prefix + "§cDer Spieler existiert nicht.");
						return;
					}
					final AdaptiveSrPlayer player = GroupManager.getPlayer(uuid);
					final AdaptiveSrGroup group = GroupManager.getGroup(args[3]);
					if(groupCheck(group, sender)) return;
					if(player.getGroups().size() == 1 && player.getGroups().values().stream().findFirst().get().getGroup().isDefault()) {
						sender.sendMessage(prefix + "§cDu kannst dem Spieler keine Gruppe entfernen.");
						return;
					}
					player.removeGroup(group.getIdentifier());
					group.getMembers().remove(player.getUniqueId());
					if(player.getGroups().isEmpty() || player.getGroups().size() == 0) 
						player.addGroup(GroupManager.getDefaultGroup(), -1L);
					sender.sendMessage(prefix + "§cDu hast den Spieler erfolgreich aus der Gruppe entfernt.");
					return;
				}
			}
		} else if(args.length == 1) {
			if(args[0].equalsIgnoreCase("save")) {
				GroupManager.saveDatas();
				sender.sendMessage(prefix + "Die Daten wurden erfolgreich abgespeichert.");
				return;
			}
		}
		sender.sendMessage(prefix + "§cSyntax Error. Bitte benutze: \n"
				+ "/srperms save\n"
				+ "/srperms group <group> info\n"
				+ "/srperms group <group> create\n"
				+ "/srperms group <group> delete\n"
				+ "/srperms group <group> setdefault\n"
				+ "/srperms group <group> add/remove <permision>"
				+ "/srperms player <player> add/remove <permission>");
	}
	
	@SuppressWarnings("deprecation")
	private boolean groupCheck(final AdaptiveSrGroup group, final CommandSender sender) {
		if(group == null) {
			sender.sendMessage(prefix + "§cDie Gruppe existiert nicht.");
			return true;
		}
		return false;
	}
}
