import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigInteger;

public class SecretFinder {

    // Lagrange interpolation to calculate f(0)
    public static BigInteger lagrangeInterpolation(BigInteger[][] points, int k) {
        BigInteger constantTerm = BigInteger.ZERO;

        for (int i = 0; i < k; i++) {
            BigInteger li = BigInteger.ONE;
            for (int j = 0; j < k; j++) {
                if (i != j) {
                    li = li.multiply(BigInteger.ZERO.subtract(points[j][0])).divide(points[i][0].subtract(points[j][0]));
                }
            }
            // Calculate contribution to the constant term
            constantTerm = constantTerm.add(points[i][1].multiply(li));
        }
        return constantTerm;
    }

    public static void main(String[] args) {
        try {
            // Load and parse JSON
            FileInputStream fileStream = new FileInputStream("testcase1.json");
            JSONObject jsonObject = new JSONObject(new JSONTokener(fileStream));

            // Extract n and k values
            JSONObject keys = jsonObject.getJSONObject("keys");
            int n = keys.getInt("n");
            int k = keys.getInt("k");

            // Parse and decode points
            BigInteger[][] points = new BigInteger[k][2];
            int count = 0;

            for (int i = 1; i <= n && count < k; i++) {
                String key = String.valueOf(i);
                if (jsonObject.has(key)) {
                    JSONObject point = jsonObject.getJSONObject(key);
                    int base = point.getInt("base");
                    String valueStr = point.getString("value");
                    BigInteger y = new BigInteger(valueStr, base);
                    points[count][0] = BigInteger.valueOf(i); // The x-coordinate is the index
                    points[count][1] = y;  // The y-coordinate is the decoded value
                    count++;
                }
            }

            // Calculate the secret (constant term)
            BigInteger secret = lagrangeInterpolation(points, k);
            System.out.println("The secret (constant term) is: " + secret);

        } catch (FileNotFoundException e) {
            System.err.println("JSON file not found.");
        } catch (NumberFormatException e) {
            System.err.println("Error parsing number: " + e.getMessage());
        }
    }
}