package jcrawler.jerold.crawl;

import java.io.Serializable;

public class CrawlUrl implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6983268930360042012L;

	public CrawlUrl() {

	}

	private String oriUrl; // 原始url

	private String url; // url地址

	public String getOriUrl() {
		return oriUrl;
	}

	public void setOriUrl(String oriUrl) {
		this.oriUrl = oriUrl;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
