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
		if (o.getClass().isAssignableFrom(Point.class))
		{
			Point p  = (Point)o;
			return X == p.X && Y == p.Y;
		}
		return false;
	}
}
