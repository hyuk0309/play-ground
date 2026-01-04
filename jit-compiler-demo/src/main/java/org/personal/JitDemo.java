package org.personal;

public class JitDemo {
	public static void main(String[] args) {
		for (int i = 0; i < 1_000_000; i++) {
			calculate(i);
		}
	}

	private static int calculate(int n) {
		return n * n;
	}
}