import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileReader;
import java.math.BigInteger;
import java.util.*;

public class SecretFinder {

    public static BigInteger computeF0(List<Integer> x, List<BigInteger> y, int k) {
        BigInteger result = BigInteger.ZERO;

        for (int i = 0; i < k; i++) {
            BigInteger numerator = BigInteger.ONE;
            BigInteger denominator = BigInteger.ONE;

            for (int j = 0; j < k; j++) {
                if (i != j) {
                    numerator = numerator.multiply(BigInteger.valueOf(-x.get(j)));
                    denominator = denominator.multiply(BigInteger.valueOf(x.get(i) - x.get(j)));
                }
            }

            BigInteger term = y.get(i).multiply(numerator).divide(denominator);
            result = result.add(term);
        }

        return result;
    }

    public static void main(String[] args) {
        try {
            // Load JSON from file
            JSONObject json = new JSONObject(new JSONTokener(new FileReader("input.json")));

            JSONObject keys = json.getJSONObject("keys");
            int n = keys.getInt("n");
            int k = keys.getInt("k");

            List<Map.Entry<Integer, BigInteger>> points = new ArrayList<>();

            for (String key : json.keySet()) {
                if (!key.equals("keys")) {
                    int x = Integer.parseInt(key);
                    JSONObject pointObj = json.getJSONObject(key);
                    int base = Integer.parseInt(pointObj.getString("base"));
                    String valueStr = pointObj.getString("value");
                    BigInteger y = new BigInteger(valueStr, base);
                    points.add(new AbstractMap.SimpleEntry<>(x, y));
                }
            }

            // Sort by x value
            points.sort(Map.Entry.comparingByKey());

            // Take the first k sorted points
            List<Integer> xList = new ArrayList<>();
            List<BigInteger> yList = new ArrayList<>();
            for (int i = 0; i < k; i++) {
                xList.add(points.get(i).getKey());
                yList.add(points.get(i).getValue());
            }

            // Calculate secret C = f(0)
            BigInteger secretC = computeF0(xList, yList, k);
            System.out.println(secretC);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

