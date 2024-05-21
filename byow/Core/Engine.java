package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Out;
import edu.princeton.cs.algs4.StdDraw;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import java.awt.*;
import java.util.List;

import static byow.Core.MapGenerator.*;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    public static boolean gameOver;
    public static boolean coinGameOver;
    private static List<Position> posList = new ArrayList<>();
    private static long seed;
    private static long newMapSeed = 99876;
    private static int coinRoomTime = 10000;
    private static int visitedCount = 0;
    private static int worldCount = 0;
    private static int coinNum = 0;
    private int menuW = 40;
    private int menuH = 40;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        boolean flag = true;
        boolean finishFlag = true;
        char c;
        char finishChar;
        String numSeed = "";
        int moveIndex = 0;

        // Menu setting
        menuSet();

        // Menu tasks
        menuTaskSet();

        while(flag) {
            // Get a character to choose menu
            if (StdDraw.hasNextKeyTyped()) {
                c = StdDraw.nextKeyTyped();

                // Press "N"  game start
                if (c == 'N' || c == 'n') {
                    StdDraw.clear(Color.black);
                    StdDraw.text(menuW, menuH / 2, "Type your seed, then press 's' if you finished");
                    StdDraw.show();

                    while(finishFlag) {
                        if (StdDraw.hasNextKeyTyped()) {
                            finishChar = StdDraw.nextKeyTyped();
                            StdDraw.text((menuW -10) + moveIndex, menuH / 2 - 2, Character.toString(finishChar));
                            StdDraw.show();
                            moveIndex += 2;
                            if (finishChar >= 47 && finishChar <= 57){
                                numSeed += Character.toString(finishChar);
                            }
                            if (finishChar == 's' || finishChar == 'S') {
                                finishFlag = false;
                            }
                        }
                    }
                    seed =  parsingHelper(numSeed);
                    Map m = mapGenerate(seed);
                    ter.initialize(Engine.WIDTH, Engine.HEIGHT);
                    ter.renderFrame(m.mapTile());
                    gameStart(m);
                    break;
                }

                // Press 'L' Load game
                else if (c == 'L' || c == 'l') {
                    Map loadedMap = loadMap();
                    ter.initialize(WIDTH,HEIGHT);
                    worldCount = loadWorldCount().get(0);
                    visitedCount = loadWorldCount().get(1);

                    System.out.println("worldCount" + worldCount);
                    if (worldCount == 0) {
                        for (int i = 0; i < loadVisitedCoinRoom0().size(); i += 1) {
                            loadedMap.mapTile()[loadVisitedCoinRoom0().get(i).x][loadVisitedCoinRoom0().get(i).y] = Tileset.TREE;
                        }
                    } else if (worldCount == 1) {
                        for (int i = 0; i < loadVisitedCoinRoom1().size(); i += 1) {
                            if (loadedMap.mapTile()[loadVisitedCoinRoom0().get(i).x][loadVisitedCoinRoom0().get(i).y] == Tileset.FLOOR) {
                                loadedMap.mapTile()[loadVisitedCoinRoom0().get(i).x][loadVisitedCoinRoom0().get(i).y] = Tileset.FLOOR;
                            } else if (loadedMap.mapTile()[loadVisitedCoinRoom0().get(i).x][loadVisitedCoinRoom0().get(i).y] == Tileset.WALL) {
                                loadedMap.mapTile()[loadVisitedCoinRoom0().get(i).x][loadVisitedCoinRoom0().get(i).y] = Tileset.WALL;
                            }
                            loadedMap.mapTile()[loadVisitedCoinRoom1().get(i).x][loadVisitedCoinRoom1().get(i).y] = Tileset.TREE;
                        }
                    } else if (worldCount == 2) {
                        for (int i = 0; i < loadVisitedCoinRoom2().size(); i += 1) {
                            if (loadedMap.mapTile()[loadVisitedCoinRoom1().get(i).x][loadVisitedCoinRoom1().get(i).y] == Tileset.FLOOR) {
                                loadedMap.mapTile()[loadVisitedCoinRoom1().get(i).x][loadVisitedCoinRoom1().get(i).y] = Tileset.FLOOR;
                            } else if (loadedMap.mapTile()[loadVisitedCoinRoom1().get(i).x][loadVisitedCoinRoom1().get(i).y] == Tileset.WALL) {
                                loadedMap.mapTile()[loadVisitedCoinRoom1().get(i).x][loadVisitedCoinRoom1().get(i).y] = Tileset.WALL;
                            }
                            loadedMap.mapTile()[loadVisitedCoinRoom2().get(i).x][loadVisitedCoinRoom2().get(i).y] = Tileset.TREE;
                        }
                    }

                    loadedMap.mapTile()[loadAvatar().x][loadAvatar().y] = Tileset.FLOOR;
                    loadedMap.mapTile()[loadedMap.getAvatarP().x][loadedMap.getAvatarP().y] = Tileset.AVATAR;
                    ter.renderFrame(loadedMap.mapTile());
                    gameOver = false;
                    gameStart(loadedMap);
                    break;
//
                }

                // Press 'Q' quit game
                else if (c == 'q' || c == 'Q') {
                    gameOver = true;
                    drawGameEnd("The game is the END");
                    System.exit(0);
                    break;
                }
            }
        }

    }



    public void drawGameEnd(String s) {

        StdDraw.clear();
        Font fontBig = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(fontBig);
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(WIDTH / 2, HEIGHT / 2, s);
        StdDraw.show();
    }

    public void saveWorldCount(int wc, int cn) {
        String fileName = "saveWorldCount.txt";
        System.out.println("WritingAvatar to " + fileName);
        Out out = new Out(fileName);
        out.println(wc);
        out.println(cn);
    }
    public List<Integer> loadWorldCount() {
        String fileName = "saveWorldCount.txt";
        System.out.println("ReadingAvatar from " + fileName + ". Contents are:");
        In in = new In(fileName);
        String[] s = in.readAllLines();
        List<Integer> lst = new ArrayList<>();
        int wc = 0;
        int cn = 0;
        wc = Integer.parseInt(s[0]);
        cn = Integer.parseInt(s[1]);
        lst.add(wc);
        lst.add(cn);
        return lst;
    }

    public void saveVisitedCoinRoom0(List<Position> rl) {
        String fileName = "saveVisitedCoinRoom.txt";
        System.out.println("WritingAvatar to " + fileName);
        Out out = new Out(fileName);
        for (int i = 0; i < rl.size(); i += 1) {
            out.println(rl.get(i).x);
            out.println(rl.get(i).y);
        }
    }

    public void saveVisitedCoinRoom1(List<Position> rl) {
        String fileName = "saveVisitedCoinRoom1.txt";
        System.out.println("WritingAvatar to " + fileName);
        Out out = new Out(fileName);
        for (int i = 0; i < rl.size(); i += 1) {
            out.println(rl.get(i).x);
            out.println(rl.get(i).y);
        }
    }


    public void saveVisitedCoinRoom2(List<Position> rl) {
        String fileName = "saveVisitedCoinRoom2.txt";
        System.out.println("WritingAvatar to " + fileName);
        Out out = new Out(fileName);
        for (int i = 0; i < rl.size(); i += 1) {
            out.println(rl.get(i).x);
            out.println(rl.get(i).y);
        }
    }


    public List<Position> loadVisitedCoinRoom0() {
        String fileName = "saveVisitedCoinRoom.txt";
        System.out.println("saveVisitedCoinRoom from " + fileName + ". Contents are:");
        In in = new In(fileName);
        String[] s = in.readAllLines();
        List<Position> returnList = new ArrayList<>();
        int xPos = 0;
        int yPos = 0;
        for (int i = 0; i < s.length - 1; i += 2) {
            xPos = Integer.parseInt(s[i]);
            yPos = Integer.parseInt(s[i + 1]);
            returnList.add(new Position(xPos, yPos));
        }
        if (in.exists()) {
            return returnList;
        }
        else {
            return null;
        }
    }


    public List<Position> loadVisitedCoinRoom1() {
        String fileName = "saveVisitedCoinRoom1.txt";
        System.out.println("saveVisitedCoinRoom1 from " + fileName + ". Contents are:");
        In in = new In(fileName);
        String[] s = in.readAllLines();
        List<Position> returnList = new ArrayList<>();
        int xPos = 0;
        int yPos = 0;
        for (int i = 0; i < s.length - 1; i += 2) {
            xPos = Integer.parseInt(s[i]);
            yPos = Integer.parseInt(s[i + 1]);
            returnList.add(new Position(xPos, yPos));
        }
        if (in.exists()) {
            return returnList;
        }
        else {
            return null;
        }
    }


    public List<Position> loadVisitedCoinRoom2() {
        String fileName = "saveVisitedCoinRoom2.txt";
        System.out.println("saveVisitedCoinRoom2 from " + fileName + ". Contents are:");
        In in = new In(fileName);
        String[] s = in.readAllLines();
        List<Position> returnList = new ArrayList<>();
        int xPos = 0;
        int yPos = 0;
        for (int i = 0; i < s.length - 1; i += 2) {
            xPos = Integer.parseInt(s[i]);
            yPos = Integer.parseInt(s[i + 1]);
            returnList.add(new Position(xPos, yPos));
        }
        if (in.exists()) {
            return returnList;
        }
        else {
            return null;
        }
    }

    public void saveAvatar(Map m) {
        String fileName = "saveAvatar.txt";
        System.out.println("WritingAvatar to " + fileName);
        Out out = new Out(fileName);
        out.println(m.getAvatarP().x);
        out.println(m.getAvatarP().y);
    }
    public Position loadAvatar() {
        String fileName = "saveAvatar.txt";
        System.out.println("ReadingAvatar from " + fileName + ". Contents are:");
        In in = new In(fileName);
        String[] s = in.readAllLines();
        int xPos = 0;
        int yPos = 0;

        xPos = Integer.parseInt(s[0]);
        yPos = Integer.parseInt(s[1]);

        if (in.exists()) {
            return new Position(xPos, yPos);
        }
        else {
            return null;
        }
    }

    public void saveMap(Map m) {
        String fileName = "saveMap.txt";
        System.out.println("Writing to " + fileName);
        Out out = new Out(fileName);
        out.println(m.getAvatarP().x);
        out.println(m.getAvatarP().y);
        out.println(m.getSeed());
        out.println(m.getPosList().get(0).x);
        out.println(m.getPosList().get(0).y);
        out.println(m.getPosList().get(1).x);
        out.println(m.getPosList().get(1).y);
    }
    public Map loadMap() {
        String fileName = "saveMap.txt";
        System.out.println("Reading from " + fileName + ". Contents are:");

        In in = new In(fileName);
        String[] s = in.readAllLines();
        int xPos = 0;
        int yPos = 0;
        long loadSeed = 0;
        int coinPosX = 0;
        int coinPosY = 0;
        int coinPosX1 = 0;
        int coinPosY1 = 0;

        xPos = Integer.parseInt(s[0]);
        yPos = Integer.parseInt(s[1]);
        loadSeed = Long.parseLong(s[2]);
        coinPosX = Integer.parseInt(s[3]);
        coinPosY = Integer.parseInt(s[4]);
        coinPosX1 = Integer.parseInt(s[5]);
        coinPosY1 = Integer.parseInt(s[6]);
        Position coinRoom1 = new Position(coinPosX, coinPosY);
        Position coinRoom2 = new Position(coinPosX1, coinPosY1);
        List<Position> coinRoomList = new ArrayList<>();
        coinRoomList.add(coinRoom1);
        coinRoomList.add(coinRoom2);


        if (in.exists()) {
            Map returnMap = new Map(loadMapGenerate(new Position(xPos, yPos), loadSeed).mapTile(), new Position(xPos, yPos),
                    coinRoomList, loadSeed + 231);
            return returnMap;
        }
        else {
            return mapGenerate(seed);
        }
    }

    public long parsingHelper(String numSeed) {
        long seedLong = Long.parseLong(numSeed);
        return seedLong;
    }

    public void menuSet() {
        StdDraw.setCanvasSize(menuW * 16, menuH * 16);
        Font font = new Font("Monaco", Font.BOLD, 40);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(menuW, menuW / 2, "Maze Escape");
    }

    public void menuTaskSet() {
        Font smallFont = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(smallFont);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(menuW, menuW / 2 - 10, "New Game (N)");
        StdDraw.text(menuW, menuW / 2 - 11, "Load Game (L)");
        StdDraw.text(menuW, menuW / 2 - 12, "Quit (Q)");
        StdDraw.show();
        StdDraw.enableDoubleBuffering();
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, running both of these:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public static TETile[][] interactWithInputString(String input) {
        // TODO: Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.

        String blindInput = input.toUpperCase();
        int indexSave = 0;
//        long seed = 0;
        boolean parsing = false;

//        if (blindInput.charAt(0) != 'N') {
//            throw new IllegalArgumentException("Start your seed with N for New World!");
//        } else if (blindInput.charAt(blindInput.length() - 1) != 'S') {
//            throw new IllegalArgumentException("End your seed with S for Stop!");
//        } else {
//            for (int i = 0; i < blindInput.length(); i++) {
//                if (blindInput.charAt(i) == 'N') {
//                    parsing = true;
//                } else if (blindInput.charAt(i) == 'S') {
//                    parsing = false;
//                } else if (Character.isDigit(blindInput.charAt(i))) {
//                    seed = seed * 10 + Character.getNumericValue(blindInput.charAt(i));
//                }
//                if (!parsing) {
//                    indexSave = i;
//                    break;
//                }
//            }
//        }
//        return inputStringGame(input);
        return null;


        // for phase 2, this part of the method should begin parsing through the input string
        // again, but this time starting at the indexSave spot.
        //
        // depending on what letters are at index position i, (such as WASD), the generated map
        // should move the avatar into place.
//        return mapGenerate(seed).mapTile();
    }

    /**
     * @SOURCE from https://www.javatpoint.com/java-date-to-string
     * @param
     */
    public void HUDtime() {
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String strDate = dateFormat.format(date);
        StdDraw.setPenColor(Color.PINK);
        StdDraw.text(WIDTH - 5,HEIGHT - 0.5, strDate);
    }

    public void HUDcoin() {
        StdDraw.setPenColor(Color.ORANGE);
        StdDraw.text(35,HEIGHT - 0.5, "Coin: " + coinNum);
    }

    public void HUDworld() {
        StdDraw.setPenColor(new Color(135,206,235));
        StdDraw.text(45,HEIGHT - 0.5, "World: " + worldCount);
    }

    public void HUDmethod(Map m) {
        int mouseX = (int) StdDraw.mouseX();
        int mouseY = (int) StdDraw.mouseY();

        if ( mouseY > HEIGHT - 1) {
            ter.renderFrame(m.mapTile());
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.text(2.5,HEIGHT - 0.5, "Out of Map");
            HUDcoin();
            HUDtime();
            HUDworld();
            StdDraw.show();
        }
        else if (m.mapTile()[mouseX][mouseY] == (Tileset.WALL) ||
                m.mapTile()[mouseX][mouseY] == (Tileset.WALL2)) {
            ter.renderFrame(m.mapTile());
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.text(2,HEIGHT - 0.5, "Wall");
            HUDcoin();
            HUDtime();
            HUDworld();
            StdDraw.show();
        } else if (m.mapTile()[mouseX][mouseY] == (Tileset.FLOOR)){
            ter.renderFrame(m.mapTile());
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.text(2,HEIGHT - 0.5, "Floor");
            HUDcoin();
            HUDtime();
            HUDworld();
            StdDraw.show();
        } else if (m.mapTile()[mouseX][mouseY] == (Tileset.LOCKED_DOOR)) {
            ter.renderFrame(m.mapTile());
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.text(3,HEIGHT - 0.5, "Locked Door");
            HUDcoin();
            HUDtime();
            HUDworld();
            StdDraw.show();
        } else if (m.mapTile()[mouseX][mouseY] == (Tileset.AVATAR)) {
            ter.renderFrame(m.mapTile());
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.text(2,HEIGHT - 0.5, "Avatar");
            HUDcoin();
            HUDtime();
            HUDworld();
            StdDraw.show();
        } else if (m.mapTile()[mouseX][mouseY] == (Tileset.COIN)) {
            ter.renderFrame(m.mapTile());
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.text(2,HEIGHT - 0.5, "Coin");
            HUDcoin();
            HUDtime();
            HUDworld();
            StdDraw.show();
        }
        else if (m.mapTile()[mouseX][mouseY] == (Tileset.FLOWER)) {
            ter.renderFrame(m.mapTile());
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.text(2.5,HEIGHT - 0.5, "Coin Room");
            HUDcoin();
            HUDtime();
            HUDworld();
            StdDraw.show();
        } else if (m.mapTile()[mouseX][mouseY] == (Tileset.TREE)) {
            ter.renderFrame(m.mapTile());
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.text(4.5,HEIGHT - 0.5, "Visited Coin Room");
            HUDcoin();
            HUDtime();
            HUDworld();
            StdDraw.show();
        }
        else {
            ter.renderFrame(m.mapTile());
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.text(2,HEIGHT - 0.5, "Nothing");
            HUDcoin();
            HUDtime();
            HUDworld();
            StdDraw.show();
        }
    }

    public Map MoveCoinRoom(Map m, char moveKey) {
            Position p = m.getAvatarP();
            if (moveKey == 'w' || moveKey == 'W') {
                if (m.mapTile()[p.x][p.y + 1] == Tileset.COIN) {
                    coinNum += 1;
                    m.mapTile()[p.x][p.y + 1] = Tileset.AVATAR;
                    m.mapTile()[p.x][p.y] = Tileset.FLOOR;
                    Position updatedPos = new Position(p.x, p.y + 1);
                    Map updateMap = new Map(m.mapTile(), updatedPos, m.getPosList(),m.getSeed());
                    return updateMap;
                }
                if (m.mapTile()[p.x][p.y + 1] == Tileset.FLOOR) {
                    m.mapTile()[p.x][p.y + 1] = Tileset.AVATAR;
                    m.mapTile()[p.x][p.y] = Tileset.FLOOR;
                    Position updatedPos = new Position(p.x, p.y + 1);
                    Map updateMap = new Map(m.mapTile(), updatedPos, m.getPosList(),m.getSeed());
                    return updateMap;
                }
            } else if (moveKey == 's' || moveKey == 'S') {
                if (m.mapTile()[p.x][p.y - 1] == Tileset.COIN) {
                    coinNum += 1;
                    m.mapTile()[p.x][p.y - 1] = Tileset.AVATAR;
                    m.mapTile()[p.x][p.y] = Tileset.FLOOR;
                    Position updatedPos = new Position(p.x, p.y - 1);
                    Map updateMap = new Map(m.mapTile(), updatedPos, m.getPosList(),m.getSeed());
                    return updateMap;
                }
                if (m.mapTile()[p.x][p.y - 1] == Tileset.FLOOR) {
                    m.mapTile()[p.x][p.y - 1] = Tileset.AVATAR;
                    m.mapTile()[p.x][p.y] = Tileset.FLOOR;
                    Position updatedPos = new Position(p.x, p.y - 1);
                    Map updateMap = new Map(m.mapTile(), updatedPos, m.getPosList(), m.getSeed());
                    return updateMap;
                }
            } else if (moveKey == 'd' || moveKey == 'D') {
                if (m.mapTile()[p.x + 1][p.y] == Tileset.COIN) {
                    coinNum += 1;
                    m.mapTile()[p.x + 1][p.y] = Tileset.AVATAR;
                    m.mapTile()[p.x][p.y] = Tileset.FLOOR;
                    Position updatedPos = new Position(p.x + 1, p.y);
                    Map updateMap = new Map(m.mapTile(), updatedPos, m.getPosList(),m.getSeed());
                    return updateMap;
                }
                if (m.mapTile()[p.x + 1][p.y] == Tileset.FLOOR) {
                    m.mapTile()[p.x + 1][p.y] = Tileset.AVATAR;
                    m.mapTile()[p.x][p.y] = Tileset.FLOOR;
                    Position updatedPos = new Position(p.x + 1, p.y);
                    Map updateMap = new Map(m.mapTile(), updatedPos, m.getPosList(), m.getSeed());
                    return updateMap;
                }
            } else if (moveKey == 'a' || moveKey == 'A') {
                if (m.mapTile()[p.x - 1][p.y] == Tileset.COIN) {
                    coinNum += 1;
                    m.mapTile()[p.x - 1][p.y] = Tileset.AVATAR;
                    m.mapTile()[p.x][p.y] = Tileset.FLOOR;
                    Position updatedPos = new Position(p.x - 1, p.y);
                    Map updateMap = new Map(m.mapTile(), updatedPos, m.getPosList(),m.getSeed());
                    return updateMap;
                }
                if (m.mapTile()[p.x - 1][p.y] == Tileset.FLOOR) {
                    m.mapTile()[p.x - 1][p.y] = Tileset.AVATAR;
                    m.mapTile()[p.x][p.y] = Tileset.FLOOR;
                    Position updatedPos = new Position(p.x - 1, p.y);
                    Map updateMap = new Map(m.mapTile(), updatedPos, m.getPosList(), m.getSeed());
                    return updateMap;
                }
            }
            return m;
    }

    public void endingGame() {
            drawGameEnd("The World is the END");
            StdDraw.pause(3000);
            drawGameEnd( "Total Coin: " + coinNum);
            StdDraw.pause(3000);
            if (coinNum >= 25) {
                drawWinGame( "Congratulation YOU WIN !");
                StdDraw.pause(3000);
            } else {
                drawLoseGame( "You lose... :(");
                StdDraw.pause(3000);
            }
            drawGameEnd( "See you... ");
            StdDraw.pause(3000);
            gameOver = true;
            System.exit(0);
    }

    public void encounterLockedDoor() {
        visitedCount = 0;
        worldCount += 1;
        newMapSeed += 10;
        coinRoomTime -= 2000;
        if (worldCount == 3) {
            endingGame();
        }
        Random random = new Random(newMapSeed);
        long rseed = random.nextLong(0,2131234);
        drawGameEnd("Moving into the Next World..");
        StdDraw.pause(1000);
        StdDraw.clear();
        drawGameEnd("World " + worldCount + " !");
        StdDraw.pause(800);
        StdDraw.clear();
        Map newMap = mapGenerate(rseed);
        ter.initialize(80,30);
        ter.renderFrame(newMap.mapTile());
        gameStart(newMap);
    }

    public void cannotMove(Map m) {

        StdDraw.clear();
        Font fontBig = new Font("LiHei Pro", Font.BOLD, 30);
        StdDraw.setFont(fontBig);
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "You need to visit        Coin Room");
        StdDraw.setPenColor(Color.green);
        StdDraw.text(WIDTH / 2 + 3, HEIGHT / 2, "ALL");
        StdDraw.show();

        StdDraw.pause(1500);
        StdDraw.clear();

        ter.initialize(80,30);
        ter.renderFrame(m.mapTile());
    }

    public void moveCoinRoom(Map m, Position p) {
        visitedCount += 1;
        posList.add(new Position(p.x, p.y));
        newMapSeed += 123;
        Random random = new Random(newMapSeed);
        Long newSeed = random.nextLong(0,newMapSeed);
        drawGameEnd(" WELCOME TO ENTER COIN ROOM ! ");
        StdDraw.pause(3000);
        StdDraw.clear();
        Map tempMap = new Map(drawNewWorld(newSeed, m.getPosList()).mapTile(),
                drawNewWorld(newSeed, m.getPosList()).getAvatarP(),
                drawNewWorld(newSeed, m.getPosList()).getPosList(),
                drawNewWorld(newSeed, m.getPosList()).getSeed() + 1234);
        ter.initialize(80,30);
        ter.renderFrame(drawNewWorld(newSeed, m.getPosList()).mapTile());
        CoinGameStart(tempMap, newSeed);
        m.mapTile()[p.x][p.y] = Tileset.TREE;

    }

    public Map MoveMethod(Map m, char moveKey) {
        Position p = m.getAvatarP();
        if (moveKey == 'w' || moveKey == 'W') {
            // if you go to locked Door, generate new map
            if (m.mapTile()[p.x][p.y + 1] == Tileset.LOCKED_DOOR){
                if (visitedCount == 2) {
                    encounterLockedDoor();
                } else {
                    cannotMove(m);
                }
            }
            // if you encounter Flower, you can go inside Coin Room
            if (m.mapTile()[p.x][p.y + 1] == Tileset.FLOWER){
                moveCoinRoom(m, new Position(p.x, p.y + 1));
            }

            if (m.mapTile()[p.x][p.y + 1] == Tileset.FLOOR) {
                m.mapTile()[p.x][p.y + 1] = Tileset.AVATAR;
                m.mapTile()[p.x][p.y] = Tileset.FLOOR;
                Position updatedPos = new Position(p.x, p.y + 1);
                Map updateMap = new Map(m.mapTile(), updatedPos, m.getPosList(),  m.getSeed());
                return updateMap;
            }
        } else if (moveKey == 's' || moveKey == 'S') {
            // if you go to locked Door, generate new map
            if (m.mapTile()[p.x][p.y - 1] == Tileset.LOCKED_DOOR){
                if (visitedCount == 2) {
                    encounterLockedDoor();
                } else {
                    cannotMove(m);
                }
            }

            // if you encounter Flower, you can go inside Coin Room
            if (m.mapTile()[p.x][p.y - 1] == Tileset.FLOWER){
                moveCoinRoom(m, new Position(p.x, p.y - 1));
            }

            if (m.mapTile()[p.x][p.y - 1] == Tileset.FLOOR) {
                m.mapTile()[p.x][p.y - 1] = Tileset.AVATAR;
                m.mapTile()[p.x][p.y] = Tileset.FLOOR;
                Position updatedPos = new Position(p.x, p.y - 1);
                Map updateMap = new Map(m.mapTile(), updatedPos, m.getPosList(), m.getSeed());
                return updateMap;
            }
        }  else if (moveKey == 'd' || moveKey == 'D') {
            // if you go to locked Door, generate new map
            if (m.mapTile()[p.x + 1][p.y] == Tileset.LOCKED_DOOR){
                if (visitedCount == 2) {
                    encounterLockedDoor();
                } else {
                    cannotMove(m);
                }
            }
            // if you encounter Flower, you can go inside Coin Room
            if (m.mapTile()[p.x + 1][p.y] == Tileset.FLOWER){
                moveCoinRoom(m, new Position(p.x + 1, p.y));
            }

            if (m.mapTile()[p.x + 1][p.y] == Tileset.FLOOR) {
                m.mapTile()[p.x + 1][p.y] = Tileset.AVATAR;
                m.mapTile()[p.x][p.y] = Tileset.FLOOR;
                Position updatedPos = new Position(p.x + 1, p.y);
                Map updateMap = new Map(m.mapTile(), updatedPos, m.getPosList(), m.getSeed());
                return updateMap;
            }
        }  else if (moveKey == 'a' || moveKey == 'A') {
            // if you go to locked Door, generate new map
            if (m.mapTile()[p.x - 1][p.y] == Tileset.LOCKED_DOOR){
                if (visitedCount == 2) {
                    encounterLockedDoor();
                } else {
                    cannotMove(m);
                }
            }
            // if you encounter Flower, you can go inside Coin Room
            if (m.mapTile()[p.x - 1][p.y] == Tileset.FLOWER){
                moveCoinRoom(m, new Position(p.x - 1, p.y));
            }

            if (m.mapTile()[p.x - 1][p.y] == Tileset.FLOOR) {
                m.mapTile()[p.x - 1][p.y] = Tileset.AVATAR;
                m.mapTile()[p.x][p.y] = Tileset.FLOOR;
                Position updatedPos = new Position(p.x - 1, p.y);
                Map updateMap = new Map(m.mapTile(), updatedPos, m.getPosList(), m.getSeed());
                return updateMap;
            }
        }
        return m;
    }

    public void drawWinGame(String s) {
        StdDraw.clear();
        Font fontBig = new Font("Geeza Pro", Font.BOLD, 50);
        StdDraw.setFont(fontBig);
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.YELLOW);
        StdDraw.text(WIDTH / 2, HEIGHT / 2, s);
        StdDraw.show();
    }

    public void drawLoseGame(String s) {
        StdDraw.clear();
        Font fontBig = new Font("Geeza Pro", Font.BOLD, 50);
        StdDraw.setFont(fontBig);
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.gray);
        StdDraw.text(WIDTH / 2, HEIGHT / 2, s);
        StdDraw.show();
    }

    public void CoinGameStart(Map coinMap, long seed){
        coinGameOver = false;
        char moveKey;
        var value = 0;
        var stTime = new Date().getTime();
        while (new Date().getTime() < stTime + 5000) {
            HUDmethod(coinMap);
            StdDraw.pause(10);
            if (StdDraw.hasNextKeyTyped()) {
                moveKey = StdDraw.nextKeyTyped();
                coinMap = MoveCoinRoom(coinMap, moveKey);

            }
            value ++;
        }

        drawGameEnd("Time Over! You got: " + coinNum +
                " Return to MAP");
        StdDraw.pause(100);
        StdDraw.clear();
        ter.initialize(80,30);
    }

    public void gameStart(Map m) {
        System.out.println("regame?");
        gameOver = false;
        char moveKey;
        String exitString = "";
        saveAvatar(m);
        List<Position> initList = new ArrayList<>();
        while (!gameOver) {
            HUDmethod(m);
            StdDraw.pause(10);
            if (StdDraw.hasNextKeyTyped()) {
                moveKey = StdDraw.nextKeyTyped();
                exitString += moveKey;
                m = MoveMethod(m, moveKey);

                // exit game
                for (int i = 0; i < exitString.length() - 1; i += 1) {
                    if (exitString.charAt(i) == ':' && exitString.charAt(i + 1) == 'q' || exitString.charAt(i + 1) == 'Q') {
                        saveMap(m);
                        if (worldCount == 0) {
                            saveVisitedCoinRoom0(posList);
                        } else if (worldCount == 1) {
                            saveVisitedCoinRoom1(posList);
                        } else if (worldCount == 2){
                            saveVisitedCoinRoom2(posList);
                        }

                        saveWorldCount(worldCount, visitedCount);
                        gameOver = true;
                    }
                }
            }
        }
        drawGameEnd("The game is the END");
        System.exit(0);
    }

}
