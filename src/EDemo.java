/**
  --
  -- The following Java code more-or-less implements the following Haskell code.
  -- Not so much the "deriving (Show)" part.
  --
  type Sym = String
  data E
      = Var Sym
      | App Expr Expr
      | Abs Sym Expr
    deriving(Show)
*/
public class EDemo {
  interface F1<T0, R> {
    R apply(T0 t0);
  }
  interface F2<T0, T1, R> {
    R apply(T0 t0, T1 t1);
  }
  static abstract class E {
    abstract <T> T fold(F1<String, T> var, F2<E, E, T> app, F2<String, E, T> abs);
    public static E var(final String v) {
      return new E() {
        @Override
        public <T> T fold(F1<String, T> var, F2<E, E, T> app, F2<String, E, T> abs) {
          return var.apply(v);
        }
      };
    }
    public static E abs(final String v, final E e) {
      return new E() {
        @Override
        public <T> T fold(F1<String, T> var, F2<E, E, T> app, F2<String, E, T> abs) {
          return abs.apply(v, e);
        }
      };
    }
    public static E app(final E l, final E r) {
      return new E() {
        @Override
        public <T> T fold(F1<String, T> var, F2<E, E, T> app, F2<String, E, T> abs) {
          return app.apply(l, r);
        }
      };
    }
  }
  static class Es {
    private Es() {}
    static String show(E e) {
      return e.fold(
          new F1<String, String>() {
            @Override
            public String apply(String v) {
              return v;
            }
          }, new F2<E, E, String>() {
            @Override
            public String apply(E e, E e2) {
              return "(" + show(e) + " " + show(e2) + ")";
            }
          }, new F2<String, E, String>() {
            @Override
            public String apply(String v, E e) {
              return "λ" + v + ". " + show(e);
            }
          }
      );
    }
  }
  public static void println(String s) {
    System.out.println(s);
  }
  public static void main(String[] args) {
    {
      // λx. λy. x y
      final E e = E.abs("x", E.abs("y", E.app(E.var("x"), E.var("y"))));
      println(Es.show(e));
    }
    {
      // λf.(λx.f(x x))(λx.f(x x))
      final E x = E.abs("x", E.app(E.var("f"), E.app(E.var("x"), E.var("x"))));
      final E f = E.abs("f", E.app(x, x));
      println(Es.show(f));
    }
  }
}