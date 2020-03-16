package part2_BuilderPattern;

public class Excercise {

	public static void main(String[] args) {
		String refrigerator = "abc";
		String laundaryMachine = "abc";
		String closet = "abc";
		String desk = "abc";
		String matrix = "abc";
		RoomRent kunkookUniversity = new RoomRent.Builder(refrigerator, laundaryMachine, closet)
				.desk(desk).matrix(matrix).build();
	}
}
