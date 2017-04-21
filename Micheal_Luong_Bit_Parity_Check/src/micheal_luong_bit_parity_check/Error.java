/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package micheal_luong_bit_parity_check;

/**
 *
 * @author micheal
 */
public class Error {
    private int row;
    private int index;

    public Error(int row) {
        this.row = row;
        this.index = 0;
    }

    public int getRow() {
        return row;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
