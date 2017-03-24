public class Point
{
	public int X;
	public int Y;

	public Point(int x, int y)
	{
		X = x;
		Y = y;
	}

	@Override
	public boolean equals(Object o)
	{
		return o instanceof Point && equals((Point)o);
	}

	public boolean equals(Point p)
	{
		return X == p.X && Y == p.Y;
	}
}
