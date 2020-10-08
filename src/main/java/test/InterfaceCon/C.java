package test.InterfaceCon;

public class C {

    public static void main(String[] args) {
        Class b = test.InterfaceCon.B.class;
        C c = new C();
        String action = c.action(b);
        System.out.println(action);
    }

    public String action(Class<A> a){
        A ain = null;
        try {
            ain = a.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return ain.getA();
    }
}
