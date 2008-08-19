package org.papernapkin.liana.swing.layout;

import java.awt.GridBagConstraints;
import java.awt.Insets;

/**
 * A convenience subclass of GridBagConstraints that uses chainable setters and
 * Enumerations to ease the use of the GridBagConstraints class.
 * 
 * @author pchapman
 */
public class GBConstraints extends GridBagConstraints
{
	private static final long serialVersionUID = 1L;

	/**
	 * @see java.awt.GridBagConstraints#gridx
	 */
	public GBConstraints setGridX(int gridx)
	{
		this.gridx = gridx;
		return this;
	}

	/**
	 * @see java.awt.GridBagConstraints#gridy
	 */
	public GBConstraints setGridY(int gridy)
	{
		this.gridy = gridy;
		return this;
	}

	/**
	 * @see java.awt.GridBagConstraints#gridwidth
	 */
	public GBConstraints setGridWidth(int gridwidth)
	{
		this.gridwidth = gridwidth;
		return this;
	}

	/**
	 * @see java.awt.GridBagConstraints#gridheight
	 */
	public GBConstraints setGridHeight(int gridheight)
	{
		this.gridheight = gridheight;
		return this;
	}

	/**
	 * @see java.awt.GridBagConstraints#weightx
	 */
	public GBConstraints setWeightX(double weightx)
	{
		this.weightx = weightx;
		return this;
	}

	/**
	 * @see java.awt.GridBagConstraints#weighty
	 */
	public GBConstraints setWeightY(double weighty)
	{
		this.weighty = weighty;
		return this;
	}

	public enum Anchor {
		Center(CENTER), East(EAST), North(NORTH), NorthEast(NORTHEAST),
		NorthWest(NORTHWEST), South(SOUTH), SouthEast(SOUTHEAST),
		SouthWest(SOUTHWEST), West(WEST);

		private int value;
		Anchor(int value)
		{
			this.value = value;
		}
		int getValue()
		{
			return value;
		}
	}

	/** 
	 * @see java.awt.GridBagConstraints#anchor
	 */
	public GBConstraints setAnchor(Anchor anchor)
	{
		this.anchor = anchor.getValue();
		return this;
	}

	public enum Fill {
		Both(BOTH), Horizontal(HORIZONTAL), None(NONE), Vertical(VERTICAL);

		private int value;
		Fill(int value)
		{
			this.value = value;
		}
		int getValue()
		{
			return value;
		}
	}

	/**
	 * @see java.awt.GridBagConstraints#fill
	 */
	public GBConstraints setFill(Fill fill)
	{
		super.fill = fill.getValue();
		return this;
	}

	/**
	 * @see java.awt.GridBagConstraints#insets
	 */
	public GBConstraints setInsets(Insets insets)
	{
		this.insets = insets;
		return this;
	}
	
	private Insets getNonNullInsets()
	{
		if (insets == null) {
			insets = new Insets(0, 0, 0, 0);
		}
		return insets;
	}

	/**
	 * Adjusts the bottom inset.
	 * @see java.awt.GridBagConstraints#insets
	 */
	public GBConstraints setInsetsBottom(int bottom)
	{
		getNonNullInsets().bottom = bottom;
		return this;
	}

	/**
	 * Adjusts the left inset.
	 * @see java.awt.GridBagConstraints#insets
	 */
	public GBConstraints setInsetsLeft(int left)
	{
		getNonNullInsets().left = left;
		return this;
	}

	/**
	 * Adusts the right inset.
	 * @see java.awt.GridBagConstraints#insets
	 */
	public GBConstraints setInsetsRight(int right)
	{
		getNonNullInsets().right = right;
		return this;
	}

	/**
	 * Adusts the top inset.
	 * @see java.awt.GridBagConstraints#insets
	 */
	public GBConstraints setInsetsTop(int top)
	{
		getNonNullInsets().top = top;
		return this;
	}
	
	/**
	 * @see java.awt.GridBagConstraints#insets
	 * @see java.awt.Insets#Insets(int, int, int, int)
	 */
	public GBConstraints setInsets(int top, int left, int bottom, int right)
	{
		this.insets = new Insets(top, left, bottom, right);
		return this;
	}

	/**
	 * @see java.awt.GridBagConstraints#ipadx
	 */
	public GBConstraints setIPadX(int ipadx)
	{
		this.ipadx = ipadx;
		return this;
	}

	/**
	 * @see java.awt.GridBagConstraints#ipady
	 */
	public GBConstraints setIPadY(int ipady)
	{
		this.ipady = ipady;
		return this;
	}

	/**
	 * A convenience method to clone this object and cast it into a
	 * GBConstraints type.
	 */
	public GBConstraints copy()
	{
		return (GBConstraints)clone();
	}
}
