/*
package ru.mpei.relayprotection;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Async;
import ru.mpei.relayprotection.model.protection.phaseHandling.ChronometricPhaseAnalyzer;
import ru.mpei.relayprotection.model.protection.phaseHandling.DifferentialPhaseAnalyzer;
import ru.mpei.relayprotection.model.protection.phaseHandling.PhaseAnalyzer;
import ru.mpei.relayprotection.model.protection.signalHandling.SignalHandler;
import ru.mpei.relayprotection.model.protection.signalHandling.StairActionManager;
import ru.mpei.relayprotection.model.protection.signalHandling.differential.DifferentialSignalHandler;
import ru.mpei.relayprotection.model.sv.SvReceiver;
import ru.mpei.relayprotection.model.sv.ValueHolder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
class RelayProtectionApplicationTests {
	@Test
	@SneakyThrows
	void testMultiThreadSignalHandling() {
		SvReceiver svReceiver = new SvReceiver(
				"Realtek USB FE Family Controller #2",
				"01:0c:cd:04:00:01",
				"01:0c:cd:04:00:02",
				true,
				5000);
		PhaseAnalyzer pa = new ChronometricPhaseAnalyzer(5, new StairActionManager(), svReceiver);
		ValueHolder value =  new ValueHolder();
		TestHandler t1 = new TestHandler(value, pa, svReceiver);
		TestHandler t2 = new TestHandler(value, pa, svReceiver);
		t1.getTask().setName("task 1");
		t2.getTask().setName("task 2");
		t1.startHandlingTask();
		t2.startHandlingTask();

		Thread.sleep(100);

		for (int i = 0; i < 5; i++) {
			value.set(i);
			for (int j = 0; j < 5; j++) {
				System.err.println("main thread msg");
				Thread.sleep(1000);
			}
			Thread.sleep(3000);
			System.out.println();
		}
	}

	@SneakyThrows
	@Test
	void testAsync() {

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
	*/
/**
	 * Метод требует создания новых экземпляров Executors.newSingleThreadExecutor() и каждый раз открывает новый поток
	 *//*

	void testInterruptAndStart() {
		Runnable task = new MyRunnable();
		ExecutorService executor;
		for (int i = 0; i < 4; i++) {
			executor = Executors.newSingleThreadExecutor();
			executor.execute(task);
			Thread.sleep(2_500);
			executor.shutdownNow();
			Thread.sleep(2_000);
		}
	}

//	@Test
//	void tryToRunChain() {
//		StairActionManager stairActionManager = new StairActionManager();
//		DifferentialPhaseAnalyzer phaseAnalyzer = new DifferentialPhaseAnalyzer(10, stairActionManager);
//
//		ValueHolder vh1 = new ValueHolder();
//		DifferentialSignalHandler signalHandler1 = new DifferentialSignalHandler(vh1, phaseAnalyzer, 80, 50);
//		ValueHolder vh2 = new ValueHolder();
//		DifferentialSignalHandler signalHandler2 = new DifferentialSignalHandler(vh2, phaseAnalyzer, 80, 50);
//	}
	private class TestHandler extends SignalHandler {
		public String msg;

	public TestHandler(ValueHolder value, PhaseAnalyzer phaseAnalyzer, Object locker) {
		super(value, phaseAnalyzer, locker);
	}

//		public TestHandler(ValueHolder value, String content, SvReceiver ) {
//			super(value, new ChronometricPhaseAnalyzer(0, new StairActionManager()));
//			this.msg = content;
//		}

		@Override
		@SneakyThrows
		public void handle() {
			for (int i = 0; i < 5; i++) {
				System.out.println(msg + ": " +value.get());
				Thread.sleep(1_000);
			}
		}
	}

	@Slf4j
	private static class MyRunnable implements Runnable{
		@Override

		public void run() {
			for (int j = 0; j < 5; j++) {
				System.out.println("message from task");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					log.error("thread interrupted");
					return;
				}
			}
			log.info("task complete");
		}
	}
}
*/
