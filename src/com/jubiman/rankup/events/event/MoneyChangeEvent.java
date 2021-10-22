package com.jubiman.rankup.events.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class MoneyChangeEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	private final Player player;
	public MoneyChangeEvent(Player p) {
		player = p;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public Player getPlayer() {
		return player;
	}
}
