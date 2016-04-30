package game;

import game.Spot;

public class Board {

    private Spot spot1;
    private Spot spot2;
    private Spot spot3;
    private Spot spot4;
    private Spot spot5;
    private Spot spot6;
    private Spot spot7;
    private Spot spot8;
    private Spot spot9;
    private Spot spot10;
    private Spot spot11;
    private Spot spot12;
    private Spot spot13;
    private Spot spot14;
    private Spot spot15;
    private Spot spot16;
    private Spot spot17;
    private Spot spot18;
    private Spot[][] board = new Spot[6][6];

    private boolean capture = false;


    public Board() {

        this.spot1 = new Spot("s1", 3, "b1");
        this.spot2 = new Spot("s2", 3, "b2");
        this.spot3 = new Spot("s3", 3, "b3");
        this.spot4 = new Spot("s4", 3, "b4");
        this.spot5 = new Spot("s5", 2, "b5");
        this.spot6 = new Spot("s6", 2, "b6");
        this.spot7 = new Spot("s7", 2, "X");
        this.spot8 = new Spot("s8", 1, "X");
        this.spot9 = new Spot("s9", 3, "X");
        this.spot10 = new Spot("s10", 3, "X");
        this.spot11 = new Spot("s11", 1, "X");
        this.spot12 = new Spot("s12", 2, "X");
        this.spot13 = new Spot("s13", 2, "w4");
        this.spot14 = new Spot("s14", 2, "w5");
        this.spot15 = new Spot("s15", 3, "w6");
        this.spot16 = new Spot("s16", 3, "w1");
        this.spot17 = new Spot("s17", 3, "w2");
        this.spot18 = new Spot("s18", 3, "w3");

        this.board[0][0] = null;
        this.board[0][1] = this.spot1;
        this.board[0][2] = null;
        this.board[0][3] = this.spot2;
        this.board[0][4] = null;
        this.board[0][5] = this.spot3;

        this.board[1][0] = this.spot4;
        this.board[1][1] = null;
        this.board[1][2] = this.spot5;
        this.board[1][3] = null;
        this.board[1][4] = this.spot6;
        this.board[1][5] = null;

        this.board[2][0] = null;
        this.board[2][1] = this.spot7;
        this.board[2][2] = null;
        this.board[2][3] = this.spot8;
        this.board[2][4] = null;
        this.board[2][5] = this.spot9;

        this.board[3][0] = this.spot10;
        this.board[3][1] = null;
        this.board[3][2] = this.spot11;
        this.board[3][3] = null;
        this.board[3][4] = this.spot12;
        this.board[3][5] = null;

        this.board[4][0] = null;
        this.board[4][1] = this.spot13;
        this.board[4][2] = null;
        this.board[4][3] = this.spot14;
        this.board[4][4] = null;
        this.board[4][5] = this.spot15;

        this.board[5][0] = this.spot16;
        this.board[5][1] = null;
        this.board[5][2] = this.spot17;
        this.board[5][3] = null;
        this.board[5][4] = this.spot18;
        this.board[5][5] = null;


    }

    public Spot[][] getBoard() {
        return board;
    }

    public void setBoard(Spot[][] aboard) {
        this.board = aboard;
    }

    public void setCapture(boolean capture) {

        this.capture = capture;

    }

    public boolean getCapture() {
        return capture;
    }


}                                                                                                                               
                                                                                                                                