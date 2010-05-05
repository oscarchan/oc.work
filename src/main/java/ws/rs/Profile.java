package ws.rs;

  import java.awt.Image;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
  @XmlType(propOrder = {"number", "name", "unrestricted", "text", "image" })
  public class Profile {
      String number;
    
      String name;
      String text;
      String image;
    
      boolean unrestricted;
    
      public Profile() { }
  
      public Profile(String number, String name, String text, String image, boolean unrestricted) {        
          this.name = name;
          this.text = text;
          this.image = image;
          this.unrestricted = unrestricted;
      }
     
      @XmlAttribute
      public String getName() {
          return name;
      }
      public void setName(String name) {
          this.name = name;
      }
    
      @XmlElement(nillable = true, required= false)
      public String getText() {
          return text;
      }
      public void setText(String text) {
          this.text = text;
      }
      
      @XmlElement(nillable = true, required= false)
      public String getImage() {
          return image;
      }
      public void setImage(String image) {
          this.image = image;
      }
     
      @XmlAttribute
      public boolean isUnrestricted() {
          return unrestricted;
      }
      public void setUnrestricted(boolean unrestricted) {
          this.unrestricted = unrestricted;
      }
    
      @XmlAttribute
      public String getNumber() {
          return number;
      }
      public void setNumber(String number) {
         this.number = number;
      }
  }
