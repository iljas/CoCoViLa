package ee.ioc.cs.vsle.graphics;

import java.io.*;

import java.awt.*;
import java.awt.geom.*;

public class Text extends Shape implements Serializable {

	/**
	 * Text string represented by the shape.
	 */
	public int fixedX;
	public int fixedY;
	public String stringValue;
	Font font;
	Color color;
	public String name;
	private int h; //* Height of the text.
	private int w;//	 * Width of the text.
	private boolean selected = false;
	private boolean fixed = false;
	boolean fixedsize = false;

	public Text(int x, int y, Font font, Color color, int transp, String s) {

		this.x = x;
		this.y = y;
		this.font = font;
		this.color = new Color(color.getRed(), color.getGreen(), color.getBlue(), transp);
		this.stringValue = s;

	} // Text

	public Text(int x, int y, Font font, Color color, int transp, String s, boolean fixed) {
		this.x = x;
		this.y = y;
		this.font = font;
		this.color = new Color(color.getRed(), color.getGreen(), color.getBlue(), transp);
		this.stringValue = s;
		this.fixedsize = fixed;
	} // Text

	public void setFixed(boolean b) {
		this.fixed = b;
	}

	public boolean isFixed() {
		return this.fixed;
	}

	public boolean isFilled() {
		return false;
	} // isFilled

	public String getName() {
		return this.name;
	} // getName

	public void setStrokeWidth(double width) {
	} // setStrokeWidth

	/**
	 * Returns the stroke with of a shape.
	 * @return double - stroke width of a shape.
	 */
	public double getStrokeWidth() {
		return 0.0;
	} // getStrokeWidth

	public boolean isSelected() {
		return this.selected;
	} // isSelected

	public void setSelected(boolean b) {
		this.selected = b;
	} // setSelected

	public String toString() {
		return getName();
	} // toString

	/**
	 * Set size using zoom multiplication.
	 * @param s1 float - set size using zoom multiplication.
	 * @param s2 float - set size using zoom multiplication.
	 */
	public void setMultSize(float s1, float s2) {
		x = x * (int) s1 / (int) s2;
		y = y * (int) s1 / (int) s2;
		this.w = this.w * (int) s1 / (int) s2;
		this.h = this.h * (int) s1 / (int) s2;
		int fontsize = font.getSize() * (int) s1 / (int) s2;

		this.font = new Font(font.getName(), font.getStyle(), fontsize);
	} // setMultSize

	public int getRealHeight() {
		return getHeight();
	} // getRealHeight

	public int getRealWidth() {
		return getWidth();
	} // getRealWidth

	public void setPosition(int x, int y) {
		this.x = getX() + x;
		this.y = getY() + y;
	} // setPosition

	/**
	 * Returns the height of the arc (the difference between the arc's beginning and
	 * end y coordinates).
	 * @return int - height of the arc.
	 */
	int getHeight() {
		return height;
	} // getHeight

	/**
	 * Set the shape name.
	 * @param s String - name of the shape.
	 */
	public void setName(String s) {
		this.name = s;
	} // setName

	/**
	 * Resizes current object.
	 * @param deltaW int - change of object with.
	 * @param deltaH int - change of object height.
	 * @param cornerClicked int - number of the clicked corner.
	 */
	public void resize(int deltaW, int deltaH, int cornerClicked) {
	} // resize

	/**
	 * Returns the used font.
	 * @return Font - used font.
	 */
	public Font getFont() {
		return this.font;
	} // getFont

	/**
	 * Returns the width of the arc (the difference between the arc's beginning and
	 * end x coordinates).
	 * @return int - width of the arc.
	 */
	int getWidth() {
		return this.width;
	} // getWidth

	/**
	 * Returns the number representing a corner the mouse was clicked in.
	 * 1: top-left, 2: top-right, 3: bottom-left, 4: bottom-right.
	 * Returns 0 if the click was not in the corner.
	 * @param pointX int - mouse x coordinate.
	 * @param pointY int - mouse y coordinate.
	 * @return int - corner number the mouse was clicked in.
	 */
	public int controlRectContains(int pointX, int pointY) {
		if (pointX >= x && pointY >= y && pointX <= x + 4 && pointY <= y + 4) {
			return 1;
		}
		if (pointX >= x + width - 4 && pointY >= y && pointX <= x + width && pointY <= y + 4) {
			return 2;
		}
		if (pointX >= x && pointY >= y + height / 2 - 4 && pointX <= x + 4 && pointY <= y + height / 2) {
			return 3;
		}
		if (pointX >= x + width - 4 && pointY >= y + height / 2 - 4 && pointX <= x + width && pointY <= y + height / 2) {
			return 4;
		}
		return 0;
	} // controlRectContains

	/**
	 * Returns the text string.
	 * @return String - the text string.
	 */
	public String getText() {
		return this.stringValue;
	} // getText

	/**
	 * Set the color of a shape.
	 * @param col Color - color of a shape.
	 */
	public void setColor(Color col) {
		this.color = col;
	} // setColor

	/**
	 * Set string font.
	 * @param f Font - string font.
	 */
	public void setFont(Font f) {
		this.font = f;
	} // setFont

	/**
	 * Set string text.
	 * @param s String - string text.
	 */
	public void setText(String s) {
		this.stringValue = s;
	} // setText

	public int getTransparency() {
		return color.getAlpha();
	} // getTransparency

	/**
	 * Returns the line typ of the shape.
	 * @return int - line type of the shape.
	 */
	public int getLineType() {
		return 0;
	} // getLineType

	/**
	 * Returns the color of the text.
	 * @return Color - color of the text.
	 */
	public Color getColor() {
		return this.color;
	} // getColor

	public int getX() {
		return x;
	} // getX

	public int getY() {
		return y;
	} // getY

	public boolean contains(int pointX, int pointY) {
		if (pointX >= x && pointX <= x + this.w && pointY >= y - this.h && pointY <= y) {
			return true;
		} else {
			return false;
		}
	} // contains

	public boolean isInside(int x1, int y1, int x2, int y2) {
		if (x >= x1 && y >= y1 && x + this.w <= x2 && y - this.h <= y2) {
			return true;
		} else {
			return false;
		}
	} // isInside

	public boolean isInsideRect(int x1, int y1, int x2, int y2) {
		if (x1 < x && y1 < y && x2 > x + this.w && y2 > y + this.h) {
			return true;
		}
		return false;
	} // isInsideRect

	/**
	 * Draw the selection markers if object selected.
	 */
	public void drawSelection() {
	} // drawSelection

	public void setTransparency(int d) {
		color = new Color(color.getRed(), color.getGreen(), color.getBlue(), d);
	}

	/**
	 * Specify the line type used at drawing the shape.
	 * @param lineType int
	 */
	public void setLineType(int lineType) {
	} // setLineType

	public BasicStroke getStroke() {
		return null;
	}

	/**
	 * Return a specification of the shape to be written into a file in XML format.
	 * @param boundingboxX - x coordinate of the bounding box.
	 * @param boundingboxY - y coordinate of the bounding box.
	 * @return String - specification of a shape.
	 */
	public String toFile(int boundingboxX, int boundingboxY) {
		int colorInt = 0;

		if (color != null) colorInt = color.getRGB();

		return "<text string=\"" + stringValue + "\" colour=\"" + colorInt + "\" x=\""
			+ (x - boundingboxX) + "\" y=\"" + (y - boundingboxY)
			+ "\" fontname=\"" + font.getName() + "\" fontstyle=\""
			+ font.getStyle() + "\" fontsize=\"" + font.getSize() + "\" transparency=\"" + getTransparency() + "\"/>\n";
	} // toFile

	public String toText() {
		int colorInt = 0;

		if (color != null) colorInt = color.getRGB();

		return "TEXT:" + x + ":" + y + ":" + colorInt + ":" + font.getName() + ":" + font.getStyle() + ":" + font.getSize() + ":" + color.getTransparency() + ":" + stringValue;
	} // toText


	public void draw(int xModifier, int yModifier, float Xsize, float Ysize, Graphics2D g2) {

		java.awt.font.FontRenderContext frc = g2.getFontRenderContext();

		Rectangle2D r = this.getFont().getStringBounds(stringValue, 0, stringValue.length(), frc);

		this.h = (int) r.getHeight();
		this.w = (int) r.getWidth();

		g2.setFont(font);
		g2.setColor(color);

		int a = xModifier + (int) (Xsize * x);
		int b = yModifier + (int) (Ysize * y);

		g2.drawString(stringValue, a, b);

		if (selected) {
			drawSelection();
		}

	} // draw

	public void drawSpecial(int xModifier, int yModifier, float Xsize, float Ysize, Graphics2D g2, String name, String value, double angle) {

		java.awt.font.FontRenderContext frc = g2.getFontRenderContext();

		Rectangle2D r = this.getFont().getStringBounds(stringValue, 0, stringValue.length(), frc);

		this.h = (int) r.getHeight();
		this.w = (int) r.getWidth();
        if (!fixedsize) {
			g2.setFont(font.deriveFont((float) Math.sqrt(Xsize * Ysize) * font.getSize()));
		} else {
			g2.setFont(font);
		}
		g2.setColor(color);


		/*g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
				java.awt.RenderingHints.VALUE_ANTIALIAS_OFF);*/


		//int a = xModifier + (int) (Xsize * x * Math.cos(angle));
		//int b = yModifier + (int) (Ysize * y + (Xsize * x * Math.sin(angle)));
		int a = 0, b = 0;
		if (fixedX == 0)
			a = xModifier + (int) (Xsize * x* Math.cos(angle));
		else if (fixedX == -1)
			a = xModifier + (int) (x* Math.cos(angle));
		else
			a = xModifier + (int) (Xsize * x* Math.cos(angle)) - fixedX;

		if (fixedY == 0)
			b = yModifier + (int) (Ysize * y+ (Xsize * x * Math.sin(angle)));
		else if (fixedY == -1)
			b = yModifier + (int)(y+ (Xsize * x * Math.sin(angle)));
		else
			b = yModifier + (int) (Ysize * y+ (Xsize * x * Math.sin(angle))) - fixedY;

		//	g2.translate(xModifier, yModifier);
//		g2.rotate(-1*angle);
//		g2.translate(-1 * (xModifier), -1 * (yModifier));

		if (stringValue.equals("*self"))
			g2.drawString(value, a, b);
		else if (stringValue.equals("*selfWithName"))
			g2.drawString(name + " = " + value, a, b);
		else
			g2.drawString(stringValue, a, b);

		if (selected) {
			drawSelection();
		}

//		g2.translate(xModifier, yModifier);
//		g2.rotate(angle);
//		g2.translate(-1 * (xModifier), -1 * (yModifier));

	}

	public Object clone() {
		return super.clone();
	} // clone

}
