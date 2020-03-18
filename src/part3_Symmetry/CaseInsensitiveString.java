package part3_Symmetry;

public class CaseInsensitiveString {
	private final String s;
	
	public CaseInsensitiveString(String s) {
		if (s == null)
			throw new NullPointerException();
		this.s = s;
	}
	
	// 대칭성 위반 케이스 
//	@Override
//	public boolean equals(Object o) {
//		if ( o instanceof CaseInsensitiveString)
//			return s.equalsIgnoreCase(
//					((CaseInsensitiveString) o).s);
//		if( o instanceof String) // 한 방향으로만 정상 동작
//			return s.equalsIgnoreCase((String) o);
//		return false;
//	}

// CaseInsensitiveString.equals("String") 은 ture 를 반화지만 문제는 String.equals(CaseInsensitiveString) 은 CaseInsensitiveString
// 이 뭔지 모른다는 것이다.

// 문제 해결 방법 

	@Override 
	public boolean equals(Object o) {
		return o instanceof CaseInsensitiveString && 
				((CaseInsensitiveString) o).s.equalsIgnoreCase(s);
	}
}