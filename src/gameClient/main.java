package gameClient;

public class main {
    public static void main(String[] a) {
        Ex2 ex2 = new Ex2();
        if(a.length!=0) {
            long id = Long.parseLong(a[0]);
            int level = Integer.parseInt(a[1]);
            Thread client = new Thread(new Ex2(id, level));
            client.start();
        }else
        ex2.Login();

    }
}
