package org.example;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LottoNumbersTest {

    @Test
    void 로또_번호_생성_통과() {
        List<Integer> numbers = new ArrayList<>();
        numbers.add(1);
        numbers.add(2);
        numbers.add(3);
        numbers.add(4);
        numbers.add(5);
        numbers.add(6);

        LottoNumbers lottoNumbers = new LottoNumbers(numbers);

        assertEquals(6, lottoNumbers.getNumbers().size());
    }

    @Test
    void 로또_번호가_6개_미만이면_익셉션() {
        List<Integer> numbers = new ArrayList<>();
        numbers.add(1);
        numbers.add(2);
        numbers.add(3);
        numbers.add(4);
        numbers.add(5);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new LottoNumbers(numbers));
        assertEquals("로또의 숫자는 6개어야 합니다.", exception.getMessage());
    }

    @Test
    void 로또_번호가_6개_이상이면_익셉션() {
        List<Integer> numbers = new ArrayList<>();
        numbers.add(1);
        numbers.add(2);
        numbers.add(3);
        numbers.add(4);
        numbers.add(5);
        numbers.add(6);
        numbers.add(7);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new LottoNumbers(numbers));
        assertEquals("로또의 숫자는 6개어야 합니다.", exception.getMessage());
    }

    @Test
    void 로또_번호가_중복되면_익셉션() {
        List<Integer> numbers = new ArrayList<>();
        numbers.add(1);
        numbers.add(2);
        numbers.add(3);
        numbers.add(4);
        numbers.add(5);
        numbers.add(5);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new LottoNumbers(numbers));
        assertEquals("로또의 숫자는 중복되면 안됩니다.", exception.getMessage());
    }


    @Test
    void numbers와_해쉬값이_같으먼_안됨() {
        List<Integer> numbers = new ArrayList<>();
        numbers.add(1);
        numbers.add(2);
        numbers.add(3);
        numbers.add(4);
        numbers.add(5);
        numbers.add(6);

        LottoNumbers lottoNumbers = new LottoNumbers(numbers);
        numbers.add(7);

        assertNotEquals(numbers, lottoNumbers.getNumbers());
        assertEquals(6, lottoNumbers.getNumbers().size());
    }

    @Test
    void Get_후에_수정_불가함() {
        List<Integer> numbers = new ArrayList<>();
        numbers.add(1);
        numbers.add(2);
        numbers.add(3);
        numbers.add(4);
        numbers.add(5);
        numbers.add(6);

        LottoNumbers lottoNumbers = new LottoNumbers(numbers);
        lottoNumbers.getNumbers().add(7);

        assertThrows(UnsupportedOperationException.class, () -> lottoNumbers.getNumbers().add(7));
        assertEquals(6, lottoNumbers.getNumbers().size());
    }
}