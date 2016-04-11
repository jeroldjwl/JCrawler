package jcrawler.jerold.crawl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class RetrievePage {
	private static String USER_AGENT = "Mozilla/4.0 (compatible; MSIE 6.0;Windows NT 5.1; SV1; QQDownload 1.7; .NET CLR 1.1.4322; CIBA; .NET CLR 2.0.50727";
	private static String DEFAULT_CHARSET = "GB2312,utf-8;q=0.7,*;q=0.7";
	private static String ACCEPT = "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8";

	/**
	 * 下载文件
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 * @throws IOException
	 */
	public static boolean downloadPage(String path) throws Exception,
			IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(path);

		httpget.addHeader("Accept-Charset", DEFAULT_CHARSET);
		// httpget.addHeader("Host", host);
		// httpget.addHeader("Accept", ACCEPT);
		// httpget.addHeader("User-Agent", USER_AGENT);

		RequestConfig requestConfig = RequestConfig.custom() // 设置超时
				.setSocketTimeout(1000).setConnectTimeout(1000).build();
		httpget.setConfig(requestConfig);
		CloseableHttpResponse response = httpclient.execute(httpget);
		try {
			HttpEntity entity = response.getEntity();
			StatusLine statusLine = response.getStatusLine();

			if (statusLine.getStatusCode() == HttpStatus.SC_MOVED_PERMANENTLY
					|| // 如果是转移
					statusLine.getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY
					|| statusLine.getStatusCode() == HttpStatus.SC_SEE_OTHER
					|| statusLine.getStatusCode() == HttpStatus.SC_TEMPORARY_REDIRECT) {
				Header header = httpget.getFirstHeader("location");
				if (header != null) {
					String newUrl = header.getValue();
					if (newUrl == null || newUrl.equals("")) {
						newUrl = "/";
						HttpGet redirect = new HttpGet(newUrl);
					}
				}
			}
			if (statusLine.getStatusCode() == HttpStatus.SC_OK) { // 成功访问
				if (entity == null) {
					// throw new ClientProtocolException(
					// "Response contains no content");
				} else {
					String filename = getFilenameByUrl(path);
					if (filename == null || "".equals(filename))
						return false;
					String type = entity.getContentType().getValue();
					if (type.contains("word") || type.contains("pdf")) {
						saveAttachMents(filename);
					}
					// OutputStream outstream = new FileOutputStream(new File(
					// CrawlConfig.CRAWL_DOWNLOAD_PATH)); // 存储到磁盘
					// try {
					// // System.out.println(convertStreamToString(instream));
					// int tempByte = -1;
					// while ((tempByte = instream.read()) > 0) {
					// outstream.write(tempByte);
					// }
					// return true;
					// } catch (Exception e) {
					// e.printStackTrace();
					// return false;
					// } finally {
					// if (instream != null) {
					// instream.close();
					// }
					// if (outstream != null) {
					// outstream.close();
					// }
					// }

				}
			}
			if (statusLine.getStatusCode() == HttpStatus.SC_INTERNAL_SERVER_ERROR
					|| statusLine.getStatusCode() == HttpStatus.SC_NOT_FOUND) {

			}
			return false;
		} finally {
			response.close();
		}
	}

	public static void saveAttachMents(String fileName) {
		BufferedWriter writer = null;
		String path = CrawlConfig.CRAWL_DOWNLOAD_PATH + fileName + ".txt";
		try {
			writer = new BufferedWriter(new FileWriter(new File(path)));
			writer.write(fileName + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static String getFilenameByUrl(String url) {
		// url = url.substring(7);
		// if (contentType.indexOf("html") != -1) {
		// url = url.replaceAll("[\\?/:*|<>\"]", "_") + ".html";
		// return url;
		// } else {
		// url = url.replaceAll("[\\?/:*|<>\"]", "_")
		// + contentType.substring(contentType.lastIndexOf('/') + 1);
		// return url;
		// }
		int beginIndex = url.indexOf(CrawlConfig.CRAWL_LIMIT_PATH);
		beginIndex += 23;
		url = url.substring(beginIndex);
		if (url.contains("?")) {
			url = url.replaceAll("\\?", "_");
		}
		if (url.contains("/")) {
			url = url.replaceAll("\\/", "_");
		}
		return url;
	}

	/**
	 * 转换流数据为字符串
	 * 
	 * @param is
	 * @return
	 */
	public static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "/n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		String url = "http://www.shyp.gov.cn/epoint/UploadFile/chenling/file/杨新江[2013]2附件一(1).doc";
		System.out.println(url.charAt(58));

	}
}
