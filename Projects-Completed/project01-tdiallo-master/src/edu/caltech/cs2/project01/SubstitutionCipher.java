package edu.caltech.cs2.project01;

import javax.crypto.Cipher;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SubstitutionCipher {
    private String ciphertext;
    private Map<Character, Character> key;

    // Use this Random object to generate random numbers in your code,
    // but do not modify this line.
    private static final Random RANDOM = new Random();

    /**
     * Construct a SubstitutionCipher with the given cipher text and key
     * @param ciphertext the cipher text for this substitution cipher
     * @param key the map from cipher text characters to plaintext characters
     */
    public SubstitutionCipher(String ciphertext, Map<Character, Character> key) {
        this.ciphertext = ciphertext;
        this.key = key;
    }

    /**
     * Construct a SubstitutionCipher with the given cipher text and a randomly
     * initialized key.
     * @param ciphertext the cipher text for this substitution cipher
     */
    public SubstitutionCipher(String ciphertext) {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Map<Character, Character> idkey = new HashMap<>();
        for (int i = 0; i < alphabet.length(); i++) {
            idkey.put(alphabet.charAt(i), alphabet.charAt(i));
        }

        SubstitutionCipher cipher = new SubstitutionCipher(ciphertext, idkey);
        for (int i = 0; i < 10000; i++) {
            cipher = cipher.randomSwap();
        }
        this.ciphertext = ciphertext;
        this.key = cipher.key;
    }

    /**
     * Returns the unedited cipher text that was provided by the user.
     * @return the cipher text for this substitution cipher
     */
    public String getCipherText() {
        return this.ciphertext;
    }

    /**
     * Applies this cipher's key onto this cipher's text.
     * That is, each letter should be replaced with whichever
     * letter it maps to in this cipher's key.
     * @return the resulting plain text after the transformation using the key
     */
    public String getPlainText() {
        String text = "";
        for (int i = 0; i < this.ciphertext.length(); i++) {
            text += key.get(this.ciphertext.charAt(i));
        }
        return text;
    }

    /**
     * Returns a new SubstitutionCipher with the same cipher text as this one
     * and a modified key with exactly one random pair of characters exchanged.
     *
     * @return the new SubstitutionCipher
     */
    public SubstitutionCipher randomSwap() {
        Map<Character, Character> copyMap = new HashMap<>(this.key);
        Character key1 = CaesarCipher.ALPHABET[this.RANDOM.nextInt(26)];
        Character key2 = CaesarCipher.ALPHABET[this.RANDOM.nextInt(26)];
        while (key1 == key2) {
            key2 = CaesarCipher.ALPHABET[this.RANDOM.nextInt(26)];
        }
        Character holder = this.key.get(key1);
        copyMap.put(key1, this.key.get(key2));
        copyMap.put(key2, holder);
        SubstitutionCipher cipher = new SubstitutionCipher(this.ciphertext, copyMap);
        return cipher;
    }

    /**
     * Returns the "score" for the "plain text" for this cipher.
     * The score for each individual quadgram is calculated by
     * the provided likelihoods object. The total score for the text is just
     * the sum of these scores.
     * @param likelihoods the object used to find a score for a quadgram
     * @return the score of the plain text as calculated by likelihoods
     */
    public double getScore(QuadGramLikelihoods likelihoods) {
        double score = 0.0;
        String holder = getPlainText();
        for (int i = 0; i < holder.length() - 3; i++) {
            score += likelihoods.get(holder.substring(i, i + 4));
        }
        return score;
    }

    /**
     * Attempt to solve this substitution cipher through the hill
     * climbing algorithm. The SubstitutionCipher this is called from
     * should not be modified.
     * @param likelihoods the object used to find a score for a quadgram
     * @return a SubstitutionCipher with the same ciphertext and the optimal
     *  found through hill climbing
     */
    public SubstitutionCipher getSolution(QuadGramLikelihoods likelihoods) {
        SubstitutionCipher cipher = new SubstitutionCipher(this.ciphertext);
        double currScore = cipher.getScore(likelihoods);
        int i = 0;
        while (i <= 1000){
            SubstitutionCipher c2 = cipher.randomSwap();
            if (currScore < c2.getScore(likelihoods)){
                cipher = c2;
                currScore = c2.getScore(likelihoods);
                i = 0;
            }
            i++;
        }
        return cipher;
    }
}
