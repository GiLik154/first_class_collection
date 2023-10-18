package org.example;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class RandomNumberCreator {
    public List<Integer> crate() {
        List<Integer> numbers = new ArrayList<>();
        SecureRandom random = new SecureRandom();

        while (isLessThanSix(numbers)) {
            int randomNumber = random.nextInt(45) + 1;

            if (!isDuplicate(numbers, randomNumber)) {
                numbers.add(randomNumber);
            }
        }

        return numbers;
    }

    private boolean isLessThanSix(List<Integer> numbers) {
        return numbers.size() < 6;
    }

    private boolean isDuplicate(List<Integer> numbers, int number) {
        return numbers.contains(number);
    }
}