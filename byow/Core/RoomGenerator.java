package byow.Core;


import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


public class RoomGenerator {
    private int x;
    private int y;
    private int width;
    private int height;
    private Position hallPosition;
    public RoomGenerator(int pos_x, int pos_y, int w, int h,Position hp) {
        this.x = pos_x;
        this.y = pos_y;
        this.width = w;
        this.height = h;
        this.hallPosition = hp;
    }
    public int getX() {
        return this.x;
    }
    public int getY() {
        return this.y;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }


    public Position HallwayOut() {
        return this.hallPosition;
    }

    public static Position randomRoom(long seed, RoomGenerator roomGenerator) {
        Random random = new Random(seed);
        System.out.println("r x:" + roomGenerator.x);
        System.out.println("r y: " + roomGenerator.y);

        int ranX = random.nextInt(roomGenerator.x, roomGenerator.x + roomGenerator.width);
        int ranY = random.nextInt(roomGenerator.y, roomGenerator.y + roomGenerator.height);
        return new Position(ranX, ranY);
    }

}
