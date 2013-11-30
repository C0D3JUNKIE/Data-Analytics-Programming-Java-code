import java.lang.StringBuffer;
import java.util.Iterator;
import java.util.Comparator;
import java.util.Queue;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

import static java.lang.Math.max;

import java.util.TreeSet;

import com.google.common.collect.Multiset.Entry;
import com.google.common.collect.TreeMultiset;
import com.google.common.collect.Multiset;

interface LogProcessor {
	public void add(String url, int time);

	public List<String> getOrderedUrlsInWindow(int K);
}

class LogItem {
	private String url;
	private int time;

	public LogItem(String url, int time) {
		this.url = url;
		this.time = time;
	}

	public String getUrl() {
		return url;
	}

	public int getTime() {
		return time;
	}

	public int compareTo(LogItem li) {
		return time - li.getTime();
	}

	@Override
	public String toString() {
		return url + ", time = " + time;
	}
}

class LogProcessorSlow implements LogProcessor {
	private int W;
	private List<LogItem> liQueue;

	public LogProcessorSlow(int W) {
		this.W = W;
		this.liQueue = new LinkedList<LogItem>();
	}

	@Override
	public String toString() {
		StringBuffer result = new StringBuffer("window size = " + W + "\n");
		for (LogItem li : liQueue) {
			result.append(li.toString() + "\n");
		}
		return result.toString();
	}

	public void add(String url, int time) {
		// System.out.println("before add:" + this.toString() );
		LogItem anItem = new LogItem(url, time);
		liQueue.add(anItem);

		// Calculate the time right now
		int mostRecentTime = -1;
		for (LogItem li : liQueue) {
			if (li.getTime() > mostRecentTime) {
				mostRecentTime = li.getTime();
			}
		}

		// Delete the items that are not within time W
		Iterator<LogItem> liIter = liQueue.iterator();
		while (liIter.hasNext()) {
			if (mostRecentTime - liIter.next().getTime() > W) {
				liIter.remove();
			}
		}
		// System.out.println("after add:" + this.toString() );
	}

	public List<String> getOrderedUrlsInWindow(int K) {
		final Map<String, Integer> urlCountMap = new HashMap<String, Integer>();

		// If url exist, then count + 1, if not, build a new key
		for (LogItem li : liQueue) {
			if (urlCountMap.containsKey(li.getUrl())) {
				int count = urlCountMap.get(li.getUrl());
				urlCountMap.put(li.getUrl(), count + 1);
			} else {
				urlCountMap.put(li.getUrl(), 1);
			}
		}

		// Use arraylist and collection sort to sort the map
		List<String> countArray = new ArrayList<String>(urlCountMap.keySet());
		Collections.sort(countArray, new Comparator<String>() {
			@Override
			public int compare(String s0, String s1) {
				int tmp = urlCountMap.get(s0) - urlCountMap.get(s1);
				if (tmp != 0) {
					return tmp;
				} else {
					// want lexicographically first strings to appear first when
					// tied
					return -s0.compareTo(s1);
				}
			}
		});

		// record the url and its count
		List<String> result = new ArrayList<String>();
		for (int i = countArray.size() - 1; i >= max(countArray.size() - K, 0); i--) {
			result.add(countArray.get(i) + ":"
					+ urlCountMap.get(countArray.get(i)));
		}
		return result;
	}

}

class PageTime implements Comparable {
	String url;
	int time;

	public int compareTo(Object o) {
		PageTime pt = (PageTime) o;
		int diff = time - pt.time;
		if (diff != 0) {
			return diff;
		} else {
			return url.compareTo(pt.url);
		}
	}

	@Override
	public String toString() {
		return url + ":" + time;
	}

	@Override
	public boolean equals(Object o) {
		PageTime obj = (PageTime) o;
		return obj.url == url && obj.time == time;
	}

	public PageTime(String url, int time) {
		this.url = url;
		this.time = time;
	}

	public int getTime() {
		return time;
	}

	public String getUrl() {
		return url;
	}
}

class PageCount implements Comparable {
	String url;
	int count;

	public int compareTo(Object o) {
		PageCount pc = (PageCount) o;
		// sort by descending counts
		int diff = -(count - pc.count);
		if (diff != 0) {
			return diff;
		} else {
			return url.compareTo(pc.url);
		}
	}
	
	public String toString() {
		return url + ":" + count;
	}

	@Override
	public boolean equals(Object o) {
		PageCount pc = (PageCount) o;
		return pc.url == url && pc.count == count;
	}

	public PageCount(String url, int count) {
		this.url = url;
		this.count = count;
	}

	public String getUrl() {
		return url;
	}

	public int getCount() {
		return count;
	}
}

class LogProcessorFast implements LogProcessor {
	TreeMultiset<PageTime> queue;
	TreeSet<PageCount> counts;
	HashMap<String, PageCount> urlToCount;
	int W;

	public LogProcessorFast(int W) {
		queue = TreeMultiset.create();
		counts = new TreeSet<PageCount>();
		urlToCount = new HashMap<String, PageCount>();
		this.W = W;
	}

	public void add(String url, int time) {

		queue.add(new PageTime(url, time));

		int lTime = queue.lastEntry().getElement().getTime();

		while (lTime - queue.firstEntry().getElement().getTime() > W) {
			queue.pollFirstEntry();
		}
	}

	public List<String> getOrderedUrlsInWindow(int K) {
		
		urlToCount = new HashMap<String, PageCount>();
		
		for (Entry<PageTime> pt : queue.entrySet()) {
			String url1 = pt.getElement().getUrl();
			if (urlToCount.containsKey(url1)) {
				urlToCount.get(url1).count += pt.getCount();
			} else {
				urlToCount.put(url1, new PageCount(url1, pt.getCount()));
			}
		}
		
		counts = new TreeSet<PageCount>();
		
		for (PageCount value : urlToCount.values()) {
			counts.add(value);
		}
		
		List<String> result = new ArrayList<String>(K);
		for (int i = counts.size() - 1; i >= max(counts.size() - K, 0 ); i-- ) {
			result.add(counts.pollFirst().toString());
		}
		
		return result;

	}
}
