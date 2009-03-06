package org.papernapkin.liana.swing.layout;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JComponent;

/**
 * Using this class saves you lots of keystrokes when
 * slugging it out with {@link GridBagLayout}.
 *
 * <p>Example:
 * <blockquote>
 * 		<xmp>
		Container c = new JPanel(new GridBagLayout());
		Bag b = new Bag();

		// Add a label
		c.add(new JLabel("Foo"), b.cell(1,1));

		// Add a textfield that spans several columns
		c.add(new JTextField(), b.nextX().fillX().colspan(3));

		// For the next row, add a big scrolling thing
		c.add(new JScrollPane(), b.nextY().fillBoth().colspan(4));
 * 		</xmp>
 * </blockquote>
 *
 * @see #inset(int)
 * @see #cell(int,int)
 * @see #fillNone()
 * @see #fillBoth()
 * @see #fillX()
 * @see #fillY()
 *
 * @author ddjohnson
 *
 * @version 1.2 Jul 02, 2006
 * @version 1.1 Aug 10, 2005
 */
public class Bag extends GridBagConstraints
{
	private static final long serialVersionUID = 1L;

	public Bag()
	{
		super();
		reset();
	}
	
	
	public Bag reset()
	{
		fillNone();
		cell(0,0);
		gridwidth = gridheight = 1;
		weightx = weighty = 0;
		return inset(1);
	}

	
	public Bag cell(int x, int y)
	{
		gridx = x;
		gridy = y;
		return this;
	}
	
	public Bag center()
	{
		anchor = CENTER;
		return this;
	}

	
	public Bag inset(int amt)
	{
		return inset(amt, amt, amt, amt);
	}

	public Bag inset(int top, int left, int bottom, int right)
	{
		insets.top = top;
		insets.left = left;
		insets.bottom = bottom;
		insets.right = right;
		return this;
	}

	
	public Bag nextX()
	{
		gridx+=gridwidth;
		return this;
	}

	public Bag nextY()
	{
		gridy+=gridheight;
		gridx=0;
		return this;
	}
	
	public Bag resetX()
	{
		gridx = 0;
		return this;
	}
	
	public Bag resetY()
	{
		gridy = 0;
		return this;
	}
	
	
	public Bag rowspan(int i)
	{
		gridheight = i;
		return this;
	}

	public Bag colspan(int i)
	{
		gridwidth = i;
		return this;
	}

	public Bag east()
	{
		anchor = EAST;
		return this;
	}

	public Bag fillNone()
	{
		fill = NONE;
		weightx = weighty = 0.0;
		return this;
	}

	public Bag fillBoth()
	{
		fill = BOTH;
		weightx = weighty = 1.0;
		return this;
	}


	public Bag fillX()
	{
		fill = HORIZONTAL;
		weightx = 1.0;
		weighty = 0.0;
		return this;
	}

	public Bag fillY()
	{
		fill = VERTICAL;
		weighty = 1.0;
		weightx = 0;
		return this;
	}

	public Bag north()
	{
		anchor = NORTH;
		return this;
	}
	
	public Bag south()
	{
		anchor = SOUTH;
		return this;
	}

	public Bag west()
	{
		anchor = WEST;
		return this;
	}
	
	public static JComponent spacer()
	{
		return new Spacer();
	}

	public static class Spacer extends JComponent
	{
		private static final long serialVersionUID = 1L;
		
		public Spacer()
		{
			setMaximumSize(new Dimension(
				Integer.MAX_VALUE,
				Integer.MAX_VALUE));
			setOpaque(false);
		}
	}

}
