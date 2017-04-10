package lixue_jiu.scworld;

public class Point2 {
    public int X;
    public int Y;

    public Point2(int x, int y) {
        X = x;
        Y = y;
    }

    public void offset(int x, int y) {
        X += x;
        Y += y;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Point2 && equals((Point2) o);
    }

    public boolean equals(Point2 p) {
        return X == p.X && Y == p.Y;
    }

    @Override
    public int hashCode() {
        return X + Y;
    }

    public static Point2 min(Point2 p1, Point2 p2) {
        return new Point2(Math.min(p1.X, p2.X), Math.min(p1.Y, p2.Y));
    }

    public static Point2 max(Point2 p1, Point2 p2) {
        return new Point2(Math.max(p1.X, p2.X), Math.max(p1.Y, p2.Y));
    }
}
