package ai.salesken.onboarding.app;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

public class KeySingleton {

	private static KeySingleton instance;
	private static Key apiKey = null;
	private static PublicKey publicKey = null;
	static {
		instance = new KeySingleton();
		String jksPassword = "test123";
		try {
			InputStream jskStream = KeySingleton.class.getClassLoader().getResourceAsStream("salesken.jks");

			KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
			ks.load(jskStream, jksPassword.toCharArray());
			apiKey = ks.getKey("hanbotest", jksPassword.toCharArray());
			publicKey = loadPublicKey();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Keys.secretKeyFor(SignatureAlgorithm.HS256);
	}

	public KeySingleton() {
		super();
	}

	public static KeySingleton getInstance() {
		return instance;
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

	public Key getKey() {
		return apiKey;
	}

	public static PublicKey loadPublicKey() throws Exception {
		// DBProperties.getProperty("CER_FILE_PATH")
		InputStream publicKeyStream = KeySingleton.class.getClassLoader().getResourceAsStream("salesken.cer");
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		Certificate cert = cf.generateCertificate(publicKeyStream);
		PublicKey retVal = cert.getPublicKey();
		return retVal;
	}

}