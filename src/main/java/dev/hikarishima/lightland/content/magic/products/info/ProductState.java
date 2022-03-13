package dev.hikarishima.lightland.content.magic.products.info;

public enum ProductState {
	LOCKED, UNLOCKED, CRAFTED;

	public int getIndex() {
		return ordinal();
	}

	public String toString() {
		return name().toLowerCase();
	}
}
