package com.github.michaelbull.rs;

import com.github.michaelbull.rs.bestiary.Bestiary;
import com.github.michaelbull.rs.ge.GrandExchange;
import com.github.michaelbull.rs.hiscores.Hiscores;

/**
 * Represents an instance of the RuneScape web-services API.
 */
public final class RuneScapeAPI {

	/**
	 * Creates a new {@link RuneScapeAPI} backed by a specific {@link Client} implementation.
	 * @param client The {@link Client} implementation.
	 * @return The {@link RuneScapeAPI}.
	 */
	public static RuneScapeAPI create(Client client) {
		return new RuneScapeAPI(client);
	}

	/**
	 * Creates a new {@link RuneScapeAPI} backed by a {@link HttpClient}.
	 * @return The {@link RuneScapeAPI}.
	 */
	public static RuneScapeAPI createHttp() {
		return create(new HttpClient());
	}

	/**
	 * The {@link Bestiary}.
	 */
	private final Bestiary bestiary;

	/**
	 * The {@link GrandExchange}.
	 */
	private final GrandExchange grandExchange;

	/**
	 * The {@link Hiscores}.
	 */
	private final Hiscores hiscores;

	/**
	 * Creates a new {@link RuneScapeAPI}.
	 * @param client The {@link Client} to use.
	 */
	private RuneScapeAPI(Client client) {
		this.bestiary = new Bestiary(client);
		this.grandExchange = new GrandExchange(client);
		this.hiscores = new Hiscores(client);
	}

	/**
	 * Gets the {@link Bestiary}.
	 * @return The {@link Bestiary}.
	 */
	public Bestiary bestiary() {
		return bestiary;
	}

	/**
	 * Gets the {@link GrandExchange}.
	 * @return The {@link GrandExchange}.
	 */
	public GrandExchange grandExchange() {
		return grandExchange;
	}

	/**
	 * Gets the {@link Hiscores}.
	 * @return The {@link Hiscores}.
	 */
	public Hiscores hiscores() {
		return hiscores;
	}
}
