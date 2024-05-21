package byow.TileEngine;

import java.awt.Color;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 *
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 *
 * Ex:
 *      world[x][y] = Tileset.FLOOR;
 *
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 */

public class Tileset {
    public static final TETile AVATAR = new TETile('@', Color.white,new Color(4,7,32), "you");
    public static final TETile WALL = new TETile('■', new Color(102,102, 102), new Color(102,102, 102),
            "wall");

    public static final TETile WALL2 = new TETile('#', new Color(187, 37, 40), new Color (22, 91, 51),
            "wall2");
    public static final TETile FLOOR2 = new TETile('·', new Color(255, 255, 153), new Color(4,7,32),
            "floor2");
    public static final TETile FLOOR = new TETile('·', new Color(255, 255, 153), new Color(4,7,32),
            "floor");
    public static final TETile NOTHING = new TETile(' ', Color.black, Color.black, "nothing");
    public static final TETile GRASS = new TETile('"', Color.green, Color.black, "grass");
    public static final TETile WATER = new TETile('≈', Color.blue, Color.black, "water");
    public static final TETile FLOWER = new TETile('❀', Color.magenta, Color.pink, "flower");
    public static final TETile LOCKED_DOOR = new TETile('█', Color.orange, Color.black,
            "locked door");
    public static final TETile UNLOCKED_DOOR = new TETile('▢', Color.orange, Color.black,
            "unlocked door");
    public static final TETile SAND = new TETile('▒', Color.yellow, Color.black, "sand");
    public static final TETile MOUNTAIN = new TETile('▲', Color.gray, Color.black, "mountain");
    public static final TETile TREE = new TETile('♠', Color.green, Color.black, "tree");


    public static final TETile COIN = new TETile('●', new Color(255,215,0), new Color(4,7,32), "coin");
    public static final TETile royalLight = new TETile('·',Color.white ,new Color(0,0,128), "royal");
    public static final TETile medi = new TETile('·', Color.white, new Color(0,0,255), "medi");
}


