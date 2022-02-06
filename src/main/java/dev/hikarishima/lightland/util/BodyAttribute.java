package dev.hikarishima.lightland.util;

import java.util.Random;
import java.util.UUID;

public class BodyAttribute {

    public static UUID getUUIDfromString(String str) {
        int hash = str.hashCode();
        Random r = new Random(hash);
        long l0 = r.nextLong();
        long l1 = r.nextLong();
        return new UUID(l0, l1);
    }

}
