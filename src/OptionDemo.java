public class OptionDemo {
  interface F1<T0, R> {
    R apply(T0 t0);
  }
  interface Show<A> {
    String show(A a);
  }
  static class Unit {
    private Unit() {}
    public static final Unit unit = new Unit();
  }
  abstract static class Option<A> {
    abstract <T> T fold(final F1<Unit, T> none, final F1<A, T> some);
    public static <A> Option<A> none() {
      return new Option<A>() {
        @Override
        <T> T fold(final F1<Unit, T> none, final F1<A, T> some) {
          return none.apply(Unit.unit); // FIX: efficiency
        }
      };
    }
    public static <A> Option<A> some(final A a) {
      return new Option<A>() {
        @Override
        <T> T fold(final F1<Unit, T> none, final F1<A, T> some) {
          return some.apply(a);
        }
      };
    }
  }
  static class Options {
    private Options() {}
    static <A> String show(Option<A> option) {
      return option.fold(
          new F1<Unit, String>() {
            @Override
            public String apply(Unit unit) {
              return "none";
            }
          }, new F1<A, String>() {
            @Override
            public String apply(A a) {
              return "some(" + a.toString() /* XXX: Using Object.toString() */ + ")";
            }
          }
      );
    }
    public static <A> Show<Option<A>> show() {
      return new Show<Option<A>>() {
        @Override
        public String show(Option<A> option) {
          return show(option);
        }
      };
    }
  }
  public static void println(Object o) { System.out.println(o); }
  public static void main(String[] args) {
    {
      final Option<Integer> a = Option.none();
      println(Options.show(a));
    }
    {
      final Option<Integer> a = Option.some(3);
      println(Options.show(a));
    }
  }
}