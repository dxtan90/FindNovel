package test.testFanShe;

public class B extends AbstructA{
    @Override
    public String getA(Integer a) {
        return "实现了:"+a;
    }

    public String getB(){
        try {
            Class<B> b = (Class<B>) Class.forName("test.testFanShe.B");
            return b.newInstance().getA(1);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }return "";
    }
}
