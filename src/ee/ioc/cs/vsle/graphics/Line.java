package ee.ioc.cs.vsle.graphics;

import java.io.*;
import ee.ioc.cs.vsle.util.*;
import java.awt.*;

import ee.ioc.cs.vsle.editor.*;

public class Line extends Shape implements Serializable {

	/**
	 * Line start x coordinate.
	 */
	int startX;

	/**
	 * Line start y coordinate.
	 */
	int startY;

	/**
	 * Line end x coordinate.
	 */
	int endX;

	/**
	 * Line end y coordinate.
	 */
	int endY;

	/**
	 * Line weight, logically equals to stroke width.
	 */
	private float lineWeight;

	/**
	 * Line color.
	 */
	Color color;

	/**
	 * Line transparency.
	 */
	float transparency = (float) 1.0;

	/**
	 * Alpha value of a color, used
	 * for defining the transparency of a filled shape.
	 */
	private float alpha;

	/**
	 * Name of the shape.
	 */
	private String name;

	/**
	 * Indicates if the shape is selected or not.
	 */
	private boolean selected = false;

	/**
	 * Defines if the shape is resizable or not.
	 */
	private boolean fixed = false;


	public Line(int x1, int y1, int x2, int y2, int colorInt, double strokeWidth, double transp) {
		startX = x1;
		startY = y1;
		endX = x2;
		endY = y2;
		this.color = new Color(colorInt);
		this.lineWeight = (float) strokeWidth;
		this.transparency = (float) transp;
		this.x = Math.min(x1, x2);
		this.y = Math.min(y1, y2);
		this.width = Math.max(x1, x2) - this.x; // endX - startX;
		this.height = Math.max(y1, y2) - this.y; // endY - startY;
	} // Line

	public void setFixed(boolean b) {
		this.fixed = b;
	}

	public boolean isFixed() {
		return this.fixed;
	}

	public String toString() {
		return getName();
	} // toString

	public boolean isFilled() {
		return false;
	} // isFilled

	public int getRealHeight() {
		return getHeight();
	} // getRealHeight

	public int getRealWidth() {
		return getWidth();
	} // getRealWidth

	public void setSelected(boolean b) {
		this.selected = b;
	} // setSelected

	public String getName() {
		return this.name;
	} // getName

	public boolean isInside(int x1, int y1, int x2, int y2) {
		int minx = Math.min(startX, endX);
		int miny = Math.min(startX, startY);
		int maxx = Math.max(startX, endX);
		int maxy = Math.max(startX, startY);

		if (x1 > minx && y1 > miny && x2 < maxx && y2 < maxy) {
			return true;
		}
		return false;
	} // isInside

	public boolean isInsideRect(int x1, int y1, int x2, int y2) {
		int minx = Math.min(startX, endX);
		int miny = Math.min(startY, endY);
		int maxx = Math.max(startX, endX);
		int maxy = Math.max(endY, startY);
		if (x1 < minx && y1 < miny && x2 > maxx && y2 > maxy) {
			return true;
		}
		return false;
	} // isInsideRect

	/**
	 * Set size using zoom multiplication.
	 * @param s1, s2 float - set size using zoom multiplication.
	 */
	public void setMultSize(float s1, float s2) {
		startX = startX * (int) s1 / (int) s2;
		startY = startY * (int) s1 / (int) s2;
		endX = endX * (int) s1 / (int) s2;
		endY = endY * (int) s1 / (int) s2;
		width = width * (int) s1 / (int) s2;
		height = height * (int) s1 / (int) s2;
	} // setMultSize

	public boolean isSelected() {
		return this.selected;
	} // isSelected

	public void setName(String s) {
		this.name = s;
	} // setName

	/**
	 * Set the percentage of transparency.
	 * @param transparencyPercentage double - the percentage of transparency.
	 */
	public void setTransparency(double transparencyPercentage) {
		this.transparency = (float) transparencyPercentage;
	} // setTransparency

	/**
	 * Set the color of a shape.
	 * @param col Color - color of a shape.
	 */
	public void setColor(Color col) {
		this.color = col;
	} // setColor

	/**
	 * Returns the color of the line.
	 * @return Color - color of the line.
	 */
	public Color getColor() {
		return this.color;
	} // getColor

	/**
	 * Returns the stroke with of a shape.
	 * @return double - stroke width of a shape.
	 */
	public double getStrokeWidth() {
		return this.lineWeight;
	} // getStrokeWidth

	/**
	 * Returns the transparency of the shape.
	 * @return double - the transparency of the shape.
	 */
	public double getTransparency() {
		return this.transparency;
	} // getTransparency

	/**
	 * Resizes current object.
	 * @param deltaW int - change of object with.
	 * @param deltaH int - change of object height.
	 * @param cornerClicked int - number of the clicked corner.
	 */
	public void resize(int deltaW, int deltaH, int cornerClicked) {
		if (!isFixed()) {
			if (cornerClicked == 1) { // TOP-LEFT
				this.startX += deltaW;
				this.startY += deltaH;
			} else if (cornerClicked == 2) { // TOP-RIGHT
				endX += deltaW;
				endY +=deltaH;
			}
		}
	} // resize

	/**
	 * Returns the number representing a corner the mouse was clicked in.
	 * 1: top-left, 2: top-right, 3: bottom-left, 4: bottom-right.
	 * Returns 0 if the click was not in the corner.
	 * @param pointX int - mouse x coordinate.
	 * @param pointY int - mouse y coordinate.
	 * @return int - corner number the mouse was clicked in.
	 */
	public int controlRectContains(int pointX, int pointY) {
		db.p(pointX+" "+ pointY+" "+ endX+" "+ endY+ " " + startX+" "+ startY +" " );
		if (pointX >= startX-2 && pointY >= startY-2 && pointX <= startX + 2 && pointY <= startY + 2) {
			return 1;
		}
		if (pointX >= endX - 2 && pointY >= endY -2 && pointX <= endX+2 && pointY <= endY + 2) {
			return 2;
		}
		return 0;
	} // controlRectContains

	public void setPosition(int x, int y) {
		endX =  endX + x;
		startX = startX + x;
		endY =  endY + y;
		startY = startY + y;
	} // setPosition

	public boolean contains(int pointX, int pointY) {
		float distance = calcDistance(startX, startY, endX, endY, pointX, pointY);
		if (distance <= 3) {
			return true;
		} else {
			return false;
		}
	} // contains

	/**
	 * Calculates the distance of a point from a line give by 2 points.
	 * @param x1 int
	 * @param y1 int
	 * @param x2 int
	 * @param y2 int
	 * @param pointX int
	 * @param pointY int
	 * @return float
	 */
	float calcDistance(int x1, int y1, int x2, int y2, int pointX, int pointY) {
		int calc1 = (pointX - x1) * (x2 - x1) + (pointY - y1) * (y2 - y1);
		int calc2 = (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1);

		float U = (float) calc1 / (float) calc2;

		float intersectX = x1 + U * (x2 - x1);
		float intersectY = y1 + U * (y2 - y1);

		double distance = Math.sqrt(
			(pointX - intersectX) * (pointX - intersectX)
			+ (pointY - intersectY) * (pointY - intersectY));

		double distanceFromEnd1 = Math.sqrt(
			(x1 - pointX) * (x1 - pointX) + (y1 - pointY) * (y1 - pointY));
		double distanceFromEnd2 = Math.sqrt(
			(x2 - pointX) * (x2 - pointX) + (y2 - pointY) * (y2 - pointY));
		double lineLength = Math.sqrt(
			(x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));

		if (lineLength < Math.max(distanceFromEnd1, distanceFromEnd2)) {
			distance = Math.max(Math.min(distanceFromEnd1, distanceFromEnd2),
				distance);
		}
		return (float) distance;
	} // calcDistance

	public void setStrokeWidth(double width) {
		try {
			if (width >= 0.0) {
				lineWeight = (float) width;
			} else {
				throw new Exception("Stroke width undefined or negative.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	} // setStrokeWidth

	void setLine(int x1, int y1, int x2, int y2) {
		startX = x1;
		startY = y1;
		endX = x2;
		endY = y2;
	} // setLine

	public int getStartX() {
		return startX;
	} // getStartX

	public int getStartY() {
		return startY;
	} // getStartY

	/**
	 * Returns the line end x coordinate.
	 * @return int - line end x coordinate.
	 */
	public int getEndX() {
		return endX;
	} // getEndX

	/**
	 * Returns the line end y coordinate.
	 * @return int - line end y coordinate.
	 */
	public int getEndY() {
		return endY;
	} // getEndY

	int getX1() {
		return startX;
	} // getX1

	int getY1() {
		return startY;
	} // getY1

	int getX2() {
		return endX;
	} // getX2

	int getY2() {
		return endY;
	} // getY2

	public int getX() {
		return x;
	} // getX

	public int getY() {
		return y;
	} // getY

	int getWidth() {
		return width;
	} // getWidth

	int getHeight() {
		return height;
	} // getHeight

	/**
	 * Return a specification of the shape to be written into a file in XML format.
	 * @param boundingboxX - x coordinate of the bounding box.
	 * @param boundingboxY - y coordinate of the bounding box.
	 * @return String - specification of a shape.
	 */
	public String toFile(int boundingboxX, int boundingboxY) {
		int colorInt = 0;

		if (color != null) colorInt = color.getRGB();

		return "<line x1=\"" + (startX - boundingboxX) + "\" y1=\""
			+ (startY - boundingboxY) + "\" x2=\"" + (endX - boundingboxX)
			+ "\" y2=\"" + (endY - boundingboxY) + "\" colour=\"" + colorInt
			+ "\" fixed=\"" + isFixed() + "\" stroke=\""+(int)this.lineWeight+"\" transparency=\""+(int)this.transparency+"\"/>\n";
	} // toFile

	public String toText() {
		int colorInt = 0;
		if (color != null) colorInt = color.getRGB();
		return "LINE:" + startX + ":" + startY + ":" + endX + ":" + endY + ":" + colorInt + ":" + (int) this.lineWeight + ":" + (int) this.transparency + ":" + isFixed();
	} // toText

	/**
	 * Draw the selection markers if object selected.
	 * @param g2 Graphics2D - shape graphics.
	 */
	public void drawSelection(Graphics2D g2) {
		g2.setColor(Color.black);
		g2.setStroke(new BasicStroke((float) 1.0));
		g2.fillRect(startX - 2, startY - 2, 4, 4);
		g2.fillRect(endX - 2, endY - 2, 4, 4);
	} // drawSelection

	public void draw(int xModifier, int yModifier, float Xsize, float Ysize, Graphics g) {

		Graphics2D g2 = (Graphics2D) g;

		g2.setStroke(new BasicStroke(lineWeight));

		alpha = (float) (1 - (this.transparency / 100));

		float red = (float) color.getRed() / 256;
		float green = (float) color.getGreen() / 256;
		float blue = (float) color.getBlue() / 256;

		g2.setColor(new Color(red, green, blue, alpha));

		if (RuntimeProperties.isAntialiasingOn) {
			g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
				java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
		}

		int a = xModifier + (int) (Xsize * startX);
		int b = yModifier + (int) (Ysize * startY);
		int c = xModifier + (int) (Xsize * endX);
		int d = yModifier + (int) (Ysize * endY);

		g2.drawLine(a, b, c, d);

		this.width = Math.abs(getStartX() - getEndX());
		this.height = Math.abs(getStartY() - getEndY());

		if (selected) {
			drawSelection(g2);
		}

	} // draw

}
