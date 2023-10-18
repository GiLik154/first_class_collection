package org.example;

import java.util.List;

public class LottoService {
    private final RandomNumberCreator creator = new RandomNumberCreator();
    private final NumbersValidity validity = new NumbersValidity();

    public boolean compare(LottoNumbers userNumbers) {
        LottoNumbers computerNumbers = new LottoNumbers(creator.crate());


        // 이후 로직


        return true;
    }
}
