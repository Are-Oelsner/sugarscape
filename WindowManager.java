import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;

public abstract class WindowManager extends JFrame
                                    implements ContainerListener,  // whenever components are added
                                               MouseListener,      // whenever mouse events occur
                                               KeyListener,        // whenever keystrokes occur
                                               FocusListener,      // whenever gain/lose-focus events occur
                                               ActionListener,
                                               ChangeListener
                                              
{
    //======================================================================
    //* public WindowManager()
    //* Default constructor for this abstract class.
    //======================================================================
    public WindowManager()
    {
        super();           // call the JFrame default constructor (no explicit size)
        setVisible(true);  // then make the window visible

        // make this window listen for when components are added to it
        getContentPane().addContainerListener( this );
    }
    
    //======================================================================
    //* public WindowManager( int width, int height )
    //* Width/height constructor for this abstract class.
    //======================================================================
    public WindowManager( int width, int height )
    {
        // actions are the same as the default constructor, except also
        // set the width and height of the window

        super();
        setSize( width, height );
        setVisible( true );
        getContentPane().addContainerListener( this );        
    }
    
    //======================================================================
    //* public WindowManager( String title, int width, int height )
    //* Title/width/height constructor for this abstract class.
    //======================================================================
    public WindowManager( String title, int width, int height )
    {
        // actions are the same as for the previous constructor, except also
        // set the title of the window

        super();
        setSize( width, height );
        setTitle( title );
        setVisible( true );
        getContentPane().addContainerListener( this );        
    }
    
    //======================================================================
    //* public void componentAdded( ContainerEvent event ) 
    //* public void componentRemoved( ContainerEvent event ) 
    //* These two methods must be implemented because we implement the
    //* ContainerListener interface.
    //======================================================================
    public void componentAdded( ContainerEvent event ) 
    { 
        validate();  // just redraw to show all components added
    }
    public void componentRemoved( ContainerEvent event ) 
    { 
        validate();  // just redraw to show all components remaining
    }
    
    //======================================================================
    //* public void mouseEntered(MouseEvent event)
    //* public void mouseExited(MouseEvent event)
    //* public void mousePressed(MouseEvent event)
    //* public void mouseReleased(MouseEvent event)
    //* public void mouseClicked(MouseEvent event)
    //* These five methods must be implemented because we implement the
    //* MouseListener interface.  Because we're only interested in mouse
    //* clicks at this point, the first four can be stubs.
    //======================================================================
    public void mouseEntered(MouseEvent event)  {}
    public void mouseExited(MouseEvent event)   {}
    public void mousePressed(MouseEvent event)  {}
    public void mouseReleased(MouseEvent event) {}
    
    public void mouseClicked(MouseEvent event)
    {
        // grab onto the component that was clicked
        Component clickedComponent = event.getComponent(); 

        // get the class type of that component (JLabel, JButton, etc.)
        String componentType = clickedComponent.getClass().getName(); 

        // if the class type is a JLabel, call the abstract method 
        // to be implemented by the extending class 
        if ( componentType.contains( "JLabel" ) ) 
        { 
            labelClicked( (JLabel) clickedComponent );   // call the correct abstract method
        }
    }
    
    //======================================================================
    //* public void keyPressed( KeyEvent event )
    //* public void keyReleased( KeyEvent event )
    //* public void keyTyped( KeyEvent event )
    //* These three methods must be implemented because we implement the
    //* KeyListener interface.  Because we are only interested when a key
    //* is actually typed (not just pressed down or released), the first
    //* two can be stubs.
    //======================================================================
    public void keyPressed( KeyEvent event ) {}
    public void keyReleased( KeyEvent event ) {}

    public void keyTyped( KeyEvent event )
    {
        // if the user types the "enter" key...
        if ( event.getKeyChar() == '\n' )
        {
            // grab onto the corresponding component (a JTextField)...
            Component component = event.getComponent();  

            // and call the correct abstract method
            textEntered( (JTextField) component );
        }
    }
    
    //======================================================================
    //* public void focusGained( FocusEvent event ) {}
    //* public void focusLost( FocusEvent event )
    //* These two methods must be implemented because we implement the
    //* FocusListener interface.  Because we are only interested when focus
    //* is lost at this point, the first method can be a stub.
    //======================================================================
    public void focusGained( FocusEvent event ) {}

    public void focusLost( FocusEvent event )
    {
        // grab onto the component that lost focus
        Component component = event.getComponent(); 

        // get the class type of that component (JLabel, JButton, etc.)
        String componentType = component.getClass().getName(); 

        if (componentType.contains("JTextField"))
        {
            focusLost( (JTextField) component );  // call the correct abstract method
        }
        else if (componentType.contains("JButton"))
        {
            focusLost( (JButton) component );  // call the correct abstract method
        }
    }
    
    //======================================================================
    //* public void actionPerformed( ActionEvent event )
    //* This method is implemented because we implement the ActionListener 
    //* interface.
    //======================================================================
    public void actionPerformed( ActionEvent event )
    {
        Object component = event.getSource();
        String componentType = component.getClass().getName();
        
        if (componentType.contains("JComboBox"))
        {
            selectionMade( (JComboBox) component );
        }
        else if (componentType.contains("JMenuItem"))
        {
            selectionMade( (JMenuItem) component );
        }
        else if (componentType.contains("JButton"))
        {
            buttonClicked( (JButton) component ); // call the correct abstract method
        }
    }
    
    //======================================================================
    //* public void stateChanged( ChangeEvent event )
    //* This method is implemented because we implement the ChangeListener 
    //* interface.
    //======================================================================
    public void stateChanged( ChangeEvent event )
    {
        Object component = event.getSource();
        String componentType = component.getClass().getName();
        
        if (componentType.contains("JSlider"))
        {
            sliderChanged( (JSlider) component );
        }        
    }

    // The following five methods can be overridden by any class extending
    // this class.  The extending class can decide on the specifics of what to
    // do in the case of each type of event occurring.

    public void labelClicked(  JLabel whichLabel )       {}
    public void textEntered(   JTextField whichField )   {}
    public void buttonClicked( JButton whichButton )     {}
    public void focusLost(     JTextField whichField )   {}
    public void focusLost(     JButton whichButton )     {}

    public void sliderChanged( JSlider whichSlider)      {}
    public void selectionMade( JComboBox whichComboBox ) {}
    public void selectionMade( JMenuItem whichItem )     {}
}
