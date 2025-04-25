
package ru.dpd.ws.calculator._2012_03_20;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getServiceCostByParcels2 complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="getServiceCostByParcels2">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="request" type="{http://dpd.ru/ws/calculator/2012-03-20}serviceCostParcelsRequest" minOccurs="0"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getServiceCostByParcels2", propOrder = {
    "request"
})
public class GetServiceCostByParcels2 {

    protected ServiceCostParcelsRequest request;

    /**
     * Gets the value of the request property.
     * 
     * @return
     *     possible object is
     *     {@link ServiceCostParcelsRequest }
     *     
     */
    public ServiceCostParcelsRequest getRequest() {
        return request;
    }

    /**
     * Sets the value of the request property.
     * 
     * @param value
     *     allowed object is
     *     {@link ServiceCostParcelsRequest }
     *     
     */
    public void setRequest(ServiceCostParcelsRequest value) {
        this.request = value;
    }

}
