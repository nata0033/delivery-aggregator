
package ru.dpd.ws.calculator._2012_03_20;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getServiceCostByParcels3 complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="getServiceCostByParcels3">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="request" type="{http://dpd.ru/ws/calculator/2012-03-20}serviceCostParcelsWithExtraServicesRequest" minOccurs="0"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getServiceCostByParcels3", propOrder = {
    "request"
})
public class GetServiceCostByParcels3 {

    protected ServiceCostParcelsWithExtraServicesRequest request;

    /**
     * Gets the value of the request property.
     * 
     * @return
     *     possible object is
     *     {@link ServiceCostParcelsWithExtraServicesRequest }
     *     
     */
    public ServiceCostParcelsWithExtraServicesRequest getRequest() {
        return request;
    }

    /**
     * Sets the value of the request property.
     * 
     * @param value
     *     allowed object is
     *     {@link ServiceCostParcelsWithExtraServicesRequest }
     *     
     */
    public void setRequest(ServiceCostParcelsWithExtraServicesRequest value) {
        this.request = value;
    }

}
