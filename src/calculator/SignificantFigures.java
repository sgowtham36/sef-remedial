package calculator;

public class SignificantFigures {
    /**
     * This method returns the number of significant digits in a number.
     * @param x - The number, in String form.
     * @return  - The number of significant digits in x.
     */
    static int getNumSignificantFigures(String x) {
        // This method doesn't actually do the computation.
        if (x.indexOf('.') == -1) {
            // First, check for a decimal point. If there isn't one, just pass the string on
            // for computation.
            return getNumSignificantFiguresInt(x);
        } else {
            int i = x.length() - 1, pointIndex = x.indexOf('.'), ans = 0;
            while (i >= pointIndex && x.charAt(i) == '0') {
                ans++;
                i--;
            }

            // Pass the pieces on for computation.
            StringBuilder sb = new StringBuilder(x);
            sb.deleteCharAt(pointIndex);
            ans += getNumSignificantFiguresInt(sb.toString());

            // And return the sum of the results.
            return ans;
        }
    }

    /**
     * This method performs the counting of the number of significant digits in a number.
     * @param x - The number. This number is assumed to be a non-negative integer.
     * @return - The number of significant digits in x.
     */
    private static int getNumSignificantFiguresInt(String x) {
        int left = 0, right = x.length() - 1, ans = 0;

        while (left <= right && x.charAt(left) == '0')		// Ignore all leading zeros.
            left++;

        if (left > right)
            return 0;

        while (x.charAt(right) == '0') {
            // Ignore trailing zeros too. These are counted before the number even gets to this
            // method.
            right--;
        }

        ans += (right - left + 1);			// Calculate the number of significant digits.
        // The logic is that everything that's significant MUST
        // lie between two non-zero digits.

        return ans;
    }
}
