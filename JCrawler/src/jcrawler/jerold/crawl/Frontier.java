package jcrawler.jerold.crawl;

public interface Frontier {
	public String getNext() throws Exception;

	public boolean putUrl(String url) throws Exception;
}
