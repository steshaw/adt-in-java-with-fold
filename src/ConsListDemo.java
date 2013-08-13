public class ConsListDemo {
  interface F1<T0, R> {
    R apply(T0 t0);
  }
  interface F2<T0, T1, R> {
    R apply(T0 t0, T1 t1);
  }
  static class Unit {
    private Unit() {}
    public static final Unit unit = new Unit();
  }
  abstract static class L<A> {
    private L() {}
    abstract <T> T fold(final F1<Unit, T> nil, final F2<A, T, T> cons);
    public static <A> L<A> nil() {
      return new L<A>() {  // FIX: efficiency
        @Override
        <T> T fold(final F1<Unit, T> nil, final F2<A, T, T> cons) {
          return nil.apply(Unit.unit);
        }
      };
    }
    public static <A> L<A> cons(final A a, final L<A> as) {
      return new L<A>() {
        @Override
       <T> T fold(final F1<Unit, T> nil, final F2<A, T, T> cons) {
          return cons.apply(a, as.fold(nil, cons));
        }
      };
    }
  }
  static class Ls {
    private Ls() {}
    static <A> String show(L<A> as) {
      return as.fold(
          new F1<Unit, String>() {
            @Override
            public String apply(Unit unit) {
              return "nil";
            }
          },new F2<A, String, String>() {
            @Override
            public String apply(A a, String s) {
              return "cons(" + a + ", " + s + ")";
            }
          }
      );
    }
    public static Integer sum(L<Integer> as) {
      return as.fold(
          new F1<Unit, Integer>() {
            @Override
            public Integer apply(Unit unit) {
              return 0;
            }
          }, new F2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer i, Integer acc) {
              return i + acc;
            }
          }
      );
    }
  }
  public static void println(Object o) { System.out.println(o); }
  public static void main(String[] args) {
    {
      final L<Integer> a = L.nil();
      println(Ls.show(a));
    }
    {
      final L<Integer> as = L.cons(3, L.<java.lang.Integer>nil());
      println(Ls.show(as));
    }
    {
      final L<Integer> as = L.cons(1, L.cons(2, L.cons(3, L.<java.lang.Integer>nil())));
      println(String.format("sum(%s) = %d", Ls.show(as), Ls.sum(as)));
    }
  }
}