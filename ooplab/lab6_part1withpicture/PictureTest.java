package lab6_part1withpicture;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class PictureTest {

	public static void main(String[] args) throws IOException{
		try {
			Tensor<Color> tensor1 = ImageProcessor.imageToTensor("img1.jpg");
			Tensor<Color> tensor2 = ImageProcessor.imageToTensor("img2.jpg");
			Tensor<Color> tensor3 = ImageProcessor.imageToTensor("img3.jpg");
			
			Tensor<Color> result1 = tensor1.add(tensor2);
		    Tensor<Color> result2 = tensor2.sub(tensor1);
		    Tensor<Color> result3 = tensor2.resize(256,256); 
	
		    ImageProcessor.saveAsJPG(result1, "result_1.jpg");
		    ImageProcessor.saveAsJPG(result2, "result_2.jpg");
		    ImageProcessor.saveAsJPG(result3, "result_3.jpg");
			} catch (IOException e) {
			e.printStackTrace();
			}
		    
		}
} 


class Tensor<T> {
    private List<Integer> shape;
    public List<T> data;
    public int capacity; 
    private Class<?> type; 
    public Tensor(List<Integer> shape, Class<?> type) {
        this.shape = shape;
        this.capacity = 1;
        for (int dim : shape) {
            capacity *= dim;
        }
        this.type = type;
        this.data = new ArrayList<>(Collections.nCopies(capacity, null));
    }
   
    public void setData(List<T> data) {
        if (data.size() != this.capacity) {
            throw new IllegalArgumentException("Data length must match tensor capacity!");
        }
        this.data = data;
    } 
     
    public T get(int index) {
        return data.get(index);
    }
   
    public T get(List<Integer> indices) {
        if (indices.size() != this.shape.size()) {
            throw new IllegalArgumentException("Indices length must match tensor dimension!");
        }
        int flatIndex = 0;
        for (int i = 0; i < indices.size(); i++) {
            flatIndex += indices.get(i) * this.strides(i);
        }
        return this.data.get(flatIndex);
    }
 
    public void set(int index, T value) {
        this.data.set(index, value);
    }
    
    public void set(List<Integer> indices, T value) {
        if (indices.size() != this.shape.size()) {
            throw new IllegalArgumentException("Indices length must match tensor dimension!");
        }
        int flatIndex = 0;
        for (int i = 0; i < indices.size(); i++) {
            flatIndex += indices.get(i) * this.strides(i);
        }
        this.data.set(flatIndex, value);
    }
 
    private int strides(int dim) {
        int stride = 1;
        for (int i = dim + 1; i < this.shape.size(); i++) {  
            stride *= this.shape.get(i);
        }
        return stride;
    }
 
    public List<Integer> shape() {
        return this.shape;
    }
 
    public Tensor<T> add(Tensor<T> tensor) {
        if (!this.shape.equals(tensor.shape)) {
            throw new IllegalArgumentException("Tensor shape must match!");
        }
        Tensor<T> result = new Tensor<>(this.shape, this.type);
        if(this.type == Color.class) {
        	    for (int i = 0; i < this.capacity; i++) {
        	        Color a = (Color)this.get(i);
        	        Color b = (Color)tensor.get(i);

        	        int red = Math.min(255, Math.max(0, a.getRed() + b.getRed()));
        	        int green = Math.min(255, Math.max(0, a.getGreen() + b.getGreen()));
        	        int blue = Math.min(255, Math.max(0, a.getBlue() + b.getBlue()));

        	        Color sumColor = new Color(red, green, blue);
        	        result.set(i, (T)sumColor);
        	    }
        }
        else {
             List<T> resultData = new ArrayList<>();
             for (int i = 0; i < this.capacity; i++) {
                   T value = (T)( (Double)(((Number) this.get(i)).doubleValue() + ((Number) tensor.get(i)).doubleValue()));
                   resultData.add(value);
             }
             result.setData(resultData);
        }
        return result;
    }
 
    public Tensor<T> sub(Tensor<T> tensor) {
        if (!this.shape.equals(tensor.shape)) {
            throw new IllegalArgumentException("Tensor shape must match!");
        }
        Tensor<T> result = new Tensor<>(this.shape, this.type);
        if(this.type == Color.class) {
        	 for (int i = 0; i < this.capacity; i++) {
     	        Color a = (Color)this.get(i);
     	        Color b = (Color)tensor.get(i);

     	        int red = Math.min(255, Math.max(0, a.getRed() - b.getRed()));
     	        int green = Math.min(255, Math.max(0, a.getGreen() - b.getGreen()));
     	        int blue = Math.min(255, Math.max(0, a.getBlue() - b.getBlue()));

     	        Color sumColor = new Color(red, green, blue);
     	        result.set(i, (T)sumColor);
     	    }
        }
        else {
             List<T> resultData = new ArrayList<>();
             for (int i = 0; i < this.capacity; i++) {
                T value = (T)(Double) (((Number) this.get(i)).doubleValue() - ((Number) tensor.get(i)).doubleValue());
                resultData.add(value);
        }
                result.setData(resultData);
        }
        return result;
    }
   
    public Tensor<T> slice(List<Integer> begin, List<Integer> end) {
        if (begin.size() != end.size()) {
            throw new IllegalArgumentException("Begin and end list length must match!");
        }
        List<Integer> shape = new ArrayList<>();
        List<Integer> strides = new ArrayList<>();
        for (int i = 0; i < begin.size(); i++) {
            if (begin.get(i) < 0 || begin.get(i) > this.shape.get(i) || end.get(i) < 0 || end.get(i) > this.shape.get(i)) {
                throw new IllegalArgumentException("Index out of bounds!");
            }
            shape.add(end.get(i) - begin.get(i));
            strides.add(this.strides(i));
        }
        Tensor<T> result = new Tensor<>(shape, this.type);
        List<T> resultData = new ArrayList<>();
        for (int i = 0; i < result.capacity; i++) {
            List<Integer> indices = new ArrayList<>();
            int flatIndex = i;
            for (int j = 0; j < begin.size(); j++) {
                int index = flatIndex / strides.get(j) + begin.get(j);
                indices.add(index);
                flatIndex %= strides.get(j);
            }
            T value = this.get(indices);
            resultData.add(value);
        }
        result.setData(resultData);
        return result;
    }
    
    public Tensor<T> pad(int padSize) {
        if (this.shape.size() != 4) {
            throw new IllegalArgumentException("Tensor must be 4D!");
        }
        List<Integer> shape = new ArrayList<>(this.shape);
        shape.set(2, shape.get(2) + padSize * 2);
        shape.set(3, shape.get(3) + padSize * 2);
        Tensor<T> result = new Tensor<>(shape, this.type);
        for(int n = 0;n < shape.get(0);n++) {
        	 for(int c = 0;c < shape.get(1);c++) {
                  for (int h = 0; h < shape.get(2); h++) {
                        for (int w = 0; w < shape.get(3); w++) {
                             if (h < padSize || h >= shape.get(2) - padSize || w < padSize || w >= shape.get(3) - padSize) {
                                  List<Integer> indices = new ArrayList<>();
                                  indices.add(n);
                                  indices.add(c);
                                  indices.add(h);
                                  indices.add(w);
                                  result.set(indices,(T)(Double)0.0);
                                  } 
                             else {
                                  List<Integer> indices = new ArrayList<>();
                                  indices.add(n);
                                  indices.add(c);
                                  indices.add(h);
                                  indices.add(w);
                                  List<Integer> origIndices = new ArrayList<>();
                                  origIndices.add(n);
                                  origIndices.add(c);                                  
                                  origIndices.add(h - padSize);                                  
                                  origIndices.add(w - padSize);
                                  T value = this.get(origIndices);                                
                                  result.set(indices, value);
                              }
                         }
                   }
             }
        }
        return result;
    }
   
   
    public Tensor<T> resize(int newH, int newW) {
        if (this.shape.size() != 4 || newH <= 0 || newW <= 0) {
            throw new IllegalArgumentException("Tensor must be 4D and new size must be positive!");
        }
        List<Integer> shape = new ArrayList<>(this.shape);
        shape.set(2, newH);
        shape.set(3, newW);
        Tensor<T> result = new Tensor<>(shape, this.type);
        for(int n = 0;n < shape.get(0);n++) {
       	   for(int c = 0;c < shape.get(1);c++) {
               for (int h = 0; h < newH; h++) {
                   for (int w = 0; w < newW; w++) {
                       int origH = h * this.shape.get(2) / newH;
                       int origW = w * this.shape.get(3) / newW;
                       List<Integer> origIndices = new ArrayList<>();
                       origIndices.add(n);
                       origIndices.add(c);
                       origIndices.add(origH);
                       origIndices.add(origW);
                       T value = this.get(origIndices);
                       List<Integer> indices = new ArrayList<>();
                       indices.add(n);
                       indices.add(c);
                       indices.add(h);
                       indices.add(w);
                       result.set(indices, value);
                    }
              }
       	   }
        }
        return result;
    }
}

class ImageProcessor { 
	public static Tensor<Color> imageToTensor(String filePath) throws IOException {
		BufferedImage img = ImageIO.read(new File(filePath));
	    int height = img.getHeight();
	    int width = img.getWidth();

	    List<Integer> shape = new ArrayList<>();
	    shape.add(1); // Batch size 
	    shape.add(3); // Channels 
	    shape.add(height);
	    shape.add(width);

	    Tensor<Color> tensor = new Tensor<>(shape, Color.class);

	    for (int h = 0; h < height; h++) {
	        for (int w = 0; w < width; w++) {
	            Color pixel = new Color(img.getRGB(w, h));
	            List<Integer> indices = new ArrayList<>();
	            indices.add(0); // Batch index
	            indices.add(0); // R
	            indices.add(h);
	            indices.add(w);
	            tensor.set(indices, pixel);
	            
	            indices.set(1, 1); // G
	            tensor.set(indices, pixel);
	            
	            indices.set(1, 2); // B
	            tensor.set(indices, pixel);
	        }
	    }

	    return tensor;
	}
	 
    public static void saveAsJPG(Tensor<Color> tensor, String filePath) throws IOException {
		List<Integer> shape = tensor.shape();
		int height = shape.get(2);
		int width = shape.get(3);
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int y = 0; y < height; y++) {
		     for (int x = 0; x < width; x++) {
		         Color color = tensor.get(List.of(0, 0, y, x));
		         image.setRGB(x, y, color.getRGB());
		         }
		    }
		    ImageIO.write(image, "jpg", new File(filePath));
		}
}



