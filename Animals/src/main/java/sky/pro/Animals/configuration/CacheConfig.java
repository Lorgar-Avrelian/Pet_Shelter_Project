package sky.pro.Animals.configuration;

import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Arrays;

@Configuration
@EnableScheduling
public class CacheConfig {
    @Bean
    @Scheduled(cron = "0 0 1 * * *")
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Arrays.asList(
                new ConcurrentMapCache("client"),
                new ConcurrentMapCache("info"),
                new ConcurrentMapCache("pet"),
                new ConcurrentMapCache("avatar"),
                new ConcurrentMapCache("volunteer"),
                new ConcurrentMapCache("variety")
        ));
        return cacheManager;
    }
}
