import java.util.ArrayList;
import java.util.Random;
import tester.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;

// Represents a MineSweeper game
class Game extends World {
  int columns;
  int rows;
  int numOfMines;
  int sizeOfCell;
  Random r;
  ArrayList<ArrayList<Cell>> board;

  // main constructor
  Game(int columns, int rows, int numOfMines, int sizeOfCell, Random r,
      ArrayList<ArrayList<Cell>> board) {
    this(columns, rows, numOfMines,r);
    this.board = board;
    this.sizeOfCell = sizeOfCell;
    this.initboard();
    this.initNeighbors();
    this.dropMines();
  }

  // Convince constructor;
  Game(int columns, int rows, int numOfMines, Random r) {
    this.columns = columns;
    this.rows = rows;
    this.numOfMines = numOfMines;
    this.sizeOfCell = 10;
    this.r = r;
    this.board = new ArrayList<ArrayList<Cell>>();
    this.initboard();
    this.initNeighbors();
    this.dropMines();

  }

  // initializes board with all cells Showing now for testing, and no cells as
  // bombs
  void initboard() {
    for (int x = 0; x < this.rows; x++) {
      ArrayList<Cell> row = new ArrayList<Cell>();
      for (int y = 0; y < this.columns; y++) {
        row.add(new Cell(true, false));
      }
      this.board.add(row);
    }
  }

  // initializes the boards neighbors depending on the edges of the board
  void initNeighbors() {
    Cell currentCell;

    for (int x = 0; x < this.rows; x++) {
      for (int y = 0; y < this.columns; y++) {
        currentCell = this.board.get(x).get(y);
        // Left corner case
        if (y == 0 & x == 0) {
          currentCell.neighbors.add(this.board.get(x).get(y + 1));
          currentCell.neighbors.add(this.board.get(x + 1).get(y));
          currentCell.neighbors.add(this.board.get(x + 1).get(y + 1));
        }

        // Top right corner case
        else if (y == 0 && x == this.rows - 1) {
          currentCell.neighbors.add(this.board.get(x).get(y + 1));
          currentCell.neighbors.add(this.board.get(x - 1).get(y));
          currentCell.neighbors.add(this.board.get(x - 1).get(y + 1));
        }

        // Bottom Left corner case
        else if (y == this.columns - 1 && x == 0) {
          currentCell.neighbors.add(this.board.get(x).get(y - 1));
          currentCell.neighbors.add(this.board.get(x + 1).get(y));
          currentCell.neighbors.add(this.board.get(x + 1).get(y - 1));
        }

        // Bottom right corner case
        else if (y == this.columns - 1 && x == this.rows - 1) {
          currentCell.neighbors.add(this.board.get(x).get(y - 1));
          currentCell.neighbors.add(this.board.get(x - 1).get(y));
          currentCell.neighbors.add(this.board.get(x - 1).get(y - 1));
        }

        // Top row edge case
        else if (y == 0) {
          currentCell.neighbors.add(this.board.get(x).get(y + 1));
          currentCell.neighbors.add(this.board.get(x + 1).get(y));
          currentCell.neighbors.add(this.board.get(x + 1).get(y + 1));
          currentCell.neighbors.add(this.board.get(x - 1).get(y + 1));
          currentCell.neighbors.add(this.board.get(x - 1).get(y));
        }

        // left edge case
        else if (x == 0) {
          currentCell.neighbors.add(this.board.get(x).get(y + 1));
          currentCell.neighbors.add(this.board.get(x).get(y - 1));
          currentCell.neighbors.add(this.board.get(x + 1).get(y));
          currentCell.neighbors.add(this.board.get(x + 1).get(y + 1));
          currentCell.neighbors.add(this.board.get(x + 1).get(y - 1));
        }

        // Right row case
        else if (x == this.rows - 1) {
          currentCell.neighbors.add(this.board.get(x).get(y + 1));
          currentCell.neighbors.add(this.board.get(x).get(y - 1));
          currentCell.neighbors.add(this.board.get(x - 1).get(y));
          currentCell.neighbors.add(this.board.get(x - 1).get(y - 1));
          currentCell.neighbors.add(this.board.get(x - 1).get(y + 1));
        }

        // Bottom row case
        else if (y == this.columns - 1) {
          currentCell.neighbors.add(this.board.get(x).get(y - 1));
          currentCell.neighbors.add(this.board.get(x - 1).get(y));
          currentCell.neighbors.add(this.board.get(x + 1).get(y));
          currentCell.neighbors.add(this.board.get(x - 1).get(y - 1));
          currentCell.neighbors.add(this.board.get(x + 1).get(y - 1));

        }

        // center cases
        else {
          currentCell.neighbors.add(this.board.get(x + 1).get(y));
          currentCell.neighbors.add(this.board.get(x - 1).get(y));
          currentCell.neighbors.add(this.board.get(x).get(y + 1));
          currentCell.neighbors.add(this.board.get(x).get(y - 1));
          currentCell.neighbors.add(this.board.get(x + 1).get(y + 1));
          currentCell.neighbors.add(this.board.get(x - 1).get(y + 1));
          currentCell.neighbors.add(this.board.get(x + 1).get(y - 1));
          currentCell.neighbors.add(this.board.get(x - 1).get(y - 1));
        }
      }
    }
  }

  // Places mines in random spots, while not repeating the places where bombs are
  // placed
  void dropMines() {
    int randX;
    int randY;
    int counter = 0;
    while (counter <= this.numOfMines) {
      randX = this.r.nextInt(this.rows);
      randY = this.r.nextInt(this.columns);

      if (!(this.board.get(randX).get(randY).isMine)) {
        this.board.get(randX).get(randY).makeMine();
        counter++;
      }
    }
  }

  // draws the board
  public WorldScene makeScene() {

    WorldScene image = new WorldScene(this.sizeOfCell * this.columns, this.sizeOfCell * this.rows);

    WorldImage cell;

    for (int x = 0; x < this.columns; x++) {
      for (int y = 0; y < this.rows; y++) {
        Cell currentCell = this.board.get(x).get(y);
        if (currentCell.isShown) {
          if (currentCell.isMine) {
            cell = new RectangleImage(this.sizeOfCell, this.sizeOfCell, OutlineMode.SOLID,
                Color.RED);
          }
          else if (currentCell.mineNeighborCount() > 0) {
            cell = new RectangleImage(this.sizeOfCell, this.sizeOfCell, OutlineMode.OUTLINE,
                Color.BLUE);
            String number = Integer.toString(currentCell.mineNeighborCount());
            cell = new TextImage(number, Color.BLUE).overlayImages(cell);
          }
          else {
            cell = new RectangleImage(this.sizeOfCell, this.sizeOfCell, OutlineMode.OUTLINE,
                Color.GRAY);
          }
        }
        else {
          cell = new RectangleImage(this.sizeOfCell, this.sizeOfCell, OutlineMode.OUTLINE,
              Color.GRAY);
        }
        image.placeImageXY(cell, y * this.sizeOfCell + this.sizeOfCell / 2,
            x * this.sizeOfCell + this.sizeOfCell / 2);
      }
    }
    return image;
  }
}

// Represents a cell
class Cell {
  boolean isShown;
  boolean isMine;
  ArrayList<Cell> neighbors;

  // main constructor
  Cell(boolean isMine, boolean isShown, ArrayList<Cell> neighbors) {
    this.isShown = isShown;
    this.isMine = isMine;
    this.neighbors = neighbors;
  }

  // Convince constructor
  Cell(boolean isShown, boolean isMine) {
    this.isShown = isShown;
    this.isMine = isMine;
    this.neighbors = new ArrayList<Cell>();
  }

  // makes a cell as a mine
  public void makeMine() {
    this.isMine = true;
  }

  // goes through the neighbors and counts the mine counts
  int mineNeighborCount() {
    int count = 0;
    for (int i = 0; i < this.neighbors.size(); i++) {
      if (this.neighbors.get(i).isMine) {
        count++;
      }
    }
    return count;
  }
}

// Represents examples of Minesweeper
class ExamplesMinesweeper {
  Cell cell1;
  Cell cell2;
  Cell cell3;
  Cell cell4;
  Cell cell5;
  Cell cell6;
  Cell cell7;

  ArrayList<Cell> row1;
  ArrayList<Cell> row2;

  ArrayList<ArrayList<Cell>> board;

  Game game1;

  WorldScene worldScene1;

  Random r = new Random(6);
  
  // Initialized data
  
  void initData() {
    cell1 = new Cell(true, false);
    cell2 = new Cell(true, false);
    cell3 = new Cell(false, false);
    cell4 = new Cell(false, true);
    cell5 = new Cell(true, false);
    cell6 = new Cell(false, false);

    cell4.neighbors.add(cell3);
    cell4.neighbors.add(cell2);
    cell4.neighbors.add(cell1);

    cell3.neighbors.add(cell4);
    cell3.neighbors.add(cell1);
    cell3.neighbors.add(cell2);
    cell3.neighbors.add(cell5);
    cell3.neighbors.add(cell6);

    cell6.neighbors.add(cell5);
    cell6.neighbors.add(cell2);
    cell6.neighbors.add(cell3);

    cell1.neighbors.add(cell3);
    cell1.neighbors.add(cell2);
    cell1.neighbors.add(cell4);

    cell2.neighbors.add(cell3);
    cell2.neighbors.add(cell4);
    cell2.neighbors.add(cell5);
    cell2.neighbors.add(cell6);
    cell2.neighbors.add(cell1);

    cell5.neighbors.add(cell6);
    cell5.neighbors.add(cell3);
    cell5.neighbors.add(cell2);

    row1 = new ArrayList<Cell>();

    row1.add(cell4);
    row1.add(cell3);
    row1.add(cell6);

    row2 = new ArrayList<Cell>();

    row2.add(cell1);
    row2.add(cell2);
    row2.add(cell5);

    board = new ArrayList<ArrayList<Cell>>();

    board.add(row2);
    board.add(row1);

    game1 = new Game(2, 3, 1, 2, new Random(1), board);
  }


  void testGame(Tester t) {
    Game g = new Game(20, 20, 20, this.r);
    g.bigBang(750, 750);
  }

  void testCountNeighbors(Tester t) {
    this.initData();
    t.checkExpect(cell1.mineNeighborCount(), 2);
    t.checkExpect(cell6.mineNeighborCount(), 0);
    cell2.isMine = true;
    cell1.isMine = true;
    t.checkExpect(cell4.mineNeighborCount(), 6);
    t.checkExpect(cell2.mineNeighborCount(), 4);
  }

  void testMakeMine(Tester t) {
    this.initData();
    this.cell1.makeMine();
    t.checkExpect(this.cell1.isMine, true);
    this.cell4.makeMine();
    t.checkExpect(this.cell4.isMine, true);
  }

  void testInitBoard(Tester t) {
    this.initData();
    t.checkExpect(this.game1.board.get(2).get(1).isShown, true);
    t.checkExpect(this.game1.board.get(0).get(0).isShown, true);
    t.checkExpect(this.game1.board.get(2).get(0).isShown, true);
  }

  void testDropMines(Tester t) {
    this.initData();
    t.checkExpect(this.game1.board.get(2).get(1).isMine, true);
    t.checkExpect(this.game1.board.get(0).get(0).isMine, false);
    t.checkExpect(this.game1.board.get(1).get(1).isMine, false);
    t.checkExpect(this.game1.board.get(1).get(0).isMine, true);
  }

  void testMakeScene(Tester t) {
    this.initData();
    worldScene1 = new WorldScene(2, 3);
    worldScene1.placeImageXY(new RectangleImage(10, 10, OutlineMode.OUTLINE, Color.GRAY), 1, 1);
    worldScene1.placeImageXY(new RectangleImage(10, 10, OutlineMode.OUTLINE, Color.BLUE), 1, 3);
    worldScene1.placeImageXY(new RectangleImage(10, 10, OutlineMode.SOLID, Color.RED), 1, 5);
    worldScene1.placeImageXY(new RectangleImage(10, 10, OutlineMode.OUTLINE, Color.BLUE), 3, 1);
    worldScene1.placeImageXY(new RectangleImage(10, 10, OutlineMode.OUTLINE, Color.BLUE), 3, 3);
    worldScene1.placeImageXY(new TextImage(Integer.toString(1), Color.BLACK)
        .overlayImages(new RectangleImage(2, 2, OutlineMode.SOLID, Color.GRAY)), 3, 5);
    t.checkExpect(game1.makeScene(), worldScene1);

  }

}