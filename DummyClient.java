import lenz.htw.duktus.net.NetworkClient;
import lenz.htw.duktus.net.Update;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class DummyClient {

    static int PB0sX;
    static int PB0sY;
    static int PB1sX;
    static int PB1sY;
    static int PB2sX;
    static int PB2sY;
    static int E1B0sX;
    static int E1B0sY;
    static int E1B1sY;
    static int E1B1sX;
    static int E1B2sX;
    static int E1B2sY;
    static int E2B0sX;
    static int E2B0sY;
    static int E2B1sX;
    static int E2B1sY;
    static int E2B2sX;
    static int E2B2sY;
    static int prevPB0sX;
    static int prevPB0sY;
    static int prevPB1sX;
    static int prevPB1sY;
    static int prevPB2sX;
    static int prevPB2sY;
    static NetworkClient net;
    static int playerNumber;
    static int enemyNumberOne;
    static int enemyNumberTwo;
    static int[][] field;

    public static void main(String[] args) throws Exception {
        net = new NetworkClient("127.0.0.1", "test", "test");
        field = updateField();
        setPlayerNumbers();
        setInitialBotPositions();
        writeImage(0);

        //CountDownLatch latch = new CountDownLatch(1);
        Thread t1 = new Thread(new Runnable() {

            @Override
            public void run() {
                int count = 1;
                for(int i = 0; i < 5; i++){
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    System.out.println("Thread T1 : "+i);

                    //net.changeDirection(0,50 );
                    //net.changeDirection(1,50 );
                    //net.changeDirection(2,50 );
                    //updateField();
                    writeImage(count);
                    count++;

                    //System.out.println("X: " + PB0sX + " Y: " + PB0sY);
                    //System.out.println("X: " + PB1sX + " Y: " + PB1sY);
                    //System.out.println("X: " + PB2sX + " Y: " + PB2sY);
                }
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                //latch.await();
                //latch.countDown();

            }
        });
        t1.start();

        while (net.isAlive()) {
            Update upd;
            while ((upd = net.pullNextUpdate()) != null) {
                updatePositions(upd.player,upd.bot,upd.x,upd.y);
                lookForObstacles();
            }
            // Strategie für eigenen Zug
            // net.changeDirection(1,20 );
        }
    }
    private static void setInitialBotPositions() {
        PB0sX = net.getStartX(playerNumber, 0)+1;
        PB0sY = net.getStartY(playerNumber, 0)+1;
        field[PB0sX][PB0sY]=255;
        PB1sX = net.getStartX(playerNumber, 1)+1;
        PB1sY = net.getStartY(playerNumber, 1)+1;
        field[PB1sX][PB1sY]=255;
        PB2sX = net.getStartX(playerNumber, 2)+1;
        PB2sY = net.getStartY(playerNumber, 2)+1;
        field[PB2sX][PB2sY]=255;
        prevPB0sX = PB0sX;
        prevPB0sY = PB0sY;
        prevPB1sX = PB1sX;
        prevPB1sY = PB1sY;
        prevPB2sX = PB2sX;
        prevPB2sY = PB2sY;
        E1B0sX = net.getStartX(enemyNumberOne, 0)+1;
        E1B0sY = net.getStartY(enemyNumberOne, 0)+1;
        field[E1B0sX][E1B0sY]=0;
        E1B1sX = net.getStartX(enemyNumberOne, 1)+1;
        E1B1sY = net.getStartY(enemyNumberOne, 1)+1;
        field[E1B1sX][E1B1sY]=0;
        E1B2sX = net.getStartX(enemyNumberOne, 2)+1;
        E1B2sY = net.getStartY(enemyNumberOne, 2)+1;
        field[E1B2sX][E1B2sY]=0;
        E2B0sX = net.getStartX(enemyNumberTwo, 0)+1;
        E2B0sY = net.getStartY(enemyNumberTwo, 0)+1;
        field[E2B0sX][E2B0sY]=0;
        E2B1sX = net.getStartX(enemyNumberTwo, 1)+1;
        E2B1sY = net.getStartY(enemyNumberTwo, 1)+1;
        field[E2B1sX][E2B1sY]=0;
        E2B2sX = net.getStartX(enemyNumberTwo, 2)+1;
        E2B2sY = net.getStartY(enemyNumberTwo, 2)+1;
        field[E2B2sX][E2B2sY]=0;
    }
    private static void setPlayerNumbers() {
        playerNumber = net.getMyPlayerNumber();
        enemyNumberOne = 0;
        enemyNumberTwo = 0;

        if (playerNumber==0){
            enemyNumberOne=1;
            enemyNumberTwo=2;
        }
        if (playerNumber==1){
            enemyNumberOne=0;
            enemyNumberTwo=2;
        }
        if (playerNumber==2){
            enemyNumberOne=0;
            enemyNumberTwo=1;
        }
    }
    private static void updatePositions(int player, int bot, int x, int y) {
        if (player == playerNumber) {
            if (bot == 0) {
                prevPB0sX=PB0sX;
                prevPB0sY=PB0sY;
                PB0sX = x+1;
                PB0sY = y+1;
            }
            if (bot == 1) {
                prevPB1sX=PB1sX;
                prevPB1sY=PB1sY;
                PB1sX = x+1;
                PB1sY = y+1;
            }
            if (bot == 2) {
                prevPB2sX=PB2sX;
                prevPB2sY=PB2sY;
                PB2sX = x+1;
                PB2sY = y+1;
            }
            field[x+1][y+1]=255;
        }
        if (player == enemyNumberOne) {
            if (bot == 0) {
                E1B0sX = x+1;
                E1B0sY = y+1;
            }
            if (bot == 1) {
                E1B1sX = x+1;
                E1B1sY = y+1;
            }
            if (bot == 2) {
                E1B2sX = x+1;
                E1B2sY = y+1;
            }
            field[x+1][y+1]=0;
        }
        if (player == enemyNumberTwo) {
            if (bot == 0) {
                E2B0sX = x+1;
                E2B0sY = y+1;
            }
            if (bot == 1) {
                E2B1sX = x+1;
                E2B1sY = y+1;
            }
            if (bot == 2) {
                E2B2sX = x+1;
                E2B2sY = y+1;
            }
            field[x+1][y+1]=0;
        }
    }
    static void writeImage(int Name) {
        String path = Name + ".png";
        BufferedImage image = new BufferedImage(257, 257, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < 257; x++) {
            for (int y = 0; y < 257; y++) {
                image.setRGB(x, y, field[x][y]);
            }
        }

        File ImageFile = new File(path);
        try {
            ImageIO.write(image, "png", ImageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    static void lookForObstacles(){
        int newDirection = 1;
        int PB0D = getBotDirection(PB0sX,PB0sY,prevPB0sX,prevPB0sY);
        int PB1D = getBotDirection(PB1sX,PB1sY,prevPB1sX,prevPB1sY);
        int PB2D = getBotDirection(PB2sX,PB2sY,prevPB2sX,prevPB2sY);

        for (int i = 0; i < 10; i++) {
            if (PB0D == 1) {
                if (field[PB0sX + i + 1][PB0sY] == 0 || field[PB0sX + i + 1][PB0sY] == 255) {
                    net.changeDirection(0, i * newDirection);
                    break;
                }
            }
            if (PB0D == 2) {
                if (field[PB0sX][PB0sY + i + 1] == 0 || field[PB0sX][PB0sY + i + 1] == 255) {
                    net.changeDirection(0, i * newDirection);
                    break;
                }
            }
            if (PB0D == 3) {
                if (field[PB0sX - i - 1][PB0sY] == 0 || field[PB0sX - i - 1][PB0sY] == 255) {
                    net.changeDirection(0, i * newDirection);
                    break;
                }
            }
            if (PB0D == 4) {
                if (field[PB0sX][PB0sY - i - 1] == 0 || field[PB0sX][PB0sY - i - 1] == 255) {
                    net.changeDirection(0, i * newDirection);
                    break;
                }
            }
        }
        for (int i = 0; i < 10; i++) {
            if (PB1D == 1) {
                if (field[PB1sX + i + 1][PB1sY] == 0 || field[PB1sX + i + 1][PB1sY] == 255) {
                    net.changeDirection(1, i * newDirection);
                    break;
                }
            }
            if (PB1D == 2) {
                if (field[PB1sX][PB1sY + i + 1] == 0 || field[PB1sX][PB1sY + i + 1] == 255) {
                    net.changeDirection(1, i * newDirection);
                    break;
                }
            }
            if (PB1D == 3) {
                if (field[PB1sX - i - 1][PB1sY] == 0 || field[PB1sX - i - 1][PB1sY] == 255) {
                    net.changeDirection(1, i * newDirection);
                    break;
                }
            }
            if (PB1D == 4) {
                if (field[PB1sX][PB1sY - i - 1] == 0 || field[PB1sX][PB1sY - i - 1] == 255) {
                    net.changeDirection(1, i * newDirection);
                    break;
                }
            }
        }
        for (int i = 0; i < 10; i++) {
            if(PB2D==1){
                if(field[PB2sX+i+1][PB2sY]==0||field[PB2sX+i+1][PB2sY]==255){
                    net.changeDirection(2,i*newDirection );
                    break;
                }
            }
            if(PB2D==2){
                if(field[PB2sX][PB2sY+i+1]==0||field[PB2sX][PB2sY+i+1]==255){
                    net.changeDirection(2,i*newDirection );
                    break;
                }
            }
            if(PB2D==3){
                if(field[PB2sX-i-1][PB2sY]==0||field[PB2sX-i-1][PB2sY]==255){
                    net.changeDirection(2,i*newDirection );
                    break;
                }
            }
            if(PB2D==4){
                if(field[PB2sX][PB2sY-i-1]==0||field[PB2sX][PB2sY-i-1]==255){
                    net.changeDirection(2,i*newDirection );
                    break;
                }
            }
        }
    }
    static int getBotDirection(int currPosX, int currPosY, int prevPosX, int prevPosY){
        if(currPosX-prevPosX>0 && currPosY-prevPosY==0){
            System.out.println("right");
            return 1; //right
        }
        if(currPosX-prevPosX==0 && currPosY-prevPosY>0) {
            System.out.println("down");
            return 2; //down
        }
        if(currPosX-prevPosX<0 && currPosY-prevPosY==0){
            System.out.println("left");
            return 3; //left
        }
        if(currPosX-prevPosX==0 && currPosY-prevPosY<0){
            System.out.println("up");
            return 4; //up
        }
        return 0;
    }
    static int[][] updateField(){
        int[][] field = new int[258][258];
        for (int i = 1; i < 256; i++) {
            for (int j = 1; j < 256; j++) {
                field[i][j]=net.getAreaId(i,j);
            }
        }
        for (int i = 0; i < 257; i++) {
            field[i][0]=0;
            field[i][257]=0;
            field[0][i]=0;
            field[257][i]=0;
        }
        return field;
    }

}