package rewardCalculation.lock;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
@Slf4j
public class RewardExecutionLock {

    private final RedissonClient redissonClient;
    private final AppLockProperties appLockProperties;

    public <T> T runWithLock(String lockName, Supplier<T> action) {
        String lockKey = appLockProperties.getLocks().get(lockName);
        if (lockKey == null) {
            throw new IllegalArgumentException("Unknown lock name: " + lockName);
        }

        RLock lock = redissonClient.getLock(lockKey);
        boolean acquired = false;

        try {
            acquired = lock.tryLock(0, 5, TimeUnit.MINUTES);
            if (!acquired) {
                log.warn("❌Lock {} уже выполняется!", lockKey);
                throw new IllegalStateException("Операция уже выполняется: " + lockKey);
            }

            log.info("🔒 Lock {} установлен Redis", lockKey);
            return action.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Ошибка при захвате Redis-блокировки: " + lockKey, e);
        } finally {
            if (acquired && lock.isHeldByCurrentThread()) {
                lock.unlock();
                log.info("🔓 Lock {} снят", lockKey);
            }
        }
    }
}
