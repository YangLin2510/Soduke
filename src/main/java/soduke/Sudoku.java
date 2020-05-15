package soduke;

import java.util.HashMap;

public class Sudoku {

    public static void main(String[] args) {
        int[][] init = new int[][]{
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 8, 9, 4, 0, 0, 3, 0, 0},
                {0, 7, 0, 8, 0, 3, 4, 5, 0},
                {0, 5, 4, 0, 0, 0, 0, 9, 0},
                {0, 0, 0, 0, 1, 0, 0, 0, 0},
                {0, 3, 0, 0, 0, 0, 8, 2, 0},
                {0, 2, 1, 3, 0, 9, 0, 7, 0},
                {0, 0, 6, 0, 0, 7, 2, 4, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
        Sudoku sudoku = new Sudoku(init);
        Long s = System.currentTimeMillis();
        try {
            sudoku.sudoku();
        } catch (Exception e) {
            //
        }
        long e = System.currentTimeMillis();
        System.out.println("处理时间: "+(e-s)+" ms");
    }

    private boolean[][] initNum;
    private int[][] init;
    private static final int MAX_INDEX = 8;
    private static final int MAX_VALUE = 9;

    public Sudoku(int[][] init) {
        this.init = init;
    }

    public void sudoku() {
        //标识初始化数字
        boolean[][] initNum = new boolean[9][9];
        for (int i = 0; i < init.length; i++) {
            for (int j = 0; j < init[i].length; j++) {
                initNum[i][j] = init[i][j] != 0;
            }
        }
        this.initNum = initNum;
        Position current = first();
        while (current.getY() <= MAX_INDEX || current.getX() <= MAX_INDEX) {
            if (init[current.getX()][current.getY()] == 0) {
                init[current.getX()][current.getY()]++;
            }

            while (init[current.getX()][current.getY()] <= MAX_VALUE) {
                if (isOk(current)) {
                    current = next(current);
                    break;
                }
                init[current.getX()][current.getY()]++;
            }

            if (init[current.getX()][current.getY()] > MAX_VALUE) {
                init[current.getX()][current.getY()] = 0;
                current = previous(current);
                init[current.getX()][current.getY()]++;
            }
        }
        print();
    }

    /**
     * 打印结果
     */
    void print() {
        for (int i = 0; i < init.length; i++) {
            for (int j = 0; j < init[i].length; j++) {
                System.out.print(init[i][j] + "\t");
            }
            System.out.println();
        }
    }

    /**
     * 数值是否是合法的
     * 满足数独的规则
     *
     * @param position
     * @return
     */
    boolean isOk(Position position) {
        //当前行判断
        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>(MAX_VALUE);
        for (int i = 0; i <= MAX_INDEX; i++) {
            int value = init[position.getX()][i];
            if (value != 0) {
                if (map.get(value) == null) {
                    map.put(value, 1);
                } else {
                    return false;
                }
            }
        }
        //当前列判断
        map.clear();
        for (int i = 0; i <= MAX_INDEX; i++) {
            int value = init[i][position.getY()];
            if (value != 0) {
                if (map.get(value) == null) {
                    map.put(value, 1);
                } else {
                    return false;
                }
            }
        }

        //当前九宫格判断
        map.clear();
        int startx, endx;
        int starty, endy;
        if (position.getX() <= 2) {
            startx = 0;
            endx = 2;
        } else if (position.getX() <= 5) {
            startx = 3;
            endx = 5;
        } else {
            startx = 6;
            endx = 8;
        }
        if (position.getY() <= 2) {
            starty = 0;
            endy = 2;
        } else if (position.getY() <= 5) {
            starty = 3;
            endy = 5;
        } else {
            starty = 6;
            endy = 8;
        }

        for (int i = startx; i <= endx; i++) {
            for (int j = starty; j <= endy; j++) {
                int value = init[i][j];
                if (value != 0) {
                    if (map.get(value) == null) {
                        map.put(value, 1);
                    } else {
                        return false;
                    }
                }
            }
        }
        return true;
    }


    /**
     * 获取第一个空白值
     *
     * @return
     */
    Position first() {
        if (initNum[0][0] == false) {
            return new Position(0, 0);
        }
        return next(new Position(0, 0));
    }


    /**
     * 获取下一个位置
     */
    Position next(Position current) {
        int x, y;
        if (current.getY() == MAX_INDEX && current.getX() == MAX_INDEX) {
            print();
            throw new RuntimeException("已经找到答案，结束");
        }
        if (current.getY() == MAX_INDEX) {
            x = current.getX() + 1;
            y = 0;
        } else {
            x = current.getX();
            y = current.getY() + 1;
        }

        Position next = new Position(x, y);
        //如果下一个值，是初始化参数，继续获取下一个。
        if (initNum[next.getX()][next.getY()]) {
            return next(next);
        }
        return next;
    }

    /**
     * 获取上一个位置
     *
     * @param current
     * @return
     */
    Position previous(Position current) {
        if (current.getX() == 0 && current.getY() == 0) {
            throw new RuntimeException("没有上一个值");
        }
        int x, y;
        if (current.getY() == 0) {
            x = current.getX() - 1;
            y = MAX_INDEX;
        } else {
            x = current.getX();
            y = current.getY() - 1;
        }
        Position previous = new Position(x, y);
        //如果上一个值是初始化参数，继续获取上一个值。
        if (initNum[previous.getX()][previous.getY()]) {
            return previous(previous);
        }
        return previous;
    }

}
