package com.play.ground.HelloMaven;

import java.io.File;
import java.util.concurrent.Callable;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(description = "Test Project", name = "test", mixinStandardHelpOptions = true, version = "1.0")
public class App implements Callable<Void> {
	@Parameters(index = "0", description = "The file to process.")
	private File file;

	@Option(names = { "-a", "--algorithm" }, description = "Enable special algo.")
	private boolean algorithm = false;

	public Void call() throws Exception {
		System.out.println("File=" + this.file.toString());
		System.out.println("Algo=" + this.algorithm);

		return null;
	}

	public static void main(String[] args) {
		CommandLine.call(new App(), args);
	}
}
