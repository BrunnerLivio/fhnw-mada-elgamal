package src.ch.fhnw.mada;

import java.math.BigInteger;
import java.util.Random;

public class ElgamalGenerator {

  public static void main(String[] args) {
    // Aufgabe 1
    String hex = """
        FFFFFFFF FFFFFFFF C90FDAA2 2168C234 C4C6628B 80DC1CD1
        29024E08 8A67CC74 020BBEA6 3B139B22 514A0879 8E3404DD
        EF9519B3 CD3A431B 302B0A6D F25F1437 4FE1356D 6D51C245
        E485B576 625E7EC6 F44C42E9 A637ED6B 0BFF5CB6 F406B7ED
        EE386BFB 5A899FA5 AE9F2411 7C4B1FE6 49286651 ECE45B3D
        C2007CB8 A163BF05 98DA4836 1C55D39A 69163FA8 FD24CF5F
        83655D23 DCA3AD96 1C62F356 208552BB 9ED52907 7096966D
        670C354E 4ABC9804 F1746C08 CA18217C 32905E46 2E36CE3B
        E39E772C 180E8603 9B2783A2 EC07A28F B5C55DF0 6F4C52C9
        DE2BCBF6 95581718 3995497C EA956AE5 15D22618 98FA0510
        15728E5A 8AACAA68 FFFFFFFF FFFFFFFF""";

    // Convert hex to BigInteger
    BigInteger n = new BigInteger(hex.replaceAll("\\s", ""), 16);
    // Producer
    BigInteger g = BigInteger.valueOf(2);

    // Aufgabe 2
    BigInteger[] keys = generateKeys(n, g);
    FileHelper.writeFile("generated/pk.txt", keys[0].toString());
    FileHelper.writeFile("generated/sk.txt", keys[1].toString());

    // Aufgabe 3
    String text = FileHelper.readFile("text.txt");
    BigInteger publicKey = new BigInteger(FileHelper.readFile("generated/pk.txt"));
    BigInteger privateKey = new BigInteger(FileHelper.readFile("generated/sk.txt"));

    BigInteger[] encryptedMessage = encrypt(n, g, publicKey, text);
    FileHelper.writeEncryptedMessageToFile("generated/chiffre.txt", encryptedMessage);
  }

  private static BigInteger[] generateKeys(BigInteger n, BigInteger g) {

    // Generate private key in range [0, n-1]
    BigInteger maxRange = n.subtract(BigInteger.valueOf(1));
    BigInteger privateKey;
    do {
      privateKey = new BigInteger(maxRange.bitLength(), new Random());
    } while (privateKey.compareTo(maxRange) >= 0);

    // g^b mod n
    BigInteger publicKey = g.modPow(privateKey, n);
    return new BigInteger[] { publicKey, privateKey };
  }

  private static BigInteger[] encrypt(BigInteger n, BigInteger g, BigInteger publicKey, String message) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  private static String decrypt(BigInteger n, BigInteger g, BigInteger privateKey, BigInteger[] encryptedMessage) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

}