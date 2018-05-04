public class Landscape
{
  private int nX = 40;
  private int nY = 40;
  private Cell cell[][] = new Cell[nX][nY];
  private double sugarRegrowthRate = 0.005;
  private double spiceRegrowthRate = 0.005;

  public Landscape(int x, int y) {
    nX = x;
    nY = y;
    cell = new Cell[nX][nY];
    for (int i = 0; i < nX; i++) {
      for (int j = 0; j < nY; j++) {
        cell[i][j] = new Cell(getGaussian(i, j), sugarRegrowthRate, getGaussian(nX-i, j), spiceRegrowthRate);

      }
    }
  }

  public double getGaussian(int x, int y) {
    return gaussianHelper(x - nX/4, y - nY/4) + gaussianHelper(x - 3*nX/4, y - 3*nY/4);
  }

  private double gaussianHelper(double x, double y) {
    double thetaX = 0.4 * nX;
    double thetaY = 0.4 * nY;
    return Math.pow(4.0, - ((x/thetaX)*(x/thetaX)) - ((y/thetaY)*(y/thetaY)));
  }

  public Cell getCellAt(int row, int col) {
    return cell[row][col];
  }

  public Agent getAgentAt(int row, int col) {
    return cell[row][col].getOccupied();
  }

  public int getXSize() {
    return nX;
  }

  public int getYSize() {
    return nY;
  }

  public void update(double time) {
    for(int x = 0; x < nX; x++) {
      for(int y = 0; y < nY; y++) {
        cell[x][y].regrowCell(time);
      }
    }
  }
}
