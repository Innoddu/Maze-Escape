package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


public class MapGenerator {
//    private static long seed = 2872229; // 2872229
//    private static Random random = new Random(seed);
    private static List<RoomGenerator> lst = new ArrayList<>();
    private static HashMap<Integer, Position> hashmap = new HashMap<>();
    private static List<Position> posList = new ArrayList<>();

    public MapGenerator() {
//        this.seed = seedNum;

    }


    /**
     * validGap method checks gap between each room that are randomly generated.
     * @param tiles world tile.
     * @param p which is start position of randomly generated room.
     * @param w which is the width of randomly generated room.
     * @param h which is the height of randomly generated room.
     * @return return false if the room overlap, otherwise true
     */
    public static boolean validGap(TETile[][] tiles, Position p, int w, int h) {
        boolean flag = true;
        // check overlap
        if (p.x + w + 3< Engine.WIDTH && p.y + h + 3< Engine.HEIGHT) {
            for (int dy = 0; dy < h; dy += 1) {
                if (tiles[p.x - 3][p.y + dy] != Tileset.NOTHING || tiles[p.x + w][p.y + dy] != Tileset.NOTHING ) {
                    flag = false;
                }
            }
            for (int dx = 0; dx < w; dx += 1) {
                if (tiles[p.x + dx][p.y - 3] != Tileset.NOTHING || tiles[p.x + dx][p.y + h] != Tileset.NOTHING){
                    flag = false;
                }
            }
        }
        return flag;
    }

    /**
     * validPosition method checks overlap rooms.
     * @param tiles world tile.
     * @param p which is start position of randomly generated room.
     * @param w which is the width of randomly generated room.
     * @param h which is the height of randomly generated room.
     * @return return false if the room overlap, otherwise true
     */
    public static boolean validPosition(TETile[][] tiles, Position p, int w, int h) {
        boolean flag = true;
        // check overlap
            for (int x = p.x; x < p.x + w; x++) {
                for (int y = p.y; y < p.y + h; y++) {
                    if (tiles[x][y] != Tileset.NOTHING) {
                        flag = false;
                    }
                }
            }
        return flag;
    }

    /**
     * drawRect method draws randomly generated rooms.
     * @param tiles world tile.
     * @param p which is start position of randomly generated room.
     * @param tile which is Tileset.WALL
     * @param w which is the width of randomly generated room.
     * @param h which is the height of randomly generated room.
     */
    public static void drawRect(TETile[][] tiles, Position p, TETile tile, int w, int h) {
        for (int dx = 0; dx < w; dx += 1) {
            tiles[p.x][p.y] = tile;
            for (int dy = 0; dy < h; dy += 1) {
                tiles[p.x + dx][p.y + dy] = tile;
            }
        }
    }

    /**
     * drawWall method is wrapping each room with wall.
     * @param tiles world tile.
     * @param p which is start position of randomly generated room.
     * @param tile which is Tileset.WALL
     * @param w which is the width of randomly generated room.
     * @param h which is the height of randomly generated room.
     */
    public static void drawWall(TETile[][] tiles, Position p, TETile tile, int w, int h) {
        for (int dy = 0; dy < h; dy += 1) {
            tiles[p.x][p.y + dy] = tile;
            tiles[p.x + w - 1][p.y + dy] = tile;
        }
        for (int dx = 0; dx < w; dx += 1) {
            tiles[p.x + dx][p.y] = tile;
            tiles[p.x + dx][p.y + h - 1] = tile;
        }

    }

    /**
     * removeWall method is randomly generate door in wall.
     * @param tiles which is world tile.
     * @param p which is start position of randomly generated room.
     * @param w which is the width of randomly generated room.
     * @param h which is the height of randomly generated room.
     * @return position of door
     */
    public static Position removeWall(TETile[][] tiles, Position p, int w, int h, long instanceSeed) {
        Random random = new Random(instanceSeed);
        int ranPosX = random.nextInt(p.x + 1, p.x + w - 1);
        int ranPosY = random.nextInt(p.y + 1, p.y + h - 1);
        int ranChoice = random.nextInt(4);
        if (ranChoice == 0) {
            tiles[ranPosX][p.y] = Tileset.FLOOR; // door
//            tiles[ranPosX][p.y - 1] = Tileset.FLOOR;  // door + 1
            return new Position(ranPosX, p.y);
        } else if (ranChoice == 1) {
            tiles[p.x][ranPosY] = Tileset.FLOOR;
//            tiles[p.x - 1][ranPosY] = Tileset.FLOOR;
            return new Position(p.x, ranPosY);
        } else if (ranChoice == 2) {
            tiles[ranPosX][p.y + h - 1] = Tileset.FLOOR;
//            tiles[ranPosX][p.y + h] = Tileset.FLOOR;
            return new Position(ranPosX, p.y + h - 1);
        } else {
            tiles[p.x + w - 1][ranPosY] = Tileset.FLOOR;
//            tiles[p.x + w][ranPosY] = Tileset.FLOOR;
            return new Position(p.x + w - 1, ranPosY);
        }

    }


    /**
     * drawWorld create random size and the random number of rooms with wrapped wall,
     * and remove the one of the wall which is hallOut.
     * Randomly generated room is stored in lst.
     * Each roomKey and hallOut is stored hashmap.
     * @param tiles
     */
    public static void drawWorld(TETile[][] tiles, long instanceSeed) {
        Random random = new Random(instanceSeed);
        int test = random.nextInt(10,100);
        RoomGenerator new_room;
        int roomKey = 0;

        double board_fullness = 0;
        double board_limit = 0.6;
        int room_area = 0;


        while (board_limit > board_fullness) {
            int randomWidth = random.nextInt(5, 15);
            int randomHeight = random.nextInt(5, 15);
            int posX = random.nextInt(3, Engine.WIDTH - randomWidth);
            int posY = random.nextInt(3, Engine.HEIGHT - randomHeight);
            Position pos = new Position(posX, posY);
            if (validPosition(tiles, pos, randomWidth, randomHeight)) {
                if (validGap(tiles, pos, randomWidth, randomHeight)) {
                    drawRect(tiles, pos, Tileset.FLOOR, randomWidth, randomHeight);
                    drawWall(tiles, pos, Tileset.WALL, randomWidth, randomHeight);
                    Position hallOut = removeWall(tiles, pos, randomWidth, randomHeight, instanceSeed);
                    new_room = new RoomGenerator(pos.x, pos.y, randomWidth, randomHeight, hallOut);
                    lst.add(new_room);
                    hashmap.put(roomKey, hallOut);
                    roomKey += 1;
                }
                room_area += randomHeight * randomWidth;
                board_fullness = room_area / (Engine.HEIGHT * Engine.WIDTH);
            }
            test -= 1;
        }
    }

    /**
     * @Source from lab11 skeleton code
     * fillBoardWithNothing method creates nothing tiles in the entire board.
     * @param tiles
     */
    public static void fillBoardWithNothing(TETile[][] tiles) {
        int height = tiles[0].length;
        int width = tiles.length;
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
    }

    /**
     *  mstList method has two list v1 and v2 which are each room(vertex).
     *  By calling drawTileMst, connect each room with Floor tile.
     *  By calling checkDoorClosed, check whether door close or not.
     * @param tiles tiles is world tile.
     * @param edgeList edgeList has edges that are connected each room.
     */
    public static void mstList(TETile[][] tiles, List<Edge> edgeList, long seed) {
        List<Integer> v1 = new ArrayList<>();
        List<Integer> v2 = new ArrayList<>();
        for (int i = 0; i < edgeList.size(); i += 1) {
            v1.add(edgeList.get(i).either());
            v2.add(edgeList.get(i).other(v1.get(i)));
        }

        for (int i = 0; i < v1.size() ; i += 1) {
            drawTileMst(tiles, v1.get(i), v2.get(i), seed);
        }

        // if room is only one, then remove hallway.
        if (edgeList.size() == 0) {
            Position p = lst.get(0).HallwayOut();
            tiles[p.x][p.y] = Tileset.WALL;
        }

        for (int i = 0; i < v1.size(); i += 1) {
            checkDoorClosed(tiles, v1.get(i), v2.get(i), Tileset.NOTHING);
        }
    }


    /**
     * sideWallY methods draw hallways' wall when hallway is the x direction.
     * @param tiles world tile.
     * @param x is start position.
     * @param x1 is destination.
     * @param y y is start position.
     * @param range the distance between x and x1 which is Math.abs(x1 - x)
     */
    public static void sideWallY(TETile[][] tiles, int x, int x1, int y, int range) {
        for (int dx = x - 1; dx <= x1 + 1; dx += 1) {
            if (tiles[dx][y + 1] == Tileset.NOTHING || tiles[dx][y + 1] == Tileset.WALL) {
                tiles[dx][y + 1] = Tileset.WALL;
            }
            if (tiles[dx][y - 1] == Tileset.NOTHING || tiles[dx][y - 1] == Tileset.WALL) {
                tiles[dx][y - 1] = Tileset.WALL;
            }
        }
        // check over step
        coverCheckY(tiles, new Position(x1 + 1, y ));
    }

    /**
     * sideWallX methods draw hallways' wall when hallway is the y direction.
     * @param tiles world tile.
     * @param x is start position.
     * @param y is start position.
     * @param y1 is destination.
     * @param range the distance between x and x1 which is Math.abs(y1 - y)
     */
    public static void sideWallX(TETile[][] tiles, int x, int y, int y1, int range) {
        for (int dy = y - 1; dy <= y1 + 1; dy += 1) {
            if (tiles[x + 1][dy] == Tileset.NOTHING || tiles[x + 1][dy] == Tileset.WALL) {
                tiles[x + 1][dy] = Tileset.WALL;
            }
            if (tiles[x - 1][dy] == Tileset.NOTHING || tiles[x - 1][dy] == Tileset.WALL) {
                tiles[x - 1][dy] = Tileset.WALL;
            }
        }

        coverCheckX(tiles, new Position(x, y1 + 1));
    }

    /**
     * coverCheckY methods check whether the position is over position or not
     * If that position is over the room, then set Nothing tiles.
     * @param tiles tiles is world tiles.
     * @param corPos corPos is the last of the wall position
     */
    public static void coverCheckY(TETile[][] tiles, Position corPos) {
        if (tiles[corPos.x][corPos.y] == Tileset.NOTHING) {
            tiles[corPos.x][corPos.y + 1] = Tileset.NOTHING;
            tiles[corPos.x][corPos.y - 1] = Tileset.NOTHING;
        }
    }

    /**
     * coverCheckX methods check whether the position is over position or not
     * If that position is over the room, then set Nothing tiles.
     * @param tiles tiles is world tiles.
     * @param corPos corPos is the last of the wall position
     */
    public static void coverCheckX(TETile[][] tiles, Position corPos) {
        if (tiles[corPos.x][corPos.y] == Tileset.NOTHING) {
            tiles[corPos.x + 1][corPos.y] = Tileset.NOTHING;
            tiles[corPos.x - 1][corPos.y] = Tileset.NOTHING;
        }
    }


    /**
     * checkDoorClosed checks whether door is closed or not.
     * if the door is open, then the door will be closed by wall.
     * @param tiles is world tile.
     * @param v1 is the room number.
     * @param v2 is the room number.
     * @param tile is Tileset.WALL.
     */
    public static void checkDoorClosed(TETile[][] tiles, int v1, int v2 , TETile tile) {
        Position room1_hallout = hashmap.get(v1);
        Position room2_hallout = hashmap.get(v2);
        int x = room1_hallout.x;
        int y = room1_hallout.y;
        int x1 = room2_hallout.x;
        int y1 = room2_hallout.y;
        if (tiles[x + 1][y] == tile || tiles[x - 1][y] == tile || tiles[x][y + 1] == tile || tiles[x][y - 1] == tile) {
            tiles[x][y] = Tileset.WALL;
        }
        if (tiles[x1 + 1][y1] == tile || tiles[x1 - 1][y1] == tile || tiles[x1][y1 + 1] == tile || tiles[x1][y1 - 1] == tile) {
            tiles[x1][y1] = Tileset.WALL;
        }
    }

    /**
     * drawTileMst draws the hallway to connect each room by Kruskal's algorithm.
     * The rooms are connected random direction x or y.
     * @param tiles is world tile.
     * @param v1 is vertex which is the first room.
     * @param v2 is vertex which is the second room.
     */
    public static void drawTileMst(TETile[][] tiles, int v1, int v2, long instanceSeed) {
        Random random = new Random(instanceSeed);
        Position room1_hallout = hashmap.get(v1);
        Position room2_hallout = hashmap.get(v2);

        Position smallerX = Position.comparePositionXS(room1_hallout,room2_hallout);
        Position largerX = Position.comparePositionXL(room1_hallout,room2_hallout);
        Position smallerY = Position.comparePositionYS(room1_hallout,room2_hallout);
        Position largerY = Position.comparePositionYL(room1_hallout,room2_hallout);

        int randChoice = random.nextInt(2);
        switch (randChoice) {
            case 0: {
                for (int dx = smallerX.x; dx < largerX.x; dx += 1) {
                    tiles[dx][smallerY.y] = Tileset.FLOOR;
                }
                sideWallY(tiles, smallerX.x, largerX.x, smallerY.y,  Math.abs(room1_hallout.y - room2_hallout.y));
                for (int dy = smallerY.y; dy < largerY.y; dy += 1) {
                    tiles[largerY.x][dy] = Tileset.FLOOR;
                }
                sideWallX(tiles, largerY.x, smallerY.y, largerY.y, Math.abs(room1_hallout.y - room2_hallout.y));
                break;
            }
            case 1: {
                for (int dy = smallerY.y; dy < largerY.y; dy += 1) {
                    tiles[smallerX.x][dy] = Tileset.FLOOR;
                }
                sideWallX(tiles, smallerX.x, smallerY.y, largerY.y, Math.abs(room1_hallout.y - room2_hallout.y));
                for (int dx = smallerX.x; dx < largerX.x; dx += 1) {
                    tiles[dx][largerX.y] = Tileset.FLOOR;
                }
                sideWallY(tiles, smallerX.x, largerX.x, largerX.y,  Math.abs(room1_hallout.y - room2_hallout.y));
                break;
            }
            default: {
            }
        }
    }

    /**
     * drawOutdoor method made outdoor space within random selected room.
     * @param tiles is world tile.
     * @param lst is stored room information.
     */
    public static void drawOutdoor(TETile[][] tiles, List<RoomGenerator> lst, long instanceSeed) {
        List<RoomGenerator> roomList = lst;
        Random random = new Random(instanceSeed);
        int room = random.nextInt(roomList.size());
        int x = roomList.get(room).getX();
        int width = roomList.get(room).getWidth();
        int y = roomList.get(room).getY();
        int height = roomList.get(room).getHeight();
        int randomChoice = random.nextInt(4);

        int ranX = random.nextInt(x + 1, x + width - 1);
        int ranY = random.nextInt(y + 1, y + height - 1);
        boolean flag = true;

        while (flag) {
            switch (randomChoice) {
                case 0: {
                    if (outdoorCheck(tiles, ranX, y)) {
                        tiles[ranX][y] = Tileset.LOCKED_DOOR;
                        flag = false;
                    } else {
                        randomChoice = random.nextInt(4);
                    }
                    break;
                }
                case 1: {
                    if (outdoorCheck(tiles, x, ranY)) {
                        tiles[x][ranY] = Tileset.LOCKED_DOOR;
                        flag = false;
                    } else {
                        randomChoice = random.nextInt(4);
                    }
                    break;
                }
                case 2: {
                    if (outdoorCheck(tiles, ranX, y + height - 1)) {
                        tiles[ranX][y + height - 1] = Tileset.LOCKED_DOOR;
                        flag = false;
                    } else {
                        randomChoice = random.nextInt(4);
                    }
                    break;
                }
                case 3: {
                    if (outdoorCheck(tiles, x + width - 1, ranY)) {
                        tiles[x + width - 1][ranY] = Tileset.LOCKED_DOOR;
                        flag = false;
                    } else {
                        randomChoice = random.nextInt(4);
                    }
                    break;
                }
            }
        }
    }


    public static Position randomAvatar(TETile[][] tiles, List<RoomGenerator> lst, long instanceSeed) {
        List<RoomGenerator> roomList = lst;
        Random random = new Random(instanceSeed);
        int room = random.nextInt(roomList.size());

        int x = roomList.get(room).getX();
        int width = roomList.get(room).getWidth();
        int y = roomList.get(room).getY();
        int height = roomList.get(room).getHeight();

        int ranX = random.nextInt(x, x + width - 1);
        int ranY = random.nextInt(y, y + height - 1);
        boolean flag = true;

        while (flag) {
            if (!outdoorCheck(tiles, ranX, ranY)) {
                tiles[ranX][ranY] = Tileset.AVATAR;
                flag = false;
            } else {
                ranX = random.nextInt(x, x + width - 1);
                ranY = random.nextInt(y, y + height - 1);
            }
        }

        return new Position(ranX, ranY);

    }

    /**
     * outdoorCheck method checks whether tiles[x][y] position is floor or not.
     * If the tiles is floor return false, otherwise return true
     * @param tiles tiles is a world tiles.
     * @param x position of x.
     * @param y position of y.
     * @return boolean true or false.
     */
    public static boolean outdoorCheck(TETile[][] tiles, int x, int y) {
        if (tiles[x][y] != Tileset.FLOOR) {
            return true;
        }
        return false;
    }

    public static boolean floorCehck(TETile[][] tiles, int x, int y) {
        if (tiles[x][y] == Tileset.FLOOR) {
            return true;
        }
        return false;
    }

    public static List<Position> treasureRoom(TETile[][] tiles, List<RoomGenerator> r, long seed) {
        Random random = new Random(seed + 100);
        Random random2 = new Random(seed + 434);

        int room = random.nextInt(r.size());
        int room2 = random2.nextInt(r.size());

        int x = r.get(room).getX();
        int width = r.get(room).getWidth();
        int y = r.get(room).getY();
        int height = r.get(room).getHeight();

        int x1 = r.get(room2).getX();
        int width1 = r.get(room2).getWidth();
        int y1 = r.get(room2).getY();
        int height1 = r.get(room2).getHeight();

        int ranX = random.nextInt(x + 1, x + width - 1);
        int ranY = random.nextInt(y + 1, y + height - 1);

        int ranX1 = random2.nextInt(x1 + 1, x1 + width1 - 1);
        int ranY1 = random2.nextInt(y1 + 1, y1 + height1 - 1);
        boolean flag = true;

        while (flag) {
            if (!outdoorCheck(tiles, ranX, ranY)) {
                tiles[ranX][ranY] = Tileset.FLOWER;
                tiles[ranX1][ranY1] = Tileset.FLOWER;
                flag = false;
            } else {
                ranX = random.nextInt(x + 1, x + width - 1);
                ranY = random.nextInt(y + 1, y + height - 1);
                ranX1 = random.nextInt(x1  +1, x1 + width1 - 1);
                ranY1 = random.nextInt(y1 + 1, y1 + height1 - 1);
            }
        }


        Position coinPos1 = new Position(ranX, ranY);
        Position coinPos2 = new Position(ranX1, ranY1);
        posList.add(coinPos1);
        posList.add(coinPos2);
        return posList;

    }


    public static Map drawNewWorld(long seed, List<Position> posList) {

        TETile[][] tiles = new TETile[Engine.WIDTH][Engine.HEIGHT];
        Random random = new Random(seed);

        int w = 40;
        int h = 15;
        int tempX = 20;
        int tempY = 5;
        int ranX = random.nextInt(tempX + 1, w);
        int ranY = random.nextInt(tempY + 1, h);

        int ranCoinX1 = random.nextInt(tempX + 1, tempX + w - 1);
        int ranCoinY1 = random.nextInt(tempY + 1, tempY + h - 1);

        int ranCoinX2 = random.nextInt(tempX + 1, tempX + w - 1);
        int ranCoinY2 = random.nextInt(tempY + 1, tempY + h - 1);

        int ranCoinX3 = random.nextInt(tempX + 1, tempX + w - 1);
        int ranCoinY3 = random.nextInt(tempY + 1, tempY + h - 1);

        int ranCoinX4 = random.nextInt(tempX + 1, tempX + w - 1);
        int ranCoinY4 = random.nextInt(tempY + 1, tempY + h - 1);

        int ranCoinX5 = random.nextInt(tempX + 1, tempX + w - 1);
        int ranCoinY5 = random.nextInt(tempY + 1, tempY + h - 1);


        Position p = new Position(ranX, ranY);

        Position coinPos1 = new Position(ranCoinX1, ranCoinY1);
        Position coinPos2 = new Position(ranCoinX2, ranCoinY2);
        Position coinPos3 = new Position(ranCoinX3, ranCoinY3);
        Position coinPos4 = new Position(ranCoinX4, ranCoinY4);
        Position coinPos5 = new Position(ranCoinX5, ranCoinY5);


        fillBoardWithNothing(tiles);


        for (int dx = 0; dx < w; dx += 1) {
            tiles[20][5] = Tileset.FLOOR;
            for (int dy = 0; dy < h; dy += 1) {
                tiles[20 + dx][5 + dy] = Tileset.FLOOR;
            }
        }

        for (int dy = 0; dy < h; dy += 1) {
            tiles[20][5 + dy] = Tileset.WALL2;
            tiles[20 + w - 1][5 + dy] = Tileset.WALL2;
        }
        for (int dx = 0; dx < w; dx += 1) {
            tiles[20 + dx][5] = Tileset.WALL2;
            tiles[20 + dx][5 + h - 1] = Tileset.WALL2;
        }

        tiles[p.x][p.y] = Tileset.AVATAR;
        tiles[coinPos1.x][coinPos1.y] = Tileset.COIN;
        tiles[coinPos2.x][coinPos2.y] = Tileset.COIN;
        tiles[coinPos3.x][coinPos3.y] = Tileset.COIN;
        tiles[coinPos4.x][coinPos4.y] = Tileset.COIN;
        tiles[coinPos5.x][coinPos5.y] = Tileset.COIN;

        Map m = new Map(tiles, p, posList, seed);
        return m;
    }


    public  static Map loadMapGenerate(Position avatarPos, long seed) {
        TETile[][] world = new TETile[Engine.WIDTH][Engine.HEIGHT];
        fillBoardWithNothing(world);
        drawWorld(world, seed);

        // Making EdgeWeightedGraph
        EdgeWeightedGraph ewg = new EdgeWeightedGraph(lst.size());

        // connect edge
        for (int i = 0; i < lst.size() - 1; i += 1) {
            Edge e = new Edge(i, i + 1, Position.pythagoreanTheorem(lst.get(i).HallwayOut(), lst.get(i + 1).HallwayOut()));
            ewg.addEdge(e);
        }
        // implement Kruskal algorithm
        KruskalMST kmst = new KruskalMST(ewg);
        mstList(world, kmst.mst, seed);

        // Generate random outdoor
        drawOutdoor(world, lst, seed);

        List<Position> coinRoomList;
        coinRoomList = treasureRoom(world, lst, seed);
        // Generate treasure
//        Position coinRoom = treasureRoom(world, lst, seed);

        Map m = new Map(world, avatarPos,coinRoomList, seed);
        return m;
    }

    public static void initializeMap() {
        List<RoomGenerator> r = new ArrayList<>();
        HashMap<Integer, Position> hm = new HashMap<>();
        lst = r;
        hashmap = hm;
    }

    /**
     * mapGenerator methods generates the world map.
     * @param seed seed is random seed that is provided by user.
     * @return return world map.
     */
    public static Map mapGenerate(long seed) {
        initializeMap();
        TETile[][] world = new TETile[Engine.WIDTH][Engine.HEIGHT];
        fillBoardWithNothing(world);
        drawWorld(world, seed);

        // Making EdgeWeightedGraph
        EdgeWeightedGraph ewg = new EdgeWeightedGraph(lst.size());

        // connect edge
        for (int i = 0; i < lst.size() - 1; i += 1) {
            Edge e = new Edge(i, i + 1, Position.pythagoreanTheorem(lst.get(i).HallwayOut(), lst.get(i + 1).HallwayOut()));
            ewg.addEdge(e);
        }
        // implement Kruskal algorithm
        KruskalMST kmst = new KruskalMST(ewg);
        mstList(world, kmst.mst, seed);
        // Generate random outdoor
        drawOutdoor(world, lst, seed);


        // Generate treasure
//        treasureRoom(world, lst, seed);

        // Generate random position Avatar
        Position avatarPos = randomAvatar(world, lst, seed);

        Map m = new Map(world, avatarPos, treasureRoom(world, lst, seed), seed);

        System.out.println("lst: " + lst);
        System.out.println("kmst: " + kmst);
        System.out.println("hashmap: " + hashmap);;
        return m;
//        return world;
    }






    // using the main method within MapGenerator to test the Engine
    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(Engine.WIDTH, Engine.HEIGHT);
        ter.renderFrame(Engine.interactWithInputString("N2872228SWW:q")); // 2872229, 3434
//        ter.renderFrame(Engine.interactWithInputString("L"));

    }

}

