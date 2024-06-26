package lab6;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.awt.Color;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;

public class Batch_test {
    public static void main(String[] args) throws IOException {
    	File figsDirectory = new File("./figs/");
    	File resultsDirectory = new File("./results/");
        if (!resultsDirectory.exists())
        	resultsDirectory.mkdirs();
        
        for (int i = 1; i <= 100; i++) { 
            File group = new File(figsDirectory, String.format("%03d", i));
            if (!group.isDirectory()) 
            	continue;
            Tensor<Integer> input = ImageProcessor.imageToTensor(group.getPath());
            Tensor<Integer> result = input.conv();
            Tensor<Integer> resultimg = input.convimg();
            String outFileName = String.format("%03d_result.txt", i);
            File output = new File(resultsDirectory,outFileName);
            String folderPath = String.format("%03d_img", i);;
            ImageProcessor.saveImages(resultimg,folderPath);             
            try {
                FileWriter writer = new FileWriter(output);
                for (Integer item : result.data) {
                       writer.write(item.toString());
                   }
               } catch (IOException e) {
                   e.printStackTrace();
               }
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
        // 不再预先填充null，而是直接用传入的数据替换现有的数据
        this.data.clear();
        this.data.addAll(data);
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
    
    public Tensor<T> conv() throws FileNotFoundException {
    	File file = new File("./conv_kernel.txt");
        Scanner scanner = new Scanner(file);

        List<Integer> shape = new ArrayList<>();
        while (scanner.hasNextInt()) {
            shape.add(scanner.nextInt());
            if (shape.size() == 4) 
               break;
        }
        scanner.nextLine();

        List<Integer> dataList = new ArrayList<>(); 
        while (scanner.hasNext()) {
            dataList.add((Integer)(scanner.nextInt()));
         }
        scanner.close();

        Tensor<Integer> kernel = new Tensor<>(shape,Integer.class);
        kernel.setData(dataList);
        if (this.shape().size() != 4 || kernel.shape().size() != 4) {
            throw new IllegalArgumentException("Input and kernel must be 4D tensor!");
        }
        if (this.shape().get(1) != kernel.shape().get(1)) {
            throw new IllegalArgumentException("Input and kernel channel must match!");
        }
        int inputH = this.shape().get(2);
        int inputW = this.shape().get(3);
        int kernelH = kernel.shape().get(2);
        int kernelW = kernel.shape().get(3);
        int padSize = 2;  
        int stride = 1;
        Tensor<T> paddedInput = this.pad(padSize);
        int batchSize = this.shape().get(0);
        int outputChannels = kernel.shape().get(0);
        int outputHeight = (inputH + 2 * padSize - kernelH) / stride + 1;
        int outputWidth = (inputW + 2 * padSize - kernelW) / stride + 1;
        Tensor<T> output = new Tensor<>(Arrays.asList(this.shape().get(0), kernel.shape().get(0), (inputH + 2 * padSize - kernelH) / stride + 1, (inputW + 2 * padSize - kernelW) / stride + 1), Integer.class);
        for (int b = 0; b < batchSize; b++) {
            for (int oc = 0; oc < outputChannels; oc++) {
                for (int oh = 0; oh < outputHeight; oh++) {
                    for (int ow = 0; ow < outputWidth; ow++) {
                        for (int ic = 0; ic < 3; ic++) {
                        	int value = 0;
                            for (int kh = 0; kh < kernel.shape.get(2); kh++) {
                                for (int kw = 0; kw < kernel.shape.get(3); kw++) {
                                    value += (Integer)paddedInput.get(Arrays.asList(b,ic,oh + kh,ow + kw)) * kernel.get(Arrays.asList(oc,ic,kh,kw));
                                }
                            }
                            output.set(Arrays.asList(b,oc,oh,ow), (T)(Integer)value);
                        }
                    }
                }
            }
        }
    
        return output;
    }
    
    public Tensor<T> convimg() throws FileNotFoundException {
    	File file = new File("./conv_kernel.txt");
        Scanner scanner = new Scanner(file);

        List<Integer> shape = new ArrayList<>();
        while (scanner.hasNextInt()) {
            shape.add(scanner.nextInt());
            if (shape.size() == 4) 
               break;
        }
        scanner.nextLine();

        List<Integer> dataList = new ArrayList<>(); 
        while (scanner.hasNext()) {
            dataList.add((Integer)(scanner.nextInt()));
         }
        scanner.close();

        Tensor<Integer> kernel = new Tensor<>(shape,Integer.class);
        kernel.setData(dataList);
        if (this.shape().size() != 4 || kernel.shape().size() != 4) {
            throw new IllegalArgumentException("Input and kernel must be 4D tensor!");
        }
        if (this.shape().get(1) != kernel.shape().get(1)) {
            throw new IllegalArgumentException("Input and kernel channel must match!");
        }
        int inputH = this.shape().get(2);
        int inputW = this.shape().get(3);
        int kernelH = kernel.shape().get(2);
        int kernelW = kernel.shape().get(3);
        int padSize = 2;  
        int stride = 1;
        Tensor<T> paddedInput = this.pad(padSize);
        int batchSize = this.shape().get(0);
        int outputChannels = kernel.shape().get(0);
        int outputHeight = (inputH + 2 * padSize - kernelH) / stride + 1;
        int outputWidth = (inputW + 2 * padSize - kernelW) / stride + 1;
        Tensor<T> output = new Tensor<>(Arrays.asList(this.shape().get(0), kernel.shape().get(0), (inputH + 2 * padSize - kernelH) / stride + 1, (inputW + 2 * padSize - kernelW) / stride + 1), Integer.class);
        for (int b = 0; b < batchSize; b++) {
            for (int oc = 0; oc < outputChannels; oc++) {
                for (int oh = 0; oh < outputHeight; oh++) {
                    for (int ow = 0; ow < outputWidth; ow++) {
                        for (int ic = 0; ic < 3; ic++) {
                        	int value = 0;
                            for (int kh = 0; kh < kernel.shape.get(2); kh++) {
                                for (int kw = 0; kw < kernel.shape.get(3); kw++) {
                                    value += (Integer)paddedInput.get(Arrays.asList(b,ic,oh + kh,ow + kw)) * kernel.get(Arrays.asList(oc,ic,kh,kw));
                                }
                            }
                            if(value < 0)
                            	value = 0;
                            else if(value > 255)
                                value = 255;
                            output.set(Arrays.asList(b,oc,oh,ow), (T)(Integer)value);
                        }
                    }
                }
            }
        }
    
        return output;
    }
    
}

class ImageProcessor { 

	public static Tensor<Integer> imageToTensor(String folderPath) throws IOException {
		File folder = new File(folderPath);
	    File[] imageFiles = folder.listFiles();
	    int numImages = imageFiles.length;
	    
	    BufferedImage firstImage = ImageIO.read(imageFiles[0]);
	    int height = firstImage.getHeight();
	    int width = firstImage.getWidth();
	    
	    List<Integer> inputShape = Arrays.asList(numImages,3,height,width);
	    Tensor<Integer> input = new Tensor<Integer>(inputShape,Integer.class);
	    
	    for (int i = 0; i < numImages; i++) {
	        BufferedImage image = ImageIO.read(imageFiles[i]);
	        for (int c = 0; c < 3; c++) {
	            for (int h = 0; h < height; h++) {
	                for (int w = 0; w < width; w++) {
	                    int pixelValue = image.getRaster().getSample(w, h, c);
	                    input.set(Arrays.asList(i,c,h,w), pixelValue);
	                }
	            }
	        }
	    }	    
	    return input;
	}
	
	public static void saveImages(Tensor<Integer> tensor,String folderPath) {
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        int imageCount = tensor.shape().get(0);
        int channels = tensor.shape().get(1);
        int height = tensor.shape().get(2);
        int width = tensor.shape().get(3);
        for (int i = 0; i < imageCount; i++) {
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (int c = 0; c < channels; c++) {
                for (int h = 0; h < height; h++) {
                    for (int w = 0; w < width; w++) {
                        int pixelValue = (int) (tensor.get(Arrays.asList(i,c,h,w)) * 255.0f);
                        image.setRGB(w, h, pixelValue << 16 | pixelValue << 8 | pixelValue);
                    }
                }
            }

            String imageName = String.format("%02d", i+1) + ".jpg";
            File outputFile = new File(folderPath, imageName);

            try {
                ImageIO.write(image, "jpg", outputFile);
                System.out.println("Image " + (i+1) + " saved successfully.");
            } catch (IOException e) {
                System.err.println("Error saving image: " + e.getMessage());
            }
        }
    }         
}




