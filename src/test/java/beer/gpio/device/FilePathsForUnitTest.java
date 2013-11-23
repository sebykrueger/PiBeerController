package beer.gpio.device;

import java.io.File;

import beer.gpio.util.FilePaths;

public class FilePathsForUnitTest extends FilePaths {
	
	private File baseDir;
	
	public FilePathsForUnitTest(File baseDir) {
		super();
		this.baseDir = baseDir;
	}
	
	@Override
	public String getGpioPath() {
		return baseDir.getAbsolutePath() + "/gpio";
	}
	
	@Override
	public String getBusPath() {
		return baseDir.getAbsolutePath() + "/bus";
	}
}