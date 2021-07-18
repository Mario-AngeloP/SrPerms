package de.suders.srperms.io;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import de.suders.srperms.storage.Const;

/**
 * Author: SrSuders aka. Mario-Angelo Date: 06.04.2021 Project: chestlink
 */
public class ConfigReader {

	private Gson gson;

	public ConfigReader() {
		this.gson = new Gson();
	}

	public JsonObject getMongoDB() {
		final File f = new File(Const.FOLDER + "mongodb.json");
		if (!f.exists()) {
			if (!f.getParentFile().exists())
				f.getParentFile().mkdir();
			JsonObject cfg = new JsonObject();
			cfg.addProperty("user", "user");
			cfg.addProperty("password", "password");
			cfg.addProperty("port", "27017");
			cfg.addProperty("host", "127.0.0.1");
			cfg.addProperty("database", "database");
			write(f, cfg.toString());
			return cfg;
		}
		return gson.fromJson(read(f), JsonObject.class);
	}

	private void write(File file, String content) {
		try {
			file.createNewFile();
			PrintWriter writer = new PrintWriter(file);
			writer.write(content);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String read(File file) {
		StringBuilder result = new StringBuilder();
		try {
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				result.append(scanner.nextLine());
			}
			scanner.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result.toString();
	}
}