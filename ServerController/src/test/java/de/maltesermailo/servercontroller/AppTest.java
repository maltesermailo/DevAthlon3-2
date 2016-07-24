package de.maltesermailo.servercontroller;

import java.io.File;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
    	System.out.println("Please wait while testing...");
    	
    	File f = new File("jars");
    	System.out.println(f.getAbsolutePath());
    	
        Bootstrap.main(new String[0]);
        ServerController.instance().addServer("test", "test", "1024M", "1024M", 25565);
        
        System.out.println("Starting test server");
        
        ServerController.instance().startServer("test");
        
        try {
			Thread.sleep(2000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        
        System.out.println("Stopping test server");
        
        ServerController.instance().stopServer("test");
        
        try {
			Thread.sleep(5500L);
		} catch (InterruptedException e) {
		}
        
        System.out.println("Removing test server");
        
        ServerController.instance().removeServer("test", true);
    }
}
