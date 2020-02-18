package services.utils.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import services.utils.URLUtils;

public class URLUtilsImpl implements URLUtils {
	public final Logger logger = LogManager.getLogger(URLUtilsImpl.class);

	@Override
	public String sendGet(String url) throws Exception {
		logger.trace("Sending get request to :" + url);
		long start = System.currentTimeMillis();
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("Content-Encoding", "gzip");
		con.setConnectTimeout(100000000);
		int responseCode = con.getResponseCode();
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		logger.trace("Sent post request to :" + url + " and received response code: " + responseCode + " in: "
				+ (System.currentTimeMillis() - start) + "ms -->" + response.toString());
		return response.toString();

	}
	
	@Override
	public String sendPostForm(String url, ArrayList<String[]> formData) {
		try {
			long start = System.currentTimeMillis();
			String boundary = Long.toHexString(System.currentTimeMillis());
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
			con.setRequestProperty("Content-Encoding", "gzip");
			con.setConnectTimeout(100000000);
			PrintWriter payload = new PrintWriter(new OutputStreamWriter(con.getOutputStream(), "UTF-8"));
			for (String[] formDataEntry : formData) {
				payload.println("--" + boundary);
				payload.println("Content-Disposition: form-data; name=\"" + formDataEntry[0] + "\"");
				payload.println("Content-Type: text/plain; charset=UTF-8");
				payload.println();
				payload.println(formDataEntry[1]);
			}
			payload.println("--" + boundary + "--");
			if (payload != null)
				payload.close();
			int responseCode = con.getResponseCode();

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			logger.trace("Sent post request to :" + url + " and received response code: " + responseCode + " in: "
					+ (System.currentTimeMillis() - start) + "ms -->" + response.toString());
			return response.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public   String sendGetZoho(String url, String token) throws Exception {
		// logger.trace("Sending get request to :" + url);
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestProperty("Authorization", "Zoho-oauthtoken " + token);

		con.setRequestMethod("GET");
		// con.setRequestProperty("User-Agent", USER_AGENT);
		con.setConnectTimeout(100000000);
		int responseCode = con.getResponseCode();
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		System.out.println("response " + response);
		return response.toString();

	}


}