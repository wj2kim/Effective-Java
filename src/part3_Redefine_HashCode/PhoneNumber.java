package part3_Redefine_HashCode;

public class PhoneNumber {
	private final short areaCode;
	private final short prefix;
	private final short lineNumber;
	
	public PhoneNumber(short areaCode, short prefix, short lineNumber) {
		rangeCheck ( areaCode, 999, "area_code");
		rangeCheck ( prefix, 999, "prefix");
		rangeCheck ( lineNumber, 9999, "line number");
		this.areaCode = areaCode;
		this.prefix = prefix;
		this.lineNumber = lineNumber;
	}
	
	private static void rangeCheck(int arg, int max, String name) {
		if (arg < 0 || arg > max)
			throw new IllegalArgumentException( name + " : " + arg);
	}
	
	@Override 
	public boolean equals(Object o) {
		if ( o == this)
			return true;
		if ( !(o instanceof PhoneNumber))
			return false;
		PhoneNumber pn = (PhoneNumber) o;
		return pn.lineNumber == lineNumber
				&& pn.prefix == prefix
				&& pn.areaCode == areaCode;	
	}
	
	// hashCode 메소드가 없으므로 문제가 발생한다! 
	
	
}
