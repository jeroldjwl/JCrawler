package jcrawler.jerold.crawl;

public abstract class AbstractFrontier {
	protected abstract void put(Object key, Object value);

	protected abstract Object get(Object key);

	protected abstract Object delete(Object key);
}
