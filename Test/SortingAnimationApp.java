import javax.swing.JFrame;
import java.awt.GridLayout;

public class SortingAnimationApp extends JFrame
{
  //Instantiating SortPanels
  private SortPanel leftPanel;
  private SortPanel rightPanel;
  public SortingAnimationApp() 
  {
    super("Sorting Animation");
    
    //Setting the Layout of the Frame to be GridLayout
    //which divides the frame in rows and columns
    //Here we require 1 row and 2 Columns
    setLayout(new GridLayout(1, 2));
    
    //Creating two new SortPanels
    leftPanel = new SortPanel();
    rightPanel = new SortPanel();
    
    //Adding the left and right Panels to the Frame
    add(leftPanel);
    add(rightPanel);
  }//End of method SortingAnimationApp()
  public static void main(String[] args)
  {
    SortingAnimationApp sortingApp = new SortingAnimationApp();
    sortingApp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    sortingApp.setSize(900, 500);
    sortingApp.setVisible(true);
  }//End of method Main()
}//End of class SortingAnimationApp
    