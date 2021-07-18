package de.suders.srperms.utils;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import de.suders.srperms.io.IOUtils;

final class NameFetcher {
	/**
	 * @param playerName The name of the player
	 * @return The UUID of the given player
	 */
	static UUID getUUID(String playerName) {
		final String url = "https://api.mojang.com/users/profiles/minecraft/" + playerName;
		try {
			final String uuidJson = callURL(url);
			if (!uuidJson.isEmpty()) {
				final JSONObject uuidObject = (JSONObject) JSONValue.parseWithException(uuidJson);
				return UUID.fromString(uuidObject.get("id").toString().replaceFirst(
						"(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)",
						"$1-$2-$3-$4-$5"));
			}

		} catch (ParseException ex) {
			ex.printStackTrace();
		}

		return null;
	}

	/**
	 * @param uuid The UUID of a player (can be trimmed or the normal version)
	 * @return The name of the given player
	 */
	static String getName(UUID uuid) {
		final String url = "https://api.mojang.com/user/profiles/" + uuid.toString().replace("-", "") + "/names";
		try {
			final String nameJson = callURL(url);
			final JSONArray nameValue = (JSONArray) JSONValue.parseWithException(nameJson);
			final String playerSlot = nameValue.get(nameValue.size() - 1).toString();
			final JSONObject nameObject = (JSONObject) JSONValue.parseWithException(playerSlot);
			return nameObject.get("name").toString();
		} catch (ParseException ex) {
			ex.printStackTrace();
		}
		return "error";
	}

	private static String callURL(String urlString) {
		final StringBuilder builder = new StringBuilder();
		try {
			final URL url = new URL(urlString);
			final URLConnection connection = url.openConnection();
			connection.setReadTimeout(60 * 1000);
			IOUtils.readURL(builder, connection);
		} catch (FileNotFoundException ignored) {
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return builder.toString();
	}

}