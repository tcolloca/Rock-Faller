package model.utils;

import java.io.PrintWriter;

public class TestWriter {

	private PrintWriter writer;
	
	public TestWriter(String name) {
		try {
			writer = new PrintWriter(name, "UTF-8");
		} catch (Exception e) {
			throw new IllegalMakeFileException();
		}
	}
	
	public void write(int size, int colors, int depth, long timeAvg) { //TODO: REMOVE GREEN FOR GAMEOVER.
		writer.println(size + "," + colors + "," + depth + "," + timeAvg);
	}
	
	public void close() {
		writer.close();
	}
}
