package ws.rs;

import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.abdera.i18n.text.CharUtils.Profile;


  @XmlRootElement
  @XmlType(propOrder = {"application", "version" , "feedId", "lastmodified", "item", "collection" })
  public class Response {
      final String application = "myApp";
      final String version = "v1";
    
      String feedId;
    
      Date lastmodified = GregorianCalendar.getInstance(new Locale("nl")).getTime();
    
      Object item;
      Collection collection;
    
      public Response() { }
    
      private Response(String feedId) { 
          this.feedId = feedId;
      }
    
      public Response(String feedId, Collection collection) {
          this(feedId);
          this.collection = collection;
      }
    
      public Response(String feedId, Object object) {        
          this(feedId);
          this.item = object;
      }
    
      @XmlAttribute
      public String getApplication() {
          return application;
      }
  
      @XmlAttribute 
      public String getVersion() {
          return version;
      }
    
      @XmlAttribute 
      public String getFeedId() {
          return feedId;
      }
      public void setFeedId(String feedId) {
          this.feedId = feedId;
      }
    
      @XmlAttribute 
      public Date getLastmodified() {
          return lastmodified;
      }
      public void setLastmodified(Date lastmodified) {
         this.lastmodified = lastmodified;
      }
    
      @XmlElement
      public Collection getCollection() {
          return collection;
      }  
   
      public void setCollection(Collection collection) {
          this.collection = collection;
      }
      @XmlElements({
         @XmlElement(name="profile",type=Profile.class)
      })
      public Object getItem() {
          return item;
      }
      public void setItem(Object item) {
          this.item = item;
      }
  }
