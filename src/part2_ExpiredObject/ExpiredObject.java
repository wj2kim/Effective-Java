package part2_ExpiredObject;

import java.util.EmptyStackException;

public class ExpiredObject {
	
	String element [];
	int size = 0;
	

	// stack 의 pop 메소드의 경우 
	public Object pop() {
		if( size == 0) 
			throw new EmptyStackException();
		Object result = element[ --size ];
		element[ size ] = null; // 만기 참조 제거 
		return result;
	}
	
}
