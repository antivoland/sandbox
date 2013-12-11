/**
 * @author antivoland
 */
package ru.antivoland.yatest;

import org.apache.commons.codec.digest.DigestUtils;

public class Main {
    public static void main(String[] args) throws Exception {
        Digest digest = new Digest() {
            @Override
            protected byte[] doDigest(byte[] input) {
                return DigestUtils.md5(input);
            }
        };

        String str = "Hello, World!";
        digest.digest(str.getBytes());
        digest.digest(str.getBytes());
    }
}
