package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.List;
import java.util.Random;

public class Position {

    public int x;
    public int y;
    public long SEED = 2873124;
    public Position avater;

//    private static Random RANDOM = new Random(SEED);
    Position(int x, int y) {
        this.x = x;
        this.y = y;
    }




    public Position shift(int dx, int dy) {
        return new Position(this.x + dx, this.y + dy);
    }

    public static Position randomPosition(long seed) {
        Random random = new Random(seed);
//        random.setSeed(System.currentTimeMillis());
        int randomX = random.nextInt(Engine.WIDTH);
        int randomY = random.nextInt(Engine.HEIGHT);
        return new Position(randomX,randomY);
    }

    public static double pythagoreanTheorem(Position a, Position b) {
        return Math.sqrt(Math.pow((Math.abs(a.x - b.x)), 2) + Math.pow((Math.abs(a.y - b.y)), 2));
    }
    public static Position comparePositionYL(Position p1, Position p2) {
        if (p1.y < p2.y) {
            return new Position(p2.x, p2.y);
        } else {
            return new Position(p1.x, p1.y);
        }
    }

    public static Position comparePositionYS(Position p1, Position p2) {
        if (p1.y < p2.y) {
            return new Position(p1.x, p1.y);
        } else {
            return new Position(p2.x, p2.y);
        }
    }

    public static Position comparePositionXL(Position p1, Position p2) {
        if (p1.x < p2.x) {
            return new Position(p2.x, p2.y);
        } else {
            return new Position(p1.x, p1.y);
        }
    }

    public static Position comparePositionXS(Position p1, Position p2) {
        if (p1.x < p2.x) {
            return new Position(p1.x, p1.y);
        } else {
            return new Position(p2.x, p2.y);
        }
    }
}
