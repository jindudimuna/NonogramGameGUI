package nonogram;



import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import javax.swing.JButton;
import java.awt.*;

/**
 * The test class PanelCellTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class PanelCellTest
{
    /**
     * Default constructor for test class PanelCellTest
     */
    public PanelCellTest()
    {
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp()
    {
    }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @After
    public void tearDown()
    {
    }
    
    
    @Test
     public void updateBackgroundTest(){
    
        NonogramPanel np = new NonogramPanel();
        PanelCell cell = new PanelCell(np, 0, 0, 0);
        cell.updateBackground(Nonogram.EMPTY);
        assertEquals(Color.WHITE, cell.getBackground());
        assertEquals("x", cell.getText());
        assertEquals(Color.RED, cell.getForeground());
        
           cell.updateBackground(Nonogram.FULL);
        assertEquals(Color.BLACK, cell.getBackground());
        assertEquals("", cell.getText());

         cell.updateBackground(Nonogram.UNKNOWN);
        assertEquals(Color.WHITE, cell.getBackground());
        assertEquals("", cell.getText());
    }
    
   //test for when it fails
   
   @Test
     public void updateBackgroundTestFail(){
        NonogramPanel np = new NonogramPanel();
        PanelCell cell = new PanelCell(np, 0, 0, 0);
        cell.updateBackground(Nonogram.EMPTY);
        if(!Color.WHITE.equals(cell.getBackground())){
    throw new AssertionError("Expected cell color white, but got " + cell.getBackground());  

}if(!"x".equals(cell.getText())){
    throw new AssertionError("Wrong text inputed");

} 
cell.updateBackground(Nonogram.FULL);
        if(!Color.BLACK.equals(cell.getBackground())){
    throw new AssertionError("Expected cell color BLACK, but got " + cell.getBackground());  
    
}

cell.updateBackground(Nonogram.UNKNOWN);
        if(!Color.WHITE.equals(cell.getBackground())){
    throw new AssertionError("Expected cell color WHITE, but got " + cell.getBackground());  
    
}
    }
    public void colors()
    {
    }

    @Test
    public void colorTest()
    {
    }
}
