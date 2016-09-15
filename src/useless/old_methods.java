package useless;

import game.model.Board;
import game.model.Player;

/**
 * Created by enry8 on 14/09/16.
 */
public class old_methods {
    @Override
    protected Object clone() throws CloneNotSupportedException {
        Board b = (Board) super.clone();
/*        b.p1 = (Player) this.p1.clone();
        b.p2 = (Player) this.p2.clone();
        b.board = new Spot[8][8];
        for(int i = 0; i< 8; i++){
            b.board[i] = Arrays.copyOf(this.board[i], this.board[i].length);
        }*/
        System.out.println("ciao");
        return b;
    }
}
