import com.example.BookingSystem.enums.SlotStatus;
import com.example.BookingSystem.model.Slot;
import com.example.BookingSystem.repository.BookingRepository;
import com.example.BookingSystem.repository.SlotRepository;
import com.example.BookingSystem.service.BookingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class BookingConcurrencyTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Test
    void concurrentBookingTest() throws Exception {

        // 1. Create slot first
        Slot slot = new Slot();
        slot.setStartTime(LocalDateTime.now());
        slot.setEndTime(LocalDateTime.now().plusHours(1));
        slot.setStatus(SlotStatus.AVAILABLE);
        slot = slotRepository.save(slot);

        int threads = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        CountDownLatch latch = new CountDownLatch(threads);

        for (int i = 0; i < threads; i++) {
            final long userId = i + 1;

            Slot finalSlot = slot;
            executor.submit(() -> {
                try {
                    bookingService.bookSlot(finalSlot.getId(), userId);
                } catch (Exception ignored) {
                    // expected for losers
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        long count = bookingRepository.count();

        assertEquals(1, count, "Only one booking should be created");
    }
}
