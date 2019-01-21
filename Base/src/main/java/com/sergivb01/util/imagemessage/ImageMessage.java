package com.sergivb01.util.imagemessage;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class ImageMessage{
	private static final char TRANSPARENT_CHAR = ' ';
	private final Color[] colors = new Color[]{new Color(0, 0, 0), new Color(0, 0, 170), new Color(0, 170, 0), new Color(0, 170, 170), new Color(170, 0, 0), new Color(170, 0, 170), new Color(255, 170, 0), new Color(170, 170, 170), new Color(85, 85, 85), new Color(85, 85, 255), new Color(85, 255, 85), new Color(85, 255, 255), new Color(255, 85, 85), new Color(255, 85, 255), new Color(255, 255, 85), new Color(255, 255, 255)};
	private String[] lines;

	public ImageMessage(BufferedImage image, int height, char imgChar){
		ChatColor[][] chatColors = this.toChatColorArray(image, height);
		this.lines = this.toImgMessage(chatColors, imgChar);
	}

	public ImageMessage(ChatColor[][] chatColors, char imgChar){
		this.lines = this.toImgMessage(chatColors, imgChar);
	}

	public ImageMessage(String... imgLines){
		this.lines = imgLines;
	}

	public ImageMessage appendText(String... text){
		for(int y = 0; y < this.lines.length; ++y){
			if(text.length <= y) continue;
			String[] arrstring = this.lines;
			int n = y;
			arrstring[n] = arrstring[n] + " " + text[y];
		}
		return this;
	}

	public ImageMessage appendCenteredText(String... text){
		for(int y = 0; y < this.lines.length; ++y){
			if(text.length <= y){
				return this;
			}
			int len = 65 - this.lines[y].length();
			this.lines[y] = this.lines[y] + this.center(text[y], len);
		}
		return this;
	}

	private ChatColor[][] toChatColorArray(BufferedImage image, int height){
		double ratio = (double) image.getHeight() / (double) image.getWidth();
		int width = (int) ((double) height / ratio);
		if(width > 10){
			width = 10;
		}
		BufferedImage resized = this.resizeImage(image, (int) ((double) height / ratio), height);
		ChatColor[][] chatImg = new ChatColor[resized.getWidth()][resized.getHeight()];
		for(int x = 0; x < resized.getWidth(); ++x){
			for(int y = 0; y < resized.getHeight(); ++y){
				ChatColor closest;
				int rgb = resized.getRGB(x, y);
				chatImg[x][y] = closest = this.getClosestChatColor(new Color(rgb, true));
			}
		}
		return chatImg;
	}

	private String[] toImgMessage(ChatColor[][] colors, char imgchar){
		String[] lines = new String[colors[0].length];
		for(int y = 0; y < colors[0].length; ++y){
			String line = "";
			for(int x = 0; x < colors.length; ++x){
				ChatColor color;
				line = line + ((color = colors[x][y]) != null ? new StringBuilder().append(colors[x][y].toString()).append(imgchar).toString() : Character.valueOf(' '));
			}
			lines[y] = line + ChatColor.RESET;
		}
		return lines;
	}

	private BufferedImage resizeImage(BufferedImage originalImage, int width, int height){
		AffineTransform af = new AffineTransform();
		af.scale((double) width / (double) originalImage.getWidth(), (double) height / (double) originalImage.getHeight());
		AffineTransformOp operation = new AffineTransformOp(af, 1);
		return operation.filter(originalImage, null);
	}

	private double getDistance(Color c1, Color c2){
		double rmean = (double) (c1.getRed() + c2.getRed()) / 2.0;
		double r = c1.getRed() - c2.getRed();
		double g = c1.getGreen() - c2.getGreen();
		int b = c1.getBlue() - c2.getBlue();
		double weightR = 2.0 + rmean / 256.0;
		double weightG = 4.0;
		double weightB = 2.0 + (255.0 - rmean) / 256.0;
		return weightR * r * r + weightG * g * g + weightB * (double) b * (double) b;
	}

	private boolean areIdentical(Color c1, Color c2){
		return Math.abs(c1.getRed() - c2.getRed()) <= 5 && Math.abs(c1.getGreen() - c2.getGreen()) <= 5 && Math.abs(c1.getBlue() - c2.getBlue()) <= 5;
	}

	private ChatColor getClosestChatColor(Color color){
		int i;
		if(color.getAlpha() < 128){
			return null;
		}
		int index = 0;
		double best = -1.0;
		for(i = 0; i < this.colors.length; ++i){
			if(!this.areIdentical(this.colors[i], color)) continue;
			return ChatColor.values()[i];
		}
		for(i = 0; i < this.colors.length; ++i){
			double distance = this.getDistance(color, this.colors[i]);
			if(distance >= best && best != -1.0) continue;
			best = distance;
			index = i;
		}
		return ChatColor.values()[index];
	}

	private String center(String s, int length){
		if(s.length() > length){
			return s.substring(0, length);
		}
		if(s.length() == length){
			return s;
		}
		int leftPadding = (length - s.length()) / 2;
		StringBuilder leftBuilder = new StringBuilder();
		for(int i = 0; i < leftPadding; ++i){
			leftBuilder.append(" ");
		}
		return leftBuilder.toString() + s;
	}

	public String[] getLines(){
		return this.lines;
	}

	public void sendToPlayer(Player player){
		for(String line : this.lines){
			player.sendMessage(line);
		}
	}
}

