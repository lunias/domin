package com.domin.ui.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import com.domin.card.CardSet;

public class Settings {

	private static File getSettingsDirectory() throws IllegalStateException {
		String userHome = System.getProperty("user.home");
		if (userHome == null) {
			throw new IllegalStateException("user.home is null");
		}

		File home = new File(userHome);
		File settingsDirectory = new File(home, ".dominion");
		if (!settingsDirectory.exists()) {
			if (!settingsDirectory.mkdir()) {
				throw new IllegalStateException("Could not create directory "
						+ settingsDirectory.toString());
			}
		}

		return settingsDirectory;
	}

	public static boolean writeConnectionSettings(String host, int port,
			String username) {
		Properties props = new Properties();
		props.setProperty("host", host);
		props.setProperty("port", String.valueOf(port));
		props.setProperty("username", username);

		try {
			File file = new File(getSettingsDirectory(),
					"connection.properties");
			props.store(new FileOutputStream(file),
					"Dominion Connection Settings");
		} catch (Exception e) {
			System.out.println("Could not write connection.properties");
			return false;
		}

		return true;
	}

	public static Properties readConnectionSettings() {
		Properties props = new Properties();
		try {
			File file = new File(getSettingsDirectory(),
					"connection.properties");
			props.load(new FileInputStream(file));
		} catch (IOException e) {
			System.out.println("Could not read connection.properties");
			return null;
		}

		return props;
	}

	public static boolean writeRandomizerSettings(List<Boolean> setList,
			List<Integer> minList, List<Integer> maxList,
			List<Boolean> otherList) {

		Properties props = new Properties();
		
		int i = 0;		
		for (Boolean toggle : setList) {
			props.setProperty(CardSet.values()[i++].toString().toLowerCase(), String.valueOf(toggle));
		}
		
		i = 0;
		for (Integer min : minList) {
			props.setProperty("min_" + i++ + "_cost", min.toString());
		}
		
		i = 0;
		for (Integer max : maxList) {
			props.setProperty("max_" + i++ + "_cost", max.toString());
		}
		
		i = 0;
		for (Boolean toggle : otherList) {
			props.setProperty("other_" + i++, String.valueOf(toggle));
		}
		
		try {
			File file = new File(getSettingsDirectory(), "randomizer.properties");
			props.store(new FileOutputStream(file), "Dominion Randomizer Settings");
		} catch (Exception e) {
			System.out.println("Could not write randomizer.properties");
			return false;
		}

		return true;		
	}
	
	public static Properties readRandomizerSettings() {
		Properties props = new Properties();
		try {
			File file = new File(getSettingsDirectory(), "randomizer.properties");
			props.load(new FileInputStream(file));
		} catch (IOException e) {
			System.out.println("Could not read randomizer.properties");
			return null;			
		}
		return props;
	}

}
