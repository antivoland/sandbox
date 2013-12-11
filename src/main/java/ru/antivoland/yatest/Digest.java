/**
 * @author antivoland
 */
package ru.antivoland.yatest;

import java.util.HashMap;
import java.util.Map;

/**
 * http://en.wikipedia.org/wiki/Digest_access_authentication
 *
 * Во-первых byte[] сравнивается по ссылке, так нужно хотя бы обернуть строкой.
 * Во вторых получим дедлок в первой строке метода digest.
 */
public abstract class Digest {
    private Map<byte[], byte[]> cache = new HashMap<byte[], byte[]>();

    public byte[] digest(byte[] input) {
        byte[] result = cache.get(input);
        if (result == null) {
            synchronized (cache) {
                result = cache.get(input);
                if (result == null) {
                    result = doDigest(input);
                    cache.put(input, result);
                }
            }
        }
        return result;
    }

    protected abstract byte[] doDigest(byte[] input);
}
