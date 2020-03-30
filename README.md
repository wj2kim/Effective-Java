# Effective-Java
Effective-Jave 2/E 에 나오는 코드 연습용 레포지토리
---
---
---
---
---
# Effective Java 2장 
객체를 만들어야하는 시점과 그 방법, 객체 생성을 피해야 하는 경우와 그 방법, 적절한 순가네 객체가 삭제되도록 보장하는 방법, 그리고 삭제 전에 반드시 이루어져야 하는 청소 작업 들을 관리하는 방법을 살펴본다. 

# 1. 생성자 대신 정적 팩토리 메소드를 사용 할 수 없는지 생각해보라

1) 일반적 클래스를 통해 객체를 만드는 일반적인 방법 → public으로 선언된 생서자 ( constructor )를 이용하는 것이다. 

2) 클래스에 public 으로 선언된 정적 팩토리 메소드 ( static factory method )를 추가하는 것이다. 

예시 :

    public static Boolean valueOf(boolean b){
    	return b ? Boolean.TRUE : Boolean.FALSE;
    }

이 메소드는 기본 타입 boolean의 값을 Boolean 객체에 대한 참조 ( reference ) 로 변환한다. 

## 1.1 정적 팩토리 메소드를 제공하는 방법의 장단점

- 장점 1 ) 생성자와는 달리 정적 팩토리 메소드에는 이름(name) 이 있다. 
생성자에 전달되는 인자(parameter)들은 어떤 객체가 생성되는지를 설명하지 못하지만, 정적 팩토리 메소드는 이름을 잘 짓기만 한다면 사용하기도 쉽고 클라이언트의 가독성(readability)도 높아진다. 
예를 들어 소수(prime)일 가능성이 높은 BigInteger 객체를 생성하는 생성자 BigInteger( int, int, Random )는 BigInteger.probablePrime과 같은 이름의 정적 팩토리 메소드로 표현했으면 더 이해하기 쉬웠을 것이다.

    < 같은 시그니처(signiture)를 갖는 생성자를 여러 개 정의할 필요가 있을 때는 그 생성자들을 정적 팩토리 메소드로 바꾸고, 메소드 이름을 보면 차이가 명확히 드러나도록 작명에 신경쓰자> 

- 장점 2) 생성자와는 달리 호출 할 때마다 새로운 객체를 생성 할 필요가 없다. 
이 기법은 경량(Flyweight) 패턴과 유사하다. 동일한 객체가 요청되는 일이 잦고, 특히 객체를 만드는 비용이 클 때 적용하면 성능을 크게 개선할 수 있다. 정적 팩토리 메소드를 사용하면 같은 객체를 반복해서 반환 할 수 있으므로 어떤 시점에서 어떤 객체가 얼마나 존재할지를 정밀하게 제어 할 수 있다. 그런 기능을 갖춘 클래스는 개체 통제 클래서 (instance-controlled class)라고 부른다. 개체 통제 클래스를 작성하는 이유 → 개체 수를 제어하면 싱클톤(singleton) 패턴을 따르도록 할 수 있고, 객체 생성이 불가능한 클래스를 만들 수도 있다.

- 장점 3) 생성자와는 달리 반환값 자료형의 하위 자료형 객체를 반환할 수 있다는 것이다. 
즉 반환되는 객체의 클래스를 훨씬 유연하게 결정할 수 있다. 이 유연성을 활용하면 public으로 선언되지 않은 클래스의 객체를 반환하는 API를 만들 수 있다. 이 기법은 인터페이스 기반 프레임워크 (interface-based framework) 구현에 적합하다. 이 프레임워크에서 인터페이스는 정적 팩토리 메소드의 반환값 자료형으로 이용된다.

 

- 장점 4) 형인자 자료형 (parameterized type) 객체를 만들 때 편하다는 점이다. 
이런 클래스의 생성자를 호출할 때는, 문맥상 형인자가 명백하더라도 반드시 인자로 형인자를 전달해야 한다. 그래서 보통 형인자는 연달아 두 번 사용하게 된다. 
예시:

    Map<String, List<String>> m = new HashMap<String, List<String>>();

이 처럼 자료형 명세를 중복하면, 형인자가 늘어나 길고 복잡한 코드가 만들어진다. 
하지만 정적 팩토리 메소드를 사용하면 컴파일러가 형인자를 스스로 알아내도록 할 수 있다. 이 기법을 자료형 유추 (type inference)라고 부른다. 
예시 : 

    public static <K, V> HashMap<K, V> newInstance(){
    	return new HashMap<k, V>();
    }
    
    Map<String, List<String>> m = HashMap.newInstance();

이런 메소드가 있으면 선언문을 좀 더 간결하게 작성할 수 있다.

** 자신만의 유틸리티 클래스 안에 이런 메소드를 만들어 넣어두면 된다. 바람직한 방법은 형인자 유틸리티 클래스 (parameterized utility class) 안에 정적 팩토리 메소드를 넣는 것이다. 

- 단점 1) 정적 팩토리 메소드만 있는 클래스를 만들면 생기는 가장 큰 문제는 , public이나 protected로 선언된 생성자가 없으므로 하위 클래스를 만들수 없다는 것이다.
- 단점 2) 정적 팩토리 메소드가 다른 정적 메소드와 확연히 구분되지 않는다는 것이다. 
생성자는 다른 메소드와 뚜렷이 구별되지만, 정적 팩토리 메소드는 그렇지 않다. 클래스나 인터페이스 주석(comment)를 통해 정적 팩토리 메소드임을 알리거나, 정적 팩토리 메소드의 이름을 지을 때 조심하는 수 밖에 없다. 
보통 정적 팩토리 메소드의 이름으로는 다음과 같은 것들을 사용한다. 
1. valueOf : 인자로 주어진 값과 같은 값을 갖는 객체를 반환한다는 뜻이다. 형변환 (type-conversion)메소드이다. 
2. of : valueOf 를 더 간단하게 쓴것이다. 
3. getInstance : 인자에 기술된 객체를 반환하지만 인자와 같은 값을 갖지 않을 수도 있다. 싱글톤(singleton)패턴을 따르는 경우, 이 메소드는 인자 없이 항상 같은 객체를 반환한다.
4. newInstance : getInstance와 같지만 호출할 때마다 다른 객체를 반환한다. 
5. getType : getInstance와 같지만, 반환될 객체의 클래스와 다른 클래스에 팩토리 메소드가 있을 때 사용한다. Type는 팩토리 메소드가 반환할 개게의 자료형이다. 
6. newType : newInstace와 같지만, 반환될 객체의 클래스와 다른 클래스에 팩토리 메소드가 있을 때 사용한다. Type는 팩토리 메소드가 반환할 객체의 자료형이다.

# 2. 생성자 인자가 많을 때는 Builder 패턴 적용을 고려하라

정적 팩토리나 생성자는 같은 문제를 갖고 있다. 선택적 인자가 많은 상황에 잘 적응 하지 못한다는 것이다. 
자바빈 패턴 ( 생성자를 호출하여 객체부터 만든 다음, setter로 선택적 필드의 값을 채우는 방법) 

하지만 자바빈 패턴은 1회의 함수 호출로 객체 생성을 끝낼 수 없으므로, 객체 일관성(consistency)이 일시적으로 깨질 수 있다는 것이다. 또 한 변경 불가능(immutable)클래스를 만들 수 없다는 것도 단점이다. 

점층적 생성자 패턴의 안전성에 자바빈 패턴의 가독성을 결합한 세 번째 대안 → 빌더(Builder) 패턴.

## 2.1 빌더 패턴

필요한 객체를 직접 생성하는 대신, 클라이언트는 먼저 필수 인자들을 생성자에 ( 또는 정적 팩토리 메소드에 ) 전부 전달하여 빌더 객체 (builder object)를 만든다. 그런 다음 빌더 객체에 정의된 설정 메소드들을 호출하여 선택적 인자들을 추가해 나아간다. 그리고 마지막으로 아무런 인자 없이 builder 메소드를 호출하여 변경 불가능 (immutable)객체를 만드는 것이다. 빌더 클래스는 빌더가 만드는 객체 클래스의 정적 멤버 클래스로 정의한다. 

    public class NutritionFacts {
    	private final int servingSize;
    	private final int servings;
    	private final int calories;
    	private final int fat;
    	private final int sodium;
    	private final int carbohydrate;
    	
    	
    	public static class Builder {
    		// 필수 인자 
    		private final int servingSize;
    		private final int servings;
    		// 선택적 인자 - 기본값으로 초기화 
    		private int calories = 0;
    		private int fat = 0;
    		private int carbohydrate = 0;
    		private int sodium = 0;
    		
    		public Builder (int servingSize, int servings) {
    			this.servingSize = servingSize;
    			this.servings = servings;
    		}
    		
    		public Builder calories (int val)
    		{ calories = val ; return this; }
    		public Builder fat (int val)
    		{ fat = val ; return this; }
    		public Builder carbohydrate(int val)
    		{ carbohydrate = val; return this; }
    		public Builder sodium (int val)
    		{ sodium = val; return this; }
    		
    		public NutritionFacts build() {
    			return new NutritionFacts(this);
    		}
    	}
    	
    	private NutritionFacts(Builder builder) {
    		servingSize = builder.servingSize;
    		servings = builder.servings;
    		calories = builder.calories;
    		fat = builder.fat;
    		sodium = builder.sodium;
    		carbohydrate = builder.carbohydrate; 
    	}
    }

NutritionFacts 객체가 변경 불가능 하다는 사실, 그리고 모든 인자의 기본값 (default value)가 한곳에 모여있다는 것에 유의하자. 빌더에 정의된 설정 메소드는 빌더 객체 자신을 반환하므로, 설정 메소드를 호출하는 코드는 쭉 이어서 쓸 수 있다. 아래의 코드 처럼 

    NutritionFacts cocaCola = new NutritionFacts.Builder(240, 8).calories(100).sodium(35)
    .carbohydrate(27).build();

생성자와 마찬가지로, 빌더 패턴을 사용하면 인자에 불변식(invariant)를 적용할 수 있다. 불변식을 위반한 경우, build 메소드는 IllegalStateException을 던져야 한다. 
생성자와 비교했을 때 빌더 패턴이 갖는 또 한가지 작은 장점은 빌더 객체는 여러개의 varargs만 인자로 가질 수 있다. 하지만 빌더는 인자마다 별도의 설정 메소드를 사용하므로, 설정 메소드마다 하나씩 필요한 만큼 varargs인자를 사용 할 수 있다. 

또 한 하나의 빌더 객체로 여러 객체를 만들 수 있다. 또 한 빌더 객체는 어떤 필드의 값은 자동으로 채울 수도 있다. 객체가 만들어 질 때마다 자동적으로 증가되는 일련번호 같은 것이 좋은 예다. 

** 요약하자면, 빌더 패턴은 인자가 많은 생성자나 정적 팩토리가 필요한 클래스를 설계할 때, 특히 대부분의 인자가 선택적 인자인 상황에 유용하다. 클라이언트 코드 가독성은 전통적인 점층적 생성자 패턴을 따를 때보다 훨씬 좋아질 것이며, 그 결과물은 자바빈을 사용할 때보다 훨씬 안전할 것이다. 

# 3. private 생성자나 enum 자료형은 싱글턴 패턴을 따르도록 설계하라

싱글턴 → 객체를 하나만 만들 수 있는 클래스.

싱글턴은 보통 유일할 수 밖에 없는 시스템 컴포넌트를 나타낸다.

창 관리자(window manager)나 파일 시스템(file system)같은 것들이 그 예다. 

JDK 1.5 이전에는 싱글톤을 구현하는 방법이 두 가지였다. 두 방법 다 생성자는 private으로 선언하고, 싱글톤 객체는 정적(static) 멤버를 통해 이용한다. 

1번째 방법 : 

    // public final 필드를 이용한 싱글톤
    public class Elvis {
    	public static final Elvis INSTANCE = new Elvis();
    	private Elvis() { ... }
    	
    	public void leaveTheBuilding() { ... }
    }

private 생성자는 public static final 필드인 Elvis.INSTANCE를 초기화 할 때 한번만 호출된다. public 이나 protected로 선언된 생성자가 없으므로, Elvis 객체는 일단 Elvis 클래스가 초기화 되고 나면 하나만 존재한다. 

2번째 방법 : 

    // 정적 팩토리를 이용한 싱글톤 
    public class Elvis{
    	private static final Elvis INSTANCE = new Elvis();
    	private Elvis() { ... }
    	public static Elvis getInstance() { return INSTANCE; }
    
    	public void leaveTheBuilding() { ... }
    }

Elvis.getInstance 는 항상 같은 객체에 대한 참조를 반환한다. 이것 외의는 Elvis 객체는 만들 수 없다. 

public static final로 선언 했으므로 항상 같은 객체를 참조하게 된다. 

JDK 1.5 부터는 싱글톤을 구현할 때 새로운 방법을 사용할 수 있다. 원소가 하나뿐인 enum 자료형을 정의하는 것이다. 

    // Enum 싱글톤 - 이렇게 하는 쪽이 더 낫다 
    public enum Elvis {
    	INSTANCE;
    
    	public void leaveTheBuilding() { ... }
    }

이 접근법과 기존의 차이는 좀 더 간결하다는 것과, 직렬화가 자동으로 처리된다는 것이다. 직렬화가 아무리 복잡하게 이루어져도 여러 객체가 생길 일이 없으며, 리플렉션(reflection)을 통한 공격에도 안전하다. 

** 원소가 하나뿐인 enum 자료형이야 말로 싱글톤을 구현하는 가장 좋은 방법이다.

# 4. 객체 생성을 막을 때는 private 생성자를 사용하라

기본 생성자는 클래스에 생성자가 없을 때 만들어지니까, private 생성자를 클래스에 넣어서 객체 생성을 방지 할 수 있다.

예시

    // 객체를 만들 수 없는 유틸리티 클래스 
    public class UtilityClass {
    	private UtilityClass(){
    		throw new AssertionError();
    	}
    	... // 나머지는 생략
    }

명시적으로 정의된 생성자가 private이므로 클래스 외부에서는 사용할 수 없다. AssertionError는 클래스 안에서 실수로 생성자를 호출하면 바로 알 수 있게 하기 위한 것. 이렇게 구현한 클래스의 객체는 어떤 상황에서도 만들 수가 없다.

# 5. 불필요한 객체는 만들지 말라

기능적으로 동일한 객체는 필요할 때마다 만드는 것보다 재사용하는 편이 낫다. 객체를 재사용하는 프로그램은 더 빠르고 더 우아하다. (immutable) 객체는 언제나 재사용할 수 있다.

예를 들어 

    String s = new String("stringette"); // 이러면 곤란하다!

위의 문장을 실행 될 때마다 String 객체를 만든다. String 생성자에 전달되는 "stringette"는 그 자체로 String 객체다. 만일 위의 문장이 순환문(loop)이나 자주 호출되는 메소드 안에 있다면, 수백 개의 String 객체가 쓸데없이 만들어질 것이다. 

그냥 아래 처럼 하는것이 바람짖ㄱ하다. 

String s = "stringette";

가상 머신에서도 실행되는 모든 코드가 해당 객체를 재사용하게 된다.

# 6. 유효기간이 지난 객체 참조는 폐기하라

예시

    public Object pop(){
    	if ( size == 0 )
    		throw new EmptyStackException();
    	Object result = element[--size];
    	element[size] = null; // 만기 참조 제거 
    	return result;
    }

만기 참조를 null로 만들면 나중에 실수로 그 참조를 사용하더라도 NullPointerException이 발생하기 때문에 프로그램은 오작동하는 대신 바로 종료된다는 장점이 있다. 

객체 참조를 무조건 null 처리 해야하는 것이 아니라 예외적인 조치가 되어야 한다. 만기 참조를 제거하는 가장 좋은 방법은 해당 참조가 보관된 유효범위(scope)를 벗어나게 두는 것이다. 변수를 정의 할 때 그 유효범위를 최대한 좁게 만들면 자연스럽게 해결된다.

**원소가 참조하는 객체는 할당된(allocated) 객체지만, 나머지 원소가 참조하는 객체는 반환 가능한 (free) 객체들이다. 하지만 쓰레기 수집기 입장에서는 전부 유효해 보인다. 그러니 null 로 만들어 버려서 가비지 컬렉터는 반환해도 좋은 객체가 어떤 것인지 바로 알 수 있다.

일반적으로, 자체적으로 관리하는 메모리가 있는 클래스를 만들 때는 메모리 누수가 발생하지 않고록 주의해야 한다. 

더 이상 사용되지 않는 원소 안에 있는 객체 참조는 반드시 null로 바꿔 주어야 한다.

** 캐시 (cashe) 도 메모리 누수가 흔히 발생하는 장소다.

객체 참조를 캐시 안에 넣어 놀고 잊어버리는 일이 많다. 

해결책
1.  WeakHashMap을 가지고 캐시를 구현한다. 
캐시 바깥에서 키(key)를 참조하고 있을 때만 값(value)을 보관하면 될 때 쓸 수 있는 전략이다. 
키에 대한 참조가 만기 참조가 되는 순간 캐시 안에 보관된 키-값 쌍은 자동으로 삭제되기 때문이다. WeakHashMap은 캐시 안에 보관되는 항목의 수명이 키에 대한 외부 참조의 수명에 따라 결정되는 상황에서만 적용 가능하다. 

2. 메모리 누수가 흔히 발견되는 또 한 곳은 리스너 (listener) 등의 역호출자 (callback) 이다. 

역호출자 등록 기능을 제공하는 API를 사용하는 클라이언트가 역호출자를 명시적으로 제거하지 않을 경우, 적절한 조치를 취하기 전까지 메모리는 점유된 상태로 남아있게 된다. 가비지 컬렉터가 역호출자를 즉시 처리하도록 할 가장 좋은 방법은 역호출자에 대한 약한 참조 (weak reference)만 저장하는 것이다. WeakHashMap의 키로 저장하는 것이 그 예다.

# 7. 종료자 사용을 피하라

종료자 (finalizer)는 예측 불가능하며, 대체로 위험하고, 일반적으로 불필요하다.

긴급한 (time-critical) 작업을 종료자 안에서 처리하면 안된다.

종료자 안에서 파일을 닫도록 하면 치명적이다.


---


# Effective Java 3장 
이번 장에서는 Object의 비-final 메소드들은 언제, 그리고 어떻게 재정의하는지 살펴 볼 것이다. 

# 1. equals를 재정의할 때는 일반 규약을 따른다

Object.equals를 재정의 하는 것이 바람직 할 때는 언제? 
객체 동일설 ( Obejct equality) 이 아닌 논리적 동일성 (logical equality)의 개념을 지원하는 클래스일 때, 그리고 상위 클래스의 equals가 하위 클래스의 필요를 충족하지 못할 때 재정의 한다. 값 클래스 (value class)는 대체로 그 조건에 부합한다. 

열거 자료형 (enum)같은 클래스는 개체 통제 기능을 사용해 값마다 최대 하나의 객체만 존재하도록 제한했다. 이런 클래스는 equals 메소드를 재정의 할 필요가 없다.

# 2. equals를 재정의할 때는 반드시 hashCode도 재정의하라

만약 equals를 재정의 하고 hashCode를 재정의 하지 않으면 Object.hashCode의 일반 규약을 어기게 되므로 , HashMap, HashSet, Hashtable 같은 해쉬( hash ) 기반 컬렉션과 함께 사용하면 오작동 하게 된다. 

equals 매소드가 논리적으로 같다고 판단한 두 객체라 해도 Object hashCode 입장에서 보면 그다지 공통점이 없는 두 객체일 뿐인다. 따라서 Object의 hashCode 메소드는 규약대로 같은 정수를 반환하는 대신, 무작위로 선택된 것 처럼 보이는 두개의 정수를 반환한다. 

예를 들어, 아래의 간단한 PhoneNumber 클래스를 보면 equals 메소드는 구현 되어 있다.

# 3. toString은 항상 재정의하라

toString 이 반환하는 문자열은 "사람이 읽기 쉽도록 간략하지만 유용한 정보를 제공해야 한다. 또 한 모든 하위 클래스는 이 메소드를 재정의함이 바람직하다. 

toString을 잘 만들어 놓으면 클래스를 좀 더 쾌적하게 사용할 수 있다. 

만약 toString을 재정의하지 않으면 진단 메세지를 통해 얻을 수 있는 정보는 거의 없다. 

toString 메소드를 재정의하면 해당 객체만 해택을 보는 것이 아니라 해당 객체에 대한 참조를 유지하는 객체들, 특히 컬렉션 까지 해택을 본다. 

가능하다면 toString 메소드는 객체 내의 중요 정보를 전부 담아 반환해야한다.

# 4. clone을 재정의할 때는 신중하라

Cloneable은 어떤 객체가 복제(clone)을 허용한다는 사실을 알리는 데 쓰려고 고안된 믹스인(mixin) 인터페이스다. Cloneable 인터페이스에 아무런 메소드도 없다면 대체 Cloneable이 하는 일은 무엇인가? protected로 선언된 Object의 clone 메소드가 어떻게 동작할지 정한다. Cloneable을 구현하지 않은 클래스라면 clone 메소드는 CloneNotSupportedException을 던진다. clone 메소드의 일반 규약은 느슨하다. 

java.lang.Object 명세를 보면 다음과 같이 되어 있다. 

- 객체의 복사본을 만들어서 반환한다. "복사"의 정확한 의미는 클래스마다 다르다. 일반적으로는 다음의 조건이 충족되어야 한다. 객체 x가 있다고 하자.  x.clone() ! = x ( 이 조건은 참이어야 한다. )
- x.clone().getClass() == x.getClass() 이 조건은 참이 되겠지만, 반드시 그래야 하는것은 아니다.
- x.clone().equals(x) 이 코드를 실행한 결과도 true 가 되겠지만, 반드시 그래야 하는 것은 아니다. 객체를 복사하면 보통 같은 클래스의 새로운 객체가 만들어지는데, 내부 자료 구조까지 복사해야 될 수 도 있다. 어떤 생성자도 호출 되지 않는다.

비-final 클래스에 clone을 재정의할 때는 반드시 super.clone을 호출해 얻은 객체를 반환해야 한다. 어떤 클래스의 모든 상위 클래스가 이 규칙을 따른다면 super.clone은 최종적으로 Object의 clone 메소드를 호출하게 될 것이고, 원하는 클래스의 객체가 만들어 질 것이다. 

    @Override
    public PhoneNumber clone(){
    	try {
    		return (PhoneNumber) super.clone();
    	}	catch ( CloneNotSupportedException e ) {
    		throw new AssertionError(); // 수행 될 일은 없음. 
    	}
    }

이 clone  메소드는 Object 가 아니라  PhoneNumber를 반환한다.

# 5. Comparable 구현을 고려하라

compareTo 메소드는 Object 에 선언되어 있지 않다. 이 메소드는 Comparable 인터페이스에 포함된 유일한 메소드다. Object의 equals 메소드와 특성은 비슷하지만, 단순한 동치성 검사 이외에 순서 비교가 가능하며, 좀 더 일반적이다. Comparable을 구현한 객체들의 배열을 정렬하는 것은 아래의 예제처럼 간단하다. 

Arrays.sort (a);
