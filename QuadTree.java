import java.awt.*;

public class QuadTree {
    private static final double COMPRESSION_LEVEL = 0.01;
    private Pixel[][] colors;
    private double max;
    private Node root;//root of tree
    private int leafCount = 1;
    private int THRESHOLD = 20;

    public QuadTree(Pixel[][] colors) {
        this.colors = colors;
        this.root = new Node(0, 0, colors[0].length, colors.length);
        /* divides root node into quadrants
        root.nw = new Node(root.xTop, root.yTop, root.xTop + (root.xBot - root.xTop) / 2, root.yTop + (root.yBot - root.yTop) / 2);
        root.ne = new Node(root.xTop + (root.xBot - root.xTop) / 2, root.yTop, root.xBot, root.yTop + (root.yBot - root.yTop) / 2);
        root.sw = new Node(root.xTop, root.yTop + (root.yBot - root.yTop) / 2, root.xTop + (root.xBot - root.xTop) / 2, root.yBot);
        root.se = new Node(root.xTop + (root.xBot - root.xTop) / 2, root.yTop + (root.yBot - root.yTop) / 2, root.xBot, root.yBot);*/
        this.max = COMPRESSION_LEVEL * (colors.length * colors[0].length);
    }

    public Pixel[][] getColors() {
        return colors;
    }

    public Node getRoot() {
        return root;
    }

    public double getMax() {
        return max;
    }

    public int getLeafCount() {
        return leafCount;
    }

    protected class Node {
        private int xTop, yTop, xBot, yBot;
        private Node ne, nw, se, sw;

        public Node(int xTop, int yTop, int xBot, int yBot) {
            this.xTop = xTop;
            this.yTop = yTop;
            this.xBot = xBot;
            this.yBot = yBot;
        }

        public int size() {
            return (this.xBot - this.xTop) * (this.yBot - this.yTop);
        }

        public void setNe(Node ne) {
            this.ne = ne;
        }

        public void setNw(Node nw) {
            this.nw = nw;
        }

        public void setSe(Node se) {
            this.se = se;
        }

        public void setSw(Node sw) {
            this.sw = sw;
        }

        private Color mean() {
            int redSum = 0, greenSum = 0, blueSum = 0;
            for (int i = xTop; i < xBot; i++) {
                for (int j = yTop; j < yBot; j++) {
                    redSum += colors[i][j].getRed();
                    greenSum += colors[i][j].getGreen();
                    blueSum += colors[i][j].getBlue();
                }
            }
            return new Color(redSum / size(), greenSum / size(), blueSum / size());
        }

        private int squaredError() {
            int errorSum = 0;
            Color mean = mean();
            for (int i = xTop; i < xBot; i++) {
                for (int j = yTop; j < yBot; j++) {
                    errorSum = errorSum + (int) Math.pow(colors[i][j].getRed() - mean.getRed(), 2)
                            + (int) Math.pow(colors[i][j].getGreen() - mean.getGreen(), 2)
                            + (int) Math.pow(colors[i][j].getBlue() - mean.getBlue(), 2);
                }
            }
            return errorSum / size();
        }
    }

    public void split() {
        Node n = splitRec(root, root.xTop, root.yTop, root.xBot, root.yBot);
        System.out.println(leafCount);
        System.out.println(n.xBot + " " + n.xTop);
    }
    private Node splitRec(Node n, int xTop, int yTop, int xBot, int yBot) {
        if (n.squaredError() < 500) {
           for (int i = yTop; i < yBot; i++) {
                colors[xTop][i].setBlue(0);
                colors[xTop][i].setGreen(0);
                colors[xTop][i].setRed(0);
            }
            for (int i = yTop; i < yBot; i++) {
                colors[xBot-1][i].setBlue(0);
                colors[xBot-1][i].setGreen(0);
                colors[xBot-1][i].setRed(0);
            }
            for (int i = xTop; i < xBot; i++) {
                colors[i][yBot-1].setBlue(0);
                colors[i][yBot-1].setRed(0);
                colors[i][yBot-1].setGreen(0);
            }
            for (int i = xTop; i < xBot; i++) {
                colors[i][yTop].setGreen(0);
                colors[i][yTop].setBlue(0);
                colors[i][yTop].setRed(0);
            }
            for (int i = xTop+1; i < xBot-1; i++) {
                for (int j = yTop+1; j < yBot-1; j++) {
                    colors[i][j].setRed(n.mean().getRed());
                    colors[i][j].setGreen(n.mean().getGreen());
                    colors[i][j].setBlue(n.mean().getBlue());
                }
            }
            return n;
        }

       else {
            if ((n.xBot - n.xTop == 1) && n.yBot - n.yTop == 2) {
                    leafCount++;
                    n.sw = new Node(xTop, yTop, xBot, yTop + 1);
                    n.nw = new Node(xTop, yTop + 1, xBot, yBot);
                    return n;
            }
            if ((n.xBot - n.xTop == 2) && n.yBot - n.yTop == 1) {
                leafCount++;
                n.sw = new Node(xTop, yTop, xBot + 1, yBot);
                n.nw = new Node(xTop + 1, yTop, xBot, yBot);
                return n;
            }
            if ((n.xBot - n.xTop <= 1 && n.yBot - n.yTop <= 1)) {
                return n;
            }
            else {
                leafCount += 3;

                n.nw = new Node(xTop, yTop, xTop + (xBot - xTop) / 2, yTop + (yBot - yTop) / 2);
                n.ne = new Node(xTop + (xBot - xTop) / 2, yTop, xBot, yTop + (yBot - yTop) / 2);
                n.sw = new Node(xTop, yTop + (yBot - yTop) / 2, xTop + (xBot - xTop) / 2, yBot);
                n.se = new Node(xTop + (xBot - xTop) / 2, yTop + (yBot - yTop) / 2, xBot, yBot);
                splitRec(n.ne, xTop + (xBot - xTop) / 2, yTop, xBot, yTop + (yBot - yTop) / 2);
                splitRec(n.nw, xTop, yTop, xTop + (xBot - xTop) / 2, yTop + (yBot - yTop) / 2);
                splitRec(n.sw, xTop, yTop + (yBot - yTop) / 2, xTop + (xBot - xTop) / 2, yBot);
                splitRec(n.se, xTop + (xBot - xTop) / 2, yTop + (yBot - yTop) / 2, xBot, yBot);
            }
            return n;
        }
    }

}
