package jcrawler.jerold.crawl;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.conn.ConnectTimeoutException;

public class JCrawler {
	public static MyFrontier unvisitedFrontier;
	public static MyFrontier visitedFrontier;
	private static int num = 0;

	public JCrawler() {
		unvisitedFrontier = new MyFrontier();
		visitedFrontier = new MyFrontier();
	}

	private void initCrawlerWithSeeds(String[] seeds) {
		synchronized (this) {
			try {
				for (int i = 0; i < seeds.length; i++) {
					String url = seeds[i];
					unvisitedFrontier.putUrl(url);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void crawling(String[] seeds, int threadId) {
		try {
			LinkFilter filter = new LinkFilter() {
				@Override
				public boolean accept(String url) {
					// Pattern pattern = Pattern
					// .compile("^((https|http|ftp|rtsp|mms)?://)"
					// +
					// "+(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?"
					// + "(([0-9]{1,3}\\.){3}[0-9]{1,3}" + "|"
					// + "([0-9a-z_!~*'()-]+\\.)*"
					// + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\."
					// + "[a-z]{2,6})" + "(:[0-9]{1,4})?"
					// + "((/?)|"
					// + "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$");
					// Matcher matcher = pattern.matcher(url);
					// boolean isMatch = matcher.matches();
					// if (isMatch &&
					// url.startsWith(CrawlConfig.CRAWL_LIMIT_PATH)) {
					// return true;
					// } else {
					// return false;
					// }
					if (url.startsWith(CrawlConfig.CRAWL_LIMIT_PATH)
							&& !url.contains("+") && !url.contains("[")) {
						return true;
					} else {
						return false;
					}
				}
			};

			initCrawlerWithSeeds(seeds);
			String visitedUrl = unvisitedFrontier.getNext();
			do {
				System.out.println("线程：" + threadId);
				if (visitedUrl == null) {
					continue;
				}

				if (visitedFrontier.contains(visitedUrl)) { // 同步数据
					visitedUrl = unvisitedFrontier.getNext();
					continue;
				}

				visitedFrontier.putUrl(visitedUrl);

				if (null == visitedUrl || "".equals(visitedUrl.trim())) { // 抓取的地址为空
					visitedFrontier.putUrl(visitedUrl);
					visitedUrl = unvisitedFrontier.getNext();
					continue;
				}

				try {
					RetrievePage.downloadPage(visitedUrl); // 下载页面
					Set<String> links = HtmlParserTool.extractLinks(visitedUrl,
							filter);
					if (links != null) {
						for (String link : links) {
							if (!visitedFrontier.contains(link)
									&& !unvisitedFrontier.contains(link)) {
								unvisitedFrontier.putUrl(link);
							}
						}
					}
				} catch (ConnectTimeoutException e) { // 超时继续读下一个地址
					visitedFrontier.putUrl(visitedUrl);
					visitedUrl = unvisitedFrontier.getNext();
					num++;
					e.printStackTrace();
					continue;
				} catch (SocketTimeoutException e) {
					visitedFrontier.putUrl(visitedUrl);
					visitedUrl = unvisitedFrontier.getNext();
					num++;
					e.printStackTrace();
					continue;
				}
				visitedUrl = unvisitedFrontier.getNext();
				num++;

			} while (MyFrontier.threads > 0);
		}

		catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
