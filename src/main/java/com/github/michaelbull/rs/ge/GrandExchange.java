package com.github.michaelbull.rs.ge;

import com.github.michaelbull.rs.Client;
import com.github.michaelbull.rs.HttpClient;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Ints;

import java.io.IOException;
import java.util.Optional;

/**
 * Represents the RuneScape Grand Exchange API.
 * @see <a href="https://runescape.wiki/w/Application_programming_interface#Grand_Exchange_Database_API">Grand Exchange APIs</a>
 */
public final class GrandExchange {

	/**
	 * The URL to the Grand Exchange web-service.
	 */
	private static final String GRAND_EXCHANGE_URL = HttpClient.WEB_SERVICES_URL + "/m=itemdb_rs/api";

	/**
	 * The format of the URL to fetch a {@link Category}.
	 */
	private static final String CATEGORY_URL_FORMAT = GRAND_EXCHANGE_URL + "/catalogue/category.json?category=%d";

	/**
	 * The format of the URL to fetch an {@link Item}.
	 */
	private static final String ITEMS_URL_FORMAT = GRAND_EXCHANGE_URL + "/catalogue/items.json?category=%d&alpha=%s&page=%d";

	/**
	 * The format of the URL to fetch {@link GraphingData}.
	 */
	private static final String GRAPH_URL_FORMAT = GRAND_EXCHANGE_URL + "/graph/%d.json";

	/**
	 * The format of the URL to fetch {@link ItemPriceInformation}.
	 */
	private static final String DETAILS_URL_FORMAT = GRAND_EXCHANGE_URL + "/catalogue/detail.json?item=%d";

	/**
	 * The categories of {@link Item}s on the Grand Exchange.
	 * @see <a href="https://runescape.wiki/w/Application_programming_interface#items">Category IDs</a>
	 */
	public static final ImmutableList<String> CATEGORIES = ImmutableList.of(
		/* 00 */ "Miscellaneous",
		/* 01 */ "Ammo",
		/* 02 */ "Arrows",
		/* 03 */ "Bolts",
		/* 04 */ "Construction materials",
		/* 05 */ "Construction projects",
		/* 06 */ "Cooking ingredients",
		/* 07 */ "Costumes",
		/* 08 */ "Crafting materials",
		/* 09 */ "Familiars",
		/* 10 */ "Farming produce",
		/* 11 */ "Fletching materials",
		/* 12 */ "Food and drink",
		/* 13 */ "Herblore materials",
		/* 14 */ "Hunting equipment",
		/* 15 */ "Hunting produce",
		/* 16 */ "Jewellery",
		/* 17 */ "Mage armour",
		/* 18 */ "Mage weapons",
		/* 19 */ "Melee armour - low level",
		/* 20 */ "Melee armour - mid level",
		/* 21 */ "Melee armour - high level",
		/* 22 */ "Melee weapons - low level",
		/* 23 */ "Melee weapons - mid level",
		/* 24 */ "Melee weapons - high level",
		/* 25 */ "Mining and smithing",
		/* 26 */ "Potions",
		/* 27 */ "Prayer armour",
		/* 28 */ "Prayer materials",
		/* 29 */ "Range armour",
		/* 30 */ "Range weapons",
		/* 31 */ "Runecrafting",
		/* 32 */ "Runes, Spells and Teleports",
		/* 33 */ "Seeds",
		/* 34 */ "Summoning scrolls",
		/* 35 */ "Tools and containers",
		/* 36 */ "Woodcutting product",
		/* 37 */ "Pocket items",
		/* 38 */ "Stone spirits",
		/* 39 */ "Salvage"
	);

	/**
	 * The web-services {@link Client}.
	 */
	private final Client client;

	/**
	 * Creates a new {@link GrandExchange}.
	 * @param client The web-services {@link Client}.
	 */
	public GrandExchange(Client client) {
		this.client = Preconditions.checkNotNull(client);
	}

	/**
	 * Gets a {@link Category} by its id.
	 * @param categoryId The id of the {@link Category}.
	 * @return An {@link Optional} containing the {@link Category}, or {@link Optional#empty()} if there is no {@link Category} for the {@link Category} id.
	 * @throws IOException If an I/O error occurs.
	 * @see <a href="https://runescape.wiki/w/Application_programming_interface#category">Category information details</a>
	 */
	public Optional<Category> category(int categoryId) throws IOException {
		Preconditions.checkElementIndex(categoryId, CATEGORIES.size(), "Category id must be between 0 and " + (CATEGORIES.size() - 1) + " inclusive.");
		String url = String.format(CATEGORY_URL_FORMAT, categoryId);
		return client.fromJson(url, Category.class);
	}

	/**
	 * Gets a {@link Category} by its name.
	 * @param categoryName The name of the {@link Category}.
	 * @return An {@link Optional} containing the {@link Category}, or {@link Optional#empty()} if there is no {@link Category} for the {@link Category} name.
	 * @throws IOException If an I/O error occurs.
	 * @see <a href="https://runescape.wiki/w/Application_programming_interface#category">Category information details</a>
	 */
	public Optional<Category> category(String categoryName) throws IOException {
		Preconditions.checkNotNull(categoryName);
		int id = CATEGORIES.indexOf(categoryName);
		return category(id);
	}

	/**
	 * Gets a set {@link CategoryPrices} based on the {@link Category}'s id, an item's prefix, and specified page.
	 * @param categoryId The id of the {@link Category}.
	 * @param prefix An item's prefix.
	 * @param page The page.
	 * @return An {@link Optional} containing the {@link CategoryPrices}, or {@link Optional#empty()} if no {@link CategoryPrices} were found.
	 * @throws IOException If an I/O error occurs.
	 * @see <a href="https://runescape.wiki/w/Application_programming_interface#items">Category price details</a>
	 */
	public Optional<CategoryPrices> categoryPrices(int categoryId, String prefix, int page) throws IOException {
		Preconditions.checkElementIndex(categoryId, CATEGORIES.size(), "Category id must be between 0 and " + (CATEGORIES.size() - 1) + " inclusive.");
		Preconditions.checkArgument(!prefix.isEmpty(), "Prefix must be at least 1 character long.");

		String alpha;
		Integer prefixPercentage = Ints.tryParse(prefix);
		if (prefixPercentage != null) {
			alpha = "%" + prefixPercentage;
		} else {
			alpha = prefix;
		}

		String url = String.format(ITEMS_URL_FORMAT, categoryId, alpha, page);
		return client.fromJson(url, CategoryPrices.class);
	}

	/**
	 * Gets a set {@link CategoryPrices} based on the {@link Category}'s name, an item's prefix, and specified page.
	 * @param categoryName The name of the {@link Category}.
	 * @param prefix An item's prefix.
	 * @param page The page.
	 * @return An {@link Optional} containing the {@link CategoryPrices}, or {@link Optional#empty()} if no {@link CategoryPrices} were found.
	 * @throws IOException If an I/O error occurs.
	 * @see <a href="https://runescape.wiki/w/Application_programming_interface#items">Category price details</a>
	 */
	public Optional<CategoryPrices> categoryPrices(String categoryName, String prefix, int page) throws IOException {
		Preconditions.checkNotNull(categoryName);
		Preconditions.checkArgument(!prefix.isEmpty(), "Prefix must not be empty.");
		return categoryPrices(CATEGORIES.indexOf(categoryName), prefix, page);
	}

	/**
	 * Gets the {@link GraphingData} of an {@link Item}.
	 * @param itemId The id of the {@link Item}.
	 * @return An {@link Optional} containing the {@link GraphingData}, or {@link Optional#empty()} if no {@link GraphingData} was found for the {@link Item} id.
	 * @throws IOException If an I/O error occurs.
	 * @see <a href="https://runescape.wiki/w/Application_programming_interface#Graph">Graphing Data</a>
	 */
	public Optional<GraphingData> graphingData(int itemId) throws IOException {
		String url = String.format(GRAPH_URL_FORMAT, itemId);
		return client.fromJson(url, GraphingData.class);
	}

	/**
	 * Gets the {@link ItemPriceInformation} of an {@link Item}.
	 * @param itemId The id of the {@link Item}.
	 * @return An {@link Optional} containing the {@link ItemPriceInformation}, or {@link Optional#empty()} if no {@link ItemPriceInformation} was found for the {@link Item} id.
	 * @throws IOException If an I/O error occurs.
	 * @see <a href="https://runescape.wiki/w/Application_programming_interface#detail">GE Item price information</a>
	 */
	public Optional<ItemPriceInformation> itemPriceInformation(int itemId) throws IOException {
		String url = String.format(DETAILS_URL_FORMAT, itemId);
		return client.fromJson(url, ItemPriceInformation.class);
	}
}
