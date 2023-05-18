package src.ch.fhnw.mada;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

public class ElgamalGenerator {

  public static void main(String[] args) {
    // Prepare
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

    // Our keys
    BigInteger[] keys = generateKeys(n, g);
    FileHelper.writeFile("generated/pk.txt", keys[0].toString());
    FileHelper.writeFile("generated/sk.txt", keys[1].toString());

    String text = FileHelper.readFile("text.txt");
    BigInteger ourPublicKey = new BigInteger(FileHelper.readFile("generated/pk.txt"));
    BigInteger ourPrivateKey = new BigInteger(FileHelper.readFile("generated/sk.txt"));

    String ourEncryptedMessage = encrypt(n, g, ourPublicKey, text);
    FileHelper.writeFile("generated/chiffre.txt", ourEncryptedMessage);

    String ourDecryptedMessage = decrypt(n, g, ourPrivateKey, ourEncryptedMessage);
    System.out.println(ourDecryptedMessage);

    // Their keys
    BigInteger theirPrivateKey = new BigInteger(FileHelper.readFile("sk.txt"));

    String theirEncryptedMessage = FileHelper.readFile("chiffre.txt");
    String theirDecryptedMessage = decrypt(n, g, theirPrivateKey, theirEncryptedMessage.toString());

    System.out.println(theirDecryptedMessage);
  }

  private static BigInteger[] generateKeys(BigInteger n, BigInteger g) {
    BigInteger privateKey = getRandomBigInteger(n);

    // g^b mod n
    BigInteger publicKey = g.modPow(privateKey, n);
    return new BigInteger[] { publicKey, privateKey };
  }

  private static String encrypt(BigInteger n, BigInteger g, BigInteger publicKey, String message) {
    return Arrays
        // Split message into chars
        .asList(message.split(""))
        .stream()
        // Get BigInteger value of char
        .map(m -> BigInteger.valueOf(m.charAt(0)))
        // Encrypt char
        .map(x -> {
          // We generate a new random BigInteger for every char
          BigInteger a = getRandomBigInteger(n);
          // y1 = g^a
          BigInteger y1 = g.modPow(a, n);
          // pk = g^b (already given)
          // y2 = x * (pk^a) mod n
          BigInteger y2 = publicKey.modPow(a, n).multiply(x).mod(n);
          return new BigInteger[] { y1, y2 };
        })
        // Converts each encrypted char into desired format
        .map(ElgamalGenerator::encrytedCharPairToString)
        .collect(Collectors.joining(";"));

  }

  private static String decrypt(BigInteger n, BigInteger g, BigInteger privateKey, String encryptedMessage) {
    return Arrays
        .asList(encryptedMessage.split(";"))
        .stream()
        // Convert from string to BigInteger[] so that we have our y1 and y2
        .map(ElgamalGenerator::stringToEncrytedCharPair)
        .map(y -> {
          BigInteger y1 = y[0];
          BigInteger y2 = y[1];
          // y2 * ((y1^b)^-1) mod n
          BigInteger x = y2.multiply(y1.modPow(privateKey, n).modInverse(n)).mod(n);
          return x;
        })
        // Convert the BigInteger value to a char and represent it as a string
        // BigInteger -> int -> char -> String
        .map(x -> String.valueOf((char) x.intValue()))
        .collect(Collectors.joining(""));
  }

  // #region Helper functions
  private static BigInteger getRandomBigInteger(BigInteger n) {
    // Generate private key in range [0, n-1]
    BigInteger maxRange = n.subtract(BigInteger.valueOf(1));
    BigInteger randomBigInteger;
    do {
      randomBigInteger = new BigInteger(maxRange.bitLength(), new Random());
    } while (randomBigInteger.compareTo(maxRange) >= 0);

    return randomBigInteger;
  }

  private static String encrytedCharPairToString(BigInteger[] y) {
    return "(" + y[0].toString() + "," + y[1].toString() + ")";
  }

  private static BigInteger[] stringToEncrytedCharPair(String s) {
    String[] split = s.replaceAll("[()]", "").split(",");
    return new BigInteger[] { new BigInteger(split[0]), new BigInteger(split[1]) };
  }

  // #endregion
}