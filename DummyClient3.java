import lenz.htw.duktus.net.NetworkClient;

public class DummyClient3 {

    public static void main(String[] args) throws Exception {
       NetworkClient net = new NetworkClient("127.0.0.3", "test3", "test3");

     //   net.getMyPlayerNumber();

       // net.getStartX(0, 0);
      //  net.getStartY(0, 0);

        //while (net.isAlive()) {
            //Update upd;
            //while ((upd = net.pullNextUpdate()) != null) {
                // upd.player
                // upd.bot
                // upd.x
                // upd.y
            //}
            // Strategie für eigenen Zug
           // net.changeDirection(0, 0);
            // net.getAreaId(0, 0) == 0? -> anfangs vergebene Mauer
        //}
    }
}