
package ru.dpd.ws.geography._2015_05_20;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getCitiesCashPay complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="getCitiesCashPay">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="request" type="{http://dpd.ru/ws/geography/2015-05-20}dpdCitiesCashPayRequest" minOccurs="0"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getCitiesCashPay", propOrder = {
    "request"
})
public class GetCitiesCashPay {

    protected DpdCitiesCashPayRequest request;

    /**
     * Gets the value of the request property.
     * 
     * @return
     *     possible object is
     *     {@link DpdCitiesCashPayRequest }
     *     
     */
    public DpdCitiesCashPayRequest getRequest() {
        return request;
    }

    /**
     * Sets the value of the request property.
     * 
     * @param value
     *     allowed object is
     *     {@link DpdCitiesCashPayRequest }
     *     
     */
    public void setRequest(DpdCitiesCashPayRequest value) {
        this.request = value;
    }

}
