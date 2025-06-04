package com.wildsim.service;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CSVLogService {
	private static CSVLogService instance;
	private final String csvFilePath;
	private final SimpleDateFormat dateFormat;

	private CSVLogService() {
		this.dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		this.csvFilePath = "crud_and_simulation_log.csv";
		initializeCSVFile();
	}

	public static CSVLogService getInstance() {
		if (instance == null) {
			instance = new CSVLogService();
		}
		return instance;
	}

	private void initializeCSVFile() {
		Path path = Paths.get(csvFilePath);
		if (!Files.exists(path)) {
			try (FileWriter writer = new FileWriter(csvFilePath)) {
				writer.append("action, timestamp\n");
			} catch (IOException e) {
				System.err.println("Error initializing CSV file: " + e.getMessage());
			}
		}
	}

	public void logAction(String action) {
		try (FileWriter writer = new FileWriter(csvFilePath, true)) {
			String timestamp = dateFormat.format(new Date());
			// escape action by enclosing it in double quotes
			writer.append("\"").append(action).append("\"").append(",").append(timestamp).append("\n");
		} catch (IOException e) {
			System.err.println("Error writing to CSV file: " + e.getMessage());
		}
	}
}
