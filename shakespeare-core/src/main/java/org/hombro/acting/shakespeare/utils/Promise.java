package org.hombro.acting.shakespeare.utils;

import java.util.function.Supplier;

public final class Promise {

    private Promise() {
        super();
    }

    public static <T> PromiseIntermediary<T> that(T o) {
        return new PromiseIntermediary<>((o));
    }

    public static <T> PromiseIntermediary<T> that(Supplier<T> s) {
        return new PromiseIntermediary<>(s.get());
    }

    public static class PromiseIntermediary<T> {
        private final T thing;
        private boolean condition = true;

        PromiseIntermediary(T thing) {
            this.thing = thing;
        }

        private void assertion(boolean bool){
            if(!(!condition || bool)){
                throw new RuntimeException();
            }
        }

        public void isTrue() {
            assertion(Boolean.TRUE.equals(thing));
        }

        public void isFalse() {
            assertion(Boolean.FALSE.equals(thing));
        }

        public PromiseIntermediary<T> when(boolean bool) {
            this.condition = bool;
            return this;
        }

        public PromiseIntermediary<T> when(Supplier<Boolean> supplier) {
            this.condition = supplier.get();
            return this;
        }

        public PromiseIntermediary<T> isNotNull() {
            assertion(thing != null);
            return this;
        }
    }
}
