import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

public class FontImage {
	public static void main(String[] args) throws Exception {
		createImage("./temp/a.png",Arrays.asList("文字1"), new Font("宋体", Font.BOLD, 30) , FontImage.getColorFmtAlpha(Color.BLACK,0.8d), Color.ORANGE);
		createImage("./temp/a1.png",Arrays.asList("文字1","文字2"), new Font("黑体", Font.PLAIN, 30), FontImage.getColorFmtAlpha(Color.BLUE,0.3d),Color.WHITE);
		addWaterMark("./resource/Penguins.jpg","./temp/w1.png",Arrays.asList("添加水印","测试水印添加"),new Font("黑体", Font.PLAIN, 30), FontImage.getColorFmtAlpha(Color.RED,0.2d));
	}
	/**
	 * 获取透明度颜色
	 * @param csource
	 * @param alf
	 * @return
	 */
	public static Color getColorFmtAlpha(Color csource,double alf) {
		alf = alf < 0 ? 0 : alf > 1 ? 1 : alf;
		int tmp = (int)(alf * 255.0);
		return  new Color(csource.getRed(), csource.getGreen(), csource.getBlue(), tmp);
	}
	/**
	 * 给图片加水印
	 * @param srcImgPath 原始图片
	 * @param tarImgPath 生成图片
	 * @param strs 添加文字
	 * @param markContentColor 颜色
	 * @param font 字体
	 */
	public static void addWaterMark(String srcImgPath, String tarImgPath, List<String> strs,Font font,Color contentColor) {
        try {
        	FontMetrics tfm = new BufferedImage(1, 1, 1).createGraphics().getFontMetrics(font);
    		Integer width=10;
    		for (int i = 0; i < strs.size(); i++) {
    			int temp=tfm.stringWidth(strs.get(i));
    			if(temp>width) {
    				width=temp;
    			}
    		}
    		Integer oheight=tfm.getHeight();
            // 读取原图片信息
            File srcImgFile = new File(srcImgPath);//得到文件
            Image srcImg = ImageIO.read(srcImgFile);//文件转化为图片
            int srcImgWidth = srcImg.getWidth(null);//获取图片的宽
            int srcImgHeight = srcImg.getHeight(null);//获取图片的高
            // 加水印
            BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufImg.createGraphics();
            g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
            g.setColor(contentColor); //根据图片的背景设置水印颜色
            g.setFont(font);              //设置字体
            //设置水印的坐标
            FontMetrics fm = g.getFontMetrics(font);
            int ascent = fm.getAscent();
    		int descent = fm.getDescent();
    		for (int i = 0; i < strs.size(); i++) {
    			g.drawString(strs.get(i), 2, oheight*i+oheight/2+ (ascent-descent)/2);// 画出字符串
    		}
    		g.dispose();
    		ImageIO.write(bufImg, "png", new File(tarImgPath));// 输出png图片

        } catch (Exception e) {
            // TODO: handle exception
        }
    }
	
	/**
	 * 创建一个文本图片
	 * @param outFile
	 * @param strs
	 * @param font
	 * @throws Exception
	 */
	public static void createImage(String outFile,List<String> strs, Font font,Color contentColor,Color blackground) throws Exception {
		//计算宽高
		FontMetrics tfm = new BufferedImage(1, 1, 1).createGraphics().getFontMetrics(font);
		Integer width=10;
		for (int i = 0; i < strs.size(); i++) {
			int temp=tfm.stringWidth(strs.get(i));
			if(temp>width) {
				width=temp;
			}
		}
		Integer oheight=tfm.getHeight();
		Integer height=oheight*strs.size();
		// 创建图片
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
		Graphics g = image.getGraphics();
		g.setClip(0, 0, width, height);
		g.setColor(blackground);
		g.fillRect(0, 0, width, height);
		g.setColor(contentColor);
		g.setFont(font);
		
		FontMetrics fm = g.getFontMetrics(font);
		int ascent = fm.getAscent();
		int descent = fm.getDescent();
		/** 用于获得y */
		for (int i = 0; i < strs.size(); i++) {
			g.drawString(strs.get(i), 2, oheight*i+oheight/2+ (ascent-descent)/2);// 画出字符串
		}
		g.dispose();
		ImageIO.write(image, "png",  new File(outFile));// 输出png图片
	}
}