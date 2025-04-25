
package ru.dpd.ws.calculator._2012_03_20;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for auth complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="auth">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="clientNumber" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         <element name="clientKey" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "auth", propOrder = {
    "clientNumber",
    "clientKey"
})
public class Auth {

    protected long clientNumber;
    @XmlElement(required = true)
    protected String clientKey;

    /**
     * Gets the value of the clientNumber property.
     * 
     */
    public long getClientNumber() {
        return clientNumber;
    }

    /**
     * Sets the value of the clientNumber property.
     * 
     */
    public void setClientNumber(long value) {
        this.clientNumber = value;
    }

    /**
     * Gets the value of the clientKey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClientKey() {
        return clientKey;
    }

    /**
     * Sets the value of the clientKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClientKey(String value) {
        this.clientKey = value;
    }

}
