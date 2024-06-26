package lab6_part1;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Body {
    public static void main(String[] args) {
    	 addTest();
    	 subtractTest();
    	 sliceTest();
    	 padTest();
    	 resizeTest();
    }
    
    private static void addTest() {
    	System.out.println("1.There is the test for addition method.");
    	List<Integer> shape1 = Arrays.asList(3,3,3);
    	Class<?> type = Integer.class;
        Tensor<Integer> tensor1 = new Tensor<Integer>(shape1,type);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    tensor1.set(Arrays.asList(i, j, k),i+j+k);
                }
            }
        }

        for (int i = 0; i < 3; i++) {
            System.out.println("Sum[" + i + "]:");
            for (int j = 0; j < 3; j++) {
            	for (int k = 0; k < 3; k++) {
                    System.out.print(tensor1.get(Arrays.asList(i,j,k)) + " ");
            	}
            	System.out.println();
            }
            System.out.println("-----------");
        }

        System.out.println("Tensor1 dimensions: " +  tensor1.shape());

        List<Integer> shape2 = Arrays.asList(3,3,3);
        Tensor tensor2 = new Tensor(shape2,type);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    tensor2.set(Arrays.asList(i,j,k),i*j*k);
                }
            }
        }

        System.out.println("Tensor2 dimensions: " + tensor2.shape());

        for (int i = 0; i < 3; i++) {
            System.out.println("Sum[" + i + "]:");
            for (int j = 0; j < 3; j++) {
            	for (int k = 0; k < 3; k++) {
                System.out.print(tensor2.get(Arrays.asList(i,j,k)) + " ");
            	}
            	System.out.println();
            }
            System.out.println("-----------");
        }

        Tensor sum = tensor1.add(tensor2);

        System.out.println("Sum dimensions: " +  sum.shape());
        
        for (int i = 0; i < 3; i++) {
            System.out.println("Sum[" + i + "]:");
            for (int j = 0; j < 3; j++) {
            	for (int k = 0; k < 3; k++) {
                System.out.print(sum.get(Arrays.asList(i,j,k)) + " ");
                }
            	System.out.println();
            }
            System.out.println("-----------");
        }
    }

    private static void subtractTest() {
    	System.out.println("2.There is the test for subtraction method.");
    	List<Integer> shape1 = Arrays.asList(3,3,3);
    	Class<?> type = Integer.class;
        Tensor<Integer> tensor1 = new Tensor<Integer>(shape1,type);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    tensor1.set(Arrays.asList(i, j, k),i+j+k);
                }
            }
        }

        for (int i = 0; i < 3; i++) {
            System.out.println("Sum[" + i + "]:");
            for (int j = 0; j < 3; j++) {
            	for (int k = 0; k < 3; k++) {
                    System.out.print(tensor1.get(Arrays.asList(i,j,k)) + " ");
            	}
            	System.out.println();
            }
            System.out.println("-----------");
        }

        System.out.println("Tensor1 dimensions: " +  tensor1.shape());

        List<Integer> shape2 = Arrays.asList(3,3,3);
        Tensor tensor2 = new Tensor(shape2,type);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    tensor2.set(Arrays.asList(i,j,k),i*j*k);
                }
            }
        }

        System.out.println("Tensor2 dimensions: " + tensor2.shape());

        for (int i = 0; i < 3; i++) {
            System.out.println("Sum[" + i + "]:");
            for (int j = 0; j < 3; j++) {
            	for (int k = 0; k < 3; k++) {
                System.out.print(tensor2.get(Arrays.asList(i,j,k)) + " ");
            	}
            	System.out.println();
            }
            System.out.println("-----------");
        }

        Tensor sum = tensor1.sub(tensor2);

        System.out.println("Sum dimensions: " +  sum.shape());
        
        for (int i = 0; i < 3; i++) {
            System.out.println("Sum[" + i + "]:");
            for (int j = 0; j < 3; j++) {
            	for (int k = 0; k < 3; k++) {
                System.out.print(sum.get(Arrays.asList(i,j,k)) + " ");
                }
            	System.out.println();
            }
            System.out.println("-----------");
        }
    }
    
    private static void sliceTest() {
    	System.out.println("3.There is the test for slice method.");
    	Class<?> type = Integer.class;
    	List<Integer> originalShape = Arrays.asList(2, 3, 4);
    	Tensor<Integer> tensor = new Tensor<>(originalShape, type);

    	for (int i = 0; i < tensor.capacity; i++) {
    	    tensor.set(Arrays.asList(i % 2, i % 3, i % 4), i);
    	}

    	List<Integer> begin = Arrays.asList(0, 0, 1);
    	List<Integer> end = Arrays.asList(2, 3, 3);
    	Tensor<Integer> slicedTensor = tensor.slice(begin, end);

    	System.out.println("Sliced Tensor Shape: " + slicedTensor.shape());
    	for (int i = 0; i < slicedTensor.data.size(); i++) {
    	    System.out.println("Element [" + i + "] = " + slicedTensor.data.get(i));
    	}

    }
    
    private static void padTest() {
    	System.out.println("4.There is the test for pad method.");
    	Tensor<Integer> originalTensor = new Tensor<>(Arrays.asList(1, 1, 3, 3), Integer.class);
        originalTensor.set(new ArrayList<>(Arrays.asList(0, 0, 0, 0)), 0);
        originalTensor.set(new ArrayList<>(Arrays.asList(0, 0, 1, 1)), 2);
        originalTensor.set(new ArrayList<>(Arrays.asList(0, 0, 2, 2)), 3);

        int padSize = 2;
        Tensor<Integer> paddedTensor = originalTensor.pad(padSize);

        System.out.println("Original tensor:");
        for (int i = 0; i < originalTensor.shape().get(2); i++) {
            for (int j = 0; j < originalTensor.shape().get(3); j++) {
                List<Integer> indices = new ArrayList<>();
                indices.add(0);
                indices.add(0);
                indices.add(i);
                indices.add(j);
                System.out.printf(originalTensor.get(indices) + "\t");
            }
            System.out.println();
        }
 
        System.out.println("\nPadded tensor:");
        for (int i = 0; i < paddedTensor.shape().get(2); i++) {
            for (int j = 0; j < paddedTensor.shape().get(3); j++) {
                List<Integer> indices = new ArrayList<>();
                indices.add(0);
                indices.add(0);
                indices.add(i);
                indices.add(j);
                System.out.printf(paddedTensor.get(indices) + "\t");
            }
            System.out.println();
        }
    }
    
    private static void resizeTest() {
    	System.out.println("5.There is the test for resize method.");
    	Tensor<Double> tensor = new Tensor<>(Arrays.asList(1, 1, 2, 2), Double.class);
        tensor.set(Arrays.asList(0, 0, 0, 0), 1.0);
        tensor.set(Arrays.asList(0, 0, 1, 0), 2.0);
        tensor.set(Arrays.asList(0, 0, 0, 1), 3.0);
        tensor.set(Arrays.asList(0, 0, 1, 1), 4.0);

        System.out.println("Original tensor:");
        for (int i = 0; i < tensor.shape().get(2); i++) {
            for (int j = 0; j < tensor.shape().get(3); j++) {
                List<Integer> indices = new ArrayList<>();
                indices.add(0);
                indices.add(0);
                indices.add(i);
                indices.add(j);
                System.out.printf(tensor.get(indices) + "\t");
            }
            System.out.println();
        }

        Tensor<Double> resizedTensor = tensor.resize(3, 3);
        
        System.out.println("\nResized tensor:");
        for (int i = 0; i < resizedTensor.shape().get(2); i++) {
            for (int j = 0; j < resizedTensor.shape().get(3); j++) {
                List<Integer> indices = new ArrayList<>();
                indices.add(0);
                indices.add(0);
                indices.add(i);
                indices.add(j);
                System.out.printf(resizedTensor.get(indices) + "\t");
            }
            System.out.println();
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

