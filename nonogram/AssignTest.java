package nonogram;


import org.junit.jupiter.api.function.Executable;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class AssignTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class AssignTest
{
    /**
     * Default constructor for test class AssignTest
     */
    public AssignTest()
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
  public void testValidConstructor() {
        Assign a = new Assign(1, 2, Nonogram.FULL);
        assertEquals(1, a.getRow());     
        assertEquals(2, a.getCol());     
        assertEquals(Nonogram.FULL, a.getState());   
    }   
    

  @Test
   public void invalidRowTest(){
        assertThrows(IllegalArgumentException.class, new Executable(){
        @Override
        public void execute() throws Throwable{
            new Assign(-2,2, Nonogram.FULL);
        }
            });
    }
     

    
    @Test
    public void invalidRowTestFornewSets(){
        assertThrows(IllegalArgumentException.class, new Executable(){
        @Override
        public void execute() throws Throwable{
            new Assign(-1,1, Nonogram.FULL);
        }
            });
    }
        @Test
      public  void invalidColTest(){
        assertThrows(IllegalArgumentException.class, new Executable(){
        @Override
        public void execute() throws Throwable{
        new Assign(1,-1, Nonogram.FULL);
        }
        });
        }
        @Test
      public  void invalidColTestFornewRange(){
        assertThrows(IllegalArgumentException.class, new Executable(){
        @Override
        public void execute() throws Throwable{
        new Assign(0,-3, Nonogram.FULL);
        }
        });
        }
    @Test
    public void test()
    {
    }




  
    
}
