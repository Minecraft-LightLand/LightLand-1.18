package dev.lcy0x1.util;

public class Lock {

	private boolean recursive = false;

	public void execute(Runnable runnable) {
		if (recursive) return;
		recursive = true;
		runnable.run();
		recursive = false;
	}

}
