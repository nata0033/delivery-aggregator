
package ru.dpd.ws.geography._2015_05_20;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getTerminalsSelfDelivery2Response complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="getTerminalsSelfDelivery2Response">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="return" type="{http://dpd.ru/ws/geography/2015-05-20}selfTerminals" minOccurs="0"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getTerminalsSelfDelivery2Response", propOrder = {
    "_return"
})
public class GetTerminalsSelfDelivery2Response {

    @XmlElement(name = "return")
    protected SelfTerminals _return;

    /**
     * Gets the value of the return property.
     * 
     * @return
     *     possible object is
     *     {@link SelfTerminals }
     *     
     */
    public SelfTerminals getReturn() {
        return _return;
    }

    /**
     * Sets the value of the return property.
     * 
     * @param value
     *     allowed object is
     *     {@link SelfTerminals }
     *     
     */
    public void setReturn(SelfTerminals value) {
        this._return = value;
    }

}
