package org.vuphone.assassins.android;

import java.util.PriorityQueue;

import org.vuphone.assassins.android.network.GameMessage;

public class MessageQueue {

	private static MessageQueue instance_ = null;
	
	private final PriorityQueue<GameMessage> queue_;
	
	private MessageQueue() {
		queue_ = new PriorityQueue<GameMessage>(10);
	}
	
	// TODO - This may not be thread safe!!!
	public static MessageQueue getInstance() {
		if (instance_ == null)
			instance_ = new MessageQueue();
		return instance_;
	}
	
	public GameMessage deque() {
		synchronized (queue_) {
			return queue_.poll();
		}
	}
	
	public void enqueue(GameMessage msg) {
		synchronized (queue_) {
			queue_.offer(msg);
		}
	}
	
	public boolean isEmpty() {
		synchronized (queue_) {
			return queue_.isEmpty();
		}
	}
}
