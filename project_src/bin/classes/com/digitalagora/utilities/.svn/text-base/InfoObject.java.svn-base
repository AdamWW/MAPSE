 /* Copyright (c) 2009 Alan Lupsha
  * Please do not redistribute this class without prior consent.
 */
 package com.digitalagora.utilities;

/*
 * This is one of the most generic info object you can have. It's an object that has
 * a name and zero or more child elements, of the same type (InfoObject). This is
 * useful to build multi-level parent-child relationships.
 * (c)2008 Wolf Saddle
 */

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import java.io.Serializable;

import java.util.Vector;

public class InfoObject implements Serializable {

    // "version 1.0 9/18/2008 (c)Alan Lupsha";
    String id = "No id defined";
    private Vector attributesVector = null;
    private Vector childrenVector = null;
    
    public InfoObject() {
        attributesVector = new Vector();
        childrenVector = new Vector();
    }
    public InfoObject( String id ) {
        attributesVector = new Vector();
        childrenVector = new Vector();
        this.id = id;
    }
    public String getId() {
        return id;
    }
    
    public int numChildren() {
        return childrenVector.size();
    }
    
    public Vector getChildrenVector() {
        return childrenVector;
    }
    // *********************************************************************************************************
    public InfoObject popFirstChild() {
        InfoObject returnInfoObject = new InfoObject("");

        if( childrenVector.size() > 0 )
        {
            returnInfoObject = (InfoObject)childrenVector.elementAt(0);
            childrenVector.removeElementAt(0); // remove it
        }
        return returnInfoObject;
    }
    // *********************************************************************************************************
    public InfoObject getFirstChild() {
        InfoObject returnInfoObject = new InfoObject("");

        if( childrenVector.size() > 0 )
            returnInfoObject = (InfoObject)childrenVector.elementAt(0);

        return returnInfoObject;
    }
    public InfoObject getFirstAttribute() {
        InfoObject returnInfoObject = new InfoObject("");

        if( attributesVector.size() > 0 )
            returnInfoObject = (InfoObject)attributesVector.elementAt(0);

        return returnInfoObject;
    }
    // *********************************************************************************************************
    public Vector getChildrenById( String commonChildrenId ) {
        Vector returnVector = new Vector();
        if( childrenVector.size() <= 0 )
            return returnVector; // in case there are absolutely no children

        for( int i = 0; i < childrenVector.size(); i++ ) {
        
            // if we have a matching id, get that element from the array, add it to our result
            if( ((InfoObject)childrenVector.elementAt(i)).matchesId( commonChildrenId ) ) {
                returnVector.add( (InfoObject)childrenVector.elementAt(i) );
                
            }
        }
        return returnVector;
    }
    public Vector getAttributesById( String commonAttributeId ) {
        Vector returnVector = new Vector();
        if( attributesVector.size() <= 0 )
            return returnVector; // in case there are absolutely no children

        for( int i = 0; i < attributesVector.size(); i++ ) {
        
            // if we have a matching id, get that element from the array, add it to our result
            if( ((InfoObject)attributesVector.elementAt(i)).matchesId( commonAttributeId ) ) {
                returnVector.add( (InfoObject)attributesVector.elementAt(i) );
                
            }
        }
        return returnVector;
    }
    // *********************************************************************************************************
    public String get( String pathString ) // ex: "ResultSet/Result/Latitude/"
    {
        // remove trailing slash
        if( pathString.substring( pathString.length()-1, pathString.length() ).equals("/") )
            pathString = pathString.substring(0, pathString.length()-1);
    
        if( pathString.indexOf("/") == -1 ) // there is no slash
            return getChildById( pathString ).getFirstChild().getId();
            
        // ex: ResultSet/
        String firstPart = pathString.substring(0, pathString.indexOf("/"));
        String secondPart = "";

        // if they passed this object's id, then take it off, ex, pathString becomes Result/Latitude
        if( getId().equals(firstPart) )
        {
            pathString = pathString.substring( pathString.indexOf("/") + 1, pathString.length() );
            
            // ex: Result
            firstPart = pathString.substring(0, pathString.indexOf("/"));
        }

        // ex: Latitude/
        secondPart = pathString.substring( pathString.indexOf("/") + 1 , pathString.length() );
    
        
        InfoObject childInfoObject = getChildById( firstPart );
        return childInfoObject.get(secondPart);
    }
    
    
    
    public String getChildByIdAsString( String idOfChildToBeGotten ) {
        return getChildById( idOfChildToBeGotten ).getId();
    }
    public InfoObject getChildById( String idOfChildToBeGotten ) {
        InfoObject returnInfoObject = new InfoObject("no child found with id:" + idOfChildToBeGotten );
        
        for( int i = 0; i < childrenVector.size(); i++ ) {
        
            // if we have a matching id, get that element from the array.
            if( ((InfoObject)childrenVector.elementAt(i)).matchesId( idOfChildToBeGotten ) )
                returnInfoObject = (InfoObject)childrenVector.elementAt(i);
        }
        return returnInfoObject;
    }

    public String getAttributeByIdAsString( String idOfAttributeToBeGotten ) {
        return getAttributeById(idOfAttributeToBeGotten).getId();
    }
    public InfoObject getAttributeById( String idOfAttributeToBeGotten ) {
        InfoObject returnInfoObject = new InfoObject("no attribute found with id:" + idOfAttributeToBeGotten );
        
        for( int i = 0; i < attributesVector.size(); i++ ) {
        
            // if we have a matching id, get that element from the array.
            if( ((InfoObject)attributesVector.elementAt(i)).matchesId( idOfAttributeToBeGotten ) )
                returnInfoObject = (InfoObject)attributesVector.elementAt(i);
        }
        return returnInfoObject;
    }
    // *********************************************************************************************************
    public void removeChildById( String idOfChildToBeRemoved ) {
        for( int i = 0; i < childrenVector.size(); i++ ) {
        
            // if we have a matching id, remove that element from the array.
            if( ((InfoObject)childrenVector.elementAt(i)).matchesId( idOfChildToBeRemoved ) )
                childrenVector.removeElementAt( i );
        }
    }
    public void addChildAllowDuplicates( InfoObject infoObjectChild ) {
        childrenVector.add( infoObjectChild );
    }
    // *********************************************************************************************************
    // Check if any of the children of this object have a matching id
    // *********************************************************************************************************
    public boolean hasChildById( String childId )
    {
        boolean r = false;
        
        for( int i = 0; i < childrenVector.size(); i++ )
        {
            if( ((InfoObject)childrenVector.elementAt(i)).matchesId( childId ) )
            {
                r = true;
                break;
            }
        }
        return r;
    }
    // *********************************************************************************************************
    // Check if any of the attributes of this object have a matching id
    // *********************************************************************************************************
    public boolean hasAttributeById( String attributeId )
    {
        boolean r = false;
        
        for( int i = 0; i < attributesVector.size(); i++ )
        {
            if( ((InfoObject)attributesVector.elementAt(i)).matchesId( attributeId ) )
            {
                r = true;
                break;
            }
        }
        return r;
    }
    // *********************************************************************************************************
    // *********************************************************************************************************
    // *********************************************************************************************************
    public void addAttribute( InfoObject attributeInfoObject )
    {
        attributesVector.add( attributeInfoObject );
    }

    public Vector getAttributesVector()
    {
        return attributesVector;
    }

    public void add( String keyString )
    {
        InfoObject childInfoObject = new InfoObject( keyString );
        addChildAllowDuplicates( childInfoObject );
    }

    public void add( String keyString, String childString )
    {
        InfoObject childInfoObject = new InfoObject( keyString );
            InfoObject grandChildInfoObject = new InfoObject( childString );
            childInfoObject.addChildAllowDuplicates( grandChildInfoObject );

        // add to this class
        addChildAllowDuplicates( childInfoObject );
    }

    /*
     * Adds an InfoObject element as a child, ensuring that there are no duplicate children
     * (based on the InfoObject's id)
     */
    public void add( InfoObject infoObjectChild )
    {
        addChildAllowDuplicates( infoObjectChild );
    }
    public void addChild( InfoObject infoObjectChild ) {
    
        if( infoObjectChild == null )
            log("You're trying to add a null child to parent with id: " + getId() );

        boolean childIdAlreadyExists = false;
        int indexOfElementInChildrenVectorWhichMatchesTheNewElementsId = -1;
    
        // scan the list of children
        for( int i=0; i < childrenVector.size(); i++ ) {

            // if the new child's id matches a child element already in the array, don't add it
            if( ((InfoObject)childrenVector.elementAt( i )).matchesId( infoObjectChild.getId() ) )
            {
                childIdAlreadyExists = true;
                indexOfElementInChildrenVectorWhichMatchesTheNewElementsId = i;
            }

            if( childIdAlreadyExists )
                break;
        }
        
        // the new child's id doesn't match any existing child's id, so we add it to the array
        if( ! childIdAlreadyExists )
            childrenVector.add( infoObjectChild );
        else {
            // The child already exists, so let's grab the child's kids, and add them to the already-existing child
            
            // pop off the element with the matching child id
            InfoObject matchesChildIdInfoObject = (InfoObject)childrenVector.remove( indexOfElementInChildrenVectorWhichMatchesTheNewElementsId );
            
            // get the new children that need to be added
            Vector newObjectChildrenVector = infoObjectChild.getChildrenVector();
            
            // insert these new children into the element that we popped off our children list
            for( int i = 0; i < newObjectChildrenVector.size(); i++ ) {
                InfoObject kidInfoObject = (InfoObject)newObjectChildrenVector.elementAt(i);
                matchesChildIdInfoObject.addChild( kidInfoObject );
            }
            
            // add the modified object (with the new kids) back to our children list
            childrenVector.add( matchesChildIdInfoObject );
        }
    }
    // *********************************************************************************************************
    // returns the value of the id of the first child of this object!
    // *********************************************************************************************************
    public String getValue()
    {
        return getFirstChild().getId();
    }
    // *********************************************************************************************************
    // Check if the id of the current object matches the argument
    // *********************************************************************************************************
    public boolean matchesId( String idToCompare ) {
        if( id.compareTo( idToCompare ) == 0 ) 
            return true;
        
        return false;
    }
    public boolean matchesIdIgnoreCase( String idToCompare ) {
        if( id.compareToIgnoreCase( idToCompare ) == 0 ) 
            return true;
        
        return false;
    }

    public boolean matchesIdLevel1Level2( String level1Id, String level2Id ) {
        boolean returnValue = false;
        if( ! matchesId( level1Id ) )
            return false;
            
        // check all children's ids
        for( int i = 0; i < childrenVector.size(); i++ ) {
            InfoObject level2Object = (InfoObject)childrenVector.elementAt(i);

            if( level2Object.matchesId( level2Id ))
                returnValue = true;
                
            if( returnValue == true ) break;
        }

        return returnValue;
    }

    public boolean matchesIdLevel1Level2Level3( String level1Id, String level2Id, String level3Id ) {
        boolean returnValue = false;
        if( ! matchesId( level1Id ) )
            return false;
            
        // check all children's ids
        for( int i = 0; i < childrenVector.size(); i++ ) {
            InfoObject level2Object = (InfoObject)childrenVector.elementAt(i);

            if( level2Object.matchesId( level2Id )) {
                Vector level2ChildrenVector = level2Object.getChildrenVector();
                for( int j = 0; j < level2ChildrenVector.size(); j++ ) {
                    InfoObject level3Object = (InfoObject)level2ChildrenVector.elementAt(j);
                    if( level3Object.matchesId( level3Id ) )
                        returnValue = true;

                    if( returnValue == true ) break;
                }
                
                if( returnValue == true ) break;
            }
            
            if( returnValue == true ) break;
        }

        return returnValue;
    }


    public boolean matchesIdLevel1Level2Level3Level4( String level1Id, String level2Id, String level3Id, String level4Id ) {
        boolean returnValue = false;
        if( ! matchesId( level1Id ) )
            return false;
            
        // check all children's ids
        for( int i = 0; i < childrenVector.size(); i++ ) {
            InfoObject level2Object = (InfoObject)childrenVector.elementAt(i);

            if( level2Object.matchesId( level2Id )) {
                Vector level2ChildrenVector = level2Object.getChildrenVector();

                for( int j = 0; j < level2ChildrenVector.size(); j++ ) {
                    InfoObject level3Object = (InfoObject)level2ChildrenVector.elementAt(j);
                    if( level3Object.matchesId( level3Id ) ) {
                        Vector level3ChildrenVector = level3Object.getChildrenVector();
                        
                        for( int k=0; k < level3ChildrenVector.size(); k++ ) {
                            InfoObject level4Object = (InfoObject)level3ChildrenVector.elementAt(k);
                            
                            if( level4Object.matchesId(level4Id))
                                returnValue = true;
                                
                            if( returnValue == true ) break;
                        }
                    }

                    if( returnValue == true ) break;
                }
                
                if( returnValue == true ) break;
            }
            
            if( returnValue == true ) break;
        }

        return returnValue;
    }

    public boolean hasMoreChildren() {
        return hasChildren();
    }

    public boolean hasChildren() {
        if( childrenVector.size() > 0 )
            return true;
        return false;
    }

    private void log( String s ) {
        System.out.println( "[InfoObject]:" + s);
    }
    
    /*
     * Returns the number of child levels that are available
     */
    public int getTotalLevels() {
        if( childrenVector.size() == 0 )
            return 1;
        else {
                int totalSubLevels = 0;
                // Scan all the children and get their levels
                for( int i = 0; i < childrenVector.size(); i++ ) {
                    InfoObject childObject = (InfoObject)childrenVector.elementAt(i);
                    
                    int childNumberOfLevels = childObject.getTotalLevels();
                    
                    // if a child object has more levels than we counted, make this the max
                    if( childNumberOfLevels > totalSubLevels )
                        totalSubLevels = childNumberOfLevels;
                }
                
                return 1 + totalSubLevels;
            }
    }

    private String getHtmlString() 
    {
        StringBuffer returnStr = new StringBuffer("");
        String eol = System.getProperty("line.separator");
        //        returnStr.append( "<blockquote><fieldset>" + eol );
        returnStr.append( "<fieldset>" + eol );
        returnStr.append( "<legend><b>" + getId() + "</b></legend>" + eol );
        // ******************************************************************************
        if( attributesVector.size() > 0 )
        {
            returnStr.append( "<br><font size=\"-1\">Attributes:</font><br>" + eol );
            for( int i = 0; i < attributesVector.size(); i++ )
            {
                InfoObject childObject = (InfoObject)attributesVector.elementAt(i);
                returnStr.append( childObject.getHtmlString() );
                childObject = null;
            }
        }
        // ******************************************************************************
        if( childrenVector.size() > 0 )
            returnStr.append( "<br><font size=\"-1\">Children:</font><br>" + eol );

        // scan all children
        for( int i = 0; i < childrenVector.size(); i++ )
        {
            InfoObject childObject = (InfoObject)childrenVector.elementAt( i );
            returnStr.append( childObject.getHtmlString() );
            childObject = null;
        }
        //        returnStr.append( "</fieldset></blockquote>" + eol );
        returnStr.append( "</fieldset>" + eol );
        
        return returnStr.toString();
    }
    
    public String getString() {
        return getString( "\t" );
    }
    
    private String getString( String optionalPrefixString ) {

        StringBuffer returnStr = new StringBuffer("");
        String eol = System.getProperty("line.separator");
        
        returnStr.append( optionalPrefixString + "id:" + getId() + eol );

        if( childrenVector.size() > 0 )
            returnStr.append( optionalPrefixString + "children:" + eol );

        // scan all children
        for( int i = 0; i < childrenVector.size(); i++ ) {
            InfoObject childObject = (InfoObject)childrenVector.elementAt( i );
            returnStr.append( childObject.getString( optionalPrefixString + optionalPrefixString ));
        }
        
        return returnStr.toString();
    }

    public void writeHtmlToFile( String fileName ) 
    {
        System.out.println("Starting to write infoObject to file: " + fileName );
        try {        
            BufferedOutputStream bos = new BufferedOutputStream( 
                        new FileOutputStream( new File( fileName )));
            bos.write( getHtmlString().getBytes() );
            bos.close();
        } catch( Exception e ) {
            System.out.println("Exception in InfoObject.writeHtmlToFile(" + fileName + ") is: " + e.toString() );
            e.printStackTrace();
        }
        System.out.println("Finished writing infoObject to file: " + fileName );
    }
    // ************************************************************************************************************
    public boolean isLeafNode()
    {
        if( childrenVector.size() == 0 && attributesVector.size() == 0 )
            return true;
        return false;
    }
    // ************************************************************************************************************
     public void writeXMLToFile( String fileName ) 
     {
         System.out.println("Starting to write infoObject to XML file: " + fileName );
         try {        
             BufferedOutputStream bos = new BufferedOutputStream( 
                         new FileOutputStream( new File( fileName )));
             bos.write( getXMLString().getBytes() );
             bos.close();
         } catch( Exception e ) {
             System.out.println("Exception in InfoObject.writeXMLToFile(" + fileName + ") is: " + e.toString() );
             e.printStackTrace();
         }
         System.out.println("Finished writing infoObject to XML file: " + fileName );
     }
     public String getXMLString() 
     {
        if( isLeafNode() )
            return getId();
     
         StringBuffer returnStr = new StringBuffer("");
         String eol = System.getProperty("line.separator");
         returnStr.append( "<" + getId() );
         // ******************************************************************************
         if( attributesVector.size() > 0 )
         {
             InfoObject childObject = null;            
             for( int i = 0; i < attributesVector.size(); i++ )
             {
                 childObject = (InfoObject)attributesVector.elementAt(i);
                 returnStr.append( " " + childObject.getId() + "=\"" + childObject.getFirstChild().getId() + "\"" );
             }
         }
         // if there are NO children objects, then add a self closing tag
         if( childrenVector.size() == 0 )
            returnStr.append("/>" + eol );
         else
            returnStr.append(">");
            
         // ******************************************************************************
         // scan all children
         for( int i = 0; i < childrenVector.size(); i++ )
         {
             InfoObject childObject = (InfoObject)childrenVector.elementAt( i );
             returnStr.append( childObject.getXMLString() );
             childObject = null;
         }

        if( childrenVector.size() > 0 )
            returnStr.append( "</" + getId() + ">" + eol ); // closing tag
         
         return returnStr.toString();
     }

    /**
     * Returns a friendly, easily walkable InfoObject
     * 
     * Converts a pure resultset InfoObject like this:
     * 
     * <0>
     * <Cell>
     *      <ColumnName>BUSINESS_NAME</ColumnName>
     *      <ColumnType>12</ColumnType>
     *      <Value>Westshore Landings One</Value>
     * </Cell>
     * <Cell>
     *      <ColumnName>ADDRESS1</ColumnName>
     *      <ColumnType>12</ColumnType>
     *      <Value>Vicinity of 1522 North Clark Avenue</Value>
     * </Cell>
     * ...
     * </0>
     * <1> ... </1> etc
     * 
     * into a friendlier, walkable InfoObject, such as this:
     * 
     *  <QueryResults>
     *      <0>
     *          <MENU_BLOCK>PRR</MENU_BLOCK>
     *          <MENU_GROUP>Request Management</MENU_GROUP>
     *          <MENU_ITEM>Entry</MENU_ITEM>
     *          <MENU_URL>requests.do</MENU_URL>
     *          <MENU_TARGET>Portal</MENU_TARGET>
     *          <DISPLAY_ORDER>20</DISPLAY_ORDER>
     *      </0>
     *      ... etc
     * 
     * Access data as such:
     * for( int i = 0; i < friendlyInfoObject.numChildren(); i++ )
     * {
     *      String block = friendlyInfoObject.get("QueryResults/" + i + "/MENU_BLOCK");
     *      String group = friendlyInfoObject.get("QueryResults/" + i + "/MENU_GROUP");
     *      String item = friendlyInfoObject.get("QueryResults/" + i + "/MENU_ITEM");
     *      ...
     *      etc
     *  }
     * 
     * @param resultSetInfoObject the InfoObject resulting from a query
     * @return an InfoObject, easily accessible in Xpath fassion
     */
    public static InfoObject getXpathAccessibleInfoObject( InfoObject resultSetInfoObject )
    {
        InfoObject o = new InfoObject("QueryResults");
        Vector allRowsVector = resultSetInfoObject.getChildrenVector();
        for( int i = 0; i < allRowsVector.size(); i++ )
        {
            InfoObject rowIo = (InfoObject)allRowsVector.elementAt(i);
            String rowIndex = rowIo.getId(); // ex: "0", or "9"

            // if it's a number, do the rest
            boolean isNumber = true;
            try 
            {
                Integer.parseInt( rowIndex );
            } catch(Exception e)
            {
                // it's not a number
                isNumber = false;
            }

            if( isNumber )
            {
                InfoObject finalRowInfoObject = new InfoObject( rowIndex ); // ex: "0", or "8"
                
                Vector rowOfCellsVector = rowIo.getChildrenVector();
                for( int j = 0; j < rowOfCellsVector.size(); j++ )
                {
                    InfoObject cell = (InfoObject)rowOfCellsVector.elementAt(j);
                    
                    // read each column name
                    String columnName = cell.get("ColumnName");
                    String cellValue = cell.get("Value");
                    finalRowInfoObject.add(columnName, cellValue);
                }
                
                o.add( finalRowInfoObject );
            }
        }// end of for loop
    return o;
    } // end of function


} // end class
