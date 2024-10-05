package ru.mpei.relayprotection;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.Map;

@SpringBootTest
class RelayProtectionApplicationTests {



	@Test
	void contextLoads() {
	}

	@SneakyThrows
	@Test
	void testAsync3() {
//		for (int i = 0; i < 5; i++) {
//			tc.print();
//			for (int j = 0; j < 5; j++) {
//				System.err.println("main thread");
//				Thread.sleep(1_000);
//			}
//			System.out.println();
//
//			Thread.sleep(3_000);
//		}

		Object lock = new Object();
		new Thread(() -> {
				synchronized (lock) {
					while (true) {
						try {
							lock.wait();
						} catch (InterruptedException e) {
							throw new RuntimeException(e);
						}
						for (int i = 0; i < 5; i++) {
							System.out.println("callable thread");
							try {
								Thread.sleep(1_000);
							} catch (InterruptedException e) {
								throw new RuntimeException(e);
							}
						}
						try {
							Thread.sleep(1_000);
						} catch (InterruptedException e) {
							throw new RuntimeException(e);
						}
						System.out.println();
					}
				}
		}).start();
		Thread.sleep(500);
		for (int i = 0; i < 5; i++) {
			synchronized (lock) {
				lock.notify();
			}
			for (int j = 0; j < 5; j++) {
				System.err.println("main thread");
				Thread.sleep(1_000);
			}
			System.out.println();

			Thread.sleep(3_000);
		}

	}

	@Test
	@SneakyThrows
	void testAsynchronousCall2() {
		Handler h = new Handler();
		Rec r = new Rec(
				Map.of("s",

				new Thread(new Runnable() {
					@Override
					@SneakyThrows
//					@Async
					public void run() {
							for (int i = 0; i < 5; i++) {
								System.out.println("callable thread");
								Thread.sleep(1_000);
							}
							System.out.println();
//						h.print();
//						Thread.currentThread().stop();
					}
				}))
		);

		for (int i = 0; i < 5; i++) {
			r.handler.get("s").run();
//			h.print();
			for (int j = 0; j < 5; j++) {
				System.err.println("main thread");
				Thread.sleep(1_000);
			}
			System.out.println();

			try {
				Thread.sleep(3_000);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private record Rec(Map<String, Thread> handler){}

	@EnableAsync
	private class Handler {
//		@Async
		@SneakyThrows
		public void print() {
			for (int i = 0; i < 5; i++) {
				System.out.println("message");
				Thread.sleep(1_000);
			}
			System.out.println();
		}
	}

	@Test
	void testAsynchronousCall() {
//		Object locker = new Object();
//		try {
//			locker.wait();
//		} catch (InterruptedException e) {
//			throw new RuntimeException(e);
//		}

		Object locker = new Object();
		locker.notify();

		Thread thread = new Thread(() -> {

//			try {
//				locker.wait();
//			} catch (InterruptedException e) {
//				System.out.println(" wait");;
//			}

			while (true) {
				try {
					locker.wait();
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
				for (int j = 0; j < 5; j++) {
					System.err.println("message");
				}
				System.out.println();
			}

			// Вызываем метод асинхронно

		});
		thread.setDaemon(true);
		thread.start();
		int i = 0;
		while (i < 10) {
			locker.notify();
//			try {
//				locker.wait();
//			} catch (InterruptedException e) {
//				throw new RuntimeException(e);
//			}

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}

		}

	}

}
