package org.example;

import java.util.List;

public class NumbersValidity {

    public void valid(List<Integer> numbers) {
        validThanSix(numbers);
        validThanDuplication(numbers);
    }

    private void validThanSix(List<Integer> numbers) {
        if (numbers.size() != 6) {
            throw new IllegalArgumentException("로또의 숫자는 6개어야 합니다.");
        }
    }

    private void validThanDuplication(List<Integer> numbers) {
        boolean isDuplicate = numbers.stream()
                .distinct()
                .count() != numbers.size();

        if (isDuplicate) {
            throw new IllegalArgumentException("로또의 숫자는 중복되면 안됩니다.");
        }
    }
}