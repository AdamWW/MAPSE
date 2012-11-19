 /* Copyright (c) 2009 Alan Lupsha
  * Please do not redistribute this class without prior consent.
 */
 /*
 * Pass in the directory/file path to an xml file, returns an InfoObject populated with the xml data
 */

package com.digitalagora.utilities;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.StringTokenizer;
import java.util.Vector;

public class XMLToInfoObjectLoader
{
    private String xmlFilePath = "";
    private StringBuffer rawXmlStringBuffer = new StringBuffer("");

    public XMLToInfoObjectLoader()
    {
        
    }

    public void setXmlContent( StringBuffer s )
    {
        this.rawXmlStringBuffer = s;
    }

    /**
     * @param xmlFilePath
     */
    public XMLToInfoObjectLoader( String xmlFilePath )
    {
        this.xmlFilePath = xmlFilePath;
    }

    /**
     * @return InfoObject
     */
    public InfoObject xmlToInfoObject()
    {
        StringBuffer rawXml = null;
        if( xmlFilePath.equals(""))
        {
            rawXml = rawXmlStringBuffer;
        }
        else
        {
            rawXml = loadXml( xmlFilePath );
        }

        // remove all unneeded xml whitespaces between tags only
        rawXml = removeXMLQuestionTags( rawXml );
        rawXml = removeXMLComments( rawXml );
        rawXml = removeXMLWhitespaces( rawXml );

        Vector infoObjectVector = createInfoObjectFromXML( rawXml );
        if( infoObjectVector.size() == 1 )
            return (InfoObject)infoObjectVector.elementAt(0);
        else
        {
            InfoObject returnInfoObject = new InfoObject();
            for( int i = 0; i < infoObjectVector.size(); i++ )
                returnInfoObject.addChildAllowDuplicates( (InfoObject)infoObjectVector.elementAt(i) );

            return returnInfoObject;
        }
        // xmlInfoObject.writeHtmlToFile( xmlFilePath + "-InfoObject.html");
    }

    private StringBuffer loadXml( String xmlFilePath )
    {
        StringBuffer xml = getFileContents( xmlFilePath );
        xml = removeEndOfLine( xml );
        xml = removeQuestionmarkTags( xml );
        return xml;
    }
    
    private Vector createInfoObjectFromXML( StringBuffer xml )
    {
        Vector itemInfoObjectVector = new Vector();
        InfoObject itemInfoObject = null;

        int startTag = -1;
        int endTag = -1;
        int selfEndTag = -1;
        String tagName = "";

        startTag = xml.indexOf("<");
        if( startTag != -1 ) // found start tag
        {
            // check if this is a self ending tag
            // ex: <Biking Name="abc"/><Next tag...
            
            int nextTagStart = xml.indexOf("<", startTag + 1 );
            if( nextTagStart != -1 ) // we actually found a next tag after this, ex: <Next...
            {
                // ex: value becomes: <Biking Name="abc"/>
                String fromStartToNextTag = xml.substring( startTag, xml.indexOf("<", (startTag + 1) ));
          
                selfEndTag = fromStartToNextTag.indexOf( "/>");
            }
            else
            {
                // check if this is the very last ending tag, i.e. our xml ends with this tag's self ending bracket />
                if( xml.substring( xml.length()-2, xml.length() ).equals("/>"))
                    selfEndTag = xml.length() - 2;
            }
            
            // if we have a self ending tag, ex: <Biking Name="abc"/>
            if( selfEndTag != -1 )
            {
                tagName = xml.substring( startTag + 1 , selfEndTag );
                itemInfoObject = createInfoObject( tagName );
                itemInfoObjectVector.add( itemInfoObject );
                
                StringBuffer remainingXml = new StringBuffer("");
                if( selfEndTag + 2 < xml.length() )
                {
                    remainingXml = new StringBuffer(
                                    xml.substring( selfEndTag + 2, xml.length() )
                                    );
                                    
                    // if we have remaining xml, keep the recursion going
                    if( remainingXml.length() > 0 )
                    {
                        Vector w = createInfoObjectFromXML( remainingXml );
                        if( w.size() == 1 )
                            itemInfoObjectVector.add( (InfoObject)w.elementAt(0) );
                        else
                        {
                            for( int i = 0; i < w.size(); i++ )
                                itemInfoObjectVector.add( (InfoObject)w.elementAt(i) );
                        }
                    }

                }
            }
            else // this is not a self ending tag
            {
                endTag = xml.indexOf(">", startTag ); // look for closing >
                if( endTag != -1 ) // found end tag
                {
                    // ex: infoObject for: solarSystem name="abc" size="9"
                    itemInfoObject = createInfoObject( xml.substring(startTag + 1, endTag) );
    
                    // ***************************************************************
                    // reduce the xml, removing the start and end tag (ex: remove <solarSystem..> and </solarSystem>)
                    // ***************************************************************
    
                    // save the tag name, ex: solarSystem name="abc" size="9"
                    String wholeTag = xml.substring(startTag + 1, endTag);
                    
                    // ex: solarSystem
                    tagName = getJustTagName( wholeTag );
                    
                    // find the location of the closing tagName, re-use variable endTag
                    endTag = xml.indexOf("</" + tagName + ">");
    
                    // if we actually found the closing tag, ex: </solarSystem>
                    if( endTag != -1 )
                    {
                        StringBuffer remainingXml = new StringBuffer("");
                        
                        // if we didn't exhaust the whole XML, i.e. there are many <planet> tags following
                        if( (endTag + 2 + tagName.length() + 1 ) < xml.length() )
                        {
                            remainingXml = new StringBuffer( 
                                            xml.substring( (endTag + 2 + tagName.length() + 1), xml.length() )
                                            );
                        }
                        // cut down on the xml
                        xml = new StringBuffer(
                                            xml.substring( 
                                                (startTag + 1 + wholeTag.length() + 1),
                                                endTag
                                            ));
    
                        Vector v = createInfoObjectFromXML( xml );
                        if( v.size() == 1 )
                            itemInfoObject.addChildAllowDuplicates( (InfoObject)v.elementAt(0) );
                        else
                        {
                            for( int i = 0; i < v.size(); i++ )
                                itemInfoObject.addChildAllowDuplicates( (InfoObject)v.elementAt(i) );
                        }
                        
                        itemInfoObjectVector.add( itemInfoObject );
                        
                        // if we have remaining xml, keep the recursion going
                        if( remainingXml.length() > 0 )
                        {
                            Vector w = createInfoObjectFromXML( remainingXml );
                            if( w.size() == 1 )
                                itemInfoObjectVector.add( (InfoObject)w.elementAt(0) );
                            else
                            {
                                for( int i = 0; i < w.size(); i++ )
                                    itemInfoObjectVector.add( (InfoObject)w.elementAt(i) );
                            }
                        }
                        
                    }

                } // end of: if( endTag != -1 ) // found end tag
                else 
                {
                    log("Error: could not find end tag for for tag: " + tagName + " so as far as I know, you have erroneous xml. Fix it.");
                }

            } // end of: else // this is not a self ending tag

        } // end of: if( startTag != -1 ) // found start tag
        else
        {
            // couldn't find a <, therefore this is pure data
            itemInfoObject = new InfoObject( xml.toString() );
            itemInfoObjectVector.add( itemInfoObject );
        }
        return itemInfoObjectVector;
    }
    
    // ****************************************************************************
    // Pass in a string, such as:
    // Example: product description="Cardigan Sweater" product_image="cardigan.jpg"
    // return an infoObject with attributes
    // ****************************************************************************
    private InfoObject createInfoObject( String tag )
    {
        tag = replaceTabWithSpace( tag );
            
        // System.out.println("Now doing tag: =>" + tag + "<=");
    
        InfoObject rio = null;
        
        // check if the tag has spaces, and thus attributes
        if( tag.indexOf(" ") >= 0 )
        {
            // ex: product description="Cardigan Sweater" product_image="cardigan.jpg"
        
            // Yes, we have attributes
            String attributeName;
            String attributeValue;
            
            // ex: product
            String s = tag.substring(0, tag.indexOf(" "));
            rio = new InfoObject(s); // the id, ex: solarSystem

            // ex: description="Cardigan Sweater" product_image="cardigan.jpg"
            tag = tag.substring( s.length() + 1, tag.length() );
            
            while( tag.length() > 0 )
            {
                // if we start with a space, remove it
                while( tag.charAt(0) == ' ') 
                {
                    tag = tag.substring( 1, tag.length() );
                    if( tag.length() == 0 ) break;
                }
                if( tag.length() == 0 ) break;
                
                int firstQuoteLocation = tag.indexOf("\"");
                String g = tag.substring(firstQuoteLocation + 1, tag.length());
                int secondQuoteLocation = firstQuoteLocation + 1 + g.indexOf("\"");

                // if we actually found an attribute, such as: description="Cardigan Sweater"
                if( firstQuoteLocation != -1 && secondQuoteLocation != -1 && firstQuoteLocation < secondQuoteLocation )
                {
                    // ex: description
                    attributeName = tag.substring( 0, firstQuoteLocation-1 );

                    // ex: Cardigan Sweater
                    attributeValue = tag.substring( firstQuoteLocation + 1, secondQuoteLocation );

                    InfoObject attributeInfoObject = new InfoObject( attributeName );
                    attributeInfoObject.addChild( new InfoObject(attributeValue));
                    rio.addAttribute( attributeInfoObject );
                    
                    tag = tag.substring( secondQuoteLocation + 1, tag.length() );
                }
                else
                {
                    log("Error: while creating info object for tag =>" + tag + "<= couldn't find quoted attributes.");
                    rio = new InfoObject("error");
                    //System.exit(-1);
                }
            }
           
        }
        else
        {
            // No, we dont' have attributes
            rio = new InfoObject(tag);
        }
        
        return rio;
    }
    // ****************************************************************************
    private String replaceTabWithSpace( String s )
    {
        return s.replace('\t', ' ');
    }
    // ****************************************************************************
    private String getJustTagName( String tag )
    {
        String returnStr = new String("");
        
        if( tag.indexOf(" ") >= 0 )
        {
            // Yes, we have attributes
            StringTokenizer st = new StringTokenizer( tag, " ");
            returnStr = new String( st.nextToken() ); // skip the first token, i.e. "solarSystem"
        }
        else
        {
            // No, we dont' have attributes
            returnStr = new String( tag );
        }
        return returnStr;
    }
    // ****************************************************************************
    private StringBuffer removeQuestionmarkTags( StringBuffer s )
    {
        if( s.indexOf("<?") == -1 )
            return s;
            
        StringBuffer r = new StringBuffer("");
        int start = s.indexOf("<?");
        int end = s.indexOf("?>", start );
        if( start != -1 && end != -1 && start < end )
            r = new StringBuffer( s.substring( end + 2, s.length() ) );
        return r;
    }
    // ****************************************************************************
    private StringBuffer removeEndOfLine( StringBuffer s )
    {
        StringBuffer r = new StringBuffer("");
        StringTokenizer st = new StringTokenizer( s.toString(), "\r\n" );
        while( st.hasMoreTokens() )
        {
            r.append( st.nextToken() );
        }
        return r;
    }
    // ****************************************************************************
    private StringBuffer removeXMLWhitespaces( StringBuffer s )
    {
        StringBuffer result = new StringBuffer("");

        /* example:

         <solarSystem name="abc" size="9">
             <sun color="red" shape="oval">bright</sun>
             <planets>
                 <planet>
                     <name>Mer
                            cury</name>
                     <mass>123 1241</mass>
                 </planet>
            </planets>
        </solarSystem>
        
        becomes:

         <solarSystem name="abc" size="9"><sun color="red" shape="oval">bright</sun><planets><planet><name>Mer                            
                    cury</name><mass>123 1241</mass></planet></planets></solarSystem>
        
        */
        char ch;
        boolean isInsideTag = false;
        StringBuffer contentOutsideTag = new StringBuffer("");
        for( int i = 0; i < s.length(); i++ )
        {
            ch = s.charAt(i);
            
            if( ch == '<' )
            {
                isInsideTag = true;
            }
            if( ch == '>')
            {
                isInsideTag = false;
                // don't forget to add the closing tag
                result.append( ch );
                i++;
                if( i < s.length() )
                    ch = s.charAt(i);
            }
            
            if( ! isInsideTag ) // if it's outside of a tag
            {
                contentOutsideTag = new StringBuffer("");
            
                // find if the next character is < or actual character data
                while( ch != '<' && i < s.length() )
                {
                    contentOutsideTag.append( ch );
                    
                    i++;
                    if( i < s.length() )
                        ch = s.charAt(i);
                }
                
                // if it's NOT all whitespace, append it
                if( ! isXmlWhitespace( contentOutsideTag ))
                    result.append( contentOutsideTag );

                // found another tag start
                if( ch == '<' )
                {
                    isInsideTag = true;
                    result.append( ch ); // add the tag start
                }
            }
            else
            {
                result.append( ch );
            }
        }
        return result;
    }
    // ****************************************************************************
    public StringBuffer getFileContents( String xmlFilePath )     
    {
        StringBuffer stringBuffer = new StringBuffer("");
        BufferedInputStream bis = null;
        try
        {
            File f = new File( xmlFilePath );
            if( ! f.exists() )
            {
                log("Error: File does not exist: " + f.getAbsolutePath() );
            }
            else
            {
                bis = new BufferedInputStream( new FileInputStream( f ) );
                
                byte b[] = new byte[ 8192 ];
                
                int bytesRead = 1;
                while( (bytesRead = bis.read( b )) > 0 )
                {
                    stringBuffer.append( new String( b, 0, bytesRead ) );
                }
            }            
        } catch(Exception e)
        {
            log("Error while creating bufferedInputStream");
        }
        
        return stringBuffer;
    }

    public static void main( String[] args )
    {
        XMLToInfoObjectLoader xMLToInfoObjectLoader = new XMLToInfoObjectLoader( "example.xml" );
    }

    private void log(String s)
    {
        System.out.println(s);
    }
    // tell me if a string has only spaces, tabs, or enters
    private boolean isXmlWhitespace( StringBuffer s )
    {
        if( s == null )
            return false;
        if( s.length() == 0 )
            return false;
    
        if( s.toString().trim().length() == 0 )
            return true; // it's whitespace
            
        return false;
    }

    private StringBuffer removeXMLComments( StringBuffer s )
    {
        StringBuffer r = new StringBuffer("");
        
        if( s.indexOf( "<!--") == -1 )
            return s;
        
        int start = -1;
        int end = -1;
        
        start = s.indexOf("<!--");
        end = s.indexOf("-->", start);
        
        if( start != -1 && end != -1 && start < end )
        {
            r.append( s.substring(0, start ) );
            r.append( removeXMLComments( new StringBuffer( s.substring( end + 3, s.length() ) ) ) );
        }
        else
        {
            log("removeXMLComments: There must be some error in the xml, can't find start comment and end comment");
        }
        return r;
    }

    private StringBuffer removeXMLQuestionTags( StringBuffer s )
    {
        StringBuffer r = new StringBuffer("");
        
        if( s.indexOf( "<?") == -1 )
            return s;
        
        int start = -1;
        int end = -1;
        
        start = s.indexOf("<?");
        end = s.indexOf("?>", start);
        
        if( start != -1 && end != -1 && start < end )
        {
            r.append( s.substring(0, start ) );
            r.append( removeXMLQuestionTags( new StringBuffer( s.substring( end + 2, s.length() ) ) ) );
        }
        else
        {
            log("removeXMLQuestionTags: There must be some error in the xml, can't find start comment <? and end comment ?>");
        }
        return r;
    }

}// end class
