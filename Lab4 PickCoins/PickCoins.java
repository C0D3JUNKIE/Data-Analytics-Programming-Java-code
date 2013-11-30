import static java.lang.Math.*;

public class PickCoins {

	public static int maxVictory(int[] C) {
		int[][] c = new int[C.length][C.length];
		return maxVictory1(C, 0, C.length - 1, c);
	}

	public static int maxVictory1(int[] C, int start, int end, int[][] cache) {
		if (start > end) {
			return 0;
		}

		if (cache[start][end] != 0) {
			return cache[start][end];
		}

		int firstStart = C[start]
				+ min(maxVictory1(C, start + 2, end, cache),
						maxVictory1(C, start + 1, end - 1, cache));
		int firstEnd = C[end]
				+ min(maxVictory1(C, start, end - 2, cache),
						maxVictory1(C, start + 1, end - 1, cache));
		
		cache[start][end] = max(firstStart, firstEnd);
		
		return cache[start][end];

	}

}
