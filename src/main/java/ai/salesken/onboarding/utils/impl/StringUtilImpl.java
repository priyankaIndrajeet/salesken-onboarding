package ai.salesken.onboarding.utils.impl;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;

import org.apache.commons.text.WordUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities.EscapeMode;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

public class StringUtilImpl {
	public static String getMd5(String input) {
		try {

			// Static getInstance method is called with hashing MD5
			MessageDigest md = MessageDigest.getInstance("MD5");

			// digest() method is called to calculate message digest
			// of an input digest() return array of byte
			byte[] messageDigest = md.digest(input.getBytes());

			// Convert byte array into signum representation
			BigInteger no = new BigInteger(1, messageDigest);

			// Convert message digest into hex value
			String hashtext = no.toString(16);
			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}
			return hashtext;
		}

		// For specifying wrong message digest algorithms
		catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public static String getStringSizeLengthFile(long size) {

		DecimalFormat df = new DecimalFormat("0.00");

		float sizeKb = 1024.0f;
		float sizeMb = sizeKb * sizeKb;
		float sizeGb = sizeMb * sizeKb;
		float sizeTerra = sizeGb * sizeKb;

		if (size < sizeMb)
			return df.format(size / sizeKb) + " Kb";
		else if (size < sizeGb)
			return df.format(size / sizeMb) + " Mb";
		else if (size < sizeTerra)
			return df.format(size / sizeGb) + " Gb";

		return "";
	}

	private static char[] c = new char[] { 'k', 'm', 'b', 't' };

	/**
	 * Recursive implementation, invokes itself for each factor of a thousand,
	 * increasing the class on each invokation.
	 * 
	 * @param n         the number to format
	 * @param iteration in fact this is the class from the array c
	 * @return a String representing the number n formatted in a cool looking way.
	 */
	public static String getStringFromValue(double n, int iteration) {

		double d = ((long) n / 100) / 10.0;
		boolean isRound = (d * 10) % 10 == 0;// true if the decimal part is equal to 0 (then it's trimmed anyway)
		return (d < 1000 ? // this determines the class, i.e. 'k', 'm' etc
				((d > 99.9 || isRound || (!isRound && d > 9.99) ? // this decides whether to trim the decimals
						(int) d * 10 / 10 : d + "" // (int) d * 10 / 10 drops the decimal
				) + "" + c[iteration]) : getStringFromValue(d, iteration + 1));

	}

	public static String stringCapitalize(String str) {
		return org.apache.commons.lang3.StringUtils.capitalize(str);
	}

	public static String wordsCapitalize(String str) {
		return WordUtils.capitalize(str);
	}

	public static String cleanHTML(String html) {
		if (html != null) {
			try {
				Document doc = new Cleaner(Whitelist.simpleText())
						.clean(Jsoup.parse(html.replaceAll("<!--.*?", "").replaceAll(".*?-->", "")));
				doc.outputSettings().escapeMode(EscapeMode.xhtml);
				html = doc.text().replaceAll("'", "");
				return html;
			} catch (Exception e) {
				return html;
			}
		} else {
			return html;
		}

	}
}
