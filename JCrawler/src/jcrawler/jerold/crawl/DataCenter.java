package jcrawler.jerold.crawl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DataCenter {
	public static List<Queue<String>> unVisitedLinks;
	public static Set<String> visitedLinks;
	private static int threadNum;
	private static int DEFAULT_THREAD_NUM = 9;

	public DataCenter() {
		threadNum = DEFAULT_THREAD_NUM;
		new DataCenter(DEFAULT_THREAD_NUM);
	}

	public DataCenter(int tNum) {
		threadNum = tNum;
		unVisitedLinks = new ArrayList<Queue<String>>(threadNum);
		for (int i = 0; i < threadNum; i++) {
			unVisitedLinks.add(new Queue<String>());
		}
		visitedLinks = new HashSet<String>();
	}

	public static void addLinks(Set<String> linkSet) {
		for (String url : linkSet) {
			int num = url.hashCode() % threadNum;
			Queue<String> queue = unVisitedLinks.get(num);
			if (!queue.contains(url) && !visitedLinks.contains(url)) {
				queue.enQueue(url);
			}
		}
	}

	public static void main(String[] args) {
		Set<String> set = new HashSet<String>();
		System.err.println(set.contains("s"));
	}
}
