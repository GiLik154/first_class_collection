# first_class_collection

# 일급 컬렉션

**Collection을 Wrapping**하면서, **그 외 다른 멤버 변수가 없는 상태**를 일급 컬렉션이라 합니다.

**Wrapping** 함으로써 다음과 같은 이점을 가지게 됩니다.

1. **비지니스**에 종속적인 **자료구조**
2. Collection의 **불변성**을 보장
3. 상태와 행위를 한 곳에서 관리
4. 이름이 있는 컬렉션

(출처 : https://jojoldu.tistory.com/412)

이 부분에서 이해가 잘 되지 않았다.
**Wrapping**을 한다는게 무슨 의미일까………….?

예시를 보니 이해가 되었다.

```java
public class LottoService {
    private final RandomNumberCreator creator = new RandomNumberCreator();
    private final NumbersValidity validity = new NumbersValidity();

    public boolean compare(List<Integer> userNumbers) {
        List<Integer> computerNumbers = creator.crate();

        validity.valid(computerNumbers);
        validity.valid(userNumbers);

        // 이후 로직

        return...
    }
}
```

```java
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
```

여기서 **valid**를 각 리스트 마다 하는 게 옳을까…?

나중에 새로운 사람이 들어와서 저 **valid**를 하는 것을 까먹고,
만약 저기서 오류가 발생한다면 그것은 치명적으로 다가올 수 있다.
따라서 **구조적**으로 막는 것이 가장 효율적인 방법이라는 것을 우리는 모두 알 수 있다.
그런 방법 중 하나는 **‘일급 컬렉션’**을 이용하는 것이다.

즉, 똑같은 구조의 **Collection** 가 있다면 객체를 통해 **Wrapping** 하여 사용하는것이다.

지금 이 로또의 형식 말고도 다른 곳에서 든 예시를 가지고 와 보면

```java
// GSConvenienceStore.class
public class GSConvenienceStore {
    private List<IceCream> iceCreams;
    
    public GSConvenienceStore(List<IceCream> iceCreams) {
        validateSize(iceCreams)
        this.iceCreams = iceCreams;
    }
    
    private void validateSize(List<IceCream> iceCreams) {
    	if (iceCreams.size() >= 10) {
            new throw IllegalArgumentException("아이스크림은 10개 이상의 종류를 팔지 않습니다.")
        }
    }
    // ...
}
```

이러한 코드가 있다는 가정하에

**CUConvenienceStore**가 새로 생기면…? 아니면 아이스크림 말고 과자가 생기게 되면….?
여러가지 가정을 하였을 때 이걸 해결하는 방법은 아래와 같다

```java
// IceCream.class
public class IceCreams {
    private List<IceCream> iceCreams;
    
    public IceCreams(List<IceCream> iceCreams) {
        validateSize(iceCreams)
        this.iceCreams = iceCreams
    }
    
    private void validateSize(List<IceCream> iceCreams) {
    	if (iceCreams.size() >= 10) {
            new throw IllegalArgumentException("아이스크림은 10개 이상의 종류를 팔지않습니다.")
        }
    }
    
    public IceCream find(String name) {
        return iceCreams.stream()
            .filter(iceCream::isSameName)
            .findFirst()
            .orElseThrow(RuntimeException::new)
    }
    // ...
}
```

(출처 : https://tecoble.techcourse.co.kr/post/2020-05-08-First-Class-Collection/)

이런식으로 일급 컬렉션을 만들면 해결이 되게 된다.

자, 그렇다면 내가 만든 로또의 코드는 일급 컬렉션을 어떻게 만들면 될까?

```java
public class LottoNumbers {
    private final List<Integer> numbers;

    public LottoNumbers(List<Integer> numbers) {
        validThatSix(numbers);
        validThatDuplication(numbers);
        this.numbers = numbers;
    }

    public List<Integer> getNumbers() {
        return numbers;
    }

    private void validThatSix(List<Integer> numbers) {
        if (numbers.size() != 6) {
            throw new IllegalArgumentException("로또의 숫자는 6개어야 합니다.");
        }
    }

    private void validThatDuplication(List<Integer> numbers) {
        boolean isDuplicate = numbers.stream()
                .distinct()
                .count() != numbers.size();

        if (isDuplicate) {
            throw new IllegalArgumentException("로또의 숫자는 중복되면 안됩니다.");
        }
    }
}
```

나는 이런식으로 구현했다.
이 코드를 보자마자 문제점을 발견하는 분들도 계시겠지만, 우선 그 부분은 넘어가고, 1차적인 코드로만 봐주면 좋겠다. 이 코드의 문제점은 밑에서 설명하도록 하겠다.

이렇게 사용한다면

```java
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
}
```

위의 모든 테스트를 통과하게 된다.
즉, 숫자의 갯수를 제한을 할 수 도 있고, 중복도 막을 수 있다.
구조적으로 실수를 막을 수 있게 된다. 그렇다면 위의 ‘일급 컬렉션’을 이용하여 로직을 수정해보겠다.

```java
public class LottoService {
    private final RandomNumberCreator creator = new RandomNumberCreator();
    private final NumbersValidity validity = new NumbersValidity();

    public boolean compare(LottoNumbers userNumbers) {
        LottoNumbers computerNumbers = new LottoNumbers(creator.crate());
        

        // 이후 로직

        return ...
    }
}
```

이런식으로 변경이 가능할 것이다.

자 그렇다면 내 코드의 문제점은 무엇일까?

우선 **‘일급 컬렉션’**의 경우는 불변의 값을 가지고 있어야만 한다. **VO(Value Object)**를 생각하면 이해가 쉬웠다.

하지만, 내 코드의 경우는 매게변수로 받는 List의 메모리 해쉬값을 공유하기 때문에, 이 부분을 수정하면 오류가 나기 쉽다.

![Untitled](https://github.com/GiLik154/first_class_collection/assets/118507239/efb4edb4-41c7-4103-8c5e-b7de6050f942)

이것은 불변이라고 보기 어렵다.
따라서 코드를 조금 수정해주어야 하는데
아래와 같이 수정하면 된다.

```java
public LottoNumbers(List<Integer> numbers) {
        validThatSix(numbers);
        validThatDuplication(numbers);
        this.numbers = new ArrayList<>(numbers);
    }
```

여기서 차이점은 ***new ArrayList<>(numbers);*** 여기이다. 이렇게 수정 후에 테스트코드를 돌려보면

![Untitled (1)](https://github.com/GiLik154/first_class_collection/assets/118507239/58c46530-1aa8-44aa-9006-d9c80ca6eebe)

잘 통과한다!

자, 하나를 통과 했으니 또 다른 문제가 있다…
바로 get을 한 뒤에 **Collection**을 사용하는 경우가 문제가 된다.

![Untitled (2)](https://github.com/GiLik154/first_class_collection/assets/118507239/1c1daadd-8db8-4448-99c9-1449c347bfa3)

이러한 경우의 해결법은 

```java
public List<Integer> getNumbers() {
        return Collections.unmodifiableList(numbers);
    }
```

이렇게 바꾸면 된다. 이렇게 바꾸고 get 해온 컬렉션을 수정하게되면

![Untitled (3)](https://github.com/GiLik154/first_class_collection/assets/118507239/342bf507-3477-4546-a735-ba4145be2419)

이러한 익셉션을 함께 볼 수 있다.

제일 좋은 것은 아예 필요한 정보만 반환하도록 작성해 주는 게 가장 좋다고 생각한다.

이렇게 오늘은 ‘**일급 컬렉션’**에 대해서 공부했다.
많은 생각을 하게 되는 공부였다.
