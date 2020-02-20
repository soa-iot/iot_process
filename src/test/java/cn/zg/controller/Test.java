package cn.zg.controller;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Lock;

public class Test {
	private AtomicInteger a ;
	
	private Lock l;
	
	private AbstractQueuedSynchronizer aqs;
	

	   private Semaphore semaphore;
}
