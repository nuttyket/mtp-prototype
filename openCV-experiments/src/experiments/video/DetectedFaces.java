package experiments.video;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;

public class DetectedFaces {
	private BufferedImage imageFrame;
	private byte[] imageByteArr;
	private ArrayList<int[]> faceRectangles;
	
	public BufferedImage getImageFrame() {
		return imageFrame;
	}
	public void setImageFrame(BufferedImage imageFrame) throws IOException {
		this.imageFrame = imageFrame;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(imageFrame, "jpg", baos);
		baos.flush();
		setImageByteArr(baos.toByteArray());
	}
	
	public void addDetectedFace(int x, int y, int i, int j) {
		if(faceRectangles == null) faceRectangles = new ArrayList<int[]>();
		
		int[] faceRect = new int[4];
		faceRect[0] = x;
		faceRect[1] = y;
		faceRect[2] = i;
		faceRect[3] = j;
		faceRectangles.add(faceRect);
	}
	
	public int[] getFirstDetectedFace() {
		return faceRectangles.get(0);
	}
	@Override
	public String toString() {
		String outString = "";
		for(int counter=0; counter < faceRectangles.size(); counter++) {
			outString += faceRectangles.get(counter)[0] + "," +
						faceRectangles.get(counter)[1] + "," +
						faceRectangles.get(counter)[2] + "," +
						faceRectangles.get(counter)[3];
		}

		return outString + " ImageBytes : " + Arrays.toString(imageByteArr);
	}
	public String getImageByteArr() {
		return Arrays.toString(imageByteArr);
	}
	public void setImageByteArr(byte[] imageByteArr) {
		this.imageByteArr = imageByteArr;
	}
}
