package util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Crypto {

	private static byte[] key = "TWBIU=MXTl:oOXqQ=OcvdlM6/0fgVf0=ybm^:psJv<dGh3mDjKL>a5RbujprDPTo".getBytes();

	public static Crypto get() {
		return new Crypto();
	}

	public static byte[] sign(String message) {
		Mac mac;
		try {
			mac = Mac.getInstance("HmacSHA1");
			mac.init(new SecretKeySpec(key, "HmacSHA1"));
			return mac.doFinal(message.getBytes("utf-8"));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void main(String[] args) {
		System.out.println(new String(Crypto.get().sign("Hallo Test")));
	}
}
