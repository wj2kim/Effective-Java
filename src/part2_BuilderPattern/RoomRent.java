package part2_BuilderPattern;

public class RoomRent {
	private final String refrigerator;
	private final String laundaryMachine;
	private final String closet;
	private final String microwave;
	private final String desk;
	private final String matrix;
	private final String bookshelf;
	private final String riceCooker;
	
	public static class Builder {
		// 필수 인자
		private final String refrigerator;
		private final String laundaryMachine;
		private final String closet;
		// 선택적 인자 - 기본값 초기화
		private String microwave;
		private String desk;
		private String matrix;
		private String bookshelf;
		private String riceCooker;
		
		public Builder (String refrigerator, String laundaryMachine, String closet) {
			this.refrigerator = refrigerator;
			this.laundaryMachine = laundaryMachine;
			this.closet = closet;
		}
		
		public Builder microwave (String param)
		{ microwave = param; return this; }
		public Builder desk (String param)
		{ desk = param; return this; }
		public Builder matrix (String param)
		{ matrix = param; return this; }
		public Builder bookshelf (String param)
		{ bookshelf = param; return this; }
		public Builder riceCooker (String param)
		{ riceCooker = param; return this; }
		
		public RoomRent build() {
			return new RoomRent(this);
		}
	}
	
	private RoomRent(Builder builder) {
		refrigerator = builder.refrigerator;
		laundaryMachine = builder.laundaryMachine;
		closet = builder.closet;
		microwave = builder.microwave;
		desk = builder.desk;
		matrix = builder.matrix;
		bookshelf = builder.bookshelf;
		riceCooker = builder.riceCooker;
	}

	public String getRefrigerator() {
		return refrigerator;
	}

	public String getLaundaryMachine() {
		return laundaryMachine;
	}

	public String getCloset() {
		return closet;
	}

	public String getMicrowave() {
		return microwave;
	}

	public String getDesk() {
		return desk;
	}

	public String getMatrix() {
		return matrix;
	}

	public String getBookshelf() {
		return bookshelf;
	}

	public String getRiceCooker() {
		return riceCooker;
	}

}
