package calculator;

import java.util.Scanner;




/**
 * <p> Title: CalculatorValue Class. </p>
 * 
 * <p> Description: A component of a JavaFX demonstration application that performs computations </p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2017 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @author Saireddy Goutham Kavuri Srikanth, karthik reddy, Venkatesh, Deepak. Sudheendra
 * 
 * @version 4.00	2017-10-18 Long integer implementation of the CalculatorValue class 
 * 
 */
public class CalculatorValue {
	
	/**********************************************************************************************

	Attributes
	
	**********************************************************************************************/
	
	// These are the major values that define a calculator value
	double measuredValue = 0;
	double measuredValueError=0;
	String errorMessage = "";
	String errErrorMessage = "";
	
	/**********************************************************************************************

	Constructors
	
	**********************************************************************************************/

	/*****
	 * This is the default constructor
	 */
	public CalculatorValue() {
	}

	/*****
	 * This constructor creates a calculator value based on a long integer. For future calculators, it
	 * is best to avoid using this constructor.
	 */
	public CalculatorValue(double v) {
		measuredValue = v;
	}

	/*****
	 * This copy constructor creates a duplicate of an already existing calculator value
	 */
	public CalculatorValue(CalculatorValue v) {
		measuredValue = v.measuredValue;
		measuredValueError=v.measuredValueError;
		errorMessage = v.errorMessage;
		errErrorMessage = v.errErrorMessage;
	}

	/*****
	 * This constructor creates a calculator value from a string... Due to the nature
	 * of the input, there is a high probability that the input has errors, so the 
	 * routine returns the value with the error message value set to empty or the string 
	 * of an error message.
	 */
	public CalculatorValue(String s,String t) {
		measuredValue = 0;
		measuredValueError=0;
		if (s.length() == 0) {								// If there is nothing there,
			errorMessage = "***Error*** Input is empty";		// signal an error	
			return;												
		}
		/* If the first character is a plus sign, ignore it.*/
		int start = 0;										// Start at character position zero
		boolean negative = false;							// Assume the value is not negative
		if (s.charAt(start) == '+')							// See if the first character is '+'
			 start++;										// If so, skip it and ignore it
		
		// If the first character is a minus sign, skip over it, but remember it
		else if (s.charAt(start) == '-'){					// See if the first character is '-'
			start++;											// if so, skip it
			negative = true;									// but do not ignore it
		}
		
		// See if the user-entered string can be converted into an integer value
		Scanner tempScanner = new Scanner(s.substring(start));// Create scanner for the digits
		errorMessage=MeasuredValueRecognizer.checkMeasureValue(s.substring(start));
		if(errorMessage.equals("")) {
			measuredValue = tempScanner.nextDouble();				// Convert the value and check to see
		}else {
			tempScanner.close();
			return;
		}

		if (tempScanner.hasNext()) {							// that there is nothing else is 
			errorMessage = "***Error*** Excess data"; 		// following the value.  If so, it
			tempScanner.close();								// is an error.  Therefore we must
			measuredValue = 0;								// return a zero value.
			return;													
		}
		tempScanner.close();

		errorMessage = "";
		if (negative)										// Return the proper value based
			measuredValue = -measuredValue;					// on the state of the flag that

		if(t.length()==0) {
			// The logic below is to automatically add an error of plus-minus 5
			// after the significant figures of the measured value.

			int numSigMV = getNumSignificantFigures(s);
			StringBuilder sb = new StringBuilder();
			boolean pointFound = false;

			System.out.println("Number of significant digits in " + s + ": " + numSigMV);

			int i = 0, j = 0;
			while (j < numSigMV) {			// For each char in the measured value
				if (s.charAt(i) == '.') {					// up to the number of significant digits,
					sb.append(".");							// Copy the decimal point
					pointFound = true;
				}
				else {                                        // Put in zeros for all numbers
					sb.append("0");
					j++;
				}

				i++;
			}

			if ((sb.toString().length() == s.length()) && !pointFound) {
				// If all the significant digits have been covered, and there was no decimal
				// point, then we are dealing with an integer, and we must add a decimal
				// point before the 5.
				sb.append(".");
			}
			sb.append("5");

			if ((sb.toString().length() < s.length())) {
				int toGo = s.length() - sb.toString().length();
				for (i = 0; i < toGo; i++) {
					sb.append("0");
				}
			}
			measuredValueError=Double.parseDouble(sb.toString());
			System.out.println("Calculated error term: " + measuredValueError);
			errErrorMessage="";
		} else {
			// See if the user-entered string can be converted into an integer value
			Scanner tempScanner1 = new Scanner(t);// Create scanner for the digits
			errErrorMessage=MeasuredValueRecognizer.checkMeasureValue(t);
			if(errErrorMessage.equals("")) {
				measuredValueError = tempScanner1.nextDouble();				// Convert the value and check to see
			}else {
				tempScanner1.close();
				return;
			}
			if (tempScanner1.hasNext()) {							// that there is nothing else is 
				errErrorMessage = "***Error*** Excess data"; 		// following the value.  If so, it
				tempScanner1.close();								// is an error.  Therefore we must
				measuredValueError = 0;								// return a zero value.
				return;													
			}
			tempScanner1.close();
			errErrorMessage = "";
		}
	}


	/**
	 * This method returns the number of significant digits in a number.
	 * @param x - The number, in String form.
	 * @return  - The number of significant digits in x.
	 */
	private int getNumSignificantFigures(String x) {
		// This method doesn't actually do the computation.
		if (x.indexOf('.') == -1) {
			// First, check for a decimal point. If there isn't one, just pass the string on
			// for computation.
			return getNumSignificantFiguresInt(x, false);
		} else {
			// If there is a decimal point, then split over it.
			String[] pieces = x.split("\\.");

			// Pass the pieces on for computation.
			int left = getNumSignificantFiguresInt(pieces[0], false),
				right = pieces.length == 2 ? getNumSignificantFiguresInt(pieces[1], true) : 0;

			// And return the sum of the results.
			return left + right;
		}
	}

	/**
	 * This method performs the counting of the number of significant digits in a number.
	 * @param x - The number. This number is assumed to be a non-negative integer.
	 * @param afterDec - This parameter indicates whether x occurs before or after the
	 *                   decimal point in the original number.
	 * @return - The number of significant digits in x.
	 */
	private int getNumSignificantFiguresInt(String x, boolean afterDec) {
		int left = 0, right = x.length() - 1, ans = 0;

		while (left <= right && x.charAt(left) == '0')		// Ignore all leading zeros.
			left++;

		if (left > right)
			return afterDec ? left : 0;

		while (x.charAt(right) == '0') {	// For trailing zeros...
			if (afterDec)					// If x occurs after the decimal point,
				ans++;						// they are significant.
			right--;						// Else, they aren't.
		}

		ans += (right - left + 1);			// Calculate the number of significant digits.
											// The logic is that everything that's significant MUST
											// lie between two non-zero digits.

		return ans;
	}

	/**********************************************************************************************

	Getters and Setters
	
	**********************************************************************************************/
	
	/*****
	 * This is the start of the getters and setters
	 * 
	 * Get the error message
	 */
	public String getErrorMessage(){
		return errorMessage;
	}
	/*****
	 * This is the start of the getters and setters
	 * 
	 * Get the measured value
	 */
	public double getMeasuredValue(){
		return this.measuredValue;
	}
	/*****
	 * This is the start of the getters and setters
	 * 
	 * Get the measured value error 
	 */
	public double getMeasuredValueError(){
		return this.measuredValueError;
	}
	
	/*****
	 * This is the start of the getters and setters
	 * 
	 * Get the error message
	 */
	public String geterrErrorMessage(){
		return errErrorMessage;
	}
	
	/*****
	 * Set the current value of a calculator value to a specific long integer
	 */
	public void setValue(double v){
		measuredValue = v;
	}
	/*****
	 * Set the current error value of a calculator value to a specific long integer
	 */
	public void setErrorValue(double v){
		measuredValueError = v;
	}
	/*****
	 * Set the current value of a calculator error message to a specific string
	 */
	public void setErrorMessage(String m){
		errorMessage = m;
	}
	
	/*****
	 * Set the current value of a calculator value to the value of another (copy)
	 */
	public void setValue(CalculatorValue v){
		measuredValue = v.measuredValue;
		errorMessage = v.errorMessage;
	}
	/**********************************************************************************************

	The toString() Method
	
	**********************************************************************************************/
	
	/*****
	 * This is the default toString method
	 * 
	 * When more complex calculator values are creating this routine will need to be updated
	 */
	public String toString() {
		EnhancedToString resultString = new EnhancedToString();
		return resultString.cvToString(this);
//		return measuredValue +" "+measuredValueError;
	}
	
	/*****
	 * This is the debug toString method
	 * 
	 * When more complex calculator values are creating this routine will need to be updated
	 */
	public String debugToString() {
		return "measuredValue = " + measuredValue+"measuredValueError = " + measuredValueError + "\nerrorMessage = " + errorMessage + "\n";
	}

	
	/**********************************************************************************************

	The computation methods
	
	**********************************************************************************************/
	

	/*******************************************************************************************************
	 * The following methods implement computation on the calculator values.  These routines assume that the
	 * caller has verified that things are okay for the operation to take place.  These methods understand
	 * the technical details of the values and their reputations, hiding those details from the business 
	 * logic and user interface modules.
	 * 
	 * Since this is addition and we do not yet support units, we don't recognize any errors.
	 */
	public void add(CalculatorValue v) {
		//measuredValueError = ((measuredValueError/measuredValue)+(v.measuredValueError/v.measuredValue));
		measuredValue += v.measuredValue;
		measuredValueError = measuredValueError+v.measuredValueError;
		errorMessage = "";
	}

	/*****
	 * The following methods have not been implemented.  The code here is just a stub to allow the code to
	 * properly compile and run.
	 * 
	 * @param v
	 */
	public void sub(CalculatorValue v) {
		//measuredValueError = ((measuredValueError/measuredValue)+(v.measuredValueError/v.measuredValue));
		measuredValue -= v.measuredValue;
		measuredValueError = measuredValueError+v.measuredValueError;
		errorMessage = "";
	}

	public void mpy(CalculatorValue v) {
		measuredValueError = ((measuredValueError/measuredValue)+(v.measuredValueError/v.measuredValue));
		measuredValue *= v.measuredValue;
		measuredValueError *= measuredValue;
		errorMessage = "";
	}

	public void div(CalculatorValue v) {
		measuredValueError = ((measuredValueError/measuredValue)+(v.measuredValueError/v.measuredValue));
		measuredValue /= v.measuredValue;
		measuredValueError *= measuredValue;
		errorMessage = "";
	}
	public void Sqr() {
		measuredValueError = ((measuredValueError/measuredValue)*(0.5));
		measuredValue = Math.sqrt(measuredValue);
		measuredValueError *= measuredValue;
		errorMessage = "";
	}
}
