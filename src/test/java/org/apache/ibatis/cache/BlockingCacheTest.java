package org.apache.ibatis.cache;

import org.apache.ibatis.cache.decorators.BlockingCache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author heqin
 */
public class BlockingCacheTest {

    @Test
    void shouldTestIdEqual(){
        BlockingCache blockingCache = new BlockingCache(new PerpetualCache("default"));
        assertEquals("default",blockingCache.getId());
    }
}
