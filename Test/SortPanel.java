import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Insets;

import java.util.Random;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class SortPanel extends JPanel
{
  private JButton populateArray;
  private JButton startSort;
  private JComboBox sortAlgorithm;
  private SortAnimationPanel animationPanel;
  private static final String[] names = {"Heap Sort", "Quick Sort", "Merge Sort"};
  private int[] array;
  private String selection;
  private static final int width = 500;
  private static final int height = 500;
  private Thread t;
  
  public SortPanel()
  {
    //Seting the Layout of the Panel to GridBagLayout
    setLayout(new GridBagLayout());
    setPreferredSize(new Dimension(width, height));
    
    //Creating a new Button
    populateArray = new JButton("Populate Array");
    populateArray.setPreferredSize(new Dimension(40, 30));
    
    //Creating a new Button
    startSort = new JButton("Sort");
    startSort.setPreferredSize(new Dimension(40, 30));
    
    //Creating a new ComboBox
    sortAlgorithm = new JComboBox <String>(names);
    sortAlgorithm.setPreferredSize(new Dimension(40, 30));
    
    //Creating a new Panel
    animationPanel = new SortAnimationPanel();
    animationPanel.setPreferredSize(new Dimension(width, 100));
    
    //Declaring GridBagConstraints for Layout
    GridBagConstraints c = new GridBagConstraints();
    
    c.fill = GridBagConstraints.HORIZONTAL; //Fills the Panel in horizontal way
    c.gridwidth = 3;                        //Takes space of 3 columns
    c.ipady = 400;                          //Increases the size of the Component in Y-axis
    c.weightx = 0.5;                        //Generic weight for the Component
    c.gridx = 0;                            //X-axis location
    c.gridy = 0;                            //Y-axis location
    add(animationPanel, c);                 //Adding the component to the Panel
    
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridwidth = -2;
    c.weightx = 0.5;
    c.ipady = 0;
    c.anchor = GridBagConstraints.PAGE_END; //Making the component to spawn at the bottom of Panel
    c.gridx = 0;
    c.gridy = 1;
    add(populateArray, c);
    
    c.fill = GridBagConstraints.HORIZONTAL;
    c.weightx = 0.5;
    //Insets(top, left, bottom, right)
    c.insets = new Insets(10, 10, 0, 10);  //Placing a space of 10 on top, bottom, left and right
    c.anchor = GridBagConstraints.PAGE_END;
    c.gridx = 1;
    c.gridy = 1;
    add(startSort, c);
    
    c.fill = GridBagConstraints.HORIZONTAL;
    c.weightx = 0.5;
    c.anchor = GridBagConstraints.PAGE_END;
    c.gridx = 2;
    c.gridy = 1;
    add(sortAlgorithm, c);
    
    //Making the sort Button inaccessible to the user first
    startSort.setEnabled(false);
    
    //Action Event when Populate Array is clicked
    populateArray.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        array = new int[getSize().width-1];
        
        //Placing random integer values into the array
        for(int i = 0; i < array.length; i++)
        {
          Random random = new Random();
          array[i] = random.nextInt((getSize().height)-1);
        }
        animationPanel.repaint();         //Calling to update the display Panel;
        populateArray.setEnabled(false);
        startSort.setEnabled(true);
        
        //Action Event when an Sort Algorithm is selected from the Combo Box
        sortAlgorithm.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
            //Getting the selected option from Combo Box
            JComboBox combo = (JComboBox)e.getSource();
            String selectedString = (String)combo.getSelectedItem();
            selection = selectedString;
            
            //Action Event when Sort is clicked
            startSort.addActionListener(new ActionListener()
            {
              public void actionPerformed(ActionEvent e)
              {
                startSort.setEnabled(false);
                t = new Thread(animationPanel); //Creating a new Thread
                t.start();
                populateArray.setEnabled(true);
              }
           });
          }
        });
      }
    });
  }//End of method SortPanel()
  public class SortAnimationPanel extends JPanel implements Runnable
  {
    private HeapSort heapSort;
    private QuickSort quickSort;
    private MergeSort mergeSort;
    
    @Override
    protected void paintComponent(Graphics g)
    {
      super.paintComponent(g);
      //clearRect(x, y, width, height)
      g.clearRect(0, 0, width, height);        //Clearing the display Panel
       
        //Lines color to Blue
        g.setColor(Color.BLUE);
        for(int i = 0; i < array.length; i++)
        {
          g.drawLine(i, height, i, array[i]);
          repaint();                            //Updating the display Panel
        }
      
    }//End of method PaintComponent()
    public void run()
    {
      if(selection == "Heap Sort")
      {
        //Declaring and calling Heap Sort
        heapSort = new HeapSort();
        heapSort.sort(array);
      }
      else if(selection == "Quick Sort")
      {
        //Declaring and calling Quick Sort
        quickSort = new QuickSort();
        quickSort.sort(array, 0, array.length-1);
      }
      else if(selection == "Merge Sort")
      {
        //Declaring and calling Merge Sort
        mergeSort = new MergeSort();
        mergeSort.sort(array, 0, array.length-1);
      }
    }//End of method Run()
    public class HeapSort
    {
      public void sort(int arr[])
      {
          int n = array.length;
          //Build heap (rearrange array)
          for(int i = n/2 - 1; i >= 0; i--)
            heapify(array, n, i);
          //One by one extract an element from heap
          for(int i = n-1; i >= 0; i--)
          {
            //Move current root to end
            int temp = array[0];
            array[0] = array[i];
            array[i] = temp;
            //call max heapify on the reduced heap
            heapify(array, i, 0);
          }
      }
      //To heapify a subtree rooted with node i which is
      //an index in arr[]. n is size of heap
      void heapify(int array[], int n, int i)
      {
        try
        {
          int largest = i;  //Initialize largest as root
          int l = 2*i + 1;  //left = 2*i + 1
          int r = 2*i + 2;  //right = 2*i + 2
          //If left child is larger than root
          if(l < n && array[l] > array[largest])
            largest = l;
          //If right child is larger than largest so far
          if(r < n && array[r] > array[largest])
            largest = r;
          //If largest is not root
          if(largest != i)
          {
            int swap = array[i];
            array[i] = array[largest];
            array[largest] = swap;
            
            animationPanel.repaint();
            t.sleep(100);
            // Recursively heapify the affected sub-tree
            heapify(array, n, largest);
          }
        }catch (Exception e) {System.out.println("interrupted");}
      }
    }//End of method HeapSort()
    public class MergeSort
    {
      //Merges two subarrayays of array[].
      //First subarrayay is array[l..m]
      //Second subarrayay is array[m+1..r]
      public void merge(int array[], int l, int m, int r)
      {
        try
        {
          //Find sizes of two subarrayays to be merged
          int n1 = m - l + 1;
          int n2 = r - m;
          //Create temp arrayays
          int L[] = new int [n1];
          int R[] = new int [n2];
          //Copy data to temp arrayays
          for(int i = 0; i < n1; ++i)
            L[i] = array[l + i];
          for(int j = 0; j < n2; ++j)
            R[j] = array[m + 1 + j];
          //Merge the temp arrayays
          //Initial indexes of first and second subarrayays
          int i = 0, j = 0;
          //Initial index of merged subarrayy arrayay
          int k = l;
          while(i < n1 && j < n2)
          {   
            if(L[i] <= R[j])
            {
              array[k] = L[i];
              i++;
            }
            else
            {
              array[k] = R[j];
              j++;
            }
            k++;
          }
          //Copy remaining elements of L[] if any
          while(i < n1)
          {
            array[k] = L[i];
            i++;
            k++;
            animationPanel.repaint();
          }
          t.sleep(100);
          //Copy remaining elements of R[] if any
          while(j < n2)
          {
            array[k] = R[j];
            j++;
            k++;
            animationPanel.repaint();
          }
          t.sleep(100);
        }catch (Exception e) {System.out.println("interrupted");}
      }
      //Main function that sorts array[l..r] using
      //merge()
      public void sort(int array[], int l, int r)
      {
        if(l < r)
        {
          //Find the middle point
          int m = (l+r)/2;
          //Sort first and second halves
          sort(array, l, m);
          sort(array , m+1, r);
          //Merge the sorted halves
          merge(array, l, m, r);
        }
      }
    }//End of method MergeSort()
    public class QuickSort
    {
      //This function takes last element as pivot,
      //places the pivot element at its correct
      //position in sorted arrayay, and places all
      //smaller (smaller than pivot) to left of
      //pivot and all greater elements to right
      //of pivot
      int a;
      public int partition(int array[], int low, int high)
      {
        try
        {
          int pivot = array[high]; 
          int i = (low-1); //index of smaller element
          for(int j = low; j < high; j++)
          {
            //If current element is smaller than or
            //equal to pivot
            if(array[j] <= pivot)
            {
              i++;
              //swap array[i] and array[j]
              int temp = array[i];
              array[i] = array[j];
              array[j] = temp;
            }
          }
          //swap array[i+1] and array[high] (or pivot)
          int temp = array[i+1];
          array[i+1] = array[high];
          array[high] = temp;
          animationPanel.repaint();
          t.sleep(100);
          a = i+1;
        }catch (Exception e) {System.out.println("interrupted");}
        return a;
      }
      //The main function that implements QuickSort()
      //array[] --> Array to be sorted,
      //low  --> Starting index,
      //high  --> Ending index
      public void sort(int array[], int low, int high)
      {
        if (low < high)
        {
          //pi is partitioning index, array[pi] is 
          //now at right place
          int pi = partition(array, low, high);
          //Recursively sort elements before
          //partition and after partition
          sort(array, low, pi-1);
          sort(array, pi+1, high);
        }
      }
    }//End of method QuickSort()
  }//End of method SortAnimationPanel()
}//End of class SortPanel