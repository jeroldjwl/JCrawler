package jcrawler.jerold.crawl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DataCenter {
	public List<Queue<String>> unVisitedLinks;
	private int threadNum;
	private static int DEFAULT_THREAD_NUM = 9;

	public DataCenter() {
		this.threadNum = DEFAULT_THREAD_NUM;
		unVisitedLinks = new ArrayList<Queue<String>>(threadNum);
	}

	public DataCenter(int threadNum) {
		this.threadNum = threadNum;
		unVisitedLinks = new ArrayList<Queue<String>>(threadNum);
		for (int i = 0; i < threadNum; i++) {
			unVisitedLinks.add(new Queue<String>());
		}
	}

	public void addLinks(Set<String> linkSet) {
		for (String url : linkSet) {
			int num = url.hashCode() % threadNum;
			Queue<String> queue = unVisitedLinks.get(num);
			queue.enQueue(url);
		}
	}

	public static void main(String[] args) {
		String s1 = "s1";
		String s2 = "s2";
		String s3 = "s3";
		String s4 = "s4";
		String s5 = "s5";
		String s6 = "s6";
		String s7 = "s7";
		String s8 = "s8";
		String s9 = "s9";
		String s10 = "s10";
		DataCenter dc = new DataCenter(9);
		Set<String> set = new HashSet<String>();
		set.add(s1);
		set.add(s2);
		set.add(s3);
		set.add(s4);
		set.add(s5);
		set.add(s6);
		set.add(s7);
		set.add(s8);
		set.add(s9);
		set.add(s10);
		dc.addLinks(set);
		List<Queue<String>> list = dc.unVisitedLinks;
		for (int i = 0; i < list.size(); i++) {
			Queue<String> queue = list.get(i);
			System.out.print("number in" + i + " contains: ");
			while (!queue.isQueueEmpty()) {
				String s = queue.deQueue();
				System.out.print(s + ",");
			}
			System.out.println();
		}
	}
}
