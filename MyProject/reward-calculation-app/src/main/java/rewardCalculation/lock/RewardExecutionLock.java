package rewardCalculation.lock;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import rewardCalculation.util.forErrors.Exceptions.LockInterruptedException;
import rewardCalculation.util.forErrors.Exceptions.OperationInProgressException;

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
                log.warn("❌ Lock {} уже выполняется!", lockKey);
                throw new OperationInProgressException(lockKey);
            }

            log.info("🔒 Lock {} установлен Redis", lockKey);
            return action.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // сохраняем флаг прерывания
            throw new LockInterruptedException("⏳ Поток прерван при захвате lock: " + lockKey, e);
        } finally {
            if (acquired && lock.isHeldByCurrentThread()) {
                try {
                    lock.unlock();
                    log.info("🔓 Lock {} снят", lockKey);
                } catch (Exception e) {
                    log.error("⚠️ Не удалось снять lock {}: {}", lockKey, e.getMessage());
                }
            }
        }
    }
}
