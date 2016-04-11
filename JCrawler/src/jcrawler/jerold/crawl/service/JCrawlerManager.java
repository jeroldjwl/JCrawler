package jcrawler.jerold.crawl.service;

import java.util.Set;

import jcrawler.jerold.crawl.DataCenter;

public class JCrawlerManager {
	private static Set<String> unvisitedLinksPerThread;

	public static JCrawlerManager crawlerManager;

	private JCrawlerManager() {
	}

	public static JCrawlerManager getInstance() {
		if (crawlerManager == null)
			crawlerManager = new JCrawlerManager();
		return crawlerManager;
	}

	public synchronized Set<String> getUnVisitedLinksPerThread() {
		Set<String> set = null;
		while (!unvisitedLinksPerThread.isEmpty()) {
			set = unvisitedLinksPerThread;
			this.notifyAll();
		}
		return set;
	}

	public synchronized void addUnVisitedLinks(Set<String> set) {
		DataCenter.addLinks(set);
		try {
			this.wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
