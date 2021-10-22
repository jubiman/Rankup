package me.desht.dhutils;

import com.jubiman.rankup.Rankup;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import java.util.*;
import java.util.logging.Level;
import java.util.regex.Pattern;

public class MiscUtil {
	private static final Map<String, String> prevColours = new HashMap<>();

	public static final String STATUS_COLOUR = ChatColor.AQUA.toString();
	public static final String ERROR_COLOUR = ChatColor.RED.toString();
	public static final String GENERAL_COLOUR = ChatColor.RESET.toString();

	private static final boolean colouredConsole = true;

	public static void errorMessage(CommandSender sender, String string) {
		setPrevColour(sender.getName(), ERROR_COLOUR);
		message(sender, ERROR_COLOUR + string, Level.WARNING);
		prevColours.remove(sender.getName());
	}

	public static void statusMessage(CommandSender sender, String string) {
		setPrevColour(sender.getName(), STATUS_COLOUR);
		message(sender, STATUS_COLOUR + string, Level.INFO);
		prevColours.remove(sender.getName());
	}

	public static void generalMessage(CommandSender sender, String string) {
		setPrevColour(sender.getName(), GENERAL_COLOUR);
		message(sender, GENERAL_COLOUR + string, Level.INFO);
		prevColours.remove(sender.getName());
	}

	private static void setPrevColour(String name, String colour) {
		prevColours.put(name, colour);
	}

	private static String getPrevColour(String name) {
		String colour = prevColours.get(name);
		return colour == null ? GENERAL_COLOUR : colour;
	}

	public static void rawMessage(CommandSender sender, String string) {
		boolean strip = sender instanceof ConsoleCommandSender && !colouredConsole;
		for (String line : string.split("\\n")) {
			if (strip) {
				sender.sendMessage(ChatColor.stripColor(line));
			} else {
				sender.sendMessage(line);
			}
		}
	}

	private static void message(CommandSender sender, String string, Level level) {
		boolean strip = sender instanceof ConsoleCommandSender && !colouredConsole;
		for (String line : string.split("\\n")) {
			if (strip) {
				Rankup.getInstance().getLogger().log(level, ChatColor.stripColor(parseColourSpec(sender, line)));
			} else {
				sender.sendMessage(parseColourSpec(sender, line));
			}
		}
	}

	private static final Pattern colourPat = Pattern.compile("(?<!&)&(?=[0-9a-fA-Fk-oK-OrR])");

	public static String parseColourSpec(CommandSender sender, String spec) {
		String who = sender == null ? "*" : sender.getName();
		String res = colourPat.matcher(spec).replaceAll("\u00A7");
		return res.replace("&-", getPrevColour(who)).replace("&&", "&");
	}
}