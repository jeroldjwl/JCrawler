package jcrawler.jerold.crawl.service;

public class CrawlerFactory {
	public synchronized JCrawlerManager getInstance() {
		return JCrawlerManager.getInstance();
	}
}
