package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.List;

public class Map {
    private TETile[][] tile;
    private Position avatarP;
    private long seed;
    private Position coinRoom;
    private List<Position> posList = new ArrayList<>();
    public Map(TETile[][] t, Position avatarPos, List<Position> posL, long seedNum) {
        this.tile = t;
        this.avatarP = avatarPos;
        this.seed = seedNum;
//        this.coinRoom = cr;
        this.posList = posL;
    }

    public List<Position> getPosList() {
        return this.posList;
    }
    public Position getCoinRoom() {
        return this.coinRoom;
    }

    public long getSeed() {
        return this.seed;
    }

    public TETile[][] mapTile() {
        return this.tile;
    }
    public Position getAvatarP() {return this.avatarP;}
}
