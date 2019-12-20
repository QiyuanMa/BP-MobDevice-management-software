package resources;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;

public class MyFont {
	
	private Font charFont = null;
	
	private static final int Font_Default_Style = 0;
	private static final float Font_Default_Size = 17;
	
	
	/**
	 * font构造函数
	 * @param style
	 * @param size
	 */
	public MyFont(int style, float size) {
		try {
			charFont = Font.createFont(Font.TRUETYPE_FONT, new File("./font/SourceHanSansSC-Normal.ttf"));
			charFont = charFont.deriveFont(style).deriveFont(size);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * font构造函数，正常，20磅
	 */
	public MyFont() {
		try {
			charFont = Font.createFont(Font.TRUETYPE_FONT, new File("./font/SourceHanSansSC-Normal.ttf"));
			charFont = charFont.deriveFont(Font_Default_Style).deriveFont(Font_Default_Size);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public Font getFont() {
		return charFont;
	}
}
