package jcrawler.jerold.crawl;

public class MyFrontier implements Frontier {

	public Queue<String> queue;

	public static int threads = CrawlConfig.CRAWL_THREAD_NUM;

	public MyFrontier() {
		queue = new Queue<String>();
	}

	@Override
	public synchronized String getNext() throws Exception {
		while (true) {
			if (!queue.isQueueEmpty()) {
				String url = queue.deQueue();
				return url;
			} else {
				threads--;
				if (threads > 0) {
					wait();
					threads++;
				} else {
					notifyAll();
					return null;
				}
			}
		}

	}

	@Override
	public synchronized boolean putUrl(String url) throws Exception {
		if (url != null && url != "" && !queue.contains(url)) {
			queue.enQueue(url);
			notifyAll();
			return true;
		}
		return false;
	}

	public synchronized boolean contains(String url) {
		return queue.contains(url);
	}

}
