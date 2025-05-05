import java.io.*;
import java.util.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class VirtualBitmap {

    private static final int PHYSICAL_BITMAP_SIZE = 500_000; // m
    private static final int VIRTUAL_BITMAP_SIZE = 500;      // l
    private static final int NUM_HASHES = 3;

    private static BitSet physicalBitmap = new BitSet(PHYSICAL_BITMAP_SIZE);
    private static Random random = new Random();
    private static MessageDigest md;

    static {
        try {
            md = MessageDigest.getInstance("MD5"); // hash for mapping
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    // Hash function for a string input to integer index
    private static int hash(String key, int range, int seed) {
        md.update((key + seed).getBytes());
        byte[] digest = md.digest();
        int hash = 0;
        for (int i = 0; i < 4; i++) {
            hash = (hash << 8) | (digest[i] & 0xff);
        }
        return Math.floorMod(hash, range);
    }

    
    public static void main(String[] args) {
        String inputFile = "project5input.txt";
        String outputFile = "virtual_bitmap_output.txt";
        List<Integer> trueSpreads = new ArrayList<>();
        List<Integer> estimatedSpreads = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            int n = Integer.parseInt(br.readLine().trim());

            for (int i = 0; i < n; i++) {
                String[] parts = br.readLine().trim().split("\\s+");
                String flowID = parts[0];
                int spread = Integer.parseInt(parts[1]);
                trueSpreads.add(spread);

                // Reset bitmap
                physicalBitmap.clear();

                // Step 1: Encode virtual bitmap indices for this flow
                int[] virtualBitmapIndices = new int[VIRTUAL_BITMAP_SIZE];
                for (int j = 0; j < VIRTUAL_BITMAP_SIZE; j++) {
                    virtualBitmapIndices[j] = hash(flowID + "_vb_" + j, PHYSICAL_BITMAP_SIZE, j);
                }

                // Step 2: Simulate recording of flow elements
                for (int j = 0; j < spread; j++) {
                    String element = flowID + "_elem_" + j;
                    int index = hash(element, VIRTUAL_BITMAP_SIZE, 101);
                    int mappedIndex = virtualBitmapIndices[index];
                    physicalBitmap.set(mappedIndex);
                }

                // Step 3: Estimate zero-bits in virtual bitmap
                int zeroCount = 0;
                for (int j = 0; j < VIRTUAL_BITMAP_SIZE; j++) {
                    if (!physicalBitmap.get(virtualBitmapIndices[j])) {
                        zeroCount++;
                    }
                }

                double V = (double) zeroCount / VIRTUAL_BITMAP_SIZE;
                double estimatedSpread = -VIRTUAL_BITMAP_SIZE * Math.log(V);
                if (V == 0) estimatedSpread = VIRTUAL_BITMAP_SIZE; // Avoid -Inf

                estimatedSpreads.add((int) Math.round(estimatedSpread));
            }

            // Write output
            try (PrintWriter pw = new PrintWriter(new FileWriter(outputFile))) {
                for (int i = 0; i < trueSpreads.size(); i++) {
                    pw.println(trueSpreads.get(i) + " " + estimatedSpreads.get(i));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
