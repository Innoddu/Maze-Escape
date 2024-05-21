package byow.Core;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.util.Random;

public class lab12 {
    /** The width of the window of this game. */
    private int width;
    /** The height of the window of this game. */
    private int height;
    /** The current round the user is on. */
    private int round;
    /** The Random object used to randomly generate Strings. */
    private Random rand;
    /** Whether or not the game is over. */
    private boolean gameOver;
    /** Whether or not it is the player's turn. Used in the last section of the
     * spec, 'Helpful UI'. */
    private boolean playerTurn;
    /** The characters we generate random Strings from. */
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    /** Encouraging phrases. Used in the last section of the spec, 'Helpful UI'. */
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
            "You got this!", "You're a star!", "Go Bears!",
            "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        long seed = Long.parseLong(args[0]);
        lab12 game = new lab12(40, 40, seed);
        game.startGame();
//        game.clearFrame();
//        game.drawFrame(game.generateRandomString(1));
//        game.flashSequence(game.generateRandomString(10));
    }

    public lab12(int width, int height, long seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        this.rand = new Random(seed);
    }

    public String generateRandomString(int n) {
        //TODO: Generate random string of letters of length n
        Random random = new Random();
        char saveChar;
        String randomString = "";

        for (int i = 0; i < n; i += 1) {
            int ranInt = random.nextInt(CHARACTERS.length);
            saveChar = CHARACTERS[ranInt];
            randomString += Character.toString(saveChar);
        }
        System.out.println(randomString);
        return randomString;
    }

    public void drawFrame(String s) {
        /* Take the input string S and display it at the center of the screen,
         * with the pen settings given below. */
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(fontBig);
        StdDraw.text(this.width / 2, this.height / 2, s);
        StdDraw.show();

        //TODO: If the game is not over, display encouragement, and let the user know if they
        // should be typing their answer or watching for the next round.
        if (this.gameOver) {
            StdDraw.text(this.width / 2, this.height / 2, s);
            StdDraw.show();
        } else {
            // draw each rounds at the top leftmost
            StdDraw.line(0, this.height - 2, this.width, this.height - 2);
            String roundString = "Round: ";
            Font fontSmall = new Font("Monaco", Font.BOLD, 20);
            StdDraw.setFont(fontSmall);
            StdDraw.text(3, this.height - 1, roundString + this.round);


            // draw type or watch
            String typeString = "Type!";
            String watchString = "Watch!";
            if (!this.playerTurn) {
                StdDraw.setFont(fontSmall);
                StdDraw.text(this.width / 2, this.height - 1, watchString);
                StdDraw.show();
            } else {
                StdDraw.setFont(fontSmall);
                StdDraw.text(this.width / 2, this.height - 1, typeString);
                StdDraw.show();
            }

            StdDraw.setFont(fontSmall);
            StdDraw.text(this.width - 7, this.height - 1, ENCOURAGEMENT[rand.nextInt(ENCOURAGEMENT.length)]);
            StdDraw.show();
        }
        StdDraw.show();
    }


    public void clearFrame() {
        StdDraw.clear(Color.BLACK);
    }

    public void flashSequence(String letters) {
        //TODO: Display each character in letters, making sure to blank the screen between letters
        for (int i = 0; i < letters.length(); i += 1) {
            drawFrame(Character.toString(letters.toString().charAt(i)));
            StdDraw.pause(1000);
            this.clearFrame();
            this.drawFrame("");
            StdDraw.pause(500);
        }
    }

    public String solicitNCharsInput(int n) {
        //TODO: Read n letters of player input
        String s = "";
        while (s.length() < n) {
            if (StdDraw.hasNextKeyTyped()) {
                s += StdDraw.nextKeyTyped();
                drawFrame(s);
                StdDraw.pause(250);
                clearFrame();
            }
        }
        return s;
    }

    // 1. keep track if game is over
    // 2. keep track round #
    // 3. keep track player turn
    // while:
    //    1) not player turn
    //          -> Display round : n in middle
    //          -> generate random string
    //          -> flashsequence
    //    2) yes player turn
    //          -> solicit() => return user string
    //          -> Test if use string  == randomString ?
    //                  -> continue
    //          -> else:
    //                  -> end game
    public void startGame() {
        //TODO: Set any relevant variables before the game starts
        this.gameOver = false;
        // initial game round
        this.round = 1;

        //TODO: Establish Engine loop
        while (!gameOver) {
//            drawFrame("You should implement this game!");
            this.playerTurn = false;
            this.drawFrame("Round: " + this.round);
            StdDraw.pause(1000);
            this.clearFrame();
            String saveRandomS = this.generateRandomString(this.round);
            this.flashSequence(saveRandomS);
            this.clearFrame();
            this.playerTurn = true;
            this.drawFrame("");

            String savePlayerS = "";
            savePlayerS = this.solicitNCharsInput(this.round);

//            String savePlayerS = this.solicitNCharsInput(this.round);

            if (savePlayerS.equals(saveRandomS)) {
                this.round += 1;
            } else {
                this.gameOver = true;
                this.drawFrame("Game Over! You made it to round: " + this.round);
            }

        }

    }
}
