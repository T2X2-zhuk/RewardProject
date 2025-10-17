package rewardCalculation.util.forServices;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

@Component
@Slf4j
public class RewardExecutionLock {

    private final ReentrantLock lock = new ReentrantLock();

    public <T> T runWithLock(Supplier<T> action) {
        boolean acquired = lock.tryLock(); // попытка захвата
        if (!acquired) {
            log.warn("❌ RewardExecutionLock: операция уже выполняется, повторный запуск запрещён!");
            throw new IllegalStateException("Другая операция расчёта/очистки уже выполняется!");
        }
        try {
            log.debug("🔒 RewardExecutionLock: блокировка установлена");
            return action.get();
        } finally {
            lock.unlock();
            log.debug("🔓 RewardExecutionLock: блокировка снята");
        }
    }
}
