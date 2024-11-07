package com.coderscampus.assignment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Assignment8Runner {

	public static void main(String[] args) {
		Assignment8 assignment = new Assignment8();
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		List<Integer> allNumbers = Collections.synchronizedList(new ArrayList<>());
		List<Future<?>> futures = new ArrayList<>();

		for (int i = 0; i < 1000; i++) {
			Future<?> future = executorService.submit(() -> {
				List<Integer> numbers = assignment.getNumbers();
				allNumbers.addAll(numbers);
			});
			futures.add(future);
		}

		futures.forEach(future -> {
			try {
				future.get();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		executorService.shutdown();

		Map<Integer, Integer> numberCounts = new ConcurrentHashMap<>();
		IntStream.rangeClosed(1, 10).forEach(i -> numberCounts.put(i, 0));

		allNumbers.forEach(num -> numberCounts.merge(num, 1, Integer::sum));

		String output = numberCounts.entrySet().stream().sorted(Map.Entry.comparingByKey())
				.map(entry -> entry.getKey() + "=" + entry.getValue()).collect(Collectors.joining(", "));

		System.out.println(output);
	}

}
