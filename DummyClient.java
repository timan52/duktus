import lenz.htw.duktus.net.NetworkClient;
import lenz.htw.duktus.net.Update;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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
    static int[][] spaceInBetween;
    static int [][] map;
    static int b0d = 1;
    static int b1d = 1;
    static int b2d = 1;
    static int distanceToObstacleB0 = 9001;
    static int distanceToObstacleB1 = 9001;
    static int distanceToObstacleB2 = 9001;
    static boolean bot0Alive = true;
    static boolean bot1Alive = true;
    static boolean bot2Alive = true;
    static int pb0count=0;
    static int pb1count=0;
    static int pb2count=0;

    public static void main(String[] args) throws Exception {
        net = new NetworkClient("141.45.51.241", "wow", "wow");
        field = updateField();
        setPlayerNumbers();
        setInitialBotPositions();
        //writeImage(0);
        //MazeSolver maze = new MazeSolver(field);
        //System.out.println(maze.GridtoString());
        //boolean solved = maze.solve();
        //System.out.println("Solved: " + solved);
        //System.out.println(maze.toString());
        //writeMap(maze.getMap());

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
                    writeImage(count);
                    count++;
                }
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                Update upd;
                while (net.isAlive()) {
                    while ((upd = net.pullNextUpdate()) != null) {
                        updatePositions(upd.player,upd.bot,upd.x,upd.y);
                        if(pb0count+2<pb1count||pb0count+2<pb2count){
                            bot0Alive=false;
                        }
                        if(pb1count+2<pb0count||pb1count+2<pb2count){
                            bot1Alive=false;
                        }
                        if(pb2count+2<pb0count||pb2count+2<pb1count){
                            bot2Alive=false;
                        }
                    }
                }
            }
        });
        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {

                while (net.isAlive()) {
                    if (true) {
                        if (distanceToObstacleB0 <= 5) {
                            goLeft(0, distanceToObstacleB0);
                        }
                    }
                    if (true) {
                        if (distanceToObstacleB1 <= 5) {
                            goLeft(1, distanceToObstacleB1);
                        }
                    }
                    if (true) {
                        if (distanceToObstacleB2 <= 5) {
                            goLeft(2, distanceToObstacleB2);
                        }
                    }
                }
            }
        });
        Thread t4 = new Thread(new Runnable() {
            @Override
            public void run() {
                if (net.isAlive()) {
                    for (int i = 0; i > -1; i++) {
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        lookForObstacles();
                    }
                }
            }
        });
        //t1.start();
        t2.start();
        t3.start();
        t4.start();
    }
    private static void goLeft(int botNr, int coordinate){
        //System.out.println(coordinate);
        if(b0d==1){
            net.changeDirection(botNr,coordinate*-1);
            //System.out.println("goes up " + botNr);
            return;
        }
        if(b0d==2){
            net.changeDirection(botNr,coordinate*-1);
            //System.out.println("goes right "+ botNr);
            return;
        }
        if(b0d==3){
            net.changeDirection(botNr,coordinate*-1);
            //System.out.println("goes down "+ botNr);
            return;
        }
        if(b0d==4){
            net.changeDirection(botNr,coordinate*-1);
            //System.out.println("goes left "+ botNr);
            return;
        }
    }
    private static void goRight(int botNr, int coordinate){
        System.out.println(coordinate);
        if(b0d==1){
            net.changeDirection(botNr,coordinate);
            return;
        }
        if(b0d==2){
            net.changeDirection(botNr,coordinate);
            return;
        }
        if(b0d==3){
            net.changeDirection(botNr,coordinate);
            return;
        }
        if(b0d==4){
            net.changeDirection(botNr,coordinate);
            return;
        }
    }
    private static void setInitialBotPositions() {
        PB0sX = net.getStartX(playerNumber, 0);
        PB0sY = net.getStartY(playerNumber, 0);
        field[PB0sX][PB0sY]=255;
        PB1sX = net.getStartX(playerNumber, 1);
        PB1sY = net.getStartY(playerNumber, 1);
        field[PB1sX][PB1sY]=255;
        PB2sX = net.getStartX(playerNumber, 2);
        PB2sY = net.getStartY(playerNumber, 2);
        field[PB2sX][PB2sY]=255;
        prevPB0sX = PB0sX;
        prevPB0sY = PB0sY;
        prevPB1sX = PB1sX;
        prevPB1sY = PB1sY;
        prevPB2sX = PB2sX;
        prevPB2sY = PB2sY;
        E1B0sX = net.getStartX(enemyNumberOne, 0);
        E1B0sY = net.getStartY(enemyNumberOne, 0);
        field[E1B0sX][E1B0sY]=0;
        E1B1sX = net.getStartX(enemyNumberOne, 1);
        E1B1sY = net.getStartY(enemyNumberOne, 1);
        field[E1B1sX][E1B1sY]=0;
        E1B2sX = net.getStartX(enemyNumberOne, 2);
        E1B2sY = net.getStartY(enemyNumberOne, 2);
        field[E1B2sX][E1B2sY]=0;
        E2B0sX = net.getStartX(enemyNumberTwo, 0);
        E2B0sY = net.getStartY(enemyNumberTwo, 0);
        field[E2B0sX][E2B0sY]=0;
        E2B1sX = net.getStartX(enemyNumberTwo, 1);
        E2B1sY = net.getStartY(enemyNumberTwo, 1);
        field[E2B1sX][E2B1sY]=0;
        E2B2sX = net.getStartX(enemyNumberTwo, 2);
        E2B2sY = net.getStartY(enemyNumberTwo, 2);
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
        b0d = getBotDirection(PB0sX,PB0sY,prevPB0sX,prevPB0sY);
        b1d = getBotDirection(PB1sX,PB1sY,prevPB1sX,prevPB1sY);
        b2d = getBotDirection(PB2sX,PB2sY,prevPB2sX,prevPB2sY);
        if (player == playerNumber) {
            if (bot == 0) {
                pb0count++;
                prevPB0sX=PB0sX;
                prevPB0sY=PB0sY;
                PB0sX = x;
                PB0sY = y;
            }
            if (bot == 1) {
                pb1count++;
                prevPB1sX=PB1sX;
                prevPB1sY=PB1sY;
                PB1sX = x;
                PB1sY = y;
            }
            if (bot == 2) {
                pb2count++;
                prevPB2sX=PB2sX;
                prevPB2sY=PB2sY;
                PB2sX = x;
                PB2sY = y;
            }
            field[x][y]=255;
        }
        if (player == enemyNumberOne) {
            if (bot == 0) {
                E1B0sX = x;
                E1B0sY = y;
            }
            if (bot == 1) {
                E1B1sX = x;
                E1B1sY = y;
            }
            if (bot == 2) {
                E1B2sX = x;
                E1B2sY = y;
            }
            field[x][y]=0;
        }
        if (player == enemyNumberTwo) {
            if (bot == 0) {
                E2B0sX = x;
                E2B0sY = y;
            }
            if (bot == 1) {
                E2B1sX = x;
                E2B1sY = y;
            }
            if (bot == 2) {
                E2B2sX = x;
                E2B2sY = y;
            }
            field[x][y]=0;
        }
    }
    static void writeImage(int Name) {
        String path = Name + ".png";
        BufferedImage image = new BufferedImage(256, 256, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < 255; x++) {
            for (int y = 0; y < 255; y++) {
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
    static void writeMap(int [][] map) {
        String path = "map.png";
        BufferedImage image = new BufferedImage(256, 256, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < 255; x++) {
            for (int y = 0; y < 255; y++) {
                image.setRGB(x, y, map[x][y]);
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
        int PB0D = b0d;
        int PB1D = b1d;
        int PB2D = b2d;
        int tempPB0sX = PB0sX;
        int tempPB0sY = PB0sY;
        int tempPB1sX = PB1sX;
        int tempPB1sY = PB1sY;
        int tempPB2sX = PB2sX;
        int tempPB2sY = PB2sY;

        if(true){
            for (int i = 1; i < 25; i++) {
                if (PB0D == 1) {
                    if(tempPB0sX + i + 1>=255){
                        distanceToObstacleB0=i;
                        break;}
                    if (field[tempPB0sX + i + 1][tempPB0sY] == 0 || field[tempPB0sX + i+ 1][tempPB0sY] == 255) {
                        distanceToObstacleB0=i;
                        break;
                    }
                }
                if (PB0D == 2) {
                    if(tempPB0sY + i + 1>=255){
                        distanceToObstacleB0=i;
                        break;
                    }
                    if (field[tempPB0sX][tempPB0sY + i+ 1] == 0 || field[tempPB0sX][tempPB0sY + i+ 1] == 255) {
                        distanceToObstacleB0=i;
                        break;
                    }
                }
                if (PB0D == 3) {
                    if(tempPB0sX-i-1<=0){
                        distanceToObstacleB0=i;
                        break;
                    }
                    if (field[tempPB0sX - i-1][tempPB0sY] == 0 || field[tempPB0sX - i-1][tempPB0sY] == 255) {
                        distanceToObstacleB0=i;
                        break;
                    }
                }
                if (PB0D == 4) {
                    if(tempPB0sY-i-1<=0){
                        distanceToObstacleB0=i;
                        break;
                    }
                    if (field[tempPB0sX][tempPB0sY - i-1] == 0 || field[tempPB0sX][tempPB0sY - i-1] == 255) {
                        distanceToObstacleB0=i;
                        break;
                    }
                }
                distanceToObstacleB0=9001;
            }
        }
        if(true){
            for (int i = 0; i < 25; i++) {
                if (PB1D == 1) {
                    if(tempPB1sX + i + 1>=255){
                        distanceToObstacleB1=9001;
                        break;}
                    if (field[tempPB1sX + i + 1][tempPB1sY] == 0 || field[tempPB1sX + i+ 1][tempPB1sY] == 255) {
                        distanceToObstacleB1=i;
                        break;
                    }
                }
                if (PB1D == 2) {
                    if(tempPB1sY + i + 1>=255){
                        distanceToObstacleB1=9001;
                        break;
                    }
                    if (field[tempPB1sX][tempPB1sY + i+ 1] == 0 || field[tempPB1sX][tempPB1sY + i+ 1] == 255) {
                        distanceToObstacleB1=i;
                        break;
                    }
                }
                if (PB1D == 3) {
                    if(tempPB1sX-i-1<=0){
                        distanceToObstacleB1=9001;
                        break;
                    }
                    if (field[tempPB1sX - i-1][tempPB1sY] == 0 || field[tempPB1sX - i-1][tempPB1sY] == 255) {
                        distanceToObstacleB1=9001;
                        break;
                    }
                }
                if (PB1D == 4) {
                    if(tempPB1sY-i-1<=0){
                        distanceToObstacleB1=9001;
                        break;
                    }
                    if (field[tempPB1sX][tempPB1sY - i-1] == 0 || field[tempPB1sX][tempPB1sY - i-1] == 255) {
                        distanceToObstacleB1=9001;
                        break;
                    }
                }
                distanceToObstacleB1=9001;
            }
        }
        if(true){
            for (int i = 0; i < 25; i++) {
                if (PB2D == 1) {
                    if(tempPB2sX + i + 1>=255){
                        distanceToObstacleB2=i;
                        break;}
                    if (field[tempPB2sX + i + 1][tempPB2sY] == 0) {
                        distanceToObstacleB2=i;
                        break;
                    }
                }
                if (PB2D == 2) {
                    if(tempPB2sY + i + 1>=255){
                        distanceToObstacleB2=i;
                        break;
                    }
                    if (field[tempPB2sX][tempPB2sY + i+ 1] == 0) {
                        distanceToObstacleB2=i;
                        break;
                    }
                }
                if (PB2D == 3) {
                    if(tempPB2sX-i-1<=0){
                        distanceToObstacleB2=i;
                        break;
                    }
                    if (field[tempPB2sX - i-1][tempPB2sY] == 0) {
                        distanceToObstacleB2=i;
                        break;
                    }
                }
                if (PB2D == 4) {
                    if(tempPB2sY-i-1<=0){
                        distanceToObstacleB2=i;
                        break;
                    }
                    if (field[tempPB2sX][tempPB2sY - i-1] == 0) {
                        distanceToObstacleB2=i;
                        break;
                    }
                }
                distanceToObstacleB2=9001;
            }
        }

    }
    static int lookForObstacles(int bot, int botdirection){
        if(bot==0){
            int PB0D = botdirection;
            for (int i = 0; i < 10; i++) {
                if (PB0D == 1) {
                    if (field[PB0sX + i + 1][PB0sY] == 0 || field[PB0sX + i + 1][PB0sY] == 255) {
                        net.changeDirection(0, i * b0d);
                        b0d=b0d*-1;
                        return i;
                    }
                }
                if (PB0D == 2) {
                    if (field[PB0sX][PB0sY + i + 1] == 0 || field[PB0sX][PB0sY + i + 1] == 255) {
                        net.changeDirection(0, i * b0d);
                        b0d=b0d*-1;
                        return i;
                    }
                }
                if (PB0D == 3) {
                    if(PB0sX-i-1<0){
                        net.changeDirection(0, i * b0d);
                        b0d=b0d*-1;
                        return i;
                    }
                    if (field[PB0sX - i - 1][PB0sY] == 0 || field[PB0sX - i - 1][PB0sY] == 255) {
                        net.changeDirection(0, i * b0d);
                        b0d=b0d*-1;
                        return i;
                    }
                }
                if (PB0D == 4) {
                    if(PB0sY-i-1<0){
                        net.changeDirection(0, i * b0d);

                        b0d=b0d*-1;
                        return i;
                    }
                    if (field[PB0sX][PB0sY - i - 1] == 0 || field[PB0sX][PB0sY - i - 1] == 255) {
                        net.changeDirection(0, i * b0d);

                        b0d=b0d*-1;
                        return i;
                    }
                }
            }
        }
        if(bot==1){
            int PB1D = botdirection;
            for (int i = 0; i < 10; i++) {
                if (PB1D == 1) {
                    if (field[PB1sX + i + 1][PB1sY] == 0 || field[PB1sX + i + 1][PB1sY] == 255) {
                        net.changeDirection(1, i * b1d);
                        b1d=b1d*-1;
                        return i;
                    }
                }
                if (PB1D == 2) {
                    if (field[PB1sX][PB1sY + i + 1] == 0 || field[PB1sX][PB1sY + i + 1] == 255) {
                        net.changeDirection(1, i * b1d);
                        b1d=b1d*-1;
                        return i;
                    }
                }
                if (PB1D == 3) {
                    if(PB1sX-i-1<0){
                        net.changeDirection(1, i * b1d);
                        b1d=b1d*-1;
                        return i;
                    }
                    if (field[PB1sX - i - 1][PB1sY] == 0 || field[PB1sX - i - 1][PB1sY] == 255) {
                        net.changeDirection(1, i * b1d);
                        b1d=b1d*-1;
                        return i;
                    }
                }
                if (PB1D == 4) {
                    if(PB1sY-i-1<0){
                        net.changeDirection(1, i * b1d);
                        b1d=b1d*-1;

                        return i;
                    }
                    if (field[PB1sX][PB1sY - i - 1] == 0 || field[PB1sX][PB1sY - i - 1] == 255) {
                        net.changeDirection(1, i * b1d);

                        b1d=b1d*-1;
                        return i;
                    }
                }
            }
        }
        if(bot==2){
            int PB2D = botdirection;
            for (int i = 0; i < 10; i++) {
                if(PB2D==1){
                    if(field[PB2sX+i+1][PB2sY]==0||field[PB2sX+i+1][PB2sY]==255){
                        net.changeDirection(2,i * b2d );
                        b2d=b2d*-1;
                        return i;
                    }
                }
                if(PB2D==2){
                    if(field[PB2sX][PB2sY+i+1]==0||field[PB2sX][PB2sY+i+1]==255){
                        net.changeDirection(2,i * b2d );
                        b2d=b2d*-1;
                        return i;
                    }
                }
                if(PB2D==3){
                    if(PB2sX-i-1<0){
                        net.changeDirection(2, i * b2d);
                        b2d=b2d*-1;
                        return i;
                    }
                    if(field[PB2sX-i-1][PB2sY]==0||field[PB2sX-i-1][PB2sY]==255){
                        net.changeDirection(2,i * b2d );
                        b2d=b2d*-1;
                        return i;
                    }
                }
                if(PB2D==4){
                    if(PB2sY-i-1<0){
                        net.changeDirection(2, i * b2d);
                        b2d=b2d*-1;

                        return i;
                    }
                    if(field[PB2sX][PB2sY-i-1]==0||field[PB2sX][PB2sY-i-1]==255){
                        net.changeDirection(2,i * b2d );
                        b2d=b2d*-1;

                        return i;
                    }
                }
            }
        }
        return 10;
    }
    static int findObstacles(int bot, int botdirection){
        boolean isObstacle=false;
        int tempPB0sX=PB0sX;
        int count = 0;
        if(bot==0) {
            if(botdirection==1){
                for (int i = 1; i <255-tempPB0sX; i++) {
                    System.out.println("i: "+i);
                    if (field[tempPB0sX + i][PB0sY] == 0 || field[tempPB0sX + count][PB0sY] == 255) {
                        return i - 1;
                    }
                }
            }
            if(botdirection==2){
                while (!isObstacle) {
                    count++;
                    if(PB0sY + count>=255){return 1;}
                    if (field[PB0sX][PB0sY + count] == 0 || field[PB0sX][PB0sY+ count] == 255) {
                        return count - 1;
                    }
                }
            }
            if(botdirection==3){
                while (!isObstacle) {
                    count++;
                    if(PB0sX - count<=0){return 1;}
                    if (field[PB0sX-count][PB0sY] == 0 || field[PB0sX-count][PB0sY] == 255) {
                        return count - 1;
                    }
                }
            }
            if(botdirection==4){
                while (!isObstacle) {
                    count++;
                    if(PB0sY - count<=0){return 1;}
                    if (field[PB0sX][PB0sY-count] == 0 || field[PB0sX][PB0sY- count] == 255) {
                        return count - 1;
                    }
                }
            }
        }
        if(bot==1) {
            if(botdirection==1){
                while (!isObstacle) {
                    count++;
                    if(PB1sX + count>=255){return 1;}
                    if (field[PB1sX + count][PB1sY] == 0 || field[PB1sX + count][PB1sY] == 255) {
                        return count - 1;
//                        spaceToObstacle = count - 1;
//                        isObstacle = true;
                    }
                }
            }
            if(botdirection==2){
                while (!isObstacle) {
                    count++;
                    if(PB1sY + count>=255){return 1;}
                    if (field[PB1sX][PB1sY + count] == 0 || field[PB1sX][PB1sY+ count] == 255) {
                        return count - 1;
                    }
                }
            }
            if(botdirection==3){
                while (!isObstacle) {
                    count++;
                    if(PB1sX - count<=0){return 1;}
                    if (field[PB1sX-count][PB1sY] == 0 || field[PB1sX-count][PB1sY] == 255) {
                        return count - 1;
                    }
                }
            }
            if(botdirection==4){
                while (!isObstacle) {
                    count++;
                    if(PB1sY - count<=0){return 1;}
                    if (field[PB1sX][PB1sY-count] == 0 || field[PB1sX][PB1sY- count] == 255) {
                        return count - 1;
                    }
                }
            }
        }
        if(bot==2) {
            if(botdirection==1){
                while (!isObstacle) {
                    count++;
                    if(PB2sX + count>=255){return 1;}
                    if (field[PB2sX + count][PB2sY] == 0 || field[PB2sX + count][PB2sY] == 255) {
                        return count - 1;
//                        spaceToObstacle = count - 1;
//                        isObstacle = true;
                    }
                }
            }
            if(botdirection==2){
                while (!isObstacle) {
                    count++;
                    if(PB0sY + count>=255){return 1;}
                    if (field[PB2sX][PB2sY + count] == 0 || field[PB2sX][PB2sY+ count] == 255) {
                        return count - 1;
                    }
                }
            }
            if(botdirection==3){
                while (!isObstacle) {
                    count++;
                    if(PB2sX - count<=0){return 1;}
                    if (field[PB2sX-count][PB2sY] == 0 || field[PB2sX-count][PB2sY] == 255) {
                        return count - 1;
                    }
                }
            }
            if(botdirection==4){
                while (!isObstacle) {
                    count++;
                    if(PB2sY - count<=0){return 1;}
                    if (field[PB2sX][PB2sY-count] == 0 || field[PB2sX][PB2sY- count] == 255) {
                        return count - 1;
                    }
                }
            }
        }
        return 9001;
    }
    static int getBotDirection(int currPosX, int currPosY, int prevPosX, int prevPosY){
        if(currPosX-prevPosX>0 && currPosY-prevPosY==0){
            return 1; //right
        }
        if(currPosX-prevPosX==0 && currPosY-prevPosY>0) {
            return 2; //down
        }
        if(currPosX-prevPosX<0 && currPosY-prevPosY==0){
            return 3; //left
        }
        if(currPosX-prevPosX==0 && currPosY-prevPosY<0){
            return 4; //up
        }
        return 0;
    }
    static int[][] updateField(){
        int[][] field = new int[256][256];
        for (int i = 0; i < 255; i++) {
            for (int j = 0; j < 255; j++) {
                field[i][j]=net.getAreaId(i,j);
            }
        }

        for (int i = 0; i < 255; i++) {
            field[i][0]=0;
            field[i][255]=0;
            field[0][i]=0;
            field[255][i]=0;
        }
        return field;
    }
    static String whereToGO(){
        int Wallsleft=0;
        int Wallsright=0;
        for (int i = 0; i < 128; i++) {
            for (int j = 0; j < 256; j++) {
                if(field[i][j]==0){Wallsleft++;}
                if(field[i+128][j]==0){Wallsright++;}
            }

        }
        if(Wallsleft<Wallsright){
            return "goRight";
        }
        else {
            return "goLeft";
        }
    }
}